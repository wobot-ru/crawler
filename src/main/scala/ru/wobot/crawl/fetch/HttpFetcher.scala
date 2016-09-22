package ru.wobot.crawl.fetch

import ru.wobot.crawl.Fetcher

class HttpFetcher extends Fetcher {

  import ru.wobot.crawl._
  import ru.wobot.crawl.net._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  def fetch(uri: URI): Future[Fetched] = request(uri).map(data => SuccessFetched(uri, System.nanoTime, Map.empty, data))

  def canFetch(uri: URI) = {
    val u = uri.toLowerCase
    u.startsWith("http://") || u.startsWith("https://")
  }
}
