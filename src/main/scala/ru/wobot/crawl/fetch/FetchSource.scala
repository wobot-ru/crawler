package ru.wobot.crawl.fetch

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.fetch.FetchJob.Input


object FetchSource {
  val FETCH_IN_TXT_PATH = "fetch.in.txt.path"

  def fromMap(map: Map[String, String])(implicit env: StreamExecutionEnvironment): Input = {
    () => new TextFileUriSourceProvider(map)(env).getSource()
  }

  class TextFileUriSourceProvider(params: Map[String, String])(implicit env: StreamExecutionEnvironment) {
    import org.apache.flink.api.scala._
    import ru.wobot.crawl.Uri

    val path = params(FETCH_IN_TXT_PATH)

    def getSource(): DataStream[Uri] = {
      env.readTextFile(path, "UTF-8").map(x => x)
    }
  }
}
