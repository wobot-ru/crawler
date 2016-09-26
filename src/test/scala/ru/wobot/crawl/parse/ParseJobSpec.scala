package ru.wobot.crawl.parse

import org.apache.flink.streaming.api.scala._
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.mockito.mock.SerializableMode
import org.scalatest.FlatSpec
import org.scalatest.mockito.MockitoSugar
import ru.wobot.crawl._
import org.apache.flink.contrib.streaming.scala.utils._
import org.scalatest.Matchers._

class ParseJobSpec extends FlatSpec with MockitoSugar {
  //  val service = mock[SourceProvider]
  //  when(service.getSource[Fetch]()).thenReturn(FetchDataSets.getFetchDataSet(env))
  val parser = mock[Parser](withSettings().serializable(SerializableMode.ACROSS_CLASSLOADERS))
  when(parser.isUriMatch(any[String])).thenReturn(true)
  when(parser.parse(any[String], any[String])).thenReturn(SuccessParsed("", ""))
  implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
  val in: DataStream[Fetched] = FetchDataStreamTestData.getFetchDataStream(env)

  val transform = new ParseJob(in, List(parser))
  behavior of "When the parseJob create and getOutput"

  it should "parse all elements" in {

    val inputs = in.collect.count(_ => true)
    val outputs = transform.getOutput.collect.count(_ => true)
    outputs shouldEqual inputs
  }
  //  it should "invoke the SourceProvider.getSource method" in {
  //    verify(service, only()).getSource()
  //  }

  //  it should "invoke parsers" in {
  //    verify(parser, times(5)).parse("http://localhost1", "data")
  //    verify(parser, times(1)).parse("http://localhost2", "data")
  //  }
}