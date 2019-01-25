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
import com.zilker.onlinejobsearch.delegate.Delegate;
import com.zilker.onlinejobsearch.utils.Validation;

public class GetUserInput {

	private static final Logger logger = Logger.getLogger(GetUserInput.class.getName());
	public Scanner scanner = new Scanner(System.in);
	Validation valid = new Validation();

	/*
	 * method for registering as a normal user.
	 */
	public void register() {

		try {
			boolean exist = false;
			int flag = 0, flag1 = 0;
			ArrayList<Technology> tech = new ArrayList<Technology>();
			User user = new User();
			Delegate delegate = new Delegate();
			Technology technology = new Technology();
			UserTechnologyMapping usertechnology = new UserTechnologyMapping();
			// scanner.nextLine();
			logger.log(Level.INFO, "enter UserName");
			String userName = scanner.nextLine();
			do {
				String email = valid.emailValidation();
				// email = valid.emailValidation();
				user.setEmail(email);
				exist = delegate.ifAlreadyExists(user);
				if (exist == true) {
					logger.log(Level.INFO, "This email id has already been registered!!");
				} else {
					exist = false;
				}
			} while (exist != false);
			String password = valid.passwordValidation();
			// scanner.nextLine();
			logger.log(Level.INFO, "enter Company/college");
			String company = scanner.nextLine();
			logger.log(Level.INFO, "enter designation/current status");
			String designation = scanner.nextLine();
			user.setUserName(userName);
			// user.setEmail(email);
			user.setPassword(password);
			user.setCompany(company);
			user.setDesignation(designation);
			flag = delegate.register(user);
			int userId = delegate.fetchUserId(user);
			logger.log(Level.INFO, "Do you want to add Technologies known to your profile?(If yes press y else n)");
			char choice = scanner.next().charAt(0);
			if (choice == 'Y' || choice == 'y') {
				user.setUserId(userId);
				addTechnologyDetails(user);
			}
			if (flag == 1) {
				logger.log(Level.INFO, "Congrats you are Registered !");
			}
			
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN REGISTERING!" + e.getMessage());
		}
	}
	
	
	
	public void addTechnologyDetails(User user) {
		try{
		int flag1 = 0;
		ArrayList<Technology> tech = new ArrayList<Technology>();
		Delegate delegate = new Delegate();
		Technology technology = new Technology();
		UserTechnologyMapping usertechnology = new UserTechnologyMapping();
		tech = delegate.displayTechnologies(technology);
		for (Technology i : tech) {
			logger.log(Level.INFO, "\n" + i.getTechnologyId() + "\t" + i.getTechnology());
		}

		logger.log(Level.INFO, "enter how many technologies you want to add to your profile?");
		int numbers = scanner.nextInt();
		for (int i = 0; i < numbers; i++) {
			logger.log(Level.INFO, "enter technology ID");
			int technologyId = scanner.nextInt();
			usertechnology.setUserId(user.getUserId());
			usertechnology.setTechnologyId(technologyId);
			flag1 = delegate.addTechnologyDetails(usertechnology);
			if (flag1 == 1) {
				logger.log(Level.INFO, "Technology added to your profile !");
			}
		}
	}catch (SQLException e) {
		logger.log(Level.INFO, "ERROR IN ADDING TECHNOLOGY TO PROFILE!" + e.getMessage());
	}
		
	}
	/*
	 * method for registering as a company admin
	 */

