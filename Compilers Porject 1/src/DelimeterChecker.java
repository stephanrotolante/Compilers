
public class DelimeterChecker {

		String[] DelimeterTable;
		
		public DelimeterChecker() {
			
			DelimeterTable = new String[] {"(" , ")", "{", "}"};
		}
		
		public boolean isItADelimeter(String word) {
			boolean EqualOrNot = false;
					
					for(int i = 0; i < DelimeterTable.length; i++) {
						
						if(word.equals(DelimeterTable[i])) {
							
							EqualOrNot = true;
						}
					}
					
					return EqualOrNot;
		}
		
}
