import java.util.ArrayList;

public class SymbolTable {

		public ArrayList<Symbol> table;
		public SymbolTable() {
			
		table = new ArrayList<Symbol>();	
		
		
	
		}
		
		
		public void dumpTable() {

			
			for(int i = 0 ; i < table.size(); i++) {
				//System.out.println(table.get(i).name + " INDEX " + i);
			}
			
			
			}
		
		
		public void enter(Symbol sym) {
			table.add(sym);
		}
		
		
		
		public boolean Exists(String name, int depth, int sub) {
			boolean yes = false;
			
			if(sub == 0) {
				for(int i = 0 ; i < table.size(); i++) {
					if(table.get(i).name.equals(name) && table.get(i).scope == depth) {
						yes = true;
						break;
					}
				}
			}
			
			
			
			
			
			for(int j = sub; j > 0; j --) {
			
			for(int i = 0 ; i < table.size(); i++) {
				
				if(table.get(i).name.equals(name) && table.get(i).scope == depth && table.get(i).subS == j) {
					//System.out.println(table.get(i).name + " " + table.get(i).scope + " " + table.get(i).subS);
					yes = true;
					break;
				}
			}
			
			}
			return yes;
			
			
		}
		
		public boolean exists(String name, int depth, int sub) {
			boolean yes = false;
			for(int i = 0 ; i < table.size(); i++) {
				if(table.get(i).name.equals(name) && table.get(i).scope == depth && table.get(i).subS == sub) {
					yes = true;
				}
			}
			return yes;
		}
		
		public boolean exists(String name, int depth , boolean fun, boolean arr) {
			boolean yes = false;
			for(int i = 0 ; i < table.size(); i++) {
				
				if(table.get(i).name.equals(name) && table.get(i).scope == depth && table.get(i).fucntion == fun && table.get(i).array == arr) {
					
					yes = true;
					break;
				}
			}
			return yes;
		}
		
		public boolean exists(String name) {
			boolean yes = false;
			for(int i = 0 ; i < table.size(); i++) {
				if(table.get(i).name.equals(name) && table.get(i).fucntion == true) {
					yes = true;
				}
			}
			return yes;
		}
		
		
		public Symbol returnFunction(String name, int scope) {
			Symbol temp = new Symbol();
			//can fix this part
			for(int i = 0; i< scope+1; i++) {
				//System.out.println(scope);
				
				
				for(int j = 0 ; j < table.size(); j++) {
					//System.out.print(table.get(j).name.equals(name) );
				//	System.out.print(table.get(j).scope == i);
					//System.out.println("*******");
					if(table.get(j).name.equals(name) && table.get(j).scope == i && table.get(j).fucntion == true && table.get(j).array == false) {
						
						temp = table.get(j);
						break;
					}
				}
				
				
				
				
			}
			
			return temp;
		}
		
		public Symbol returnSym(String name, int depth, boolean fun, boolean arr, int sub) {
			Symbol temp = new Symbol();
			if(sub == 0) {
				for(int i = 0 ; i < table.size(); i++) {
					if(table.get(i).name.equals(name) && table.get(i).scope == depth && table.get(i).fucntion == fun && table.get(i).array == arr) {
						temp = table.get(i);
						break;
					}
				}
			}
			
			boolean found = false;
			for(int j = sub; j > 0; j --) {
				
				if(found == true) {
					break;
				}
				
			for(int i = 0 ; i < table.size(); i++) {
				if(table.get(i).name.equals(name) && table.get(i).scope == depth && table.get(i).fucntion == fun && table.get(i).array == arr && table.get(i).subS==j) {
					//System.out.println(table.get(i).subS);
					temp = table.get(i);
					found = true;
					break;
				}
				
			}
			}
			return temp;
			
		}
		
		
		public boolean paramsExits(String name, String paramName, int depth) {
			boolean yes = false;
			for(int i = 0 ; i < table.size(); i++) {
				//looks for a function with same name
				if(table.get(i).name.equals(name) && table.get(i).fucntion == true) {
					
					for( int j = 0; j < table.get(i).params.size(); j++) {
						//looks for parameters that match
						if(table.get(i).params.get(j).name.equals(paramName) && table.get(i).params.get(j).scope == depth) {
							yes = true;
							break;
						} 
							
							
						
					}
				}
			}
			return yes;
		}
		
		
		public boolean seeIfFunctionHasBeenDefined(String name, int scope) {
			boolean exists = false;
			
			for(int i = 1; i< scope+1; i++) {
				
				
				
				if(exists(name,i,true,false)) {
					exists = true;
					break;
				} else {
					
				}
			}
			
			
			
			
			return exists;
		}
		
		
		
		
		
