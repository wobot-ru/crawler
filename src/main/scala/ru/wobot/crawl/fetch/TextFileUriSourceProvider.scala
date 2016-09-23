package ru.wobot.crawl.fetch

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

class TextFileUriSourceProvider(params: Map[String, String])(implicit env: StreamExecutionEnvironment) extends UriSourceProvider {

  import UriSourceProvider.CLI_CONST.URI_PATH
  import org.apache.flink.api.scala._
  import ru.wobot.crawl.Uri

  val path = params(URI_PATH)

  def getSource(): DataStream[Uri] = {
    env.readTextFile(path, "UTF-8").map(x => x)
  }
}
