package com.bridegelabz.fundoo.elasticsearch;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridegelabz.fundoo.notes.model.Notes;


public interface IElasticSearch 
{
	public Notes create(Notes note);
	public Notes updateNote(Notes note);
	public void deleteNote(int NoteId);
	public List<Notes> searchData(String query, int userId);
}
