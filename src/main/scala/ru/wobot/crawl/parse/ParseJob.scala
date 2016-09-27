package ru.wobot.crawl.parse

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream
import ru.wobot.crawl.{Fetched, Parsed, Parser}

class ParseJob(source: => DataStream[Fetched], parsers: => List[Parser]) {
  def getOutput(): DataStream[Parsed] = source.flatMap(new ParseFlatMapFunction(parsers))
}

object ParseJob {
  type SourceProvider = () => DataStream[Fetched]
  type ParserFactory = () => List[Parser]

  def main(args: Array[String]): Unit = {

  }

  //def apply(source: => DataStream[Fetched], parsers: => List[Parser]): ParseJob = new ParseJob(source, parsers)

  def apply(src: => SourceProvider, p: => ParserFactory): ParseJob = new ParseJob(src(), p())
}