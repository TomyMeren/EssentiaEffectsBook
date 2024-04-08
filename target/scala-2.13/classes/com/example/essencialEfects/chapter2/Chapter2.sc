import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.{catsSyntaxMonadError, catsSyntaxTuple2Semigroupal}

val hw: IO[Unit] = IO.delay(println("Hello world!")) //fail with eval
val delay = IO.delay(throw new RuntimeException("oh noes!"))

val ohNoes: IO[Int] =
  IO.raiseError(new RuntimeException("oh noes!"))
val b = IO(12).map(_ + 1)
val c = (IO(12), IO("hi")).mapN((i, s) => s"$s: $i")

val a = for {
  i <- IO(12)
  _ <- ohNoes
  j <- IO(i + 1)
} yield j.toString


val ohNoes2 =
  IO.raiseError[Int](new RuntimeException("oh noes2!"))

val handled: IO[Int] = ohNoes2.handleErrorWith(_ => IO(12))
val handledSimpl: IO[Int] = ohNoes2.handleError(_ => 12)
val handledNew: IO[Int] = ohNoes2.handleErrorWith(t => IO.raiseError(new RuntimeException(t + " meh")))
val handledNewSimpl: IO[Int] = ohNoes2.adaptError(t => new RuntimeException(t + " suu"))

val d = for {
  i <- IO(12)
  x <- handledSimpl
  j <- IO(13)
} yield i + j + x

ohNoes2.attempt

d.unsafeRunSync
