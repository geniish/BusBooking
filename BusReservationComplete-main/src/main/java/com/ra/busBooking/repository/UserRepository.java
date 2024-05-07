package com.ra.busBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ra.busBooking.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String emailId);
	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = 2")
	List<User> findUsersByRoleIdTwo();
}
