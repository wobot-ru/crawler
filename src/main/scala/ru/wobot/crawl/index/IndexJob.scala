package ru.wobot.crawl.index

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.util.Collector
import ru.wobot.crawl._

class IndexJob(source: => DataStream[Parsed]) {
  def getOutput(): DataStream[Document] = source.flatMap((e: Parsed, out: Collector[Document]) => {
    e match {
      case (p: SuccessParsed[List[Document]]) => p.content.foreach(out.collect(_))
      case (p: SuccessParsed[Document]) => out.collect(p.content)
      case _ => println("Unknown ParsedType")
    }
  })
}

object IndexJob {
  type IndexSource = () => DataStream[Parsed]

  def apply(src: => IndexSource): IndexJob = new IndexJob(src())
}
