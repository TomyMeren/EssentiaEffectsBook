package com.example.essencialEfects.chapter5

import cats.effect.{IO, IOApp}

object TickTockBloquerThread extends IOApp.Simple {
  def run: IO[Unit] = {
    for {
      _ <- IO("on default").debug()
      _ <- IO.blocking("on blocker").debug()
    } yield ()
  }
}
