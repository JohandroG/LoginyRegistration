package com.lyr.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lyr.models.User;
import com.lyr.services.UserService;

@Controller
public class UsersController {
    private final UserService us;
    
    public UsersController(UserService us) {
        this.us = us;
    }
    
//---------------------------------------------------------------------------------
    @RequestMapping("/")
    public String Home(@ModelAttribute("user") User user) {
        return "redirect:/registration";
    }
    
    
    @RequestMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "Registration.jsp";
    }
    @RequestMapping("/login")
    public String login() {
        return "Login.jsp";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String registerUser(@RequestParam (value = "email") String email,
					    		@RequestParam (value = "password") String password,
					    		@RequestParam (value = "confirmpassword") String conf,
					    		HttpSession session,RedirectAttributes redirectAttributes) {
    	
    	List<User> match = us.getlistofUsersByEmail(email);
    	
    	if( match.size() > 0 ) {
    		redirectAttributes.addFlashAttribute("errorMessage", "That email already in use!");
			return "redirect:/registration";
    	}
	    if (password.equals( conf )) {
	    	Integer  u = us.registerUser(email, password);
	    	session.setAttribute("user_id", u);
	    	return "redirect:/home";
	    }
	    else {
	    	redirectAttributes.addFlashAttribute("errorMessage","The password doesn't match");
	    	return "redirect:/registration";
	    }
		
 
    }
    
    @RequestMapping(value="/log/in", method=RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        
    	User current = us.getUserByEmail(email);
    	
    	
    	
    		if( us.validateUser(current, password) ) {
    			User u = us.getUserByEmail(email);
		    	session.setAttribute("user_id", u.getId());
		    	return "redirect:/home";
			}
			else {
				
		    	redirectAttributes.addFlashAttribute("lerrorMessage", "The password for this email is incorrect");
		    	return "redirect:/login";
    	}
    	
    	
    }
    
    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
    	
    	Long user_id =  (Long) session.getAttribute("user_id");
    	
    	User current = us.findUsingID(user_id);
    	
    	model.addAttribute("userInfo", current );
    	return "Home.jsp";
    }
    
    @RequestMapping("/logout")
    public String logout( HttpSession session ) {
		session.removeAttribute("user_id");
		return "redirect:/login";
	}
}
