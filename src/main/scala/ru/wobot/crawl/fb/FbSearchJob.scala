package ru.wobot.crawl.fb

import java.net.URLEncoder

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import ru.wobot.crawl.Parser
import ru.wobot.crawl.fetch.FetchJob._
import ru.wobot.crawl.fetch.{FetchJob, FetchSinks, FetcherFactory}
import ru.wobot.crawl.index.IndexJob
import ru.wobot.crawl.index.IndexJob.IndexSource
import ru.wobot.crawl.parse.ParseJob.{ParseSource, ParserFactory}
import ru.wobot.crawl.parse.{FbSearchParser, ParseJob}

import scala.collection.JavaConversions._

object FbSearchJob {
  final val FB_QUERY = "fb-query"
  final val FB_PAGES = "fb-pages"
  final val FB_PAGES_DEF = 15
  final val FB_HEIGHT = "fb-height"
  final val FB_HEIGHT_DEF = 50000

  def main(args: Array[String]) {
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val params: ParameterTool = ParameterTool.fromArgs(args)
    val props = params.toMap.toMap

    val fetchSrc: FetchSource = getFetchSrcFromQueryFile(params.getRequired(FB_QUERY), params.getInt(FB_PAGES, FB_PAGES_DEF), params.getInt(FB_HEIGHT, FB_HEIGHT_DEF))
    val fetchJob = FetchJob(fetchSrc, FetchSinks.fromParam(props), FetcherFactory.fromParam(props))

    val parseSrc: ParseSource = () => fetchJob.getOutput
    val parserFactory: ParserFactory = () => List[Parser](new FbSearchParser)
    val parseJob: ParseJob = ParseJob(parseSrc, parserFactory)

    val indexSrc: IndexSource = () => parseJob.getOutput()
    val indexJob: IndexJob = IndexJob(indexSrc)

    indexJob.getOutput().print()
    env.execute("FbSearchCrawlJob")
  }

  def getFetchSrcFromQueryFile(queryPath: String, pages: Int, height: Int)(implicit env: StreamExecutionEnvironment): FetchSource = {
    () => env.readTextFile(queryPath).map(q => {
      val encode: String = URLEncoder.encode(q, "UTF-8").replace("+","%20")
      s"http://127.0.0.1:8888/facebook/search/$encode/$pages/pages/$height/height"
    })
  }
}