	public void registerAsAdmin() {

		try {
			char c = '\0';
			boolean exist = false;
			int flag = 0, flag1 = 0, companyId = 0, flag2 = 0;
			String companyName = "";
			User user = new User();
			Company company = new Company();
			Delegate delegate = new Delegate();
			logger.log(Level.INFO, "enter UserName");
			String userName = scanner.nextLine();
			do {
				logger.log(Level.INFO, "enter Company name");
				companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					do {
						logger.log(Level.INFO,
								"\n This company is not registered with us!!Enter a choice \n 1.Retry \n 2.Add your company");
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
							logger.log(Level.INFO, "enter a valid choice");
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
				// email = valid.emailValidation();
				user.setEmail(email);
				exist = delegate.ifAlreadyExists(user);
				if (exist == true) {
					logger.log(Level.INFO, "This email id has already been registered!!");
				} else {
					exist = false;
				}
			} while (exist != false);
			String password = valid.passwordValidation();
			user.setUserName(userName);
			user.setPassword(password);
			user.setCompany(companyName);
			user.setDesignation("admin");
			user.setRoleId(2);
			flag = delegate.registerAsAdmin(user);
			if (flag == 1) {
				logger.log(Level.INFO, "Congrats you are Registered !");
			}

			int userId = delegate.fetchUserId(user);
			if (userId != 0) {
				logger.log(Level.INFO, "user details fetched");
				user.setUserId(userId);
				flag2 = delegate.insertIntoAdmin(user, company);
				if (flag2 == 1) {
					logger.log(Level.INFO, "YOU ARE NOW AN ADMIN!");
				}
			}

		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN REGISTERING!" + e.getMessage());
		}

	}

	/*
	 * method for getting user input for logging in.
	 */
	public void login() {
		try {
			int i = 0;
			User user = new User();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			logger.log(Level.INFO, "enter Email");
			String email = scanner.next();
			// email = scanner.next();
			logger.log(Level.INFO, "enter Password");
			String password = scanner.next();
			user.setEmail(email);
			user.setPassword(password);
			i = delegate.login(user);
			if (i == 0) {
				logger.log(Level.INFO, "Invalid login credentials!");
			} else if (i == 1) {
				logger.log(Level.INFO, "Congrats you are logged in !");
				roleseparated.userFlow(user);
			} else if (i == 2) {
				logger.log(Level.INFO, "Congrats you are logged in !");
				roleseparated.adminFlow(user);
			} else if (i == 3) {
				logger.log(Level.INFO, "Congrats you are logged in !");
				roleseparated.siteAdminFlow();
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
			Delegate delegate = new Delegate();
			logger.log(Level.INFO, " Reenter Email");
			String email = scanner.next();
			logger.log(Level.INFO, " Reenter Password");
			String password = scanner.next();
			user.setEmail(email);
			user.setPassword(password);
			userId = delegate.fetchUserId(user);
			if (userId != 0) {
				logger.log(Level.INFO, "User details fetched !");
			}
			if (userId == 0) {
				logger.log(Level.INFO, "Invalid account crdentials!!Do you still want to delete?(y/n)");
				c = scanner.next().charAt(0);
				if (c == 'y' || c == 'Y') {
					deleteUserAccount();
				}
			} else {
				user.setUserId(userId);
				flag = delegate.deleteUserAccount(user);
				if (flag == 1) {
					logger.log(Level.INFO, "User deleted !");
				}
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN DELETING ACCOUNT!" + e.getMessage());
		}
	}

	/*
	 * method for getting user input for searching based on company.
	 */
	public void searchCompany(User user) {
		try {
			ArrayList<Company> comp = new ArrayList<Company>();
			ArrayList<Company> comp1 = new ArrayList<Company>();
			ArrayList<Company> comp2 = new ArrayList<Company>();
			ArrayList<Company> comp3 = new ArrayList<Company>();
			Company company = new Company();
			Delegate delegate = new Delegate();
			int companyId = 0;
			int flag = 0;
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = delegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}

			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO, "Company has is not registered with us as of now");
				} else {
					flag = 1;
				}
			} while (flag != 1);
			company.setCompanyId(companyId);
			comp1 = delegate.retrieveVacancyByCompany(company);
			if (comp1.isEmpty()) {
				logger.log(Level.INFO, "No vacancy in this Company!!");
			}
			for (Company j : comp1) {
				logger.log(Level.INFO, "\nCOMPANY NAME:" + j.getCompanyName() + "\nWEBSITE URL:"
						+ j.getCompanyWebsiteUrl() + "\nAVERAGE RATING:" + j.getAverageRating());
				break;
			}
			comp2 = delegate.retrieveVacancyByCompany1(company);
			if (comp2.isEmpty()) {
				logger.log(Level.INFO, "No vacancy in this Company!!");
			}
			for (Company i : comp2) {
				logger.log(Level.INFO, "\nJOB DESIGNATION:" + i.getJobRole() + "\nJOB DESCRIPTION:"
						+ i.getJobDescription() + "\nLOCATION:" + i.getLocation() + "\nSALARY(lpa):" + i.getSalary());
			}
			comp3 = delegate.retrieveReview(company);
			logger.log(Level.INFO, "REVIEWS");
			if (comp3.isEmpty()) {
				logger.log(Level.INFO, "No Reviews for this Company!!");
			}
			for (Company i : comp3) {
				logger.log(Level.INFO, i.getReview());
			}

		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN RETREIVING COMPANY!" + e.getMessage());
		}
	}

	/*
	 * method for getting user input for searching based on jobs.
	 */
	public void searchJobs(User user) {
		try {
			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			ArrayList<Company> comp = new ArrayList<Company>();
			ArrayList<Company> comp1 = new ArrayList<Company>();
			ArrayList<Company> comp2 = new ArrayList<Company>();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			Company company = new Company();
			JobMapping jobmapping = new JobMapping();
			int jobId = 0;
			int flag = 0;
			char c;
			job = delegate.displayJobs(jobmapping);
			for (JobMapping i : job) {
				logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
			}
			do {
				logger.log(Level.INFO, "enter Job designation");
				String jobDesignation = scanner.nextLine();
				jobmapping.setJobRole(jobDesignation);
				jobId = delegate.fetchJobId(jobmapping);
				if (jobId == 0) {
					logger.log(Level.INFO,
							"No vacancies in this designation as of now!!Do you still want to search for other job designation?(y/n)");
					c = scanner.next().charAt(0);
					scanner.nextLine();
					flag = 0;
				} else {
					logger.log(Level.INFO, "job details fetched !");
					flag = 1;
					c = 'n';
				}
			} while (c == 'y' || c == 'Y');
			if (flag == 1) {
				company.setJobId(jobId);
				comp = delegate.retrieveVacancyByJob(company);
				if (comp.isEmpty()) {
					logger.log(Level.SEVERE, "No vacancy in this designation!!");
				}
				for (Company i : comp) {
					logger.log(Level.INFO, "\nJOB DESIGNATION:" + i.getJobRole());
				}
				comp1 = delegate.retrieveVacancyByJob1(company);
				if (comp1.isEmpty()) {
					logger.log(Level.SEVERE, "No vacancy in this designation!!");
				}
				for (Company i : comp1) {
					logger.log(Level.INFO, "\nCOMPANY NAME:" + i.getCompanyName() + "\nWEBSITE URL:"
							+ i.getCompanyWebsiteUrl() + "\nAVERAGE RATING:" + i.getAverageRating());
					logger.log(Level.INFO, "\nJOB DESCRIPTION:" + i.getJobDescription() + "\nLOCATION:"
							+ i.getLocation() + "\nSALARY:" + i.getSalary());

				}
				comp2 = delegate.retrieveInterviewProcess(company);
				logger.log(Level.INFO, "\nREVIEWS:");
				if (comp2.isEmpty()) {
					logger.log(Level.SEVERE, "No reviews on interview process!!");
				}
				for (Company j : comp2) {
					logger.log(Level.INFO,
							"\n COMPANY NAME:" + j.getCompanyName() + "\n INTERVIEW PROCESS" + j.getInterviewProcess());
				}

			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.userFlow(user);
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN RETRIEVING JOBS!" + e.getMessage());
		}
	}

	/*
	 * method for adding new company.
	 */
	public void addNewCompany() {
		int flag = 0;
		try {
			Delegate delegate = new Delegate();
			Company company = new Company();
			logger.log(Level.INFO, "enter Company name");
			String companyName = scanner.nextLine();
			logger.log(Level.INFO, "enter Company website url");
			String companyWebsiteUrl = scanner.nextLine();
			company.setCompanyName(companyName);
			company.setCompanyWebsiteUrl(companyWebsiteUrl);
			flag = delegate.addNewCompany(company);
			if (flag == 1) {
				logger.log(Level.INFO, "Your company details are added !");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING NEW COMPANY!" + e.getMessage());
		}
	}

	/*
	 * method for adding company.
	 */
	public int addingCompanyDetails() {
		try {
			int companyId = 0;
			int flag = 0;
			Company company = new Company();
			Delegate delegate = new Delegate();
			ArrayList<Company> comp = new ArrayList<Company>();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = delegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO, "Please add the company first");
					addNewCompany();
				} else {
					flag = 1;
					logger.log(Level.INFO, "Company details fetched !");
				}
			} while (flag != 1);
			return companyId;
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING COMPANY DETAILS!" + e.getMessage());
			return 0;
		}
	}

	/*
	 * method for adding job details.
	 */
	public int addingJobDetails() {
		try {

			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			int jobId = 0;
			JobMapping jobmapping = new JobMapping();
			Delegate delegate = new Delegate();
			job = delegate.displayJobs(jobmapping);
			for (JobMapping i : job) {
				logger.log(Level.INFO, "\n" + i.getJobId() + "\t" + i.getJobRole());
			}
			logger.log(Level.INFO, "enter Job ID");
			logger.log(Level.INFO, "Do you want to add a new Job Description not listed above(y/n)?");
			char ch = scanner.next().charAt(0);
			if (ch == 'y' || ch == 'Y') {
				jobId = addNewJob();
			} else {
				logger.log(Level.INFO, "enter any one of the listed Job IDs");
				jobId = scanner.nextInt();
			}
			return jobId;
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING JOB DETAILS!" + e.getMessage());
			return 0;
		}
	}

	/*
	 * method for publishing new vacancy.
	 */
	public void publishVacancy() {
		try {
			int flag = 0;
			Company company = new Company();
			Delegate delegate = new Delegate();
			int companyId = 0;
			int jobId = 0;
			companyId = addingCompanyDetails();
			jobId = addingJobDetails();
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
			flag = delegate.publishVacancy(company);
			if (flag == 1) {
				logger.log(Level.INFO, "Your company vacancy is published !");
			}
			delegate.compareVacancyWithRequest(company);
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN PUBLISHING VACANCY!" + e.getMessage());
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
			Delegate delegate = new Delegate();
			int companyId = 0;
			int jobId = 0;

			int userId = delegate.fetchUserId(user);
			user.setUserId(userId);
			companyId = delegate.fetchCompanyIdByAdmin(user);
			jobId = addingJobDetails();
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
			flag = delegate.publishVacancy(company);
			if (flag == 1) {
				logger.log(Level.INFO, "Your company vacancy is published !");
			}
			delegate.compareVacancyWithRequest(company);
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN PUBLISHING VACANCY!" + e.getMessage());
		}
	}

	/*
	 * method for deleting existing vacancy.
	 */
	public void removeVacancy() {
		try {
			int flag = 0, flag1 = 0;
			int companyId = 0;
			Company company = new Company();
			Delegate delegate = new Delegate();
			JobMapping jobmapping = new JobMapping();
			ArrayList<Company> comp = new ArrayList<Company>();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = delegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			do {
				logger.log(Level.INFO, "enter Company name you would like to remove");
				String companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO, "Please enter the correct company name");
				} else {
					flag = 1;
				}
			} while (flag != 1);
			delegate.displayJobs(jobmapping);
			logger.log(Level.INFO, "enter JobId of designation you would like to remove");
			int jobId = scanner.nextInt();
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			flag1 = delegate.removeVacancy(company);
			if (flag1 == 1) {
				logger.log(Level.INFO, "Vacancy deleted !");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN REMOVING VACANCY!" + e.getMessage());
		}
	}

	/*
	 * method for deleting existing vacancy by company admin.
	 */

	public void removeVacancyAdmin(User user) {
		try {
			int flag1 = 0;
			int companyId = 0;
			Company company = new Company();
			Delegate delegate = new Delegate();
			JobMapping jobmapping = new JobMapping();

			int userId = delegate.fetchUserId(user);
			user.setUserId(userId);
			companyId = delegate.fetchCompanyIdByAdmin(user);

			delegate.displayJobs(jobmapping);
			logger.log(Level.INFO, "enter JobId of designation you would like to remove");
			int jobId = scanner.nextInt();
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			flag1 = delegate.removeVacancy(company);
			if (flag1 == 1) {
				logger.log(Level.INFO, "Vacancy deleted !");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN REMOVING VACANCY!" + e.getMessage());
		}

	}

	/*
	 * method for deleting company.
	 */
	public void deleteCompany() {
		try {
			int companyId = 0;
			int flag = 0, flag1 = 0;
			char c = '\0';
			ArrayList<Company> comp = new ArrayList<Company>();
			Company company = new Company();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = delegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}

			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
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
				flag1 = delegate.deleteCompany(company);
				if (flag1 == 1) {
					logger.log(Level.INFO, "Company Details deleted !");
				}
			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.siteAdminFlow();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN DELETING COMPANY!" + e.getMessage());
		}
	}

	/*
	 * method for adding new job.
	 */
	public int addNewJob() {
		try {
			int jobId = 0;
			Delegate delegate = new Delegate();
			JobMapping jobmapping = new JobMapping();
			logger.log(Level.INFO, "enter Job Role/Designation");
			scanner.nextLine();
			String jobRole = scanner.nextLine();
			jobmapping.setJobRole(jobRole);
			jobId = delegate.addNewJob(jobmapping);
			if (jobId != 0) {
				logger.log(Level.INFO, "New Job designation added !");
			}
			return jobId;
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING NEW JOB!" + e.getMessage());
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
			Delegate delegate = new Delegate();
			JobRequest jobrequest = new JobRequest();
			jobId = addingJobDetails();
			logger.log(Level.INFO, "Press enter to continue");
			scanner.nextLine();
			logger.log(Level.INFO, "enter location of job");
			String location = scanner.nextLine();
			logger.log(Level.INFO, "enter salary (lpa)");
			float salary = scanner.nextFloat();
			jobrequest.setEmail(user.getEmail());
			jobrequest.setJobId(jobId);
			jobrequest.setLocation(location);
			jobrequest.setSalary(salary);
			flag1 = delegate.requestNewVacancy(jobrequest);
			if (flag1 == 1) {
				logger.log(Level.INFO, "Congrats your request is saved !");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN PROCESSING VACANCY REQUEST!" + e.getMessage());
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
			ArrayList<Company> comp = new ArrayList<Company>();
			Delegate delegate = new Delegate();
			Company company = new Company();

			userId = delegate.fetchUserId(user);
			user.setUserId(userId);

			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = delegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			scanner.nextLine();
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
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
				logger.log(Level.INFO, "Rate the company on a scannerale of 5");
				int rating = scanner.nextInt();
				company.setCompanyId(companyId);
				company.setReview(review);
				company.setRating(rating);
				flag1 = delegate.reviewAndRateCompany(user, company);
				if (flag1 == 1) {
					logger.log(Level.INFO, "Congrats your comments are added!");
				}
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING YOUR COMMENTS!" + e.getMessage());
		}
	}

	/*
	 * method for adding interview process for a company.
	 */
	public void writeInterviewProcess(User user) {
		try {
			int userId = 0;
			int companyId = 0;
			int jobId = 0;
			int flag = 0, flag1 = 0;
			char c = '\0';
			ArrayList<Company> comp = new ArrayList<Company>();
			Delegate delegate = new Delegate();
			Company company = new Company();
			JobMapping jobmapping = new JobMapping();
			RoleSeparated roleseparated = new RoleSeparated();
			userId = delegate.fetchUserId(user);
			user.setUserId(userId);

			System.out.println(userId);
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp = delegate.displayCompanies(company);
			for (Company i : comp) {
				logger.log(Level.INFO, "\n" + i.getCompanyName());
			}
			scanner.nextLine();
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = scanner.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
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
				jobId = addingJobDetails();
				scanner.nextLine();
				logger.log(Level.INFO, "Share your interview Experience");
				String interviewProcess = scanner.nextLine();
				company.setCompanyId(companyId);
				jobmapping.setJobId(jobId);
				company.setInterviewProcess(interviewProcess);
				flag1 = delegate.interviewProcess(user, company, jobmapping);
				if (flag1 == 1) {
					logger.log(Level.INFO, "Congrats your comments are added!");
				}
			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.userFlow(user);
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING COMMENTS!" + e.getMessage());
		}
	}

	public void UpdateVacancy() {
		// TODO Auto-generated method stub

	}

	public void updateUserAccount(User user) {
		// TODO Auto-generated method stub
		try {

			Delegate delegate = new Delegate();
			int userId = delegate.fetchUserId(user);
			user.setUserId(userId);
			boolean loop=true;
			boolean flag=false;
			do{
				logger.log(Level.INFO, "Select a field you want to update \n1.USERNAME \n2.COMPANYNAME \n3.DESIGNATION \n4.TECHNOLOGY \n5.EXIT");
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch(choice){
				case 1:
					logger.log(Level.INFO, "Enter new user name");
					String userName = scanner.nextLine();
					user.setUserName(userName);
					flag=delegate.updateUserName(user);
					if(flag==true){
						logger.log(Level.INFO, "user name updated!");
					}else{
						logger.log(Level.INFO, "Error updating username!");
					}
					flag=false;
					break;
				case 2:
					logger.log(Level.INFO, "Enter new company name");
					String companyName = scanner.nextLine();
					user.setCompany(companyName);
					flag=delegate.updateCompanyName(user);
					if(flag==true){
						logger.log(Level.INFO, "company name updated!");
					}else{
						logger.log(Level.INFO, "Error updating company name!");
					}
					flag=false;
					break;
				case 3:
					logger.log(Level.INFO, "Enter new designation");
					String designation = scanner.nextLine();
					user.setDesignation(designation);
					flag=delegate.updateUserDesignation(user);
					if(flag==true){
						logger.log(Level.INFO, "Designation updated!");
					}else{
						logger.log(Level.INFO, "Error updating designation!");
					}
					flag=false;
					break;
				case 4:
				{
					boolean status=true;
					Technology technology = new Technology();
					UserTechnologyMapping userTechnologyMapping = new UserTechnologyMapping();
					ArrayList<Technology> tech = new ArrayList<Technology>();
					ArrayList<UserTechnologyMapping> userTechnology = new ArrayList<UserTechnologyMapping>();
					userTechnology = delegate.displayUserTechnologies(userTechnologyMapping,user);
					do{
					logger.log(Level.INFO, "\n Enter your choice: \n 1.CHANGE EXISTING TECHNOLOGY \n 2.ADD NEW TECHNOLOGY \n 3.EXIT");
					int ch = scanner.nextInt();
					switch(ch){
					case 1:
						logger.log(Level.INFO, "\n ID's OF TECHNOLOGIES SAVED:");
						for (UserTechnologyMapping i : userTechnology) {
							logger.log(Level.INFO, "\n" + i.getTechnologyId());
						}
						logger.log(Level.INFO, "\n TECHNOLOGY ID \t TECHNOLOGY");
						tech = delegate.displayTechnologies(technology);
						for (Technology i : tech) {
							logger.log(Level.INFO, "\n" + i.getTechnologyId() + "\t" + i.getTechnology());
						}

						logger.log(Level.INFO, "\n Enter how many technology you want to update:");
						int no = scanner.nextInt();
						for(int i=0;i < no;i++){
							logger.log(Level.INFO, "\n Enter the id of technology you want to change");
							int oldTechnologyId = scanner.nextInt();
							logger.log(Level.INFO, "\n Enter the id of technology you want to add");
							int newTechnologyId = scanner.nextInt();
							userTechnologyMapping.setOldTechnologyId(oldTechnologyId);
							userTechnologyMapping.setTechnologyId(newTechnologyId);
							flag=delegate.updateUserTechnology(userTechnologyMapping,user);
						}
						break;
					case 2:
						addTechnologyDetails(user);
						break;
					case 3:
						status=false;
						break;
					default:
						logger.log(Level.INFO, "Enter a valid choice");
						break;
					}
					}while(status==true);
					break;
				}
				case 5:
					loop=false;
					break;
				default:
					logger.log(Level.INFO, "Enter a valid choice");
					break;
						
				}
			}while(loop==true);
			
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN UPDATING PROFILE!" + e.getMessage());
		}
	}

}
