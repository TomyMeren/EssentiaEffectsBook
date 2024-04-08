package com.example.essencialEfects.chapter6

import cats.effect._

object Never extends IOApp.Simple { def run: IO[Unit] =
    never
      .guarantee(IO.println("i guess never is now"))

    val never: IO[Nothing] = IO.async_(cb => ())
}
