package primeNumberClient;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
	//IO Streams
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	
	public void start(Stage primaryStage) {
		//Text Field for input
		BorderPane textFieldPane = new BorderPane();
		textFieldPane.setPadding(new Insets(5,5,5,5));
		textFieldPane.setStyle("-fx-border-color: green");
		textFieldPane.setLeft(new Label("Enter an integer: "));
		
		TextField textInputbox = new TextField();
		textInputbox.setAlignment(Pos.BASELINE_RIGHT);
		textFieldPane.setCenter(textInputbox);
		
		//Display contents
		BorderPane mainPane = new BorderPane();
		TextArea contents = new TextArea();
		mainPane.setCenter(new ScrollPane(contents));
		mainPane.setTop(textFieldPane);
		
		//Create scene
		Scene scene = new Scene(mainPane, 450, 200);
		primaryStage.setTitle("Prime Number App - Client");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		textInputbox.setOnAction(e -> {
			try {
				//Get integer from the text field
				long testNumber = Long.parseLong(textInputbox.getText().trim());
				
				//Send testNumber to the server
				toServer.writeLong(testNumber);
				toServer.flush();
				
				//Get result from server
				boolean isPrime = fromServer.readBoolean();
				
				//Display results
				contents.appendText("The number " + testNumber + " " + resultText(isPrime) + "prime.\n");
			}
			catch (IOException ex) {
				System.err.println(ex);
			}
		});
		
		try {
			Socket socket = new Socket("localhost", 8000);
			
			fromServer = new DataInputStream(socket.getInputStream());
			
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
			contents.appendText(ex.toString() + '\n');
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private String resultText(boolean isPrime) {
		if(isPrime == true) {
			return "is ";
		}
		else {
			return "is not ";
		}
	}
}
