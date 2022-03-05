package it.aldinucci.todoapp.adapter.out.persistence.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NaturalId;

@Entity
public class UserJPA {

	@Id
	@GeneratedValue
	private Long id;
	
	@NaturalId
	private String email;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	private boolean enabled;
		
	@OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ProjectJPA> projects;
	
	public UserJPA() {
		this.projects = new LinkedList<>();
		this.enabled = false;
	}

	
	public UserJPA(Long id, String email, String username, String password, boolean enabled,
			List<ProjectJPA> projects) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.projects = projects;
	}
	
	public UserJPA(Long id, String email, String username, String password, boolean enabled) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.projects = new LinkedList<>();
	}

	
	public UserJPA(Long id, String email, String username, String password) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = false;
		this.projects = new LinkedList<>();
	}
	
	public UserJPA(String email, String username, String password) {
		this.id = null;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = false;
		this.projects = new LinkedList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public List<ProjectJPA> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectJPA> projects) {
		this.projects = projects;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, enabled, id, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserJPA other = (UserJPA) obj;
		return Objects.equals(email, other.email) && enabled == other.enabled && Objects.equals(id, other.id)
				&& Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserJPA [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", enabled=" + enabled + "]";
	}

	
}
