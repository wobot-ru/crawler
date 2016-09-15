package ru.wobot.crawl

import org.apache.flink.streaming.api.scala.DataStream

trait SourceProvider {
  def getSource[T](): DataStream[T]
}
