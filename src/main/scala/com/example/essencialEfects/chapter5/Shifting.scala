package com.example.essencialEfects.chapter5

import cats.effect._
import com.example.debug._
object Shifting extends IOApp.Simple { def run: IO[Unit] =
  for {
    _ <- debugWithThread("one")
    _ <- IO.cede
    _ <- debugWithThread("two")
    _ <- IO.cede
    _ <- debugWithThread("three")
  } yield ()
}
