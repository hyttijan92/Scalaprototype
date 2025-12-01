package scalatestapplication.models

import upickle.ReadWriter

case class Quote(author: String, quote: String) derives ReadWriter {

  override def toString: String = s"$author: $quote"
}
