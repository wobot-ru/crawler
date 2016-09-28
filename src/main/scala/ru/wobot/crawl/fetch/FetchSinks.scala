package ru.wobot.crawl.fetch

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09
import org.apache.flink.streaming.util.serialization.TypeInformationSerializationSchema
import ru.wobot.crawl.fetch.FetchJob.FetchSinks
import ru.wobot.crawl.{MapUtil, fetchedTI}

object FetchSinks {

  final val KAFKA_BROKER = "fetch.out.kafka.bootstrap.servers"
  final val KAFKA_TOPIC = "fetch.out.kafka.topic"

  def fromMap(map: Map[String, String])(implicit env: StreamExecutionEnvironment): FetchSinks = () => List(new KafkaFetchSink(map))

  class KafkaFetchSink(map: Map[String, String])(implicit env: StreamExecutionEnvironment)
    extends FlinkKafkaProducer09(
      MapUtil.getRequired(map, KAFKA_TOPIC),
      new TypeInformationSerializationSchema(fetchedTI, env.getConfig), MapUtil.toPropWithPrefix(map, "fetch.out.kafka."))

}

