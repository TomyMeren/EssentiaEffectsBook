package com.example.essencialEfects.chapter4

import cats.effect._
import com.example.debug._

import scala.concurrent.duration.DurationInt

object Cancel extends IOApp.Simple {
  def run: IO[Unit] = {
    val task : IO[String] = debugWithThread("task") *> IO.never

    for {
      fiber <- task
        .onCancel(debugWithThread("I was canceled").void)
        .start
      _ <- debugWithThread("pre-canceled")
      _ <- fiber.cancel
      _ <- debugWithThread("post-canceled")
    } yield ()
  }
}