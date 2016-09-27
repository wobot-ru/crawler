package ru.wobot.crawl.fb


import java.net.URLEncoder

import com.mongodb.BasicDBObject
import com.mongodb.hadoop.io.BSONWritable
import com.mongodb.hadoop.mapred.MongoInputFormat
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.hadoop.mapred.HadoopInputFormat
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.hadoop.mapred.JobConf


object FbSearchUriFromMongoJob {
  final val URI_OUT = "uri-out"

  def main(args: Array[String]) {
    val params = ParameterTool.fromArgs(args)
    val env = ExecutionEnvironment.getExecutionEnvironment

    val hdIf = new HadoopInputFormat[BSONWritable, BSONWritable](new MongoInputFormat, classOf[BSONWritable], classOf[BSONWritable], new JobConf)


    // specify connection parameters
    hdIf.getJobConf.set("mongo.input.uri", "mongodb://91.218.113.211:27018/focus_crawl_db.themes")

    val input: DataSet[(BSONWritable, BSONWritable)] = env.createInput(hdIf)
    val query: DataSet[String] = input.map(new MapFunction[(BSONWritable, BSONWritable), String]() {
      @throws[Exception]
      def map(record: (BSONWritable, BSONWritable)): String = {
        val fb: BasicDBObject = record._2.getDoc.get("facebook").asInstanceOf[BasicDBObject]
        val query: String = fb.getString("query")
        query
      }
    })

    val uri: DataSet[(String, String)] = query
      .map(x => (x, URLEncoder.encode(x, "UTF-8")))
      .map((tuple: (String, String)) => (tuple._1, s"http://127.0.0.1:8888/facebook/search/${tuple._2}/15/pages/100000/height"))

    if (params.has(URI_OUT)) {
      val p: String = params.get(URI_OUT)
      println("-" * 10)
      println(s"Store URI to: $p")
      println("-" * 10)
      uri.map(x => x._2).writeAsText(p, WriteMode.OVERWRITE).setParallelism(1)
    }
    uri.print()

    // execute program
    //env.execute("Flink Scala API Skeleton")
  }
}
