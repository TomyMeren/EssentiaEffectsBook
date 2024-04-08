package com.example.essencialEfects.chapter6

import cats.effect._
import java.util.concurrent.CompletableFuture
import scala.jdk.FunctionConverters._

object AsyncCompletable extends IOApp.Simple { def run: IO[Unit] =
  effect.debug().void

  val effect: IO[String] = fromCF(IO(cf()))

  def fromCF[A](cfa: IO[CompletableFuture[A]]): IO[A] =
    cfa.flatMap { fa => //CompletableFuture[A]
      IO.async_ { cb => // Either[Throwable, A] => Unit
        val handler: (A, Throwable) => Unit = {
          case (elem, null) =>  cb(Right(elem))
          case (null, except) => cb(Left(except))
          case (a, b) => sys.error(s"CompletableFuture handler should always have one null, got: $a, $b")
        }
        fa.handle(handler.asJavaBiFunction)

        () //: (Either[Throwable,Int] => Unit) => Unit
      }
    }

  def cf(): CompletableFuture[String] = CompletableFuture.supplyAsync(() => "woo!")
}