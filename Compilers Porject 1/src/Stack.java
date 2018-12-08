import java.util.ArrayList;

public class Stack {
	
		ArrayList<String> stack;
		

		public Stack () {
			
			stack = new ArrayList<String>();
			
			//stack.add("$");
			
		}
		
		
		
		public void pop() {
			if(stack.size() >0) {
				stack.remove(stack.size()-1);
			}
			
		}
		
		public void add(String word) {
			stack.add(word);
		}
		
		public void addZero(String word) {
			stack.add(0, word);
		}
		
		public String get(int index) {
			return stack.get(index);
			
		}
		
		public int size() {
			return stack.size();
		}
		
		public void checkStack() {
			for( int i = 0; i < stack.size(); i++) {
				System.out.println(stack.get(i));
			}
		}
}
