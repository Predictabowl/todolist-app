package it.aldinucci.todoapp.domain;

import java.util.Date;
import java.util.Objects;

public class VerificationToken {

	private String token;
	private Date expiryDate;

	public VerificationToken() {
	}

	public VerificationToken(String token, Date expiryDate) {
		this.token = token;
		this.expiryDate = expiryDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public boolean isExpired(Date date) {
		return getExpiryDate().before(date);
	}

	@Override
	public int hashCode() {
		return Objects.hash(expiryDate, token);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerificationToken other = (VerificationToken) obj;
		return Objects.equals(expiryDate, other.expiryDate) && Objects.equals(token, other.token);
	}

	@Override
	public String toString() {
		return "VerificationToken [token=" + token + ", expiryDate=" + expiryDate + "]";
	}
	
	
}
