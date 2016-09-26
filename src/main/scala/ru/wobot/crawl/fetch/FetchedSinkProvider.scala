package ru.wobot.crawl.fetch

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import ru.wobot.crawl.fetch.FetchJob.FetchedSinksProvider


object FetchedSinkProvider {
  def fromParam(param: Map[String, String])(implicit env: StreamExecutionEnvironment): FetchedSinksProvider = {
    () => List()
  }
}

