package com.bridegelabz.fundoo.notes.controller;

import java.io.UnsupportedEncodingException;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridegelabz.fundoo.notes.dto.NotesDto;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.notes.service.NoteService;
import com.bridegelabz.fundoo.response.Response;
@CrossOrigin( origins = "*", allowedHeaders = "*")
@RestController
public class NotesRestController 
{
	@Autowired
	private NoteService noteService;
	@PostMapping("/createnote")
	public ResponseEntity<Response> crateNotes(@RequestParam String token,@RequestBody NotesDto noteDto ) throws Exception
	{
		System.out.println("create note");
		Response response = noteService.createNote(noteDto, token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PutMapping("/updatenote")
	public ResponseEntity<Response> updateNotes(@RequestParam String token, @RequestParam int noteId, @RequestBody NotesDto noteDto) throws Exception
	{
		System.out.println("hit to update");
		Response response = noteService.updateNote(noteDto, token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	@PutMapping("/deletenote")
	public ResponseEntity<Response> deleteNote(@RequestParam String token, @RequestParam int noteId) throws Exception
	{
		System.out.println("Inside delete");
		Response response = noteService.deleteNote(token, noteId);
		
		return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	@PutMapping("/deletenotepermanently")
	public ResponseEntity<Response> deleteNotePermanently(@RequestParam String token, @RequestParam int noteId) throws Exception
	{
		Response response = noteService.deleteNotePermanently(token, noteId); 
		return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	@GetMapping("/getallnotes")
	public List<Notes> getAllNotes(@RequestParam String token) throws UnsupportedEncodingException
	{
		List<Notes> listOfNotes = noteService.getAllNotes(token);
		System.out.println("List of Notes"+listOfNotes);
		return listOfNotes;
	}
	@GetMapping("/getalltrashednotes")
	public List<Notes> getAllTrashedNotes(@RequestParam String token) throws UnsupportedEncodingException
	{
		List<Notes> listOfTrashedNotes = noteService.getAllTrashedNotes(token);
		return listOfTrashedNotes;
	}
	@GetMapping("/getallpinednotes")
	public List<Notes> getAllPinnedNotes(@RequestParam String token) throws UnsupportedEncodingException
	{
		System.out.println("hit to pinned notes");
		List<Notes> listOfPinnedNotes = noteService.getAllPinnedNotes(token);
		return listOfPinnedNotes;
	}
	@GetMapping("/getallarchivednotes")
	public List<Notes> getAllArchivedNotes(@RequestParam String token) throws UnsupportedEncodingException
	{
		List<Notes> listOfArchivedNotes = noteService.getAllArchivedNotes(token);
		return listOfArchivedNotes;
	}
	@PutMapping("/pin")
	public ResponseEntity<Response> pin(@RequestParam String token, @RequestParam int noteId) throws Exception
	{
		Response response = noteService.pin(token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PutMapping("/archive")
	public ResponseEntity<Response> archive(@RequestParam String token, @RequestParam int noteId) throws Exception
	{
		Response response = noteService.archive(token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PutMapping("/trash")
	public ResponseEntity<Response> trash(@RequestParam String token, @RequestParam int noteId) throws Exception
	{
		Response response = noteService.Trash(token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PostMapping("/addreminder")
	public ResponseEntity<Response> addReminder(@RequestParam String token, @RequestParam int noteId, @RequestParam String reminder) throws UnsupportedEncodingException
	{
		Response response = noteService.addReminder(token, noteId, reminder);
		return new ResponseEntity<Response> (response, HttpStatus.OK);
	}
	@PutMapping("/removeReminder")
	public ResponseEntity<Response> removeReminder(@RequestParam String token, @RequestParam int noteId) throws UnsupportedEncodingException
	{
		Response response = noteService.removeReminder(token, noteId);
		return new ResponseEntity<Response> (response, HttpStatus.OK);
		
	}
	@GetMapping("/getreminder")
	public ResponseEntity<String> getReminder(@RequestParam String token, @RequestParam int noteId) throws UnsupportedEncodingException
	{
		System.out.println("Get Reminder Backend");
		String response = noteService.getReminder(token, noteId);
		return new ResponseEntity<String> (response, HttpStatus.OK);
	}
}
