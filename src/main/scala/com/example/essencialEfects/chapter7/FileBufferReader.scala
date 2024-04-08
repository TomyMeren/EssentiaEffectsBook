package com.example.essencialEfects.chapter7

import cats.effect._
import java.io.RandomAccessFile

class FileBufferReader private (in: RandomAccessFile) {
  def readBuffer(offset:Long): IO[(Array[Byte], Int)] = {
    IO {
      in.seek(offset)

      val buf = new Array[Byte](FileBufferReader.bufferSize)
      val len = in.read(buf)

      (buf, len)
    }
  }

  private def close:IO[Unit] = IO(in.close())
}

object FileBufferReader {
  val bufferSize = 4096

  def makeResource(fileName: String): Resource[IO, FileBufferReader] = {
    Resource.make { // def make[A](acquire: IO[A])(release: A => IO[Unit]): Resource[IO, A]
      IO(new FileBufferReader(new RandomAccessFile(fileName, "r")))
    }{ res =>
      res.close
    }
  }
}
