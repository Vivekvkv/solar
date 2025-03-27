package com.solcare.solar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solcare.solar.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	public Optional<User> findByEmail(String email);
//	public User findByEmail(String email);
	
//	@Query("select u from User as u where u.email = : email")
//	public User getUserByUserName(@Param("email") String email);
}
