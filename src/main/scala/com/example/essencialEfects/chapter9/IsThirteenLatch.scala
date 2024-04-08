package com.example.essencialEfects.chapter9

import cats.effect._
import cats.syntax.all._
import com.example.debug._
import scala.concurrent.duration._

object IsThirteenLatch extends IOApp.Simple {
  def run: IO[Unit] = for {
    lanch <- CountDownLanch(13)
    _ <- (beeper(lanch), tickingClock(lanch)).parTupled
  } yield ()

  def beeper(lanch:CountdownLatch):IO[Unit] = {
    for {
      _ <- lanch.await
      _ <- debugWithThread("BEEP!") } yield ()
  }

  def tickingClock(lanch:CountdownLatch):IO[Unit] = {
    for {
      _ <- IO.sleep(1.seconds)
      _ <- IO.realTimeInstant.debug()
      _ <- lanch.decrement
      _ <- tickingClock(lanch)
    } yield ()
  }
}
