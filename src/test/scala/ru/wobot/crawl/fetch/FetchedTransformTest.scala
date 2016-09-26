package ru.wobot.crawl.fetch

import org.scalatest.FlatSpec
import org.scalatest.mockito.MockitoSugar
import ru.wobot.crawl.fetch.FetcherFactory.HttpFetcher

class FetchedTransformTest extends FlatSpec with MockitoSugar {

  import org.apache.flink.contrib.streaming.scala.utils._
  import org.apache.flink.streaming.api.scala._
  import org.scalatest.Matchers._
  import ru.wobot.crawl._

  import scala.collection.immutable.Map
  import scala.concurrent.Future

  implicit val env = StreamExecutionEnvironment.createLocalEnvironment()
  val seeds = SeedsDataStreamTestData.getSeeds


  val transform = {
    val stub = new HttpFetcher {
      override def canFetch(uri: URI) = true

      override def fetch(uri: URI) = Future.successful(SuccessFetched(uri, 0, Map.empty, "data"))
    }

    new FetchJob(seeds, Nil, List(stub))
  }
  behavior of "When FetchTransform initialize with HttpFetcher"

  it should "fetch all elements" in {
    val inputs = seeds.collect.count(_ => true)
    val outputs = transform.getOutput.collect.count(_ => true)
    outputs shouldEqual inputs
  }
}