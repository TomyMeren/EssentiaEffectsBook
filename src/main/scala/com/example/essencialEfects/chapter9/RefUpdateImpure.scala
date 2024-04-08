package com.example.essencialEfects.chapter9

import cats.effect._
import cats.syntax.all._

object RefUpdateImpure extends IOApp.Simple {
  def run: IO[Unit] = for {
    ref <- Ref[IO].of(0)
    _ <- List(1, 2, 3).parTraverse(task(_, ref))
  } yield ()

  def task(count:Int, ref: Ref[IO, Int]): IO[Unit] = {
    ref
      .modify(previous => count -> IO(s"$previous -> $count").debug())
      .flatten
      .replicateA(3)
      .void
  }
}
