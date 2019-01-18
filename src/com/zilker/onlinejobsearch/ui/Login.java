package com.zilker.onlinejobsearch.ui;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login {
	private static final Logger logger = Logger.getLogger(Login.class.getName());
/*
 * Main class that gets loaded first
 */
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		try {
			GetUserInput getinput = new GetUserInput();
			String value="true";
			do {
				logger.log(Level.INFO, "enter your choice \n 1.New user?REGISTER \n 2.Existing user?LOGIN \n 3.EXIT");
				int ch = sc.nextInt();
				switch (ch) {
				case 1:
					getinput.register();
					break;
				case 2:
					getinput.login();
					break;
				case 3:
					logger.log(Level.INFO, "BYE");
					value ="false";
					System.exit(0);
					break;
				default:
					logger.log(Level.INFO, "select a valid choice");
					break;
				}
			} while (value.equals("true"));
			logger.log(Level.INFO, "BYE");
		} catch (Exception e) {

		} finally {
			sc.close();
		}
	}

}
