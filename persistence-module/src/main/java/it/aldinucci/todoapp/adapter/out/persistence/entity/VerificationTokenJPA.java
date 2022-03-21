package it.aldinucci.todoapp.adapter.out.persistence.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NaturalId;

@Entity
public class VerificationTokenJPA {

	@Id
	private Long id;

	@Column(nullable = false)
	@NaturalId
	private String token;

	@OneToOne (fetch = FetchType.LAZY, optional = false)
	@MapsId
	private UserJPA user;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;

	public VerificationTokenJPA() {
	}

	public VerificationTokenJPA(Long id, String token, UserJPA user, Date expiryDate) {
		this.id = id;
		this.token = token;
		this.user = user;
		this.expiryDate = expiryDate;
	}
	
	public VerificationTokenJPA(String token, UserJPA user, Date expiryDate) {
		this.id = null;
		this.token = token;
		this.user = user;
		this.expiryDate = expiryDate;
	}

	public final Long getId() {
		return id;
	}

	public final void setId(Long id) {
		this.id = id;
	}

	public final String getToken() {
		return token;
	}

	public final void setToken(String token) {
		this.token = token;
	}

	public final UserJPA getUser() {
		return user;
	}

	public final void setUser(UserJPA user) {
		this.user = user;
	}

	public final Date getExpiryDate() {
		return expiryDate;
	}

	public final void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(expiryDate, id, token, user);
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
		return Objects.equals(expiryDate, other.expiryDate) && Objects.equals(id, other.id)
				&& Objects.equals(token, other.token) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "VerificationTokenJPA [id=" + id + ", token=" + token + ", user=" + user + ", expiryDate=" + expiryDate
				+ "]";
	}


}
