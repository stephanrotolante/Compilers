
public class SpecialSymbols {

	String[] Symbols;
	
	public SpecialSymbols() {
		
		Symbols = new String[] {"+","-","*","/","<",">","=",";",",", "[", "]"};
	}
	
	
	
	public boolean isItASpecialSymbol(String word) {
		boolean EqualOrNot = false;
		
		for(int i = 0; i < Symbols.length; i++) {
			
			if(word.equals(Symbols[i])) {
				
				EqualOrNot = true;
			}
		}
		
		return EqualOrNot;
	}
}
