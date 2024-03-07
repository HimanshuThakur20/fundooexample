package com.bridegelabz.fundoo.notes.service;

import  org.springframework.core.env.Environment;




import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridegelabz.fundoo.exception.CreateNoteExceptions;
import com.bridegelabz.fundoo.notes.dto.NotesDto;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.notes.repository.NoteRepository;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.util.StatusHelper;
import com.bridegelabz.fundoo.util.UserToken;

@Service
public class NoteService 
{
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private NoteRepository noteRepositpory;
	@Autowired
	private Environment environment;
	public Response createNote(NotesDto noteDto, String token) throws UnsupportedEncodingException
	{
		if(noteDto.getTitle().isEmpty() || noteDto.getDescription().isEmpty())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		
		Notes notes = modelMapper.map(noteDto, Notes.class);
		int id = UserToken.tokenVerify(token);
				System.out.println("title="+notes.getTitle());
				notes.setUserId(id);
				notes.setCreatedDateAndTime(LocalDateTime.now());
				noteRepository.save(notes);
				return StatusHelper.statusInfo(environment.getProperty("status.notes.noteCreated"), Integer.parseInt(environment.getProperty("status.notes.success")));		
	}
	public Response updateNote(NotesDto noteDto, String token, int noteId) throws Exception
	{
		if(noteDto.getTitle().isEmpty() && noteDto.getDescription().isEmpty())
			throw new CreateNoteExceptions(environment.getProperty("throw new CreateNoteExceptions"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		int userId = UserToken.tokenVerify(token);
	   
	    	Optional <Notes> notes = noteRepository.findById(noteId);
	    		notes.get().setModefiedDateTime(LocalDateTime.now());
	    		notes.get().setTitle(noteDto.getTitle());
	    		notes.get().setDescription(noteDto.getDescription());
				noteRepository.save(notes.get()); 		
	    return StatusHelper.statusInfo(environment.getProperty("status.notes.notesUpdated"), Integer.parseInt(environment.getProperty("status.notes.success")));
	}
	public Response deleteNote(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
		
			Optional<Notes> notes = noteRepository.findById(noteId);
			System.out.println(notes.get().isTrash());
			if(notes.get().isTrash())
			{
		    	throw new CreateNoteExceptions(environment.getProperty("status.notes.alreadyDeleted"),Integer.parseInt(environment.getProperty("status.notes.failure")));
			}
			else
			{
				notes.get().setTrash(true);
			}
			System.out.println(notes.get().isTrash());
			return StatusHelper.statusInfo(environment.getProperty("status.notes.noteDeleted"), Integer.parseInt(environment.getProperty("status.notes.success")));
	}
	public Response deleteNotePermanently(String token, int noteId) throws Exception 
	{
		int id = UserToken.tokenVerify(token);
		
			Optional<Notes> notes = noteRepository.findById(noteId);
			if(notes.isPresent())
			{
				if(notes.get().isTrash())
				{
					noteRepository.delete(notes.get());
				}	
				return StatusHelper.statusInfo(environment.getProperty("status.notes.notePermanentlyDeleted"), Integer.parseInt(environment.getProperty("status.notes.success")));
			}
			else
			{
				throw new CreateNoteExceptions(environment.getProperty("CreateNoteExceptions"), Integer.parseInt("status.notes.failure"));
			}
	}
	public List<Notes> getAllNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		List<Notes> notes = noteRepository.findNotesByUserId(id);
			List<Notes> listOfNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isArchive() == false && userNotes.isTrash() == false && userNotes.isPin()==false) {
					listOfNotes.add(userNotes);
			}
		}
		return listOfNotes;	
	}
	public List<Notes> getAllTrashedNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		List<Notes> notes = noteRepository.findNotesByUserId(id);
			List<Notes> listOfTrashedNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isTrash() == true) {
					listOfTrashedNotes.add(userNotes);
			}
		}
		return listOfTrashedNotes;
	}
	public List<Notes> getAllPinnedNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
			List<Notes> notes = noteRepository.findNotesByUserId(id);
			List<Notes> listOfPinnedNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isPin() == true) {
					listOfPinnedNotes.add(userNotes);
			}
		}
		return listOfPinnedNotes;
	}
	public List<Notes> getAllArchivedNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
			List<Notes> notes = noteRepository.findNotesByUserId(id);
			List<Notes> listOfArchivedNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isArchive() == true && userNotes.isTrash()==false && userNotes.isPin()==false) {
					listOfArchivedNotes.add(userNotes);
			}
		}
		return listOfArchivedNotes;
		
	}
	public Response pin(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
			Optional<Notes> notes = noteRepository.findById(noteId);
			if(notes.get().isPin())
			{
				notes.get().setPin(false);
				notes.get().setModefiedDateTime(LocalDateTime.now());
				noteRepository.save(notes.get());
				return StatusHelper.statusInfo(environment.getProperty("status.notes.unpin"), Integer.parseInt(environment.getProperty("status.notes.success")));
			}
			else
			{
				notes.get().setPin(true);
				notes.get().setModefiedDateTime(LocalDateTime.now());
				noteRepository.save(notes.get());
				return StatusHelper.statusInfo(environment.getProperty("status.notes.pin"), Integer.parseInt(environment.getProperty("status.notes.success")));
			}
  }		
	public Response archive(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
			Optional<Notes> notes = noteRepository.findById(noteId);
			if(notes.get().isArchive())
			{
				notes.get().setArchive(false);
				notes.get().setModefiedDateTime(LocalDateTime.now());
				noteRepository.save(notes.get());
				return StatusHelper.statusInfo(environment.getProperty("status.notes.unArchive"), Integer.parseInt(environment.getProperty("status.notes.success")));
			}
			else
			{
				notes.get().setArchive(true);
				notes.get().setModefiedDateTime(LocalDateTime.now());
				noteRepository.save(notes.get());
				return StatusHelper.statusInfo(environment.getProperty("status.notes.archived"), Integer.parseInt(environment.getProperty("status.notes.success")));
			}	
	}
	public Response Trash(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
			Optional<Notes> notes = noteRepository.findById(noteId);
			if(notes.get().isTrash())
			{
				notes.get().setTrash(false);
				notes.get().setModefiedDateTime(LocalDateTime.now());
				noteRepository.save(notes.get());
				return StatusHelper.statusInfo(environment.getProperty("status.notes.untrash"), Integer.parseInt(environment.getProperty("status.notes.success")));
			}
			else
			{
				notes.get().setTrash(true);
				notes.get().setModefiedDateTime(LocalDateTime.now());
				noteRepository.save(notes.get());
				return StatusHelper.statusInfo(environment.getProperty("status.notes.trash"), Integer.parseInt(environment.getProperty("status.notes.success")));
			}
	}
	public Response addReminder(String token, int noteId, String reminder) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional<Notes> note = noteRepository.findById(noteId);
		if(!note.isPresent())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.noteDoesNotExist "),Integer.parseInt(environment.getProperty("status.notes.failure")));
		note.get().setReminder(reminder);
		noteRepository.save(note.get());
		return StatusHelper.statusInfo("Reminder Set SuccssFully", Integer.parseInt(environment.getProperty("status.notes.success")));
		
	}
	public Response removeReminder(String token, int noteId) throws UnsupportedEncodingException 
	{
		int id = UserToken.tokenVerify(token);
		Optional<Notes> note = noteRepository.findById(noteId);
		if(!note.isPresent())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.noteDoesNotExist "),Integer.parseInt(environment.getProperty("status.notes.failure")));
		note.get().setReminder(null);
		noteRepository.save(note.get());
		return StatusHelper.statusInfo("Reminder Removed SuccssFully", Integer.parseInt(environment.getProperty("status.notes.success")));
	}
	public String getReminder(String token, int noteId) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional<Notes> note = noteRepository.findById(noteId);
		if(!note.isPresent())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.noteDoesNotExist "),Integer.parseInt(environment.getProperty("status.notes.failure")));
		String reminder = note.get().getReminder();
		System.out.println("NoteId:="+note.get().getId()+"Reminder:="+reminder);
		return reminder;
	
	}
}
	
	
