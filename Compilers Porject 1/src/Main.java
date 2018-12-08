import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Main {
	
	
	public static int counter = 0;
	public static boolean sLine = false;

	public static void main(String args[]) throws IOException {
		
		IntermediateFile TokenFile = new IntermediateFile("Tokens.txt");
		
		
		//Puts the file into the scanner
		Scanner fileReader = new Scanner(new File(args[0]));
		ArrayList<Token> token = new  ArrayList<Token>();

		while(fileReader.hasNextLine()) {
			
			String brokenLine[] = fileReader.nextLine().split("\\s+");	
			//dumpArray(brokenLine);
			//System.out.println("YYY");
			passThroughArray(brokenLine, TokenFile, token);
		} 
		TokenFile.CloseFile();
		
		
		
		Parser parser = new Parser( new File("Tokens.txt") );
		 
		 Semantic sem = new Semantic(token);
		 
		if(parser.Error == false) {
		 
		 //Semantic sem = new Semantic(token);
		 
		 if(sem.work == true) {
		// System.out.println("ACCEPT");
			 
		} else{
		//System.out.println("REJECT");
		 }
		 
		} else {
		//	System.out.println("REJECT Parser");
		}
	
	
	}
	

	
	//Used this to dump the array when testing the logic (Will Most Likely Delete this
	public static void dumpArray(String[] Array) {
		
		for(int i = 0; i < Array.length; i++) {
			
			System.out.println(Array[i]);
		}
	}
	
	public static void passThroughArray(String[] Array, IntermediateFile file, ArrayList<Token> List) {
		
		KeyWordTable Table = new KeyWordTable();
		DelimeterChecker DelTable = new DelimeterChecker();
		SpecialSymbols SymbolTable = new SpecialSymbols();
		NumberChecker NumberTable = new NumberChecker();
		legalCharacters characterTable = new legalCharacters();
	
		
		for(int i = 0; i < Array.length; i++) {
			
			//Mulit Line Comment
			if( Array[i].equals("/*") && sLine == false) {
			//file.WriteToFile(Array[i] + " ");
			counter = counter + 1;
		    //Single Line Comment
			} else if( Array[i].equals("//") && sLine == false) {
				//for(int j = i; j < Array.length; j++) {
				//file.WriteToFile(Array[j] + " ");
				//}
				//i = Array.length;
				sLine = true;
			//Closing MultiLine
			} else if( Array[i].equals("*/") && counter > 0 ) {
			
			counter = counter - 1;
			} 
			else if( Table.isItAKeyWord(Array[i]) && sLine == false && counter == 0) {
				file.WriteLineToFile(/*"KEYWORD: " + */Array[i]);
				List.add(new Token(Array[i], "KEYWORD"));
				//System.out.println(/*"KEYWORD: " + */Array[i]);
			//Delimeter	& Special Symbol
			} else if( (DelTable.isItADelimeter(Array[i]) || SymbolTable.isItASpecialSymbol(Array[i]) ) && sLine == false && counter == 0 ) {
				file.WriteLineToFile(Array[i] );
				List.add(new Token(Array[i]));
				//System.out.println(Array[i]);
			//Int
			} else if ( NumberTable.isItANumber(Array[i]) && counter == 0) {
				file.WriteLineToFile("NUM"/* Array[i]*/);
				List.add(new Token(Array[i], "NUM"));
				//System.out.println("NUM"/* Array[i]*/);
			//Error Not Valid Charcter	
			} else if( !characterTable.isItALegalCharacter(Array[i]) && (Array[i].length() == 1) && sLine == false && counter == 0) {
				
				//file.WriteLineToFile("ERROR " + "'" + Array[i] + "'" + " is not a valid character");
				//System.out.println("ERROR " + "'" + Array[i] + "'" + " is not a valid character");
			//Comments if sinle line comment is seen else where
			} else if(sLine == true) {
			
			} else if (SymbolTable.isItASpecialSymbol(Array[i]) && counter == 0) {
			
				file.WriteLineToFile(Array[i]);
				List.add(new Token(Array[i]));
				//System.out.println(Array[i]);
			
				
			}	else {
			
				//Goes to Split the text	
				String[] brokenString = Array[i].split("");
				
				analyzeGrouping(brokenString, file , List);
				
			}
		}
		
		sLine = false;
			
}
		
		
		
		
	
	
	public static void analyzeGrouping(String[] Array, IntermediateFile file, ArrayList<Token> List) {
		
		String temp = "";
		boolean ESYM = false;
		DelimeterChecker DelTable = new DelimeterChecker();
		SpecialSymbols SymbolTable = new SpecialSymbols();
		NumberChecker NumberTable = new NumberChecker();
		legalCharacters characterTable = new legalCharacters();
		KeyWordTable Table = new KeyWordTable();
		boolean fuckUp = false;
		
		for(int i = 0; i < Array.length; i++) {
				
			//Illegal Character
			if( characterTable.isItALegalCharacter(Array[i]) ) {
				temp = temp + Array[i];
				if(counter > 0) {
					temp = "";
				}
			} else {
			ESYM = true;	
			
			}
			
			//Can Peak Ahead
			if( i < Array.length -1 ) {
				// Checks the !=
				if(Array[i].equals("!") && Array[i+1].equals("=")) {
					ESYM = false;
					fuckUp = true;
				} 
	
				if(temp.equals("") && ESYM == true && counter == 0) {
					//file.WriteLineToFile("ERROR " + "'" + Array[i] + "'" + " is not a valid character");
					//System.out.println("ERROR " + "'" + Array[i] + "'" + " is not a valid character");
					ESYM = false;
					
				} else if(Array[i].equals("/") && Array[i+1].equals("*")) {
					counter = counter + 1;
					i = i+1;
				
				
				} else if(Array[i].equals("*") && Array[i+1].equals("/") && counter > 0) {
					counter = counter - 1;
					i = i+1;
					if(counter == 0) {
						
						//file.WriteLineToFile("");
						//System.out.println("");
						temp = "";
					}
				//prints out mulitline comments
				}else if(counter > 0) {
					
				} else if(temp.equals("") && counter == 0 ) {
					//Special Symbol	
				} else if( SymbolTable.isItASpecialSymbol(temp) && counter == 0) {
									
										
										//single line comment	
										if( temp.equals("/") && Array[i+1].equals("/") ) {
											//for(int j = i; j < Array.length; j++) {
												//file.WriteToFile(Array[j]);
												//}
												i = Array.length;
												sLine = true;
										//expression
										} else if( temp.equals("=") && Array[i+1].equals("=") ||
												   temp.equals(">") && Array[i+1].equals("=") ||
												   temp.equals("<") && Array[i+1].equals("=") ) {
										
												 file.WriteLineToFile(temp + Array[i+1]);
												 List.add(new Token(temp+Array[i+1]));
												 i = i + 1;
												 temp = "";				
										} else {
											//Will print the special symbol by itself
											file.WriteLineToFile(temp);
											List.add(new Token(temp, "S"));
											//System.out.println(temp);
											temp = "";
										}
				//Delimeter			
				} else if(DelTable.isItADelimeter(temp) && counter == 0) {
						file.WriteLineToFile(temp);
						List.add(new Token(temp));
						//System.out.println(temp);
						temp = "";
				//Number		
				} else if( NumberTable.isStringANumber(temp) && counter == 0) {
					
							int deep = 0;
							
							//If the letter is not a number then the writer will print
							if( !NumberTable.isItANumber(Array[i+1]) && !Array[i+1].equals(".") && !Array[i+1].equals("E") ) {
								file.WriteLineToFile("NUM" +temp /* + temp*/);
								List.add(new Token(temp, "NUM"));
								//System.out.println("NUM" /*+ temp*/);
								temp = "";
							//Float1
							} else if (!NumberTable.isItANumber(Array[i+1]) && Array[i+1].equals("E")) {
								deep = deep + 1;
								int State = 4;
								String temp3 = temp + Array[i+1];
								boolean print = false;
							
								for(int k = i + 2; k < Array.length; k++) {
										//State 4
										if(State == 4 && (Array[k].equals("+") || Array[k].equals("-") || NumberTable.isItANumber(Array[k])) ) {
											deep = deep + 1;
											State = 5;
											temp3 = temp3 + Array[k];
											
											if(NumberTable.isItANumber(Array[k])) {
												State = 6;
											}
										//State 5
										} else if(State == 5 && NumberTable.isItANumber(Array[k]) ) {
											deep = deep + 1;
											State = 6;
											temp3 = temp3 + Array[k];
										//State 6
										} else if(State == 6 && NumberTable.isItANumber(Array[k]) ) {
											deep = deep + 1;
											temp3 = temp3 + Array[k];
											if(k == Array.length -1) {
												file.WriteLineToFile("FLOAT" /*+ temp3*/);
												List.add(new Token(temp3, "FLOAT"));
												//System.out.println("FLOAT"/* + temp3*/);
												print = true;
												temp = "";
												i = Array.length;
											}
											
										} else {
											//Error StaTE 4
											if(State == 4 && (!Array[k].equals("+") || !Array[k].equals("-") || !NumberTable.isItANumber(Array[k])) && print == false) {
												file.WriteLineToFile("NUM"/* + temp*/);
												List.add(new Token(temp, "NUM"));
												//System.out.println("NUM"/* + temp*/);
												temp = "";
												print = true;
											//ERROR STATE 5
											} else if(State == 5 && !NumberTable.isItANumber(Array[k]) && print == false ) {
												file.WriteLineToFile("NUM" /*+ temp*/);
												List.add(new Token(temp, "NUM"));
												//System.out.println("NUM" /*+ temp*/);
												print = true;
												temp = "";
											//Error State 6
											} else if(State == 6 && !NumberTable.isItANumber(Array[k]) && print == false) {
												file.WriteLineToFile("FLOAT" /* + temp3*/);
												List.add(new Token(temp3, "FLOAT"));
												//System.out.println("FLOAT" /*+ temp3*/);
												print = true;
												temp = "";
												i = i + deep;
											}
											
										}
								}
								
								if(State == 6 && print == false) {
									file.WriteLineToFile("FLOAT" /* + temp3 */);
									List.add(new Token(temp3, "FLOAT"));
									//System.out.println("FLOAT"/* + temp3*/);
									temp = "";
									i = i + deep;
								}
							//Float2
							} else if(!NumberTable.isItANumber(Array[i+1]) && Array[i+1].equals(".")) {
							
							
								deep = deep + 1;
								boolean print = false;
								String temp3 = temp + Array[i+1];
								String temp6 = "";
								int deep3 = 0;
								int State = 2;
								for(int k = i + 2; k < Array.length; k++ ) {
									
										
										//State 2
										if(State == 2 && NumberTable.isItANumber(Array[k]) ) {
											deep = deep + 1;
											State = 3;
											temp3 = temp3 + Array[k];
										//State 3
										} else if(State == 3 && NumberTable.isItANumber(Array[k]) ) {
											deep = deep + 1;
											deep3 = deep;
											temp3 = temp3 + Array[k];
										//State 3	
										} else if(State ==3 && Array[k].equals("E")) {
											deep = deep + 1;
											State = 4;
											temp6 = temp3 + Array[k];
											//End of Array is reached
											if(k == Array.length -1) {
												file.WriteLineToFile("FLOAT"/* + temp3*/);
												List.add(new Token(temp3, "FLOAT"));
												//System.out.println("FLOAT" /*+ temp3*/);
												
												print = true;
												temp = "";
												i = Array.length;
											}
										//State 4
										} else if(State == 4 && (Array[k].equals("+") || Array[k].equals("-") || NumberTable.isItANumber(Array[k])) ) {
											deep = deep + 1;
											State = 5;
											temp6 = temp6 + Array[k];
											if(NumberTable.isItANumber(Array[k])) {
												State = 6;
											}
										//State 5
										} else if(State == 5 && NumberTable.isItANumber(Array[k]) ) {
											deep = deep + 1;
											State = 6;
											temp6 = temp6 + Array[k];
										//State 6
										} else if(State == 6 && NumberTable.isItANumber(Array[k]) ) {
											deep = deep + 1;
											temp6 = temp6 + Array[k];
										} else {
											//State 2 error
											if(State == 2 && !NumberTable.isItANumber(Array[k]) && print == false) {
												file.WriteLineToFile("NUM" /* + temp*/);
												List.add(new Token(temp, "NUM"));
												//System.out.println("NUM"/* + temp*/);
												temp = "";
												print = true;
											
											//State 3 error
											} else if(State == 3 && (!NumberTable.isItANumber(Array[k]) || !Array[k].equals("E") ) && print == false) {
												file.WriteLineToFile("FLOAT"/* + temp3*/);
												List.add(new Token(temp3, "FLOAT"));
												//System.out.println("FLOAT"/* + temp3*/);
												
												//This is where the fuck up happened you can change back to 1 if gives you problems later
												print = true;
												temp = "";
												i = i + deep3+2;
												
											//State 4 Error	
											} else if(State == 4 && (!Array[k].equals("+") || !Array[k].equals("-") || !NumberTable.isItANumber(Array[k])) && print == false) {
												file.WriteLineToFile("FLOAT"/* + temp3*/);
												List.add(new Token(temp3, "FLOAT"));
												//System.out.println("FLOAT"/* + temp3*/);
												print = true;
												i = i + deep3;
												temp = "";
												
											//State 5 Erreor	
											} else if(State == 5 && !NumberTable.isItANumber(Array[k])  && print == false) {
												file.WriteLineToFile("FLOAT"/* + temp3*/);
												List.add(new Token(temp3, "FLOAT"));
												//System.out.println("FLOAT"/* + temp3*/);
												print = true;
												temp = "";
												i = i + deep3;
												
											} else if( State == 6 && !NumberTable.isItANumber(Array[k])  && print == false) {
												file.WriteLineToFile("FLOAT" /* + temp6*/);
												List.add(new Token(temp6, "FLOAT"));
												//System.out.println("FLOAT"/* + temp6*/);
												print = true;
												temp = "";
												i = i + deep;
												
											}
										}							
								}
									//prints if there were no errors
									if(print == false && print == false) {
										if(State == 3) {
											file.WriteLineToFile("FLOAT" /*+ temp3*/);
											List.add(new Token(temp3, "FLOAT"));
											//System.out.println("FLOAT" /*+ temp3*/);
											temp = "";
											i = i + 1 + deep;
										} else if(State == 6) {
											file.WriteLineToFile("FLOAT" /*+ temp6*/);
											List.add(new Token(temp6, "FLOAT"));
											//System.out.println("FLOAT" /*+ temp6*/);
											temp = "";
											i = i + 1 + deep;
										}
									}
									temp = "";		
							}		
				//ID
				} else if( (NumberTable.isStringANumber(Array[i+1]) || DelTable.isItADelimeter(Array[i+1]) || SymbolTable.isItASpecialSymbol(Array[i+1]) || ESYM == true ) && counter == 0 && !Table.isItAKeyWord(temp)) {
					if(!temp.equals(" ") && !temp.equals("")) {
					file.WriteLineToFile("ID" /*+ temp*/);
					List.add(new Token(temp, "ID"));
					//System.out.println("ID" /*+ temp*/);
					}
					if(ESYM == true) {
						//file.WriteLineToFile("ERROR" + "'" + Array[i] + "'" + " is not a valid character");
						//System.out.println("ERROR" + "'" + Array[i] + "'" + " is not a valid character");
						ESYM = false;
					
					}
					temp = "";
					
				//Key Word
				} else if( (Table.isItAKeyWord(temp) && ( NumberTable.isStringANumber(Array[i+1]) || DelTable.isItADelimeter(Array[i+1]) || SymbolTable.isItASpecialSymbol(Array[i+1]) )) && counter == 0 ) {
					file.WriteLineToFile(/*KEYWORD" */temp);
					List.add(new Token(temp, "KEYWORD"));
					//System.out.println(/*KEYWORD" */temp);
					temp = "";
				}
				
				if(fuckUp == true && counter == 0) {
					
					file.WriteLineToFile("!=");
					List.add(new Token("!="));
					//System.out.println("!=");
					i = i + 1;
					fuckUp = false;
					}	
				
				} else {
					//Number
					if( NumberTable.isItANumber(temp) || NumberTable.isStringANumber(temp) && counter == 0) {
					file.WriteLineToFile("NUM" /*+ temp*/);	
					List.add(new Token(temp, "NUM"));
					//System.out.println("NUM" /*+ temp*/);
					//Special Symbol	
					} else if( SymbolTable.isItASpecialSymbol(temp) && counter == 0 ) {
						
					file.WriteLineToFile(temp);
					List.add(new Token(temp));
					//System.out.println(temp);
					//Delimeter
					} else if( DelTable.isItADelimeter(temp)  && counter == 0) {
					file.WriteLineToFile(temp);
					List.add(new Token(temp));
					//System.out.println(temp);
					//Error
					} else if( Table.isItAKeyWord(temp) ) {
					file.WriteLineToFile(/*KEYWORD" */temp);
					List.add(new Token(temp, "KEYWORD"));
					//System.out.println(/*KEYWORD" */temp);
							
					}else if(!characterTable.isItALegalCharacter(Array[i]) && counter == 0) {
					
						if(!temp.equals("") && !temp.equals(" ")) {
							file.WriteLineToFile("ID" /*+ temp*/);
							List.add(new Token(temp, "ID"));
							//System.out.println("ID" /*+ temp*/);
							temp ="";
						}
					//file.WriteLineToFile("ERROR " + "'" + Array[i] + "'" + " is not a valid character");	
					//System.out.println("ERROR " + "'" + Array[i] + "'" + " is not a valid character");
					//ID
					} else {
						if(!temp.equals("") && counter == 0) {
							file.WriteLineToFile("ID" /*+ temp*/);
							List.add(new Token(temp, "ID"));
							//System.out.println("ID"/* + temp*/);

						}
							
					
					}
		}
		
	}
  }
}

/*
 * functions declared int or float  must have a return value of the correct type. ---- DONE
 
 * -void functions may or may not have a return, but must not return a value  ------ DONE

	parameters and arguments agree in number - parameters and arguments agree in type ---- DONE

	operand agreement 
	
	operand/operator agreement

	array index agreement - Done **************

	variable declaration (all variables must be declared ... scope) - ----- DONE
	variable declaration (all variables declared once ... scope) -------- DONE

	each program must have one main function ---- done
	
	

	return only simple structures --- done

	id's should not be type void - Done *************

	each function should be defined (actually a linker error) -- DONE
 * 
 * 
 */
	 


