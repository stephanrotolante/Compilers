import java.io.*; 
import java.util.Scanner;

public class Parser {

	
		Stack codeStack, grammarStack;
		Scanner fileReader;
		boolean Error;
		
	
		public Parser (File  file) throws FileNotFoundException {
		
			Error = false;
			codeStack = new Stack();
			grammarStack = new Stack();
			fileReader = new Scanner(file);
			fillCodeStack();
		}
		//Fills the code stack in the correct order so we can check if it is legal
		public void fillCodeStack() {
			grammarStack.addZero("$");
			while (fileReader.hasNext()) {
			
				codeStack.addZero(fileReader.nextLine());
				
			}
			//Marks the end of the stack
			codeStack.addZero("$");
			
			program();
			
			 
		}
		
		// 1 program -> declaration_list ***********************************
		public void program() {
			grammarStack.add("declaration_list");
			declaration_list();
		}
		// 2 declaration_list  -> declaration declaration_list1 *****************************
		public void declaration_list() {
			grammarStack.pop();
			grammarStack.add("declaration_list1");
			grammarStack.add("declaration");
			declaration();
			declaration_list1();
			
		}
		// 3 declaration_list1 -> declaration declaration_list1 | empty **************************
		public void declaration_list1() {
		grammarStack.pop();
		
		if(codeStack.get(codeStack.size()-1).equals("$")) {
			
			
		} else if(codeStack.get(codeStack.size() -1).equals("int")|| codeStack.get(codeStack.size() -1).equals("float")||codeStack.get(codeStack.size() -1).equals("void") ) {
			
			grammarStack.add("declaration_list1");
			grammarStack.add("declaration");
			declaration();
			declaration_list1();
		} else {
			Error = true;
		}
		}
		
		// 4 declaration -> type_specifier ID M  ************************************************
		public void declaration() {
			grammarStack.pop();
			grammarStack.add("M");
			grammarStack.add("ID");
			grammarStack.add("type_specifier");
			type_specifier();
			//IF no ID then error
			if(codeStack.get(codeStack.size() -1).equals(grammarStack.get(grammarStack.size()-1)) || codeStack.get(codeStack.size() -1).equals("main") ) {
				codeStack.pop();
				grammarStack.pop();
			} else {
				Error = true;
			}
			
			
			//If not first of M detected then error
			if(codeStack.get(codeStack.size() -1).equals("(")|| codeStack.get(codeStack.size() -1).equals("[")|| codeStack.get(codeStack.size() -1).equals(";") ) {
				M();
			} else {
				Error = true;
			}
			
			
		}
		
		
		
