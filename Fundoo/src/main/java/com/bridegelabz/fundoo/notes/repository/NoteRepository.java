package com.bridegelabz.fundoo.notes.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.user.model.User;
@Repository
public interface NoteRepository extends  CrudRepository<Notes, Integer>
{
	 public Optional<Notes> findById(int id);
//	 @Query(value="select * from notes where user_id:=user_id",nativeQuery=true)
//	 public List<Notes> findNotesByUserId(@Param("user_id")int userId);
	 
	 public List<Notes> findNotesByUser(User user);
}
