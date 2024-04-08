package com.example.essencialEfects.chapter4

import cats.effect._
import cats.implicits._

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object TimeOut extends IOApp.Simple {
  def run: IO[Unit] = {
    def annotatedSleep(name: String, duration: FiniteDuration):IO[String] = {
      IO.sleep(duration)
        .as(name)
        .debug(name)
    }

    val task = annotatedSleep("task", 100.millis)
    val timeOut = annotatedSleep("time out", 500.millis)

    IO.race(task, timeOut)
      .debug()
      .void
  }
}
