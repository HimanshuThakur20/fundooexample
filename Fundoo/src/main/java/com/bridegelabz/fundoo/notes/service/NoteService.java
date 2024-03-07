package com.bridegelabz.fundoo.notes.service;
import java.io.UnsupportedEncodingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import  org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridegelabz.fundoo.elasticsearch.ElasticSearchImpl;
import com.bridegelabz.fundoo.elasticsearch.IElasticSearch;
import com.bridegelabz.fundoo.exception.CreateNoteExceptions;
import com.bridegelabz.fundoo.notes.dto.NotesDto;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.notes.repository.NoteRepository;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.user.model.Email;
import com.bridegelabz.fundoo.user.model.User;
import com.bridegelabz.fundoo.user.repository.UserRepository;
import com.bridegelabz.fundoo.user.services.EmailService;
import com.bridegelabz.fundoo.util.GenerateMail;
import com.bridegelabz.fundoo.util.NoteContainer;
import com.bridegelabz.fundoo.util.NoteOperation;
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
	private UserRepository userRepositpory;
	@Autowired
	private Environment environment;
	@Autowired
	private EmailService emailService;
	@Autowired
	private IElasticSearch elasticSearch;
	
	@Autowired
	private ElasticSearchImpl elasticImpl;
	@Autowired
	private GenerateMail rabbitMqService;
	@Autowired
	private NoteContainer noteContainer;
	public Response createNote(NotesDto noteDto, String token) throws UnsupportedEncodingException
	{
		if(noteDto.getTitle().isEmpty() || noteDto.getDescription().isEmpty())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		
		Notes notes = modelMapper.map(noteDto, Notes.class);
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
				System.out.println("title="+notes.getTitle());
				notes.setUser(user.get());
				notes.setCreatedDateAndTime(LocalDateTime.now());
				Notes savedNote = noteRepository.save(notes);
//				elasticSearch.save(savedNote);
				
				noteContainer.setNote(notes);
				noteContainer.setNoteOperation(NoteOperation.CREATE);
				elasticSearch.create(notes);
				//rabbitMqService.sendNote(noteContainer);
//				rabbitMqService.operation(noteContainer);
				return StatusHelper.statusInfo(environment.getProperty("status.notes.noteCreated"), Integer.parseInt(environment.getProperty("status.notes.success")));
		}	
		
		return null;
	}
	public Response updateNote(NotesDto noteDto, String token, int noteId) throws Exception
	{
		if(noteDto.getTitle().isEmpty() && noteDto.getDescription().isEmpty())
			throw new CreateNoteExceptions(environment.getProperty("throw new CreateNoteExceptions"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		int userId = UserToken.tokenVerify(token);
	    Optional <User> user = userRepositpory.findById(userId); 
	    if(user.isPresent())
	    {
	    	Optional <Notes> notes = noteRepository.findById(noteId);
	    	int existingUserId = notes.get().getUser().getId();
	    	if(userId == existingUserId)
	    	{
	    		notes.get().setModefiedDateTime(LocalDateTime.now());
	    		notes.get().setTitle(noteDto.getTitle());
	    		notes.get().setDescription(noteDto.getDescription());
				noteRepository.save(notes.get());
				noteContainer.setNote(notes.get());
				noteContainer.setNoteOperation(NoteOperation.UPDATE);
				elasticSearch.updateNote(notes.get());
				//rabbitMqService.sendNote(noteContainer);
//				rabbitMqService.operation(noteContainer);
	    	}
	    		
	    }
	    else
	    {
	    	throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
	    }
	    return StatusHelper.statusInfo(environment.getProperty("status.notes.notesUpdated"), Integer.parseInt(environment.getProperty("status.notes.success")));
	}
	public Response deleteNote(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
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
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
		
	}
	public Response deleteNotePermanently(String token, int noteId) throws Exception 
	{
		int id = UserToken.tokenVerify(token);
		Optional <User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
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
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
		
	}
//	public List<Notes>  searchNote(String query, String token) throws UnsupportedEncodingException {
//		int userId = UserToken.tokenVerify(token);
//		List<Notes> data = elasticSearch.searchData(query, userId);
////		System.out.println("data" + data);
//		return data;
//	}
	
	public List<Notes> searchNotes(String query,String token) throws UnsupportedEncodingException 
	{
		int userId = UserToken.tokenVerify(token);
		List<Notes> data = elasticSearch.searchData(query, userId);
		System.out.println("data" + data);
		return data;
//		Map<String,Float> fields = new HashMap<>();
//		fields.put("title", 3.0f);
//		fields.put("description", 2.0f);
//
//		List<Notes> searchedNotes = elasticSearchService.searchedNotes("elasticdb", "note_info", fields, searchText,userId );
	}
	public List<Notes> getAllNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional <User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
			List<Notes> notes = noteRepository.findNotesByUser(user.get());
			List<Notes> listOfNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isArchive() == false && userNotes.isTrash() == false && userNotes.isPin()==false) {
					listOfNotes.add(userNotes);
			}
		}
		return listOfNotes;
		}	
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
		
	}
	public List<Notes> getAllTrashedNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional <User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
			List<Notes> notes = noteRepository.findNotesByUser(user.get());
			List<Notes> listOfTrashedNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isTrash() == true) {
					listOfTrashedNotes.add(userNotes);
			}
		}
		return listOfTrashedNotes;
		}	
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
		
	}
	public List<Notes> getAllPinnedNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional <User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
			List<Notes> notes = noteRepository.findNotesByUser(user.get());
			List<Notes> listOfPinnedNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isPin() == true) {
					listOfPinnedNotes.add(userNotes);
			}
		}
		return listOfPinnedNotes;
		}	
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
		
	}
	public List<Notes> getAllArchivedNotes(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional <User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
			List<Notes> notes = noteRepository.findNotesByUser(user.get());
			List<Notes> listOfArchivedNotes = new ArrayList<>();
			for(Notes userNotes : notes) 
			{
				if(userNotes.isArchive() == true && userNotes.isTrash()==false && userNotes.isPin()==false) {
					listOfArchivedNotes.add(userNotes);
			}
		}
		return listOfArchivedNotes;
		}	
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
		
	}
	public Response pin(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
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
	else
	{
		throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));

	}
  }		
	public Response archive(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
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
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));

		}
		
	}
	public Response Trash(String token, int noteId) throws Exception
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
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
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));

		}
	}
	
	public Response changeColor(String token,String color, int noteId) throws UnsupportedEncodingException {
		int id = UserToken.tokenVerify(token);
		Optional <User> user = userRepositpory.findById(id);
		List<Notes> notes = noteRepository.findNotesByUser(user.get());
		Notes note = notes.stream().filter(data-> data.getId()== noteId).findFirst().get();
		System.out.println(note);
		note.setColor(color);
		noteRepository.save(note);
		return StatusHelper.statusInfo("color change", 200);
		
	}
	public Response collaborate(String token, String emailId, int noteId) throws UnsupportedEncodingException
	{
		Email email = new Email();
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
			Optional <User> receiverUser = userRepositpory.findByEmailId(emailId);
			if(receiverUser.isPresent())
			{
				if(user.get().getEmailId().equals(receiverUser))
				{
					System.out.println("Inside user Already exist");
					throw new CreateNoteExceptions(environment.getProperty("status.notes.userAlreadyExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
				}
				else
				{
					Optional <Notes> note = noteRepository.findById(noteId);
					if(note.isPresent())
					{
						if(user.get().getCollaboratedNotes().contains(note.get()))
						{
							throw new CreateNoteExceptions("User is Already Collaborated", Integer.parseInt(environment.getProperty("status.notes.failure")));
						}
						note.get().getCollaboratedUser().add(receiverUser.get());
						receiverUser.get().getCollaboratedNotes().add(note.get());
						noteRepository.save(note.get());
						userRepositpory.save(user.get());
						email.setFrom(user.get().getEmailId());
						System.out.println(user.get().getEmailId());
						email.setSubject("Note Collaborated");
						email.setTo(receiverUser.get().getEmailId());
						System.out.println(receiverUser.get().getEmailId());
						email.setBody("Note Collaborated to You!");
						emailService.sendMail(email);
						System.out.println("Mail Sent "+user.get().getCollaboratedNotes());
						return StatusHelper.statusInfo(environment.getProperty("status.notes.collaborated"), Integer.parseInt(environment.getProperty("status.notes.success")));
					}
					else
					{
						throw new CreateNoteExceptions(environment.getProperty("status.notes.noteDoesNotExist "),Integer.parseInt(environment.getProperty("status.notes.failure")));
					}
				}
			}	
			else
			{
				throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
			}
		}
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
	}
	public Response removeCollaborator(String token, String emailId, int noteId) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
			Optional <User> receiverUser = userRepositpory.findByEmailId(emailId);
			if(receiverUser.isPresent())
			{
				Optional <Notes> note = noteRepository.findById(noteId);
				if(note.isPresent())
				{
					System.out.println("Inside Remove Collaborator");
					note.get().getCollaboratedUser().remove(receiverUser.get());
					user.get().getCollaboratedNotes().remove(note.get());
					System.out.println("Removed "+note.get().getCollaboratedUser());
					userRepositpory.save(receiverUser.get());
					noteRepository.save(note.get());
					return StatusHelper.statusInfo(environment.getProperty("status.notes.removeCollaborator"), Integer.parseInt(environment.getProperty("status.notes.success")));
				}
				else
				{
					throw new CreateNoteExceptions(environment.getProperty("status.notes.noteDoesNotExist "),Integer.parseInt(environment.getProperty("status.notes.failure")));
				}
			}	
			else
			{
				throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
			}
		}
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
	}
	public Set<Notes> getAllCollaboratedNotes(String token) throws UnsupportedEncodingException 
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
				System.out.println("inside if of user");
				System.out.println("Inside if of another user");
				System.out.println(user.get().getCollaboratedNotes());
				return user.get().getCollaboratedNotes();
		}
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
	}
	public Set<User> getAllCollaboratedUser(String token, int noteId) throws UnsupportedEncodingException 
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(user.isPresent())
		{
			System.out.println("inside if of user");
			Optional<Notes> note = noteRepository.findById(noteId);
			if(note.isPresent())
			{
				System.out.println("Inside if of another user");
				System.out.println("MyNotes="+ note.get().getCollaboratedUser());
				return note.get().getCollaboratedUser();
				
			}
			else
			{
				throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
			}
		}
		else
		{
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		}
	}
	
	public Response addReminder(String token, int noteId, String reminder) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepositpory.findById(id);
		if(!user.isPresent())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
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
		Optional<User> user = userRepositpory.findById(id);
		if(!user.isPresent())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
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
		Optional<User> user = userRepositpory.findById(id);
		if(!user.isPresent())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.userNotExist"),Integer.parseInt(environment.getProperty("status.notes.failure")));
		Optional<Notes> note = noteRepository.findById(noteId);
		if(!note.isPresent())
			throw new CreateNoteExceptions(environment.getProperty("status.notes.noteDoesNotExist "),Integer.parseInt(environment.getProperty("status.notes.failure")));
		String reminder = note.get().getReminder();
		System.out.println("NoteId:="+note.get().getId()+"Reminder:="+reminder);
		return reminder;
	
	}
}
	
	

