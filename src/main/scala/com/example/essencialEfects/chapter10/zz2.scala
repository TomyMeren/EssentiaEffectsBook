package com.example.essencialEfects.chapter10

import cats.effect._


// Implement Zzz as a concurrent state machine

trait Zzz2 {
  def sleep: IO[Unit]
  def wakeUp: IO[Unit]
}

object Zzz2 {
  def asleep: IO[Zzz2] = {
    sealed trait State
    case class Asleep(wakeUp: Deferred[IO, Unit]) extends State
    case class Awake(sleep: Deferred[IO, Unit]) extends State
    for {
      wakeUp <- Deferred[IO, Unit]
      state <- Ref[IO].of[State](Asleep(wakeUp))
    } yield new Zzz2 {
      def sleep: IO[Unit] = // Nos vamos a dormir!
          state.modify {
            case zzz@Asleep(wakeUp) => zzz -> wakeUp.get // Si ya estamos dormidos, nos quedamos en espera
            case Awake(sleep) => Asleep(???) -> IO.unit // Si estamos despiertos, nos dormimos
          }.flatten

      def wakeUp: IO[Unit] = // Nos despertamos!
        state.modify {
          case Asleep(wakeUp) => Awake(???) -> wakeUp.complete(()).void // si estamos dormidos nos despertamos y marcamos que ya despertamos
          case openEyes@Awake(sleep) => openEyes -> IO.unit  // si ya estamos despiertos no hacemos nada
        }.flatten
    }
  }
}
