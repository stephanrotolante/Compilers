
public class KeyWordTable {

		String[] KeyWords;
	
		public KeyWordTable() {
		
			KeyWords =  new String[] {"int", "if", "else", "void", "return", "while", "main", "float"};
				
		}
		
		//This will check to see if the word from the text file is equal to the keyword bank
		public boolean isItAKeyWord(String word) {
			
			boolean EqualOrNot = false;
			
			for(int i = 0; i < KeyWords.length; i++) {
				
				if(word.equals(KeyWords[i])) {
					
					EqualOrNot = true;
				}
			}
			
			return EqualOrNot;
		}
}
