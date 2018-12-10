import java.util

import data.Aggregators

import scala.collection.JavaConverters._
import scala.util.control.Breaks._

object InfoGetter {
  val agrs: Set[String] = Aggregators.AGGREGATORS_HOSTNAMES.toSet

  def getInfo(companyName: String): util.List[String] = {
    val sites = BingAsker.searchSingle(companyName)
    sites.map { site =>
      var depth = 2

      for {agr <- agrs} {
        if (site.contains(agr)) {
          depth = 1
          break
        }
      }

      EverySiteDownloader.download(site, depth)
    }.asJava
  }
}
