package xbis.mouzou.TReSA_GUI;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.stage.Stage;
import xbis.mouzou.TReSA_Lucene.LuceneTester;

public class SearchResults {

	public static void resultsWindow(Stage stage, String searchQuery, int resultNum, boolean advSearch) {

		Stage resultsWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		resultsWin.setMinHeight(700);
		resultsWin.setMinWidth(900); 
		
		LuceneTester search = new LuceneTester();
        search.tester(searchQuery, resultNum, advSearch);
        
        VBox mainVBox = new VBox();
        
        HBox buttonHBox = new HBox();
 
        //label with the searched query
        Label queryLabel = new Label("Search results for query: '" + searchQuery + "'");
        queryLabel.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        //give a type to the tableView of the returned Class
        //TableView<Result> tableView = new TableView<Result>();
        TableView table = new TableView();
        
        //table.setEditable(false);
        
        TableColumn filePath = new TableColumn("File Path");
        //filePath.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        
        TableColumn score = new TableColumn("Score");
        //score.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        TableColumn text = new TableColumn("Text");
        //text.setCellValueFactory(new PropertyValueFactory<>("text"));
        
        table.getColumns().addAll(filePath, score, text);
        
      //column text alignment
        filePath.setStyle( "-fx-alignment: CENTER;");
        score.setStyle( "-fx-alignment: CENTER;");
        text.setStyle( "-fx-alignment: CENTER;");

        //make columns occupy all table space
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        Button openButton = new Button("Open");
        openButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        openButton.setPrefSize(120.0, 60.0);
        
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        backButton.setPrefSize(120.0, 60.0);
        
        stackPane.getChildren().addAll(mainVBox);
        
        mainVBox.getChildren().addAll(queryLabel, table, buttonHBox);
        
        buttonHBox.getChildren().addAll(openButton, backButton);
        
        mainVBox.setSpacing(20);
        buttonHBox.setSpacing(10);
        
        mainVBox.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);
        
        StackPane.setMargin(mainVBox, new Insets(20, 20, 20, 20));
        
        //tableview
        //file path
        //score
        //highlighted part of the file
        
		resultsWin.setTitle("Search Results");
		resultsWin.setScene(scene);
		resultsWin.show();
		
		openButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//get selected file path in system and open
			}
        });
		
		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(advSearch == true) {
					AdvancedSearch advSearchWin = new AdvancedSearch();
					advSearchWin.advSearchWindow(stage);
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
