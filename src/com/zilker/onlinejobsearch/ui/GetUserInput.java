package com.zilker.onlinejobsearch.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.zilker.onlinejobsearch.beans.Company;
import com.zilker.onlinejobsearch.beans.JobMapping;
import com.zilker.onlinejobsearch.beans.JobRequest;
import com.zilker.onlinejobsearch.beans.Technology;
import com.zilker.onlinejobsearch.beans.User;
import com.zilker.onlinejobsearch.beans.UserTechnologyMapping;
import com.zilker.onlinejobsearch.delegate.CompanyDelegate;
import com.zilker.onlinejobsearch.delegate.JobDelegate;
import com.zilker.onlinejobsearch.delegate.UserDelegate;
import com.zilker.onlinejobsearch.utils.Validation;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class GetUserInput {

	private static final Logger logger = Logger.getLogger(GetUserInput.class.getName());
	public Scanner scanner = new Scanner(System.in);
	Validation valid = new Validation();

	/*
	 * method for registering as a user.
	 */
	public void register() {

		try {
			User user = new User();
			UserDelegate userDelegate = new UserDelegate();
			Technology technology = new Technology();
			ArrayList<Technology> tech = new ArrayList<Technology>();
			boolean isvalid = false;
			boolean check = false;
			boolean exist = false;
			int flag = 0, userId = 0;
			String password = "", userName = "", company = "", designation = "", confirmPassword = "";

			do {
				logger.log(Level.INFO, "Enter UserName");
				userName = scanner.nextLine();
				isvalid = valid.StringValidation(userName);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid userName");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter emailId (eg: username@domain.com)");
				String email = scanner.nextLine();
				user.setEmail(email);
				exist = userDelegate.ifAlreadyExists(user);
				if (exist == true) {
					logger.log(Level.INFO, "This email id has already been registered!!");
				} else {
					exist = false;
				}
				isvalid = valid.emailValidation(email);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid email ID");
				}
			} while (isvalid == false || exist == true);

			isvalid = false;

			do {
				do {
					logger.log(Level.INFO, "enter password(8-15 characters,at least one letter and one number:)");
					password = scanner.nextLine();
					isvalid = valid.passwordValidation(password);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid password");
					}
				} while (isvalid == false);
				logger.log(Level.INFO, "Please confirm your password");
				confirmPassword = scanner.nextLine();
				if (password.equals(confirmPassword)) {
					check = true;
				}
				if (check == false) {
					logger.log(Level.INFO, "Fields of password and confirm password must match!!!");
				}

			} while (check != true);

			isvalid = false;

			do {
				logger.log(Level.INFO, "Enter Company/College");
				company = scanner.nextLine();
				isvalid = valid.StringValidation(company);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid input");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "Enter Designation/CurrentStatus");
				designation = scanner.nextLine();
				isvalid = valid.StringValidation(designation);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid input");
				}
			} while (isvalid == false);

			// user.setEmail(email);
			user.setUserName(userName);
			user.setPassword(password);
			user.setCompany(company);
			user.setDesignation(designation);
			flag = userDelegate.register(user);

			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			userDelegate.insertIntoUser(user);

			logger.log(Level.INFO, "Do you want to add Technologies known to your profile?(If yes press y else n)");
			char choice = scanner.next().charAt(0);

			if (choice == 'Y' || choice == 'y') {
				tech = userDelegate.displayTechnologies(technology);
				for (Technology i : tech) {
					logger.log(Level.INFO, "\n" + i.getTechnologyId() + "\t" + i.getTechnology());
				}
				user.setUserId(userId);
				addTechnologyDetails(user);
			}
			if (flag == 1) {
				logger.log(Level.INFO, "Congrats you are Registered !");
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN REGISTERING!" + e.getMessage());
		}
	}

	/*
	 * method for adding technology details to user profile.
	 */
	public void addTechnologyDetails(User user) {
		try {

			boolean isvalid = false;
			boolean loop = true;
			int flag1 = 0, numbers=0,number = 0;
			int technologyId = 0;
			UserDelegate userDelegate = new UserDelegate();
			UserTechnologyMapping usertechnology = new UserTechnologyMapping();
			Technology technology = new Technology();
			do {
				logger.log(Level.INFO, "ENTER A CHOICE \n 1.ADD A TECHNOLOGY LISTED ABOVE"
						+ "\n 2.ADD A NEW TECHNOLOGY NOT LISTED \n 3.EXIT");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1:
					scanner.nextLine();
					do {
						logger.log(Level.INFO, "Enter how many technologies you want to add to your profile?");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							numbers = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					for (int i = 0; i < numbers; i++) {
						boolean exist = false;

						do {
							do {
								logger.log(Level.INFO, "Enter technology ID");
								String check = scanner.nextLine();
								isvalid = valid.intValidation(check);
								if (isvalid == false) {
									logger.log(Level.INFO, "Please enter a valid input");
								} else {
									technologyId = Integer.parseInt(check);
								}
							} while (isvalid == false);

							isvalid = false;

							technology.setTechnologyId(technologyId);
							exist = userDelegate.ifTechnologyIdExists(technology);
							if (exist == false) {
								logger.log(Level.INFO, "Enter a valid technology ID");
							}
						} while (exist == false);

						usertechnology.setUserId(user.getUserId());
						usertechnology.setTechnologyId(technologyId);
						flag1 = userDelegate.addTechnologyDetails(usertechnology);
						if (flag1 == 1) {
							logger.log(Level.INFO, "Technology added to your profile !");
						}
					}
					break;
				case 2:
					scanner.nextLine();
					do {
						logger.log(Level.INFO, "Enter how many technologies you want to add to your profile?");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							number = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;
					
					for (int i = 0; i < number; i++) {
						logger.log(Level.INFO, "Press enter to continue");
						scanner.nextLine();
						technologyId = addNewTechnology(user);
						usertechnology.setUserId(user.getUserId());
						usertechnology.setTechnologyId(technologyId);
						flag1 = userDelegate.addTechnologyDetails(usertechnology);
						if (flag1 == 1) {
							logger.log(Level.INFO, "Technology added to your profile !");
						}
					}
					break;
				case 3:
					loop = false;
					break;
				default:
					logger.log(Level.INFO, "PLEASE ENTER A VALID CHOICE");
					break;
				}

			} while (loop == true);

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING TECHNOLOGY TO PROFILE!" + e.getMessage());
		}

	}

	/*
	 * method for adding new technology that is not existing in database.
	 */

	public int addNewTechnology(User user) {

		try {
			int technologyId = 0;
			UserDelegate userDelegate = new UserDelegate();
			Technology technology = new Technology();

			// validation////to be done
			
			
			logger.log(Level.INFO, "Enter the Technology you want to add!!!");
			String technologyName = scanner.nextLine();

			
			technology.setTechnology(technologyName);
			technologyId = userDelegate.addNewTechnology(technology, user);
			if (technologyId != 0) {
				logger.log(Level.INFO, "New Technology added !");
			}
			return technologyId;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING NEW TECHNOLOGY!" + e.getMessage());
			return 0;
		}

	}

	/*
	 * method for registering as a company admin
	 */

	public void registerAsAdmin() {

		try {
			char c = '\0';
			boolean isvalid = false;
			boolean check = false;
			boolean exist = false;
			int flag = 0, flag1 = 0, companyId = 0, flag2 = 0;
			String companyName = "", userName = "", email = "";
			String password = "";
			User user = new User();
			UserDelegate userDelegate = new UserDelegate();
			Company company = new Company();
			CompanyDelegate companyDelegate = new CompanyDelegate();

			do {
				logger.log(Level.INFO, "Enter UserName");
				userName = scanner.nextLine();
				isvalid = valid.StringValidation(userName);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid userName(Name should contain only characters)");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "Enter Company name");
				companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = companyDelegate.fetchCompanyId(company);
				if (companyId == 0) {
					do {
						logger.log(Level.INFO,
								"\n This company is not registered with us!! ENTER A CHOICE \n 1.RETRY \n 2.ADD YOUR COMPANY");
						int ch = scanner.nextInt();
						switch (ch) {
						case 1:
							scanner.nextLine();
							c = 'n';
							break;
						case 2:
							scanner.nextLine();
							addNewCompany();
							c = 'n';
							break;
						default:
							logger.log(Level.INFO, "ENTER A VALID CHOICE");
							break;
						}

					} while (c != 'n');

				} else {
					flag1 = 1;
					company.setCompanyId(companyId);
					logger.log(Level.INFO, "Company details fetched !");
				}
			} while (flag1 != 1);

			do {
				logger.log(Level.INFO, "enter emailId (eg: username@domain.com)");
				email = scanner.nextLine();
				// user.setEmail(email);
				exist = userDelegate.ifAlreadyExists(user);
				if (exist == true) {
					logger.log(Level.INFO, "This email id has already been registered!!");
				} else {
					exist = false;
				}
				isvalid = valid.emailValidation(email);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid email ID");
				}
			} while (isvalid == false || exist == true);

			isvalid = false;
			do {
				do {
					logger.log(Level.INFO, "enter password(8-15 characters,at least one letter and one number:)");
					password = scanner.nextLine();
					isvalid = valid.passwordValidation(password);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid password");
					}
				} while (isvalid == false);
				logger.log(Level.INFO, "Please confirm your password");
				String confirmPassword = scanner.nextLine();
				if (password.equals(confirmPassword)) {
					check = true;
				}
				if (check == false) {
					logger.log(Level.INFO, "Fields of password and confirm password must match!!!");
				}

			} while (check != true);

			isvalid = false;

			user.setEmail(email);
			user.setUserName(userName);
			user.setPassword(password);
			user.setCompany(companyName);
			user.setDesignation("admin");
			user.setRoleId(2);
			flag = userDelegate.registerAsAdmin(user);
			if (flag == 1) {
				logger.log(Level.INFO, "CONGRATS YOU ARE REGISTERED WITH US!!!");
			}

			int userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			userDelegate.insertIntoUser(user);
			if (userId != 0) {
				user.setUserId(userId);
				flag2 = userDelegate.insertIntoAdmin(user, company);
				CompanyDelegate.insertIntoCompanyDetails(user, company);
				if (flag2 == 1) {
					logger.log(Level.INFO, "YOU ARE NOW AN ADMIN!");
				}
			}

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN REGISTERING!" + e.getMessage());
		}

	}

	/*
	 * method for logging in.
	 */
	public void login() {
		try {
			int role = 0;
			User user = new User();
			UserDelegate userDelegate = new UserDelegate();
			RoleSeparated roleseparated = new RoleSeparated();
			logger.log(Level.INFO, "Enter Email");
			String email = scanner.next();
			logger.log(Level.INFO, "Enter Password");
			String password = scanner.next();
			user.setEmail(email);
			user.setPassword(password);
			role = userDelegate.login(user);
			if (role == 0) {
				logger.log(Level.INFO, "***Invalid Login Credentials!***");
			} else if (role == 1) {
				logger.log(Level.INFO, "Congrats you are logged in!!!");
				roleseparated.userFlow(user);
			} else if (role == 2) {
				logger.log(Level.INFO, "Congrats you are logged in!!!");
				roleseparated.adminFlow(user);
			} else if (role == 3) {
				logger.log(Level.INFO, "Congrats you are logged in!!!");
				roleseparated.siteAdminFlow(user);
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN LOGGING IN!" + e.getMessage());
		}
	}

	/*
	 * method for deleting user account.
	 */
	public void deleteUserAccount(User user) {
		try {
			int flag = 0, flag1 = 0;
			int userId = 0;
			char c = '\0';
			UserDelegate userDelegate = new UserDelegate();

			logger.log(Level.INFO, " Reenter Password");
			String password = scanner.next();
			user.setEmail(user.getEmail());
			user.setPassword(password);
			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);

			if (userId == 0) {
				logger.log(Level.INFO, "Invalid account credentials!!Do you still want to delete?(y/n)");
				c = scanner.next().charAt(0);
				if (c == 'y' || c == 'Y') {
					deleteUserAccount(user);
				}
			} else {
				flag = userDelegate.checkPasswordBeforeDelete(user);
				if (flag == 1) {
					flag1 = userDelegate.deleteUserAccount(user);

					if (flag1 == 1) {
						logger.log(Level.INFO, "YOUR ACCOUNT HAS BEEN DELETED!!!");
					}
				} else {
					logger.log(Level.INFO, "Your password didn't match!!Do you still want to try again?(y/n)");
					c = scanner.next().charAt(0);
					if (c == 'y' || c == 'Y') {
						deleteUserAccount(user);
					}
				}
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN DELETING ACCOUNT!" + e.getMessage());
		}
	}

	/*
	 * method for searching based on company.
	 */
	public void searchCompany(User user) {
		try {
			ArrayList<Company> displayCompanies = new ArrayList<Company>();
			ArrayList<Company> companyDetails = new ArrayList<Company>();
			ArrayList<Company> vacancyDetails = new ArrayList<Company>();
			ArrayList<Company> companyReviews = new ArrayList<Company>();
			ArrayList<Company> interviewProcess = new ArrayList<Company>();
			Company company = new Company();

			CompanyDelegate companyDelegate = new CompanyDelegate();
			UserDelegate userDelegate = new UserDelegate();
			int companyId = 0, jobId = 0;
			int flag = 0, flag1 = 0;
			boolean isvalid = false;
			String companyName = "";
			do {

				logger.log(Level.INFO, " \n Companies Registered With Us :");
				displayCompanies = companyDelegate.displayCompanies(company);
				for (Company i : displayCompanies) {
					logger.log(Level.INFO, "\n" + i.getCompanyName());
				}

				do {

					do {
						logger.log(Level.INFO, "Enter a Company Name to search");
						companyName = scanner.nextLine();
						isvalid = valid.StringValidation(companyName);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid Company Name");
						}
					} while (isvalid == false);

					company.setCompanyName(companyName);
					companyId = companyDelegate.fetchCompanyId(company);
					if (companyId == 0) {
						logger.log(Level.INFO, "***Company is not registered with us as of now!!!***");
					} else {
						flag = 1;
					}
				} while (flag != 1);
				company.setCompanyId(companyId);
				companyDetails = companyDelegate.retrieveVacancyByCompany(company);
				if (companyDetails.isEmpty()) {
					logger.log(Level.INFO, "***No Vacancy in this Company!!!***");

				}

				for (Company j : companyDetails) {
					if (j.getAverageRating() == 0) {
						logger.log(Level.INFO, "\nCOMPANY NAME:" + j.getCompanyName() + "\nWEBSITE URL:"
								+ j.getCompanyWebsiteUrl() + "\nAVERAGE RATING: ***NO RATING FOR THIS COMPANY***");
					} else {
						logger.log(Level.INFO, "\nCOMPANY NAME:" + j.getCompanyName() + "\nWEBSITE URL:"
								+ j.getCompanyWebsiteUrl() + "\nAVERAGE RATING:" + j.getAverageRating());
					}
					break;
				}
				vacancyDetails = companyDelegate.retrieveVacancyByCompany1(company);
				if (vacancyDetails.isEmpty()) {
					logger.log(Level.INFO, "***No Vacancy in this Company!!!***");

				} else {
					for (Company i : vacancyDetails) {
						jobId = i.getJobId();
						logger.log(Level.INFO,
								"\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:" + i.getJobDescription()
										+ "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary()
										+ "\nNUMBER OF VACANCIES:" + i.getVacancyCount());

						company.setJobId(jobId);
						logger.log(Level.INFO, "REVIEWS ON INTERVIEW PROCESS");
						interviewProcess = userDelegate.retrieveInterviewProcess(company);
						if (interviewProcess.isEmpty()) {
							logger.log(Level.INFO, "***No reviews on Interview process!!!***");
						}
						for (Company j : interviewProcess) {
							logger.log(Level.INFO,
									"\nUSERNAME:" + j.getUserName() + "\tINTERVIEW PROCESS:" + j.getInterviewProcess());
						}

					}

					companyReviews = userDelegate.retrieveReview(company);
					logger.log(Level.INFO, "GENERAL COMPANY REVIEWS");
					if (companyReviews.isEmpty()) {
						logger.log(Level.INFO, "***No Reviews for this Company!!!***");
					}
					for (Company i : companyReviews) {
						logger.log(Level.INFO, "\nUSERNAME:" + i.getUserName() + "\tREVIEW: " + i.getReview());
					}

				}
				logger.log(Level.INFO, "Do you want to search for any other company?(Y/N)!!");
				char choice = scanner.next().charAt(0);
				scanner.nextLine();
				if (choice == 'y' || choice == 'Y') {
					flag1 = 0;
				} else {
					flag1 = 1;
				}

			} while (flag1 == 0);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN RETREIVING COMPANY!" + e.getMessage());
		}
	}

	/*
	 * method for searching based on location.
	 */
	public void searchByLocation(User user) {
		try {

			ArrayList<Company> retrieveByLocation = new ArrayList<Company>();
			ArrayList<Company> companyDetails = new ArrayList<Company>();
			ArrayList<Company> vacancyDetails = new ArrayList<Company>();
			ArrayList<Company> interviewProcess = new ArrayList<Company>();
			Company company = new Company();
			UserDelegate userDelegate = new UserDelegate();
			JobDelegate jobDelegate = new JobDelegate();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			int flag = 0;
			String location = "";
			boolean isvalid = false;
			do {

				do {
					logger.log(Level.INFO, "Enter your preferred Location to search");
					location = scanner.nextLine();
					isvalid = valid.StringValidation(location);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid location");
					}
				} while (isvalid == false);

				company.setLocation(location);
				retrieveByLocation = companyDelegate.retrieveVacancyByLocation(company);
				if (retrieveByLocation.isEmpty()) {
					logger.log(Level.INFO, "No vacancy in this Location as of now!!");
					logger.log(Level.INFO, "Do you want to search for any other location?(Y/N)!!");
					char choice = scanner.next().charAt(0);
					scanner.nextLine();
					if (choice == 'y' || choice == 'Y') {
						flag = 0;
					}
				} else {
					for (Company i : retrieveByLocation) {
						int companyId = i.getCompanyId();
						int jobId = i.getJobId();
						company.setCompanyId(companyId);
						company.setJobId(jobId);
						companyDetails = companyDelegate.retrieveVacancyByCompany(company);
						if (companyDetails.isEmpty()) {
							logger.log(Level.INFO, "***No Vacancy in this Company!!!***");
						}
						for (Company j : companyDetails) {
							if (j.getAverageRating() == 0) {
								logger.log(Level.INFO,
										"\nCOMPANY NAME:" + j.getCompanyName() + "\nWEBSITE URL:"
												+ j.getCompanyWebsiteUrl()
												+ "\nAVERAGE RATING: ***NO RATING FOR THIS COMPANY***");
							} else {
								logger.log(Level.INFO, "\nCOMPANY NAME:" + j.getCompanyName() + "\nWEBSITE URL:"
										+ j.getCompanyWebsiteUrl() + "\nAVERAGE RATING:" + j.getAverageRating());
							}
							break;
						}
						vacancyDetails = jobDelegate.retrieveVacancyByJob(company);
						if (vacancyDetails.isEmpty()) {
							logger.log(Level.SEVERE, "***No vacancy in this designation!!!***");
						}
						for (Company k : vacancyDetails) {
							logger.log(Level.INFO, "\nJOB DESIGNATION:" + k.getJobRole());
						}

						logger.log(Level.INFO,
								"\nJOB DESCRIPTION:" + i.getJobDescription() + "\nLOCATION:" + i.getLocation()
										+ "\nSALARY(lpa):" + i.getSalary() + "\nNUMBER OF VACANCIES:"
										+ i.getVacancyCount());

						logger.log(Level.INFO, "REVIEWS ON INTERVIEW PROCESS");
						interviewProcess = userDelegate.retrieveInterviewProcess(company);
						if (interviewProcess.isEmpty()) {
							logger.log(Level.INFO, "***No Reviews on Interview process!!!***");
						}
						for (Company j : interviewProcess) {
							logger.log(Level.INFO, " \nUSERNAME:" + j.getUserName() + "\tINTERVIEW PROCESS:"
									+ j.getInterviewProcess());
						}

					}
					logger.log(Level.INFO, "Do you want to search for any other location?(Y/N)!!");
					char choice = scanner.next().charAt(0);
					scanner.nextLine();
					if (choice == 'y' || choice == 'Y') {
						flag = 0;
					} else {
						flag = 1;
					}
				}

			} while (flag != 1);

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN RETREIVING COMPANY!" + e.getMessage());
		}

	}

	/*
	 * method for searching based on jobs.
	 */
	public void searchJobs(User user) {
		try {
			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			ArrayList<Company> jobRole = new ArrayList<Company>();
			ArrayList<Company> vacancyDetails = new ArrayList<Company>();
			ArrayList<Company> interviewProcess = new ArrayList<Company>();
			JobDelegate jobDelegate = new JobDelegate();
			UserDelegate userDelegate = new UserDelegate();
			Company company = new Company();
			JobMapping jobmapping = new JobMapping();

			int jobId = 0;
			int flag = 0, flag1 = 0;
			String jobDesignation = "";
			char c;
			boolean isvalid = false;
			do {
				job = jobDelegate.displayJobs(jobmapping);
				for (JobMapping i : job) {
					logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
				}
				do {

					do {
						logger.log(Level.INFO, "Enter a Job Designation to search");
						jobDesignation = scanner.nextLine();
						isvalid = valid.StringValidation(jobDesignation);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid job designation");
						}
					} while (isvalid == false);

					jobmapping.setJobRole(jobDesignation);
					jobId = jobDelegate.fetchJobId(jobmapping);
					if (jobId == 0) {
						logger.log(Level.INFO,
								"No vacancies in this designation as of now!!Do you still want to search for other job designation?(y/n)");
						c = scanner.next().charAt(0);
						scanner.nextLine();
						flag = 0;
					} else {
						flag = 1;
						c = 'n';
					}
				} while (c == 'y' || c == 'Y');
				if (flag == 1) {
					company.setJobId(jobId);
					jobRole = jobDelegate.retrieveVacancyByJob(company);
					if (jobRole.isEmpty()) {
						logger.log(Level.INFO, "***No vacancy in this designation!!!***");
					}
					for (Company i : jobRole) {
						logger.log(Level.INFO, "\nJOB DESIGNATION:" + i.getJobRole());
					}
					vacancyDetails = jobDelegate.retrieveVacancyByJob1(company);
					if (vacancyDetails.isEmpty()) {
						logger.log(Level.INFO, "***No vacancy in this designation!!!***");
					}
					for (Company i : vacancyDetails) {

						if (i.getAverageRating() == 0) {
							logger.log(Level.INFO, "\nCOMPANY NAME:" + i.getCompanyName() + "\nWEBSITE URL:"
									+ i.getCompanyWebsiteUrl() + "\nAVERAGE RATING: ***NO RATING FOR THIS COMPANY***");
						}

						else {
							logger.log(Level.INFO, "\nCOMPANY NAME:" + i.getCompanyName() + "\nWEBSITE URL:"
									+ i.getCompanyWebsiteUrl() + "\nAVERAGE RATING:" + i.getAverageRating());
						}
						logger.log(Level.INFO,
								"\nJOB DESCRIPTION:" + i.getJobDescription() + "\nLOCATION:" + i.getLocation()
										+ "\nSALARY:" + i.getSalary() + "\n NUMBER OF VACANCIES:"
										+ i.getVacancyCount());

						company.setCompanyId(i.getCompanyId());
						logger.log(Level.INFO, "REVIEWS ON INTERVIEW PROCESS");
						interviewProcess = userDelegate.retrieveInterviewProcess(company);
						if (interviewProcess.isEmpty()) {
							logger.log(Level.INFO, "***No reviews on Interview process!!!***");
						}
						for (Company j : interviewProcess) {
							logger.log(Level.INFO,
									"\nUSERNAME:" + j.getUserName() + "\tINTERVIEW PROCESS:" + j.getInterviewProcess());
						}
					}
					logger.log(Level.INFO, "Do you want to search for any other job designation?(Y/N)!!");
					char choice = scanner.next().charAt(0);
					scanner.nextLine();
					if (choice == 'y' || choice == 'Y') {
						flag1 = 0;
					} else {
						flag1 = 1;
					}

				}

			} while (flag1 == 0);

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN RETRIEVING JOBS!" + e.getMessage());
		}
	}

	/*
	 * method for adding new company details.
	 */
	public void addNewCompany() {
		int flag = 0;
		String companyName = "", companyWebsiteUrl = "";
		try {

			CompanyDelegate companyDelegate = new CompanyDelegate();
			Company company = new Company();

			boolean isvalid = false;

			do {
				logger.log(Level.INFO, "enter Company name");
				companyName = scanner.nextLine();
				isvalid = valid.StringValidation(companyName);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid Company Name");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter Company website url");
				companyWebsiteUrl = scanner.nextLine();
				isvalid = valid.urlValidation(companyWebsiteUrl);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid Website URL");
				}
			} while (isvalid == false);

			company.setCompanyName(companyName);
			company.setCompanyWebsiteUrl(companyWebsiteUrl);
			flag = companyDelegate.addNewCompany(company);
			if (flag == 1) {
				logger.log(Level.INFO, "ADDED YOUR COMPANY DETAILS!!!");
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING NEW COMPANY!" + e.getMessage());
		}
	}

	/*
	 * method for adding new company by site admin.
	 */
	public void addNewCompanyBySiteAdmin(User user) {
		int flag = 0;
		try {
			UserDelegate userDelegate = new UserDelegate();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			Company company = new Company();
			int userId = 0;
			String companyName = "", companyWebsiteUrl = "";
			boolean isvalid = false;

			do {
				logger.log(Level.INFO, "enter Company name");
				companyName = scanner.nextLine();
				isvalid = valid.StringValidation(companyName);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid CompanyName");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter Company website url");
				companyWebsiteUrl = scanner.nextLine();
				isvalid = valid.urlValidation(companyWebsiteUrl);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid Website URL");
				}
			} while (isvalid == false);

			company.setCompanyName(companyName);
			company.setCompanyWebsiteUrl(companyWebsiteUrl);
			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			flag = companyDelegate.addNewCompanyBySiteAdmin(company, user);
			if (flag == 1) {
				logger.log(Level.INFO, "ADDED YOUR COMPANY DETAILS!!!");
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING NEW COMPANY!" + e.getMessage());
		}
	}

	/*
	 * method for adding company.
	 */
	public int addingCompanyDetails(User user) {
		try {
			int companyId = 0;
			int flag = 0;
			Company company = new Company();
			String companyName = "";
			boolean isvalid = false;
			CompanyDelegate companyDelegate = new CompanyDelegate();
			ArrayList<Company> comp = new ArrayList<Company>();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = companyDelegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			do {
				do {
					logger.log(Level.INFO, "enter Company name");
					companyName = scanner.nextLine();
					isvalid = valid.StringValidation(companyName);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid CompanyName");
					}
				} while (isvalid == false);

				company.setCompanyName(companyName);
				companyId = companyDelegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO, "***Please add the company first!!***");
					addNewCompany();
				} else {
					flag = 1;
					logger.log(Level.INFO, "Company details fetched !");
				}
			} while (flag != 1);
			return companyId;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING COMPANY DETAILS!" + e.getMessage());
			return 0;
		}
	}

	/*
	 * method for adding job details.
	 */
	public int addingJobDetails(User user) {
		try {

			boolean isvalid=false;
			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			int jobId = 0;
			JobMapping jobmapping = new JobMapping();
			boolean exist=false;
			JobDelegate jobDelegate = new JobDelegate();
			job = jobDelegate.displayJobs(jobmapping);
			for (JobMapping i : job) {
				logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
			}
			logger.log(Level.INFO, "enter Job ID");
			logger.log(Level.INFO, "Do you want to add a new Job Description not listed above(y/n)?");
			char ch = scanner.next().charAt(0);
			if (ch == 'y' || ch == 'Y') {
				jobId = addNewJob(user);
			} else {
					scanner.nextLine();
				do {
					do {
						logger.log(Level.INFO, "Enter Job ID");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							jobId = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					jobmapping.setJobId(jobId);
					exist = jobDelegate.ifJobIdExists(jobmapping);
					if (exist == false) {
						logger.log(Level.INFO, "Enter a valid Job ID");
					}
				} while (exist == false);
				
				
				/*
				do {
					logger.log(Level.INFO, "Enter any one of the listed Job IDs");
					String check = scanner.nextLine();
					isvalid = valid.intValidation(check);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid input");
					} else {
						jobId = Integer.parseInt(check);
					}
				} while (isvalid == false);

				isvalid = false;
					*/	
			}
			return jobId;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING JOB DETAILS!" + e.getMessage());
			return 0;
		}
	}

	/*
	 * method for publishing new vacancy by website admin.
	 */
	public void publishVacancySiteAdmin(User user) {
		try {
			int flag = 0;
			Company company = new Company();
			UserDelegate userDelegate = new UserDelegate();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			int companyId = 0;
			int jobId = 0;
			boolean isvalid = false;
			String location = "", jobDescription = "";
			float salary = 0;
			int vacancyCount = 0;
			companyId = addingCompanyDetails(user);
			jobId = addingJobDetails(user);

			logger.log(Level.INFO, "Press enter to continue");
			scanner.nextLine();
			do {
				logger.log(Level.INFO, "enter location of job");
				location = scanner.nextLine();
				isvalid = valid.StringValidation(location);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid location");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter job Description");
				jobDescription = scanner.nextLine();
				isvalid = valid.StringValidation(jobDescription);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid job Description");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter salary (lpa)");
				String check = scanner.nextLine();
				isvalid = valid.floatValidation(check);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid salary");
				} else {
					salary = Float.parseFloat(check);
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter Vacancy count");
				String check = scanner.nextLine();
				isvalid = valid.intValidation(check);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid vacancy count");
				} else {
					vacancyCount = Integer.parseInt(check);
				}
			} while (isvalid == false);

			isvalid = false;
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			company.setLocation(location);
			company.setJobDescription(jobDescription);
			company.setSalary(salary);
			company.setVacancyCount(vacancyCount);
			int userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			flag = companyDelegate.publishVacancy(company, user);
			if (flag == 1) {
				logger.log(Level.INFO, "Your company vacancy is published !");
			}
			companyDelegate.compareVacancyWithRequest(company);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN PUBLISHING VACANCY!" + e.getMessage());
		}
	}

	/*
	 * method for publishing new vacancy by a company admin.
	 */
	public void publishVacancyAdmin(User user) {
		try {
			int flag = 0;
			// User user= new User();
			Company company = new Company();
			UserDelegate userDelegate = new UserDelegate();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			int companyId = 0, jobId = 0, userId = 0;
			boolean isvalid = false;
			String location = "", jobDescription = "";
			float salary = 0;
			int vacancyCount = 0;

			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			companyId = userDelegate.fetchCompanyIdByAdmin(user);
			jobId = addingJobDetails(user);
			logger.log(Level.INFO, "Press enter to continue");
			scanner.nextLine();

			do {
				logger.log(Level.INFO, "enter location of job");
				location = scanner.nextLine();
				isvalid = valid.StringValidation(location);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid location");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter job Description");
				jobDescription = scanner.nextLine();
				isvalid = valid.StringValidation(jobDescription);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid job description");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter salary (lpa)");
				String check = scanner.nextLine();
				isvalid = valid.floatValidation(check);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid salary amount");
				} else {
					salary = Float.parseFloat(check);
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter Vacancy count");
				String check = scanner.nextLine();
				isvalid = valid.intValidation(check);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid vacancy count");
				} else {
					vacancyCount = Integer.parseInt(check);
				}
			} while (isvalid == false);

			isvalid = false;
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			company.setLocation(location);
			company.setJobDescription(jobDescription);
			company.setSalary(salary);
			company.setVacancyCount(vacancyCount);
			flag = companyDelegate.publishVacancy(company, user);
			if (flag == 1) {
				logger.log(Level.INFO, "Your company vacancy is published !");
			}
			companyDelegate.compareVacancyWithRequest(company);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN PUBLISHING VACANCY!" + e.getMessage());
		}
	}

	/*
	 * method for deleting existing vacancy.
	 */
	public void removeVacancy(User user) {
		try {
			int flag = 0, flag1 = 0;
			int companyId = 0;
			int jobId = 0;
			boolean isvalid = false;
			String companyName = "";
			Company company = new Company();
			UserDelegate userDelegate = new UserDelegate();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			JobDelegate jobDelegate = new JobDelegate();
			JobMapping jobmapping = new JobMapping();
			ArrayList<Company> comp = new ArrayList<Company>();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = companyDelegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}

			do {

				do {
					logger.log(Level.INFO, "enter Company name");
					companyName = scanner.nextLine();
					isvalid = valid.StringValidation(companyName);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid CompanyName");
					}
				} while (isvalid == false);

				isvalid = false;

				company.setCompanyName(companyName);
				companyId = companyDelegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO, "Please enter the correct company name");
				} else {
					flag = 1;
				}
			} while (flag != 1);

			jobDelegate.displayJobs(jobmapping);

			do {
				logger.log(Level.INFO, "enter JobId of designation you would like to remove");
				String check = scanner.nextLine();
				isvalid = valid.intValidation(check);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid Job Id");
				} else {
					jobId = Integer.parseInt(check);
				}
			} while (isvalid == false);

			isvalid = false;
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			int userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			flag1 = companyDelegate.removeVacancy(company, user);
			if (flag1 == 1) {
				logger.log(Level.INFO, "Vacancy deleted !");
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN REMOVING VACANCY!" + e.getMessage());
		}
	}

	/*
	 * method for deleting existing vacancy by company admin.
	 */

	public void removeVacancyAdmin(User user) {
		try {
			int flag1 = 0;
			int companyId = 0, jobId = 0, userId = 0;
			boolean isvalid = false;
			ArrayList<Company> vacancyDetails = new ArrayList<Company>();
			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			Company company = new Company();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			UserDelegate userDelegate = new UserDelegate();
			JobDelegate jobDelegate = new JobDelegate();
			JobMapping jobmapping = new JobMapping();
			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			companyId = userDelegate.fetchCompanyIdByAdmin(user);
			company.setCompanyId(companyId);
			vacancyDetails = companyDelegate.retrieveVacancyByCompany1(company);
			if (vacancyDetails.isEmpty()) {
				logger.log(Level.INFO, "No vacancy in this Company!!");
			}
			for (Company i : vacancyDetails) {
				logger.log(Level.INFO,
						"\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:" + i.getJobDescription()
								+ "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary()
								+ "\n NUMBER OF VACANCIES:" + i.getVacancyCount());
			}

			job = jobDelegate.displayJobs(jobmapping);
			for (JobMapping i : job) {
				logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
			}

			do {
				logger.log(Level.INFO, "enter JobId of designation you would like to remove");
				String check = scanner.nextLine();
				isvalid = valid.intValidation(check);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid Job Id");
				} else {
					jobId = Integer.parseInt(check);
				}
			} while (isvalid == false);

			company.setCompanyId(companyId);
			company.setJobId(jobId);
			flag1 = companyDelegate.removeVacancy(company, user);
			if (flag1 == 1) {
				logger.log(Level.INFO, "Vacancy deleted !");
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN REMOVING VACANCY!" + e.getMessage());
		}

	}

	/*
	 * method for deleting company by website admin.
	 */
	public void deleteCompany(User user) {
		try {
			int companyId = 0;
			int flag = 0, flag1 = 0;
			char c = '\0';
			String companyName = "";
			boolean isvalid = false;
			ArrayList<Company> companies = new ArrayList<Company>();
			Company company = new Company();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			RoleSeparated roleseparated = new RoleSeparated();
			logger.log(Level.INFO, " \n Companies registered with us :");
			companies = companyDelegate.displayCompanies(company);
			for (Company i : companies) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}

			do {

				do {
					logger.log(Level.INFO, "enter Company name");
					companyName = scanner.nextLine();
					isvalid = valid.StringValidation(companyName);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid CompanyName");
					}
				} while (isvalid == false);

				isvalid = false;

				company.setCompanyName(companyName);
				companyId = companyDelegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO,
							"Sorry this company is not registered!!Do you want to try deleting any other company?(y/n)");
					c = scanner.next().charAt(0);
					scanner.nextLine();
					flag = 0;
				} else {
					flag = 1;
					c = 'n';
				}
			} while (c == 'y' || c == 'Y');
			if (flag == 1) {
				company.setCompanyId(companyId);
				flag1 = companyDelegate.deleteCompany(company);
				if (flag1 == 1) {
					logger.log(Level.INFO, "Company Details deleted !");
				}
			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.siteAdminFlow(user);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN DELETING COMPANY!" + e.getMessage());
		}
	}

	/*
	 * method for adding new job not present in database.
	 */
	public int addNewJob(User user) {
		try {
			int jobId = 0;
			String jobRole = "";
			JobDelegate jobDelegate = new JobDelegate();
			JobMapping jobmapping = new JobMapping();
			boolean isvalid = false;
			do {
				logger.log(Level.INFO, "enter Job Role/Designation");
				scanner.nextLine();
				jobRole = scanner.nextLine();
				isvalid = valid.StringValidation(jobRole);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid input");
				}
			} while (isvalid == false);

			isvalid = false;

			jobmapping.setJobRole(jobRole);
			jobId = jobDelegate.addNewJob(jobmapping, user);
			if (jobId != 0) {
				logger.log(Level.INFO, "New Job designation added !");
			}
			return jobId;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING NEW JOB!" + e.getMessage());
			return 0;
		}
	}

	/*
	 * method for requesting vacancy so that the user is intimated.
	 */
	public void requestVacancy(User user) {
		try {
			int jobId = 0, userId = 0;
			int flag1 = 0;
			String location = "";
			float salary = 0;
			boolean isvalid = false;
			UserDelegate userDelegate = new UserDelegate();
			JobRequest jobrequest = new JobRequest();
			jobId = addingJobDetails(user);
			logger.log(Level.INFO, "Press enter to continue");
			scanner.nextLine();
			do {
				logger.log(Level.INFO, "enter location of job");
				location = scanner.nextLine();
				isvalid = valid.StringValidation(location);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid location");
				}
			} while (isvalid == false);

			isvalid = false;

			do {
				logger.log(Level.INFO, "enter salary (lpa)");
				String check = scanner.nextLine();
				isvalid = valid.floatValidation(check);
				if (isvalid == false) {
					logger.log(Level.INFO, "Please enter a valid salary amount");
				} else {
					salary = Float.parseFloat(check);
				}
			} while (isvalid == false);
			isvalid = false;

			jobrequest.setEmail(user.getEmail());
			jobrequest.setJobId(jobId);
			jobrequest.setLocation(location);
			jobrequest.setSalary(salary);
			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			flag1 = userDelegate.requestNewVacancy(jobrequest, user);
			if (flag1 == 1) {
				logger.log(Level.INFO, "Congrats your request is saved !");
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN PROCESSING VACANCY REQUEST!" + e.getMessage());
		}
	}

	/*
	 * method for reviewing and rating a company.
	 */
	public void reviewAndRateCompany(User user) {
		try {

			int userId = 0;
			int companyId = 0;
			int flag = 0, flag1 = 0;
			char c = '\0';
			boolean isvalid = false;
			float rating = 0;
			ArrayList<Company> companies = new ArrayList<Company>();
			String companyName = "", review = "";
			UserDelegate userDelegate = new UserDelegate();
			Company company = new Company();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);

			logger.log(Level.INFO, " \n Companies registered with us :");
			companies = companyDelegate.displayCompanies(company);
			for (Company i : companies) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			scanner.nextLine();
			do {
				do {
					logger.log(Level.INFO, "enter Company name");
					companyName = scanner.nextLine();
					isvalid = valid.StringValidation(companyName);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid CompanyName");
					}
				} while (isvalid == false);

				isvalid = false;

				company.setCompanyName(companyName);
				companyId = companyDelegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO,
							"Sorry this company is not registered!!Do you want to share your interview experience in any other company?(y/n)");
					c = scanner.next().charAt(0);
					scanner.nextLine();
					flag = 0;
				} else {
					flag = 1;
					c = 'n';
				}
			} while (c == 'y' || c == 'Y');
			if (flag == 1) {

				do {
					logger.log(Level.INFO, "Write a review about the company");
					review = scanner.nextLine();
					isvalid = valid.StringValidation(review);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid input");
					}
				} while (isvalid == false);

				isvalid = false;

				do {
					logger.log(Level.INFO, "Rate the company on a scale of 5");
					String check = scanner.nextLine();
					isvalid = valid.ratingValidation(check);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid input for rating");
					} else {
						rating = Float.parseFloat(check);
					}
				} while (isvalid == false);
				isvalid = false;

				company.setCompanyId(companyId);
				company.setReview(review);
				company.setRating(rating);
				flag1 = userDelegate.reviewAndRateCompany(user, company);
				if (flag1 == 1) {
					logger.log(Level.INFO, "Congrats your comments are added!");
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING YOUR COMMENTS!" + e.getMessage());
		}
	}

	/*
	 * method for adding interview process for a job in a company.
	 */
	public void writeInterviewProcess(User user) {
		try {
			int userId = 0;
			int companyId = 0;
			int jobId = 0;
			int flag = 0, flag1 = 0;
			char c = '\0';
			String companyName = "";
			String interviewProcess = "";
			boolean isvalid = false;
			ArrayList<Company> companies = new ArrayList<Company>();
			Company company = new Company();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			UserDelegate userDelegate = new UserDelegate();
			JobMapping jobmapping = new JobMapping();
			RoleSeparated roleseparated = new RoleSeparated();
			userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			logger.log(Level.INFO, " \n Companies registered with us :");
			companies = companyDelegate.displayCompanies(company);
			for (Company i : companies) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			scanner.nextLine();
			do {
				do {
					logger.log(Level.INFO, "enter Company name");
					companyName = scanner.nextLine();
					isvalid = valid.StringValidation(companyName);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid CompanyName");
					}
				} while (isvalid == false);

				isvalid = false;

				company.setCompanyName(companyName);
				companyId = companyDelegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO,
							"Sorry this company is not registered!!Do you want to share your interview experience in any other company?(y/n)");
					c = scanner.next().charAt(0);
					scanner.nextLine();
					flag = 0;
				} else {
					flag = 1;
					c = 'n';
				}
			} while (c == 'y' || c == 'Y');

			if (flag == 1) {
				jobId = addingJobDetails(user);
				scanner.nextLine();

				do {
					logger.log(Level.INFO, "Share your interview Experience");
					interviewProcess = scanner.nextLine();
					isvalid = valid.StringValidation(interviewProcess);
					if (isvalid == false) {
						logger.log(Level.INFO, "Please enter a valid input");
					}
				} while (isvalid == false);

				isvalid = false;

				company.setCompanyId(companyId);
				jobmapping.setJobId(jobId);
				company.setInterviewProcess(interviewProcess);
				flag1 = userDelegate.interviewProcess(user, company, jobmapping);
				if (flag1 == 1) {
					logger.log(Level.INFO, "Congrats your comments are added!");
				}
			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.userFlow(user);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN ADDING COMMENTS!" + e.getMessage());
		}
	}

	/*
	 * method for updating published vacancy.
	 */
	public void UpdateVacancy(User user) {
		try {
			boolean loop = true;
			boolean flag = false;
			int companyId = 0;
			int oldJobId = 0, jobId = 0;
			boolean isvalid = false;
			String location = "";
			String jobDescription = "";
			int vacancyCount = 0;
			float salary = 0;
			ArrayList<Company> vacancyDetails = new ArrayList<Company>();
			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			JobMapping jobmapping = new JobMapping();
			Company company = new Company();

			CompanyDelegate companyDelegate = new CompanyDelegate();
			UserDelegate userDelegate = new UserDelegate();
			JobDelegate jobDelegate = new JobDelegate();
			int userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			companyId = userDelegate.fetchCompanyIdByAdmin(user);
			company.setCompanyId(companyId);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			user.setCurrentTime(dtf.format(now));

			do {

				logger.log(Level.INFO,
						"Select a field you want to update \n1.JOB DESIGNATION \n2.JOB LOCATION \n3.JOB DESCRIPTION \n4.SALARY \n5.VACANCY COUNT \n6.EXIT");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 1:
					vacancyDetails = companyDelegate.retrieveVacancyByCompanyAdmin(company);
					if (vacancyDetails.isEmpty()) {
						logger.log(Level.INFO, "No vacancy in this Company!!");
					}
					for (Company i : vacancyDetails) {
						logger.log(Level.INFO,
								"\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:" + i.getJobDescription()
										+ "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary()
										+ "\n NUMBER OF VACANCIES:" + i.getVacancyCount());
					}
					job = jobDelegate.displayJobs(jobmapping);
					for (JobMapping i : job) {
						logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
					}

					do {
						logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							oldJobId = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					do {
						logger.log(Level.INFO, "Enter new Designation ID!!");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							jobId = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					company.setOldJobId(oldJobId);
					company.setJobId(jobId);
					flag = companyDelegate.updateVacancyJobId(company, user);
					if (flag == true) {
						logger.log(Level.INFO, "Job Designation updated!");
					} else {
						logger.log(Level.INFO, "Error updating Job Designation!");
					}
					flag = false;
					break;

				case 2:
					vacancyDetails = companyDelegate.retrieveVacancyByCompanyAdmin(company);
					if (vacancyDetails.isEmpty()) {
						logger.log(Level.INFO, "No vacancy in this Company!!");
					}
					for (Company i : vacancyDetails) {
						logger.log(Level.INFO,
								"\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:" + i.getJobDescription()
										+ "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary()
										+ "\n NUMBER OF VACANCIES:" + i.getVacancyCount());
					}
					job = jobDelegate.displayJobs(jobmapping);
					for (JobMapping i : job) {
						logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
					}
					do {
						logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							oldJobId = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					scanner.nextLine();
					company.setOldJobId(oldJobId);

					do {
						logger.log(Level.INFO, "enter location of job");
						location = scanner.nextLine();
						isvalid = valid.StringValidation(location);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid location");
						}
					} while (isvalid == false);

					isvalid = false;

					company.setLocation(location);
					flag = companyDelegate.updateVacancyLocation(company, user);
					if (flag == true) {
						logger.log(Level.INFO, "Job Location updated!");
					} else {
						logger.log(Level.INFO, "Error updating Job Location!");
					}
					flag = false;
					break;

				case 3:
					vacancyDetails = companyDelegate.retrieveVacancyByCompanyAdmin(company);
					if (vacancyDetails.isEmpty()) {
						logger.log(Level.INFO, "No vacancy in this Company!!");
					}
					for (Company i : vacancyDetails) {
						logger.log(Level.INFO,
								"\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:" + i.getJobDescription()
										+ "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary()
										+ "\n NUMBER OF VACANCIES:" + i.getVacancyCount());
					}
					job = jobDelegate.displayJobs(jobmapping);
					for (JobMapping i : job) {
						logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
					}

					do {
						logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							oldJobId = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					scanner.nextLine();
					company.setOldJobId(oldJobId);

					do {
						logger.log(Level.INFO, "Enter new Job Description!!");
						jobDescription = scanner.nextLine();
						isvalid = valid.StringValidation(jobDescription);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid location");
						}
					} while (isvalid == false);

					isvalid = false;

					company.setJobDescription(jobDescription);
					flag = companyDelegate.updateVacancyDescription(company, user);
					if (flag == true) {
						logger.log(Level.INFO, "Job Description updated!");
					} else {
						logger.log(Level.INFO, "Error updating Job Description!");
					}
					flag = false;
					break;
				case 4:
					vacancyDetails = companyDelegate.retrieveVacancyByCompanyAdmin(company);
					if (vacancyDetails.isEmpty()) {
						logger.log(Level.INFO, "No vacancy in this Company!!");
					}
					for (Company i : vacancyDetails) {
						logger.log(Level.INFO,
								"\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:" + i.getJobDescription()
										+ "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary()
										+ "\n NUMBER OF VACANCIES:" + i.getVacancyCount());
					}
					job = jobDelegate.displayJobs(jobmapping);
					for (JobMapping i : job) {
						logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
					}

					do {
						logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							oldJobId = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					scanner.nextLine();
					company.setOldJobId(oldJobId);

					do {
						logger.log(Level.INFO, "Enter new Salary (lpa)");
						String check = scanner.nextLine();
						isvalid = valid.floatValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid salary amount");
						} else {
							salary = Float.parseFloat(check);
						}
					} while (isvalid == false);

					isvalid = false;

					company.setSalary(salary);
					flag = companyDelegate.updateVacancySalary(company, user);
					if (flag == true) {
						logger.log(Level.INFO, "Job Salary updated!");
					} else {
						logger.log(Level.INFO, "Error updating Job Salary!");
					}
					flag = false;
					break;
				case 5:
					vacancyDetails = companyDelegate.retrieveVacancyByCompanyAdmin(company);
					if (vacancyDetails.isEmpty()) {
						logger.log(Level.INFO, "No vacancy in this Company!!");
					}
					for (Company i : vacancyDetails) {
						logger.log(Level.INFO,
								"\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:" + i.getJobDescription()
										+ "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary()
										+ "\n NUMBER OF VACANCIES:" + i.getVacancyCount());
					}
					job = jobDelegate.displayJobs(jobmapping);
					for (JobMapping i : job) {
						logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
					}

					do {
						logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						} else {
							oldJobId = Integer.parseInt(check);
						}
					} while (isvalid == false);

					scanner.nextLine();
					company.setOldJobId(oldJobId);

					do {
						logger.log(Level.INFO, "enter Vacancy count");
						String check = scanner.nextLine();
						isvalid = valid.intValidation(check);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid vacancy count");
						} else {
							vacancyCount = Integer.parseInt(check);
						}
					} while (isvalid == false);

					isvalid = false;

					company.setVacancyCount(vacancyCount);
					if (vacancyCount == 0) {
						company.setVacancyStatus("expired");
					} else {
						company.setVacancyStatus("available");
					}
					flag = companyDelegate.updateVacancyCount(company, user);
					if (flag == true) {
						logger.log(Level.INFO, "Number of vacancies updated!");
					} else {
						logger.log(Level.INFO, "Error updating number of vacancies!");
					}
					flag = false;
					break;
				case 6:
					loop = false;
					break;
				}
			} while (loop == true);

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN UPDATING VACANCY!" + e.getMessage());
		}
	}

	/*
	 * method for updating user profile.
	 */

	public void updateUserAccount(User user) {
		try {
			boolean isvalid = false;
			int no=0,oldTechnologyId=0,newTechnologyId=0;
			String userName = "", designation = "", company = "";
			UserDelegate userDelegate = new UserDelegate();
			int userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			boolean loop = true;
			boolean flag = false;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			user.setCurrentTime(dtf.format(now));

			do {
				logger.log(Level.INFO,
						"Select a field you want to update \n1.USERNAME \n2.COMPANYNAME \n3.DESIGNATION \n4.TECHNOLOGY \n5.EXIT");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 1:

					do {
						logger.log(Level.INFO, "Enter UserName");
						userName = scanner.nextLine();
						isvalid = valid.StringValidation(userName);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid userName");
						}
					} while (isvalid == false);

					isvalid = false;

					user.setUserName(userName);
					flag = userDelegate.updateUserName(user);
					if (flag == true) {
						logger.log(Level.INFO, "User Name Updated!");
					} else {
						logger.log(Level.INFO, "Error updating username!");
					}
					flag = false;
					break;
				case 2:
					do {
						logger.log(Level.INFO, "Enter Company/College");
						company = scanner.nextLine();
						isvalid = valid.StringValidation(company);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						}
					} while (isvalid == false);

					isvalid = false;

					user.setCompany(company);
					flag = userDelegate.updateCompanyName(user);
					if (flag == true) {
						logger.log(Level.INFO, "Company Name Updated!");
					} else {
						logger.log(Level.INFO, "Error updating company name!");
					}
					flag = false;
					break;
				case 3:

					do {
						logger.log(Level.INFO, "Enter Designation/CurrentStatus");
						designation = scanner.nextLine();
						isvalid = valid.StringValidation(designation);
						if (isvalid == false) {
							logger.log(Level.INFO, "Please enter a valid input");
						}
					} while (isvalid == false);

					isvalid = false;

					user.setDesignation(designation);
					flag = userDelegate.updateUserDesignation(user);
					if (flag == true) {
						logger.log(Level.INFO, "Designation Updated!");
					} else {
						logger.log(Level.INFO, "Error updating designation!");
					}
					flag = false;
					break;
				case 4: {
					boolean status = true;
					Technology technology = new Technology();
					UserTechnologyMapping userTechnologyMapping = new UserTechnologyMapping();
					ArrayList<Technology> technologies = new ArrayList<Technology>();
					ArrayList<UserTechnologyMapping> userTechnology = new ArrayList<UserTechnologyMapping>();
					userTechnology = userDelegate.displayUserTechnologies(userTechnologyMapping, user);
					do {
						logger.log(Level.INFO,
								"\n ENTER YOUR CHOICE: \n 1.CHANGE EXISTING TECHNOLOGY \n 2.ADD NEW TECHNOLOGY \n 3.EXIT");
						int ch = scanner.nextInt();
						switch (ch) {
						case 1:
							logger.log(Level.INFO, "\n ID's OF TECHNOLOGIES SAVED:");
							for (UserTechnologyMapping i : userTechnology) {
								logger.log(Level.INFO, "\n" + i.getTechnologyId());
							}
							logger.log(Level.INFO, "\n TECHNOLOGY ID \t TECHNOLOGY");
							technologies = userDelegate.displayTechnologies(technology);
							for (Technology i : technologies) {
								logger.log(Level.INFO, "\n" + i.getTechnologyId() + "\t" + i.getTechnology());
							}

							do {
								logger.log(Level.INFO, "\n Enter how many technology you want to update:");
								String check = scanner.nextLine();
								isvalid = valid.intValidation(check);
								if (isvalid == false) {
									logger.log(Level.INFO, "Please enter a valid input");
								} else {
									no = Integer.parseInt(check);
								}
							} while (isvalid == false);

							isvalid = false;

							/*
							logger.log(Level.INFO, "\n Enter how many technology you want to update:");
							int no = scanner.nextInt();
							*/
							for (int i = 0; i < no; i++) {
								
								do {
									logger.log(Level.INFO, "\n Enter the id of technology you want to change");
									String check = scanner.nextLine();
									isvalid = valid.intValidation(check);
									if (isvalid == false) {
										logger.log(Level.INFO, "Please enter a valid input");
									} else {
										oldTechnologyId = Integer.parseInt(check);
									}
								} while (isvalid == false);

								isvalid = false;
								
								do {
									logger.log(Level.INFO, "\n Enter the id of technology you want to add");
									String check = scanner.nextLine();
									isvalid = valid.intValidation(check);
									if (isvalid == false) {
										logger.log(Level.INFO, "Please enter a valid input");
									} else {
										newTechnologyId = Integer.parseInt(check);
									}
								} while (isvalid == false);

								isvalid = false;
								
								/*
								logger.log(Level.INFO, "\n Enter the id of technology you want to change");
								int oldTechnologyId = scanner.nextInt();
								logger.log(Level.INFO, "\n Enter the id of technology you want to add");
								int newTechnologyId = scanner.nextInt();
								*/
								userTechnologyMapping.setOldTechnologyId(oldTechnologyId);
								userTechnologyMapping.setTechnologyId(newTechnologyId);
								flag = userDelegate.updateUserTechnology(userTechnologyMapping, user);
								if(flag==true){
									logger.log(Level.INFO, "Technology updated in your profile");
								}
							
							}
							break;
						case 2:
							addTechnologyDetails(user);
							break;
						case 3:
							status = false;
							break;
						default:
							logger.log(Level.INFO, "Enter a valid choice");
							break;
						}
					} while (status == true);
					break;
				}
				case 5:
					loop = false;
					break;
				default:
					logger.log(Level.INFO, "ENTER A VALID CHOICE");
					break;

				}
			} while (loop == true);

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN UPDATING PROFILE!" + e.getMessage());
		}
	}

}
