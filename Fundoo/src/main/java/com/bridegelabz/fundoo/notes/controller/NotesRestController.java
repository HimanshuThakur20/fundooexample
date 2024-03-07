package com.bridegelabz.fundoo.notes.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridegelabz.fundoo.notes.dto.NotesDto;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.notes.repository.NoteRepository;
import com.bridegelabz.fundoo.notes.service.NoteService;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.user.model.User;
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
	@PutMapping("/color")
	public ResponseEntity<Response> changeColor(@RequestParam String color, @RequestParam int noteId,@RequestParam String token) throws UnsupportedEncodingException{
		System.out.println("Inside color");
		System.out.println(color);
		Response response = noteService.changeColor(token, color, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	@PostMapping("/addcollaborator")
	public ResponseEntity<Response> collaborate(@RequestParam String token, @RequestParam String emailId, @RequestParam int noteId) throws UnsupportedEncodingException
	{
		Response response = noteService.collaborate(token, emailId,noteId);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
	}
	@PutMapping("/removecollaborator")
	public ResponseEntity<Response> removeCollaborator(@RequestParam String token, @RequestParam String emailId, @RequestParam int noteId) throws UnsupportedEncodingException
	{
		Response response = noteService.removeCollaborator(token, emailId,noteId);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
	}
	@GetMapping("/getcollaboratednotes")
	public Set<Notes> getCollaboratedNotes(@RequestParam String token) throws UnsupportedEncodingException
	{
		Set<Notes> listOfCollaboratedNotes = noteService.getAllCollaboratedNotes(token);
		return listOfCollaboratedNotes;
		
	}
	@GetMapping("/getcollaborateduser")
	public Set<User> getCollaboratedUser(@RequestParam String token,@RequestParam int noteId) throws UnsupportedEncodingException
	{
		Set<User> listOfCollaboratedUser = noteService.getAllCollaboratedUser(token, noteId);
		return listOfCollaboratedUser;
		
	}
//	@GetMapping("/search")
//	public List<Notes> searchNote(@RequestParam String token , @RequestParam String query) throws UnsupportedEncodingException {
//		System.out.println("Before Inside search controller");
//		List<Notes> notes = noteService.searchNote(query, token);
//		System.out.println("After Inside Search Controller"+notes);
//		return notes;
//	}
	
	@GetMapping("/searchnotes/{searchText}")
	public ResponseEntity<List<Notes>> searchNotes(@PathVariable String searchText,@RequestHeader String token) throws UnsupportedEncodingException
	{
//		log.info("token-->"+token);
//		log.info("search note List called ");
		List<Notes> searchedNotes = noteService.searchNotes(searchText,token);
		
		return new ResponseEntity<>(searchedNotes,HttpStatus.OK);
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
