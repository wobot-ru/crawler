package ru.wobot.crawl.fetch

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.{Fetched, _}

import scala.collection.JavaConversions._

class FetchJob(source: => DataStream[Uri], sinks: => List[SinkFunction[Fetched]], fetchers: => List[Fetcher]) {
  def getOutput(): DataStream[Fetched] = {
    val output = source.flatMap(new FetchMapFunction[Uri](fetchers))
    for (s <- sinks) {
      output.addSink(s)
    }
    output
  }
}

object FetchJob {
  type UriSourceProvider = () => DataStream[Uri]
  type FetchedSinksProvider = () => List[SinkFunction[Fetched]]
  type FetcherFactory = () => List[Fetcher]


  def main(args: Array[String]) {
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val p: ParameterTool = ParameterTool.fromArgs(args)
    val params: Map[String, String] = p.toMap.toMap

    val stream: UriSourceProvider = UriSourceProvider.fromParam(params)
    val param: FetchedSinksProvider = FetchedSinkProvider.fromParam(params)
    val job: FetchJob = FetchJob(stream, param, FetcherFactory.fromParam(params))
    job.getOutput().print()
    //    val seeds: DataStream[Uri] = env.fromElements("https://twitter.com", "https://facebook.com", "http://facebook.com", "http://vk.com", "http://production.wobot.ru", "http://wobot.ru", "http://www.wobot.ru", "https://mail.ru", "https://vk.com", "http://www.ya.ru", "https://ya.ru", "http://mail.ru")
    //    seeds.writeAsCsv("C:\\src\\focus\\src\\test\\resources\\ru\\wobot\\crawl\\seeds.csv", WriteMode.OVERWRITE).setParallelism(1)
    //    seeds.writeAsText("C:\\src\\focus\\src\\test\\resources\\ru\\wobot\\crawl\\seeds.txt", WriteMode.OVERWRITE).setParallelism(1)
    env.execute("FetchJob")
  }

  def apply(uriProv: => UriSourceProvider, sinksProv: => FetchedSinksProvider, factory: => FetcherFactory): FetchJob =
    new FetchJob(uriProv(), sinksProv(), factory())

  def apply(source: DataStream[Uri], sinks: List[SinkFunction[Fetched]], fetchers: List[Fetcher]): FetchJob = new FetchJob(source, sinks, fetchers)
}





