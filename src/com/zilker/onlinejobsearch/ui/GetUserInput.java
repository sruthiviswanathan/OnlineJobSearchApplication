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

public class GetUserInput {

	private static final Logger logger = Logger.getLogger(GetUserInput.class.getName());
	public Scanner scanner = new Scanner(System.in);
	Validation valid = new Validation();

	/*
	 * method for registering as a user.
	 */
	public void register() {

		try {
			boolean check = false;
			boolean exist = false;
			int flag = 0;
			String password = "";
			User user = new User();
			UserDelegate userDelegate = new UserDelegate();
			Technology technology = new Technology();
			ArrayList<Technology> tech = new ArrayList<Technology>();
			logger.log(Level.INFO, "Enter UserName");
			String userName = scanner.nextLine();
			do {
				String email = valid.emailValidation();
				user.setEmail(email);
				exist = userDelegate.ifAlreadyExists(user);
				if (exist == true) {
					logger.log(Level.INFO, "This email id has already been registered!!");
				} else {
					exist = false;
				}
			} while (exist != false);
			do {
				password = valid.passwordValidation();
				logger.log(Level.INFO, "Please confirm your password");
				String confirmPassword = scanner.nextLine();
				if (password.equals(confirmPassword)) {
					check = true;
				}
				if (check == false) {
					logger.log(Level.INFO, "Fields of password and confirm password must match!!!");
				}
			} while (check != true);
			logger.log(Level.INFO, "Enter Company/College");
			String company = scanner.nextLine();
			logger.log(Level.INFO, "Enter Designation/CurrentStatus");
			String designation = scanner.nextLine();
			user.setUserName(userName);
			user.setPassword(password);
			user.setCompany(company);
			user.setDesignation(designation);
			flag = userDelegate.register(user);
			int userId = userDelegate.fetchUserId(user);
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
			boolean loop = true;
			int flag1 = 0;
			int technologyId = 0;
			UserDelegate userDelegate = new UserDelegate();
			UserTechnologyMapping usertechnology = new UserTechnologyMapping();

			do {
				logger.log(Level.INFO, "ENTER A CHOICE \n 1.ADD A TECHNOLOGY LISTED ABOVE"
						+ "\n 2.ADD A NEW TECHNOLOGY NOT LISTED \n 3.EXIT");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1:
					logger.log(Level.INFO, "Enter how many technologies you want to add to your profile?");
					int numbers = scanner.nextInt();
					for (int i = 0; i < numbers; i++) {
						logger.log(Level.INFO, "Enter technology ID");
						technologyId = scanner.nextInt();
						usertechnology.setUserId(user.getUserId());
						usertechnology.setTechnologyId(technologyId);
						flag1 = userDelegate.addTechnologyDetails(usertechnology);
						if (flag1 == 1) {
							logger.log(Level.INFO, "Technology added to your profile !");
						}
					}
					break;
				case 2:
					logger.log(Level.INFO, "Enter how many technologies you want to add to your profile?");
					int number = scanner.nextInt();
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
			boolean check = false;
			boolean exist = false;
			int flag = 0, flag1 = 0, companyId = 0, flag2 = 0;
			String companyName = "";
			String password = "";
			User user = new User();
			UserDelegate userDelegate = new UserDelegate();
			Company company = new Company();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			logger.log(Level.INFO, "Enter UserName");
			String userName = scanner.nextLine();
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
				String email = valid.emailValidation();
				user.setEmail(email);
				exist = userDelegate.ifAlreadyExists(user);
				if (exist == true) {
					logger.log(Level.INFO, "***This email id has already been registered!!***");
				} else {
					exist = false;
				}
			} while (exist != false);

			do {
				password = valid.passwordValidation();
				logger.log(Level.INFO, "Please confirm your password");
				String confirmPassword = scanner.nextLine();
				if (password.equals(confirmPassword)) {
					check = true;
				}
				if (check == false) {
					logger.log(Level.INFO, "***Fields of password and confirm password must match!!!***");
				}
			} while (check != true);
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
	public void deleteUserAccount() {
		try {
			int flag = 0;
			int userId = 0;
			char c = '\0';
			User user = new User();

			UserDelegate userDelegate = new UserDelegate();
			logger.log(Level.INFO, " Reenter Email");
			String email = scanner.next();
			logger.log(Level.INFO, " Reenter Password");
			String password = scanner.next();
			user.setEmail(email);
			user.setPassword(password);
			userId = userDelegate.fetchUserId(user);
			if (userId != 0) {
				// logger.log(Level.INFO, "User details fetched!");
			}
			if (userId == 0) {
				logger.log(Level.INFO, "Invalid account crdentials!!Do you still want to delete?(y/n)");
				c = scanner.next().charAt(0);
				if (c == 'y' || c == 'Y') {
					deleteUserAccount();
				}
			} else {
				user.setUserId(userId);
				flag = userDelegate.deleteUserAccount(user);
				if (flag == 1) {
					logger.log(Level.INFO, "YOUR ACCOUNT HAS BEEN DELETED!!!");
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
			int companyId = 0;
			int flag = 0;
			logger.log(Level.INFO, " \n Companies Registered With Us :");
			displayCompanies = companyDelegate.displayCompanies(company);
			for (Company i : displayCompanies) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}

			do {
				logger.log(Level.INFO, "Enter Company Name");
				String companyName = scanner.nextLine();
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
			}
			for (Company i : vacancyDetails) {
				int jobId = i.getJobId();
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

			do {
				logger.log(Level.INFO, "Enter your preferred location");
				String location = scanner.nextLine();
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
					flag = 1;
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
			RoleSeparated roleseparated = new RoleSeparated();
			Company company = new Company();
			JobMapping jobmapping = new JobMapping();
			int jobId = 0;
			int flag = 0;
			char c;
			job = jobDelegate.displayJobs(jobmapping);
			for (JobMapping i : job) {
				logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
			}
			do {
				logger.log(Level.INFO, "Enter Job Designation");
				String jobDesignation = scanner.nextLine();
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
							"\nJOB DESCRIPTION:" + i.getJobDescription() + "\nLOCATION:" + i.getLocation() + "\nSALARY:"
									+ i.getSalary() + "\n NUMBER OF VACANCIES:" + i.getVacancyCount());

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

			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.userFlow(user);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "ERROR IN RETRIEVING JOBS!" + e.getMessage());
		}
	}

	/*
	 * method for adding new company details.
	 */
	public void addNewCompany() {
		int flag = 0;
		try {

			CompanyDelegate companyDelegate = new CompanyDelegate();
			Company company = new Company();
			logger.log(Level.INFO, "enter Company name");
			String companyName = scanner.nextLine();
			logger.log(Level.INFO, "enter Company website url");
			String companyWebsiteUrl = scanner.nextLine();
			company.setCompanyName(companyName);
			company.setCompanyWebsiteUrl(companyWebsiteUrl);
			// user.setUserId(user.getUserId());
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
			logger.log(Level.INFO, "enter Company name");
			String companyName = scanner.nextLine();
			logger.log(Level.INFO, "enter Company website url");
			String companyWebsiteUrl = scanner.nextLine();
			company.setCompanyName(companyName);
			company.setCompanyWebsiteUrl(companyWebsiteUrl);
			int userId = userDelegate.fetchUserId(user);
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

			CompanyDelegate companyDelegate = new CompanyDelegate();
			ArrayList<Company> comp = new ArrayList<Company>();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = companyDelegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			do {
				logger.log(Level.INFO, "Enter Company name");
				String companyName = scanner.nextLine();
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

			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			int jobId = 0;
			JobMapping jobmapping = new JobMapping();

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
				logger.log(Level.INFO, "enter any one of the listed Job IDs");
				jobId = scanner.nextInt();
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
			companyId = addingCompanyDetails(user);
			jobId = addingJobDetails(user);
			logger.log(Level.INFO, "Press enter to continue");
			scanner.nextLine();
			logger.log(Level.INFO, "enter location of job");
			String location = scanner.nextLine();
			logger.log(Level.INFO, "enter job Description");
			String jobDescannerription = scanner.nextLine();
			logger.log(Level.INFO, "enter salary (lpa)");
			float salary = scanner.nextFloat();
			logger.log(Level.INFO, "enter Vacancy count");
			int vacancyCount = scanner.nextInt();
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			company.setLocation(location);
			company.setJobDescription(jobDescannerription);
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
			int companyId = 0;
			int jobId = 0;

			int userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			companyId = userDelegate.fetchCompanyIdByAdmin(user);
			jobId = addingJobDetails(user);
			logger.log(Level.INFO, "Press enter to continue");
			scanner.nextLine();
			logger.log(Level.INFO, "enter location of job");
			String location = scanner.nextLine();
			logger.log(Level.INFO, "enter job Description");
			String jobDescannerription = scanner.nextLine();
			logger.log(Level.INFO, "enter salary (lpa)");
			float salary = scanner.nextFloat();
			logger.log(Level.INFO, "enter Vacancy count");
			int vacancyCount = scanner.nextInt();
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			company.setLocation(location);
			company.setJobDescription(jobDescannerription);
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
				logger.log(Level.INFO, "enter Company name you would like to remove");
				String companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = companyDelegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO, "Please enter the correct company name");
				} else {
					flag = 1;
				}
			} while (flag != 1);
			jobDelegate.displayJobs(jobmapping);
			logger.log(Level.INFO, "enter JobId of designation you would like to remove");
			int jobId = scanner.nextInt();
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
			int companyId = 0;
			ArrayList<Company> vacancyDetails = new ArrayList<Company>();
			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			Company company = new Company();
			CompanyDelegate companyDelegate = new CompanyDelegate();
			UserDelegate userDelegate = new UserDelegate();
			JobDelegate jobDelegate = new JobDelegate();
			JobMapping jobmapping = new JobMapping();
			int userId = userDelegate.fetchUserId(user);
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
			logger.log(Level.INFO, "enter JobId of designation you would like to remove");
			int jobId = scanner.nextInt();
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
				logger.log(Level.INFO, "enter Company name");
				String companyName = scanner.nextLine();
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

			JobDelegate jobDelegate = new JobDelegate();
			JobMapping jobmapping = new JobMapping();
			logger.log(Level.INFO, "enter Job Role/Designation");
			scanner.nextLine();
			String jobRole = scanner.nextLine();
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
			int jobId = 0;
			int flag1 = 0;

			UserDelegate userDelegate = new UserDelegate();
			JobRequest jobrequest = new JobRequest();
			jobId = addingJobDetails(user);
			logger.log(Level.INFO, "Press enter to continue");
			scanner.nextLine();
			logger.log(Level.INFO, "Enter location of job");
			String location = scanner.nextLine();
			logger.log(Level.INFO, "Enter salary (lpa)");
			float salary = scanner.nextFloat();
			jobrequest.setEmail(user.getEmail());
			jobrequest.setJobId(jobId);
			jobrequest.setLocation(location);
			jobrequest.setSalary(salary);
			int userId = userDelegate.fetchUserId(user);
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
			ArrayList<Company> companies = new ArrayList<Company>();

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
				logger.log(Level.INFO, "Enter Company name");
				String companyName = scanner.nextLine();
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
				logger.log(Level.INFO, "Write a review about the company");
				String review = scanner.nextLine();
				logger.log(Level.INFO, "Rate the company on a scale of 5");
				float rating = scanner.nextFloat();
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
				logger.log(Level.INFO, "Enter Company name");
				String companyName = scanner.nextLine();
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
				logger.log(Level.INFO, "Share your interview Experience");
				String interviewProcess = scanner.nextLine();
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
			int oldJobId = 0;
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
					logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
					oldJobId = scanner.nextInt();
					company.setOldJobId(oldJobId);
					logger.log(Level.INFO, "Enter new Designation ID!!");
					int jobId = scanner.nextInt();
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
					logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
					oldJobId = scanner.nextInt();
					scanner.nextLine();
					company.setOldJobId(oldJobId);
					logger.log(Level.INFO, "Enter new Location!!");
					String location = scanner.nextLine();
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
					logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
					oldJobId = scanner.nextInt();
					scanner.nextLine();
					company.setOldJobId(oldJobId);
					logger.log(Level.INFO, "Enter new Job Description!!");
					String jobDescription = scanner.nextLine();
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
					logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
					oldJobId = scanner.nextInt();
					scanner.nextLine();
					company.setOldJobId(oldJobId);
					logger.log(Level.INFO, "Enter new Salary!!");
					float salary = scanner.nextFloat();
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
					logger.log(Level.INFO, "Enter ID of designation that needs to be updated!!");
					oldJobId = scanner.nextInt();
					scanner.nextLine();
					company.setOldJobId(oldJobId);
					logger.log(Level.INFO, "Enter current number of vacancies!!");
					int vacancyCount = scanner.nextInt();
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

			UserDelegate userDelegate = new UserDelegate();
			int userId = userDelegate.fetchUserId(user);
			user.setUserId(userId);
			boolean loop = true;
			boolean flag = false;
			do {
				logger.log(Level.INFO,
						"Select a field you want to update \n1.USERNAME \n2.COMPANYNAME \n3.DESIGNATION \n4.TECHNOLOGY \n5.EXIT");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 1:
					logger.log(Level.INFO, "Enter new user name");
					String userName = scanner.nextLine();
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
					logger.log(Level.INFO, "Enter new company name");
					String companyName = scanner.nextLine();
					user.setCompany(companyName);
					flag = userDelegate.updateCompanyName(user);
					if (flag == true) {
						logger.log(Level.INFO, "Company Name Updated!");
					} else {
						logger.log(Level.INFO, "Error updating company name!");
					}
					flag = false;
					break;
				case 3:
					logger.log(Level.INFO, "Enter new designation");
					String designation = scanner.nextLine();
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

							logger.log(Level.INFO, "\n Enter how many technology you want to update:");
							int no = scanner.nextInt();
							for (int i = 0; i < no; i++) {
								logger.log(Level.INFO, "\n Enter the id of technology you want to change");
								int oldTechnologyId = scanner.nextInt();
								logger.log(Level.INFO, "\n Enter the id of technology you want to add");
								int newTechnologyId = scanner.nextInt();
								userTechnologyMapping.setOldTechnologyId(oldTechnologyId);
								userTechnologyMapping.setTechnologyId(newTechnologyId);
								flag = userDelegate.updateUserTechnology(userTechnologyMapping, user);
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
