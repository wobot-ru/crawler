package ru.wobot

package object crawl {

  trait Fetch {
    def uri: String

    def crawlDate: Long
  }

  case class SuccessFetch[T](uri: String, crawlDate: Long, data: T) extends Fetch

  trait Parsed

  case class SuccessParsed[T](uri: String, data: T) extends Parsed

  trait Parser {
    def parse(uri: String, data: String): Parsed

    def isUriMatch(uri: String): Boolean
  }

  case class FbSearchParser() extends Parser {
    import org.jsoup.Jsoup
    import org.jsoup.nodes.Document
    import scala.collection.JavaConversions._

    override def parse(uri: String, data: String): Parsed = {
      val document: Document = Jsoup.parse(data, uri)
      val elements = document.select(".userContentWrapper")
      for (el <- elements) {
        val profileLink = el.select(".fwb.fcg a:first-child")
        val name: String = profileLink.toString
        val profileHref: String = profileLink.attr("href")
        System.out.println(profileHref)
      }
      new SuccessParsed[String](uri, data)
    }

    override def isUriMatch(uri: String): Boolean = true
  }

}
