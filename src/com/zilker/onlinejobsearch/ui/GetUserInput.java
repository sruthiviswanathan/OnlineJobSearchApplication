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
	public Scanner sc = new Scanner(System.in);
	Validation valid = new Validation();
	/*
	 * method for getting user input for registering.
	 */
	public void register() {
		
		try {
			int flag=0,flag1=0;
			ArrayList<Technology> tech = new ArrayList<Technology>();
			User user = new User();
			Delegate delegate = new Delegate();
			Technology technology = new Technology();
			UserTechnologyMapping usertechnology = new UserTechnologyMapping();
			sc.nextLine();
			logger.log(Level.INFO, "enter UserName");
			String userName = sc.nextLine();
			String email = valid.emailValidation();
			String password = valid.passwordValidation();
			//sc.nextLine();
			logger.log(Level.INFO, "enter Company/college");
			String company = sc.nextLine();
			logger.log(Level.INFO, "enter designation/current status");
			String designation = sc.nextLine();
			user.setUserName(userName);
			user.setEmail(email);
			user.setPassword(password);
			user.setCompany(company);
			user.setDesignation(designation);
			flag=delegate.register(user);
			if(flag==1){
				logger.log(Level.INFO, "Congrats you are Registered !");
			}
			int userId = delegate.fetchUserId(user);
			if(userId!=0){
				logger.log(Level.INFO, "User details fetched !");
			}
			logger.log(Level.INFO, "Do you want to add Technologies known to your profile?(If yes press y else n)");
			char choice = sc.next().charAt(0);
			if (choice == 'Y' || choice == 'y') {
				tech = delegate.displayTechnologies(technology);
				 for(Technology i : tech) {
			            logger.log(Level.INFO,"\n"+i.getTechnologyId()+"\t" +i.getTechnology());
			        }
				logger.log(Level.INFO, "enter how many technologies you want to add to your profile?");
				int numbers = sc.nextInt();
				for (int i = 0; i < numbers; i++) {
					logger.log(Level.INFO, "enter technology ID");
					int technologyId = sc.nextInt();
					usertechnology.setUserId(userId);
					usertechnology.setTechnologyId(technologyId);
					flag1=delegate.addTechnologyDetails(usertechnology);
					if(flag1==1){
						logger.log(Level.INFO, "Technology added to your profile !");
					}
				}

			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN REGISTERING!"+ e.getMessage());
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
			String email = sc.next();
			logger.log(Level.INFO, "enter Password");
			String password = sc.next();
			user.setEmail(email);
			user.setPassword(password);
			i = delegate.login(user);
			if(i == 0){
				logger.log(Level.INFO, "Invalid login credentials!");
			}else if (i == 1) {
				logger.log(Level.INFO, "Congrats you are logged in !");
				roleseparated.userFlow();
			} else if (i == 2) {
				logger.log(Level.INFO, "Congrats you are logged in !");
				roleseparated.adminFlow();
			} else if (i == 3) {
				logger.log(Level.INFO, "Congrats you are logged in !");
				roleseparated.siteAdminFlow();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN LOGGING IN!"+ e.getMessage());
		}
	}
	/*
	 * method for deleting user account.
	 */
	public void deleteUserAccount() {
		try {
			int flag=0;
			int userId = 0;
			char c = '\0';
			User user = new User();
			Delegate delegate = new Delegate();
			logger.log(Level.INFO, " Reenter Email");
			String email = sc.next();
			logger.log(Level.INFO, " Reenter Password");
			String password = sc.next();
			user.setEmail(email);
			user.setPassword(password);
			userId = delegate.fetchUserId(user);
			if(userId!=0){
				logger.log(Level.INFO, "User details fetched !");
			}
			if (userId == 0) {
				logger.log(Level.INFO, "Invalid account crdentials!!Do you still want to delete?(y/n)");
				c = sc.next().charAt(0);
				if (c == 'y' || c == 'Y') {
					deleteUserAccount();
				}
			} else {
				user.setUserId(userId);
				flag=delegate.deleteUserAccount(user);
				if(flag==1){
					logger.log(Level.INFO, "User deleted !");
				}
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN DELETING ACCOUNT!"+ e.getMessage());
		}
	}
	/*
	 * method for getting user input for searching based on company.
	 */
	public void searchCompany() {
		try {
			ArrayList<Company> comp = new ArrayList<Company>();
			ArrayList<Company> comp1 = new ArrayList<Company>();
			ArrayList<Company> comp2 = new ArrayList<Company>();
			Company company = new Company();
			Delegate delegate = new Delegate();
			int companyId = 0;
			int flag = 0;	
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp= delegate.displayCompanies(company);
			for(Company i : comp) {
	            logger.log(Level.INFO,"\n"+i.getCompanyName());
	        }
			
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = sc.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO, "Company has is not registered with us as of now");
				} else {
					flag = 1;
				}
			} while (flag != 1);
			company.setCompanyId(companyId);
			comp1=delegate.retrieveVacancyByCompany(company);
			if(comp1.isEmpty()){
				logger.log(Level.INFO, "No vacancy in this Company!!");
			}
			for(Company j : comp1) {
	            logger.log(Level.INFO,"\nCOMPANY NAME:"+j.getCompanyName() +"\tWEBSITE URL:"+j.getCompanyWebsiteUrl()+"\tAVERAGE RATING:"+j.getAverageRating());
	            break;
	        }
			comp2=delegate.retrieveVacancyByCompany1(company);
			if(comp2.isEmpty()){
				logger.log(Level.INFO, "No vacancy in this Company!!");
			}
			for(Company i : comp2) {
	            logger.log(Level.INFO,"\nJOB DESIGNATION:"+i.getJobRole()+"\tJOB DESCRIPTION:"+i.getJobDescription()+"\nLOCATION:"+i.getLocation()+"\tSALARY(lpa):"+i.getSalary());
	        }
					
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN RETREIVING COMPANY!"+ e.getMessage());
		}
	}
	/*
	 * method for getting user input for searching based on jobs.
	 */
	public void searchJobs() {
		try {
			ArrayList<JobMapping> job = new ArrayList<JobMapping>();
			ArrayList<Company> comp = new ArrayList<Company>();
			ArrayList<Company> comp1 = new ArrayList<Company>();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			Company company = new Company();
			JobMapping jobmapping = new JobMapping();
			int jobId = 0;
			int flag = 0;
			char c;
			job= delegate.displayJobs(jobmapping);
			 for(JobMapping i : job) {
		            logger.log(Level.INFO,"\n"+i.getJobId()+"\t" +i.getJobRole());
		        }
			do {
				logger.log(Level.INFO, "enter Job designation");
				String jobDesignation = sc.nextLine();
				jobmapping.setJobRole(jobDesignation);
				jobId = delegate.fetchJobId(jobmapping);
				if (jobId == 0) {
					logger.log(Level.INFO,
							"No vacancies in this designation as of now!!Do you still want to search for other job designation?(y/n)");
					c = sc.next().charAt(0);
					sc.nextLine();
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
				if(comp.isEmpty()){
					logger.log(Level.SEVERE, "No vacancy in this designation!!");
				}
				for(Company i : comp) {
		            logger.log(Level.INFO,"\nJOB DESIGNATION:"+i.getJobRole());
		        }
				comp1 = delegate.retrieveVacancyByJob1(company);
				if(comp1.isEmpty()){
					logger.log(Level.SEVERE, "No vacancy in this designation!!");
				}
				for(Company i : comp1) {
					logger.log(Level.INFO,"\nCOMPANY NAME:"+i.getCompanyName() +"\tWEBSITE URL:"+i.getCompanyWebsiteUrl()+"\tAVERAGE RATING:"+i.getAverageRating());
		            logger.log(Level.INFO,"\nJOB DESCRIPTION:"+i.getJobDescription()+"\tLOCATION:"+i.getLocation()+"\tSALARY:"+i.getSalary());
		        }
				
			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.userFlow();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN RETRIEVING JOBS!"+ e.getMessage());
		}
	}
	/*
	 * method for adding new company.
	 */
	public void addNewCompany() {
		int flag=0;
		try {
			Delegate delegate = new Delegate();
			Company company = new Company();
			logger.log(Level.INFO, "enter Company name");
			String companyName = sc.nextLine();
			logger.log(Level.INFO, "enter Company website url");
			String companyWebsiteUrl = sc.nextLine();
			company.setCompanyName(companyName);
			company.setCompanyWebsiteUrl(companyWebsiteUrl);
			flag=delegate.addNewCompany(company);
			if(flag==1){
				logger.log(Level.INFO, "Your company details are added !");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING NEW COMPANY!"+ e.getMessage());
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
			comp= delegate.displayCompanies(company);
			for(Company i : comp) {
	            logger.log(Level.INFO,"\n"+i.getCompanyName());
	        }	
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = sc.nextLine();
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
			logger.log(Level.INFO, "ERROR IN ADDING COMPANY DETAILS!"+ e.getMessage());
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
			job= delegate.displayJobs(jobmapping);
			 for(JobMapping i : job) {
		            logger.log(Level.INFO,"\n"+i.getJobId()+"\t" +i.getJobRole());
		        }
			logger.log(Level.INFO, "enter Job ID");
			logger.log(Level.INFO, "Do you want to add a new Job Description not listed above(y/n)?");
			char ch = sc.next().charAt(0);
			if (ch == 'y' || ch == 'Y') {
				jobId = addNewJob();
			} else {
				logger.log(Level.INFO, "enter any one of the listed Job IDs");
				jobId = sc.nextInt();
			}
			return jobId;
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING JOB DETAILS!"+ e.getMessage());
			return 0;
		}
	}
	/*
	 * method for publishing new vacancy.
	 */
	public void publishVacancy() {
		try {
			int flag=0;
			Company company = new Company();
			Delegate delegate = new Delegate();
			int companyId = 0;
			int jobId = 0;
			companyId = addingCompanyDetails();
			jobId = addingJobDetails();
			logger.log(Level.INFO, "Press enter to continue");
			sc.nextLine();
			logger.log(Level.INFO, "enter location of job");
			String location = sc.nextLine();
			logger.log(Level.INFO, "enter job Description");
			String jobDescription = sc.nextLine();
			logger.log(Level.INFO, "enter salary (lpa)");
			float salary = sc.nextFloat();
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			company.setLocation(location);
			company.setJobDescription(jobDescription);
			company.setSalary(salary);
			flag=delegate.publishVacancy(company);
			if(flag==1){
				logger.log(Level.INFO, "Your company vacancy is published !");
			}
			delegate.compareVacancyWithRequest(company);
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN PUBLISHING VACANCY!"+ e.getMessage());
		}
	}
	/*
	 * method for deleting existing vacancy.
	 */
	public void removeVacancy() {
		try {
			int flag = 0,flag1=0;
			int companyId = 0;
			Company company = new Company();
			Delegate delegate = new Delegate();
			JobMapping jobmapping = new JobMapping();
			ArrayList<Company> comp = new ArrayList<Company>();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp=delegate.displayCompanies(company);	
			for(Company i : comp) {
	            logger.log(Level.INFO,"\n"+i.getCompanyName());
	        }	
			do {
				logger.log(Level.INFO, "enter Company name you would like to remove");
				String companyName = sc.nextLine();
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
			int jobId = sc.nextInt();
			company.setCompanyId(companyId);
			company.setJobId(jobId);
			flag1=delegate.removeVacancy(company);
			if(flag1==1){
				logger.log(Level.INFO, "Vacancy deleted !");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN REMOVING VACANCY!"+ e.getMessage());
		}
	}
	/*
	 * method for deleting company.
	 */
	public void deleteCompany() {
		try {
			int companyId = 0;
			int flag = 0,flag1=0;
			char c = '\0';
			ArrayList<Company> comp = new ArrayList<Company>();
			Company company = new Company();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp=delegate.displayCompanies(company);
			for(Company i : comp) {
	            logger.log(Level.INFO,"\n"+i.getCompanyName());
	        }
		
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = sc.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO,
							"Sorry this company is not registered!!Do you want to share your interview experience in any other company?(y/n)");
					c = sc.next().charAt(0);
					sc.nextLine();
					flag = 0;
				} else {
					flag = 1;
					c = 'n';
				}
			} while (c == 'y' || c == 'Y');
			if (flag == 1) {
				company.setCompanyId(companyId);
				flag1=delegate.deleteCompany(company);
				if(flag1==1){
					logger.log(Level.INFO, "Company Details deleted !");
				}
			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.siteAdminFlow();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN DELETING COMPANY!"+ e.getMessage());
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
			sc.nextLine();
			String jobRole = sc.nextLine();
			jobmapping.setJobRole(jobRole);
			jobId = delegate.addNewJob(jobmapping);
			if(jobId !=0 ){
				logger.log(Level.INFO, "New Job designation added !");	
			}
			return jobId;
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING NEW JOB!"+ e.getMessage());
			return 0;
		}
	}
	/*
	 * method for requesting vacancy so that the user is intimated.
	 */
	public void requestVacancy() {
		try {
			int jobId = 0;
			String email = "";
			int flag = 0,flag1=0;
			Delegate delegate = new Delegate();
			JobRequest jobrequest = new JobRequest();
			do {
				email = valid.emailValidation();
				flag = checkIfRegistered(email);
			} while (flag != 1);
			jobId = addingJobDetails();
			logger.log(Level.INFO, "Press enter to continue");
			sc.nextLine();
			logger.log(Level.INFO, "enter location of job");
			String location = sc.nextLine();
			logger.log(Level.INFO, "enter salary (lpa)");
			float salary = sc.nextFloat();
			jobrequest.setEmail(email);
			jobrequest.setJobId(jobId);
			jobrequest.setLocation(location);
			jobrequest.setSalary(salary);
			flag1=delegate.requestNewVacancy(jobrequest);
			if(flag1==1){
				logger.log(Level.INFO, "Congrats your request is saved !");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN PROCESSING VACANCY REQUEST!"+ e.getMessage());
		}
	}
	/*
	 * method for reviewing and rating a company.
	 */
	public void reviewAndRateCompany() {
		try {
			String email = "";
			int count = 0;
			int userId = 0;
			int companyId = 0;
			int flag = 0,flag1=0;
			char c = '\0';
			ArrayList<Company> comp = new ArrayList<Company>();
			User user = new User();
			Delegate delegate = new Delegate();
			Company company = new Company();
			do {
				email = valid.emailValidation();
				count = checkIfRegisteredReview(email);
			} while (count != 1);
			user.setEmail(email);
			userId = delegate.fetchUserId(user);
			if(userId!=0){
				logger.log(Level.INFO, "User details fetched !");
			}
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp=delegate.displayCompanies(company);
			for(Company i : comp) {
	            logger.log(Level.INFO,"\n"+i.getCompanyName());
	        }		
			sc.nextLine();
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = sc.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO,
							"Sorry this company is not registered!!Do you want to share your interview experience in any other company?(y/n)");
					c = sc.next().charAt(0);
					sc.nextLine();
					flag = 0;
				} else {
					flag = 1;
					c = 'n';
				}
			} while (c == 'y' || c == 'Y');
			if (flag == 1) {
				logger.log(Level.INFO, "Write a review about the company");
				String review = sc.nextLine();
				logger.log(Level.INFO, "Rate the company on a scale of 5");
				int rating = sc.nextInt();
				user.setUserId(userId);
				company.setCompanyId(companyId);
				company.setReview(review);
				company.setRating(rating);
				flag1=delegate.reviewAndRateCompany(user, company);
				if(flag1==1){
					logger.log(Level.INFO, "Congrats your comments are added!");
				}
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING YOUR COMMENTS!"+ e.getMessage());
		}
	}
	/*
	 * method for adding interview process for a company.
	 */
	public void writeInterviewProcess() {
		try {
			int userId = 0;
			int companyId = 0;
			int jobId = 0;
			int flag = 0,flag1=0;
			int count = 0;
			String email = "";
			char c = '\0';
			User user = new User();
			ArrayList<Company> comp = new ArrayList<Company>();
			Delegate delegate = new Delegate();
			Company company = new Company();
			JobMapping jobmapping = new JobMapping();
			RoleSeparated roleseparated = new RoleSeparated();
			do {
				email = valid.emailValidation();
				count = checkIfRegisteredProcess(email);
			} while (count != 1);
			user.setEmail(email);
			userId = delegate.fetchUserId(user);
			if(userId!=0){
				logger.log(Level.INFO, "User details fetched !");
			}
			logger.log(Level.INFO, " \n Companies registered with us :");
			comp=delegate.displayCompanies(company);
			for(Company i : comp) {
	            logger.log(Level.INFO,"\n"+i.getCompanyName());
	        }
			sc.nextLine();
			do {
				logger.log(Level.INFO, "enter Company name");
				String companyName = sc.nextLine();
				company.setCompanyName(companyName);
				companyId = delegate.fetchCompanyId(company);
				if (companyId == 0) {
					logger.log(Level.INFO,
							"Sorry this company is not registered!!Do you want to share your interview experience in any other company?(y/n)");
					c = sc.next().charAt(0);
					sc.nextLine();
					flag = 0;
				} else {
					flag = 1;
					c = 'n';
				}
			} while (c == 'y' || c == 'Y');

			if (flag == 1) {
				jobId = addingJobDetails();
				sc.nextLine();
				logger.log(Level.INFO, "Share your interview Experience");
				String interviewProcess = sc.nextLine();
				user.setUserId(userId);
				company.setCompanyId(companyId);
				jobmapping.setJobId(jobId);
				company.setInterviewProcess(interviewProcess);
				flag1=delegate.interviewProcess(user, company, jobmapping);
				if(flag1==1){
					logger.log(Level.INFO, "Congrats your comments are added!");
				}
			} else {
				logger.log(Level.INFO, "BYE");
				roleseparated.userFlow();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "ERROR IN ADDING COMMENTS!"+ e.getMessage());
		}
	}
/*
 * method for checking if user is registered
 */
	public int checkIfRegisteredProcess(String email) {
		try {
			int flag = 0;
			User user = new User();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			GetUserInput userinput = new GetUserInput();
			user.setEmail(email);
			flag = delegate.checkIfRegistered(user);
			if (flag == 1) {
				return flag;
			} else {
				logger.log(Level.INFO, "You are not registered with us !");
				logger.log(Level.INFO, "\n 1.Retry \n 2.Get Registered First \n 3.BACK!");
				int c = sc.nextInt();
				sc.nextLine();
				switch (c) {
				case 1:
					userinput.writeInterviewProcess();
					;
					flag = 1;
					roleseparated.userFlow();
					break;
				case 2:
					userinput.register();
					userinput.writeInterviewProcess();
					flag = 1;
					roleseparated.userFlow();
					break;
				case 3:
					roleseparated.userFlow();
					break;
				default:
					logger.log(Level.INFO, "enter a valid choice!");
					break;
				}
				return flag;
			}
		} catch (SQLException e) {
			logger.log(Level.INFO,e.getMessage());
			return 0;
		}
	}

	public int checkIfRegistered(String email) {
		try {
			int flag = 0;
			User user = new User();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			GetUserInput userinput = new GetUserInput();
			user.setEmail(email);
			flag = delegate.checkIfRegistered(user);
			if (flag == 1) {
				return flag;
			} else {
				logger.log(Level.INFO, "You are not registered with us !");
				logger.log(Level.INFO, "\n 1.Retry \n 2.Get Registered First \n 3.BACK!");
				int c = sc.nextInt();
				sc.nextLine();
				switch (c) {
				case 1:
					userinput.requestVacancy();
					flag = 1;
					roleseparated.userFlow();
					break;
				case 2:
					userinput.register();
					userinput.requestVacancy();
					flag = 1;
					roleseparated.userFlow();
					break;
				case 3:
					roleseparated.userFlow();
					break;
				default:
					logger.log(Level.INFO, "enter a valid choice!");
					break;
				}
				return flag;
			}
		} catch (SQLException e) {
			logger.log(Level.INFO,e.getMessage());
			return 0;
		}

	}

	public int checkIfRegisteredReview(String email) {
		try {
			int flag = 0;
			User user = new User();
			Delegate delegate = new Delegate();
			RoleSeparated roleseparated = new RoleSeparated();
			GetUserInput userinput = new GetUserInput();
			user.setEmail(email);
			flag = delegate.checkIfRegistered(user);
			if (flag == 1) {
				return flag;
			} else {
				logger.log(Level.INFO, "You are not registered with us !");
				logger.log(Level.INFO, "\n 1.Retry \n 2.Get Registered First \n 3.BACK!");
				int c = sc.nextInt();
				sc.nextLine();
				switch (c) {
				case 1:
					userinput.reviewAndRateCompany();
					flag = 1;
					roleseparated.userFlow();
					break;
				case 2:
					userinput.register();
					userinput.reviewAndRateCompany();
					flag = 1;
					roleseparated.userFlow();
					break;
				case 3:
					roleseparated.userFlow();
					break;
				default:
					logger.log(Level.INFO, "enter a valid choice!");
					break;
				}
				return flag;
			}

		} catch (SQLException e) {
			logger.log(Level.INFO,e.getMessage());
			return 0;
		}

	}
}
