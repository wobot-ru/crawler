package ru.wobot.crawl.fetch

import ru.wobot.crawl.{SourceProvider, Uri}

trait UriSourceProvider extends SourceProvider[Uri]

object UriSourceProvider {
  object CLI_CONST {
    val URI_PATH = "uri-path"
  }
}