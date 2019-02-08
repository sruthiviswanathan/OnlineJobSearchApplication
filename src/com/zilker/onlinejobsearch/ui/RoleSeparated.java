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
			String value = "true";
			do {
				GetUserInput getUserInput = new GetUserInput();
				logger.log(Level.INFO,
						"\n enter your choice \n 1.SEARCH FOR A COMPANY \n 2.SEARCH FOR A JOB \n 3.SEARCH BY LOCATION \n 4.WRITE ABOUT THE INTERVIEW PROCESS OF JOB \n 5.WRITE A REVIEW AND GIVE RATING FOR A COMPANY  \n 6.REQUEST FOR A JOB \n 7.DELETE YOUR ACCOUNT \n 8.UPDATE YOUR ACCOUNT \n 9.GENERAL REVIEWS OF A COMPANY\n 10.BACK");
				int ch = scanner.nextInt();
				switch (ch) {
				case 1:
					getUserInput.searchCompany(user);
					break;
				case 2:
					getUserInput.searchJobs(user);
					break;
				case 3:
					getUserInput.searchByLocation(user);
					break;
				case 4:
					getUserInput.writeInterviewProcess(user);
					break;
				case 5:
					getUserInput.reviewAndRateCompany(user);
					break;
				case 6:
					getUserInput.requestVacancy(user);
					break;
				case 7:
					getUserInput.deleteUserAccount(user);
					break;
				case 8:
					getUserInput.updateUserAccount(user);
					break;
				case 10:
					value = "false";
					Login.main(null);
					break;
				case 9:
					getUserInput.generalCompanyReviews();
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
			String value = "true";
			do {
				GetUserInput getUserInput = new GetUserInput();
				logger.log(Level.INFO,
						"enter your choice \n 1.ADD A NEW VACANCY \n 2.DELETE AN EXISTING VACANCY \n 3.UPDATE EXISTING VACANCY \n 4.BACK");
				int ch = scanner.nextInt();
				switch (ch) {
				case 1:
					getUserInput.publishVacancyAdmin(user);
					break;
				case 2:
					getUserInput.removeVacancyAdmin(user);
					break;
				case 3:
					getUserInput.UpdateVacancy(user);
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

	public void siteAdminFlow(User user) {
		try {
			String value = "true";
			do {
				GetUserInput getUserInput = new GetUserInput();
				logger.log(Level.INFO,
						"enter your choice \n1.ADD NEW COMPANY \n 2.ADD A NEW VACANCY \n 3.DELETE AN EXISTING VACANCY \n 4.DELETE A COMPANY \n 5.BACK");
				int ch = scanner.nextInt();
				switch (ch) {
				case 1:
					getUserInput.addNewCompanyBySiteAdmin(user);
					break;
				case 2:
					getUserInput.publishVacancySiteAdmin(user);
					break;
				case 3:
					getUserInput.removeVacancy(user);
					break;
				case 4:
					getUserInput.deleteCompany(user);
					break;
				case 5:
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

}
