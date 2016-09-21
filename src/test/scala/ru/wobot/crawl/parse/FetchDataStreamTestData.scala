package ru.wobot.crawl.parse

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.{Fetched, SuccessFetched}

object FetchDataStreamTestData {
  def getFetchDataStream(env: StreamExecutionEnvironment): DataStream[Fetched] = {
    val foo = List[Fetched](
      SuccessFetched[String]("http://localhost1", 1473860919641l, Map.empty, "data"),
      SuccessFetched[String]("http://localhost2", 1473860919642l, Map.empty, "data"),
      SuccessFetched[String]("http://localhost3", 1473860919643l, Map.empty, "data"),
      SuccessFetched[String]("http://localhost4", 1473860919644l, Map.empty, "data"),
      SuccessFetched[String]("http://localhost6", 1473860919645l, Map.empty, "data")
    )

    env.fromCollection(foo)
  }
}
