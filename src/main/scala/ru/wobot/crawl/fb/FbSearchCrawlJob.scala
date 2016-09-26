package ru.wobot.crawl.fb

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import ru.wobot.crawl.Parser
import ru.wobot.crawl.fetch.{FetchJob, FetchedSinkProvider, FetcherFactory, UriSourceProvider}
import ru.wobot.crawl.parse.ParseJob.{ParserFactory, SourceProvider}
import ru.wobot.crawl.parse.{FbSearchParser, ParseJob}

import scala.collection.JavaConversions._


object FbSearchCrawlJob {

  def main(args: Array[String]) {
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val params = ParameterTool.fromArgs(args).toMap.toMap
    val fetchJob = FetchJob(UriSourceProvider.fromParam(params), FetchedSinkProvider.fromParam(params), FetcherFactory.fromParam(params))
    val parseSrc: SourceProvider = () => fetchJob.getOutput
    val parserFactory: ParserFactory = () => List[Parser](new FbSearchParser)
    ParseJob(parseSrc, parserFactory)
    env.execute("FbSearchCrawlJob")
  }

}
