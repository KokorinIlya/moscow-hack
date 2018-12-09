import java.nio.file.{Files, Paths}
import java.util.stream.Collectors

object TagRemover {
  def removeTags(html: String): String = {
    html
      .replaceAll("<script[^>]*>[^<]*</script>", "<")
      .replaceAll("<[^>]*>", "<")
      .replaceAll("&[a-z]+;", " ")
      .replaceAll("[ \\t\\n\\x0B\\f\\r]+", " ")
      .replaceAll("<", "\n")
      .replaceAll("(\\n[ \\t\\n\\x0B\\f\\r]+)+", "\n")
  }

  def main(args: Array[String]): Unit = {
    val html = Files.newBufferedReader(Paths.get("innov2.htm"))
      .lines().collect(Collectors.joining("\n"))
    Files.newBufferedWriter(Paths.get("clear_innov2.txt")).write(removeTags(html))
    //println(EverySiteDownloader.download("https://navigator-pravo.ru/"))
  }
}
