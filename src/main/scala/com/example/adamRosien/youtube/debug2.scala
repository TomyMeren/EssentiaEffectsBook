package com.example.adamRosien.youtube

import cats.effect._
import com.example.Colorize
/** `import com.innerproduct.ee.debug._` to access
 * the `debug` extension methods. */
object debug2 {
  /** Extension methods for an effect of type `IO[A]`. */
  implicit class DebugHelper[A](ioa: IO[A]) {
    /** Print to the console the value of the effect
     * along with the thread it was computed on. */
    def debug2: IO[A] =
      for {
        a <- ioa
        tn = Thread.currentThread.getName
        _ = println(s"[${Colorize.reversed(tn)}] $a")
      } yield a }
}
