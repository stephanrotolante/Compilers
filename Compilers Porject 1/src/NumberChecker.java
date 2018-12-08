
public class NumberChecker {

	String[] NumberTable;
	
	public NumberChecker() {
		
		NumberTable = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	}
	
	public boolean isStringANumber(String word) {
		
		int counter = 0;
		
		String[] brokenLine = word.split("");
		
		for(int i = 0; i < brokenLine.length; i++) {
			
			if( isItANumber(brokenLine[i]) ) {
				counter = counter + 1;
			} 
		}
		
				boolean EqualOrNot = false;
				
				if(counter < brokenLine.length) {
					
				} else {
					EqualOrNot = true;
				}
				
				return EqualOrNot;
	 
	 	
		
	}
	
	
	
	public boolean isItANumber(String word) {
		
		boolean EqualOrNot = false;
		
		for(int i = 0; i < NumberTable.length; i++) {
			
			if(word.equals(NumberTable[i])) {
				
				EqualOrNot = true;
			}
		}
		
		return EqualOrNot;
	
	}
}
