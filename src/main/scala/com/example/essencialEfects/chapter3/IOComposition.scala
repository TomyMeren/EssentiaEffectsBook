package com.example.essencialEfects.chapter3

import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.implicits._

object IOComposition extends App {
  def hello = IO(println(s"[${Thread.currentThread.getName}] Hello"))
  def world = IO(println(s"[${Thread.currentThread.getName}] World"))

  val hw1: IO[Unit] = for {
    _ <- hello
    _ <- world } yield ()

  hw1.unsafeRunSync()

  val hw2: IO[Unit] =
    (hello, world).mapN((_, _) => ())

  hw2.unsafeRunSync()
}
