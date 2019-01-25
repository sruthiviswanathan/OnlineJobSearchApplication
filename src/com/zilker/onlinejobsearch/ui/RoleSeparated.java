package com.zilker.onlinejobsearch.ui;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zilker.onlinejobsearch.beans.User;
/*
 * separated flow based on login credentials.(user|company admin|website admin)
 */
public class RoleSeparated {
	private static final Logger logger = Logger.getLogger(RoleSeparated.class.getName());
	public Scanner scanner = new Scanner(System.in);
	public void userFlow(User user) {
		try {
			String value="true";
			do {
				GetUserInput getuserinput = new GetUserInput();
				logger.log(Level.INFO,
						"\n enter your choice \n 1.SEARCH FOR A COMPANY \n 2.SEARCH FOR A JOB  \n 3.WRITE ABOUT THE INTERVIEW PROCESS OF JOB \n 4.WRITE A REVIEW AND GIVE RATING FOR A COMPANY  \n 5.REQUEST FOR A JOB \n 6.DELETE YOUR ACCOUNT \n 7.UPDATE YOUR ACCOUNT \n 8.BACK");
				int ch = scanner.nextInt();
				switch (ch) {
				case 1:
					getuserinput.searchCompany(user);
					break;
				case 2:
					getuserinput.searchJobs(user);
					break;
				case 3:
					getuserinput.writeInterviewProcess(user);
					break;
				case 4:
					getuserinput.reviewAndRateCompany(user);
					break;
				case 5:
					getuserinput.requestVacancy(user);
					break;
				case 6:
					getuserinput.deleteUserAccount();
					break;
				case 7:
					getuserinput.updateUserAccount(user);
					break;
				case 8:
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
				scanner.close();
		}
	}

	public void adminFlow(User user) {
		
		try {
			String value="true";
			do {
				GetUserInput getuserinput = new GetUserInput();
				logger.log(Level.INFO,
						"enter your choice \n 1.ADD A NEW VACANCY \n 2.DELETE AN EXISTING VACANCY \n3.UPDATE EXISTING VACANCY  \n 4.BACK");
				int ch = scanner.nextInt();
				switch (ch) {
				case 1:
					getuserinput.publishVacancyAdmin(user);
					break;
				case 2:
					getuserinput.removeVacancyAdmin(user);
					break;
				case 3:
					getuserinput.UpdateVacancy();
					break;
				case 4:
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
				scanner.close();
		}
	}

	public void siteAdminFlow() {
		try {
			String value="true";
			do {
				GetUserInput getuserinput = new GetUserInput();
				logger.log(Level.INFO,
						"enter your choice \n1.ADD NEW COMPANY \n 2.ADD A NEW VACANCY \n 3.DELETE AN EXISTING VACANCY \n 4.DELETE A COMPANY \n 5.BACK");
				int ch = scanner.nextInt();
				switch (ch) {
				case 1:
					getuserinput.addNewCompany();
					break;
				case 2:
					getuserinput.publishVacancy();
					break;
				case 3:
					getuserinput.removeVacancy();
					break;
				case 4:
					getuserinput.deleteCompany();
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
				scanner.close();
		}
	}

}
