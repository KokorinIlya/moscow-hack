package scalacode

import java.io.InputStreamReader
import java.net.URI

object RusProfilesAsker {
  def makeRequest(ip: Boolean, name: String, pageNum: Int): String = {
    val tp = if (ip) "ip" else "ul"
    val url = URI.create(s"https://www.rusprofile.ru/search?page=$pageNum&type=$tp&query=$name")
    val stream = url.toURL.openStream()
    val reader = new InputStreamReader(stream)
    val builder = StringBuilder.newBuilder
    val arr = new Array[Char](500)
    var len = 0
    do {
      len = reader.read(arr)
      if (len != -1) {
        builder.append(new String(arr, 0, len))
      }
    } while (len != -1)
    builder.toString()
  }

  def main(args: Array[String]): Unit = {

  }
}
