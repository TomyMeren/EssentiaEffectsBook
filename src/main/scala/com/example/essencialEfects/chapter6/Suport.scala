package com.example.essencialEfects.chapter6

import cats.effect.IO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Suport {
  trait API {
    def compute:Future[Int] = Future(1)//???
  }

  //val k :(Either[Throwable,Int] => Unit) => Unit =
  //  cb =>
  //    api.compute.onComplete {
  //      case Failure(exception) => cb(Left(exception))
  //      case Success(value) => cb(Right(value))
  //    }


  def doSomething(api:API):IO[Int] = {
    IO.async_ { // k : (Either[Throwable, A] => Unit) => Unit
      cb => // Either[Throwable, A] => Unit
      api.compute.onComplete {
        case Failure(exception) => cb(Left(exception))
        case Success(value) => cb(Right(value))
      }: Unit
    }
  }

  val api = new API {}
  doSomething(api)
}
