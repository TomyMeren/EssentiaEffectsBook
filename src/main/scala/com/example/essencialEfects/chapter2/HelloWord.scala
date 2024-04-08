package com.example.essencialEfects.chapter2

import cats.effect._

object HelloWord  extends IOApp {
  def run(args: List[String]):IO[ExitCode] = {
    val helloWord: IO[Unit] = IO(println("Hello, World!"))

    helloWord.as(ExitCode.Success)
  }
}
