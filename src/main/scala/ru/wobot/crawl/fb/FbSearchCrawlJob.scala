package ru.wobot.crawl.fb

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import ru.wobot.crawl.{Parsed, Parser}
import ru.wobot.crawl.fetch.{FetchJob, FetchedSinkProvider, FetcherFactory, UriSourceProvider}
import ru.wobot.crawl.parse.ParseJob.{ParserFactory, SourceProvider}
import ru.wobot.crawl.parse.{FbSearchParser, ParseJob}

import scala.collection.JavaConversions._


object FbSearchCrawlJob {

  def main(args: Array[String]) {
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val params = ParameterTool.fromArgs(args).toMap.toMap
    val fetchJob = FetchJob(UriSourceProvider.fromParam(params), FetchedSinkProvider.fromParam(params), FetcherFactory.fromParam(params))
    //fetchJob.getOutput().print()
    val parseSrc: SourceProvider = () => fetchJob.getOutput

    val parserFactory: ParserFactory = () => List[Parser](new FbSearchParser)
    val parseJob: ParseJob = ParseJob(parseSrc, parserFactory)
    val output: DataStream[Parsed] = parseJob.getOutput()
    output.print()

    env.execute("FbSearchCrawlJob")
  }

}
