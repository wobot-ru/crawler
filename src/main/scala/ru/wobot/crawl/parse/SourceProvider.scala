package ru.wobot.crawl.parse

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.{Fetch, SuccessFetch}

/**
  * Created by kviz on 9/14/2016.
  */
trait SourceProvider {
  def getSource(): DataStream[Fetch]
}

//case class LocalSourceProvider(env: StreamExecutionEnvironment) extends SourceProvider {
//  override def getSource(): DataStream[Fetch] = {
//    FetchDataSets.getFetchDataSet(env)
//  }
//}