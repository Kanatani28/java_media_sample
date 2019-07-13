package com.example.media.aspect;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.media.security.SecurityUser;

@ControllerAdvice
public class SetUpLoginUserControllerAdvice {
    
    @ModelAttribute
    public void setUpLoginUser(Model model, Authentication auth) {
        if (auth == null) {
            return;
        }
        SecurityUser springSecuirityUser = (SecurityUser) auth.getPrincipal();
        model.addAttribute("loginUser", springSecuirityUser.getLoginUser());
    }
}
