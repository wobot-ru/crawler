package ru.wobot.crawl.fetch

import org.apache.flink.contrib.streaming.scala.utils._
import org.apache.flink.streaming.api.scala._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.mock.SerializableMode
import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import org.scalatest.mockito.MockitoSugar
import ru.wobot.crawl._

import scala.collection.immutable.Map

class FetchedTransformTest extends FlatSpec with MockitoSugar {
  implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  val seeds: DataStream[Uri] = SeedsDataStreamTestData.getSeeds

  val fetcher: Fetcher = mock[Fetcher](withSettings().serializable(SerializableMode.ACROSS_CLASSLOADERS))
  when(fetcher.canFetch(any[String])).thenReturn(true)
  when(fetcher.fetch(any[Uri])).thenReturn(SuccessFetched[String]("", 0, Map.empty, ""))

  val transform = new FetchTransform(seeds, List[Fetcher](fetcher))

  behavior of "When FetchTransform initialize with HttpFetcher"

  it should "fetch all elements" in {
    val inputs = seeds.collect().count(_ => true)
    val outputs = transform.getOutput().collect().count(_ => true)
    outputs shouldEqual inputs
  }
}