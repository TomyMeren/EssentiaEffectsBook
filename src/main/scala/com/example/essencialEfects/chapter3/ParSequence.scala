package com.example.essencialEfects.chapter3

import cats.effect._
import cats.implicits._

object ParSequence extends IOApp {
  def run(args: List[String]):IO[ExitCode] = {
    import com.example.adamRosien.youtube.debug2._

    def task(id:Int): IO[Int] = IO(id).debug2
    val tasks: List[IO[Int]] = List.tabulate(100)(task)

    tasks
      .parSequence
      .debug2
      .as(ExitCode.Success)
  }
}
