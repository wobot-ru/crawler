package ru.wobot.crawl.fetch

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.fetch.FetchJob.UriSourceProvider


object UriSourceProvider {
  def fromParam(param: Map[String, String])(implicit env: StreamExecutionEnvironment): UriSourceProvider = {
    () => new TextFileUriSourceProvider(param)(env).getSource()
  }

  class TextFileUriSourceProvider(params: Map[String, String])(implicit env: StreamExecutionEnvironment) {

    import UriSourceProvider.CLI_CONST.URI_PATH
    import org.apache.flink.api.scala._
    import ru.wobot.crawl.Uri

    val path = params(URI_PATH)

    def getSource(): DataStream[Uri] = {
      env.readTextFile(path, "UTF-8").map(x => x)
    }
  }

  object CLI_CONST {
    val URI_PATH = "uri-path"
  }

}
