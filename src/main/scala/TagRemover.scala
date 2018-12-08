import java.nio.file.{Files, Paths}
import java.util.stream.Collectors

object TagRemover {
  def removeTags(html: String): String = {
    html
      .replaceAll("<script>[^<]*</script>", "<")
      .replaceAll("<[^>]*>", "<")
      .replaceAll("[ \\t\\n\\x0B\\f\\r]+", " ")
      .replaceAll("<", "\n")
      .replaceAll("(\\n[ \\t\\n\\x0B\\f\\r]+)+", "\n")
  }

  def main(args: Array[String]): Unit = {
    val html = Files.newBufferedReader(Paths.get("fuck.html")).lines().collect(Collectors.joining("\n"))
    println(removeTags(html))
  }
}
