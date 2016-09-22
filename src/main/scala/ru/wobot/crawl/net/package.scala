package ru.wobot.crawl

package object net {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future
  import scala.io.Source

  def request(uri: URI): Future[String] = Future {
    val f = Source.fromURL(uri)
    try f.mkString
    finally f.close()
  }

  //    lazy val ws = {
  //      import java.io.File
  //      import akka.actor.ActorSystem
  //      import akka.stream.ActorMaterializer
  //      import com.typesafe.config.ConfigFactory
  //      import org.asynchttpclient.AsyncHttpClientConfig
  //      import play.api._
  //      import play.api.libs.ws._
  //      import play.api.libs.ws.ahc.{AhcConfigBuilder, AhcWSClient, AhcWSClientConfig}
  //
  //      val configuration = Configuration.reference ++ Configuration(ConfigFactory.parseString(
  //        """
  //          |ws.followRedirects = true
  //        """.stripMargin))
  //
  //      val environment = Environment(new File("."), this.getClass.getClassLoader, Mode.Prod)
  //      val parser = new WSConfigParser(configuration, environment)
  //      val config = AhcWSClientConfig(wsClientConfig = parser.parse())
  //      val builder = new AhcConfigBuilder(config)
  //      val logging = new AsyncHttpClientConfig.AdditionalChannelInitializer() {
  //        override def initChannel(channel: io.netty.channel.Channel): Unit = {
  //          channel.pipeline.addFirst("log", new io.netty.handler.logging.LoggingHandler("debug"))
  //        }
  //      }
  //      val ahcConfig = builder.configure().setHttpAdditionalChannelInitializer(logging).build()
  //
  //      implicit val system = ActorSystem()
  //      implicit val materializer = ActorMaterializer()
  //      new AhcWSClient(ahcConfig)
  //    }
}
