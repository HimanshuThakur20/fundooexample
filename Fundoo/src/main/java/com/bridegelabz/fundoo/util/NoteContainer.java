package com.bridegelabz.fundoo.util;

import org.springframework.stereotype.Component;

import com.bridegelabz.fundoo.notes.model.Notes;

@Component
public class NoteContainer {
	
	private Notes note;
	private NoteOperation noteOperation;
	
	public NoteContainer() {
		super();
	}
	public Notes getNote() {
		return note;
	}
	public void setNote(Notes note) {
		this.note = note;
	}
	public NoteOperation getNoteOperation() {
		return noteOperation;
	}
	public void setNoteOperation(NoteOperation noteOperation) {
		this.noteOperation = noteOperation;
	}
	@Override
	public String toString() {
		return "NoteContainer [note=" + note + ", noteOperation=" + noteOperation + "]";
	}
	
}