		public boolean nestedArray(String name, int scope, SymbolTable tab, ArrayList<Token> t, int part,int sub) {
			boolean work = true;
			for(int i = part; i < t.size(); i++) {
				
				
				//sees an array
				if(!t.get(i-1).type.equals("KEYWORD") && t.get(i).type.equals("ID") && t.get(i+1).value.equals("[")) {
					
					
					//checks is the variable is local
					if(tab.exists(t.get(i).value, scope, false, true)) {
						//System.out.println("LOCAL VARIABLE");
						//break;
						int C = 0;
						i = i + 1;
						while(true) {
							//if sees a FLOAT NUMBER
							if(t.get(i).type.equals("FLOAT")) {
								//System.out.println("YOU ARE TRYING TO GRAB THE INDEX OF AN ARRAY WITH FLOAT");
								work = false;
								break;
							}
							
							if(t.get(i).value.equals("[") ) {
								C = C + 1;
								i = i +1;
								
							}	
							//kills the 
							if(t.get(i).value.equals("]") && C == 1) {
								break;
							} else if(t.get(i).value.equals("]")) {
								C = C - 1;
								i = i +1;
							}
							//checks for function
							if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals("int") && t.get(i+1).value.equals("(")) {
								
								if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
									if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,sub)) {
										//System.out.println("Got it");
										int W = 0;
										i = i + 1;
										while(true) {
											if(t.get(i).value.equals("(") ) {
												W = W + 1;
												
											}	
											
											
											//kills the 
											if(t.get(i).value.equals(")") && W == 1) {
												//i = i + 1;
												break;
											} else if(t.get(i).value.equals(")")) {
												W = W - 1;
											}
											i = i + 1;
										}
									}else {
										work = false;
										//System.out.println("The function call fucked up");
										break;
									}
								} else {
									work = false;
									//System.out.println("Function has not been defined yet");
									break;
								}
							
						
							
							//checks if the variable is exists locally
							}else if(tab.Exists(t.get(i).value, scope,sub) && t.get(i).type.equals("ID") && C==1) {
								
								//checks if the variable is an int
								//not function
								if(tab.returnSym(t.get(i).value, scope, false, false,sub).type.equals("int")) {
								//function	
									//System.out.println("Going through here");
								} //else if(tab.returnSym(t.get(i).value, 0, true, false).type.equals("int")) {
								
								//} 
								//an array	local
								else if(tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("int")) {
									//System.out.println("NESTED LOCAL ARRAY");
									//checks the nested array
									if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
										int W = 0;
										i = i + 1;
										while(true) {

											if(t.get(i).value.equals("[") ) {
												W = W + 1;
												
											}	
											//kills the 
											if(t.get(i).value.equals("]") && W == 1) {
												break;
											} else if(t.get(i).value.equals("]")) {
												W = W - 1;
											}
											i = i + 1;
										}
									} else {
										work = false;
										//System.out.println("The nested array fucked up");
										break;
									}
									//an array global
								} else {
									work = false;
									//System.out.println("The variable is not type int");
									break;
								}
							//checks if the variable exists globally
							} else if(tab.exists(t.get(i).value, 0,0) && t.get(i).type.equals("ID") && C == 1) {
								
								//checks if the variable is an int
								//not function
								if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals("int")) {
									
								} else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("int")) {
									//System.out.println("NESTED GLOBAL ARRAY");
									if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
										int W = 0;
										i = i + 1;
										while(true) {

											if(t.get(i).value.equals("[") ) {
												W = W + 1;
												
											}	
											//kills the 
											if(t.get(i).value.equals("]") && W == 1) {
												break;
											} else if(t.get(i).value.equals("]")) {
												W = W - 1;
											}
											i = i + 1;
										}
									}else {
										work = false;
										//System.out.println("The nested array fucked up");
										break;
									}
								}else {
									work = false;
									//System.out.println(tab.returnSym(t.get(i).value, 0, false, false,0).type);
									//System.out.println(t.get(i).value);
									//System.out.println("The variable is not type int7");
									break;
								}
							} else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/") || t.get(i).value.equals("(") || t.get(i).value.equals(")") || t.get(i).type.equals("NUM")) {
								
							}else {
								work = false;
								//System.out.println(t.get(i).value);
								//System.out.println("The variable is not found globally or locally");
								break;
							}
								
						
							i = i + 1;
						}  //end while
						
						
						
						
						
					} else if(tab.exists(t.get(i).value, 0, false, true)) {
						//System.out.println("GLOBAL VARIABLE");
						//break;
						int C = 0;
						i = i + 1;
						while(true) {
							
							//if sees a FLOAT NUMBER
							if(t.get(i).type.equals("FLOAT")) {
								//System.out.println("YOU ARE TRYING TO GRAB THE INDEX OF AN ARRAY WITH FLOAT");
								work = false;
								break;
							}
							
							if(t.get(i).value.equals("[") ) {
								C = C + 1;
								i = i +1;
								
							}	
							//kills the 
							if(t.get(i).value.equals("]") && C == 1) {
								break;
							} else if(t.get(i).value.equals("]")) {
								C = C - 1;
								i = i +1;
							}
							
							//checks for function
							if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals("int") && t.get(i+1).value.equals("(")) {
								
								if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
									if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,sub)) {
									//	System.out.println("Got it");
										int W = 0;
										i = i + 1;
										while(true) {
											if(t.get(i).value.equals("(") ) {
												W = W + 1;
												
											}	
											
											
											//kills the 
											if(t.get(i).value.equals(")") && W == 1) {
												//i = i + 1;
												break;
											} else if(t.get(i).value.equals(")")) {
												W = W - 1;
											}
											i = i + 1;
										}
									}else {
										work = false;
										//System.out.println("The function call fucked up");
										break;
									}
								} else {
									work = false;
									//System.out.println("Function has not been defined yet");
									break;
								}
							
						
							
							//checks if the variable is exists locally
							}else if(tab.Exists(t.get(i).value, scope,sub) && t.get(i).type.equals("ID") && C == 1) {
								
								//checks if the variable is an int
								//not function
								if(tab.returnSym(t.get(i).value, scope, false, false,sub).type.equals("int")) {
								//function	
									//System.out.println("Going through here");
								} 
									else if(tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("int")) {
										//System.out.println("NESTED LOCAL ARRAY");
										//checks the nested array
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
											int W = 0;
											i = i + 1;
											while(true) {

												if(t.get(i).value.equals("[") ) {
													W = W + 1;
													
												}	
												//kills the 
												if(t.get(i).value.equals("]") && W == 1) {
													break;
												} else if(t.get(i).value.equals("]")) {
													W = W - 1;
												}
												i = i + 1;
											}
										} else {
											work = false;
											//System.out.println("The nested array fucked up");
											break;
										}
									}  else {
									work = false;
									//System.out.println("The variable is not type int");
									break;
								}
							//checks if the variable exists globally
							} else if(tab.Exists(t.get(i).value, 0,0) && t.get(i).type.equals("ID") && C ==1) {
								
								//checks if the variable is an int
								//not function
								if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals("int")) {
								//function	not done
								}  else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("int")) {
									//System.out.println("NESTED GLOBAL ARRAY");
									if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
										int W = 0;
										i = i + 1;
										while(true) {

											if(t.get(i).value.equals("[") ) {
												W = W + 1;
												
											}	
											//kills the 
											if(t.get(i).value.equals("]") && W == 1) {
												break;
											} else if(t.get(i).value.equals("]")) {
												W = W - 1;
											}
											i = i + 1;
										}
									}else {
										work = false;
										//System.out.println("The nested array fucked up");
										break;
									}
								}else {
									work = false;
									//System.out.println("The variable is not type int");
									break;
								}
							}else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/") || t.get(i).value.equals("(") || t.get(i).value.equals(")") || t.get(i).type.equals("NUM")) {
								
							}else {
								work = false;
								//System.out.println("The variable is not found globally or locally");
								break;
							}
							
							i = i + 1;
						}  //end while
					} else {
						work = false;
						//System.out.println("The variable is not found globally or locally");
						break;
					}
					
					//breaks out of the for loop
					if(t.get(i).value.equals("]")) {
						break;
					}
					
					
				}
				
			
			
			
			
			
			}
			
			return work;
		}

		
		
		
		public boolean nestedFunctionCall(String name, int scope, SymbolTable tab, ArrayList<Token> t, int part, int sub) {
			boolean work = true;
			
			for(int i = part; i < t.size(); i++) {
					
				
					if(t.get(i).value.equals(";")) {
						break;
					}
		
				if(  t.get(i).type.equals("ID") && t.get(i+1).value.equals("(") && scope >0) {	
					
					//System.out.println("FUNCTION CALL tttt" + t.get(i).value);
					
					ArrayList<Symbol> params = new ArrayList<Symbol>();
					//checks to see if function exists before
					
					if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
						int C = 0;
						int call = 0;
						params = tab.returnFunction(t.get(i).value, scope).params;
						
						
						i = i +1;
						while(true) {
							
							//System.out.println(t.get(i).value + " " + i + " " + C);
							//System.out.println("YOUNG");
						
						
						
						//for void types
						if(params.get(call).type.equals("void")) {
							if(t.get(i).value.equals(")") && C == 1) {
								break;
							} else if(t.get(i).value.equals("(") && C ==0) {
								C = C+1;
								i = i +1;
							}else {
								work = false;
								///System.out.println("This function has no paramters");
								break;
							}
						} 
						
						
						
						//System.out.println(t.get(i).value + " " + i + " " + C);
						
						
						//type int no  array
						if(params.get(call).type.equals("int") && params.get(call).array ==false) {
							//System.out.println(t.get(i).value + " r" + C);
							
							if(t.get(i).value.equals("(") ) {
								C = C + 1;
								i = i +1;
								
							}	
							
							//kills the loop
							else if(t.get(i).value.equals(")") && C == 1) {
								//System.out.println("break");
								break;
							} else if(t.get(i).value.equals(")")) {
								C = C - 1;
								i = i +1;
							}
							
							//+ - * /
							else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/")) {
								i = i +1;
							} else if(t.get(i).value.equals(",") && C == 1 ) {
								call = call +1;
								i = i + 1;
							}
							
							
							
							
							
							
							//number
							else if(t.get(i).type.equals("NUM")) {
								i = i +1;
								
							//looks for local vari
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false)) && (tab.returnSym(t.get(i).value, scope, false, false,sub).type.equals("int") || tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("int"))){
								
								
								
								
								
								
								//checks to see varible is type int
								if(tab.returnSym(t.get(i).value, scope, false, false,sub).type.equals("int")) {
									i = i +1;
								//array int
								} else if(tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("int")) {
										if(t.get(i+1).value.equals("[")) {
											if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
												int W = 0;
												i = i + 1;
												while(true) {

													if(t.get(i).value.equals("[") ) {
														W = W + 1;
														
													}	
													//kills the 
													if(t.get(i).value.equals("]") && W == 1) {
														i = i + 1;
														break;
													} else if(t.get(i).value.equals("]")) {
														W = W - 1;
													}
													i = i + 1;
												}
											} else {
												work = false;
												//System.out.println("The nested array fucked up");
												break;
											}
										}else {
											work = false;
											//System.out.println("You need to take a value from the array");
											break;
										}
										
										
										
								} else {
								
									work = false;
									
									//System.out.println("NOT TYPE INT");
									break;
								}
								
							//looks fo global vari	
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, 0, false, true) || tab.exists(t.get(i).value, 0, false, false)) && (tab.returnSym(t.get(i).value, 0, false, false,0).type.equals("int") || tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("int"))){
								
								//checks for type int
								if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals("int")) {
									i = i +1;
								//an array
								} else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("int")) {
									if(t.get(i+1).value.equals("[")) {
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
											int W = 0;
											i = i + 1;
											while(true) {

												if(t.get(i).value.equals("[") ) {
													W = W + 1;
													
												}	
												//kills the 
												if(t.get(i).value.equals("]") && W == 1) {
													i = i + 1;
													break;
												} else if(t.get(i).value.equals("]")) {
													W = W - 1;
												}
												i = i + 1;
											}
										} else {
											work = false;
											//System.out.println("The nested array fucked up");
											break;
										}
									}else {
										work = false;
										//System.out.println("You need to take a value from the array");
										break;
									}
								} else {
									work = false;
									//System.out.println("NOT TYPE INT");
									break;
								}
							//found a function that exists
							}else if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals("int") && t.get(i+1).value.equals("(")) {
								
									if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
										if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,sub)) {
											//System.out.println("Got it");
											int W = 0;
											i = i + 1;
											while(true) {
												if(t.get(i).value.equals("(") ) {
													W = W + 1;
													
												}	
												//kills the 
												if(t.get(i).value.equals(")") && W == 1) {
													i = i + 1;
													break;
												} else if(t.get(i).value.equals(")")) {
													W = W - 1;
												}
												i = i + 1;
											}
										}else {
											work = false;
											//System.out.println("The function call fucked up");
											break;
										}
									} else {
										work = false;
										//System.out.println("Function has not been defined yet 6");
										break;
									}
								
							} else {
							
								work = false;
								//System.out.println(t.get(i).value +"44ffe4llll" + sub);
								//System.out.println("NOT TYPE INT or the variables dont exist local/globally");
								break;
							}
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
						//type int array
						} else if(params.get(call).type.equals("int") && params.get(call).array ==true) {
						
							
							
							if(t.get(i).value.equals("(") ) {
								if(C ==0) {
									C = C + 1;
									i = i +1;
								} else {
									work = false;
									//System.out.println("You fucked up");
									break;
								}
								
								
							}	
							
							
							
							
							
							
							
							
							if(t.get(i).type.equals("ID") && tab.exists(t.get(i).value, scope, false, true) && tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("int")){
								if(!t.get(i+1).value.equals("[")) {
									if(tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("int")) {
										i = i +1;
									//an array
									} else {
										work = false;
										//System.out.println("NOT TYPE INT");
										break;
									}
								}else {
									work = false;
									//System.out.println("You are not passing a value from array");
									break;
								}
							} else if(t.get(i).type.equals("ID") && tab.exists(t.get(i).value, 0, false, true) && tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("int")) {
								if(!t.get(i+1).value.equals("[")) {
									if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("int")) {
										i = i +1;
									//an array
									} else {
										work = false;
										//System.out.println("NOT TYPE INT");
										break;
									}
								}else {
									work = false;
									//System.out.println("You are not passing a value from array");
									break;
								}
							} else {
								work = false;
								//System.out.println("Could not find local or global variable");
								break;
							}
							
							//kills the loop
							if(t.get(i).value.equals(")") && C == 1) {
								break;
							} 
							
							 else if(t.get(i).value.equals(",") && C == 1 ) {
								call = call +1;
								i = i + 1;
							} else {
								work = false;
								//System.out.println("You fucked up");
								break;
							}
							
							
							
							
							
							
							
						//type float no array
						} else if(params.get(call).type.equals("float") && params.get(call).array ==false) {
							
							
							if(t.get(i).value.equals("(") ) {
								C = C + 1;
								i = i +1;
								
							}	
							
							//kills the loop
							else if(t.get(i).value.equals(")") && C == 1) {
								break;
							} else if(t.get(i).value.equals(")")) {
								C = C - 1;
								i = i +1;
							}
							
							//+ - * /
							else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/")) {
								i = i +1;
							//moves the call
							} else if(t.get(i).value.equals(",") && C == 1 ) {
								call = call +1;
								i = i + 1;
							} else if(t.get(i).type.equals("FLOAT")) {
								i = i +1;
								
							//c
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false) ) && (tab.returnSym(t.get(i).value, scope, false, false,sub).type.equals("float") || tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("float"))){
								
								//checks to see varible is type int
								if(tab.returnSym(t.get(i).value, scope, false, false,sub).type.equals("float")) {
									i = i +1;
								//an array
								} else if(tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("float")) {
									if(t.get(i+1).value.equals("[")) {
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
											int W = 0;
											i = i + 1;
											while(true) {

												if(t.get(i).value.equals("[") ) {
													W = W + 1;
													
												}	
												//kills the 
												if(t.get(i).value.equals("]") && W == 1) {
													i = i + 1;
													break;
												} else if(t.get(i).value.equals("]")) {
													W = W - 1;
												}
												i = i + 1;
											}
										} else {
											work = false;
											//System.out.println("The nested array fucked up");
											break;
										}
									}else {
										work = false;
										//System.out.println("You need to take a value from the array");
										break;
									}
								} else {
									work = false;
									//System.out.println("NOT TYPE FLOAT");
									break;
								}
								
							//looks fo global vari	
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, 0, false, true) || tab.exists(t.get(i).value, 0, false, false)) && (tab.returnSym(t.get(i).value, 0, false, false,0).type.equals("float") || tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("float"))){
								
								//checks for type int
								if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals("float")) {
									i = i +1;
								} else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("float")) {
									if(t.get(i+1).value.equals("[")) {
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,sub)) {
											int W = 0;
											i = i + 1;
											while(true) {

												if(t.get(i).value.equals("[") ) {
													W = W + 1;
													
												}	
												//kills the 
												if(t.get(i).value.equals("]") && W == 1) {
													i = i + 1;
													break;
												} else if(t.get(i).value.equals("]")) {
													W = W - 1;
												}
												i = i + 1;
											}
										} else {
											work = false;
											//System.out.println("The nested array fucked up");
											break;
										}
									}else {
										work = false;
										//System.out.println("You need to take a value from the array");
										break;
									}
								} else {
									work = false;
									//System.out.println("NOT TYPE FLOAT");
									break;
								}
							} else if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals("float") && t.get(i+1).value.equals("(")) {
								
								if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
									if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,sub)) {
										//System.out.println("Got it");
										int W = 0;
										i = i + 1;
										while(true) {
											if(t.get(i).value.equals("(") ) {
												W = W + 1;
												
											}	
											//kills the 
											if(t.get(i).value.equals(")") && W == 1) {
												i = i + 1;
												break;
											} else if(t.get(i).value.equals(")")) {
												W = W - 1;
											}
											i = i + 1;
										}
									}else {
										work = false;
										//System.out.println("The function call fucked up");
										break;
									}
								} else {
									work = false;
									//System.out.println("Function has not been defined yet 6" );
									break;
								}
							
						}
							else {
								
								work = false;
								//System.out.println("NOT TYPE FLOAT or the variables dont exist local/globally");
								break;
							}
							
							//type float array	
						} else if(params.get(call).type.equals("float") && params.get(call).array ==true) {
							
							if(t.get(i).value.equals("(") ) {
								if(C ==0) {
									C = C + 1;
									i = i +1;
								} else {
									work = false;
									//System.out.println("You fucked up");
									break;
								}
								
								
							}	
							
							
							
							if(t.get(i).type.equals("ID") && tab.exists(t.get(i).value, scope, false, true) && tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("float")){
								if(!t.get(i+1).value.equals("[")) {
									if(tab.returnSym(t.get(i).value, scope, false, true,sub).type.equals("float")) {
										i = i +1;
									//an array
									} else {
										work = false;
										//System.out.println("NOT TYPE FLOAT");
										break;
									}
								}else {
									work = false;
									//System.out.println("You are not passing a value from array");
									break;
								}
							} else if(t.get(i).type.equals("ID") && tab.exists(t.get(i).value, 0, false, true) && tab.returnSym(t.get(i).value, 0, false, true,sub).type.equals("float")) {
								if(!t.get(i+1).value.equals("[")) {
									if(tab.returnSym(t.get(i).value, 0, false, true,sub).type.equals("float")) {
										i = i +1;
									//an array
									} else {
										work = false;
										//System.out.println("NOT TYPE FLOAT");
										break;
									}
								}else {
									work = false;
									//System.out.println("You are not passing a value from array");
									break;
								}
							} else {
								work = false;
								//System.out.println("Could not find local or global variable");
								break;
							}
							
							//kills the loop
							if(t.get(i).value.equals(")") && C == 1) {
								break;
							} 
							
							 else if(t.get(i).value.equals(",") && C == 1 ) {
								call = call +1;
								i = i + 1;
							} else {
								work = false;
								//System.out.println("You fucked up");
								break;
							}
							
							
							
						}
						
						
						
					}
						
					if(call != params.size()-1) {
						work = false;
						//System.out.println("MISING ARGUEMENTS");
						break;
					}
						
						
						
					} else {
						work = false;
						//System.out.println("This function has not been defined yet 44");
						break;
					}
					
				}
				
				
				
				
				
				
			
		  }//end for
			
			
			
			return work;
			
		}
		//checks the functions
		
		
}





