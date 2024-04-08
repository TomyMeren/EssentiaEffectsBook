package com.example.essencialEfects.chapter5

import cats.effect._
import com.example.debug._
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object ShiftingMultiple extends IOApp.Simple {
  def run: IO[Unit] = {

    def ec(name: String):ExecutionContext =
      ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor { r =>
        val t = new Thread(r, s"pool-$name-thread-1")
        t.setDaemon(true)
        t
      })

    for {
      _ <- debugWithThread("one").evalOn(ec("1"))
      _ <- debugWithThread("two").evalOn(ec("2"))
      _ <- debugWithThread("three")
    } yield ()

  }
}