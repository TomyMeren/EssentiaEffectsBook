package com.example.essencialEfects.chapter10

import cats.effect._


// Implement Zzz as a concurrent state machine

trait Zzz { // Acciones
  def sleep: IO[Unit]
  def wakeUp: IO[Unit]
}

object Zzz {
  def asleep: IO[Zzz] = {
    sealed trait State // Estados
    case class Asleep(wakeUp: Deferred[IO, Unit]) extends State
    case object Awake extends State
    for {
      wakeUp <- Deferred[IO, Unit]
      state <- Ref[IO].of[State](Asleep(wakeUp))
    } yield new Zzz {
      def sleep: IO[Unit] = // Nos vamos a dormir!
        for {
          asleep <- Deferred[IO, Unit].map(Asleep) // creamos un nuevo Deferred para dormir. Este defered nunca se pondra en espera por que
          // no tenemos ninguna operacion bloqueante que espere a que nos durmamos TODO: Esto no casa con el grafico auqnue si podria casar con el rpopsito
          _ <- state.modify {
            case zzz@Asleep(wakeUp) => zzz -> wakeUp.get // Si ya estamos dormidos, nos quedamos en espera
            case Awake => asleep -> IO.unit // Si estamos despiertos, nos dormimos
          }.flatten
        } yield ()

      def wakeUp: IO[Unit] = // Nos despertamos!
        state.modify {
          case Asleep(wakeUp) => Awake -> wakeUp.complete(()).void // si estamos dormidos nos despertamos y marcamos que ya despertamos
          case Awake => Awake -> IO.unit  // si ya estamos despiertos no hacemos nada
        }.flatten
    }
  }
}
