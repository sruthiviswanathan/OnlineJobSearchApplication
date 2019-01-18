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

	private Connection conn = null;
	private PreparedStatement pst, pst1, pst2 = null;
	private ResultSet rs, rs1, rs2 = null;
	private Statement stmt = null;

	/*
	 * method for registering new user.
	 */
	public int register(User user) throws SQLException {
		int flag = 0;
		try {
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTUSER);
			pst.setString(1, user.getUserName());
			pst.setString(2, user.getEmail());
			pst.setString(3, user.getPassword());
			pst.setString(4, user.getCompany());
			pst.setString(5, user.getDesignation());
			pst.executeUpdate();
			flag = 1;
		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return flag;
	}
	/*
	 * method for adding technology details of a user.
	 */
	public int addTechnologyDetails(UserTechnologyMapping usertechnology) throws SQLException {
		int flag = 0;
		try {
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTTECHNOLOGY);
			pst.setInt(1, usertechnology.getUserId());
			pst.setInt(2, usertechnology.getTechnologyId());
			pst.executeUpdate();
			flag = 1;
		} catch (SQLException e) {
			flag = 0;
			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return flag;
	}

	/*
	 * method for logging in.
	 */
	public int login(User user) throws SQLException {
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVEUSERDATA);
			String email = user.getEmail();
			String password = user.getPassword();
			int roleId = 0;
			while (rs.next()) {
				if ((email.equals(rs.getString(1))) && (password.equals(rs.getString(2)))) {

					roleId = rs.getInt(3);
					break;
				}
			}
			return roleId;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
	}

	/*
	 * method for sending notification if a vacancy is published.
	 */
	public void compareVacancyWithRequest(Company company) throws SQLException {
		NotifyUser notifyuser = new NotifyUser();
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVEJOBREQUESTS);
			int jobId = company.getJobId();
			String location = company.getLocation();
			while (rs.next()) {
				if ((jobId == rs.getInt(2)) && location.equals(rs.getString(3))) {
					String email = rs.getString(1);
					notifyuser.sendNotification(email);
				}
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}

	}

	/*
	 * method for publishing new vacancy.
	 */
	public int publishVacancy(Company company) throws SQLException {
		int flag = 0;
		try {
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTVACANCY);
			pst.setInt(1, company.getCompanyId());
			pst.setInt(2, company.getJobId());
			pst.setString(3, company.getLocation());
			pst.setString(4, company.getJobDescription());
			pst.setFloat(5, company.getSalary());
			pst.setInt(6, 1);
			pst.executeUpdate();
			flag = 1;

		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return flag;
	}

	/*
	 * method for adding new company to the site.
	 */
	public int addNewCompany(Company company) throws SQLException {
		int flag = 0;
		try {
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTCOMPANY);
			pst.setString(1, company.getCompanyName());
			pst.setString(2, company.getCompanyWebsiteUrl());
			pst.executeUpdate();
			flag = 1;
		} catch (SQLException e) {
			flag = 0;
			throw e;

		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return flag;
	}

	/*
	 * method for fetching company id giving company name as input.
	 */
	public int fetchCompanyId(Company company) throws SQLException {
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVECOMPANYDATA);
			int companyId = 0;
			String companyName = company.getCompanyName();
			while (rs.next()) {
				if (companyName.equals(rs.getString(2))) {
					companyId = rs.getInt(1);
					break;
				}

			}

			return companyId;
		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
	}

	/*
	 * method for adding new company to the site.
	 */
	public ArrayList<JobMapping> displayJobs(JobMapping jobmapping) throws SQLException {
		ArrayList<JobMapping> job = new ArrayList<JobMapping>();
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVEJOBDATA);
			while (rs.next()) {
				JobMapping jm = new JobMapping();
				jm.setJobId(rs.getInt(1));
				jm.setJobRole(rs.getString(2));
				job.add(jm);
			}

		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}

		return job;
	}

	/*
	 * method for displaying company to the site.
	 */
	public ArrayList<Technology> displayTechnologies(Technology technology) throws SQLException {
		ArrayList<Technology> tech = new ArrayList<Technology>();
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVETECHNOLOGYDATA);
			while (rs.next()) {
				Technology t = new Technology();
				t.setTechnologyId(rs.getInt(1));
				t.setTechnology(rs.getString(2));
				tech.add(t);
			}

		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return tech;
	}

	/*
	 * method for checking if the user is registered.
	 */

	public int checkIfRegistered(User user) throws SQLException {
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVEUSERDATA);
			String email = user.getEmail();
			int flag = 0;
			while (rs.next()) {
				if (email.equals(rs.getString(1))) {
					flag = 1;
					break;
				} else {
					flag = 0;
				}
			}

			return flag;

		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}

	}

	/*
	 * method for displaying companies.
	 */
	public ArrayList<Company> displayCompanies(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVECOMPANYDATA);
			
			while (rs.next()) {
				Company c = new Company();
				c.setCompanyName(rs.getString(2));
				comp.add(c);
			}

		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return comp;
	}

	/*
	 * method for adding new jobs.
	 */
	public int addNewJob(JobMapping jobmapping) throws SQLException {
		try {
			int jobId = 0;
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTJOB);
			pst.setString(1, jobmapping.getJobRole());
			pst.executeUpdate();
			jobId = fetchJobId(jobmapping);
			return jobId;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}

	}

	/*
	 * method for fetching user id given user mail.
	 */
	public int fetchUserId(User user) throws SQLException {
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVEUSERDATA);
			int userId = 0;
			String email = user.getEmail();
			while (rs.next()) {
				if (email.equals(rs.getString(1))) {
					userId = rs.getInt(4);
					break;
				}
			}
			return userId;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}

	}

	/*
	 * method for fetching job id given job designation.
	 */
	public int fetchJobId(JobMapping jobmapping) throws SQLException {
		try {
			conn = DButils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(QueryConstants.RETRIEVEJOBDATA);
			int jobId = 0;
			String jobRole = jobmapping.getJobRole();
			while (rs.next()) {
				if (jobRole.equals(rs.getString(2))) {
					jobId = rs.getInt(1);
					break;
				}
			}
			return jobId;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
	}

	/*
	 * method for fetching job id given job designation.
	 */
	public int removeVacancy(Company company) throws SQLException {
		int flag = 0;
		try {
			conn = DButils.getConnection();

			pst = conn.prepareStatement(QueryConstants.DELETEVACANCY);
			pst.setInt(1, company.getCompanyId());
			pst.setInt(2, company.getJobId());
			pst.executeUpdate();
			flag = 1;

		} catch (SQLException e) {

			flag = 0;
			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return flag;
	}

	/*
	 * method for deleting a company.
	 */
	public int deleteCompany(Company company) throws SQLException {
		int flag = 0;
		try {
			conn = DButils.getConnection();

			pst = conn.prepareStatement(QueryConstants.DELETECOMPANY);
			pst.setInt(1, company.getCompanyId());
			pst.executeUpdate();
			flag = 1;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return flag;
	}

	/*
	 * method for deleting an user account.
	 */
	public int deleteUserAccount(User user) throws SQLException {
		int flag = 0;
		try {
			conn = DButils.getConnection();

			pst = conn.prepareStatement(QueryConstants.DELETEUSER);
			pst.setInt(1, user.getUserId());
			pst.executeUpdate();
			flag = 1;

		} catch (SQLException e) {

			flag = 0;
			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return flag;
	}

	/*
	 * method 1 for retrieving vacancy based on company.
	 */
	public ArrayList<Company> retrieveVacancyByCompany(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
			pst.setInt(1, company.getCompanyId());
			rs = pst.executeQuery();
			while (rs.next()) {

				Company c = new Company();
				c.setCompanyName(rs.getString(1));
				c.setCompanyWebsiteUrl(rs.getString(3));
				c.setAverageRating(rs.getInt(2));
				comp.add(c);

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return comp;
	}/*
	 * method 2 for retrieving vacancy based on company.
	 */
	public ArrayList<Company> retrieveVacancyByCompany1(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			
			conn = DButils.getConnection();
			int jobId = 0;
			pst1 = conn.prepareStatement(QueryConstants.RETRIEVEVACANCYBYCOMPID);
			pst1.setInt(1, company.getCompanyId());
			rs1 = pst1.executeQuery();
			while (rs1.next()) {
				Company c = new Company();
				c.setJobDescription(rs1.getString(4));
				c.setLocation(rs1.getString(3));
				c.setSalary(rs1.getFloat(5));

				String jobRole = rs1.getString(2);
				jobId = Integer.parseInt(jobRole);
				pst2 = conn.prepareStatement(QueryConstants.RETRIEVEJOBDESIGNATION);
				pst2.setInt(1, jobId);
				rs2 = pst2.executeQuery();
				while (rs2.next()) {
					c.setJobRole(rs2.getString(1));
					comp.add(c);
					
				}

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(conn, pst1, rs1);
		}
		return comp;
	}

	/*
	 * method 1 for retrieving vacancy based on job.
	 */
	public ArrayList<Company> retrieveVacancyByJob(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.RETRIEVEJOBDESIGNATION);
			pst.setInt(1, company.getJobId());
			rs = pst.executeQuery();
			while (rs.next()) {
				Company c = new Company();
				c.setJobRole(rs.getString(1));
				comp.add(c);
			}

		} catch (SQLException e) {
			throw e;
			
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return comp;
	}
	/*
	 * method 2 for retrieving vacancy based on job.
	 */
	public ArrayList<Company> retrieveVacancyByJob1(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {
			
			conn = DButils.getConnection();
			int companyId = 0;
			pst = conn.prepareStatement(QueryConstants.RETRIEVEVACANCYBYJOBID);
			pst.setInt(1, company.getJobId());
			rs = pst.executeQuery();
			while (rs.next()) {
				
				Company c = new Company();
				c.setLocation(rs.getString(3));
				c.setJobDescription(rs.getString(4));
				c.setSalary(rs.getFloat(5));

				String companyid = rs.getString(1);
				companyId = Integer.parseInt(companyid);
				pst1 = conn.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
				pst1.setInt(1, companyId);
				rs1 = pst1.executeQuery();
				while (rs1.next()) {

					c.setCompanyName(rs1.getString(1));
					c.setCompanyWebsiteUrl(rs1.getString(3));
					c.setAverageRating(rs1.getInt(2));
					comp.add(c);
				}

			}
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
		return comp;
	}

	/*
	 * method for requesting vacancy.
	 */
	public int requestNewVacancy(JobRequest jobrequest) throws SQLException {
		try {
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTJOBREQUEST);
			pst.setString(1, jobrequest.getEmail());
			pst.setInt(2, jobrequest.getJobId());
			pst.setString(3, jobrequest.getLocation());
			pst.setFloat(4, jobrequest.getSalary());
			pst.executeUpdate();
			return 1;
		} catch (SQLException e) {

			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
	}

	/*
	 * method for adding interview process of a company
	 */
	public int interviewProcess(User user, Company company, JobMapping jobmapping) throws SQLException {
		try {
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTJOBREVIEW);
			pst.setInt(1, user.getUserId());
			pst.setInt(2, company.getCompanyId());
			pst.setInt(3, jobmapping.getJobId());
			pst.setString(4, company.getInterviewProcess());
			pst.executeUpdate();
			return 1;
		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(conn, pst, rs);
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
			conn = DButils.getConnection();
			pst = conn.prepareStatement(QueryConstants.INSERTREVIEWRATING);
			pst.setInt(1, user.getUserId());
			pst.setInt(2, company.getCompanyId());
			pst.setString(3, company.getReview());
			pst.setInt(4, company.getRating());
			pst.executeUpdate();
			newRating = company.getRating();
			pst = conn.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
			pst.setInt(1, company.getCompanyId());
			rs = pst.executeQuery();
			while (rs.next()) {	
					existingRating = rs.getInt(2);
					flag = 1;
			}
			if (flag == 1) {
				existingRating = (existingRating + newRating) / 2;
			}
			pst = conn.prepareStatement(QueryConstants.UPDATECOMPANYRATING);
			pst.setInt(1, existingRating);
			pst.setInt(2, company.getCompanyId());
			pst.executeUpdate();
			return 1;
		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(conn, pst, rs);
		}
	}

}
