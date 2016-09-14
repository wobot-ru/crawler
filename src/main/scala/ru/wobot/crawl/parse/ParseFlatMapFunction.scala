package ru.wobot.crawl.parse

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.util.Collector
import ru.wobot.crawl.{Fetch, Parsed, Parser, SuccessFetch}

class ParseFlatMapFunction(parsers: List[Parser]) extends FlatMapFunction[Fetch, Parsed] {
  override def flatMap(fetch: Fetch, out: Collector[Parsed]): Unit = {
    fetch match {
      case f: SuccessFetch[String] => {
        for (parser <- parsers) {
          if (parser.isUriMatch(fetch.uri)) {


            out.collect(parser.parse(fetch.uri, f.data))
          }
        }
      }
      case _ => ()
    }
  }
}
