package xbis.mouzou.TReSA_GUI;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import xbis.mouzou.TReSA_Lucene.LuceneTester;
import xbis.mouzou.TReSA_Lucene.Result;

public class SearchResults {
	
	public static ObservableList <Result> data = FXCollections.observableArrayList();

	public static List<Result> results = new ArrayList<>();
	
	public static void resultsWindow(Stage stage, String searchQuery, int resultNum, boolean advSearch, List<Boolean> checkBoxes) {

		Stage resultsWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		resultsWin.setMinHeight(700);
		resultsWin.setMinWidth(900); 
		
		LuceneTester search = new LuceneTester();
        results = search.tester(searchQuery, resultNum, advSearch, checkBoxes);
        
        VBox mainVBox = new VBox();
        
        HBox buttonHBox = new HBox();
 
        //label with the searched query
        Label queryLabel = new Label("Search results for query: '" + searchQuery + "' \t Top: " + resultNum + " results");
        queryLabel.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        TableView<Result> table = new TableView<>();
        

        TableColumn<Result, String> filePath = new TableColumn<>("File Path");
        filePath.setCellValueFactory(new PropertyValueFactory<>("path"));
        filePath.setMinWidth(80);
        
        TableColumn<Result, String> score = new TableColumn<>("Score");
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        TableColumn<Result, String> text = new TableColumn<>("Text");
        text.setCellValueFactory(new PropertyValueFactory<>("fragments"));
        text.setCellFactory(new Callback<TableColumn<Result, String>, TableCell<Result, String>>() {

			@Override
			public TableCell<Result, String> call(TableColumn<Result, String> param) {
				 return new TableCell<Result, String>(){
					 @Override
					 protected void updateItem(String item, boolean empty) {
						 super.updateItem(item, empty);
						 
						 if(item == null || empty) {
							 setText(null);
							 setGraphic(null);
							 setStyle("");
						 }
						 else {
							 WebView webView = new WebView();
							 WebEngine engine = webView.getEngine();
							 
							 webView.setPrefHeight(150);
							 
							 setGraphic(webView); 
							 
							 engine.loadContent(item);
							 
						 }
					 }
				 };
			}        	
        });
        
        table.getColumns().add(filePath);
        table.getColumns().add(score);
        table.getColumns().add(text);
        
        //column text alignment
        filePath.setStyle( "-fx-alignment: CENTER;");
        score.setStyle( "-fx-alignment: CENTER;");
        text.setStyle( "-fx-alignment: CENTER;");

        //make columns occupy all table space
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        data = FXCollections.observableArrayList(results);
        
        table.setItems(data);
        
        Button similarButton = new Button("Similar");
        similarButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        similarButton.setPrefSize(120.0, 60.0);
        
        Button openButton = new Button("Open");
        openButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        openButton.setPrefSize(120.0, 60.0);
        
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        backButton.setPrefSize(120.0, 60.0);
        
        stackPane.getChildren().addAll(mainVBox);
        
        mainVBox.getChildren().addAll(queryLabel, table, buttonHBox);
        
        buttonHBox.getChildren().addAll(similarButton, openButton, backButton);
        
        mainVBox.setSpacing(20);
        buttonHBox.setSpacing(10);
        
        mainVBox.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);
        
        StackPane.setMargin(mainVBox, new Insets(20, 20, 20, 20));
        
		resultsWin.setTitle("Search Results");
		resultsWin.setScene(scene);
		resultsWin.show();
		
		if(results.isEmpty()) {
        	Alert alert = new Alert(AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(resultsWin);
            alert.setTitle("Documents Search Results");
            alert.setHeaderText("No results");
            alert.setContentText("There are no documents matching your query");
            alert.showAndWait();
            
            if(advSearch == true) {
    			AdvancedSearch.advSearchWindow(stage);
    		}
    		else {
    			Search.newSearchWindow(stage);
    		}
            resultsWin.close();
        }
		
		similarButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Result result = table.getSelectionModel().getSelectedItem();
				if(result != null) {
					int docId = result.getDocId();
				
					List<Result> similarResults = new ArrayList<>();
				
					try {
						similarResults = search.moreLikeThis(docId, resultNum);
						
						if(similarResults.isEmpty()) {
							Alert alert = new Alert(AlertType.INFORMATION);
		                    alert.initModality(Modality.APPLICATION_MODAL);
		                    alert.initOwner(resultsWin);
		                    alert.setTitle("Similar Documents");
		                    alert.setHeaderText("No results");
		                    alert.setContentText("There are no documents similar to the selected option");
		                    alert.showAndWait();
						}
						else {
							data = FXCollections.observableArrayList(similarResults);
							
							Path filePath = Paths.get(result.getPath());
							Path fileName = filePath.getFileName();
							String article = fileName.toString();
							queryLabel.setText("Similar Documents to '" + article + "' \t Top: " + resultNum + " results");
					        
							table.setItems(data);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (InvalidTokenOffsetsException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
        });
		
		openButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Result result = table.getSelectionModel().getSelectedItem();
				if(result != null) {
					String path = result.getPath();
					File file = new File(path);
					try {
						if(Desktop.isDesktopSupported()){
							Desktop.getDesktop().open(file);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
        });
		
		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(advSearch == true) {
					AdvancedSearch.advSearchWindow(stage);
				}
				else {
					App mainWin = new App();
					mainWin.start(stage);
				}
				resultsWin.close();	
			}
        });
		
	}
	
}
