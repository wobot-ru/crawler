package ru.wobot
import org.apache.flink.streaming.api.scala._

object StreamJob {

  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.execute("Scala Stream Example")
  }

}
