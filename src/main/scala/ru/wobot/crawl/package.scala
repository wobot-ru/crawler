package ru.wobot

package object crawl {

  trait Fetch {
    def uri: String

    def crawlDate: Long
  }

  case class SuccessFetch[T](uri: String, crawlDate: Long, data: T) extends Fetch

  trait Parsed

  case class SuccessParsed[T](val uri: String, data: T) extends Parsed

  trait Parser{
    def parse(uri: String, data: String): Parsed

    def isUriMatch(uri: String): Boolean
  }

  case class FbSearchParser() extends Parser {
    override def parse(uri: String, data: String): Parsed = {
      new SuccessParsed[String](uri, data)
    }

    override def isUriMatch(uri: String): Boolean = true
  }
}
