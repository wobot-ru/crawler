package ru.wobot.crawl.parse

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.util.Collector
import ru.wobot.crawl.{Fetched, Parsed, Parser, SuccessFetched}

class ParseFlatMapFunction(parsers: List[Parser]) extends FlatMapFunction[Fetched, Parsed] {
  override def flatMap(fetch: Fetched, out: Collector[Parsed]): Unit = {
    fetch match {
      case f: SuccessFetched[String] => {
        for (parser <- parsers) {
          if (parser.isUriMatch(fetch.uri)) {
            out.collect(parser.parse(fetch.uri, f.content))
          }
        }
      }
      case _ => ()
    }
  }
}
