import java.util.ArrayList;

public class Symbol {

		boolean array,fucntion;
		String name,type;
		int scope, subS;
		ArrayList<Symbol> params;
		
		
		public Symbol() {
			
			array = false;
			fucntion    = false;
			name = "";
			type = "";
			scope = 0;
			params = new ArrayList<Symbol>();			
			
		
		}
		
		
		public void addP(Symbol sym) {
			params.add(sym);
		}
		
		public ArrayList<Symbol> getList(){
			return params;
		}
}
