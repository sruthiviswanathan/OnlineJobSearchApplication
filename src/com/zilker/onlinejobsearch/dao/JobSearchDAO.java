package com.zilker.onlinejobsearch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.zilker.onlinejobsearch.beans.Company;
import com.zilker.onlinejobsearch.beans.JobMapping;
import com.zilker.onlinejobsearch.beans.JobRequest;
import com.zilker.onlinejobsearch.beans.Technology;
import com.zilker.onlinejobsearch.beans.User;
import com.zilker.onlinejobsearch.beans.UserTechnologyMapping;
import com.zilker.onlinejobsearch.constants.QueryConstants;
import com.zilker.onlinejobsearch.utils.DButils;
import com.zilker.onlinejobsearch.utils.NotifyUser;

public class JobSearchDAO {

	private Connection connection = null;
	private PreparedStatement preparestatement, preparestatement1, preparestatement2 = null;
	private ResultSet resultset, resultset1, resultset2 = null;
	private Statement statement = null;

	/*
	 * method for registering new user.
	 */
	public int register(User user) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTUSER);
			preparestatement.setString(1, user.getUserName());
			preparestatement.setString(2, user.getEmail());
			preparestatement.setString(3, user.getPassword());
			preparestatement.setString(4, user.getCompany());
			preparestatement.setString(5, user.getDesignation());
			preparestatement.executeUpdate();
			flag = 1;
		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}
	/*
	 * method for adding technology details of a user.
	 */
	public int addTechnologyDetails(UserTechnologyMapping usertechnology) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTTECHNOLOGY);
			preparestatement.setInt(1, usertechnology.getUserId());
			preparestatement.setInt(2, usertechnology.getTechnologyId());
			preparestatement.executeUpdate();
			flag = 1;
		} catch (SQLException e) {
			flag = 0;
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	/*
	 * method for logging in.
	 */
	public int login(User user) throws SQLException {
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVEUSERDATA);
			String email = user.getEmail();
			String password = user.getPassword();
			int roleId = 0;
			while (resultset.next()) {
				if ((email.equals(resultset.getString(1))) && (password.equals(resultset.getString(2)))) {

					roleId = resultset.getInt(3);
					break;
				}
			}
			return roleId;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}

	/*
	 * method for sending notification if a vacancy is published.
	 */
	public void compareVacancyWithRequest(Company company) throws SQLException {
		NotifyUser notifyuser = new NotifyUser();
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVEJOBREQUESTS);
			int jobId = company.getJobId();
			String location = company.getLocation();
			while (resultset.next()) {
				if ((jobId == resultset.getInt(2)) && location.equals(resultset.getString(3))) {
					String email = resultset.getString(1);
					notifyuser.sendNotification(email);
				}
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}

	}

	/*
	 * method for publishing new vacancy.
	 */
	public int publishVacancy(Company company) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTVACANCY);
			preparestatement.setInt(1, company.getCompanyId());
			preparestatement.setInt(2, company.getJobId());
			preparestatement.setString(3, company.getLocation());
			preparestatement.setString(4, company.getJobDescription());
			preparestatement.setFloat(5, company.getSalary());
			preparestatement.setInt(6, company.getVacancyCount());
			preparestatement.executeUpdate();
			flag = 1;

		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	/*
	 * method for adding new company to the site.
	 */
	public int addNewCompany(Company company) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTCOMPANY);
			preparestatement.setString(1, company.getCompanyName());
			preparestatement.setString(2, company.getCompanyWebsiteUrl());
			preparestatement.executeUpdate();
			flag = 1;
		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	/*
	 * method for fetching company id giving company name as input.
	 */
	public int fetchCompanyId(Company company) throws SQLException {
		try {
			connection = DButils.getConnection();
			statement =connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVECOMPANYDATA);
			int companyId = 0;
			String companyName = company.getCompanyName();
			while (resultset.next()) {
				if (companyName.equalsIgnoreCase(resultset.getString(2))) {
					companyId = resultset.getInt(1);
					break;
				}

			}

			return companyId;
		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}

	/*
	 * method for adding new company to the site.
	 */
	public ArrayList<JobMapping> displayJobs(JobMapping jobmapping) throws SQLException {
		ArrayList<JobMapping> job = new ArrayList<JobMapping>();
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVEJOBDATA);
			while (resultset.next()) {
				JobMapping jm = new JobMapping();
				jm.setJobId(resultset.getInt(1));
				jm.setJobRole(resultset.getString(2));
				job.add(jm);
			}

		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}

		return job;
	}

	/*
	 * method for displaying company to the site.
	 */
	public ArrayList<Technology> displayTechnologies(Technology technology) throws SQLException {
		ArrayList<Technology> tech = new ArrayList<Technology>();
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVETECHNOLOGYDATA);
			while (resultset.next()) {
				Technology t = new Technology();
				t.setTechnologyId(resultset.getInt(1));
				t.setTechnology(resultset.getString(2));
				tech.add(t);
			}

		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return tech;
	}
	/*
	 * method for displaying reviews of a particular company.
	 */
	public ArrayList<Company> retrieveReview(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			connection = DButils.getConnection();
			preparestatement= connection.prepareStatement(QueryConstants.RETRIEVEREVIEW);
			preparestatement.setInt(1, company.getCompanyId());
			resultset = preparestatement.executeQuery();	
			while (resultset.next()) {
				Company c = new Company();
				c.setReview(resultset.getString(1));
				comp.add(c);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return comp;
	}
	/*
	 * method for displaying interview process of a job in a company.
	 */
	public ArrayList<Company> retrieveInterviewProcess(Company company)  throws SQLException{
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			connection = DButils.getConnection();
			preparestatement= connection.prepareStatement(QueryConstants.RETRIEVEINTERVIEWPROCESS);
			preparestatement.setInt(1, company.getJobId());
			resultset = preparestatement.executeQuery();	
			while (resultset.next()) {
				Company c = new Company();
				preparestatement= connection.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
				preparestatement.setInt(1,resultset.getInt(1));
				resultset1=preparestatement.executeQuery();
				while(resultset1.next()){
					c.setCompanyName(resultset1.getString(1));
				}
				c.setInterviewProcess(resultset.getString(2));
				comp.add(c);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return comp;
	}

	/*
	 * method for displaying companies.
	 */
	public ArrayList<Company> displayCompanies(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVECOMPANYDATA);
			
			while (resultset.next()) {
				Company c = new Company();
				c.setCompanyName(resultset.getString(2));
				comp.add(c);
			}

		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return comp;
	}

	/*
	 * method for adding new jobs.
	 */
	public int addNewJob(JobMapping jobmapping) throws SQLException {
		try {
			int jobId = 0;
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTJOB);
			preparestatement.setString(1, jobmapping.getJobRole());
			preparestatement.executeUpdate();
			jobId = fetchJobId(jobmapping);
			return jobId;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}

	}

	/*
	 * method for fetching user id given user mail.
	 */
	public int fetchUserId(User user) throws SQLException {
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVEUSERDATA);
			int userId = 0;
			String email = user.getEmail();
			while (resultset.next()) {
				if (email.equals(resultset.getString(1))) {
					userId = resultset.getInt(4);
					break;
				}
			}
			return userId;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}

	}

	/*
	 * method for fetching job id given job designation.
	 */
	public int fetchJobId(JobMapping jobmapping) throws SQLException {
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVEJOBDATA);
			int jobId = 0;
			String jobRole = jobmapping.getJobRole();
			while (resultset.next()) {
				if (jobRole.equals(resultset.getString(2))) {
					jobId = resultset.getInt(1);
					break;
				}
			}
			return jobId;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}

	/*
	 * method for fetching job id given job designation.
	 */
	public int removeVacancy(Company company) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();

			preparestatement = connection.prepareStatement(QueryConstants.DELETEVACANCY);
			preparestatement.setInt(1, company.getCompanyId());
			preparestatement.setInt(2, company.getJobId());
			preparestatement.executeUpdate();
			flag = 1;

		} catch (SQLException e) {

			flag = 0;
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	/*
	 * method for deleting a company.
	 */
	public int deleteCompany(Company company) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();

			preparestatement = connection.prepareStatement(QueryConstants.DELETECOMPANY);
			preparestatement.setInt(1, company.getCompanyId());
			preparestatement.executeUpdate();
			flag = 1;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	/*
	 * method for deleting an user account.
	 */
	public int deleteUserAccount(User user) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();

			preparestatement = connection.prepareStatement(QueryConstants.DELETEUSER);
			preparestatement.setInt(1, user.getUserId());
			preparestatement.executeUpdate();
			preparestatement = connection.prepareStatement(QueryConstants.DELETEJOBREQUEST);
			preparestatement.setString(1,user.getEmail());
			preparestatement.executeUpdate();
			flag = 1;

		} catch (SQLException e) {

			flag = 0;
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	/*
	 * method 1 for retrieving vacancy based on company.
	 */
	public ArrayList<Company> retrieveVacancyByCompany(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
			preparestatement.setInt(1, company.getCompanyId());
			resultset = preparestatement.executeQuery();
			while (resultset.next()) {

				Company c = new Company();
				c.setCompanyName(resultset.getString(1));
				c.setCompanyWebsiteUrl(resultset.getString(3));
				c.setAverageRating(resultset.getInt(2));
				comp.add(c);

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return comp;
	}/*
	 * method 2 for retrieving vacancy based on company.
	 */
	public ArrayList<Company> retrieveVacancyByCompany1(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			
			connection = DButils.getConnection();
			int jobId = 0;
			preparestatement1 = connection.prepareStatement(QueryConstants.RETRIEVEVACANCYBYCOMPID);
			preparestatement1.setInt(1, company.getCompanyId());
			resultset1 = preparestatement1.executeQuery();
			while (resultset1.next()) {
				Company c = new Company();
				c.setJobDescription(resultset1.getString(4));
				c.setLocation(resultset1.getString(3));
				c.setSalary(resultset1.getFloat(5));

				String jobRole = resultset1.getString(2);
				jobId = Integer.parseInt(jobRole);
				preparestatement2 = connection.prepareStatement(QueryConstants.RETRIEVEJOBDESIGNATION);
				preparestatement2.setInt(1, jobId);
				resultset2 = preparestatement2.executeQuery();
				while (resultset2.next()) {
					c.setJobRole(resultset2.getString(1));
					comp.add(c);
					
				}

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement1, resultset1);
			DButils.closeConnection(connection, preparestatement2, resultset2);
		}
		return comp;
	}

	/*
	 * method 1 for retrieving vacancy based on job.
	 */
	public ArrayList<Company> retrieveVacancyByJob(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVEJOBDESIGNATION);
			preparestatement.setInt(1, company.getJobId());
			resultset = preparestatement.executeQuery();
			while (resultset.next()) {
				Company c = new Company();
				c.setJobRole(resultset.getString(1));
				comp.add(c);
			}

		} catch (SQLException e) {
			throw e;
			
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return comp;
	}
	/*
	 * method 2 for retrieving vacancy based on job.
	 */
	public ArrayList<Company> retrieveVacancyByJob1(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			
			connection = DButils.getConnection();
			int companyId = 0;
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVEVACANCYBYJOBID);
			preparestatement.setInt(1, company.getJobId());
			resultset = preparestatement.executeQuery();
			while (resultset.next()) {
				
				Company c = new Company();
				c.setLocation(resultset.getString(3));
				c.setJobDescription(resultset.getString(4));
				c.setSalary(resultset.getFloat(5));

				String companyid = resultset.getString(1);
				companyId = Integer.parseInt(companyid);
				preparestatement1 = connection.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
				preparestatement1.setInt(1, companyId);
				resultset1 = preparestatement1.executeQuery();
				while (resultset1.next()) {

					c.setCompanyName(resultset1.getString(1));
					c.setCompanyWebsiteUrl(resultset1.getString(3));
					c.setAverageRating(resultset1.getInt(2));
					comp.add(c);
				}

			}
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
			DButils.closeConnection(connection, preparestatement1, resultset1);
		}
		return comp;
	}

	/*
	 * method for requesting vacancy.
	 */
	public int requestNewVacancy(JobRequest jobrequest) throws SQLException {
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTJOBREQUEST);
			preparestatement.setString(1, jobrequest.getEmail());
			preparestatement.setInt(2, jobrequest.getJobId());
			preparestatement.setString(3, jobrequest.getLocation());
			preparestatement.setFloat(4, jobrequest.getSalary());
			preparestatement.executeUpdate();
			return 1;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}

	/*
	 * method for adding interview process of a company
	 */
	public int interviewProcess(User user, Company company, JobMapping jobmapping) throws SQLException {
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTJOBREVIEW);
			preparestatement.setInt(1, user.getUserId());
			preparestatement.setInt(2, company.getCompanyId());
			preparestatement.setInt(3, jobmapping.getJobId());
			preparestatement.setString(4, company.getInterviewProcess());
			preparestatement.executeUpdate();
			return 1;
		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}

	/*
	 * method for adding review and rating of a company
	 */
	public int reviewAndRateCompany(User user, Company company) throws SQLException {
		try {
			int existingRating = 0;
			int newRating = 0;
			int flag = 0;
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTREVIEWRATING);
			preparestatement.setInt(1, user.getUserId());
			preparestatement.setInt(2, company.getCompanyId());
			preparestatement.setString(3, company.getReview());
			preparestatement.setInt(4, company.getRating());
			preparestatement.executeUpdate();
			newRating = company.getRating();
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
			preparestatement.setInt(1, company.getCompanyId());
			resultset = preparestatement.executeQuery();
			while (resultset.next()) {	
					existingRating = resultset.getInt(2);
					flag = 1;
			}
			if (flag == 1) {
				existingRating = (existingRating + newRating) / 2;
			}
			preparestatement = connection.prepareStatement(QueryConstants.UPDATECOMPANYRATING);
			preparestatement.setInt(1, existingRating);
			preparestatement.setInt(2, company.getCompanyId());
			preparestatement.executeUpdate();
			return 1;
		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}
	public boolean ifAlreadyExists(User user)throws SQLException {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVEUSERDATA);
			String email = user.getEmail();
			while (resultset.next()) {
				if (email.equals(resultset.getString(1))) 
						{
					System.out.println(email);
						flag=true;
					break;
				}
			}
			return flag;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		
	}
	public int registerAsAdmin(User user) throws SQLException{
		// TODO Auto-generated method stub
		int flag = 0;
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTADMIN);
			preparestatement.setString(1, user.getUserName());
			preparestatement.setString(2, user.getEmail());
			preparestatement.setString(3, user.getPassword());
			preparestatement.setString(4, user.getCompany());
			preparestatement.setString(5, user.getDesignation());
			preparestatement.setInt(6, 2);
			preparestatement.executeUpdate();
			flag = 1;
			
		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}
	
	public int insertIntoAdmin(User user, Company company) throws SQLException{
		// TODO Auto-generated method stub
		int flag = 0,userId=0,companyId=0;
		try {
			connection = DButils.getConnection();
			userId=user.getUserId();
			companyId=company.getCompanyId();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTCOMPANYADMIN);
			preparestatement.setInt(1,userId);
			preparestatement.setInt(2,companyId);
			preparestatement.executeUpdate();
			flag = 1;
			
		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}
	public int fetchCompanyIdByAdmin(User user) throws SQLException{
		// TODO Auto-generated method stub
		try {
			connection = DButils.getConnection();
			statement = connection.createStatement();
			resultset = statement.executeQuery(QueryConstants.RETRIEVECOMPANYID);
			int companyId = 0;
			int userId = user.getUserId();
			while (resultset.next()) {
				if (userId == resultset.getInt(1))
				{
					companyId = resultset.getInt(2);
					break;
				}

			}

			return companyId;
		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}
	public boolean updateUserName(User user)throws SQLException {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			connection = DButils.getConnection();
			int userId=user.getUserId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEUSERNAME);
			preparestatement.setString(1,user.getUserName());
			preparestatement.setInt(2,userId);
			preparestatement.executeUpdate();
			flag = true;
			
		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}
	public boolean updateCompanyName(User user)throws SQLException {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			connection = DButils.getConnection();
			int userId=user.getUserId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEUSERCOMPANY);
			preparestatement.setString(1,user.getCompany());
			preparestatement.setInt(2,userId);
			preparestatement.executeUpdate();
			flag = true;
			
		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}
	public boolean updateUserDesignation(User user)throws SQLException {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			connection = DButils.getConnection();
			int userId=user.getUserId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEUSERDESIGNATION);
			preparestatement.setString(1,user.getDesignation());
			preparestatement.setInt(2,userId);
			preparestatement.executeUpdate();
			flag = true;
			
		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}
	public ArrayList<UserTechnologyMapping> displayUserTechnologies(UserTechnologyMapping userTechnologyMapping,User user)throws SQLException {
		// TODO Auto-generated method stub
		
		ArrayList<UserTechnologyMapping> userTechnology = new ArrayList<UserTechnologyMapping>();
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVEUSERTECHNOLOGY);
			preparestatement.setInt(1, user.getUserId());
			resultset = preparestatement.executeQuery();

			while (resultset.next()) {
				UserTechnologyMapping t = new UserTechnologyMapping();
				t.setTechnologyId(resultset.getInt(1));
				userTechnology.add(t);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return userTechnology;
	}
	
	public boolean updateUserTechnology(UserTechnologyMapping userTechnologyMapping, User user)throws SQLException  {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			connection = DButils.getConnection();
			int userId=user.getUserId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEUSERTECHNOLOGY);
			preparestatement.setInt(1,userTechnologyMapping.getTechnologyId());
			preparestatement.setInt(2,userId);
			preparestatement.setInt(3,userTechnologyMapping.getOldTechnologyId());
			preparestatement.executeUpdate();
			flag = true;
			
		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}
	
	
	

}
