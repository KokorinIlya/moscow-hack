package scalacode

import java.nio.file.{Files, Paths}
import java.util.function.Function

import bing.BingWebSearch
import play.api.libs.json.{JsArray, JsObject, JsString, Json}
import utils.Utils

import scala.collection.JavaConverters._

object BingAsker {
  def searchSingle(search: String) = {
    val result = BingWebSearch.SearchWeb(search)
    val prettyJson = BingWebSearch.prettify(result.getJson)
    Json.parse(prettyJson).asInstanceOf[JsObject]
      .value("webPages").asInstanceOf[JsObject]
      .value("value") match {
      case JsArray(array) => array.map { value =>
        value.asInstanceOf[JsObject].value("url") match {
          case JsString(url) => url
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val namesStream = Files.newBufferedReader(Paths.get("dataset.txt")).lines().map(
      new Function[String, String] {
        override def apply(t: String): String = {
          val splitted = t.split(";; ")
          val shortName = splitted(1)
          shortName
        }
      }
    )
    val names = Utils.collect(namesStream).asScala.toList
    println(names)
    println(searchSingle("Бинг говно"))
  }
}
