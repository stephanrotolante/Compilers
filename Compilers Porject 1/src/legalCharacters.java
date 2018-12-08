
public class legalCharacters {

	String[] listOfCharacters;
	
	public legalCharacters() {
		listOfCharacters = new String[] {"(" , ")", "{", "}", "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
				,"+","-","*","/","<",">","=",";",",", "[", "]", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" ,""};
		
		}
	
	
	public boolean isItALegalCharacter(String word) {
		boolean EqualOrNot = false;
		
		for(int i = 0; i < listOfCharacters.length; i++) {
			
			if(word.equals(listOfCharacters[i])) {
				
				EqualOrNot = true;
			}
		}
		
		return EqualOrNot;
	}
}
