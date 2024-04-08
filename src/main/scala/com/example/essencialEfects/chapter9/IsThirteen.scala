package com.example.essencialEfects.chapter9

import cats.effect._
import cats.syntax.all._
import com.example.debug._
import scala.concurrent.duration._

object IsThirteen extends IOApp.Simple {
  def run: IO[Unit] =
    for {
      count <- Ref[IO].of(0L)
      alert13 <- Deferred[IO, Unit]
      _ <- (printTimeAndMod(count, alert13), print13AndWait(alert13)).parTupled
    } yield ()

  def printTimeAndMod(value: Ref[IO, Long], deferred: Deferred[IO, Unit]):IO[Unit] =
    for {
      _ <- IO.sleep(1.seconds)
      _ <- IO.println(System.currentTimeMillis())
      newCount <- value.getAndUpdate(_ + 1)
      _ <- if(newCount >= 13) deferred.complete(()) else IO.unit
      _ <-printTimeAndMod(value, deferred)
    } yield ()

  def print13AndWait(value: Deferred[IO, Unit]):IO[Unit] =
    for {
      _ <- value.get
      _ <- IO.println("Beep!")
    } yield ()
}