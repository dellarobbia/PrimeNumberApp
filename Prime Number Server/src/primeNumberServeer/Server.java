package primeNumberServeer;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		//Displays server's actions and status
		TextArea serverLog = new TextArea();
		
		//Create the scene
		Scene scene = new Scene(new ScrollPane(serverLog), 450, 200);
		primaryStage.setTitle("Prime Number App - Server");
		primaryStage.setScene(scene);;
		primaryStage.show();
		
		new Thread( () -> {
			try {
				//Create server socket
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() ->
					serverLog.appendText("Server started at " + new Date() + '\n'));
				
				//Listen for connection request
				Socket socket = serverSocket.accept();
				
				//Create data input/output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
				
				while (true) {
					//Receive number to test from client
					long testNumber = inputFromClient.readLong();
					
					//Test if the number entered is prime
					boolean isPrime = testIfPrime(testNumber);
					
					//Tell client if the number was prime or not
					outputToClient.writeBoolean(isPrime);
					
					Platform.runLater(() -> {
						serverLog.appendText("Number received from client: " + testNumber + '\n');
						serverLog.appendText("Results: " + isPrime + '\n');
					});
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	private boolean testIfPrime(long testNumber) {
		boolean isPrime = true;
		
		if(testNumber <= 1) {
			return false;
		}
		
		for(int i = 2; i < Math.sqrt(testNumber); i++) {
			if (testNumber % i == 0) {
				return false;
			}
		}
		
		return isPrime;
	}
	
}
