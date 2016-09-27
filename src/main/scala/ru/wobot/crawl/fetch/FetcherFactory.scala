package ru.wobot.crawl.fetch

import ru.wobot.crawl.FailureFetched


object FetcherFactory {

  import ru.wobot.crawl.Fetcher
  import ru.wobot.crawl.fetch.FetchJob.FetcherFactory

  import scala.concurrent.ExecutionContext.Implicits.global

  def fromParam(param: Map[String, String]): FetcherFactory = {
    () => List(new HttpFetcher)
  }

  class HttpFetcher extends Fetcher {

    import ru.wobot.crawl.net.http
    import ru.wobot.crawl.{Fetched, SuccessFetched, URI}

    import scala.concurrent.Future

    def fetch(uri: URI): Future[Fetched] = http.request(uri)
      .map(data => SuccessFetched(uri, System.nanoTime, Map.empty, data))
      .recover { case (value: Throwable) => FailureFetched(uri, System.nanoTime, Map.empty, value.getMessage) }

    def canFetch(uri: URI) = {
      val u = uri.toLowerCase
      u.startsWith("http://") || u.startsWith("https://")
    }
  }

}