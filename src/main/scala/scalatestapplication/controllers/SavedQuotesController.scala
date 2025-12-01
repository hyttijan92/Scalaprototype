package scalatestapplication.controllers

import javafx.collections.FXCollections
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ListView}
import javafx.stage.Stage
import scalatestapplication.models.Quote
import scalatestapplication.services.StorageDataService

import scala.jdk.CollectionConverters.*
final class SavedQuotesController extends BaseController:
  @FXML private var savedQuotesListView: ListView[Quote] = _
  @FXML private var deleteButton: Button = _
  @FXML private var backButton: Button = _
  def initialize(): Unit = {
    quoteList = StorageDataService.getQuotesFromStorage()
    savedQuotesListView.setItems(FXCollections.observableList(quoteList.toList.asJava))
    deleteButton.setOnAction { (_: ActionEvent) =>
      val selectedIndex: Integer = savedQuotesListView.getSelectionModel().getSelectedIndex()
      if(selectedIndex != -1) {
        val tempQuoteList = quoteList.toBuffer
        tempQuoteList.remove(selectedIndex)
        quoteList = tempQuoteList.toArray
        StorageDataService.saveQuotesToFile(quoteList)
        savedQuotesListView.setItems(FXCollections.observableList(quoteList.toList.asJava))
      }
    }
    backButton.setOnAction { (_: ActionEvent) =>
      val stage: Stage = backButton.getScene().getWindow().asInstanceOf[Stage]
      stage.close()
    }
  }

  
