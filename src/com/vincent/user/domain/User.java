package com.vincent.user.domain;

import java.io.Serializable;

public class User implements Serializable {

	private String uId;
	private String userName;
	private String uPassword;
	private String cId;
	private String uTime;
	private String role;
	private String fileSize;

	public User() {
		super();
	}

	public User(String uId, String userName, String uPassword, String cId,
			String uTime, String role, String fileSize) {
		super();
		this.uId = uId;
		this.userName = userName;
		this.uPassword = uPassword;
		this.cId = cId;
		this.uTime = uTime;
		this.role = role;
		this.fileSize = fileSize;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getuPassword() {
		return uPassword;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getuTime() {
		return uTime;
	}

	public void setuTime(String uTime) {
		this.uTime = uTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cId == null) ? 0 : cId.hashCode());
		result = prime * result
				+ ((fileSize == null) ? 0 : fileSize.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((uId == null) ? 0 : uId.hashCode());
		result = prime * result
				+ ((uPassword == null) ? 0 : uPassword.hashCode());
		result = prime * result + ((uTime == null) ? 0 : uTime.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		if (fileSize == null) {
			if (other.fileSize != null)
				return false;
		} else if (!fileSize.equals(other.fileSize))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (uId == null) {
			if (other.uId != null)
				return false;
		} else if (!uId.equals(other.uId))
			return false;
		if (uPassword == null) {
			if (other.uPassword != null)
				return false;
		} else if (!uPassword.equals(other.uPassword))
			return false;
		if (uTime == null) {
			if (other.uTime != null)
				return false;
		} else if (!uTime.equals(other.uTime))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
