package scalatestapplication.services

import scalatestapplication.models.Quote
import sttp.client4.quick.*

object APIService {

  def fetchQuote(): Quote = {
    val response = quickRequest.get(uri"https://dummyjson.com/quotes/random").send()
    val json = ujson.read(response.body)
    val quote = json("quote").str
    val author = json("author").str
    Quote(author, quote)
  }
}
