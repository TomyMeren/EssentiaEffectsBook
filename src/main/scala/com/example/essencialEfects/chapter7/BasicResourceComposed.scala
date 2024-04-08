package com.example.essencialEfects.chapter7

import cats.effect._
import cats.syntax.all._


object BasicResourceComposed extends IOApp.Simple {
  def run: IO[Unit] =
    (stringResource, intResource).parTupled
      .use { case (s, i) =>
        IO.println(s"$s is so cool!") *>
        IO.println(s"$i is a great number!")
      }
      .void


  val stringResource: Resource[IO, String] =
    Resource.make(
      IO.println("> acquiring stringResource") *> IO("String")
    )(_ => IO.println("releasing stringResource"))

  val intResource: Resource[IO, Int] =
    Resource.make(
      IO.println("> acquiring intResource") *> IO(42)
    )(_ => IO.println("releasing intResource"))
}
