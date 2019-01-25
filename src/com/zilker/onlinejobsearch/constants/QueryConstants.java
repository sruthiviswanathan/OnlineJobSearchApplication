package com.zilker.onlinejobsearch.constants;
/*
 * Class containing all Database Queries.
 */
public class QueryConstants {
	public static final String CONNECTIONSTRING = "jdbc:mysql://localhost:3306/jobsearchsystem";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "root@123";
	public static final String INSERTUSER ="insert into user_classification(user_name,email,password,company_name,designation) values(?,?,?,?,?)";
	public static final String INSERTADMIN ="insert into user_classification(user_name,email,password,company_name,designation,role) values(?,?,?,?,?,?)";
	public static final String INSERTCOMPANYADMIN ="insert into company_admin values(?,?)";
	public static final String RETRIEVEUSERDATA = "select email,password,role,user_id from user_classification";
	public static final String INSERTCOMPANY ="insert into company_details(company_name,website_url) values(?,?)";
	public static final String RETRIEVECOMPANYDATA ="select company_id,company_name from company_details";
	public static final String RETRIEVEJOBDATA = "select * from job";
	public static final String INSERTVACANCY = "insert into vacancy_publish(company_id,job_id,location,job_description,salary,vacancy_count) values(?,?,?,?,?,?)";
	public static final String INSERTJOB = "insert into job(job_designation)" + "values(?)";
	public static final String DELETEVACANCY ="delete from vacancy_publish where company_id=? and job_id=?";
	public static final String RETRIEVEVACANCYBYCOMPID="select * from vacancy_publish where company_id=?";
	public static final String RETRIEVECOMPANYNAME ="select company_name,average_rating,website_url from company_details where company_id=?";
	public static final String RETRIEVEJOBDESIGNATION="select job_designation from job where job_id=?";
	public static final String RETRIEVEVACANCYBYJOBID ="select * from vacancy_publish where job_id=?";
	public static final String INSERTJOBREQUEST="insert into job_request(email,job_id,location,salary) values(?,?,?,?)";
	public static final String INSERTJOBREVIEW ="insert into job_reviews(user_id,company_id,job_id,interview_process) values(?,?,?,?)";
	public static final String INSERTREVIEWRATING ="insert into company_reviews(user_id,company_id,reviews,rating) values(?,?,?,?)";
	public static final String UPDATECOMPANYRATING="update company_details set average_rating = ? where company_id = ?";
	public static final String DELETECOMPANY ="delete from company_details where company_id = ?";
	public static final String DELETEUSER="delete from user_classification where user_id=?";
	public static final String INSERTTECHNOLOGY="insert into user_technology_mapping(user_id,technology_id)values(?,?)";	
	public static final String RETRIEVETECHNOLOGYDATA = "select * from technology";
	public static final String RETRIEVEJOBREQUESTS="select email,job_id,location from job_request";
	public static final String RETRIEVEREVIEW="select reviews from company_reviews where company_id=? ";
	public static final String RETRIEVEINTERVIEWPROCESS="select company_id,interview_process from job_reviews where job_id=?";
	public static final String RETRIEVECOMPANYID="select * from company_admin";
	public static final String DELETEJOBREQUEST="delete from job_request where email=?";
	public static final String UPDATEUSERNAME="update user_classification set user_name=? where user_id=?";
	public static final String UPDATEUSERCOMPANY="update user_classification set company_name=? where user_id=?";
	public static final String UPDATEUSERDESIGNATION="update user_classification set designation=? where user_id=?";
	public static final String RETRIEVEUSERTECHNOLOGY="select technology_id from user_technology_mapping where user_id=?";
	public static final String UPDATEUSERTECHNOLOGY="update user_technology_mapping set technology_id=? where user_id=? and technology_id=?";
}
