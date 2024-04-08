package com.example.essencialEfects.chapter4

import cats.effect._
import com.example.debug._

object Start extends IOApp.Simple {
  def run: IO[Unit] = {

    val task : IO[String] = debugWithThread("task")

    for {
      _ <- task.start
      _ <- debugWithThread("task was started")
    } yield ()

  }
}
