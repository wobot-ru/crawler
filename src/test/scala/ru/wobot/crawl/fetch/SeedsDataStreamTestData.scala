package ru.wobot.crawl.fetch

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.Uri

object SeedsDataStreamTestData {
  def getSeeds(implicit env: StreamExecutionEnvironment): DataStream[Uri] = {
    val coll: List[Uri] = List[Uri](
      "http://localhost1",
      "http://localhost2",
      "http://localhost3",
      "http://localhost4",
      "http://localhost6"
    )

    env.fromCollection(coll)
  }
}
