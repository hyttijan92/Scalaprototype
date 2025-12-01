package scalatestapplication.services

import com.gluonhq.attach.storage.StorageService
import com.gluonhq.attach.util.Services
import scalatestapplication.models.Quote
import upickle.default.*

object StorageDataService {
  
  protected val ROOT_DIR: os.Path = os.Path(Services.get(classOf[StorageService]).flatMap(_.getPrivateStorage).orElseThrow())
  
  def saveQuotesToFile(quoteList: Array[Quote]): Unit =
    os.write.over(ROOT_DIR/"quotes.json", write(quoteList))

  def getQuotesFromStorage(): Array[Quote] = {
    if (!os.exists(ROOT_DIR / "quotes.json")) {
      os.write(ROOT_DIR / "quotes.json", write(Array[Quote]()))
    }
    read[Array[Quote]](os.read(ROOT_DIR / "quotes.json"))
  }  

}
