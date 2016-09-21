package ru.wobot.crawl.fetch

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream
import ru.wobot.crawl.{Fetched, _}

class FetchTransform(source: DataStream[Uri], fetchers: List[Fetcher]) {
  def getOutput(): DataStream[Fetched] = source.flatMap(new FetchMapFunction(fetchers))
}


