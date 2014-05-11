package com.example.animationtobhost.model;

/*
 * 客户资料类
 */
public class ContactsBean {
	private String name;
	private String number;
	private Long id;
	private Long phoneId;
	private Long duration;
	private Long date;
	private Integer type;//1 通讯录，2：通讯录下的电话号码
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(Long phoneId) {
		this.phoneId = phoneId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
}
