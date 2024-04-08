package com.example.essencialEfects.chapter3

import cats.effect._
import cats.implicits._

object ParTraverse extends IOApp {
  def run(args: List[String]):IO[ExitCode] = {
    import com.example.adamRosien.youtube.debug2._
    val tasks: List[Int] = List.range(1, 100)
    def task(id:Int): IO[Int] = IO(id).debug2

    tasks
      .parTraverse(task)
      .debug2
      .as(ExitCode.Success)
  }
}
