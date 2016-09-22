package ru.wobot.crawl.fetch

import org.apache.flink.api.common.functions.FlatMapFunction
import ru.wobot.crawl.{Fetched, Fetcher, _}

class FetchMapFunction(fetchers: List[Fetcher]) extends FlatMapFunction[URI, Fetched] {

  import org.apache.flink.util.Collector

  import scala.concurrent.Await
  import scala.concurrent.duration.Duration
  import scala.util.control.Breaks._

  override def flatMap(uri: URI, out: Collector[Fetched]): Unit = {
    breakable {
      for (f <- fetchers) {
        if (f.canFetch(uri)) {
          val resp = Await.result(f.fetch(uri), Duration.Inf)
          out.collect(resp)
          break
        }
      }
    }
  }
}
