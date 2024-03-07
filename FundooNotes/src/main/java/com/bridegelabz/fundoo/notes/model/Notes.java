package com.bridegelabz.fundoo.notes.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "notes")
public class Notes 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String title;
	private String description;
	private String color;
	private String reminder;
	private int userId;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	private boolean isPin=false;
	private boolean isArchive=false;
	private boolean isTrash=false;
	private boolean isRestored=false;
	public boolean isRestored() {
		return isRestored;
	}
	public void setRestored(boolean isRestored) {
		this.isRestored = isRestored;
	}
	private LocalDateTime createdDateAndTime;
	private LocalDateTime modefiedDateTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isPin() {
		return isPin;
	}
	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}
	public boolean isArchive() {
		return isArchive;
	}
	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}
	public boolean isTrash() {
		return isTrash;
	}
	public void setTrash(boolean isTrash) {
		this.isTrash = isTrash;
	}
	public LocalDateTime getCreatedDateAndTime() {
		return createdDateAndTime;
	}
	public void setCreatedDateAndTime(LocalDateTime createdDateAndTime) {
		this.createdDateAndTime = createdDateAndTime;
	}
	public LocalDateTime getModefiedDateTime() {
		return modefiedDateTime;
	}
	public void setModefiedDateTime(LocalDateTime modefiedDateTime) {
		this.modefiedDateTime = modefiedDateTime;
	}
//	@Override
//	public String toString() {
//		return "Notes [id=" + id + ", title=" + title + ", description=" + description + ", color=" + color + ", isPin="
//				+ isPin + ", isArchive=" + isArchive + ", isTrash=" + isTrash + ", isRestored=" + isRestored
//				+ ", createdDateAndTime=" + createdDateAndTime + ", modefiedDateTime=" + modefiedDateTime + ", user="
//				+ user + ", label=" + label + "]";
//	}
		
}
