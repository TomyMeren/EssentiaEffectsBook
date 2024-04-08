package com.example.essencialEfects.chapter4

import cats.effect._
import cats.implicits._

object MyParMap {
  def myParMapN[A, B, C](ia:IO[A], ib:IO[B])(f:(A, B) => C):IO[C] = {
    IO.racePair(ia, ib).flatMap { // IO[Either[(OutcomeIO[A], FiberIO[B]), (FiberIO[A], OutcomeIO[B])]]]
      case Left((oa, fb)) => // (OutcomeIO[A], FiberIO[B])
        oa match { // OutcomeIO[A]
          case Outcome.Succeeded(sa) => // Outcome[IO, Throwable, A]
            fb.join.flatMap { // Fiber[IO, Throwable, A] + join = IO[Outcome[IO, Throwable, A]]
              case Outcome.Succeeded(sb) => (sa, sb).mapN(f)
              case Outcome.Errored(e) => IO.raiseError(e)
              case Outcome.Canceled() => IO.never
            }
          case Outcome.Errored(e) => fb.cancel *> IO.raiseError(e)
          case Outcome.Canceled() => fb.cancel *> IO.never
        }
      case Right((fa, ob)) => // (FiberIO[A], OutcomeIO[B])
        ob match { //OutcomeIO[B]
          case Outcome.Succeeded(sb) => // Outcome[IO, Throwable, A]
          fa.join.flatMap { // Fiber[IO, Throwable, A] + join = IO[Outcome[IO, Throwable, A]]
            case Outcome.Succeeded(sa) => (sa, sb).mapN(f)
            case Outcome.Errored(e) => IO.raiseError(e)
            case Outcome.Canceled() => IO.never
          }
          case Outcome.Errored(e) => fa.cancel *> IO.raiseError(e)
          case Outcome.Canceled() => fa.cancel *> IO.never
        }
    }
  }
}
