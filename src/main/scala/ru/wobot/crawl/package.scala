package ru.wobot

package object crawl {

  type Uri = String
  type Meta = Map[String, Any]

  trait Seed {
    def uri: Uri

    def metadata: Meta
  }

  trait Fetched extends Seed {
    def crawlDate: Long
  }

  case class SuccessFetched[T](uri: Uri, crawlDate: Long, metadata: Meta, content: T) extends Fetched

  case class FailureFetched[T](uri: Uri, crawlDate: Long, metadata: Meta, msg: String) extends Fetched

  trait Fetcher {
    def fetch(uri: Uri): Fetched

    def canFetch(uri: Uri): Boolean
  }

  trait Parsed

  case class SuccessParsed[T](uri: Uri, content: T) extends Parsed

  trait Parser {
    def parse(uri: Uri, content: String): Parsed

    def isUriMatch(uri: Uri): Boolean
  }

  trait Document[T] {
    def uri: Uri

    def content: T

    def metadata: Meta
  }

  case class Post(url: Uri, date: String, profileName: String, profileUrl: String, body: String, likes: Long, reposts: Long, comments: Long, title: Option[String], city: Option[String])

}
