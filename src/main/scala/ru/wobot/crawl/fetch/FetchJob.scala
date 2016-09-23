package ru.wobot.crawl.fetch

import java.util

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.{Fetched, _}

class FetchJob(source: DataStream[Uri], fetchers: List[Fetcher]) {
  def getOutput(): DataStream[Fetched] = source.flatMap(new FetchMapFunction[Uri](fetchers))
}

object FetchJob {
  def apply(source: DataStream[Uri], fetchers: List[Fetcher]): FetchJob = new FetchJob(source, fetchers)

  def apply(p: UriSourceProvider, fetchers: List[Fetcher]): FetchJob = new FetchJob(p.getSource, fetchers)

  def main(args: Array[String]) {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val params: ParameterTool = ParameterTool.fromArgs(args)
    val toMap: util.Map[String, String] = params.toMap

    //    val seeds: DataStream[Uri] = env.fromElements("https://twitter.com", "https://facebook.com", "http://facebook.com", "http://vk.com", "http://production.wobot.ru", "http://wobot.ru", "http://www.wobot.ru", "https://mail.ru", "https://vk.com", "http://www.ya.ru", "https://ya.ru", "http://mail.ru")
    //    seeds.writeAsCsv("C:\\src\\focus\\src\\test\\resources\\ru\\wobot\\crawl\\seeds.csv", WriteMode.OVERWRITE).setParallelism(1)
    //    seeds.writeAsText("C:\\src\\focus\\src\\test\\resources\\ru\\wobot\\crawl\\seeds.txt", WriteMode.OVERWRITE).setParallelism(1)
    env.execute("FetchJob")
  }
}





