package ru.wobot.crawl.fetch

import ru.wobot.crawl.FailureFetched

object FetcherFactory {

  import ru.wobot.crawl.Fetcher
  import ru.wobot.crawl.fetch.FetchJob.FetcherFactory

  def fromMap(param: Map[String, String]): FetcherFactory = {
    () => List(new HttpFetcher)
  }

  class HttpFetcher extends Fetcher {

    import ru.wobot.crawl.net.http._
    import ru.wobot.crawl.{Fetched, SuccessFetched, URI}

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future

    def fetch(uri: URI): Future[Fetched] = request(uri)
      .map(data => SuccessFetched(uri, System.nanoTime, Map.empty, data))
      .recover { case (value: Throwable) =>
        //        println("!" * 10)
        //        println(s"FailureFetched: $uri")
        //        println("!" * 10)
        FailureFetched(uri, System.nanoTime, Map.empty, value.getMessage)
      }

    def canFetch(uri: URI) = {
      val u = uri.toLowerCase
      u.startsWith("http://") || u.startsWith("https://")
    }
  }

}