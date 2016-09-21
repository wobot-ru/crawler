package ru.wobot.crawl.fetch

import org.apache.flink.api.common.functions.FlatMapFunction
import ru.wobot.crawl.{Fetched, Fetcher, _}

class FetchMapFunction(fetchers: List[Fetcher]) extends FlatMapFunction[Uri, Fetched] {

  import org.apache.flink.util.Collector
  import scala.util.control.Breaks._

  override def flatMap(uri: Uri, out: Collector[Fetched]): Unit = {
    breakable {
      for (f <- fetchers) {
        if (f.canFetch(uri)) {
          out.collect(f.fetch(uri))
          break
        }
      }
    }
  }
}
