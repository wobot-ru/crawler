package ru.wobot.crawl

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import ru.wobot.crawl.fetch.{FetchJob, FetchSinks, FetcherFactory, FetchSource}
import ru.wobot.crawl.index.IndexJob
import ru.wobot.crawl.parse.{FbSearchParser, ParseJob}

import scala.collection.JavaConversions._


object CrawlJob {

  def main(args: Array[String]) {
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val params = ParameterTool.fromArgs(args).toMap.toMap

    val fetchJob = FetchJob(FetchSource.fromParam(params), FetchSinks.fromParam(params), FetcherFactory.fromParam(params))
    val parseJob: ParseJob = ParseJob(() => fetchJob.getOutput, () => List[Parser](new FbSearchParser))
    val indexJob: IndexJob = IndexJob(() => parseJob.getOutput())

    indexJob.getOutput()

    env.execute("FbSearchCrawlJob")
  }

}
