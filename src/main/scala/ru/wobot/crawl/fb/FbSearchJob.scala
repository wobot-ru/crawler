package ru.wobot.crawl.fb

import java.net.URLEncoder

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala._
import ru.wobot.crawl.{FailureFetched, Fetched, Parser}
import ru.wobot.crawl.fetch.FetchJob.{Input=>FetchInput}
import ru.wobot.crawl.fetch.{FetchJob, FetchSinks, FetcherFactory}
import ru.wobot.crawl.index.IndexJob
import ru.wobot.crawl.index.IndexJob.IndexSource
import ru.wobot.crawl.parse.ParseJob.{Input=>ParseInput, ParserFactory}
import ru.wobot.crawl.parse.{FbSearchParser, ParseJob}

import scala.collection.JavaConversions._

object FbSearchJob {
  final val FB_QUERY = "fetch.in.query.path"
  final val FB_PAGES = "fetcher.fb.pages"
  final val FB_PAGES_DEF = 15
  final val FB_HEIGHT = "fetcher.fb.height"
  final val FB_HEIGHT_DEF = 50000

  def main(args: Array[String]) {
    implicit val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val params: ParameterTool = ParameterTool.fromArgs(args)
    val props = params.toMap.toMap

    val fetchIn: FetchInput = getFetchSrcFromQueryFile(params.getRequired(FB_QUERY), params.getInt(FB_PAGES, FB_PAGES_DEF), params.getInt(FB_HEIGHT, FB_HEIGHT_DEF))
    val fetchJob = FetchJob(fetchIn, FetchSinks.fromMap(props), FetcherFactory.fromMap(props))

    val failed: DataStream[FailureFetched] = fetchJob.getOutput
      .filter((fetched: Fetched) => fetched match {
        case (t: FailureFetched) => true
        case _ => false
      }).map(_.asInstanceOf[FailureFetched])

//    failed.writeAsText("file:///c:\\tmp\\failed.txt", WriteMode.OVERWRITE).setParallelism(1)
    failed.print()

    val parseIn: ParseInput = ParseJob.Input.fromMap(props)
    val parserFactory: ParserFactory = () => List[Parser](new FbSearchParser)
    val parseJob: ParseJob = ParseJob(parseIn, parserFactory)

    val indexSrc: IndexSource = () => parseJob.getOutput()
    val indexJob: IndexJob = IndexJob(indexSrc)

    indexJob.getOutput().print()
    //indexJob.getOutput().writeAsText("file:///c:\\tmp\\posts.txt", WriteMode.OVERWRITE).setParallelism(1)

    env.execute("FbSearchCrawlJob")
  }

  def getFetchSrcFromQueryFile(queryPath: String, pages: Int, height: Int)(implicit env: StreamExecutionEnvironment): FetchInput = {
    () => env.readTextFile(queryPath).map(q => {
      val encode: String = URLEncoder.encode(q, "UTF-8").replace("+","%20")
      s"http://127.0.0.1:8888/facebook/search/$encode/$pages/pages/$height/height"
    })
  }
}
