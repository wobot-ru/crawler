package ru.wobot.crawl.parse

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.{Fetch, SuccessFetch}

object FetchDataSets {
  def getFetchDataSet(env: StreamExecutionEnvironment): DataStream[Fetch] ={
    val coll: List[Fetch] = List[Fetch](
      SuccessFetch[String]("http://localhost1", 1473860919641l, "data"),
      SuccessFetch[String]("http://localhost2", 1473860919642l, "data"),
      SuccessFetch[String]("http://localhost3", 1473860919643l, "data"),
      SuccessFetch[String]("http://localhost4", 1473860919644l, "data"),
      SuccessFetch[String]("http://localhost6", 1473860919645l, "data")
    )

    env.fromCollection(coll)
  }
}
