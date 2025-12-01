package scalatestapplication.controllers

import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Scene
import javafx.scene.control.{Button, Label}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import scalatestapplication.Main.dimensions
import scalatestapplication.Main
import scalatestapplication.models.Quote
import scalatestapplication.services.{APIService, StorageDataService}


final class MainController extends BaseController:
  @FXML private var quoteLabel: Label = _
  @FXML private var getQuoteButton: Button = _
  @FXML private var saveQuoteButton: Button = _
  @FXML private var showQuotesButton: Button = _
  @FXML private var topPane: BorderPane = _
  @FXML private var imageView: ImageView = _
  private var currentQuote: Quote = null

  def initialize(): Unit = {
    imageView.setImage(new Image(this.getClass.getResourceAsStream("../images/monument.jpg")))
    topPane.setCenter(imageView)
    getQuoteButton.setOnAction { (_: ActionEvent) =>
      currentQuote = APIService.fetchQuote()
      quoteLabel.setText(s"${currentQuote.quote}\n- ${currentQuote.author}")
      quoteLabel.setWrapText(true)
      saveQuoteButton.setDisable(false)
    }

    saveQuoteButton.setOnAction( (_: ActionEvent) =>
      quoteList = StorageDataService.getQuotesFromStorage() :+ currentQuote
      StorageDataService.saveQuotesToFile(quoteList)
      saveQuoteButton.setDisable(true)
    )
    showQuotesButton.setOnAction( (_: ActionEvent) =>
      val root = FXMLLoader.load[BorderPane](classOf[Main].getResource("views/savedquotesview.fxml"))
      val stage = Stage()
      stage.setTitle("Saved quotes")
      stage.setScene(new Scene(root, dimensions.getWidth, dimensions.getHeight))
      stage.show()
    )
  }

