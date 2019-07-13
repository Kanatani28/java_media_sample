package com.example.media.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.media.Constants;
import com.example.media.form.LoginForm;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    HttpSession session;

    @GetMapping("/login")
    public String index(@ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult) {
        
        Optional<LoginForm> loginFormAtFailure = getLoginFormAtFailure();
        loginFormAtFailure.ifPresent(f -> {
             BeanUtils.copyProperties(f, loginForm);
             f.getLoginErrors().forEach(e -> bindingResult.addError(e));
        });  

        return "admin/index";
    }
    
    private Optional<LoginForm> getLoginFormAtFailure() {
        
        Object loginForm = session.getAttribute(Constants.SESSION_KEY_LOGIN_FORM);
        session.removeAttribute(Constants.SESSION_KEY_LOGIN_FORM);

        if (loginForm == null) {
            return Optional.empty();
        } else {
            return Optional.of((LoginForm) loginForm);
        }
    }
}
