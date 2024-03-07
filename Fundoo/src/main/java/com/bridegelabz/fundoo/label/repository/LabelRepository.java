package com.bridegelabz.fundoo.label.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bridegelabz.fundoo.label.model.Label;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.user.model.User;
@Repository
public interface LabelRepository extends CrudRepository<Label, Integer> 
{
	 public Optional<Label> findById(int id);
	 public Optional<Label> findByname(String name);
	 public List<Label> findNotesByUser(User user);
}
