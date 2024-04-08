package com.example.essencialEfects.chapter7

import cats.effect._

object BasicResourceFailure extends IOApp.Simple {
    def run: IO[Unit] = {
        // def make[A](acquire: IO[A])(release: A => IO[Unit]): Resource[IO, A]
        val stringResource: Resource[IO, String] =
            Resource.make(IO.println("open connection") *> IO("String")) {
                _ => IO.println("releasing connection")
            }

        stringResource
          .use(_ => IO.raiseError[Unit](new RuntimeException("Boooom!!")).debug())
          .attempt
          .void
    }
}
