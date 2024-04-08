package com.example.essencialEfects.chapter5

import cats.effect._
import cats.syntax.all._
import com.example.debug._

object Parallelism extends IOApp.Simple {
  def run: IO[Unit] = {
    val numCpus = Runtime.getRuntime().availableProcessors()
    def task(i: Int): IO[Int] = debugWithThread(i)
    val tasks: IO[List[Int]] = List.range(0, numCpus * 2).parTraverse(task)

    for {
      _ <- IO.println(s"number of cpus = $numCpus")
      _ <- tasks.debug()
    } yield ()
  }
}
