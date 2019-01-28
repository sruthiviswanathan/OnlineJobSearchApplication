package com.zilker.onlinejobsearch.utils;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.zilker.onlinejobsearch.constants.Constants;



public class Validation {

	public Scanner scanner = new Scanner(System.in);
	private static Pattern pattern;
	private static Matcher matcher;

	
	/*
	 * method for email validation
	 */
	public boolean emailValidation(String email) {
			
			boolean isvalid = false;
			pattern = Pattern.compile(Constants.EMAILPATTERN);
			matcher = pattern.matcher(email);
			if (matcher.matches() == true) {
				isvalid = true;
			} 
		return isvalid;
	}
	
	/*
	 * method for password validation
	 */
	public boolean passwordValidation(String password) {
		
		boolean isvalid = false;
		pattern = Pattern.compile(Constants.PASSWORDPATTERN);
		matcher = pattern.matcher(password);
		if (matcher.matches() == true) {
			isvalid = true;
		}
		return isvalid;
	}
	
	/*
	 * method for String validation
	 */
	public boolean StringValidation(String check) {
			
			boolean isvalid = false;
			pattern = Pattern.compile(Constants.CHARACTERSONLY);
			matcher = pattern.matcher(check);
			if (matcher.matches() == true) {
				isvalid = true;
			} 
		return isvalid;
	}
	/*
	 * method for Website URL validation
	 */
	public boolean urlValidation(String check) {
			
			boolean isvalid = false;
			pattern = Pattern.compile(Constants.URL);
			matcher = pattern.matcher(check);
			if (matcher.matches() == true) {
				isvalid = true;
			} 
		return isvalid;
	}
	/*
	 * method for Float point number validation
	 */
	public boolean floatValidation(String check) {
			
			boolean isvalid = false;
			pattern = Pattern.compile(Constants.FLOATNUMBERS);
			matcher = pattern.matcher(check);
			if (matcher.matches() == true) {
				isvalid = true;
			} 
		return isvalid;
	}
	
	/*
	 * method for Integer validation
	 */
	public boolean intValidation(String check) {
			
			boolean isvalid = false;
			pattern = Pattern.compile(Constants.INTEGERNUMBERS);
			matcher = pattern.matcher(check);
			if (matcher.matches() == true) {
				isvalid = true;
			} 
		return isvalid;
	}

	public boolean ratingValidation(String check) {
		// TODO Auto-generated method stub
		boolean isvalid = false;
		pattern = Pattern.compile(Constants.RATING);
		matcher = pattern.matcher(check);
		if (matcher.matches() == true) {
			isvalid = true;
		} 
	return isvalid;
	}
	
	
}
