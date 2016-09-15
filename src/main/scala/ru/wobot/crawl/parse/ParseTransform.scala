package ru.wobot.crawl.parse

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream
import ru.wobot.crawl.{Fetch, Parsed, Parser}

class ParseTransform(source: DataStream[Fetch], parsers: List[Parser]) {
  def getOutput(): DataStream[Parsed] = source.flatMap(new ParseFlatMapFunction(parsers))
}