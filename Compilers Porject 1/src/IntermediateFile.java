import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

//Object class used to create the file writer for the tokens

public class IntermediateFile {

	
		PrintWriter writer;
		
		
		//Constructor For the Intermediate File
		
		public IntermediateFile(String filename) {

						
					
			
						try {
							FileWriter fw = new FileWriter(filename,false);
							
							writer = new PrintWriter(fw);
							
							
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
						
			
			
		}
		
		
		//Writes a new line to the intermediate File
		public void WriteLineToFile(String string) {
			
			writer.println(string);
			
		}
		
		//Writes to the intermediate file
		public void WriteToFile(String string) {
			writer.print(string);
		}
		
		//Flushes the intermediate file
		public void FlushFile() {
			writer.flush();
		}
		
		//Closes the intermediate file 
		public void CloseFile() {
			writer.close();
		} 
}
		