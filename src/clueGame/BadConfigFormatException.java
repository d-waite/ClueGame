// Authors: David Waite & Dillinger Day
package clueGame;
import java.io.*;

public class BadConfigFormatException extends Exception {
	private String message;
	
	public BadConfigFormatException() {
		super("File has bad configuration format.");
		errorLogFile(message);
		
	}
	
	public BadConfigFormatException(String message) {
		super(message);
		this.message = message;
		errorLogFile(message);
	}
	
	private void errorLogFile(String message) {
		//Writes the error message to a text file
		try {
			FileWriter writer = new FileWriter("errorLog.txt");
			writer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
