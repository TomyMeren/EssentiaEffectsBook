package com.example.essencialEfects.chapter3

import cats.implicits._
import scala.concurrent._
import scala.concurrent.duration._

object Future1 extends App {
  implicit val ec = ExecutionContext.global
  def hello = Future(println(s"[${Thread.currentThread.getName}] Hello"))
  def world = Future(println(s"[${Thread.currentThread.getName}] World"))

  val hw1: Future[Unit] = for {
    _ <- hello
    _ <- world } yield ()

  Await.ready(hw1, 5.seconds)

  val hw2: Future[Unit] =
    (hello, world).mapN((_, _) => ())

  Await.ready(hw2, 5.seconds)
}
