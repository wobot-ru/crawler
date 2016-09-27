package ru.wobot.crawl.fb

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import ru.wobot.crawl.Parser
import ru.wobot.crawl.fetch.{FetchJob, FetchSinks, FetcherFactory, FetchSource}
import ru.wobot.crawl.index.IndexJob
import ru.wobot.crawl.index.IndexJob.IndexSource
import ru.wobot.crawl.parse.ParseJob.{ParserFactory, ParseSource}
import ru.wobot.crawl.parse.{FbSearchParser, ParseJob}

import scala.collection.JavaConversions._


object FbSearchCrawlJob {

  def main(args: Array[String]) {
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val params = ParameterTool.fromArgs(args).toMap.toMap
    val fetchJob = FetchJob(FetchSource.fromParam(params), FetchSinks.fromParam(params), FetcherFactory.fromParam(params))

    val parseSrc: ParseSource = () => fetchJob.getOutput
    val parserFactory: ParserFactory = () => List[Parser](new FbSearchParser)
    val parseJob: ParseJob = ParseJob(parseSrc, parserFactory)

    val indexSrc: IndexSource = () => parseJob.getOutput()
    val indexJob: IndexJob = IndexJob(indexSrc)

    indexJob.getOutput().print()

    env.execute("FbSearchCrawlJob")
  }

}
