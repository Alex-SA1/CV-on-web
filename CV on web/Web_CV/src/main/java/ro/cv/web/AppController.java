package ro.cv.web;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AppController {
    
	
	@Autowired
	 private UserRepository repo;
	
	  
    
    
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
         
        return "signup_form";
    }
    
    @PostMapping("/process_register")
    public String processRegister(User user) {
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
    	String encodedPassword = encoder.encode(user.getPassword());
    	user.setPassword(encodedPassword);
    	
         repo.save(user);
         
        return "register_success";
    }
    
      
    @GetMapping("/homepage")
    public String homePage(Model model) {
         return "homepage";
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
        	return "login";
        }
        return "redirect:/";
    }
   
    
    /*
    @GetMapping("/comments")
    public String commentPage() {
    	return "comments";
    }*/
    
    @Autowired
	private CommentService commentService;
	
   
	
	
	@GetMapping("/showNewCommentForm")
	public String showNewEmployeeForm(Model model) {
		// create model attribute to bind form data
		Comment comment = new Comment();
		model.addAttribute("comment", comment);
		return "new_comment";
	}
	
	
	@PostMapping("/saveComment")
	public String saveComment(@ModelAttribute("comment") Comment comment) {
		// save comment to database
		
		commentService.saveComment(comment);
		return "redirect:/comments";
	}
	
    @GetMapping("/comments")
    public String viewComments(Model model) {
    	model.addAttribute("listComments", commentService.getAllComments());
    	
    	return "comments";
    }
	
	

	
	
	/*
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		// get comment from the service
		Comment comment = commentService.getCommentById(id);
		
		model.addAttribute("comment", comment);
		return "update_comment";
	}*/
	
	/*
	@GetMapping("/deleteComment/{id}")
	public String deleteComment(@PathVariable (value = "id") long id) {
		
		// call delete employee method 
		this.commentService.deleteCommentById(id);
		return "redirect:/";
	}*/
	
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<Comment> page = commentService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Comment> listComments = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listComments", listComments);
		return "comments";
	}
  
    
    
  
    
}
