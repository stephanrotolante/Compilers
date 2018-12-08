import java.util.ArrayList;


public class Semantic {

	boolean work;
	SymbolTable tab ;
	
		
		public Semantic(ArrayList<Token> t) {
			work = true;
			
			tab = new SymbolTable();
			
			
			
			tabCreation(t);
			p4(t);
			//arrayCheck(t);
			//functiomCallCheck(t);
			//returnCheck(t);
			//setCheck(t);
			//booleanCheck(t);
		}
		
	
		
		
		public void tabCreation(ArrayList<Token> t) {
			
			//Hwere i can go back
			int mainCount = 0;
			int scope = 0;
			int subScope = 0;
			for(int i = 0; i < t.size(); i++) {
				
				if(t.get(i).value.equals("{")) {
					subScope = subScope +1;
				} else if(t.get(i).value.equals("}")) {
					subScope = subScope -1;
				}
				//grabs variable
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return") && t.get(i+1).type.equals("ID") && !t.get(i+2).value.equals("(")) {
					Symbol sym = new Symbol();
					
					//if the vairable is void error
					if(t.get(i).value.equals("void")) {
						//System.out.println("Void Variable");
						work = false;
						break;
					} else {
						//checks to see if exists
						if(!tab.exists(t.get(i+1).value, scope,subScope)) {
							//checks for an array
							if(t.get(i+2).value.equals("[")) {
							sym.array = true;	
							}
							sym.subS = subScope;
							sym.scope = scope;
							sym.fucntion = false;
							sym.name = t.get(i+1).value;
							sym.type = t.get(i).value;
							
							tab.enter(sym);
						} else {
							//System.out.println(t.get(i+1).value);
							//System.out.println("Variable Already Exists at this scope");
							work = false;
							break;
						}
					}	
				}
				
			
				
				//grabs a function
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return") && (t.get(i+1).type.equals("ID") || t.get(i+1).value.equals("main")) && t.get(i+2).value.equals("(")) {
					scope = scope + 1;
					
					if(mainCount > 0) {
						work = false;
						//System.out.println("You cannot definf functions after main");
						break;
					}
					
					Symbol function = new Symbol();
					if(t.get(i+1).value.equals("main")) {
						mainCount = mainCount + 1;
					}
					//checks to see if any global varis exists with same name exists or a function
					if(!tab.exists(t.get(i+1).value,0,0) ) {
						//cehcks to see if oither functions have the same name
						if(!tab.exists(t.get(i+1).value)){
						function.scope = scope;
						function.fucntion = true;
						function.name = t.get(i+1).value;
						function.type = t.get(i).value;
						function.array = false;
						tab.enter(function);
						i = i +1;
						while(true) {
							
							Symbol param = new Symbol();
							
							
							
							if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return")) {
								
								if(t.get(i+1).type.equals("ID") && !t.get(i+2).value.equals("(")) {
									//checks to see if exists
									if(!tab.paramsExits(function.name,t.get(i+1).value, scope)) {
										//checks for an array
										if(t.get(i+2).value.equals("[")) {
										param.array = true;	
										}
										param.subS = 1;
										param.scope = scope;
										param.fucntion = false;
										param.name = t.get(i+1).value;
										param.type = t.get(i).value;
										tab.enter(param);
										function.addP(param);
									} else {
										//System.out.println("Param Already Exists at this scope");
										work = false;
										break;
									}
								} else if(t.get(i).value.equals("void")) {
									
									param.type = "void";
									function.addP(param);
								}
							
							}
							//end of the loop
							if(t.get(i).value.equals(")")) {
								break;
								
							}	
						i = i + 1;
						}
						
						//tab.table.remove(tab.table.size()-1);
						//tab.enter(function);
						}else {
							//System.out.println("Function cannot be created because a function uses this name");
							work = false;
							break;
						}
					} else {
						//System.out.println("Function cannot be created because global varuavle uses this name");
						work = false;
						break;
					}
					
				}
				
				
				
				
			}
			
			if(mainCount == 0) {
				work = false;
				//System.out.println("There is no main function");
			}
			
