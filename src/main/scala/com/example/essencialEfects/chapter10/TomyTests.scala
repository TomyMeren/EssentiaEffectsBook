package com.example.essencialEfects.chapter10

import cats.effect._

object TomyTests extends IOApp.Simple {
  def run: IO[Unit] = for {
    _ <- IO.println("hola").void.guaranteeCase(_ => IO.println("a2")).start
    _ <- IO.println("meh")
  } yield ()


}
