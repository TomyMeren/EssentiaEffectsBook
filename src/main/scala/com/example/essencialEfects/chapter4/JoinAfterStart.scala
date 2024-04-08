package com.example.essencialEfects.chapter4

import cats.effect._
import com.example.debug._

import scala.concurrent.duration.DurationInt

object JoinAfterStart extends IOApp.Simple {
  def run: IO[Unit] = {
    val task : IO[String] = IO.sleep(2.seconds) *>
      debugWithThread("task")

    for {
      fiber <- task.start
      _ <- debugWithThread("pre-joined")
      _ <- fiber.join.debug()
      _ <- debugWithThread("post-join")
    } yield ()
  }
}
