case class MyIO[A](unSafeRun: () => A) {
  def map[B](f: A => B): MyIO[B] =
    MyIO(() => f(unSafeRun()))

  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    MyIO(() => f(unSafeRun()).unSafeRun())
}

object MyIO {
  def putString(s: => String):MyIO[Unit] =
    MyIO(() => println(s))
}

val hello = MyIO.putString("Hello")
val goodBye = MyIO.putString("Goodbye!")
//hello.unSafeRun()

val helloGoodBye:MyIO[Unit] = for {
  _ <- hello
  _ <- goodBye
} yield ()

helloGoodBye.unSafeRun()