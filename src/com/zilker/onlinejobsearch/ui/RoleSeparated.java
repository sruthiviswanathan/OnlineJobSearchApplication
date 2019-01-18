package com.zilker.onlinejobsearch.ui;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * separated flow based on login credentials.(user|company admin|website admin)
 */
public class RoleSeparated {
	private static final Logger logger = Logger.getLogger(RoleSeparated.class.getName());
	public Scanner sc = new Scanner(System.in);
	public void userFlow() {
		try {
			String value="true";
			do {
				GetUserInput getinput = new GetUserInput();
				logger.log(Level.INFO,
						"\n enter your choice \n 1.SEARCH FOR A COMPANY \n 2.SEARCH FOR A JOB  \n 3.WRITE ABOUT THE INTERVIEW PROCESS OF JOB \n 4.WRITE A REVIEW AND GIVE RATING FOR A COMPANY  \n 5.LEAVE A NOTE \n 6.DELETE YOUR ACCOUNT \n 7.BACK");
				int ch = sc.nextInt();
				switch (ch) {
				case 1:
					getinput.searchCompany();
					break;
				case 2:
					getinput.searchJobs();
					break;
				case 3:
					getinput.writeInterviewProcess();
					break;
				case 4:
					getinput.reviewAndRateCompany();
					break;
				case 5:
					getinput.requestVacancy();
					break;
				case 6:
					getinput.deleteUserAccount();
					break;
				case 7:
					value = "false";
					Login.main(null);
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

	public void adminFlow() {
		
		try {
			String value="true";
			do {
				GetUserInput getinput = new GetUserInput();
				logger.log(Level.INFO,
						"enter your choice \n1.ADD NEW COMPANY \n 2.ADD A NEW VACANCY \n 3.DELETE AN EXISTING VACANCY \n 4.WRITE A REVIEW AND RATE A COMPANY \n 5.DELETE YOUR ACCOUNT \n 6.BACK");
				int ch = sc.nextInt();
				switch (ch) {
				case 1:
					getinput.addNewCompany();
					break;
				case 2:
					getinput.publishVacancy();
					break;
				case 3:
					getinput.removeVacancy();
					break;
				case 4:
					getinput.reviewAndRateCompany();
					break;
				case 5:
					getinput.deleteUserAccount();
					break;
				case 6:
					value = "false";
					Login.main(null);
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

	public void siteAdminFlow() {
		try {
			String value="true";
			do {
				GetUserInput getinput = new GetUserInput();
				logger.log(Level.INFO,
						"enter your choice \n1.ADD NEW COMPANY \n 2.ADD A NEW VACANCY \n 3.DELETE AN EXISTING VACANCY \n 4.DELETE A COMPANY \n 5.BACK");
				int ch = sc.nextInt();
				switch (ch) {
				case 1:
					getinput.addNewCompany();
					break;
				case 2:
					getinput.publishVacancy();
					break;
				case 3:
					getinput.removeVacancy();
					break;
				case 4:
					getinput.deleteCompany();
					break;
				case 5:
					value = "false";
					Login.main(null);
					break;
				default:
					logger.log(Level.INFO, "select a valid choice");
					break;
				}
			}  while (value.equals("true"));
			logger.log(Level.INFO, "BYE");
		} catch (Exception e) {

		} finally {
				sc.close();
		}
	}

}
