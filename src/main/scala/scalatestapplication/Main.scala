package scalatestapplication

import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.application.Application
import javafx.stage.Stage
import scala.jdk.FunctionConverters.enrichAsJavaFunction

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
  override def start(primaryStage: Stage): Unit =
    val root = FXMLLoader.load[BorderPane](classOf[Main].getResource("views/mainview.fxml"))
    primaryStage.setScene(new Scene(root, dimensions.getWidth, dimensions.getHeight))
    primaryStage.setTitle("Main view")
    primaryStage.show()

  private def postInit(scene: Scene): Unit =
    Swatch.AMBER.assignTo(scene)
    scene.getWindow.setWidth(dimensions.getWidth)
    scene.getWindow.setHeight(dimensions.getHeight)
