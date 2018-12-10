package scalacode

import java.nio.file.{Files, Paths}
import java.util.function.Predicate
import java.util.stream.Collectors

import crawler.WebCrawler
import downloader.{CachingDownloader, URLUtils}
import utils.Utils

import scala.collection.JavaConverters._

object EverySiteDownloader {
  def download(url: String, depth: Int): String = {
    val host = URLUtils.getHost(url)
    val path = Paths.get("downloaded").resolve(url)
    val crawler = new WebCrawler(
      new CachingDownloader(path),
      5,
      20,
      5,
      new Predicate[String] {
        override def test(curUrl: String): Boolean = {
          URLUtils.getHost(curUrl) == host
        }
      }
    )
    crawler.download(url, depth)
    crawler.close()
    val text = Utils.collect(Files.list(path)).asScala.map { path =>
      val reader = Files.newBufferedReader(path)
      reader.lines().collect(Collectors.joining("\n"))
    }.toList.mkString("\n")
    val ans = TagRemover.removeTags(text).split("\n").filter(_.length > 8).toSet.mkString("\n")
    Files.newBufferedWriter(path.resolve("clear_version.txt")).write(ans)
    ans
  }
}
