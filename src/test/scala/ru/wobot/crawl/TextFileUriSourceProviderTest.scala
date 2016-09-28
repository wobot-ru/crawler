package ru.wobot.crawl


import org.scalatest.FlatSpec
import org.scalatest.mockito.MockitoSugar
import ru.wobot.crawl.fetch.FetchSource
import ru.wobot.crawl.fetch.FetchSource.TextFileUriSourceProvider

class TextFileUriSourceProviderTest extends FlatSpec with MockitoSugar {

  import org.apache.flink.contrib.streaming.scala.utils._
  import org.apache.flink.streaming.api.scala._
  import org.scalatest.Matchers._

  val seedsPath = "file:///" + getClass.getResource("/ru/wobot/crawl/seeds.txt").getPath
  val param = Map(FetchSource.FETCH_IN_TXT_PATH -> seedsPath)
  implicit val env = StreamExecutionEnvironment.createLocalEnvironment()
  val provider = new TextFileUriSourceProvider(param)
  val source = provider.getSource()

  behavior of "When create provider with local path and when getSource"

  it should "read all 12 uris" in {
    val total = source.collect().count(_ => true)
    total shouldEqual 12
  }
}