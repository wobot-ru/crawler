package ru.wobot.crawl.parse

import java.util.Properties

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.TypeInformationSerializationSchema
import ru.wobot.crawl._

import scala.collection.JavaConversions._

class ParseJob(source: => DataStream[Fetched], parsers: => List[Parser]) {
  def getOutput(): DataStream[Parsed] = source.flatMap(new ParseFlatMapFunction(parsers))
}

object ParseJob {
  type Input = () => DataStream[Fetched]
  type ParserFactory = () => List[Parser]

  def main(args: Array[String]): Unit = {
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
    val props = ParameterTool.fromArgs(args).toMap.toMap
    val parseIn = ParseJob.Input.fromMap(props)
    val parserFactory: ParserFactory = () => List[Parser](new FbSearchParser)
    val parseJob: ParseJob = ParseJob(parseIn, parserFactory)
    parseJob.getOutput().print()
    env.execute("ParseJob")
  }

  def apply(src: => Input, p: => ParserFactory): ParseJob = new ParseJob(src(), p())

  object Input {
    final val KAFKA_BROKER = "parse.in.kafka.bootstrap.servers"
    final val KAFKA_TOPIC = "parse.in.kafka.topic"
    final val KAFKA_OFFSET_RESET = "parse.in.kafka.auto.offset.reset"
    final val KAFKA_GROUP_ID = "parse.in.kafka.group.id"

    def fromMap(map: Map[String, String])(implicit env: StreamExecutionEnvironment): Input = {
      def requiredKey(k: String) = MapUtil.getRequired(map, k)

      () => {
        val cons = new FlinkKafkaConsumer09(
          requiredKey(KAFKA_TOPIC),
          new TypeInformationSerializationSchema(fetchedTI, env.getConfig),
          MapUtil.toPropWithPrefix(map, "parse.in.kafka."))

        env.addSource(cons)
      }
    }
  }

}