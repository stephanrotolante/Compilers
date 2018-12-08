
public class Token {

	String type, value;
	
	public Token() {
		type = "";
		value = "";
	}
	
	public Token(String val ) {
		
		value = val;
		type = "";
	}
	
	public Token(String val, String ty ) {
		
		value = val;
		type = ty;
	}
	
	
	
}
