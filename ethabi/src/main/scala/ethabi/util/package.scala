package ethabi

import java.io.{File, PrintWriter}

package object util {
  def writeToFile(path: String, fileName: String, content: String): Unit = {
    val dir = new File(path)
    dir.mkdirs
    new PrintWriter(path + s"/$fileName") {
      write(content)
      close()
    }
  }

  def writeToFile(path: String, content: String): Unit = {
    new PrintWriter(path) {
      write(content)
      close()
    }
  }
}
