package it.aldinucci.todoapp.adapter.out.persistence.entity;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class VerificationTokenJPA {

	@Id
	@GeneratedValue
	private UUID token;

	@OneToOne (fetch = FetchType.LAZY, optional = false)
	private UserJPA user;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;

	public VerificationTokenJPA() {
	}

	public VerificationTokenJPA(UUID token, UserJPA user, Date expiryDate) {
		this.token = token;
		this.user = user;
		this.expiryDate = expiryDate;
	}
	
	public VerificationTokenJPA(UserJPA user, Date expiryDate) {
		this.token = null;
		this.user = user;
		this.expiryDate = expiryDate;
	}

	public UUID getToken() {
		return token;
	}

	public void setToken(UUID token) {
		this.token = token;
	}

	public UserJPA getUser() {
		return user;
	}

	public void setUser(UserJPA user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(expiryDate, token, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerificationTokenJPA other = (VerificationTokenJPA) obj;
		return Objects.equals(expiryDate, other.expiryDate) && Objects.equals(token, other.token)
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "VerificationTokenJPA [token=" + token + ", user=" + user + ", expiryDate=" + expiryDate + "]";
	}

}
