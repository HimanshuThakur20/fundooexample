package com.bridegelabz.fundoo.label.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "label")
public class Label 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private LocalDateTime createDate;
	private LocalDateTime modifiedDate;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@JsonIgnore
	@ManyToMany(mappedBy="label",cascade = CascadeType.ALL)
//	@JoinColumn(name = "note_id")
	private List<Notes> note;
	public Label() 
	{
		

	}
	

	public List<Notes> getNote() {
		return note;
	}


	public void setNote(List<Notes> note) {
		this.note = note;
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
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Label [id=" + id + ", name=" + name + ", createDate=" + createDate + ", modifiedDate=" + modifiedDate
				+ ", user=" + user +"]";
	}
	
}
