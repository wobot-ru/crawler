package ru.wobot.crawl.fetch

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.{Fetched, _}

class FetchJob(source: DataStream[URI], fetchers: List[Fetcher]) {
  def getOutput(): DataStream[Fetched] = source.flatMap(new FetchMapFunction(fetchers))
}

object FetchJob {
  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val seeds: DataStream[URI] = env.fromElements("http://wobot.ru", "https://vk.com")
    val fetched: DataStream[Fetched] = new FetchJob(seeds, List(new HttpFetcher)).getOutput()
    fetched.print()
    env.execute("Scala Stream Example")
  }
}

