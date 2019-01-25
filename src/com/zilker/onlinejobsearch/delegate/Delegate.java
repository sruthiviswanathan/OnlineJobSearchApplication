package com.zilker.onlinejobsearch.delegate;
import java.sql.SQLException;
import java.util.ArrayList;

import com.zilker.onlinejobsearch.beans.Company;
import com.zilker.onlinejobsearch.beans.JobMapping;
import com.zilker.onlinejobsearch.beans.JobRequest;
import com.zilker.onlinejobsearch.beans.Technology;
import com.zilker.onlinejobsearch.beans.User;
import com.zilker.onlinejobsearch.beans.UserTechnologyMapping;
import com.zilker.onlinejobsearch.dao.JobSearchDAO;
/*
 * all methods redirects the control from UI to DAO
 */
public class Delegate {

	public int register(User user) throws SQLException{
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag = jobsearchdao.register(user);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int fetchUserId(User user)throws SQLException {
		int userId = 0;
		// TODO Auto-generated method stub
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			userId = jobsearchdao.fetchUserId(user);
			return userId;
		} catch (SQLException e) {
			throw e;
		}
	}

	public int addTechnologyDetails(UserTechnologyMapping usertechnology)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.addTechnologyDetails(usertechnology);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int login(User user)throws SQLException {
		// TODO Auto-generated method stub
		int i = 0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			i = jobsearchdao.login(user);		
		} catch (SQLException e) {
			throw e;
		}
		return i;
	}

	public ArrayList<Company> displayCompanies(Company company) throws SQLException  {
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			comp = jobsearchdao.displayCompanies(company);
		} catch (SQLException e) {
			throw e;
		}
		return comp;
	}

	public int fetchCompanyId(Company company)throws SQLException {
		// TODO Auto-generated method stub
		int companyId = 0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			companyId = jobsearchdao.fetchCompanyId(company);
			return companyId;
		} catch (SQLException e) {
			throw e;
		}
	}

	public ArrayList<Company> retrieveVacancyByCompany(Company company) throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			comp = jobsearchdao.retrieveVacancyByCompany(company);
		} catch (SQLException e) {
			throw e;
		}
		return comp;
	}
	public ArrayList<Company> retrieveVacancyByCompany1(Company company) throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			comp = jobsearchdao.retrieveVacancyByCompany1(company);
		} catch (SQLException e) {
			throw e;
		}
		return comp;
	}

	public ArrayList<JobMapping> displayJobs(JobMapping jobmapping) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<JobMapping> job = new ArrayList<JobMapping>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			job = jobsearchdao.displayJobs(jobmapping);
			
		} catch (SQLException e) {
			throw e;
		}
		return job;
	}

	public int fetchJobId(JobMapping jobmapping)throws SQLException {
		// TODO Auto-generated method stub
		int jobId = 0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			jobId = jobsearchdao.fetchJobId(jobmapping);
			return jobId;
		} catch (SQLException e) {
			throw e;
		}
	}

	public ArrayList<Company> retrieveVacancyByJob(Company company)throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			comp = jobsearchdao.retrieveVacancyByJob(company);
		} catch (SQLException e) {
			throw e;
		}
		return comp;
	}
	public ArrayList<Company> retrieveVacancyByJob1(Company company)throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			comp = jobsearchdao.retrieveVacancyByJob1(company);
		} catch (SQLException e) {
			throw e;
		}
		return comp;
	}

	public int addNewCompany(Company company) throws SQLException{
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.addNewCompany(company);
		} catch (SQLException e) {
			throw e;
		}

		return flag;
	}

	public int publishVacancy(Company company)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.publishVacancy(company);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public void compareVacancyWithRequest(Company company)throws SQLException {
		// TODO Auto-generated method stub
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			jobsearchdao.compareVacancyWithRequest(company);
		} catch (SQLException e) {
			throw e;
		}
	}

	public int removeVacancy(Company company)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag =jobsearchdao.removeVacancy(company);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int deleteCompany(Company company)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.deleteCompany(company);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int addNewJob(JobMapping jobmapping)throws SQLException {
		// TODO Auto-generated method stub
		int jobId = 0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			jobId = jobsearchdao.addNewJob(jobmapping);
			return jobId;
		} catch (SQLException e) {
			throw e;
		}

	}

	public int requestNewVacancy(JobRequest jobrequest)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.requestNewVacancy(jobrequest);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int reviewAndRateCompany(User user, Company company)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.reviewAndRateCompany(user, company);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int interviewProcess(User user, Company company, JobMapping jobmapping)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.interviewProcess(user, company, jobmapping);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int deleteUserAccount(User user) throws SQLException{
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag=jobsearchdao.deleteUserAccount(user);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public ArrayList<Technology> displayTechnologies(Technology technology) throws SQLException  {
		// TODO Auto-generated method stub
		ArrayList<Technology> tech = new ArrayList<Technology>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			tech = jobsearchdao.displayTechnologies(technology);
			
		} catch (SQLException e) {
			throw e;
		}
		return tech;
		
	}

	public ArrayList<Company> retrieveReview(Company company) throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			comp = jobsearchdao.retrieveReview(company);
			
		} catch (SQLException e) {
			throw e;
		}
			
		return comp;
	}

	public ArrayList<Company> retrieveInterviewProcess(Company company) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			comp = jobsearchdao.retrieveInterviewProcess(company);
			
		} catch (SQLException e) {
			throw e;
		}
			
		return comp;
	}

	public boolean ifAlreadyExists(User user)  throws SQLException{
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag =jobsearchdao.ifAlreadyExists(user);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int registerAsAdmin(User user) throws SQLException{
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag = jobsearchdao.registerAsAdmin(user);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int insertIntoAdmin(User user, Company company)throws SQLException {
		// TODO Auto-generated method stub
		int flag=0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag = jobsearchdao.insertIntoAdmin(user,company);
		} catch (SQLException e) {
			throw e;
		}
		return flag;
	}

	public int fetchCompanyIdByAdmin(User user)throws SQLException {
		// TODO Auto-generated method stub
		int companyId = 0;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			companyId = jobsearchdao.fetchCompanyIdByAdmin(user);
			return companyId;
		} catch (SQLException e) {
			throw e;
		}
	}

	public boolean updateUserName(User user) throws SQLException  {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag = jobsearchdao.updateUserName(user);
			return flag;
		} catch (SQLException e) {
			throw e;
		}
	}

	public boolean updateCompanyName(User user)throws SQLException  {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag = jobsearchdao.updateCompanyName(user);
			return flag;
		} catch (SQLException e) {
			throw e;
		}
	}

	public boolean updateUserDesignation(User user)throws SQLException  {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag = jobsearchdao.updateUserDesignation(user);
			return flag;
		} catch (SQLException e) {
			throw e;
		}
	}

	public ArrayList<UserTechnologyMapping> displayUserTechnologies(UserTechnologyMapping userTechnologyMapping,User user) throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<UserTechnologyMapping> usertechnology = new ArrayList<UserTechnologyMapping>();
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			usertechnology = jobsearchdao.displayUserTechnologies(userTechnologyMapping,user);
			
		} catch (SQLException e) {
			throw e;
		}
		return usertechnology;
	}

	public boolean updateUserTechnology(UserTechnologyMapping userTechnologyMapping,User user) throws SQLException{
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			JobSearchDAO jobsearchdao = new JobSearchDAO();
			flag = jobsearchdao.updateUserTechnology(userTechnologyMapping,user);
			return flag;
		} catch (SQLException e) {
			throw e;
		}
	}

}
