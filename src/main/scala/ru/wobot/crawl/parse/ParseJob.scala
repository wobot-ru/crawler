package ru.wobot.crawl.parse

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.wobot.crawl.{Fetch, Parser}

class ParseJob(val src: SourceProvider, parsers: List[Parser])(implicit env: StreamExecutionEnvironment) {
  val source: DataStream[Fetch] = src.getSource()

  var parse = source.flatMap(new ParseFlatMapFunction(parsers))
  source.print()
  env.execute()
}



