package com.example.essencialEfects.chapter3

import cats.effect._
import cats.implicits._

object ParMapN extends IOApp {
  def run(array: List[String]): IO[ExitCode] = {
    import com.example.adamRosien.youtube.debug2._

    val hello = IO("hello").debug2
    val world = IO("world").debug2
    val seq = (hello, world)
      .parMapN((h, w) => s"$h $w")
      .debug2

    seq.as(ExitCode.Success)
  }
}