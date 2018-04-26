package com.finder.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "finder")
public class FinderInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "area")
	private String area;
	
	@Column(name = "recepti")
	private String recepti;
	
	@Column(name = "date")
	private String date;

	@Column(name = "cause")
	private String cause;
	
	@Column(name = "estate")
	private String estate;
	
	@Column(name = "lotnumber")
	private String lotnumber;
	
	@Column(name = "house")
	private String house;
	
	@Column(name = "folderpath")
	private String folderpath;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;

	public FinderInfo() {
		super();
	}
	
	public FinderInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public FinderInfo(int id, String recepti, String area, String date, String cause, String estate, String lotnumber,
			String house, String folderpath) {
		super();
		this.id = id;
		this.recepti = recepti;
		this.area = area;
		this.date = date;
		this.cause = cause;
		this.estate = estate;
		this.lotnumber = lotnumber;
		this.house = house;
		this.folderpath = folderpath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRecepti() {
		return recepti;
	}

	public void setRecepti(String recepti) {
		this.recepti = recepti;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getEstate() {
		return estate;
	}

	public void setEstate(String estate) {
		this.estate = estate;
	}

	public String getLotnumber() {
		return lotnumber;
	}

	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getFolderpath() {
		return folderpath;
	}

	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}

		
	
}
