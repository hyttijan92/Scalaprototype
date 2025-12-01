package scalatestapplication.controllers

import scalatestapplication.models.Quote

abstract class BaseController {
  var quoteList: Array[Quote] = null
}
