package ru.wobot

package object crawl {

  import scala.concurrent.Future

  type URI = String
  type Meta = Map[String, Any]

  trait Uri {
    def uri: URI
  }

  trait Metadata {
    def metadata: Meta
  }

  trait Seed extends Uri with Metadata

  trait Fetched extends Seed {
    def fetchDate: Long
  }

  case class SuccessFetched[T](uri: URI, fetchDate: Long, metadata: Meta, content: T) extends Fetched

  case class FailureFetched(uri: URI, fetchDate: Long, metadata: Meta, msg: String) extends Fetched

  @SerialVersionUID(1L)
  trait Fetcher extends Serializable {
    def fetch(uri: URI): Future[Fetched]

    def canFetch(uri: URI): Boolean
  }

  trait Parsed

  case class SuccessParsed[T](uri: URI, content: T) extends Parsed

  trait Parser {
    def parse(uri: URI, content: String): Parsed

    def isUriMatch(uri: URI): Boolean
  }

  trait Document[T] {
    def uri: URI

    def content: T

    def metadata: Meta
  }

  case class Post(url: URI, date: String, profileName: String, profileUrl: String, body: String, likes: Long, reposts: Long, comments: Long, title: Option[String], city: Option[String])

}
