package ru.wobot.crawl.net

object http {

  import akka.stream.ActorMaterializer
  import org.asynchttpclient.AsyncHttpClientConfig
  import play.api.libs.concurrent.Execution.Implicits._
  import play.api.libs.ws.WSConfigParser
  import play.api.libs.ws.ahc.{AhcConfigBuilder, AhcWSClient, AhcWSClientConfig}
  import play.api.{Configuration, Environment, Mode}
  import ru.wobot.crawl._

  import scala.concurrent.Future
  import scala.io.Source

  lazy val ws = {
    import java.io.File

    import akka.actor.ActorSystem
    import com.typesafe.config.ConfigFactory

    val configuration = Configuration.reference ++ Configuration(ConfigFactory.parseString(
      """
        |ws.followRedirects = true
      """.stripMargin))

    val environment = Environment(new File("."), this.getClass.getClassLoader, Mode.Prod)
    val parser = new WSConfigParser(configuration, environment)
    val config = AhcWSClientConfig(wsClientConfig = parser.parse())
    val builder = new AhcConfigBuilder(config)
    val logging = new AsyncHttpClientConfig.AdditionalChannelInitializer() {
      override def initChannel(channel: io.netty.channel.Channel): Unit = {
        channel.pipeline.addFirst("log", new io.netty.handler.logging.LoggingHandler("debug"))
      }
    }
    val ahcConfig = builder.configure().setHttpAdditionalChannelInitializer(logging).build()

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    new AhcWSClient(ahcConfig)
  }

  def withRetry[T](retries: Int = 5)(f: => Future[T]): Future[T] =
    f.recover {
      case t: Throwable if (retries > 0) => withRetry[T](retries - 1)(f).asInstanceOf
    }

  def request(uri: URI): Future[String] = Future {
    val f = Source.fromURL(uri)
    try f.mkString
    finally f.close()
  }
}
