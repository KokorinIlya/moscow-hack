import java.nio.file.{Files, Paths}
import java.util.function.Predicate
import java.util.stream.Collectors

import scala.collection.JavaConverters._
import crawler.WebCrawler
import downloader.{CachingDownloader, URLUtils}
import utils.Utils

object EverySiteDownloader {
  def download(url: String): String = {
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
    crawler.download(url, 2)
    crawler.close()
    val text = Utils.collect(Files.list(path)).asScala.map { path =>
      val reader = Files.newBufferedReader(path)
      reader.lines().collect(Collectors.joining("\n"))
    }.toList.mkString("\n")
    val ans = TagRemover.removeTags(text).split("\n").filter(_.length > 8).toSet.mkString("\n")
    println(s"LENGTH IS ${ans.length}")
    ans
  }
}