		// 5 M -> (params) compound_stmt | X  ***********************************
		public void M() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size() -1).equals("(")) {
				codeStack.pop();
				grammarStack.add("compound_stmt");
				grammarStack.add(")");
				grammarStack.add("params");
				params();
				compound_stmt();
				
			} else if(codeStack.get(codeStack.size() -1).equals("[") || codeStack.get(codeStack.size() -1).equals(";") ) {
				
				grammarStack.add("X");
				X();
			} else {
				Error = true;
			}
			
			
		}
		
		// 6 var_declaration -> type_specifier ID X *** DONEEEEEEEEEEEEEEEEEEEEEEE
		public void var_declaration() {
			grammarStack.pop();
			grammarStack.add("X");
			grammarStack.add("ID");
			grammarStack.add("type_specifier");
			
			type_specifier();
			
			//ID
			if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) ) {
				codeStack.pop();
				grammarStack.pop();
			} else {
				Error = true;
			}  
			
			X();
			
		}
		
		// 7 type_specifier -> int | void | float *********************************************
		public void type_specifier() {
			if(codeStack.get(codeStack.size() -1).equals("int") || codeStack.get(codeStack.size() -1).equals("void") || codeStack.get(codeStack.size() -1).equals("float") ) {
				codeStack.pop();
				grammarStack.pop();
			} else {
				Error = true;
			}
		}
		
		// 8 X -> [Num]; | ; ** **************************************************************************8
		public void X() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size() -1).equals("[") && Error == false) {
				grammarStack.add(";");
				grammarStack.add("]");
				grammarStack.add("NUM");
				grammarStack.add("[");
			} else {
				
				grammarStack.add(";");
				
				
			}
			
			//Checks for first [
			if(codeStack.get(codeStack.size() -1).equals(grammarStack.get(grammarStack.size()-1)) && codeStack.get(codeStack.size() -1).equals("[") && Error == false) {
				codeStack.pop();
				grammarStack.pop();
				
				//checks for NUM
				if(codeStack.get(codeStack.size() -1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false) {
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
				//checks for ]
				if(codeStack.get(codeStack.size() -1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false) {
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
				
				//checks for ;
				if (codeStack.get(codeStack.size() -1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false) {
					codeStack.pop();
					grammarStack.pop();
					
				} else {
					Error = true;
				}
				
				//the ; by itself
			} else {
				if(codeStack.get(codeStack.size() -1).equals(grammarStack.get(grammarStack.size()-1)) ) {
					codeStack.pop();
					grammarStack.pop();
				} 
			}
	
		}
		
		
		// 10 params -> int ID Y param_list1 | float ID Y param_list1 | void W DONEEEEEEEEEEEEEEEEe
		public void params() {
			grammarStack.pop();
			//checks for int
			if(codeStack.get(codeStack.size()-1).equals("int") ) {
				codeStack.pop();
				
				//ADD
				grammarStack.add("param_list1");
				grammarStack.add("Y");
				grammarStack.add("ID");
				
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) ) {
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
				
				Y();
				param_list1();
				
			
			}
			//checks for float
			else if (codeStack.get(codeStack.size()-1).equals("float") && Error == false) {
				codeStack.pop();
				
				//ADD
				grammarStack.add("param_list1");
				grammarStack.add("Y");
				grammarStack.add("ID");
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false) {
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
				Y();
				param_list1();
			}
			//checks for void
			else if (codeStack.get(codeStack.size()-1).equals("void") && Error == false) {
				codeStack.pop();
			
				grammarStack.add("W");
				W();
				
			}
			//ERROR if non are true
			else {
				Error = true;
			}
		}
		
		// 11 W -> empty | ID Y param_list1 DONEEEEEEEEEEEEEEEEEEEEEEEE
		public void W() {
			grammarStack.pop();
			//end
			if(codeStack.get(codeStack.size()-1).equals(")")) {
				codeStack.pop();
				//ID
			} else if(codeStack.get(codeStack.size()-1).equals("ID") && Error == false) {
				codeStack.pop();
			
				
				grammarStack.add("param_list1");
				grammarStack.add("Y");
				Y();
				param_list1();
				
				
			} else {
				Error = true;
			}
			
		}
		

		
		// 12 param_list1 -> , param param_list1 | empty DONEEEEEEEEEEEEEEEEE
		public void param_list1() {
			grammarStack.pop();
			
			//if , is first
			if(codeStack.get(codeStack.size()-1).equals(",") && Error == false) {
				codeStack.pop();
				grammarStack.add("param_list1");
				grammarStack.add("param");
				param();
				param_list1();
				
			} else if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && codeStack.get(codeStack.size()-1).equals(")") && Error == false) {
				grammarStack.pop();
				codeStack.pop();
			} else {
				Error = true;
			}
			
		}
		
		// 13 param -> type-specifier ID Y DONEEEEEEEEEEEE
		public void param() {
			grammarStack.pop();
			grammarStack.add("Y");
			grammarStack.add("ID");
			grammarStack.add("type_specifier");
			type_specifier();
			
			//ID
			if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false) {
				codeStack.pop();
				grammarStack.pop();
			} else {
				Error = true;
			}
			
			Y();
		
		}
		
		
		
		// 14 Y -> empty | [ ] ****************************************************************
		public void Y() {
			grammarStack.pop();
			//empty Y nothing happens
			if(codeStack.get(codeStack.size() -1 ).equals(")") || codeStack.get(codeStack.size() -1 ).equals(",") && Error == false) {
				
			} else if(codeStack.get(codeStack.size() -1 ).equals("[") && Error == false) {
				codeStack.pop();
				grammarStack.add("]");
				
				
				
				//equals ]
				if(codeStack.get(codeStack.size() -1 ).equals(grammarStack.get(grammarStack.size()-1)) && Error == false) {
					grammarStack.pop();
					codeStack.pop();
				} else {
					Error = true;
				}
				
			} else {
				Error = true;
			}
			
		}
		
	
		// 15 compound_stmt    -> { local_declarations statement_list } *************
		public void compound_stmt() {
			grammarStack.pop();
			grammarStack.add("}");
			grammarStack.add("statement_list");
			grammarStack.add("local_declarations");
			grammarStack.add("{");
			if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false) {
				grammarStack.pop();
				codeStack.pop();
				local_declaration();
				statement_list();
			} else {
				Error = true;
			}
		
			
			
		}
		
	
		// 16 local_declaration -> local_declaration1 DONEEEEEEEEEEEEEE
		public void local_declaration() {
			grammarStack.pop();
			grammarStack.add("local_declaration1");
			local_declaration1();
		}
		
		
		
		
		
		
		public boolean varEmptyChecker() {
			boolean empty = false;
			
			if(codeStack.get(codeStack.size() -1).equals("int") || codeStack.get(codeStack.size() -1).equals("void") || codeStack.get(codeStack.size() -1).equals("float")) {
			
				if(codeStack.get(codeStack.size()-2).equals("ID")) {
					
				} else {
					empty = true;
				}
				
			} else {
				empty = true;
			}
			
			
			return empty;
		}
		
		
		
		
		// 17 local_declaration1 -> var-declaration local-declaration' | empty ****************************
		public void local_declaration1() {
			grammarStack.pop();
			grammarStack.add("local_declaration1");
			grammarStack.add("var_declaration");			
			//check if is empty
			if(varEmptyChecker() == false) {
				var_declaration();
				local_declaration1();
			} else {
				grammarStack.pop();
				grammarStack.pop();
			}
			
		}
		
		
		
		
		// 18 statement_list  -> statement_list1 ********** DONEEEEEEEEEEEEEEEEEEEEEEEEe
		public void statement_list() {

			grammarStack.pop();
			grammarStack.add("statement_list1");
			statement_list1();
		}
		
		// 19 statement_list1 -> statement statement_list1 | empty *****************
		public void statement_list1() {
			grammarStack.pop();
			grammarStack.add("statement_list1");
			grammarStack.add("statement");
			if(codeStack.get(codeStack.size() -1).equals("}") || codeStack.get(codeStack.size() -1).equals("else") && Error == false ) {
				
				grammarStack.pop();
				grammarStack.pop();
				grammarStack.pop();
				codeStack.pop();
			} else if (Error == false){
				statement();	
				statement_list1();
			}
			
			
		}
		
		// 20 statement -> expression_stmt | compound_stmt | selection_stmt | iteration_stmt | return_stmt   ***********************************
		public void statement() {

			grammarStack.pop();
			//selection_stmt not done
			if(codeStack.get(codeStack.size()-1).equals("if") && Error == false) {
				codeStack.pop();
				grammarStack.add("selection_stmt");
				selection_stmt();
				
			//iteration_stmt not done
			} else if(codeStack.get(codeStack.size()-1).equals("while") && Error == false){   
				codeStack.pop();
				grammarStack.add("iteration_stmt");
				iteration_stmt();
			//return_stmt not done
			} else if(codeStack.get(codeStack.size()-1).equals("return") && Error == false){
				codeStack.pop();
				grammarStack.add("return_stmt");
				return_stmt();
				//expression_stmt not done
			}	else if(codeStack.get(codeStack.size()-1).equals("(") || codeStack.get(codeStack.size()-1).equals("FLOAT") || codeStack.get(codeStack.size()-1).equals("ID")||codeStack.get(codeStack.size()-1).equals("NUM")|| codeStack.get(codeStack.size()-1).equals(";") && Error == false) {
				grammarStack.add("expression_stmt");
				expression_stmt();
				
			} else if(codeStack.get(codeStack.size()-1).equals("{") && Error == false){
				//codeStack.pop();
				grammarStack.pop();
				grammarStack.add("compound_stmt");
				compound_stmt();
			}else {
			
				Error = true;
			}
		}
		
		// 21 expression_stmt -> expression ; | ; *******************************
		public void expression_stmt() {
			grammarStack.pop();
			// for ;
			if(codeStack.get(codeStack.size()-1).equals(";") && Error == false) {
				codeStack.pop();
			//everything else
			} else if(codeStack.get(codeStack.size()-1).equals("(") || codeStack.get(codeStack.size()-1).equals("ID")||codeStack.get(codeStack.size()-1).equals("NUM") |codeStack.get(codeStack.size()-1).equals("FLOAT") && Error == false) {
				grammarStack.add(";");
				grammarStack.add("expression");
				expression();
							if(grammarStack.get(grammarStack.size()-1).equals(codeStack.get(codeStack.size()-1)) && Error == false) {
								grammarStack.pop();
								codeStack.pop();
							}  else {
								Error = true;
							}
			} else {
				Error = true;
			}
		}
		
		// 22 selection-stmt -> if (expression) statement Z
		public void selection_stmt() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals("(") && Error == false) {
				codeStack.pop();
				grammarStack.add("Z");
				grammarStack.add("statement");
				grammarStack.add(")");
				grammarStack.add("expression");
				expression();
				if(codeStack.get(codeStack.size()-1).equals(")") && Error == false) {
					codeStack.pop();
				} else {
					Error = true;
				}
				statement();
				Z();
				
			} else {
				Error = true;
			}
			
		}
		
		// 23 Z -> empty | else statement
		public void Z() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals("else") && Error == false) {
				codeStack.pop();
				grammarStack.add("statement");
				statement();
			}
		}
		
		// 24 iteration_stmt -> while (expression) statement
		public void iteration_stmt() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals("(") && Error == false) {
				codeStack.pop();
				grammarStack.add("statement");
				grammarStack.add(")");
				grammarStack.add("expression");
				expression();
				if(codeStack.get(codeStack.size()-1).equals(")") && Error == false) {
					codeStack.pop();
				} else {
					Error = true;
				}
				statement();
				
				
			} else {
				Error = true;
			}
		}
		
		// 25 return_stmt -> return expression_stmt
		public void return_stmt() {
			grammarStack.pop();
			grammarStack.add("expression_stmt");
			expression_stmt();
		}
		
		
		
		
		
		// 26 expression -> ID E | (expression) term1 additive-expression1 T | Num term1 additive-expression1 T |  FLOAT term1 additive-expression1 T *****************************
		public void expression() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals(";")) {
				codeStack.pop();
			}
			//ID
			else if(codeStack.get(codeStack.size()-1).equals("ID") && Error == false) {
				codeStack.pop();
				grammarStack.add("E");
				
					E();
			//(
			} else if(codeStack.get(codeStack.size()-1).equals("(") && Error == false){
				codeStack.pop();
				grammarStack.add("T");
				grammarStack.add("additive-expression1");
				grammarStack.add("term1");
				grammarStack.add(")");
				grammarStack.add("expression");
				expression();
				
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false){
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				} 
				term1();
				additive_expression1();
				T();
			//num	
			}else if(codeStack.get(codeStack.size()-1).equals("NUM") && Error == false) {
				codeStack.pop();
				grammarStack.add("T");
				grammarStack.add("additive-expression1");
				grammarStack.add("term1");
				term1();
				additive_expression1();
				T();
				//float
			}else if(codeStack.get(codeStack.size()-1).equals("FLOAT") && Error == false) {
				codeStack.pop();
				grammarStack.add("T");
				grammarStack.add("additive-expression1");
				grammarStack.add("term1");
				term1();
				additive_expression1();
				T();
			}  else {
				
				Error = true;
			}
		}
		
		
		// 27 E -> [expression] G | (args)  term1 additive-expression1 T | = expression | term1 additive-expression1 T ***********************************************
		public void E() {
			grammarStack.pop();
			
			if(codeStack.get(codeStack.size()-1).equals(";") || codeStack.get(codeStack.size()-1).equals(")") || codeStack.get(codeStack.size()-1).equals(",") || codeStack.get(codeStack.size()-1).equals("]")) {
			
			}
			//term1 done
			else if(codeStack.get(codeStack.size()-1).equals("*") || codeStack.get(codeStack.size()-1).equals("/") || codeStack.get(codeStack.size()-1).equals("+") || codeStack.get(codeStack.size()-1).equals("-") && Error == false) {
				grammarStack.add("T");
				grammarStack.add("additive-expression1");
				grammarStack.add("term1");
				term1();
				additive_expression1();
				T();
				
			} else if(codeStack.get(codeStack.size()-1).equals("<") || codeStack.get(codeStack.size()-1).equals(">") || codeStack.get(codeStack.size()-1).equals("==") || codeStack.get(codeStack.size()-1).equals(">=") || codeStack.get(codeStack.size()-1).equals("<=") || codeStack.get(codeStack.size()-1).equals("!=")&& Error == false) {
				grammarStack.add("T");
				T();
			}else if(codeStack.get(codeStack.size()-1).equals("[") && Error == false){
				codeStack.pop();
				grammarStack.add("G");
				grammarStack.add("]");
				grammarStack.add("expression");
				expression();
				//checks for ]
				
				
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false){
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
				G();
				
				
			} //args
			else if(codeStack.get(codeStack.size()-1).equals("(") && Error == false){
				codeStack.pop();
				grammarStack.add("T");
				grammarStack.add("additive-expression1");
				grammarStack.add("term1");
				grammarStack.add(")");
				grammarStack.add("args");
				args();
				//checks for )
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false){
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
				term1();
				additive_expression1();
				T();
			//equals
			}else if(codeStack.get(codeStack.size()-1).equals("=") && Error == false) {
				codeStack.pop();
				grammarStack.add("expression");
				expression();
			}else {
				
				Error = true;
			}
			
	
		}
		// 28 G -> = expression | term1 additive-expression1 T ***********
		public void G() {
			grammarStack.pop();
	
			
			if(codeStack.get(codeStack.size()-1).equals(";") || codeStack.get(codeStack.size()-1).equals(")")  || codeStack.get(codeStack.size()-1).equals("]") || codeStack.get(codeStack.size()-1).equals(",")&& Error == false) {
				
			}
			//term1 done
				else if(codeStack.get(codeStack.size()-1).equals("*") || codeStack.get(codeStack.size()-1).equals("/") || codeStack.get(codeStack.size()-1).equals("+") || codeStack.get(codeStack.size()-1).equals("-") && Error == false) {
				grammarStack.add("T");
				grammarStack.add("additive-expression1");
				grammarStack.add("term1");
				term1();
				additive_expression1();
				T();
			//equals
			} else if(codeStack.get(codeStack.size()-1).equals("=")) {
				codeStack.pop();
				grammarStack.add("expression");
				expression();
			}		else if(codeStack.get(codeStack.size()-1).equals("<") || codeStack.get(codeStack.size()-1).equals(">") || codeStack.get(codeStack.size()-1).equals("==") || codeStack.get(codeStack.size()-1).equals(">=") || codeStack.get(codeStack.size()-1).equals("<=") || codeStack.get(codeStack.size()-1).equals("!=") && Error == false) {
				grammarStack.add("T");
				T();
			}else {
				Error = true;
			}
		}
		
		
		// 29 S -> empty | [expression] *********************************
		public void S() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals("[") && Error == false) {
				codeStack.pop();
				grammarStack.add("]");
				grammarStack.add("expression");
				expression();
				//checks for ]
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false){
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
			} else {

			}
		}
		
		// 31 T -> empty | relop additive_expression 8888888888888888888888888888888888888888888888888
		public void T() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals("<") || codeStack.get(codeStack.size()-1).equals(">") || codeStack.get(codeStack.size()-1).equals("==") || codeStack.get(codeStack.size()-1).equals("!=") || codeStack.get(codeStack.size()-1).equals("<=") ||codeStack.get(codeStack.size()-1).equals(">=") && Error == false) {
				codeStack.pop();
				grammarStack.add("additive_expression");
				additive_expression();
			}
		}
		
		
		// 34 additive_expression -> term additive_expression1 88888888888888888888888888888888888888888888888888888888
		public void additive_expression() {
			grammarStack.pop();
			if( codeStack.get(codeStack.size()-1).equals("ID") || codeStack.get(codeStack.size()-1).equals("(") || codeStack.get(codeStack.size()-1).equals("NUM") || codeStack.get(codeStack.size()-1).equals("FLOAT") && Error == false) {
				grammarStack.add("additive_expression1");
				grammarStack.add("term");
				term();
				additive_expression1();
				
				
			} else {
				Error = true;
			}
		}
		
		// 35 additive_expression1 -> addop term additive_expression' | empty 888888888888888888888888888888
		public void additive_expression1() {
			grammarStack.pop();	
			if(codeStack.get(codeStack.size()-1).equals("+") || codeStack.get(codeStack.size()-1).equals("-") && Error == false) {
				codeStack.pop();
				grammarStack.add("additive_expression1");
				grammarStack.add("term");
				additive_expression1();
				term1();
			}
		}
		
		
		// 36 addop -> + | - 88888888888888888888888888888888888888
		public void addop() {
			
			if(codeStack.get(codeStack.size()-1).equals("+") || codeStack.get(codeStack.size()-1).equals("-") && Error == false) {
				codeStack.pop();
			} else {
				Error = true;
			}
		}
		
		// 37 term -> factor term1 88888888888888888888888888888888888
		public void term() {
			grammarStack.pop();
			grammarStack.add("term1");
			grammarStack.add("factor");
			factor();
			term1();
			
			
		}
		
		// 38 term1 -> mulop factor term1 | empty 8888888888888888888888888888888888888888888888
		public void term1() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals("*") || codeStack.get(codeStack.size()-1).equals("/") || codeStack.get(codeStack.size()-1).equals("+") || codeStack.get(codeStack.size()-1).equals("-") && Error == false) {
				codeStack.pop();
				grammarStack.add("term1");
				grammarStack.add("factor");
				factor();
				term1();
			} 
			
		}
		
		
		// 39 factor -> (expression) | ID C | Num | FLOAT **************************
		public void factor() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size() -1).equals("ID") && Error == false) {
				codeStack.pop();
				grammarStack.add("C");
				C();
			} else if(codeStack.get(codeStack.size() -1).equals("(") && Error == false) {
				codeStack.pop();
				grammarStack.add(")");
				grammarStack.add("expression");
				expression();
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false){
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
			}else  if(codeStack.get(codeStack.size() -1).equals("NUM") && Error == false) {
				codeStack.pop();
			}else if(codeStack.get(codeStack.size() -1).equals("FLOAT") && Error == false){
				codeStack.pop();
			}else {
				Error = true;
			}
			
		}
		
		
		// 40 T -> S | (args) *************************
		public void C() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size() -1).equals("(") && Error == false) {
				codeStack.pop();
				grammarStack.add(")");
				grammarStack.add("args");
				args();
				if(codeStack.get(codeStack.size()-1).equals(grammarStack.get(grammarStack.size()-1)) && Error == false){
					codeStack.pop();
					grammarStack.pop();
				} else {
					Error = true;
				}
			} else {
				grammarStack.add("S");
				S();
			}
		}
		

		
		// 41 args -> arg_list | empty *******************************
		public void args() {
			grammarStack.pop();
			if(codeStack.get(codeStack.size()-1).equals("(") || codeStack.get(codeStack.size()-1).equals("FLOAT") || codeStack.get(codeStack.size()-1).equals("ID")||codeStack.get(codeStack.size()-1).equals("NUM") && Error == false) {
				grammarStack.add("arg_list");
				arg_list();
			} else if(codeStack.get(codeStack.size()-1).equals(")") && grammarStack.get(grammarStack.size()-1).equals(")") && Error == false) {
			} else {
				Error = true;
			}
			
		}
		
		// 42 arg_list -> expression arg_list1 ******************************************
		public void arg_list() {
			grammarStack.pop();
			grammarStack.add("arg_list1");
			grammarStack.add("expression");
			if(codeStack.get(codeStack.size()-1).equals("(") || codeStack.get(codeStack.size()-1).equals("FLOAT") || codeStack.get(codeStack.size()-1).equals("ID")||codeStack.get(codeStack.size()-1).equals("NUM") && Error == false) {
				expression();
				arg_list1();
			} else {
				Error = true;
			}
			
		}
		
		// 43 arg-lsit' -> , expression arg_list1 | empty *****************************************
		public void arg_list1() {
			grammarStack.pop();
			grammarStack.add("arg_list1");
			grammarStack.add("expression");
			if(codeStack.get(codeStack.size()-1).equals(",") && Error == false) {
				codeStack.pop();
				expression();
				arg_list1();
			} else if(codeStack.get(codeStack.size()-1).equals(")") && Error == false) {
				grammarStack.pop();
				grammarStack.pop();
			} else {
				Error = true;
			}
		}
	}




