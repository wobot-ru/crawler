package ru.wobot.crawl.parse

import org.apache.commons.io.IOUtils
import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import ru.wobot.crawl.SuccessParsed

class FbSearchParserSpec extends FlatSpec with MockitoSugar {
  val searchData = IOUtils.toString(getClass.getResource("/ru/wobot/crawl/parse/fb/search2.html"))
  val url = "https://www.facebook.com/search/str/%D1%82%D0%B5%D0%BB%D0%B52/stories-keyword/this-week/date/stories/intersect"
  val parser = new FbSearchParser

  behavior of "When parse FaceBook HTML response"

  it should "searchData not null" in {
    searchData should (not be null)
  }

  it should "work" in {
    parser.parse(url, searchData)
  }

  it should "return SuccessParsed" in {
    parser.parse(url, searchData) should matchPattern { case SuccessParsed(url, _, _) => }
  }
}