package com.example.essencialEfects.chapter9

import cats.effect._
import cats.syntax.all._

import scala.concurrent.duration._

object ConcurrentStateVar extends IOApp.Simple {
  def run: IO[Unit] = for {
    ticks <- Ref[IO].of(0L)
    _ <- (tickingClock(ticks), printTicks(ticks)).parTupled
  } yield ()


  def tickingClock(ticks:Ref[IO, Long]):IO[Unit] = for {
    _ <- IO.sleep(1.seconds)
    _ <- IO.println(System.currentTimeMillis())
    _ <- ticks.update(_ + 1)
    _ <- tickingClock(ticks)
  } yield ()

  def printTicks(ticks:Ref[IO, Long]): IO[Unit] = for {
    _ <- IO.sleep(5.seconds)
    t <- ticks.get
    _ <- IO.println(s"TICKS: $t")
    _ <- printTicks(ticks)
  } yield ()
}
