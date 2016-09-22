package ru.wobot.crawl.fetch

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.URI

object SeedsDataStreamTestData {
  def getSeeds(implicit env: StreamExecutionEnvironment): DataStream[URI] = {
    val coll: List[URI] = List[URI](
      "http://localhost1",
      "http://localhost2",
      "http://localhost3",
      "http://localhost4",
      "http://localhost6"
    )

    env.fromCollection(coll)
  }
}
