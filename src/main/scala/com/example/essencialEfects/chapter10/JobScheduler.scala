package com.example.essencialEfects.chapter10

import cats.data._
import cats.effect._
import cats.syntax.all._
import com.example.essencialEfects.chapter10.Job._

trait JobScheduler {
  def schedule(task: IO[_]):IO[Id]
}

object JobScheduler {
  case class State( //State sera manejado como un Ref[IO, State] ver main
                    maxRunning: Int,
                    scheduled: Chain[Scheduled] = Chain.empty,
                    running: Map[Id, Running] = Map.empty,
                    completed: Chain[Completed] = Chain.empty
                  ) {

    def enqueue(job: Scheduled): State =
      copy(scheduled = scheduled :+ job)

    def dequeue: (State, Option[Scheduled]) =
      if (running.size >= maxRunning) this -> None
      else {
        scheduled.uncons match {
          case Some((job, rest)) => (copy(scheduled = rest), Some(job))
          case None => (this, None)
        }
      }

    def running(job:Running):State =
      copy(running = running + (job.id -> job))

    def onComplete(job:Completed):State = //echo por github Copilot
      copy(
        running = running - job.id,
        completed = completed :+ job
      )
  }

  //EntryPoint
  def resource(maxRunning:Int):IO[Resource[IO, JobScheduler]] = {
    for {
      schedulerState <- Ref[IO].of(JobScheduler.State(maxRunning))
      zzz <- Zzz.asleep
      scheduler = new JobScheduler {
        def schedule(task: IO[_]): IO[Id] =
          for {
           job <- Job.create(task)
           _ <-  schedulerState.update(_.enqueue(job))
           _ <-  zzz.wakeUp
          } yield job.id
      }
      reactor = Reactor(schedulerState)
      onStart = ??? // dependen de una clase llamada JobReporting que no esta definida en el libro pero se puede ver en github
      onComplete = ???
      loop = (zzz.sleep *> reactor.whenAwake(onStart, onComplete))
        .foreverM
    } yield loop.background.as(scheduler)
  }
}
