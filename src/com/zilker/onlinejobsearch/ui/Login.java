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
		Scanner scanner = new Scanner(System.in);
		try {
			GetUserInput getUserInput = new GetUserInput();
			boolean value=true;
			do {
				logger.log(Level.INFO, "ENTER YOUR CHOICE \n 1.NEW USER? REGISTER \n 2.EXISTING USER? LOGIN \n 3.EXIT");
				int ch = scanner.nextInt();
				switch (ch) {
				case 1:
					boolean flag=true;
					do{
					logger.log(Level.INFO, "ENTER YOUR CHOICE \n 1.REGISTER AS A USER \n 2.REGISTER AS A COMPANY ADMIN \n 3.BACK" );
					int choice=scanner.nextInt();
					switch(choice){
					case 1:
						getUserInput.register();
						main(null);
						break;
					case 2:
						getUserInput.registerAsAdmin();
						main(null);
						break;
					case 3:
						flag=false;
						main(null);
						break;
					default:
						logger.log(Level.INFO, "PLEASE SELECT A VALID CHOICE");
						break;	
					}
					}while(flag);
					break;
				case 2:
					getUserInput.login();
					break;
				case 3:
					logger.log(Level.INFO, "BYE");
					value =false;
					System.exit(0);
					break;
				default:
					logger.log(Level.INFO, "PLEASE SELECT A VALID CHOICE");
					break;
				}
			} while (value == true);
			logger.log(Level.INFO, "BYE");
		} catch (Exception e) {

		} finally {
			scanner.close();
		}
	}

}
