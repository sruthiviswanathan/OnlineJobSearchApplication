package com.zilker.onlinejobsearch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.zilker.onlinejobsearch.beans.Company;
import com.zilker.onlinejobsearch.beans.User;
import com.zilker.onlinejobsearch.constants.QueryConstants;
import com.zilker.onlinejobsearch.utils.DButils;
import com.zilker.onlinejobsearch.utils.NotifyUser;

public class CompanyDAO {

	private Connection connection = null;
	private PreparedStatement preparestatement, preparestatement1, preparestatement2 = null;
	private ResultSet resultset, resultset1, resultset2 = null;
	private Statement statement = null;

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
	public int publishVacancy(Company company, User user) throws SQLException {
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
			preparestatement.setInt(7, user.getUserId());
			preparestatement.setInt(8, user.getUserId());
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
	 * method for adding new company to the site by company admin.
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
	 * method for adding new company by site admin.
	 */
	public int addNewCompanyBySiteAdmin(Company company, User user) throws SQLException {
		// TODO Auto-generated method stub
		int flag = 0;
		try {
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.INSERTCOMPANYBYSITEADMIN);
			preparestatement.setString(1, company.getCompanyName());
			preparestatement.setString(2, company.getCompanyWebsiteUrl());
			preparestatement.setInt(3, user.getUserId());
			preparestatement.setInt(4, user.getUserId());
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
			statement = connection.createStatement();
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
	 * method for fetching job id given job designation.
	 */
	public int removeVacancy(Company company, User user) throws SQLException {
		int flag = 0;
		try {
			connection = DButils.getConnection();

			preparestatement = connection.prepareStatement(QueryConstants.DELETEVACANCY);
			preparestatement.setInt(1, user.getUserId());
			preparestatement.setInt(2, company.getCompanyId());
			preparestatement.setInt(3, company.getJobId());
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
	 * method 1 for retrieving vacancy based on company.
	 */
	public ArrayList<Company> retrieveVacancyByCompany(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {

			float averageRating = 0;
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVECOMPANYNAME);
			preparestatement.setInt(1, company.getCompanyId());
			resultset = preparestatement.executeQuery();
			while (resultset.next()) {

				Company c = new Company();
				c.setCompanyName(resultset.getString(1));
				c.setCompanyWebsiteUrl(resultset.getString(2));
				averageRating = calculateAverageRating(company);
				c.setAverageRating(averageRating);
				comp.add(c);

			}

		} catch (SQLException e) {
			throw e;
		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return comp;
	}

	public float calculateAverageRating(Company company) throws SQLException {

		float averageRating = 0;
		try {

			float rating = 0;
			String ratings = "";
			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVERATINGSFORCOMPANY);
			preparestatement.setInt(1, company.getCompanyId());
			resultset = preparestatement.executeQuery();
			while (resultset.next()) {
				ratings = resultset.getString(1);
				rating = Float.parseFloat(ratings);
				averageRating = (averageRating + rating) / 2;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			// DButils.closeConnection(connection, preparestatement, resultset);
		}

		return averageRating;
	}

	/*
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
				c.setVacancyCount(resultset1.getInt(6));

				String jobRole = resultset1.getString(2);
				jobId = Integer.parseInt(jobRole);
				preparestatement2 = connection.prepareStatement(QueryConstants.RETRIEVEJOBDESIGNATION);
				preparestatement2.setInt(1, jobId);
				resultset2 = preparestatement2.executeQuery();
				while (resultset2.next()) {
					c.setJobId(jobId);
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

	public ArrayList<Company> retrieveVacancyByCompanyAdmin(Company company) throws SQLException {
		ArrayList<Company> comp = new ArrayList<Company>();
		try {

			connection = DButils.getConnection();
			int jobId = 0;
			preparestatement1 = connection.prepareStatement(QueryConstants.RETRIEVEVACANCYADMIN);
			preparestatement1.setInt(1, company.getCompanyId());
			resultset1 = preparestatement1.executeQuery();
			while (resultset1.next()) {
				Company c = new Company();
				c.setJobDescription(resultset1.getString(4));
				c.setLocation(resultset1.getString(3));
				c.setSalary(resultset1.getFloat(5));
				c.setVacancyCount(resultset1.getInt(6));

				String jobRole = resultset1.getString(2);
				jobId = Integer.parseInt(jobRole);
				preparestatement2 = connection.prepareStatement(QueryConstants.RETRIEVEJOBDESIGNATION);
				preparestatement2.setInt(1, jobId);
				resultset2 = preparestatement2.executeQuery();
				while (resultset2.next()) {
					c.setJobId(jobId);
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

	public ArrayList<Company> retrieveVacancyByLocation(Company company) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Company> comp = new ArrayList<Company>();
		try {

			connection = DButils.getConnection();
			preparestatement = connection.prepareStatement(QueryConstants.RETRIEVECOMPANYBYLOCATION);
			preparestatement.setString(1, company.getLocation());
			resultset = preparestatement.executeQuery();
			while (resultset.next()) {
				Company c = new Company();
				c.setLocation(resultset.getString(3));
				c.setJobDescription(resultset.getString(4));
				c.setSalary(resultset.getFloat(5));
				c.setVacancyCount(resultset.getInt(6));
				c.setCompanyId(resultset.getInt(1));
				c.setJobId(resultset.getInt(2));
				comp.add(c);
			}
		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
			DButils.closeConnection(connection, preparestatement1, resultset1);
			DButils.closeConnection(connection, preparestatement2, resultset2);
		}
		return comp;

	}

	public boolean updateVacancyJobId(Company company, User user) throws SQLException {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			connection = DButils.getConnection();
			int companyId = company.getCompanyId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEVACANCYDESIGNATION);
			preparestatement.setInt(1, company.getJobId());
			preparestatement.setInt(2, user.getUserId());
			preparestatement.setString(3, user.getCurrentTime());
			preparestatement.setInt(4, companyId);
			preparestatement.setInt(5, company.getOldJobId());
			preparestatement.executeUpdate();
			flag = true;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	public boolean updateVacancyLocation(Company company, User user) throws SQLException {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			connection = DButils.getConnection();
			int companyId = company.getCompanyId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEVACANCYLOCATION);
			preparestatement.setString(1, company.getLocation());
			preparestatement.setInt(2, user.getUserId());
			preparestatement.setString(3, user.getCurrentTime());
			preparestatement.setInt(4, companyId);
			preparestatement.setInt(5, company.getOldJobId());
			preparestatement.executeUpdate();
			flag = true;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	public boolean updateVacancyDescription(Company company, User user) throws SQLException {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			connection = DButils.getConnection();
			int companyId = company.getCompanyId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEVACANCYDESCRIPTION);
			preparestatement.setString(1, company.getJobDescription());
			preparestatement.setInt(2, user.getUserId());
			preparestatement.setString(3, user.getCurrentTime());
			preparestatement.setInt(4, companyId);
			preparestatement.setInt(5, company.getOldJobId());
			preparestatement.executeUpdate();
			flag = true;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	public boolean updateVacancySalary(Company company, User user) throws SQLException {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			connection = DButils.getConnection();
			int companyId = company.getCompanyId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEVACANCYSALARY);
			preparestatement.setFloat(1, company.getSalary());
			preparestatement.setInt(2, user.getUserId());
			preparestatement.setString(3, user.getCurrentTime());
			preparestatement.setInt(4, companyId);
			preparestatement.setInt(5, company.getOldJobId());
			preparestatement.executeUpdate();
			flag = true;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	public boolean updateVacancyCount(Company company, User user) throws SQLException {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			connection = DButils.getConnection();
			int companyId = company.getCompanyId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATEVACANCYCOUNT);
			preparestatement.setInt(1, company.getVacancyCount());
			preparestatement.setString(2, company.getVacancyStatus());
			preparestatement.setInt(3, user.getUserId());
			preparestatement.setString(4, user.getCurrentTime());
			preparestatement.setInt(5, companyId);
			preparestatement.setInt(6, company.getOldJobId());
			preparestatement.executeUpdate();
			flag = true;

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
		return flag;
	}

	public void insertIntoCompanyDetails(User user, Company company) throws SQLException {
		// TODO Auto-generated method stub
		int userId = 0, companyId = 0;
		try {
			connection = DButils.getConnection();
			userId = user.getUserId();
			companyId = company.getCompanyId();
			preparestatement = connection.prepareStatement(QueryConstants.UPDATECOMPANYCREATER);
			preparestatement.setInt(1, userId);
			preparestatement.setInt(2, userId);
			preparestatement.setInt(3, companyId);
			preparestatement.executeUpdate();

		} catch (SQLException e) {
			throw e;

		} finally {
			DButils.closeConnection(connection, preparestatement, resultset);
		}
	}

}
