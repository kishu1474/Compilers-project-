package project_compilers;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	static String[][] variables = new String[10][2];
	static int k = 0;
	static int count = 0;

	public static void main(String args[]) {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Please Give Input: ");

		while (scanner.hasNextLine()) {

			String tokens = scanner.nextLine();
			if (tokens == null || tokens.isEmpty()) {
				break;
			}
			
			if(!tokens.contains(";")) {
				System.out.println("Without semicolon not allowed !!");
				break;
			}
			String[] lines = tokens.split(";", -2);

			for (int i = 0; i < lines.length; i++) {
				lines[i] = lines[i].replaceAll("\\s", "");

				if (lines[i] == null || lines[i].isEmpty()) {
					continue;
				} 
				else {
					statement(lines[i]);
				}
			}
		}
		scanner.close();
	}

	public static void statement(String lines) {

		String[] s = lines.split("=", 2);
		
		if(s.length < 2) {
			System.out.println("Uninitialized Variable !!");
			System.exit(0);
		}
		
		String identifier = s[0];
		String expression = s[1];

		String[] splitted = expression.split("[-+*()]");
		for (int i = 0; i < splitted.length; i++) {
				
				for(int q = 0; q <= k-1; q++) {
					
					int w = 1;
					for(w = q+w; w <= k; w++) {
						
						if(splitted[i].equals(variables[q][0])) {
							expression = expression.replace(splitted[i], variables[q][1]);
						}
					}
				}
		}

		variables[k][0] = identifier;
		variables[k][1] = expression;
		
		identifier = identifier(variables[k][0]);
		
		expression = expression(variables[k][1]);
		
		int c = 0;
		for(int t=0; t<expression.length(); t++) {
			if(expression.charAt(t) == '-') {
				c++;
			}
		}
		expression = expression.substring(c);
		if(c % 2 == 1) {
			expression = "-" + expression;
		}
		
		if(identifier.equals("")) {
			System.out.println("Invalid Identifier!");
			System.exit(0); 
		}

		if(expression.equals("")) {
			System.out.println("Invalid Value!");
			System.exit(0); 
		}
		
		String statement = identifier + " = " + expression;

		System.out.println("Output Statement: " + statement);
		
		variables[k][0] = identifier;
		variables[k][1] = expression;
		
		k++; 
	}

	public static String expression(String expression) {

		String final_exp = "";
		String final_term = "";
		String final_expression_output = "";
		String regex = "[+-]?[1-9]+";
		int e = 0, t = 0, et_output = 0;
	
		  if (expression.contains("*")) {
			  
			  final_term += term(expression);
			  final_expression_output += final_term;
			  
			  return final_expression_output; 
		  }
		 
		
		if (!expression.contains("+") && !expression.contains("-")) {
			
			final_term += term(expression);
			final_expression_output += final_term;
			
			return final_expression_output;
		}

		for(int l=0; l<expression.length(); l++) {
			
			if (expression.charAt(l) == '(') {
				
				int j;
				for (j = l + 1; j < expression.length(); j++) {
					
					if (expression.charAt(j) == ')') {
						
						int p;
						for (p = j + 1; p < expression.length(); p++) {
							
							if (expression.charAt(p) == '+') {
								
								final_exp += expression(expression.substring(l, j+1));
								final_term += expression(expression.substring(p+1));
								
								final_expression_output += final_exp +"+"+final_term;
								final_expression_output = expression(final_expression_output);
								
								return final_expression_output;
							}
							else if (expression.charAt(p) == '-') {
								
								final_exp += expression(expression.substring(l, j+1));
								final_term += expression(expression.substring(p+1));
								
								final_expression_output += final_exp +"-"+ final_term;
								final_expression_output = expression(final_expression_output);
								
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
				
				return final_expression_output;
			}
			else {
				if (expression.charAt(i) == '+') {
					
					final_exp += term(expression.substring(0, i));
					final_term += expression(expression.substring(i + 1));
	
					Pattern p = Pattern.compile(regex);
					Matcher m_1 = p.matcher(final_exp);
					Matcher m_2 = p.matcher(final_term);
	
					if ((m_1.find() && m_1.group().equals(final_exp)) 
							&& (m_2.find() && m_2.group().equals(final_term))) {
	
						e += Integer.parseInt(final_exp);
						t += Integer.parseInt(final_term);
						et_output += e + t;
						final_expression_output += Integer.toString(et_output);
						
						return final_expression_output;
					} 
					else {
						
						final_expression_output += final_exp + final_term;
						return final_expression_output;
					}
				} 
				else if (expression.charAt(i) == '-') {
					
					for(int j = i+1; j < expression.length(); j++) {
						
						if(expression.charAt(j) == '-') {
							
							String additional = "";
							int r = 0;
							final_exp += expression(expression.substring(0, i));
							final_term += term(expression.substring(i+1,j));
							additional += term(expression.substring(j+1));
								
							Pattern p = Pattern.compile(regex);
							Matcher m_1 = p.matcher(final_exp);
							Matcher m_2 = p.matcher(final_term);
							Matcher m_3 = p.matcher(additional);
			
							if ((m_1.find() && m_1.group().equals(final_exp)) 
									&& (m_2.find() && m_2.group().equals(final_term))
									&& (m_3.find() && m_3.group().equals(additional))) {
			
								e += Integer.parseInt(final_exp);
								t += Integer.parseInt(final_term);
								r += Integer.parseInt(additional);
								et_output += e - t - r;
								
								final_expression_output += Integer.toString(et_output);
								return final_expression_output;
							} 
							else {
								final_expression_output += final_exp + final_term + additional;
								return final_expression_output;
							}									
						}
					}
					
					final_exp += expression(expression.substring(0, i));
					final_term += term(expression.substring(i + 1));
	
					Pattern p = Pattern.compile(regex);
					Matcher m_1 = p.matcher(final_exp);
					Matcher m_2 = p.matcher(final_term);
	
					if ((m_1.find() && m_1.group().equals(final_exp)) 
							&& (m_2.find() && m_2.group().equals(final_term))) {
	
						e += Integer.parseInt(final_exp);
						t += Integer.parseInt(final_term);
						et_output += e - t;
						final_expression_output += Integer.toString(et_output);
						
						return final_expression_output;
					} 
					else {
						
						final_expression_output += final_exp + final_term;
						return final_expression_output;
					}
				}
			}
		}
		return final_expression_output;
	}

	public static String term(String term) {

		String final_term = "";
		String final_fact = "";
		String final_term_output = "";
		int e = 0, t = 0, et_output = 0;
		String regex = "[+-]?[1-9]+";

		if (!term.contains("*")) {

			final_fact += fact(term);
			final_term_output += final_fact;
			
			return final_term_output;
		}
		
		for (int i = 0; i < term.length(); i++) {
			
			if (term.charAt(i) == '*') {

				final_term += term(term.substring(0, i));
				final_fact += fact(term.substring(i + 1));

				Pattern p = Pattern.compile(regex);
				Matcher m_1 = p.matcher(final_term);
				Matcher m_2 = p.matcher(final_fact);

				if ((m_1.find() && m_1.group().equals(final_term)) 
						&& (m_2.find() && m_2.group().equals(final_fact))) {

					e += Integer.parseInt(final_term);
					t += Integer.parseInt(final_fact);
					et_output += e * t;
					final_term_output += Integer.toString(et_output);

					return final_term_output;
				}
				else {
					
					final_term_output += final_term + final_fact;
					return final_term_output;
				}
			}
		}
		return final_term_output;
	}

	public static String fact(String fact) {

		String final_fact_1 = "";
		String final_fact_output = "";
		
		for(int p=0; p<fact.length(); p++) {
			if(fact.charAt(p) == '-') {
				count++;
			}
			else {
				break;
			}
		}

		if(fact.length() == 1) {
			count = 0;
		}

		if (!fact.contains("+") && !fact.contains("-") 
				&& !fact.contains("(") && !fact.contains(")")) {

			final_fact_1 = literal(fact);
			
			if(final_fact_1.equals("")) {
				final_fact_1 = identifier(fact);
			}
		}

		for (int i = 0; i < fact.length(); i++) {
			
			if (fact.charAt(0) == '(') {

				int j;
				for (j = i + 1; j < fact.length(); j++) {
					
					if (fact.charAt(j) == ')') {
	
						final_fact_1 += expression(fact.substring(i + 1, j));
						final_fact_output += final_fact_1;
						
						return final_fact_output;
					}
				}
				i = j;
			}
			else if (fact.charAt(0) == '-') {
	
				if(count % 2 == 1) {
					final_fact_1 += "-" + fact(fact.substring(count));	
					final_fact_output += final_fact_1;
				}
				else {
					final_fact_1 += fact(fact.substring(count));	
					final_fact_output += final_fact_1;
				}

				return final_fact_output;
			}
			else if (fact.charAt(0) == '+') {
				
				final_fact_1 += fact(fact.substring(i+1));	
				final_fact_output += final_fact_1;
			
				return final_fact_output;
			}
		}	
		
		final_fact_output += final_fact_1;

		return final_fact_output;
	}

	public static String identifier(String identifier) {

		for (int i = 0; i < identifier.length(); i++) {

			boolean output_1 = false, output_2 = false;
			if (i == 0) {
				output_1 = output_2 = letter(identifier.charAt(i));
				
				if (output_1 == false && output_2 == false) {
					return "";
				}
			} else {
				output_1 = letter(identifier.charAt(i));
				output_2 = digit(identifier.charAt(i));
				
				if (output_1 == false && output_2 == false) {
					return "";
				}
			}
		}
		return identifier;
	}

	public static boolean letter(char letter) {

		String new_letter = String.valueOf(letter);

		if (new_letter.matches("^[a-zA-Z]*$") || new_letter.equals("_")) {
			
			return true;
		}
		else {
			return false;
		}	
	}

	public static String literal(String literal) {

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

		String new_non_zero_digit = String.valueOf(non_zero_digit);
		String regex = "[+-]?[1-9]+";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(new_non_zero_digit);

		if (m.find() && m.group().equals(new_non_zero_digit)) {

			return true;
		} 
		else {
			return false;
		}

	}

	public static boolean digit(char digit) {

		String new_digit = String.valueOf(digit);
		String regex = "[+-]?[0-9]+";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(new_digit);

		if (m.find() && m.group().equals(new_digit)) {

			return true;
		} 
		else {
			return false;
		}
	}

}
