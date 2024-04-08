package com.example.essencialEfects.chapter2

import cats.effect._
import cats.implicits._
import com.example.debug.debugWithThread

import scala.concurrent.duration.DurationInt

object TickingClock extends IOApp.Simple {

    val ohNoes = IO.sleep(3.second) *> IO.raiseError(new RuntimeException("oh noes!"))

    val tickingClock: IO[Unit] =
      for {
        _ <- debugWithThread(System.currentTimeMillis)
        _ <- IO.sleep(1.second)
        _ <- tickingClock
      } yield ()

  def run: IO[Unit] = {
    (tickingClock, ohNoes).parTupled.void

  }
}