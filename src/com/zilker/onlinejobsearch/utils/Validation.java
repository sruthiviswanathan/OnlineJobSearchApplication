package com.zilker.onlinejobsearch.utils;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zilker.onlinejobsearch.constants.Constants;
import com.zilker.onlinejobsearch.ui.GetUserInput;
/*
 * method for email validation
 */
public class Validation {
	private static final Logger logger = Logger.getLogger(GetUserInput.class.getName());
	public Scanner sc = new Scanner(System.in);
	private static Pattern pattern;
	private static Matcher matcher;
	
	public String emailValidation() {
		int counter = 0;
		String email = "";
		do {
			logger.log(Level.INFO, "enter emailId (eg: username@domain.com)");
			String emailId = sc.nextLine();
			pattern = Pattern.compile(Constants.EMAILPATTERN);
			matcher = pattern.matcher(emailId);
			if (matcher.matches() == true) {
				email = String.valueOf(emailId);
				counter = 1;
				break;
			} else {
				logger.log(Level.INFO, "enter valid email ID");
			}
		} while (counter != 1);

		return email;
	}
	/*
	 * method for password validation
	 */
	public String passwordValidation() {
		int counter = 0;
		String pswd = "";
		do {
			logger.log(Level.INFO, "enter password(8-10characters,at least one letter and one number:)");
			String password = sc.nextLine();
			pattern = Pattern.compile(Constants.PASSWORDPATTERN);
			matcher = pattern.matcher(password);
			if (matcher.matches() == true) {
				pswd = String.valueOf(password);
				counter = 1;
				break;
			} else {
				logger.log(Level.INFO, "enter valid password");
			}
		} while (counter != 1);

		return pswd;
	}
}
