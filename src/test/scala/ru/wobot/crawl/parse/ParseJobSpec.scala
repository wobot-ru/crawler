package ru.wobot.crawl.parse

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.mockito.mock.SerializableMode
import org.scalatest.FlatSpec
import org.scalatest.mockito.MockitoSugar
import ru.wobot.crawl.{Parser, SuccessParsed}

class ParseJobSpec extends FlatSpec with MockitoSugar {
  implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val service = mock[SourceProvider]
  when(service.getSource()).thenReturn(FetchDataSets.getFetchDataSet(env))

  val parser = mock[Parser](withSettings().serializable(SerializableMode.ACROSS_CLASSLOADERS))
  when(parser.isUriMatch(any[String])).thenReturn(true)
  when(parser.parse(any[String], any[String])).thenReturn(new SuccessParsed[String]("", ""))

  val job = new ParseJob(service, List[Parser](parser))

  behavior of "When parseJob created and executed"

  it should "invoke the SourceProvider.getSource method" in {
    verify(service, only()).getSource()
  }

//  it should "invoke parsers" in {
//    verify(parser, times(5)).parse("http://localhost1", "data")
//    verify(parser, times(1)).parse("http://localhost2", "data")
//  }
}