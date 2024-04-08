package com.example.essencialEfects.chapter10

import cats.effect._
import cats.implicits._
import com.example.essencialEfects.chapter10.Job._
import com.example.essencialEfects.chapter10.JobScheduler.State

/*
 * La ejecucion(De scheduler a running) Va en una fiber dentro de Scheduled.start -> Reactor.apply: Fiber[IO, Throwable, Unit]
 * El estado en un REF. Reactor.apply: Ref[IO, State]
 * El resultado del job es un Deferred: Deferred[IO, OutcomeIO[_]]. exitCase en la clase Job
 * La espera del complete se hace en otra fiber (linea 38) IO[FiberIO[Unit]] que es igual a Fiber[IO, Throwable, Unit]
 */

trait Reactor { // El reactor cuando se despierta, inicia todos los trabajos que puede.
  def whenAwake (
                  onStart: Id => IO[Unit],
                  onComplete:(Id, OutcomeIO[_]) => IO[Unit]
                ):IO[Unit]
}

object Reactor {
  def apply(stateRef: Ref[IO, State]): Reactor = { // El state se usa en este REF
    new Reactor {
      def whenAwake(onStart: Id => IO[Unit], //Callbacks para avisar de que un job empieza a los listeners
                    onComplete: (Id, OutcomeIO[_]) => IO[Unit] //Callbacks para avisar de que un job acaba a los listeners
                   ): IO[Unit] = {
        def jobCompleted(job:Completed):IO[Unit] = {
          stateRef
            .update(_.onComplete(job)) // actualizamos el state
            .flatTap(_ => onComplete(job.id, job.exitCase).attempt) // Notificamos
        }

        def registerOnComplete(job: Running):IO[FiberIO[Unit]] =
          job
            .await //pone el deferend en espera
            .flatMap(jobCompleted)
            .start // Esta fiber ESTARA ESPERANDO A QUE SE COMPLETE EL TRABAJO

        def startJob(scheduled: Scheduled): IO[Running] = { // Inicia un nuevo job
          for {
            running <- scheduled.start // difurcamos la ejecuion en una Fiber. // Esta fiber EJECUTA EL TRABAJO
            _ <- stateRef.update(_.running(running)) //añadimos el job a la lista de running
            _ <- registerOnComplete(running) // creamos una fiber (linea 38) para esperar a que se complete el job luego modifica el estado Complete y notifica a los listeners
            _ <- onStart(running.id) //invocamos a onStart para avisar a los listeners que el job ha comenzado
          } yield running
        }

        def startNewJob:IO[Option[Running]] = {
          for {
            job <- stateRef.modify(_.dequeue) //acabamos el primer job de la cola si hay recursos disponibles para ejecutarlo
            running <- job.traverse(startJob)  //from Option[Scheduled] to IO[Option[Running]]
          } yield running
        }

        startNewJob // Iniciamos un nuevo job IO[Option[Running]]
          .iterateUntil(_.isEmpty) // Ejecutamos hasta que no haya mas jobs
          .void
      }
    }
  }
}
