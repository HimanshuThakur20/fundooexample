package com.bridegelabz.fundoo.user.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bridegelabz.fundoo.user.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> 
{
	public Optional<User> findByEmailId(String userName);
	public Optional<User> findById(int id);
}
