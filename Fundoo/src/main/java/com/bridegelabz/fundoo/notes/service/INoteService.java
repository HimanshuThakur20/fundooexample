package com.bridegelabz.fundoo.notes.service;

import java.io.UnsupportedEncodingException;

import com.bridegelabz.fundoo.notes.dto.NotesDto;
import com.bridegelabz.fundoo.response.Response;

public interface INoteService 
{
	public Response createNote(NotesDto noteDto, String token) throws UnsupportedEncodingException;
	public Response updateNote(NotesDto noteDto, String token, int noteId) throws Exception;
	public Response deleteNote(String token, int noteId) throws Exception;
	public Response deleteNotePermanently(String token, int noteId) throws Exception;
	public Response pin(String token, int noteId) throws Exception;
	public Response archive(String token, int noteId) throws Exception;
	public Response Trash(String token, int noteId) throws Exception;
	public Response changeColor(String token,String color, int noteId);
}
