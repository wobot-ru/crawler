package ru.wobot

import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala.DataStream

import scala.collection.immutable

package object crawl {

  type URI = String
  type Meta = Map[String, Any]

  trait WithUri {
    def uri: URI
  }

  trait WithMeta {
    def metadata: Meta
  }

  trait UriLike extends WithUri with WithMeta

  trait Fetched extends UriLike {
    def fetchDate: Long
  }

  @SerialVersionUID(1L)
  trait Fetcher extends Serializable {

    import scala.concurrent.Future

    def fetch(uri: URI): Future[Fetched]

    def canFetch(uri: URI): Boolean
  }

  trait Parsed

  @SerialVersionUID(1L)
  trait Parser extends Serializable {
    def parse(uri: URI, content: String): Parsed

    def isUriMatch(uri: URI): Boolean
  }

  trait Document extends WithMeta {
    def id: String

    def toJson: String
  }

  trait SourceProvider[T] {
    def getSource(): DataStream[T]
  }

  trait SinkProvider[T] {
    def getSinks(): List[SinkFunction[T]]
  }

  case class Uri(uri: URI, metadata: Meta) extends UriLike {
    override def toString: URI = uri
  }

  case class SuccessFetched[T](uri: URI, fetchDate: Long, metadata: Meta, content: T) extends Fetched

  case class FailureFetched(uri: URI, fetchDate: Long, metadata: Meta, msg: String) extends Fetched

  case class SuccessParsed[T](uri: URI, content: T) extends Parsed

  case class Post(url: URI, date: String, profileName: String, profileUrl: String, body: String, likes: Long, reposts: Long, comments: Long, title: Option[String], city: Option[String], meta: Meta = immutable.Map.empty) extends Document {
    override def id: String = id

    override def toJson: String = s"{id=$id}"

    override def metadata: Meta = meta
  }


  object Uri {
    implicit def toURI(s: WithUri): URI = s.uri

    implicit def fromURI(s: URI): Uri = Uri.apply(s)

    def apply(uri: URI): Uri = apply(uri, collection.immutable.Map.empty)
  }

}
