package ru.wobot.crawl.parse
import ru.wobot.crawl._

case class FbSearchParser() extends Parser {

  val FACEBOOK_URL = "https://www.facebook.com/"

  import org.jsoup.Jsoup
  import org.jsoup.nodes.Element
  import org.jsoup.nodes.{Document => SoupDoc}
  import java.net.URL

  import scala.collection.JavaConversions._

  override def isUriMatch(uri: String): Boolean = true

  override def parse(uri: String, content: String): Parsed = {
    val document: SoupDoc = Jsoup.parse(content, uri)
    val elements = document.select(".userContentWrapper")
    for (el <- elements) {
      val (profileUrl, profileName) = getProfile(el)
      val post = Post(
        url = getPostUrl(el),
        date = getDate(el),
        profileName = profileName,
        profileUrl = profileUrl,
        body = getBody(el),
        likes = getLikes(el),
        reposts = getReposts(el),
        comments = getComments(el),
        title = getTitle(el),
        city = getCity(el)
      )
      System.out.println(post)
    }
    new SuccessParsed[String](uri, content)
  }

  def getProfile(el: Element): (String, String) = {
    val profileLink = el.select(".fwb.fcg a:nth-of-type(1)")
    val (url, name) = if (!profileLink.attr("href").isEmpty)
      (profileLink.attr("href"), profileLink.text().trim)
    else {
      val profileLink = el.select("a.profileLink:nth-child(1)")
      (profileLink.attr("href"), profileLink.text().trim)
    }

    if (url.contains("https://www.facebook.com/profile.php")) {
      val queryIndex = url.indexOf("&")
      if (queryIndex > 0)
        (url.substring(0, queryIndex), name)
      else (url, name)
    }
    else {
      val queryIndex = url.lastIndexOf("?")
      if (queryIndex > 0)
        (url.substring(0, queryIndex), name)
      else (url, name)
    }
  }

  def getPostUrl(el: Element): String = {
    val e = el.select("._5pcq")
    val url: String = e.attr("href")
    if (url.startsWith(FACEBOOK_URL))
      url
    else
      new URL(new URL(FACEBOOK_URL), url).toString
  }

  def getDate(el: Element): String = {
    val e = el.select("abbr._5ptz")
    e.attr("data-utime")
  }

  def getCity(el: Element): Option[String] = {
    val e = el.select("._5pcq:nth-of-type(2)")
    optionForEmpty(e.text())
  }

  def getBody(el: Element): String = {
    val e = el.select(".userContent")
    e.text()
  }

  def getTitle(el: Element): Option[String] = {
    val e = el.select(".accessible_elem a")
    optionForEmpty(e.text())
  }

  //todo: add support for non Russian
  def getComments(el: Element): Long = {
    val e = el.select("._36_q:nth-of-type(1)")
    zeroForEmpty(e.text().replace(" ", "").replace("Комментарии:", ""))
  }

  //todo: add support for non Russian
  def getReposts(el: Element): Long = {
    val e = el.select("._36_q:nth-of-type(2)")
    zeroForEmpty(e.text().replace(" ", "").replace("перепост", "").replace("Перепосты:", ""))
  }

  def getLikes(el: Element): Long = {
    val e = el.select("._4arz")
    zeroForEmpty(e.text())
  }

  private def zeroForEmpty(s: String): Long = {
    if (s.isEmpty)
      0
    else
      s.toLong
  }

  private def optionForEmpty(s: String): Option[String] = {
    if (s.isEmpty)
      None
    else
      Some(s)
  }
}
