package ru.wobot.crawl.fetch

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import ru.wobot.crawl.fetch.FetchJob.FetchSinks


object FetchSinks {
  def fromParam(param: Map[String, String])(implicit env: StreamExecutionEnvironment): FetchSinks = () => List()
}

