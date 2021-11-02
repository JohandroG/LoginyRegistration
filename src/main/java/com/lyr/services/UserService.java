package com.lyr.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.lyr.models.User;
import com.lyr.repositories.UsersRepository;

@Service
public class UserService {

	public final UsersRepository ur;
	
	public  UserService(UsersRepository ur) {
        this.ur = ur;
    }
	
//---------------------------------------User------------------------------------
	
	public List<User> allUsers(){
		return ur.findAll();
	}
	
	public User findUsingID(Long id) {
		return ur.findbyId(id);
	}
	
	public List<User> getlistofUsersByEmail( String email ){
		return ur.selectUsersByEmail(email);
	}
	
	public User getUserByEmail( String email ){
		return ur.selectUserByEmail(email);
	}
	
	
	public Integer  registerUser( String email, String password ) {
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		return ur.insertNewUser(email, encryptedPassword);
	}
	
	public Boolean validateUser (User currentUser, String password){
		return BCrypt.checkpw( password, currentUser.getPassword() );
	}
	
}
