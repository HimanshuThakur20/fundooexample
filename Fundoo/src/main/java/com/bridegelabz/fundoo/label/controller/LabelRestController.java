package com.bridegelabz.fundoo.label.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridegelabz.fundoo.label.dto.LabelDto;
import com.bridegelabz.fundoo.label.model.Label;
import com.bridegelabz.fundoo.label.repository.LabelRepository;
import com.bridegelabz.fundoo.label.service.LabelService;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.user.services.UserService;
@CrossOrigin( origins = "*", allowedHeaders = "*")
@RestController
public class LabelRestController 
{
	@Autowired
	LabelService labelService;
	@PostMapping("/createlabel")
	public ResponseEntity<Response> createLabel(@RequestParam String token,@RequestBody LabelDto labelDto) throws UnsupportedEncodingException
	{
		Response response = labelService.createLabel(labelDto, token);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	@PutMapping("/updatelabel")
	public ResponseEntity<Response> updateLabel(@RequestParam String token, @RequestParam int labelId, @RequestBody LabelDto labelDto) throws UnsupportedEncodingException
	{
		System.out.println("inside update hit backend");
		Response response = labelService.updateLabel(labelDto, token, labelId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	@PutMapping("/deletelabel")
	public ResponseEntity<Response> deleteLabel(@RequestParam String token, @RequestParam int labelId) throws UnsupportedEncodingException
	{
		Response response = labelService.deleteLabel(token, labelId);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
	}
	@PostMapping("/addlabeltonote")
	public ResponseEntity<Response> addLabelToNote(@RequestBody LabelDto labelDto, @RequestParam String token, @RequestParam int noteId) throws UnsupportedEncodingException
	{
		Response response = labelService.addLabelToNote(labelDto, token, noteId);
		return new ResponseEntity<Response> (response, HttpStatus.OK);
		
	}
	@PutMapping("/removelabelfromnote")
	public ResponseEntity<Response> removeLabelFromNote(@RequestParam String token, @RequestParam int noteId, @RequestParam int labelId) throws UnsupportedEncodingException
	{
		Response response = labelService.removeLabelFromNote(token, noteId, labelId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	@GetMapping("/getalllabels")
	public List<Label> getAllLabels(@RequestParam String token) throws UnsupportedEncodingException
	{
		List<Label> listOfLabels = labelService.getAllLabels(token);
		return listOfLabels;	
	}
	@GetMapping("/getalllabelsofnote")
	public List<Label> getAllLebelsOfNote(@RequestParam String token, @RequestParam int noteId) throws UnsupportedEncodingException
	{
		List<Label> listOfLabels = labelService.getLebelsOfNote(token, noteId);
		return listOfLabels;
	}
}
