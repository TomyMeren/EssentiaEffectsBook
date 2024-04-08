package com.example.essencialEfects.chapter9

import cats.effect._
import cats.syntax.all._

trait CountdownLatch {
  def await: IO[Unit]
  def decrement: IO[Unit]
}

sealed trait State
case class Outstanding(n: Long, whenDone: Deferred[IO, Unit]) extends State
case class Done() extends State

object CountDownLanch {
  def apply(n: Long): IO[CountdownLatch] = for {
    whenDone <- Deferred[IO, Unit]
    state <- Ref[IO].of[State](Outstanding(n, whenDone))
  } yield new CountdownLatch {

    //ponemos el closer en espera
    def await: IO[Unit] = {
      state.get.flatMap {
        case Outstanding(_, whenDone) => whenDone.get
        case Done() => IO.unit
      }
    }
    //descendemos
    def decrement: IO[Unit] = {
      state.modify {
        case Outstanding(1, whenDone) => Done() -> whenDone.complete(()).void
        case Outstanding(n, whenDone) => Outstanding(n -1, whenDone) -> IO.unit
        case Done() => Done() -> IO.unit
      }.flatten
    }
  }
}