			for( int i = 0; i < tab.table.size(); i++) {
				//System.out.println("TYPE: " +tab.table.get(i).type + " NAME: " + tab.table.get(i).name + " SCOPE: " + tab.table.get(i).scope + " ARRAY: " + tab.table.get(i).array + " FUNCTION: " + tab.table.get(i).fucntion+ " SUB SCOPE " + tab.table.get(i).subS );
				if(tab.table.get(i).params.size() > 0) {
					for( int j = 0; j < tab.table.get(i).params.size(); j++) {
						//System.out.println("TYPE: " +tab.table.get(i).params.get(j).type+ " NAME: " + tab.table.get(i).params.get(j).name + " SCOPE: " + tab.table.get(i).params.get(j).scope + " ARRAY: " + tab.table.get(i).params.get(j).array);
					}
					
				}
				//System.out.println(" ");
			}
			
		}

		
		public void p4(ArrayList<Token> t) {
			ArrayList<IMCODE> list = new ArrayList<IMCODE>();
			int c = 1;
			int scope = 0;
			int subScope = 0;
			String fn="";
			Align align = new Align();
			//align.addLine("Line", "Operation","Operand","Operand","Result");
			list.add(new IMCODE("Line", "Operation","Operand","Operand","Result",subScope,scope,"-"));
			for(int i = 0; i < t.size(); i++) {
				
				
				if(t.get(i).value.equals("{")) {
					subScope = subScope +1;
				} else if(t.get(i).value.equals("}")) {
					
					//end the func
					if(subScope == 1) {
						
						//align.addLine(String.valueOf(c),"end","func",fn);
						list.add(new IMCODE(String.valueOf(c),"end","func",fn,"-",subScope,scope,"-"));
						
						c = c+1;
						
					}
					
					subScope = subScope -1;
				}
				
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return")) {
					
					//function
				if(t.get(i+1).type.equals("ID") || t.get(i+1).value.equals("main")) {
					if(t.get(i+2).value.equals("(")) {
						scope = scope + 1;
						fn = t.get(i+1).value;
						if(tab.returnFunction(t.get(i+1).value, scope).params.get(0).type.equals("void")) {
							//align.addLine(String.valueOf(c),"func",t.get(i+1).value,t.get(i).value);
							
						//	IMCODE c2 = new IMCODE(String.valueOf(c),"func",t.get(i+1).value,t.get(i).value,subScope,scope);
							list.add(new IMCODE(String.valueOf(c),"func",t.get(i+1).value,t.get(i).value,"-",subScope,scope,"-"));
							c = c+1;
						} else {
							//align.addLine(String.valueOf(c),"func",t.get(i+1).value,t.get(i).value,String.valueOf(tab.returnFunction(t.get(i+1).value, scope).params.size()));
							list.add(new IMCODE(String.valueOf(c),"func",t.get(i+1).value,t.get(i).value,String.valueOf(tab.returnFunction(t.get(i+1).value, scope).params.size()),subScope,scope,"-"));
							c = c+1;
							for(int k = 0; k < tab.returnFunction(t.get(i+1).value, scope).params.size(); k++ ) {
								//align.addLine(String.valueOf(c),"param");
								list.add(new IMCODE(String.valueOf(c),"param","-","-","-",subScope,scope,"-"));
								c=c+1;
							
								
							}
						}
					
					
					
					
					}
				}
				
			}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				//System.out.println("NIGGA " + t.get(i).value );
				
				
				if(t.get(i).value.equals("while") || t.get(i).value.equals("if")) {
					String con = "";
					if(t.get(i).value.equals("while")) {
						con = "w";
					} else {
						con = "i";
					}
					
					int temp = i +2 ;
					
					int Pc = 0;
					int Pc2 = 0;
					int total = 0;
					boolean stop = false;
					boolean ck1= false;
					boolean ck0= true;
					
					
					int parC = 0;
					
					int count = 0;
					
					//goes through
					while(true) {
						//System.out.println(t.get(i).value );
						//System.out.println(t.get(i).value );
						
						i = temp;
						
						//break the arrays down
						for(int r = temp; r < t.size(); r++) {
							//System.out.print(t.get(r).value);
							if(t.get(r).value.equals(")") && parC ==0) {
								parC = 0;
								//S//ystem.out.println("IJJJJSSA");
									break;
									
									
								} else if(t.get(r).value.equals(")")) {
									parC = parC-1;
								} else if(t.get(r).value.equals("(")) {
								parC = parC+1;
								}
						//count = count +1;
							
							//if(t.get(r).value.equals(";")) {
							//System.out.println(count);
							//	break;
								
							//}
						}
						
						
						
						
						
						
						
						
						
						int g = temp;
						while(true) {
							
								if(t.get(g).value.equals(")") && parC ==0) {
								parC = 0;
								//System.out.println("IJJJJSSA");
									break;
									
									
								} else if(t.get(g).value.equals(")")) {
									parC = parC-1;
								} else if(t.get(g).value.equals("(")) {
								parC = parC+1;
								}
							
							//System.out.println(t.get(i).value + " yyyyy");
					if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("ID") &&t.get(g+3).value.equals("]")) {
						//System.out.println("NIGGA");
						
						//]
						t.remove(g+3);
						//align.addLine(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c),subScope+1,scope,con));
						c= c+1;
						//ID
						t.remove(g+2);
						//[
						t.remove(g+1);
						//align.addLine(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c),subScope+1,scope,con));
						
						Token newT = new Token("t"+String.valueOf(c),"ID");	
						t.set(g, newT);
						c= c+1;
						i = temp;
						break;
					}
						g = g +1;
						}
						
						g = temp;
						while(true) {
							
							if(t.get(g).value.equals(")") && parC ==0) {
								parC = 0;
							//	System.out.println("IJJJJSSA");
									break;
									
									
								} else if(t.get(g).value.equals(")")) {
									parC = parC-1;
								} else if(t.get(g).value.equals("(")) {
								parC = parC+1;
								}
							
							
							if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("NUM") &&t.get(g+3).value.equals("]")) {
								
								
								//]
								t.remove(g+3);
								
								
								int size = Integer.valueOf(t.get(g+2).value)*4;
								
								//NUM
								t.remove(g+2);
								//[
								t.remove(g+1);
								//align.addLine(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c),subScope+1,scope,con));
								
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(g, newT);
								c= c+1;
								i = temp;
								break;
							}
								g = g +1;
							
						}
						
						
						g = temp;
						//System.out.println(t.get(i).value + " PUSSSSSS" );
						//nested function
						//boolean Nested = false;
						int nestedSpot = 0;
						while (true) {
							
							if(t.get(g).value.equals(")") && parC ==0) {
								parC = 0;
								//System.out.println("IJJJJSSA");
									break;
									
									
								} else if(t.get(g).value.equals(")")) {
									parC = parC-1;
								} else if(t.get(g).value.equals("(")) {
								parC = parC+1;
								}
							
							//System.out.println("HERE");
							//System.out.println(t.get(g).value);
							if(t.get(g).value.equals("}") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							
							
							//System.out.println("TEST " + t.get(g).value);
							if(t.get(g).type.equals("ID") && t.get(g+1).value.equals("(") ) {
								
								for(int k = g+1; k< t.size(); k++) {
									//System.out.println(t.get(k).value + " " + t.get(k+1).value);
									if(t.get(k).type.equals("ID") && t.get(k+1).value.equals("(")) {
									//System.out.println("Nested");
										nestedSpot = k;
										//System.out.println(nestedSpot);
									} else {
										
										nestedSpot = temp;
										//System.out.println("Not Nested");
									}
									
								}
								
								g = nestedSpot;
								
								
								
								
								
								
								
								
								while(true) {
								//	System.out.println("TEST2 " + t.get(g).value);
									if(t.get(g).value.equals(")") && parC ==0) {
										parC = 0;
										//System.out.println("IJJJJSSA");
											break;
											
											
										} else if(t.get(g).value.equals(")")) {
											parC = parC-1;
										} else if(t.get(g).value.equals("(")) {
										parC = parC+1;
										}
									
									if( t.get(g-1).value.equals("(") &&(t.get(g).type.equals("NUM") ||t.get(g).type.equals("FLOAT") || t.get(g).type.equals("ID") )  && t.get(g+1).value.equals(",")) {
										
										t.remove(g+1);
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g).value,subScope+1,scope,con));
										
										c= c+1;
										t.remove(g);
										g = nestedSpot;
										
									}
									
									if(t.get(g).value.equals("(") && (t.get(g+1).type.equals("NUM") ||t.get(g+1).type.equals("FLOAT") || t.get(g+1).type.equals("ID") )  &&  t.get(g+2).value.equals(")")) {
										
										// )
										t.remove(g+2);
										
										
										// arg
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g+1).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g+1).value,subScope+1,scope,con));
										
										c= c+1;
										t.remove(g+1);
										
										// (
										t.remove(g);
										
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										//align.addLine(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c),subScope+1,scope,con));
										c = c+1;
										t.set(g-1, newT);
										
										
										break;
										
									}
									g = g+1;
								}
								

								
								
								
							
							}
							
							if(t.get(g).value.equals("}") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							g = g+1;
						}
						
						
						//System.out.println("TEST44 " + t.get(i).value);
						
						for(int r = temp; r < t.size(); r++) {
							//System.out.print(t.get(r).value);
							if(t.get(r).value.equals(")") && parC ==0) {
								parC = 0;
								//S//ystem.out.println("IJJJJSSA");
									break;
									
									
								} else if(t.get(r).value.equals(")")) {
									parC = parC-1;
								} else if(t.get(r).value.equals("(")) {
								parC = parC+1;
								}
						//count = count +1;
							
							//if(t.get(r).value.equals(";")) {
							//System.out.println(count);
							//	break;
								
							//}
						}
						
						
						//System.out.println(" jjjfjfjfjjf " + t.get(i).value );
						
						
						
						
						
						
						///does the parens
						while(true) {
												//lloks for the last ( and how deep it is
												while(stop == false) {
												//	System.out.println("backhere " + t.get(i).value);
													
													if( !t.get(i-1).type.equals("ID") && t.get(i).value.equals("(")) {
														Pc = Pc+1;
														total = Pc;
														//System.out.println(total+"yt");
														//break;
													} 
													if(t.get(i).value.equals(")") && Pc >1 ) {
														
														Pc = Pc-1;
														//break;
													} else if(t.get(i).value.equals(")") && Pc ==1 ) {
														Pc = Pc -1;
														stop=true;
														
										
														i = temp;
						
														break;
													}
													
													if(t.get(i).value.equals(")") && parC ==0) {
														parC = 0;
													//	System.out.println("IJJJJSSAtttttt");
															break;
															
															
														} else if(t.get(i).value.equals(")")) {
															parC = parC-1;
														} else if(t.get(i).value.equals("(")) {
														parC = parC+1;
														}
													i = i+1;
												}
												
											
					//System.out.println(t.get(i+1).value + "ypyp");
						
						if(!t.get(i-1).type.equals("ID") &&t.get(i).value.equals("(")) {
							Pc2 = Pc2+1;
							int tempSpot = i;
							boolean c1 = false;
							boolean c2 = false;
							//System.out.println(Pc2 + " " + total);
							//System.out.println("HERE"+ total+ " "+Pc);
							//checks inside the paren
							if(Pc2 == total) {
								//System.out.println("NIGGA");
								//System.out.println("HERE");
								//checks the mult/div
								while(c1==false) {
									
									//System.out.println("yiyiyiiy");
									//mult
									if( !t.get(i-1).value.equals("]") && !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(") ) {
									//	align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out3");
										break;
									//div
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out");
										break;
										
									}
								
									if(t.get(i).value.equals(")")) {
										c1=true;
										i = tempSpot;
										//S//ystem.out.println("HERE");
										break;	
									}
									i = i+1;
								}
								//System.out.println("HERE");
								
								//does the add/sub
								while(c2==false) {
									//add
									
									
									if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										i = tempSpot;
										//break;
										
									//subtract
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-")&& !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
										
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										i = tempSpot;
										c= c+1;
										//break;
									}
									
									if(t.get(i).value.equals(")") && !t.get(i+2).value.equals("[")) {
										c2 = true;
										//System.out.println("HERE");
										//System.out.println(t.get(tempSpot).value);
										t.remove(i);
										t.remove(tempSpot);
										stop = false;
										i = temp;
										Pc2 = 0;
										break;
									}
									i = i+1;
								
								}
								
								
								
								
							
								
							}
						}
										
						i = i+1;
						
						if(t.get(i).value.equals("{")) {
							break;
							
						}
					//	System.out.println("HRRRRRRRR " + t.get(i).value);
						if(t.get(i).value.equals(")") && parC ==0) {
							parC = 0;
							//System.out.println("IJJJJSSA");
								break;
								
								
							} else if(t.get(i).value.equals(")")) {
								parC = parC-1;
							} else if(t.get(i).value.equals("(")) {
							parC = parC+1;
							}
						
						}
					//System.out.println("tttt");
						//////////////////////////////////////////////////////////////////////
					
						i = temp;
						while(ck0==true) {
							
							//System.out.println("yiyiyiiy");
							//mult
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[") && !t.get(i+2).value.equals("(")) {
							//	align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
								
								//System.out.println(t.get(i).value+ " t " + t.get(i+1).value);
								
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out3");
								break;
							//div
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out");
								break;
								
							}
						
							if(t.get(i).value.equals(")") && parC ==0) {
								parC = 0;
								ck0 = false;
								ck1= true;
								//System.out.println("IJJJJSSA");
									break;
									
									
								} else if(t.get(i).value.equals(")")) {
									parC = parC-1;
								} else if(t.get(i).value.equals("(")) {
								parC = parC+1;
								}
							i = i+1;
						}
						//System.out.println("tttt");
						
						i = temp;
						
					
						
						while(ck1==true) {
							//add
							
							//System.out.println("NIIIIIIIIIIIIII");
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								break;
								
							//subtract
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope+1,scope,con));
								
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
						
								c= c+1;
								break;
							}
							

							if(t.get(i).value.equals(")") && parC ==0) {
								parC = 0;
								//System.out.println("IJJJJSSA");
									break;
									
									
								} else if(t.get(i).value.equals(")")) {
									parC = parC-1;
								} else if(t.get(i).value.equals("(")) {
								parC = parC+1;
								}
							i = i+1;
						
						}
						
						
			
						
						
						
						
				//	System.out.println("NIGGA" );
						
						
						//System.out.println( t.get(i-2).value);
						//System.out.println( t.get(i-1).value);
						//System.out.println( t.get(i).value + " MIDDLE");
						//System.out.println( t.get(i+1).value);
						//System.out.println( t.get(i+2).value);
						
						
						
						if( t.get(i-4).value.equals("(") && (t.get(i-3).type.equals("ID") || t.get(i-3).type.equals("NUM") || t.get(i-3).type.equals("FLOAT")) && (t.get(i-2).value.equals(">") ||t.get(i-2).value.equals("<") || t.get(i-2).value.equals(">=") ||t.get(i-2).value.equals("<=") || t.get(i-2).value.equals("==") ||t.get(i-2).value.equals("!=")) && (t.get(i-1).type.equals("ID") || t.get(i-1).type.equals("NUM") || t.get(i-1).type.equals("FLOAT")  ) && t.get(i).value.equals(")") ) {
							//align.addLine(String.valueOf(c), "comp", t.get(i-3).value, t.get(i-1).value, "t"+String.valueOf(c));
							list.add(new IMCODE(String.valueOf(c), "comp", t.get(i-3).value, t.get(i-1).value, "t"+String.valueOf(c),subScope+1,scope,con));
							
							c= c+1;
							
							
							if(t.get(i-2).value.equals(">")) {
								//align.addLine(String.valueOf(c), "BGT", String.valueOf(c-1), "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "BGT", "t"+String.valueOf(c-1), "-", String.valueOf(c+2),subScope+1,scope,con));
								
								c= c+1;
								//align.addLine(String.valueOf(c), "B", "-", "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "B", "-", "-", "sub",subScope+1,scope,con));
								
								c= c+1;
							} else if(t.get(i-2).value.equals("<")) {
								//align.addLine(String.valueOf(c), "BLT", String.valueOf(c-1), "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "BLT", "t"+String.valueOf(c-1), "-", String.valueOf(c+2),subScope+1,scope,con));
								c= c+1;
							//	align.addLine(String.valueOf(c), "B", "-", "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "B", "-", "-", "sub",subScope+1,scope,con));
								
								c= c+1;
							} else if(t.get(i-2).value.equals(">=")) {
								//align.addLine(String.valueOf(c), "BGTE", String.valueOf(c-1), "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "BGTE", "t"+String.valueOf(c-1), "-", String.valueOf(c+2),subScope+1,scope,con));
								c= c+1;
								//align.addLine(String.valueOf(c), "B", "-", "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "B", "-", "-", "sub",subScope+1,scope,con));
								
								c= c+1;
							} else if(t.get(i-2).value.equals("<=")) {
								//align.addLine(String.valueOf(c), "BLTE", String.valueOf(c-1), "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "BLTE", "t"+String.valueOf(c-1), "-", String.valueOf(c+2),subScope+1,scope,con));
								c= c+1;
								//align.addLine(String.valueOf(c), "B", "-", "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "B", "-", "-", "sub",subScope+1,scope,con));
								
								c= c+1;
							} else if(t.get(i-2).value.equals("==")) {
								//align.addLine(String.valueOf(c), "EQ", String.valueOf(c-1), "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "EQ", "t"+String.valueOf(c-1), "-", String.valueOf(c+2),subScope+1,scope,con));
								c= c+1;
								//align.addLine(String.valueOf(c), "B", "-", "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "B", "-", "-", "sub",subScope+1,scope,con));
								
								c= c+1;
							} else if(t.get(i-2).value.equals("!=")) {
								//align.addLine(String.valueOf(c), "NEQ", String.valueOf(c-1), "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "NEQ", "t"+String.valueOf(c-1), "-", String.valueOf(c+2),subScope+1,scope,con));
								c= c+1;
								//align.addLine(String.valueOf(c), "B", "-", "-", "sub");
								list.add(new IMCODE(String.valueOf(c), "B", "-", "-", "sub",subScope+1,scope,con));
								
								c= c+1;
							}
							
							
							
							
							break;
						}
						if(count == 3) {
							break;
						}
						
					}
					
					
					
					
					
					
					
					
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				if(t.get(i).value.equals("return")) {
					int temp = i;
					int Pc = 0;
					int Pc2 = 0;
					int total = 0;
					boolean stop = false;
					boolean ck1= false;
					boolean ck0= true;
					
					
					
					
					int count = 0;
					
					//goes through
					while(true) {
						
						
						
						i = temp;
						
						
						
						
						
						
						
						
						
						
						
						int g = temp;
						while(true) {
							
							if(t.get(g).value.equals(";")) {
								
									break;
									
								}
					if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("ID") &&t.get(g+3).value.equals("]")) {
						//System.out.println("NIGGA");
						
						//]
						t.remove(g+3);
						//align.addLine(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c),subScope,scope,"-"));
						
						c= c+1;
						//ID
						t.remove(g+2);
						//[
						t.remove(g+1);
						//align.addLine(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c),subScope,scope,"-"));
						
						Token newT = new Token("t"+String.valueOf(c),"ID");	
						t.set(g, newT);
						c= c+1;
						i = temp;
						break;
					}
						g = g +1;
						}
						
						g = temp;
						while(true) {
							
							if(t.get(g).value.equals(";")) {
								
								break;
								
							}
							
							
							if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("NUM") &&t.get(g+3).value.equals("]")) {
								
								
								//]
								t.remove(g+3);
								
								
								int size = Integer.valueOf(t.get(g+2).value)*4;
								
								//NUM
								t.remove(g+2);
								//[
								t.remove(g+1);
								//align.addLine(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c),subScope,scope,"-"));
								
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(g, newT);
								c= c+1;
								i = temp;
								break;
							}
								g = g +1;
							
						}
						
						
						g = temp;
						
						//nested function
						//boolean Nested = false;
						int nestedSpot = 0;
						while (true) {
							
							//System.out.println("HERE");
							//System.out.println(t.get(g).value);
							if(t.get(g).value.equals(";") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							
							
							if(t.get(g).type.equals("ID") && t.get(g+1).value.equals("(") ) {
								
								for(int k = g+1; k< t.size(); k++) {
									//System.out.println(t.get(k).value + " " + t.get(k+1).value);
									if(t.get(k).type.equals("ID") && t.get(k+1).value.equals("(")) {
									//System.out.println("Nested");
										nestedSpot = k;
										//System.out.println(nestedSpot);
									} else {
										
										nestedSpot = temp;
										//System.out.println("Not Nested");
									}
									
								}
								
								g = nestedSpot;
								while(true) {
									
									if(t.get(g).value.equals(")")) {
										break;	
									}
									
									if( t.get(g-1).value.equals("(") &&(t.get(g).type.equals("NUM") ||t.get(g).type.equals("FLOAT") || t.get(g).type.equals("ID") )  && t.get(g+1).value.equals(",")) {
										
										t.remove(g+1);
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g).value,subScope,scope,"-"));
										
										c= c+1;
										t.remove(g);
										g = nestedSpot;
										
									}
									
									if(t.get(g).value.equals("(") && (t.get(g+1).type.equals("NUM") ||t.get(g+1).type.equals("FLOAT") || t.get(g+1).type.equals("ID") )  &&  t.get(g+2).value.equals(")")) {
										
										// )
										t.remove(g+2);
										
										
										// arg
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g+1).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g+1).value,subScope,scope,"-"));
										
										c= c+1;
										t.remove(g+1);
										
										// (
										t.remove(g);
										
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										//align.addLine(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c),subScope,scope,"-"));
										
										c = c+1;
										t.set(g-1, newT);
										
										
										break;
										
									}
									g = g+1;
								}
								

								
								
								
							
							}
							
							if(t.get(g).value.equals(";") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							g = g+1;
						}
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						///does the parens
						while(true) {
												//lloks for the last ( and how deep it is
												while(stop == false) {
												//	System.out.println("backhere " + t.get(i).value);
													
													if( !t.get(i-1).type.equals("ID") && t.get(i).value.equals("(")) {
														Pc = Pc+1;
														total = Pc;
														//System.out.println(total+"yt");
														//break;
													} 
													if(t.get(i).value.equals(")") && Pc >1 ) {
														
														Pc = Pc-1;
														//break;
													} else if(t.get(i).value.equals(")") && Pc ==1 ) {
														Pc = Pc -1;
														stop=true;
														
										
														i = temp;
						
														break;
													}
													
													if(t.get(i).value.equals(";")) {
														stop=true;
														Pc = 0;
														i = temp;
														break;	
													}
													i = i+1;
												}
												
											
					//System.out.println(t.get(i+1).value + "ypyp");
						
						if(!t.get(i-1).type.equals("ID") &&t.get(i).value.equals("(")) {
							Pc2 = Pc2+1;
							int tempSpot = i;
							boolean c1 = false;
							boolean c2 = false;
							//System.out.println(Pc2 + " " + total);
							//System.out.println("HERE"+ total+ " "+Pc);
							//checks inside the paren
							if(Pc2 == total) {
								//System.out.println("NIGGA");
								//System.out.println("HERE");
								//checks the mult/div
								while(c1==false) {
									
									//System.out.println("yiyiyiiy");
									//mult
									if( !t.get(i-1).value.equals("]") && !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(") ) {
										//align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out3");
										break;
									//div
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out");
										break;
										
									}
								
									if(t.get(i).value.equals(")")) {
										c1=true;
										i = tempSpot;
										//S//ystem.out.println("HERE");
										break;	
									}
									i = i+1;
								}
								//System.out.println("HERE");
								
								//does the add/sub
								while(c2==false) {
									//add
									
									
									if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										i = tempSpot;
										//break;
										
									//subtract
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-")&& !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										i = tempSpot;
										c= c+1;
										//break;
									}
									
									if(t.get(i).value.equals(")") && !t.get(i+2).value.equals("[")) {
										c2 = true;
										//System.out.println("HERE");
										//System.out.println(t.get(tempSpot).value);
										t.remove(i);
										t.remove(tempSpot);
										stop = false;
										i = temp;
										Pc2 = 0;
										break;
									}
									i = i+1;
								
								}
								
								
								
								
							
								
							}
						}
										
						i = i+1;
						
						if(t.get(i).value.equals(";")) {
							//System.out.println("NIGGA");	
							break;
						}
						
						}
						//System.out.println("tttt");
						//////////////////////////////////////////////////////////////////////
						i = temp;
						while(ck0==true) {
							
							//System.out.println("yiyiyiiy");
							//mult
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[") && !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out3");
								break;
							//div
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out");
								break;
								
							}
						
							if(t.get(i).value.equals(";")) {
								ck1=true;
								ck0=false;
								//S//ystem.out.println("HERE");
								break;	
							}
							i = i+1;
						}
						//System.out.println("tttt");
						
						i = temp;
						
						
						while(ck1==true) {
							//add
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								break;
								
							//subtract
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
						
								c= c+1;
								break;
							}
							
							if(t.get(i).value.equals(";")) {
								break;
							}
							i = i+1;
						
						}
						
						
						//break the arrays down
						for(int r = temp; r < t.size(); r++) {
							
						//count = count +1;
							//System.out.print(t.get(r).value);
							if(t.get(r).value.equals(";")) {
							//System.out.println(count);
								break;
								
							}
						}
						
						
						
						
						//System.out.println("NIGGA" );
						
						
						
					
						if( t.get(i-2).value.equals("return")    &&  (t.get(i-1).type.equals("ID") || t.get(i-1).type.equals("NUM") || t.get(i-1).type.equals("FLOAT")) && t.get(i).value.equals(";")) {
							///align.addLine(String.valueOf(c),"return","-","-", t.get(i-1).value);
							list.add(new IMCODE(String.valueOf(c),"return","-","-", t.get(i-1).value,subScope,scope,"-"));
							
							c = c+1;
							i = i -1;
							break;
						}
						
						if( t.get(i-1).value.equals("return")    &&  t.get(i).value.equals(";")) {
							//align.addLine(String.valueOf(c),"return","-","-", "-");
							list.add(new IMCODE(String.valueOf(c),"return","-","-", "-",subScope,scope,"-"));
							
							c = c+1;
							i = i -1;
							break;
						}
						if(count == 3) {
							break;
						}
						
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				if(i < t.size()-1)	{
				
				
				
				//function call
				if((t.get(i).value.equals("}") || t.get(i).value.equals(";") || t.get(i).value.equals("{")) &&t.get(i+1).type.equals("ID") && t.get(i+2).value.equals("(") ) {
					String tm = "";
					
					
					
					if(t.get(i+2).value.equals("[")) {
						
						//just a num
						if(t.get(i+3).type.equals("NUM") && t.get(i+4).value.equals("]")) {
							int size = Integer.valueOf(t.get(i+3).value)*4;
							tm = "t"+String.valueOf(c);
							//align.addLine(String.valueOf(c),"disp",String.valueOf(size),t.get(i+1).value,tm);
							list.add(new IMCODE(String.valueOf(c),"disp",String.valueOf(size),t.get(i+1).value,tm,subScope,scope,"-"));
							
							c= c+1;
							i = i + 5;
						}
					} else {
						tm = t.get(i+1).value;
						i = i+1;	
					}
					
					int temp = i;
					int Pc = 0;
					int Pc2 = 0;
					int total = 0;
					boolean stop = false;
					boolean ck1= false;
					boolean ck0= true;
					
					
					
					
					int count = 0;
					
					//goes through
					while(true) {
						
						
						
						i = temp;
						
						
						
						
						
						
						
						
						
						
						
						int g = temp;
						while(true) {
							
							if(t.get(g).value.equals(";")) {
								
									break;
									
								}
					if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("ID") &&t.get(g+3).value.equals("]")) {
						//System.out.println("NIGGA");
						
						//]
						t.remove(g+3);
						//align.addLine(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c),subScope,scope,"-"));
						
						c= c+1;
						//ID
						t.remove(g+2);
						//[
						t.remove(g+1);
						//align.addLine(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c),subScope,scope,"-"));
						
						Token newT = new Token("t"+String.valueOf(c),"ID");	
						t.set(g, newT);
						c= c+1;
						i = temp;
						break;
					}
						g = g +1;
						}
						
						g = temp;
						while(true) {
							
							if(t.get(g).value.equals(";")) {
								
								break;
								
							}
							
							
							if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("NUM") &&t.get(g+3).value.equals("]")) {
								
								
								//]
								t.remove(g+3);
								
								
								int size = Integer.valueOf(t.get(g+2).value)*4;
								
								//NUM
								t.remove(g+2);
								//[
								t.remove(g+1);
								//align.addLine(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c),subScope,scope,"-"));
								
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(g, newT);
								c= c+1;
								i = temp;
								break;
							}
								g = g +1;
							
						}
						
						
						g = temp;
						
						//nested function
						//boolean Nested = false;
						int nestedSpot = 0;
						while (true) {
							
							//System.out.println("HERE");
							//System.out.println(t.get(g).value);
							if(t.get(g).value.equals(";") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							
							
							if(t.get(g).type.equals("ID") && t.get(g+1).value.equals("(") ) {
								
								for(int k = g+1; k< t.size(); k++) {
									//System.out.println(t.get(k).value + " " + t.get(k+1).value);
									if(t.get(k).type.equals("ID") && t.get(k+1).value.equals("(")) {
										//System.out.println("Nested");
										nestedSpot = k;
										//System.out.println(nestedSpot);
									} else {
										
										nestedSpot = temp;
										//System.out.println("Not Nested");
									}
									
								}
								
								g = nestedSpot;
								while(true) {
									
									if(t.get(g).value.equals(")")) {
										break;	
									}
									
									if( t.get(g-1).value.equals("(") &&(t.get(g).type.equals("NUM") ||t.get(g).type.equals("FLOAT") || t.get(g).type.equals("ID") )  && t.get(g+1).value.equals(",")) {
										
										t.remove(g+1);
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g).value,subScope,scope,"-"));
										
										c= c+1;
										t.remove(g);
										g = nestedSpot;
										
									}
									
									if(t.get(g).value.equals("(") && (t.get(g+1).type.equals("NUM") ||t.get(g+1).type.equals("FLOAT") || t.get(g+1).type.equals("ID") )  &&  t.get(g+2).value.equals(")")) {
										
										// )
										t.remove(g+2);
										
										
										// arg
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g+1).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g+1).value,subScope,scope,"-"));
										
										c= c+1;
										t.remove(g+1);
										
										// (
										t.remove(g);
										
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										//align.addLine(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c),subScope,scope,"-"));
										
										c = c+1;
										t.set(g-1, newT);
										
										
										break;
										
									}
									g = g+1;
								}
								

								
								
								
							
							}
							
							if(t.get(g).value.equals(";") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							g = g+1;
						}
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						///does the parens
						while(true) {
												//lloks for the last ( and how deep it is
												while(stop == false) {
												//	System.out.println("backhere " + t.get(i).value);
													
													if( !t.get(i-1).type.equals("ID") && t.get(i).value.equals("(")) {
														Pc = Pc+1;
														total = Pc;
														//System.out.println(total+"yt");
														//break;
													} 
													if(t.get(i).value.equals(")") && Pc >1 ) {
														
														Pc = Pc-1;
														//break;
													} else if(t.get(i).value.equals(")") && Pc ==1 ) {
														Pc = Pc -1;
														stop=true;
														
										
														i = temp;
						
														break;
													}
													
													if(t.get(i).value.equals(";")) {
														stop=true;
														Pc = 0;
														i = temp;
														break;	
													}
													i = i+1;
												}
												
											
					//System.out.println(t.get(i+1).value + "ypyp");
						
						if(!t.get(i-1).type.equals("ID") &&t.get(i).value.equals("(")) {
							Pc2 = Pc2+1;
							int tempSpot = i;
							boolean c1 = false;
							boolean c2 = false;
							//System.out.println(Pc2 + " " + total);
							//System.out.println("HERE"+ total+ " "+Pc);
							//checks inside the paren
							if(Pc2 == total) {
								//System.out.println("NIGGA");
								//System.out.println("HERE");
								//checks the mult/div
								while(c1==false) {
									
									//System.out.println("yiyiyiiy");
									//mult
									if( !t.get(i-1).value.equals("]") && !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(") ) {
										//align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out3");
										break;
									//div
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out");
										break;
										
									}
								
									if(t.get(i).value.equals(")")) {
										c1=true;
										i = tempSpot;
										//S//ystem.out.println("HERE");
										break;	
									}
									i = i+1;
								}
								//System.out.println("HERE");
								
								//does the add/sub
								while(c2==false) {
									//add
									
									
									if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										i = tempSpot;
										//break;
										
									//subtract
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-")&& !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										i = tempSpot;
										c= c+1;
										//break;
									}
									
									if(t.get(i).value.equals(")") && !t.get(i+2).value.equals("[")) {
										c2 = true;
										//System.out.println("HERE");
										//System.out.println(t.get(tempSpot).value);
										t.remove(i);
										t.remove(tempSpot);
										stop = false;
										i = temp;
										Pc2 = 0;
										break;
									}
									i = i+1;
								
								}
								
								
								
								
							
								
							}
						}
										
						i = i+1;
						
						if(t.get(i).value.equals(";")) {
							//System.out.println("NIGGA");	
							break;
						}
						
						}
						//System.out.println("tttt");
						//////////////////////////////////////////////////////////////////////
						i = temp;
						while(ck0==true) {
							
							//System.out.println("yiyiyiiy");
							//mult
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[") && !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out3");
								break;
							//div
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out");
								break;
								
							}
						
							if(t.get(i).value.equals(";")) {
								ck1=true;
								ck0=false;
								//System.out.println("HERE");
								break;	
							}
							i = i+1;
						}
					//System.out.println("tttt");
						
						i = temp;
						
						
						while(ck1==true) {
							//add
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								break;
								
							//subtract
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
						
								c= c+1;
								break;
							}
							
							if(t.get(i).value.equals(";")) {
								break;
							}
							i = i+1;
						
						}
						
						
						//break the arrays down
						for(int r = temp; r < t.size(); r++) {
							
						//count = count +1;
							//System.out.print(t.get(r).value);
							if(t.get(r).value.equals(";")) {
							//System.out.println(count);
								break;
								
							}
						}
						
						
						
						
						//System.out.println("NIGGA" );
						
						
						
					
						if(t.get(i-1).type.equals("ID") && t.get(i).value.equals(";")) {
							//align.addLine(String.valueOf(c),"assign",t.get(i-1).value,"-", tm);
							list.add(new IMCODE(String.valueOf(c),"assign",t.get(i-1).value,"-", tm,subScope,scope,"-"));
							
							c = c+1;
							i = i -1;
							break;
						}
						if(count == 3) {
							break;
						}
						
					}
					
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			//	System.out.println(t.get(i).value+ "hereee");
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				//System.out.println(" YUH");
				
				
				
				
				
				
				
				
				//setting
				if((t.get(i).value.equals("}") || t.get(i).value.equals(";")  || t.get(i).value.equals("{")) &&t.get(i+1).type.equals("ID") && !t.get(i+2).value.equals("(") ) {
					String tm = "";
					if(t.get(i+2).value.equals("[")) {
						
						//just a num
						if(t.get(i+3).type.equals("NUM") && t.get(i+4).value.equals("]")) {
							int size = Integer.valueOf(t.get(i+3).value)*4;
							tm = "t"+String.valueOf(c);
							//align.addLine(String.valueOf(c),"disp",String.valueOf(size),t.get(i+1).value,tm);
							list.add(new IMCODE(String.valueOf(c),"disp",String.valueOf(size),t.get(i+1).value,tm,subScope,scope,"-"));
							
							c= c+1;
							i = i + 5;
						}
					} else {
						tm = t.get(i+1).value;
						i = i+2;	
					}
					
					int temp = i;
					int Pc = 0;
					int Pc2 = 0;
					int total = 0;
					boolean stop = false;
					boolean ck1= false;
					boolean ck0= true;
					
					
					
					
					int count = 0;
					
					//goes through
					while(true) {
						
						
						
						i = temp;
						
						
						
						
						
						
						
						
						
						
						
						int g = temp;
						while(true) {
							
							if(t.get(g).value.equals(";")) {
								
									break;
									
								}
					if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("ID") &&t.get(g+3).value.equals("]")) {
						//System.out.println("NIGGA");
						
						//]
						t.remove(g+3);
						//align.addLine(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c), "mult", "4",t.get(g+2).value,"t"+String.valueOf(c),subScope,scope,"-"));
						
						c= c+1;
						//ID
						t.remove(g+2);
						//[
						t.remove(g+1);
						//align.addLine(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c));
						list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, "t"+String.valueOf(c-1) ,"t"+String.valueOf(c),subScope,scope,"-"));
						
						Token newT = new Token("t"+String.valueOf(c),"ID");	
						t.set(g, newT);
						c= c+1;
						i = temp;
						break;
					}
						g = g +1;
						}
						
						g = temp;
						while(true) {
							
							if(t.get(g).value.equals(";")) {
								
								break;
								
							}
							
							
							if(t.get(g).type.equals("ID") &&t.get(g+1).value.equals("[") &&t.get(g+2).type.equals("NUM") &&t.get(g+3).value.equals("]")) {
								
								
								//]
								t.remove(g+3);
								
								
								int size = Integer.valueOf(t.get(g+2).value)*4;
								
								//NUM
								t.remove(g+2);
								//[
								t.remove(g+1);
								//align.addLine(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"disp", t.get(g).value, String.valueOf(size) ,"t"+String.valueOf(c),subScope,scope,"-"));
								
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(g, newT);
								c= c+1;
								i = temp;
								break;
							}
								g = g +1;
							
						}
						
						
						g = temp;
						
						//nested function
						//boolean Nested = false;
						int nestedSpot = 0;
						while (true) {
							
							//System.out.println("HERE");
							//System.out.println(t.get(g).value);
							if(t.get(g).value.equals(";") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							
							
							if(t.get(g).type.equals("ID") && t.get(g+1).value.equals("(") ) {
								
								for(int k = g+1; k< t.size(); k++) {
									//System.out.println(t.get(k).value + " " + t.get(k+1).value);
									if(t.get(k).type.equals("ID") && t.get(k+1).value.equals("(")) {
										//System.out.println("Nested");
										nestedSpot = k;
										//System.out.println(nestedSpot);
									} else {
										
										nestedSpot = temp;
										//System.out.println("Not Nested");
									}
									
								}
								
								g = nestedSpot;
								while(true) {
									
									if(t.get(g).value.equals(")")) {
										break;	
									}
									
									if( t.get(g-1).value.equals("(") &&(t.get(g).type.equals("NUM") ||t.get(g).type.equals("FLOAT") || t.get(g).type.equals("ID") )  && t.get(g+1).value.equals(",")) {
										
										t.remove(g+1);
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g).value,subScope,scope,"-"));
										
										c= c+1;
										t.remove(g);
										g = nestedSpot;
										
									}
									
									if(t.get(g).value.equals("(") && (t.get(g+1).type.equals("NUM") ||t.get(g+1).type.equals("FLOAT") || t.get(g+1).type.equals("ID") )  &&  t.get(g+2).value.equals(")")) {
										
										// )
										t.remove(g+2);
										
										
										// arg
										//align.addLine(String.valueOf(c),"arg", "-", "-", t.get(g+1).value);
										list.add(new IMCODE(String.valueOf(c),"arg", "-", "-", t.get(g+1).value,subScope,scope,"-"));
										
										c= c+1;
										t.remove(g+1);
										
										// (
										t.remove(g);
										
										Token newT = new Token("t"+String.valueOf(c),"ID");	
									//	align.addLine(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"call",t.get(g-1).value,String.valueOf(tab.returnFunction(t.get(g-1).value, scope).params.size()), "t"+String.valueOf(c),subScope,scope,"-"));
										
										c = c+1;
										t.set(g-1, newT);
										
										
										break;
										
									}
									g = g+1;
								}
								

								
								
								
							
							}
							
							if(t.get(g).value.equals(";") || (t.get(g).type.equals("ID") && t.get(g+1).value.equals(";") )) {
								break;	
							}
							g = g+1;
						}
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						///does the parens
						while(true) {
												//lloks for the last ( and how deep it is
												while(stop == false) {
												//	System.out.println("backhere " + t.get(i).value);
													
													if( !t.get(i-1).type.equals("ID") && t.get(i).value.equals("(")) {
														Pc = Pc+1;
														total = Pc;
														//System.out.println(total+"yt");
														//break;
													} 
													if(t.get(i).value.equals(")") && Pc >1 ) {
														
														Pc = Pc-1;
														//break;
													} else if(t.get(i).value.equals(")") && Pc ==1 ) {
														Pc = Pc -1;
														stop=true;
														
										
														i = temp;
						
														break;
													}
													
													if(t.get(i).value.equals(";")) {
														stop=true;
														Pc = 0;
														i = temp;
														break;	
													}
													i = i+1;
												}
												
											
					//System.out.println(t.get(i+1).value + "ypyp");
						
						if(!t.get(i-1).type.equals("ID") &&t.get(i).value.equals("(")) {
							Pc2 = Pc2+1;
							int tempSpot = i;
							boolean c1 = false;
							boolean c2 = false;
							//System.out.println(Pc2 + " " + total);
							//System.out.println("HERE"+ total+ " "+Pc);
							//checks inside the paren
							if(Pc2 == total) {
								//System.out.println("NIGGA");
								//System.out.println("HERE");
								//checks the mult/div
								while(c1==false) {
									
									//System.out.println("yiyiyiiy");
									//mult
									if( !t.get(i-1).value.equals("]") && !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(") ) {
										//align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out3");
										break;
									//div
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										//System.out.println("Breaking Out");
										break;
										
									}
								
									if(t.get(i).value.equals(")")) {
										c1=true;
										i = tempSpot;
										//S//ystem.out.println("HERE");
										break;	
									}
									i = i+1;
								}
								//System.out.println("HERE");
								
								//does the add/sub
								while(c2==false) {
									//add
									
									
									if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										c= c+1;
										i = tempSpot;
										//break;
										
									//subtract
									} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-")&& !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
										//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
										list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
										
										t.remove(i+1);
										Token newT = new Token("t"+String.valueOf(c),"ID");	
										t.set(i, newT);
										t.remove(i-1);
										i = tempSpot;
										c= c+1;
										//break;
									}
									
									if(t.get(i).value.equals(")") && !t.get(i+2).value.equals("[")) {
										c2 = true;
										//System.out.println("HERE");
										//System.out.println(t.get(tempSpot).value);
										t.remove(i);
										t.remove(tempSpot);
										stop = false;
										i = temp;
										Pc2 = 0;
										break;
									}
									i = i+1;
								
								}
								
								
								
								
							
								
							}
						}
										
						i = i+1;
						
						if(t.get(i).value.equals(";")) {
							//System.out.println("NIGGA");	
							break;
						}
						
						}
						//System.out.println("tttt");
						//////////////////////////////////////////////////////////////////////
						i = temp;
						while(ck0==true) {
							
							//System.out.println("yiyiyiiy");
							//mult
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("*") && !t.get(i+2).value.equals("[") && !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"mult",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out3");
								break;
							//div
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("/") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"div",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								//System.out.println("Breaking Out");
								break;
								
							}
						
							if(t.get(i).value.equals(";")) {
								ck1=true;
								ck0=false;
								//S//ystem.out.println("HERE");
								break;	
							}
							i = i+1;
						}
						//System.out.println("tttt");
						
						i = temp;
						
						
						while(ck1==true) {
							//add
							if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("+") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"add",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								//System.out.println(t.get(i-1).value+ " " + t.get(i+1).value);
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
								c= c+1;
								break;
								
							//subtract
							} else if(!t.get(i-1).value.equals("]")&& !t.get(i-1).value.equals(")")&& t.get(i).value.equals("-") && !t.get(i+2).value.equals("[")&& !t.get(i+2).value.equals("(")) {
								//align.addLine(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c));
								list.add(new IMCODE(String.valueOf(c),"sub",t.get(i-1).value,t.get(i+1).value,"t"+String.valueOf(c),subScope,scope,"-"));
								
								t.remove(i+1);
								Token newT = new Token("t"+String.valueOf(c),"ID");	
								t.set(i, newT);
								t.remove(i-1);
						
								c= c+1;
								break;
							}
							
							if(t.get(i).value.equals(";")) {
								break;
							}
							i = i+1;
						
						}
						
						
						//break the arrays down
						for(int r = temp; r < t.size(); r++) {
							
						//count = count +1;
							///System.out.print(t.get(r).value);
							if(t.get(r).value.equals(";")) {
							//System.out.println(count);
								break;
								
							}
						}
						
						
						
						
				
						
						
						
					
						if((t.get(i-1).type.equals("ID") || t.get(i-1).type.equals("NUM") || t.get(i-1).type.equals("FLOAT")) && t.get(i).value.equals(";")) {
							//align.addLine(String.valueOf(c),"assign",t.get(i-1).value,"-", tm);
							list.add(new IMCODE(String.valueOf(c),"assign",t.get(i-1).value,"-", tm,subScope,scope,"-"));
							
							c = c+1;
							i = i -1;
							break;
						}
						if(count == 3) {
							break;
						}
						
					}
					
				}
				
				if(t.get(i).value.equals("else")) {
					
					//align.addLine(String.valueOf(c), "B", "-", "-", "sub");
					list.add(new IMCODE(String.valueOf(c), "B", "-", "-", "sub",subScope+1,scope,"e"));
					
					c= c+1;
				}
				
				
				
					
					
					//variables
					if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return") && t.get(i+1).type.equals("ID") && !t.get(i+2).value.equals("(")) {
						
						//Arrays Not done
						if(t.get(i+2).value.equals("[")) {
							if(t.get(i+3).type.equals("NUM")) {
								int size = Integer.valueOf(t.get(i+3).value)*4;
								//align.addLine(String.valueOf(c),"alloc",String.valueOf(size),"-",t.get(i+1).value);
								list.add(new IMCODE(String.valueOf(c),"alloc",String.valueOf(size),"-",t.get(i+1).value,subScope,scope,"-"));
								
								c= c+1;
							}
							
							
						} else {
						//align.addLine(String.valueOf(c),"alloc","4","-",t.get(i+1).value);
						list.add(new IMCODE(String.valueOf(c),"alloc","4","-",t.get(i+1).value,subScope,scope,"-"));
						
						c= c+1;
						}
					}
				}
				
				
				
			}
		//	System.out.println(" YUH");
			int y =0;
			int sub = -1;
			for (int p = 0 ; p < list.size(); p++) {
			
				//align.addLine(list.get(p).L,list.get(p).O,list.get(p).OP1,list.get(p).OP2,list.get(p).R, String.valueOf(list.get(p).sub),String.valueOf(list.get(p).S), list.get(p).C);
				if( list.get(p).C.equals("w") && list.get(p).sub != sub ) {
					//System.out.println("NIGGA"+ sub + " " + list.get(p).sub );
					//System.out.println(list.get(p).L + " oooorr" + list.get(p).O );
					String loc = list.get(p).L;
					
					sub = Integer.valueOf(list.get(p).sub);
					//System.out.println(sub);
					int temp = p;
					int temp2 =0;
					//sets the end of the while
					while(true) {
						
						//System.out.println(list.get(p).L + " "+p);
						if(Integer.valueOf(list.get(p).sub) < sub) {
							//System.out.println("NECT");
							list.add(p, new IMCODE(list.get(temp).L,"B","-","-",list.get(temp).L,list.get(temp).sub,0,"wd"));
							temp2 =p;
							break;
						}
						p = p +1;
					}
					p = temp;
					
					 y = 1;
					for (int h = 1 ; h < list.size(); h++) {
						list.get(h).L = String.valueOf(y);
						y = y+1;
						}
					
					
					p = temp;
				}
				
				if(list.get(p).C.equals("i") && list.get(p).O.equals("B") && list.get(p).sub != sub) {
					sub = Integer.valueOf(list.get(p).sub);
					if(list.get(p-2).O.equals("comp") ) {
						int q = p;
						while(true) {
							//System.out.println(sub  + " " + list.get(q).C + " "  +list.get(q).sub);
							if(list.get(q).O.equals("B") &&list.get(q).C.equals("e")&& !list.get(q-2).O.equals("comp") ) {
								
								int l = q;
								
								while(true) {
									if (list.get(l).sub < sub) {
										list.get(q).R = String.valueOf(Integer.valueOf(list.get(l).L));
										break;
									}
									
									l = l+1;
								}
								
							} 
							
							if (list.get(q).sub == sub  && list.get(q).C.equals("e")) {
								list.get(p).R = String.valueOf(Integer.valueOf(list.get(q).L)+1);
								break;
								
							} else if(list.get(q).sub < sub) {
								list.get(p).R = String.valueOf(Integer.valueOf(list.get(q).L));
								break;
							}	else if(q == list.size()-1) {
							
								break;
							}
							q= q+1;
						}
					}
					
				}
				
				y = 1;
				for (int h = 1 ; h < list.size(); h++) {
					list.get(h).L = String.valueOf(y);
					y = y+1;
					}
				
			} 
			
			
			
			for (int p = 0 ; p < list.size(); p++) {
				//System.out.println("PASS" + p);
				if(list.get(p).O.equals("B") && list.get(p).C.equals("w")&&list.get(p-2).O.equals("comp") ) {
					//System.out.println("HERE");
					
					int temp5= p;
					p = p+1;
					if(p == list.size()) {
						break;
					}
					int sub3 = list.get(p-1).sub;
					while(true) {
						if(p == list.size()) {
							break;
						}
						
						//System.out.println(list.get(p).O.equals("B") );
						//System.out.println(list.get(p).C.equals("wd"));
						///System.out.println(sub3);
						///.out.println("******");
						
						if(list.get(p).O.equals("B") && list.get(p).C.equals("wd") && list.get(p).sub == sub3 ) {
							//System.out.println("HERE");
							
							//System.out.println(list.get(p).O + String.valueOf(Integer.valueOf(list.get(p).L))+ "NIGGAJAJAJJAJAJA " +p);
							list.get(temp5).R = String.valueOf(Integer.valueOf(list.get(p).L) +1);
							break;
						}
						p = p+1;
					}
				
					p = temp5;
				}
				
				
				
			}
			
			
			
			for (int p = 0 ; p < list.size(); p++) {
				align.addLine(list.get(p).L,list.get(p).O,list.get(p).OP1,list.get(p).OP2,list.get(p).R);
				
			}
			
			 align.output((String s) -> System.out.println(s));
		}
		
		public void arrayCheck (ArrayList<Token> t) {
			int scope = 0;
			int subScope = 0;
			for(int i = 0; i < t.size(); i++) {
				//adds the scope
				
				if(t.get(i).value.equals("{")) {
					subScope = subScope +1;
				} else if(t.get(i).value.equals("}")) {
					subScope = subScope -1;
				}
				
			if(i < t.size()-3)	{
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return")) {
						
					if(t.get(i+1).type.equals("ID") || t.get(i+1).value.equals("main")) {
						if(t.get(i+2).value.equals("(")) {
							scope = scope + 1;
						}
						
					}
					
				}
			
			
			
				//sees an array
				if(!t.get(i).type.equals("KEYWORD") && t.get(i+1).type.equals("ID") && t.get(i+2).value.equals("[")) {
					//System.out.println("bally or locally");
					
					i = i + 1;
					//checks is the variable is local -*
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
							
							//checks for function - Done
							if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals("int") && t.get(i+1).value.equals("(")) {
								
								if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
									if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
										//System.out.println("Got it");
										int W = 0;
										i = i + 1;
										while(true) {
											if(t.get(i).value.equals("(") ) {
												W = W + 1;
												
											}	
											
											//System.out.println("HELOO " + t.get(i).value );
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
							}	else if(tab.Exists(t.get(i).value, scope,subScope) && t.get(i).type.equals("ID") && C==1 ) {
								
														//checks if the variable is an int
														//not function
														if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("int")) {
														
															//System.out.println("Going through here");
														} //else if(tab.returnSym(t.get(i).value, 0, true, false).type.equals("int")) {
														
														//} 
														//an array	local
														else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int")) {
															//System.out.println("NESTED LOCAL ARRAY");
															//checks the nested array
															if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
							} else if(tab.exists(t.get(i).value, 0,0) && t.get(i).type.equals("ID")  && C==1) {
								
											//checks if the variable is an int
											//not function
											if(tab.returnSym(t.get(i).value, scope, false, true, subScope).type.equals("int")) {
											
												//System.out.println("Going through here");
											}  else if(tab.returnSym(t.get(i).value, scope, false, true, subScope).type.equals("int")) {
												//System.out.println("NESTED GLOBAL ARRAY");
												if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
							} else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/") || t.get(i).value.equals("(") || t.get(i).value.equals(")") || t.get(i).type.equals("NUM")) {
							
							} else {
								//System.out.println(t.get(i).value);
								work = false;
								//ystem.out.println("The variable is not found globally or locally3");
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
									if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
							} else if(tab.Exists(t.get(i).value, scope,subScope) && t.get(i).type.equals("ID")  && C==1) {
								
								//checks if the variable is an int
								//not function
								if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("int")) {
								//function	
									//System.out.println("Going through here");
								} //else if(tab.returnSym(t.get(i).value, 0, true, false).type.equals("int")) {
									
									//} 
									//an array	local
									else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int")) {
										//System.out.println("NESTED LOCAL ARRAY");
										//checks the nested array
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
							} else if(tab.Exists(t.get(i).value, 0,0) && t.get(i).type.equals("ID")  && C==1 ) {
								
								//checks if the variable is an int
								//not function
								if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals("int")) {
								//function	not done
									//System.out.println("Going through here");
								} 
								 else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("int")) {
									//System.out.println("NESTED GLOBAL ARRAY");
									if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
							} else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/") || t.get(i).value.equals("(") || t.get(i).value.equals(")") || t.get(i).type.equals("NUM")) {
								
							}else {
								work = false;
								//System.out.println(tab.Exists(t.get(i).value, 0,0));
								//System.out.println("The variable is not found globally or locallyeeee");
								break;
							}
							
							i = i + 1;
						}  //end while
					} else {
						work = false;
						//System.out.println("The variable is not found globally or locallye");
						break;
					}
					
					
				}
				
			}
			
			}
		}
		
		
		public void functiomCallCheck(ArrayList<Token> t) {
			int subScope = 0;
			int scope = 0;
			for(int i = 0; i < t.size(); i++) {
				//adds the scope
				
				
				
			if(i < t.size()-3)	{
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return")) {
						
					if(t.get(i+1).type.equals("ID") || t.get(i+1).value.equals("main")) {
						if(t.get(i+2).value.equals("(")) {
							scope = scope + 1;
						}	
					}
				}
				
				
				if(t.get(i).value.equals("{")) {
					subScope = subScope +1;
				} else if(t.get(i).value.equals("}")) {
					subScope = subScope -1;
				}
				
				
				
				if(!t.get(i).type.equals("KEYWORD") && t.get(i+1).type.equals("ID") && t.get(i+2).value.equals("(") && scope >0) {	
					i = i + 1;
					//System.out.println("FUNCTION CALL");
					
					ArrayList<Symbol> params = new ArrayList<Symbol>();
					//checks to see if function exists before
					if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
						int C = 0;
						int call = 0;
						params = tab.returnFunction(t.get(i).value, scope).params;
						
				
						i = i +1;
						while(true) {
							
						
						
						//for void types
						if(params.get(call).type.equals("void")) {
							if(t.get(i).value.equals(")") && C == 1) {
								break;
							} else if(t.get(i).value.equals("(") && C ==0) {
								C = C+1;
								i = i +1;
							}else {
								work = false;
								//System.out.println("This function has no paramters");
								break;
							}
						} 
						
						
						
						
						
						
						//type int no  array
						if(params.get(call).type.equals("int") && params.get(call).array ==false) {
							
							
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
							} else if(t.get(i).value.equals(",") && C == 1 ) {
								call = call +1;
								i = i + 1;
							}
							
							
							
							
							
							
							//number
							else if(t.get(i).type.equals("NUM")) {
								i = i +1;
								
							//looks for local vari
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false)) && (tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("int") || tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int"))){
								
								
								
								
								
								
								//checks to see varible is type int
								if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("int")) {
									i = i +1;
								//array int
								} else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int")) {
										if(t.get(i+1).value.equals("[")) {
											if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
										if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
										//System.out.println("Function has not been defined yet");
										break;
									}
								
							} else {
								
								work = false;
								//System.out.println("NOT TYPE INT or the variables dont exist local/gloyyyybally");
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
							
							
							
							
							
							
							
							
							if(t.get(i).type.equals("ID") && tab.exists(t.get(i).value, scope, false, true) && tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int")){
								if(!t.get(i+1).value.equals("[")) {
									if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int")) {
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
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false) ) && (tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("float") || tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("float"))){
								
								//checks to see varible is type int
								if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("float")) {
									i = i +1;
								//an array
								} else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("float")) {
									if(t.get(i+1).value.equals("[")) {
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
									if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
									//System.out.println("Function has not been defined yet");
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
							
							
							
							if(t.get(i).type.equals("ID") && tab.exists(t.get(i).value, scope, false, true) && tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("float")){
								if(!t.get(i+1).value.equals("[")) {
									if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("float")) {
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
							} else if(t.get(i).type.equals("ID") && tab.exists(t.get(i).value, 0, false, true) && tab.returnSym(t.get(i).value, 0, false, true,subScope).type.equals("float")) {
								if(!t.get(i+1).value.equals("[")) {
									if(tab.returnSym(t.get(i).value, 0, false, true,subScope).type.equals("float")) {
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
						//System.out.println("This function has not been defined yet");
						break;
					}
					
				}
				
				
				
				
				
				
			}
		  }//end for
		}
		
		
		
		public void returnCheck(ArrayList<Token> t) {
			int returnC = 0;
			int scope = 0;
			int subScope = 0;
			String type = "";
			for(int i = 0; i < t.size(); i++) {
				//adds the scope
				
			
				
			if(i < t.size()-3)	{
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return")) {
						type = t.get(i).value;
					if(t.get(i+1).type.equals("ID") || t.get(i+1).value.equals("main")) {
						if(t.get(i+2).value.equals("(")) {
							scope = scope +1;
							int Q = 0;
							i = i + 1;
							while(true) {
								
								if(t.get(i).value.equals("{")) {
									subScope = subScope +1;
								} else if(t.get(i).value.equals("}")) {
									subScope = subScope -1;
								}
								
								
								
								
								//System.out.println(t.get(i).value + " y " + type);
								if(t.get(i).value.equals("{") ) {
									Q = Q + 1;
									
								}	
								//kills the 
								if(t.get(i).value.equals("}") && Q == 1) {
									if(type.equals("int") || type.equals("float")) {
										if(returnC > 0) {
											returnC = 0;
										} else {
											work = false;
											//System.out.println("There is no return");
											break;
										}
									}
									break;
								} else if(t.get(i).value.equals("}")) {
									Q = Q - 1;
								}
								i = i + 1;
								
								//void
								if(type.equals("void")) {
									
									//the return for void
									if(t.get(i).value.equals("return")) {
										if(!(t.get(i+1).type.equals("ID") || t.get(i+1).type.equals("NUM") || t.get(i+1).type.equals("FLOAT"))) {
											
										}else {
											work = false;
											//System.out.println("You can't return a value with a void function");
											break;
										}
									} 
									
								} else if(type.equals("int")) {
									
									if(t.get(i).value.equals("return")) {
										if((t.get(i+1).type.equals("ID") || t.get(i+1).type.equals("NUM") )) {
											i = i+1;
											returnC = returnC +1;
											while(true) {
											
												//System.out.println(t.get(i).value);
												if(t.get(i).value.equals(";")) {
													break;
												}
												
												
												if(t.get(i).value.equals("(") ) {
													
													i = i +1;
													
												}	
												
												
												 else if(t.get(i).value.equals(")")) {
													
													i = i +1;
												}
												
												//+ - * /
												else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/")) {
													i = i +1;
												} 
												
												
												
												
												
												
												//number
												else if(t.get(i).type.equals("NUM")) {
													i = i +1;
													
												//looks for local vari
												} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false)) && (tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("int") || tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int"))){
													
													
													
													
													
													
													//checks to see varible is type int
													if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("int")) {
														i = i +1;
													//array int
													} else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("int")) {
															if(t.get(i+1).value.equals("[")) {
																if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
															if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
															//System.out.println("this");
															if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
															//System.out.println("Function has not been defined yet");
															break;
														}
													
												} else {
												
													work = false;
													//System.out.println("NOT TYPE INT or the variables dont exist local/gloyyyybally");
													break;
												}
												
												
												
												
												
												
												
												
												
												
											}
										}else {
											work = false;
											//System.out.println("You can't return a float with an int function");
											break;
										}
									} 
									
								} else if (type.equals("float")) {
									
									if(t.get(i).value.equals("return")) {
										if((t.get(i+1).type.equals("ID") || t.get(i+1).type.equals("FLOAT") )) {
											i = i+1;
											returnC = returnC +1;
											while(true) {
											
												//System.out.println(t.get(i).value);
												if(t.get(i).value.equals(";")) {
													//System.out.println("HOPPING OUT");
													break;
												}
												
												
												if(t.get(i).value.equals("(") ) {
													
													i = i +1;
													
												}	
												
												
												 else if(t.get(i).value.equals(")")) {
													
													i = i +1;
												}
												
												//+ - * /
												else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/")) {
													i = i +1;
												} 
												
												
												
												
												
												
												//number
												else if(t.get(i).type.equals("FLOAT")) {
													i = i +1;
													
												//looks for local vari
												} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false)) && (tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("float") || tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("float"))){
													
													
													
													
													
													
													//checks to see varible is type int
													if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals("float")) {
														i = i +1;
													//array int
													} else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals("float")) {
															if(t.get(i+1).value.equals("[")) {
																if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
													//an array
													} else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals("float")) {
														if(t.get(i+1).value.equals("[")) {
															if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
												//found a function that exists
												}else if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals("float") && t.get(i+1).value.equals("(")) {
														
														if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
															if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
																//System.out.println("Got it");
																int W = 0;
																i = i + 1;
																while(true) {
																	if(t.get(i).value.equals("(") ) {
																		W = W + 1;
																		
																	}	
																	//kills the 
																	if(t.get(i).value.equals(")") && W == 1) {
																		i = i+1;
																		//System.out.println("i");
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
															//System.out.println("Function has not been defined yet eee");
															break;
														}
													
												} else {
												
													work = false;
													//System.out.println("NOT TYPE INT or the variables dont exist local/gloyyyybally");
													break;
												}
												
												
												
												
												
												
												
												
												
												
											}
										}else {
											work = false;
											//System.out.println("You can't return a float with an int function");
											break;
										}
									} 
									
									
								}
								
								
								
								
								
								
								
								
								
								
							}
						}
						
					}
					
				}
				
			}
		
		}
			
	}
	
					
		public void setCheck(ArrayList<Token> t) {
			int scope = 0;
			int subScope = 0;
			String type = "";
			for(int i = 0; i < t.size(); i++) {
				//adds the scope
				if(t.get(i).value.equals("{")) {
					subScope = subScope +1;
				} else if(t.get(i).value.equals("}")) {
					subScope = subScope -1;
				}
			
				
			if(i < t.size()-3)	{
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return")) {
						type = t.get(i).value;
					if(t.get(i+1).type.equals("ID") || t.get(i+1).value.equals("main")) {
						if(t.get(i+2).value.equals("(")) {
						scope = scope + 1;
						}
					}
					
				}
				
				if( (t.get(i).value.equals("{") || t.get(i).value.equals(";") )&& t.get(i+1).type.equals("ID")  && !t.get(i+2).value.equals("(") ) {
					if(!(tab.exists(t.get(i+1).value, scope, false, true) || tab.exists(t.get(i+1).value, scope, false, false)) && !(tab.exists(t.get(i+1).value, 0, false, true) || tab.exists(t.get(i+1).value, 0, false, false))) {
						work = false;
						//System.out.println("variable not found");
						break;
					}
				}
				
				//ID local
				if((t.get(i).value.equals("{") || t.get(i).value.equals(";")) && t.get(i+1).type.equals("ID") && (tab.exists(t.get(i+1).value, scope, false, true) || tab.exists(t.get(i+1).value, scope, false, false)) && !t.get(i+2).value.equals("(")) {
					i = i +1;
					if(tab.exists(t.get(i).value, scope, false, true)) {
						type = tab.returnSym(t.get(i).value, scope, false, true,subScope).type;
						if(t.get(i+1).value.equals("[")) {
							if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
								//System.out.println("The nested array fucked up4");
								break;
							}
						}else {
							work = false;
							//System.out.println("You need to take a value from the array");
							break;
						}
					} else if( tab.exists(t.get(i).value, scope, false, false)) {
						
						type = tab.returnSym(t.get(i).value, scope, false, false,subScope).type;
						i = i + 1;
					}
					
					
					
					
					if(t.get(i).value.equals("=")) {
						i = i +1;
					} else {
						work = false;
						//System.out.println(t.get(i).value + " " + t.get(i+1).value + t.get(i+2).value);
						//System.out.println("You neeed to assign values f");
						break;
					}
					
					while(true) {
						
						//System.out.println(t.get(i).value);
						if(t.get(i).value.equals(";")) {
							break;
						}
						
						
						if(t.get(i).value.equals("(") ) {
							
							i = i +1;
							
						}	
						
						
						 else if(t.get(i).value.equals(")")) {
							
							i = i +1;
						}
						
						//+ - * /
						else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/")) {
							i = i +1;
						} 
						
						
						
						
						
						
						//number
						else if(t.get(i).type.equals("NUM") && type.equals("int")) {
							i = i +1;
							
						
						} else if(t.get(i).type.equals("FLOAT") && type.equals("float")) {
							i = i +1;
							
						//looks for local vari
						} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false)) && (tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals(type) || tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals(type))){
							
							
							
							
							
							
							//checks to see varible is type int
							if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals(type)) {
								i = i +1;
							//array int
							} else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals(type)) {
									if(t.get(i+1).value.equals("[")) {
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
								//System.out.println("NOT Correct TYPE");
								break;
							}
							
						//looks fo global vari	
						} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, 0, false, true) || tab.exists(t.get(i).value, 0, false, false)) && (tab.returnSym(t.get(i).value, 0, false, false,0).type.equals(type) || tab.returnSym(t.get(i).value, 0, false, true,0).type.equals(type))){
							
							//checks for type int
							if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals(type)) {
								i = i +1;
							//an array
							} else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals(type)) {
								if(t.get(i+1).value.equals("[")) {
									if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
								//System.out.println("Correect type");
								break;
							}
						//found a function that exists
						}else if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals(type) && t.get(i+1).value.equals("(")) {
								
								if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
									//System.out.println("this");
									if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
									//System.out.println("Function has not been defined yet");
									break;
								}
							
						} else {
						
							work = false;
							//System.out.println("NOT TYPE INT or the variables dont exist local/gloyyyybally");
							break;
						}
						
						
						
						
						
						
						
						
						
						
					}
					
					
					
				//global
				} else if((t.get(i).value.equals("{") || t.get(i).value.equals(";")) && t.get(i+1).type.equals("ID") && (tab.exists(t.get(i+1).value, 0, false, true) || tab.exists(t.get(i+1).value, 0, false, false)) && !t.get(i+2).value.equals("(")) {
					
					i = i +1;
					if(tab.exists(t.get(i).value, 0, false, true)) {
						type = tab.returnSym(t.get(i).value, 0, false, true,0).type;
						if(t.get(i+1).value.equals("[")) {
							if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
					} else if( tab.exists(t.get(i).value, 0, false, false)) {
						
						type = tab.returnSym(t.get(i).value, 0, false, false,0).type;
						i = i + 1;
					}
					
					
					if(t.get(i).value.equals("=")) {
						
					} else {
						work = false;
						
						//System.out.println("You neeed to assign values");
						break;
					}
				
			 } 
				
				
			
			
			
			}
			
			
			
			
			
			}
		}
		
		
		
		public void booleanCheck(ArrayList<Token> t) {
			
			int subScope = 0;
			int scope = 0;
			String type = "";
			for(int i = 0; i < t.size(); i++) {
				//adds the scope
				
				if(t.get(i).value.equals("{")) {
					subScope = subScope +1;
				} else if(t.get(i).value.equals("}")) {
					subScope = subScope -1;
				}
				
			if(i < t.size()-3)	{
				if(t.get(i).type.equals("KEYWORD") && !t.get(i).value.equals("return")) {
						type = t.get(i).value;
					if(t.get(i+1).type.equals("ID") || t.get(i+1).value.equals("main")) {
						if(t.get(i+2).value.equals("(")) {
						scope = scope + 1;
						}
					}
					
				}
				
			}
			
			if(   (t.get(i).value.equals("if") || t.get(i).value.equals("while") ) && t.get(i+1).value.equals("(")    ) {
					i = i +2;
					
					if(t.get(i).type.equals("NUM")) {
						i = i +1;
						type = "int";
						
					
					} else if(t.get(i).type.equals("FLOAT")) {
						type = "float";
						i = i +1;
						
					
					}
					
					else if(tab.exists(t.get(i).value, scope, false, true)) {
						type = tab.returnSym(t.get(i).value, scope, false, true,subScope).type;
						if(t.get(i+1).value.equals("[")) {
							if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
								//System.out.println("The nested array fucked up4");
								break;
							}
						}else {
							work = false;
							//System.out.println("You need to take a value from the array");
							break;
						}
					} else if( tab.exists(t.get(i).value, scope, false, false)) {
						
						type = tab.returnSym(t.get(i).value, scope, false, false,subScope).type;
						i = i + 1;
					}
					
					else if(tab.exists(t.get(i).value, 0, false, true)) {
						type = tab.returnSym(t.get(i).value, 0, false, true,0).type;
						if(t.get(i+1).value.equals("[")) {
							if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
								//System.out.println("The nested array fucked up4");
								break;
							}
						}else {
							work = false;
							//System.out.println("You need to take a value from the array");
							break;
						}
					} else if( tab.exists(t.get(i).value, 0, false, false)) {
						
						type = tab.returnSym(t.get(i).value, 0, false, false,0).type;
						i = i + 1;
					} else if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope) && t.get(i+1).value.equals("(") ) {
						type = tab.returnFunction(t.get(i).value, scope).type;
						if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
						//System.out.println("Variable has not been defined or does nor exist");
						break;
					}
					
					if(type.equals("void")) {
						work = false;
						//System.out.println("You cannot have void types");
						break;
							
						
					}
					
					
						while(true) {
							
							//System.out.println(t.get(i).value + " 6");
							if(t.get(i).value.equals("<") || t.get(i).value.equals(">")  || t.get(i).value.equals(">=") || t.get(i).value.equals("<=")|| t.get(i).value.equals("==")|| t.get(i).value.equals("!=")) {
								break;
							}
							
							
							if(t.get(i).value.equals("(") ) {
								
								i = i +1;
								
							}	
							
							
							 else if(t.get(i).value.equals(")")) {
								
								i = i +1;
							}
							
							//+ - * /
							else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/")) {
								i = i +1;
							} 
							
							
							
							
							
							
							//number
							else if(t.get(i).type.equals("NUM") && type.equals("int")) {
								i = i +1;
								
							
							} else if(t.get(i).type.equals("FLOAT") && type.equals("float")) {
								i = i +1;
								
							//looks for local vari
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false)) && (tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals(type) || tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals(type))){
								
								
								
								
								
								
								//checks to see varible is type int
								if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals(type)) {
									i = i +1;
								//array int
								} else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals(type)) {
										if(t.get(i+1).value.equals("[")) {
											if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
									//System.out.println("NOT Correct TYPE");
									break;
								}
								
							//looks fo global vari	
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, 0, false, true) || tab.exists(t.get(i).value, 0, false, false)) && (tab.returnSym(t.get(i).value, 0, false, false,0).type.equals(type) || tab.returnSym(t.get(i).value, 0, false, true,0).type.equals(type))){
								
								//checks for type int
								if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals(type)) {
									i = i +1;
								//an array
								} else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals(type)) {
									if(t.get(i+1).value.equals("[")) {
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
									//System.out.println("Correect type");
									break;
								}
							//found a function that exists
							}else if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals(type) && t.get(i+1).value.equals("(")) {
									
									if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
										//System.out.println("this");
										if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
										//System.out.println("Function has not been defined yet");
										break;
									}
								
							} else {
							
								work = false;
								//System.out.println("NOT TYPE INT or the variables dont exist local/gloyyyybally  r");
								break;
							}
							
							
							
							
							
							
							
							
							
							
						}
						
						i = i + 1;
						
						
							while(true) {
							
							//System.out.println(t.get(i).value);
							//System.out.println(type);
							if(t.get(i).value.equals("{")) {
								subScope = subScope + 1;
								break;
							}
							
							
							if(t.get(i).value.equals("(") ) {
								
								i = i +1;
								
							}	
							
							
							 else if(t.get(i).value.equals(")")) {
								
								i = i +1;
							}
							
							//+ - * /
							else if(t.get(i).value.equals("+") || t.get(i).value.equals("*") || t.get(i).value.equals("-") || t.get(i).value.equals("/")) {
								i = i +1;
							} 
							
							
							
							
							
							
							//number
							else if(t.get(i).type.equals("NUM") && type.equals("int")) {
								i = i +1;
								
							
							} else if(t.get(i).type.equals("FLOAT") && type.equals("float")) {
								i = i +1;
								
							//looks for local vari
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, scope, false, true) || tab.exists(t.get(i).value, scope, false, false)) && (tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals(type) || tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals(type))){
								
								
								
								
								
								
								//checks to see varible is type int
								if(tab.returnSym(t.get(i).value, scope, false, false,subScope).type.equals(type)) {
									i = i +1;
								//array int
								} else if(tab.returnSym(t.get(i).value, scope, false, true,subScope).type.equals(type)) {
										if(t.get(i+1).value.equals("[")) {
											if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
									//System.out.println("NOT Correct TYPE");
									break;
								}
								
							//looks fo global vari	
							} else if(t.get(i).type.equals("ID") && (tab.exists(t.get(i).value, 0, false, true) || tab.exists(t.get(i).value, 0, false, false)) && (tab.returnSym(t.get(i).value, 0, false, false,0).type.equals(type) || tab.returnSym(t.get(i).value, 0, false, true,0).type.equals(type))){
								
								//checks for type int
								if(tab.returnSym(t.get(i).value, 0, false, false,0).type.equals(type)) {
									i = i +1;
								//an array
								} else if(tab.returnSym(t.get(i).value, 0, false, true,0).type.equals(type)) {
									if(t.get(i+1).value.equals("[")) {
										if(tab.nestedArray(t.get(i).value, scope, tab, t, i,subScope)) {
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
									//System.out.println("Correect type");
									break;
								}
							//found a function that exists
							}else if(t.get(i).type.equals("ID") && tab.returnFunction(t.get(i).value, scope).type.equals(type) && t.get(i+1).value.equals("(")) {
									
									if(tab.seeIfFunctionHasBeenDefined(t.get(i).value, scope)) {
										//System.out.println("this");
										if(tab.nestedFunctionCall(t.get(i).value, scope, tab, t, i,subScope)) {
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
										//System.out.println("Function has not been defined yet");
										break;
									}
								
							} else {
							
								work = false;
								//System.out.println(t.get(i).type + " Scope: " + subScope);
								//System.out.println("NOT TYPE INT or the variables dont exist local/gloyyyybally 55");
								break;
							}
							
							
							
							
							
							
							
							
							
							
						}
					
					
				
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
			
			
			
			
		}
		
	

		

		
		
		
		
}

