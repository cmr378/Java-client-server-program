import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Server{
	
	private static BufferedReader br = null; 
	private static PrintWriter pw = null; 
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null; 
	
	
	public static void run() {
		
		String clientMessage = "", contents = ""; 
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
		LocalDateTime now; 
		
		try {
			
			serverSocket = new ServerSocket(5520);
			System.out.println("Server running! \nWaiting for connection...");
			
			while(true) {	
				
				clientSocket = serverSocket.accept();
				br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				now = LocalDateTime.now();
				System.out.println("Connection made: " + dtf.format(now)); 
				System.out.println("Connected to: " + clientSocket.getInetAddress() + " Port: " + clientSocket.getPort());
				while (br.ready() && (clientMessage = br.readLine()) != null) {
					
					if(clientMessage.isEmpty()) {
						break;
					}
					else {
						contents += clientMessage; 
					}	
				}
				
				System.out.println("Contents: " + contents);
				
				if(saveFile(contents)) {
					
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("@");
					pw.flush();
					
				}
				
				else {
					
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("!");
					pw.flush();
					
				}	
			}		
		}
		
		catch(IOException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public static StringBuilder getData(ArrayList<Character> s) {
		
		StringBuilder content = new StringBuilder(s.size());
		
		for(Character ch: s) {
			
			content.append(ch); 
			
		}
		
		return content; 
		
	}
	
	public static boolean createFile(StringBuilder fileName, StringBuilder fileContents) {
		
		System.out.println("Contents received: " + fileContents); 
		
		
		try {
			
			File newFile = new File(fileName.toString()); 
			
			if(!newFile.exists()) {
				
				newFile.createNewFile(); 
				
			}
			
			Files.write(Paths.get(fileName.toString()),fileContents.toString().getBytes()); 
			System.out.println("File saved successfully"); 
			
			return true; 
			
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return false; 

	}
	
	public static boolean saveFile(String s) {
		
		StringBuilder content,fileName,fileSize; 
		ArrayList<Character> charContent = new ArrayList<>(),charName = new ArrayList<>(),charSize = new ArrayList<>(); 
		int counter = 0 ;
		
		for(int i = 0; i < s.length(); i++) {
			
			if(s.charAt(i) == '\0'){
				counter++; 
			}
			
			if(counter == 0) {
				charName.add(s.charAt(i)); 
			}
			
			else if(counter == 1) {
				charSize.add(s.charAt(i)); 
			}
			
			else if(counter == 2) {
				charContent.add(s.charAt(i)); 
			}
			
		}
		
		fileName = new StringBuilder(charName.size()); 
		content = new StringBuilder(charContent.size());
		fileSize = new StringBuilder(charSize.size()); 
		
		
		content = getData(charContent); 
		fileName = getData(charName);
		fileSize = getData(charSize); 
	
		System.out.println("File content: " + content + "\nFile name: " + fileName + "\nFile size: " + fileSize); 
		
		if(createFile(fileName,content)) {
			return true; 
		}
		else {
			return false; 
		}
		 
	}
	

	
	public static void main(String[]args) {	
		run(); 
	}
}
