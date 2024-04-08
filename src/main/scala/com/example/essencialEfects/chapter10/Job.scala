package com.example.essencialEfects.chapter10

import cats.effect._
import java.util.UUID

sealed trait Job
object Job {
  case class Scheduled(id:Id, task:IO[_]) extends Job {
    def start:IO[Running] = for {
      exitCase <- Deferred[IO, OutcomeIO[_]] // creamos el Deferred para almencear el resultado del job.
      // OutcomeIO (ExitCase) representa el resultado de evaluar la task. TODO: ¿Usariamos Outcome si no usaramos fiber? SI, es el equivalente a ExitCase
      fiber <- task.void // con .void se omite el resultado y de esa forma no nos improta el tipo de job que se ejecute (no quiere decir que no se ejecute el efecto)
        .guaranteeCase(exitCase.complete(_).void) // Se garantiza que se completara el Deferred sea cual el resultado tras evaluar el efecto task(error, cancel o succeeded).
        // NO TIENE NADA QUE VER con ninguna Fiber, el outcome (antes exit case) the finalizer (guaranteeCase(finalizer)) representa el como va a terminar el efecto
        //TODO:¿Cuando se realiza el complete? al finalizar el "join" del task? o al declararlo?(poco sentido)
        //Respuesta: Al finalizarlo, que el start este mas adelante no cambia nada
        .start //Separamos la fiber
    } yield Running(id, fiber, exitCase)
  }
  case class Running(
                      id:Id,
                      fiber: Fiber[IO, Throwable, Unit],
                      exitCase: Deferred[IO, OutcomeIO[_]]
                    ) extends Job {
    // falta el IO.join?
    val await:IO[Completed] = exitCase.get.map(Completed(id ,_)) //Ponemos el deferred en espera y lo cambiamos como Completed
  }
  case class Completed(id:Id, exitCase:OutcomeIO[_]) extends Job

  case class Id(id:UUID) extends AnyVal

  def create[A](task:IO[A]):IO[Scheduled] =
    IO(Id(UUID.randomUUID())).map(Scheduled(_, task))

}
