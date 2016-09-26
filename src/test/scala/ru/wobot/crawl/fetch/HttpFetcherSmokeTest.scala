package ru.wobot.crawl.fetch

import org.scalatest.FlatSpec
import org.scalatest.mockito.MockitoSugar
import ru.wobot.crawl.fetch.FetcherFactory.HttpFetcher

class HttpFetcherSmokeTest extends FlatSpec with MockitoSugar {

  import org.scalatest.Matchers._
  import ru.wobot.crawl.SuccessFetched

  import scala.concurrent.Await
  import scala.concurrent.duration.Duration

  behavior of "When make request to https://ya.ru"

  val response = Await.result(new HttpFetcher().fetch("https://ya.ru"), Duration.Inf)

  it should "be not null" in {
    response should not be null
  }

  it should "be match as a SuccessFetched" in {
    response should matchPattern { case SuccessFetched("https://ya.ru", _, _, _) => }
  }
}
