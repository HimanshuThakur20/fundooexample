package com.bridegelabz.fundoo.user.model;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bridegelabz.fundoo.notes.model.Notes;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="user")
public class User 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotEmpty(message="Name Cannot Not be Empty")
	@NotNull(message="Name Cannot Not be null")
	private String name;
	@NotEmpty(message="Email Cannot Not be Empty")
	@NotNull(message="Email Cannot Not be null")
	private String emailId;
	@NotEmpty(message="PhNumber Cannot Not be Empty")
	@NotNull(message="PhNumber Cannot Not be null")
	private String phNumber;
	@NotEmpty(message="password Cannot Not be Empty")
	@NotNull(message="password Cannot Not be null")
	private String password;
	private boolean isVerified=false;
	private LocalDate registeredDate;
	private LocalDate modifiedDate;
	private String profilePic;
	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	@JsonIgnore
	@ManyToMany
	private Set<Notes> collaboratedNotes;
	public Set<Notes> getCollaboratedNotes() {
		return collaboratedNotes;
	}

	public void setCollaboratedNotes(Set<Notes> collaboratedNotes) {
		this.collaboratedNotes = collaboratedNotes;
	}

	public User() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhNumber() {
		return phNumber;
	}

	public void setPhNumber(String phNumber) {
		this.phNumber = phNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public LocalDate getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(LocalDate registeredDate) {
		this.registeredDate = registeredDate;
	}

	public LocalDate getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDate modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", emailId=" + emailId + ", phNumber=" + phNumber + ", password="
				+ password + ", isVerified=" + isVerified + ", registeredDate=" + registeredDate + ", modifiedDate="
				+ modifiedDate + "]";
	}


	
}
