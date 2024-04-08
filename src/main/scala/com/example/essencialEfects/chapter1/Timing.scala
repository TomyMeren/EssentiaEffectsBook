package com.example.essencialEfects.chapter1

import com.example.adamRosien.youtube.MyIO

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

object Timing extends App {
  private val clock: MyIO[Long] =
    MyIO(() => System.currentTimeMillis) // <1>

  private def time[A](action: MyIO[A]): MyIO[(FiniteDuration, A)] =
    for {
      ini  <- clock
      act <- action
      fin <- clock
    } yield (FiniteDuration(fin - ini, MILLISECONDS), act)

  private val timedHello = Timing.time(MyIO.putStr("hello"))

  timedHello.unsafeRun() match {
    case (duration, _) => println(s"'hello' took $duration")
  }
}
