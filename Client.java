import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class Client {
	
	@FXML 
	Button openFile,connect;
		
	@FXML 
	ListView<File> fileView;
	
	@FXML
	ListView<String> messageBoard; 
	
	@FXML 
	TextField serverAddress, fileName; 
	
	private File chosenFile = null; 
	private BufferedReader br = null; 
	
	/*
	 * Allows user to traverse directories for doc files
	 * limits the user only allowing doc files
	 */
	
	@FXML 
	private void openFileClicked() {
				
		FileChooser fc = new FileChooser(); 
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DOC Files","*.docx","*.doc"); 
		fc.getExtensionFilters().add(extFilter);
		chosenFile = fc.showOpenDialog(null); 
		
		if(chosenFile != null) { 
			
			System.out.println(chosenFile.getName()); 
			fileView.getItems().add(chosenFile);
			fileName.setText(chosenFile.getName());
			
		}	
	}
	
	public String readContent() {
		
		String content = "";
		
		try {
			content = new String(Files.readAllBytes(Paths.get(chosenFile.getCanonicalPath()))); 
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return content; 
		
	}
	
	@FXML
	private void connectClicked() {
		
		String serverMessage = new String();  
		Socket socket; 
		
		try {
						
			if(!serverAddress.getText().isEmpty() && !fileName.getText().isEmpty()) { 
				
				socket = new Socket(serverAddress.getText(),5520); 
				messageBoard.getItems().add("Connected to: " + serverAddress.getText()); 
				
				String content = chosenFile.getName() + '\0' + String.valueOf(chosenFile.length()) + '\0' + readContent(); 
				PrintWriter pw = new PrintWriter(socket.getOutputStream(),true); 
				pw.println(content); 
				pw.flush(); 
				
				messageBoard.getItems().add("Sent file: " + chosenFile.getName()); 
				messageBoard.getItems().add("File size: " + chosenFile.length());
				messageBoard.getItems().add(content); 
				
				br = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				
				while((serverMessage = br.readLine()) != null) {
					break; 
				}
				
				if(serverMessage.equals("@")) {
					messageBoard.getItems().add("Received: " + serverMessage + " upload SUCCESSFUL");
					socket.close(); 
				}
				else {
					messageBoard.getItems().add("Error with response from server"); 
					socket.close(); 
				}
				
				messageBoard.getItems().add("Disconnected");
			}
			
			else {
				messageBoard.getItems().add("Specify file and/or server"); 
			}
			
			
		}
		
		catch(IOException e) {
			messageBoard.getItems().add("Could not connect to server: " + serverAddress.getText()); 
			e.printStackTrace(); 
		}
		
		
		
	}


}
