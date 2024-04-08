package com.example.essencialEfects.chapter10

import cats.effect._
import com.example.essencialEfects.chapter10.JobScheduler.State
import Job.Id

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    ???
  }

  //ESTO ES SOLO UN EJEMPLO:
  /*
  def scheduled(stateRef:Ref[IO, State], zzz: Zzz):JobScheduler = {
    // El estado del Ref  se encapsula en una ref para que sea modificado de forma atomica
    new JobScheduler { //TODO: ¿Este estado es global de toda la aplicacion o solo afecta a 1 o varios job? Si es lo primero (COmo parece) ¿como se añaden mas tareas?
      def schedule(task: IO[_]): IO[Id] = for //TODO: Solo una tarea? es la de inicio?
      {
        job <- Job.create(task)
        _ <-  stateRef.update(_.enqueue(job))
        _ <-  zzz.wakeUp
      } yield job.id
    }
  }

    def reactor(schedulerState: Ref[IO, State], zzz: Zzz): Reactor = new Reactor (
      schedulerState,
      onStart = ???,
      onComplete = (id, exitCase) => zzz.wakeUp
    )

    def loop(zzz: Zzz, reactor: Reactor): IO[Nothing] = (zzz.sleep *> reactor.whenAwake(onStart, onComplete))
      .foreverM*/
}
