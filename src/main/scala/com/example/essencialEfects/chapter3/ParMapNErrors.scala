package com.example.essencialEfects.chapter3

import cats.effect._
import cats.implicits._

import scala.concurrent.duration.DurationInt

object ParMapNErrors extends IOApp {
  def run(array: List[String]): IO[ExitCode] = {
    import com.example.adamRosien.youtube.debug2._

    val ok = {
      //IO.sleep(1.second).as("hifirst").debug2 *>
      IO("hi").debug2
    }

    val ko1 = {
      //IO.sleep(1.second).as("ko1").debug2 *>
      IO.raiseError[String](new RuntimeException("oh!")).debug2
    }

    val ko2 = IO.raiseError[String](new RuntimeException("noes!")).debug2
    val e1 = (ok, ko1). parTupled.void
    val e2 = (ko1, ok). parTupled.void
    val e3 = (ko1, ko2).parTupled.void

    e1.attempt.debug2 *>
      //IO("---").debug2 *>
      e2.attempt.debug2 *>
      //IO("---").debug2 *>
      e3.attempt.debug2 *>
      IO.pure(ExitCode.Success)
  }
}