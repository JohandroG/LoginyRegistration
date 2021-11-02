package com.lyr.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.lyr.models.User;

public interface UsersRepository extends CrudRepository <User,Long> {

// --------------------------------- Java ----------------------------------
	List<User> findAll();
	
	
	
	
//--------------------------------- Query ---------------------------------
	
	@Query( "SELECT u FROM User u WHERE id = ?1" )
	User findbyId( Long id );
	
	@Query( "SELECT u FROM User u WHERE email = ?1" )
	List<User> selectUsersByEmail( String email );
	
	@Query( "SELECT u FROM User u WHERE email = ?1" )
	User selectUserByEmail( String email );
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO users(email, password) " +
	"VALUES(?1, ?2)", nativeQuery=true)
	Integer insertNewUser( String email, String encryptedpassword);
}
