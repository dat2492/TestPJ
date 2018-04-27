package com.finder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.*;
import org.hibernate.validator.constraints.NotEmpty;



@Entity
@XmlRootElement
@Table(name = "finder")
public class FinderInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", nullable = false)		
	private int id;

	@Column(name = "area")	
	private String area;
	

	@Column(name = "recepti")	
	@NotEmpty(message = "Recepti is not empty")
	@Pattern(regexp = "[0-9]*", message = "Please input number !" )
	private String recepti;
	
	
	@Column(name = "date", nullable = false)
	@NotEmpty(message = "Date is not empty")
	private String date;

	
	@Column(name = "cause", nullable = false)
	@NotEmpty(message = "Cause is not empty")	
	private String cause;
	
	@Column(name = "estate", nullable = true)	
	private String estate;
	
	@Column(name = "lotnumber")
	private String lotnumber;	
	
	@Column(name = "house")
	private String house;
	
	
	@Column(name = "folderpath", nullable = false)
	@NotEmpty(message = "Path is not empty")	
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
	@XmlTransient
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	@XmlTransient
	public void setPassword(String password) {
		this.password = password;
	}   
	
	

	public FinderInfo(int id, String recepti, String area, String date, String cause, String estate, String lotnumber,
			String house, String folderpath, String username, String password) {
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
		//this.folderpathupload = folderpathupload;
		this.username = username;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}
	@XmlTransient
	public void setId(int id) {
		this.id = id;
	}
	
	public String getArea() {
		return area;
	}
	@XmlElement(name="担当局名")
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getRecepti() {
		return recepti;
	}
	@XmlElement(name="受付番号")
	public void setRecepti(String recepti) {
		this.recepti = recepti;
	}
	
	public String getDate() {
		return date;
	}
	@XmlElement(name="受付年月日")
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getCause() {
		return cause;
	}
	@XmlElement(name="原因")
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	public String getEstate() {
		return estate;
	}
	@XmlElement(name="不動産番号")
	public void setEstate(String estate) {
		this.estate = estate;
	}

	public String getLotnumber() {
		return lotnumber;
	}
	@XmlElement(name="地番")
	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}

	public String getHouse() {
		return house;
	}
	@XmlElement(name="家屋番号")
	public void setHouse(String house) {
		this.house = house;
	}

	public String getFolderpath() {
		return folderpath;
	}
	@XmlTransient
	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}
}
