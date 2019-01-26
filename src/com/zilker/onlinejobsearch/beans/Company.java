package com.zilker.onlinejobsearch.beans;
/*
 * bean class for company details.
 */
public class Company {

	private int companyId;
	private String companyName;
	private int jobId, vacancyCount;
	private int oldJobId;
	private String companyWebsiteUrl, jobDescription, location;
	private float salary;
	private String review;
	private float rating,averageRating;
	private String interviewProcess;
	private String jobRole;
	private String vacancyStatus;
	
	
	
	public int getOldJobId() {
		return oldJobId;
	}

	public void setOldJobId(int oldJobId) {
		this.oldJobId = oldJobId;
	}
	
	public int getVacancyCount() {
		return vacancyCount;
	}

	public void setVacancyCount(int vacancyCount) {
		this.vacancyCount = vacancyCount;
	}

	public String getVacancyStatus() {
		return vacancyStatus;
	}

	public void setVacancyStatus(String vacancyStatus) {
		this.vacancyStatus = vacancyStatus;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
	
	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getInterviewProcess() {
		return interviewProcess;
	}

	public void setInterviewProcess(String interviewProcess) {
		this.interviewProcess = interviewProcess;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	
	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}


	public String getCompanyWebsiteUrl() {
		return companyWebsiteUrl;
	}

	public void setCompanyWebsiteUrl(String companyWebsiteUrl) {
		this.companyWebsiteUrl = companyWebsiteUrl;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	
}
