package ru.wobot.crawl.parse.smoke

import org.apache.flink.streaming.api.scala._
import org.scalatest.FlatSpec
import ru.wobot.crawl.parse.ParseJob.Input

class ParseInputSmokeTest extends FlatSpec {
  implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
  private val map = Map(
    Input.KAFKA_BROKER -> "localhost:9092",
    Input.KAFKA_TOPIC -> "FB-SEARCH-FETCHED",
    Input.KAFKA_GROUP_ID -> "SMOKE-TESTS",
    Input.KAFKA_OFFSET_RESET -> "earliest"
  )

  private val inputDs = Input.fromMap(map).apply()
  behavior of "When get ParseInput from Map"
  it should "correct read data" in {
    inputDs.print()
    env.execute("ReadData")
  }
}