package xbis.mouzou.TReSA_GUI;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import xbis.mouzou.TReSA_Lucene.LuceneTester;

public class Delete {

	public static void deleteWindow(Stage stage) {
		
		Stage deleteWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		deleteWin.setMinHeight(700);
		deleteWin.setMinWidth(900);   
		
		LuceneTester tester = new LuceneTester(); 
		
		VBox mainVBox = new VBox();
		
		VBox deleteVBox = new VBox();
		
		HBox deleteHBox = new HBox();
		
		HBox buttonHBox = new HBox();
		
		ComboBox<String> resultNumCombo = new ComboBox<String>();
        
        resultNumCombo.getItems().addAll("10", "20", "30", "50", "70", "100", "120");
        
        resultNumCombo.getSelectionModel().select(2);
        
		Label deleteDocLabel = new Label("Delete Document from Index");
		deleteDocLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 28));

        //places
        Label deleteLabel = new Label("Query document to delete");
        deleteLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField deleteField = new TextField();
        deleteField.setPromptText("Enter term");
        deleteField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        //buttons
        Button deleteButton = new Button("Delete");
        deleteButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        deleteButton.setPrefSize(120.0, 60.0);
        
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        backButton.setPrefSize(120.0, 60.0);
        
        stackPane.getChildren().addAll(mainVBox);
        deleteVBox.getChildren().addAll(deleteLabel, deleteHBox);
        deleteHBox.getChildren().addAll(deleteField, resultNumCombo);
        buttonHBox.getChildren().addAll(deleteButton, backButton);
        mainVBox.getChildren().addAll(deleteDocLabel, deleteVBox, buttonHBox);
        
        mainVBox.setSpacing(30);
        deleteVBox.setSpacing(10);
        buttonHBox.setSpacing(20);
        deleteHBox.setSpacing(20);
        
        mainVBox.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);
        deleteHBox.setAlignment(Pos.CENTER);
        
        StackPane.setMargin(mainVBox, new Insets(20, 20, 20, 20));
        
        resultNumCombo.setStyle("-fx-font-size: 16");
        
        deleteHBox.setHgrow(deleteField, Priority.ALWAYS);
        deleteHBox.setHgrow(resultNumCombo, Priority.NEVER);
        
        
        deleteWin.setTitle("Delete article");
        deleteWin.setScene(scene);
        deleteWin.show();
        
        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				String term = deleteField.getText();
				
				if(term.isBlank()) {
					Alert alert = new Alert(AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(deleteWin);
                    alert.setTitle("Delete Error");
                    alert.setHeaderText("Empty Bar");
                    alert.setContentText("Enter a term to query documents to delete");
                    alert.showAndWait();
				}
				else {
					String result = resultNumCombo.getValue();
		        	int num = Integer.parseInt(result);
					boolean delete = false;
					
					try {
						delete = tester.deleteFile(term, num);
					} catch (ParseException | IOException e) {
						e.printStackTrace();
					}
					
					if(delete == true) {
						Alert alert = new Alert(AlertType.INFORMATION);
	                    alert.initModality(Modality.APPLICATION_MODAL);
	                    alert.initOwner(deleteWin);
	                    alert.setTitle("Delete Success");
	                    alert.setHeaderText("Success");
	                    alert.setContentText("Term deleted successfully from index");
	                    alert.showAndWait();
					}
					else {
						Alert alert = new Alert(AlertType.ERROR);
	                    alert.initModality(Modality.APPLICATION_MODAL);
	                    alert.initOwner(deleteWin);
	                    alert.setTitle("Delete Error");
	                    alert.setHeaderText("Index error");
	                    alert.setContentText("Error deleting term from index");
	                    alert.showAndWait();
					}
				}
			}
        });
        
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Search.newSearchWindow(stage);
				deleteWin.close();	
			}
        });
		
	}

	
}
