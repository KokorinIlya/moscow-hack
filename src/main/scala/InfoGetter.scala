import java.util

import data.Aggregators

import scala.collection.JavaConverters._

object InfoGetter {
  val agrs: Set[String] = Aggregators.AGGREGATORS_HOSTNAMES.toSet

  def getInfo(companyName: String): util.List[String] = {
    val sites = BingAsker.searchSingle(companyName)
    sites.map { site =>
      var depth = 2

      for {agr <- agrs} {
        if (site.contains(agr)) {
          depth = 1
        }
      }

      EverySiteDownloader.download(site, depth)
    }.asJava
  }
}
