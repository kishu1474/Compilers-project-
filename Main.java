package project_compilers;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	static String[][] variables = new String[10][2];
	static int[][] values = new int[10][2];
	static int k = 0;
	static int v = 0;
	static int count = 0;

	public static void main(String args[]) {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Please Give Input: ");
		// String[] tokens = new String[10];

		while (scanner.hasNextLine()) {

			String tokens = scanner.nextLine();
			if (tokens == null || tokens.isEmpty()) {
				break;
			}
			System.out.println("token: " + tokens);
			
			if(!tokens.contains(";")) {
				System.out.println("without semicolon not allowed!");
				break;
			}
			String[] lines = tokens.split(";", -2);

			for (int i = 0; i < lines.length; i++) {
				lines[i] = lines[i].replaceAll("\\s", "");

				if (lines[i] == null || lines[i].isEmpty()) {
					continue;
				} else {
					System.out.println("line: " + lines[i]);
					statement(lines[i]);
				}
				System.out.println();
			}
		}
		scanner.close();

	}

	public static void statement(String lines) {

		String[] s = lines.split("=", 2);
		
		String identifier = s[0];
		String expression = s[1];

		String[] splitted = expression.split("[-+*()]");
		for (int i = 0; i < splitted.length; i++) {
				System.out.println("..here.."+splitted[i]+"..");
				for(int q = 0; q <= k-1; q++) {
					int w = 1;
					for(w = q+w; w <= k; w++) {
						System.out.println("exp: "+expression);
						if(splitted[i].equals(variables[q][0])) {
							expression = expression.replace
									(splitted[i], variables[q][1]);
							System.out.println(".."+expression+"..");
						}
					}
				}
		}

		System.out.println("k: " + k); 
		
		variables[k][0] = identifier;
		variables[k][1] = expression;
		
		identifier = identifier(variables[k][0]);
		
		expression = expression(variables[k][1]);
		
		System.out.println("Identifier!"+identifier);
		if(identifier.equals("")) {
			System.out.println("Invalid Identifier!");
			System.exit(0); 
		}
		System.out.println("Expression!"+expression);
		if(expression.equals("")) {
			System.out.println("Invalid Value!");
			System.exit(0); 
		}
		
		String statement = identifier + " = " + expression;

		System.out.println("output statement: " + statement);
		
		variables[k][0] = identifier;
		variables[k][1] = expression;
		
		for(int q = 0; q <= k; q++) { 
			System.out.println("final: "+variables[q][0] +"="+variables[q][1]); 
		}
		
		k++; 
	}

	public static String expression(String expression) {

		System.out.println("expression: " + expression);
		String final_exp = "";
		String final_term = "";
		String final_expression_output = "";
		String regex = "[+-]?[1-9]+";
		int e = 0, t = 0, et_output = 0;
		
		//String expression = Integer.toString(exp);

		
		  if (expression.contains("*")) {
			  System.out.println("with * ");
			  final_term += term(expression);
			  final_expression_output += final_term;
			  System.out.println("final_expression_output: " + final_expression_output);
			  return final_expression_output; 
		  }
		 
		
		if (!expression.contains("+") && !expression.contains("-")) {
			System.out.println("without + or -");
			final_term += term(expression);
			final_expression_output += final_term;
			System.out.println("final_expression_output: " + final_expression_output);
			return final_expression_output;
		}

		for(int l=0; l<expression.length(); l++) {
			if (expression.charAt(l) == '(') {
				System.out.println("in (");
				int j;
				for (j = l + 1; j < expression.length(); j++) {
					if (expression.charAt(j) == ')') {
						System.out.println(")");
						int p;
						for (p = j + 1; p < expression.length(); p++) {
							if (expression.charAt(p) == '+') {
								System.out.println("in exp + ");
								final_exp += expression(expression.substring(l, j+1));
								System.out.println("final_exp in exp + : "+final_exp);
								final_term += expression(expression.substring(p+1));
								System.out.println("final_term in term + : "+final_term);
								final_expression_output += final_exp +"+"+final_term;
								final_expression_output = expression(final_expression_output);
								System.out.println("final_expression_output in ( ) +"+
										final_expression_output);
								return final_expression_output;
							}
							else if (expression.charAt(p) == '-') {
								System.out.println("in exp - ");
								final_exp += expression(expression.substring(l, j+1));
								final_term += expression(expression.substring(p+1));
								System.out.println("final_exp in exp - : "+final_exp);
								System.out.println("final_exp in term - : "+final_term);
								final_expression_output += final_exp +"-"+ final_term;
								final_expression_output = expression(final_expression_output);
								System.out.println("final_expression_output in ( ) -"+
										final_expression_output);
								return final_expression_output;
							}
						}
						j = p;
						break;
					}
				}
				l = j;
				break;
			}
		}	
		
		for (int i = 0; i < expression.length(); i++) {
			
			if(expression.charAt(0) == '+' || expression.charAt(0) == '-'
					|| expression.charAt(0) == '(' || expression.contains("*")) {
					
				final_term += term(expression);
				final_expression_output += final_term;
				System.out.println("final_expression_output: " + final_expression_output);
				return final_expression_output;
			}
			else {
				if (expression.charAt(i) == '+') {
					System.out.println("in +");
	
					final_exp += term(expression.substring(0, i));
					final_term += expression(expression.substring(i + 1));
					System.out.println("final_exp: " + final_exp);
					System.out.println("final_term: " + final_term);
	
					Pattern p = Pattern.compile(regex);
					Matcher m_1 = p.matcher(final_exp);
					Matcher m_2 = p.matcher(final_term);
	
					if ((m_1.find() && m_1.group().equals(final_exp)) 
							&& (m_2.find() && m_2.group().equals(final_term))) {
	
						System.out.println("in + of int");
						e += Integer.parseInt(final_exp);
						t += Integer.parseInt(final_term);
						et_output += e + t;
						final_expression_output += Integer.toString(et_output);
						System.out.println("final_expression_output in int: " 
								+ final_expression_output);
						return final_expression_output;
					} else {
						final_expression_output += final_exp + final_term;
						return final_expression_output;
					}
				} else if (expression.charAt(i) == '-') {
					System.out.println("in -");
	
					final_exp += expression(expression.substring(0, i));
					final_term += term(expression.substring(i + 1));
	
					Pattern p = Pattern.compile(regex);
					Matcher m_1 = p.matcher(final_exp);
					Matcher m_2 = p.matcher(final_term);
	
					if ((m_1.find() && m_1.group().equals(final_exp)) 
							&& (m_2.find() && m_2.group().equals(final_term))) {
	
						System.out.println("in - of int");
						e += Integer.parseInt(final_exp);
						t += Integer.parseInt(final_term);
						et_output += e - t;
						final_expression_output += Integer.toString(et_output);
						return final_expression_output;
					} else {
						final_expression_output += final_exp + final_term;
						return final_expression_output;
					}
				}
			}
		}
		System.out.println("final_expression_output: " + final_expression_output);
		return final_expression_output;
	}

	public static String term(String term) {

		System.out.println("term: " + term);
		String final_term = "";
		String final_fact = "";
		String final_term_output = "";
		int e = 0, t = 0, et_output = 0;
		String regex = "[+-]?[1-9]+";
		
		//String term = Integer.toString(ter);

		if (!term.contains("*")) {
			System.out.println("without *");
			final_fact += fact(term);
			final_term_output += final_fact;
			return final_term_output;
		}
		
		for (int i = 0; i < term.length(); i++) {
			if (term.charAt(i) == '*') {
				System.out.println("in *");

				final_term += term(term.substring(0, i));
				final_fact += fact(term.substring(i + 1));
				System.out.println("final_term: " + final_term);
				System.out.println("final_fact: " + final_fact);

				Pattern p = Pattern.compile(regex);
				Matcher m_1 = p.matcher(final_term);
				Matcher m_2 = p.matcher(final_fact);

				if ((m_1.find() && m_1.group().equals(final_term)) 
						&& (m_2.find() && m_2.group().equals(final_fact))) {

					System.out.println("in * of int");
					e += Integer.parseInt(final_term);
					t += Integer.parseInt(final_fact);
					et_output += e * t;
					final_term_output += Integer.toString(et_output);
					System.out.println("final_expression_output in int: " 
							+ final_term_output);
					return final_term_output;
				} else {
					final_term_output += final_term + final_fact;
					return final_term_output;
				}
			}
		}

		System.out.println("final_term_output: " + final_term_output);
		return final_term_output;
	}

	public static String fact(String fact) {

		System.out.println("fact: " + fact);
		String final_fact_1 = "";
		String final_fact_output = "";
		int t = 0, et_output = 0;
		String regex = "[+-]?[1-9]+";
		
		for(int p=0; p<fact.length(); p++) {
			if(fact.charAt(p) == '-') {
				count++;
			}
			else {
				break;
			}
		}
		System.out.println("count before going: "+count);
		if(fact.length() == 1) {
			System.out.println("fact length: "+fact.length());
			count = 0;
		}
		
		//String fact = Integer.toString(fac);

		if (!fact.contains("+") && !fact.contains("-") 
				&& !fact.contains("(") && !fact.contains(")")) {
			
			System.out.println("without + or - or ( or )");
			final_fact_1 = literal(fact);
			
			if(final_fact_1.equals("")) {
				final_fact_1 = identifier(fact);
			}
		}

		for (int i = 0; i < fact.length(); i++) {
			if (fact.charAt(0) == '(') {
				System.out.println("in fact (");
				int j;
				for (j = i + 1; j < fact.length(); j++) {
					if (fact.charAt(j) == ')') {
						System.out.println("in fact )");
						final_fact_1 += expression(fact.substring(i + 1, j));
						final_fact_output += final_fact_1;
						System.out.println("final_fact_output: " + final_fact_output);
						
						return final_fact_output;
					}
				}
				i = j;
			}
			else if (fact.charAt(0) == '-') {
				System.out.println("in fact -");
				System.out.println("count: " + count);
				if(count % 2 == 1) {
					final_fact_1 += "-" + fact(fact.substring(count));	
					final_fact_output += final_fact_1;
				}
				else {
					final_fact_1 += fact(fact.substring(count));	
					final_fact_output += final_fact_1;
				}
				
				System.out.println("final_fact_output: " + final_fact_output);
				return final_fact_output;
			}
			else if (fact.charAt(0) == '+') {
				System.out.println("in fact +");
				
				final_fact_1 += fact(fact.substring(i+1));	
				final_fact_output += final_fact_1;
				
				System.out.println("final_fact_output: " + final_fact_output);				
				return final_fact_output;
			}
		}	
		
		final_fact_output += final_fact_1;
		System.out.println("final_fact_output: " + final_fact_output);
		
		return final_fact_output;
	}

	public static String identifier(String identifier) {

		System.out.println("identifier: " + identifier);
		for (int i = 0; i < identifier.length(); i++) {

			boolean output_1 = false, output_2 = false;
			if (i == 0) {
				output_1 = output_2 = letter(identifier.charAt(i));
				//System.out.println("in id if o-1: "+output_1);
				//System.out.println("in id if o-2: "+output_2);
				if (output_1 == false && output_2 == false) {
					return "";
				}
			} else {
				output_1 = letter(identifier.charAt(i));
				output_2 = digit(identifier.charAt(i));
				//System.out.println("in id else o-1: "+output_1);
				//System.out.println("in id else o-2: "+output_2);
				if (output_1 == false && output_2 == false) {
					return "";
				}
			}
		}
		return identifier;
	}

	public static boolean letter(char letter) {

		System.out.println("letter: " + letter);
		String new_letter = String.valueOf(letter);

		if (new_letter.matches("^[a-zA-Z]*$") || new_letter.equals("_")) {
			System.out.println("in letter true");
			return true;
		}
		return false;
	}

	public static String literal(String literal) {

		System.out.println("literal: " + literal);
		for (int i = 0; i < literal.length(); i++) {

			boolean output_1 = false;
			if (i == 0) {
				if (literal.charAt(i) == '0') {
					if(literal.length() > 1) {
						if(literal.charAt(1) != '0') {
							output_1 = true;
						}
						else {
							output_1 = false;
							System.out.println("Can't have 2 zeros at starting!");
							return "";
						}			
					}	
					output_1 = true;
				}
				else {
					output_1 = non_zero_digit(literal.charAt(i));
				}
			} 
			else {
				output_1 = digit(literal.charAt(i));
			}
			
			if (output_1 == false) {
				return "";
			}
		}
		return literal;
	}

	public static boolean non_zero_digit(char non_zero_digit) {

		System.out.println("non_zero_digit: " + non_zero_digit);
		String new_non_zero_digit = String.valueOf(non_zero_digit);
		String regex = "[+-]?[1-9]+";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(new_non_zero_digit);

		if (m.find() && m.group().equals(new_non_zero_digit)) {
			System.out.println("in non digit true");
			return true;
		} else {
			return false;
		}

	}

	public static boolean digit(char digit) {

		System.out.println("digit: " + digit);
		String new_digit = String.valueOf(digit);
		String regex = "[+-]?[0-9]+";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(new_digit);

		if (m.find() && m.group().equals(new_digit)) {
			System.out.println("in digit true");
			return true;
		} else {
			return false;
		}
	}

}
