package baitap10.controller;

import baitap10.entity.User;
import baitap10.service.IUserService;
import jakarta.servlet.http.HttpSession;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Access denied or invalid credentials");
        }
        return "login";
    }

    @PostMapping("/perform_login")
    public String performLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,   
                               ModelMap model) {
        User user = userService.findByUsernameAndPassword(username, password);
        if (user != null) {
            session.setAttribute("username", username); 
            session.setAttribute("role", user.getRole());

            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/home";
            } else if ("USER".equals(user.getRole())) {
                return "redirect:/user/home";
            }
        }
        return "redirect:/login?error=true";
    }
}
