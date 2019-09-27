package com.realspeed.jwt.model;

public class Data {

	private long id;
	private String subject;
	private String role;

	
	public Data(long id, String subject, String role) {
		super();
		this.id = id;
		this.subject = subject;
		this.role = role;
	}

	public Data() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
