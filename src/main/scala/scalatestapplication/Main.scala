package scalatestapplication
import com.gluonhq.charm.glisten.control.Alert
import javafx.scene.control.Alert.AlertType
import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.application.AppManager
import com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW
import com.gluonhq.charm.glisten.control.{AppBar, FloatingActionButton}
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.{MaterialDesignIcon, Swatch}
import javafx.event.ActionEvent
import javafx.geometry.{Dimension2D, Pos}
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.VBox
import javafx.application.Application
import javafx.stage.Stage

import scala.jdk.FunctionConverters.enrichAsJavaFunction
import scala.util.chaining.scalaUtilChainingOps
import com.gluonhq.connect.provider.RestClient
import com.gluonhq.connect.provider.DataProvider

object Main:
  def main(args: Array[String]): Unit = Application.launch(classOf[Main], args: _*)

  val DESKTOP_WIDTH: Int = 800
  val DESKTOP_HEIGHT: Int = 640

  lazy val dimensions: Dimension2D =
    if Platform.isDesktop then
      new Dimension2D(DESKTOP_WIDTH, DESKTOP_HEIGHT)
    else
      DisplayService.create
        .map(((ds: DisplayService) => ds.getDefaultDimensions).asJava)
        .orElse(new Dimension2D(DESKTOP_WIDTH, DESKTOP_HEIGHT))

final class Main extends Application:
  import Main._

  private val appManager = AppManager.initialize(postInit)

  override def init(): Unit =
    val imageView = new ImageView(new Image(this.getClass.getResourceAsStream("monument.jpg"))).tap { view =>
      view.setFitHeight(200)
      view.setPreserveRatio(true)
    }
    val root = new View(new VBox(20, imageView, new Label("Push the button to get new quote")).tap { _.setAlignment(Pos.CENTER) }) {
      override protected def updateAppBar(appBar: AppBar): Unit = appBar.setTitleText("Scala 3.3.6 Test application")
    }.tap { view =>
      new FloatingActionButton(MaterialDesignIcon.INFO.text, (_: ActionEvent) =>
        val restClient = RestClient.create().host("https://dummyjson.com").path("quotes/random").method("GET")
        val responseJSON = restClient.createObjectDataReader(classOf[String]).readObject()
        val quoteAndAuthor = parseQuoteAndAuthor(responseJSON)
        val alert = new Alert(AlertType.INFORMATION, quoteAndAuthor.map { case (quote, author) =>
          s"$quote\n- $author"
        }.getOrElse("Could not get quote or author for you, sorry"))
        alert.showAndWait()).tap { _.showOn(view) }
    }
    appManager.addViewFactory(HOME_VIEW, () => root)
    
  def parseQuoteAndAuthor(json: String): Option[(String, String)] =
    val quoteAndAuthorRegex = """"quote":"(.*?)","author":"(.*?)"""".r
    quoteAndAuthorRegex.findFirstMatchIn(json).map { matchResult =>
      (matchResult.group(1), matchResult.group(2))
    }

  override def start(stage: Stage): Unit = appManager.start(stage)
  
  private def postInit(scene: Scene): Unit =
    Swatch.AMBER.assignTo(scene)
    scene.getWindow.setWidth(dimensions.getWidth)
    scene.getWindow.setHeight(dimensions.getHeight)