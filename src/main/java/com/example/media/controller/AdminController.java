package com.example.media.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.media.form.AdminForm;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    HttpSession session;
    
    @GetMapping("form")
    public String edit(Model model) {
    	AdminForm adminForm;
    	if(!model.containsAttribute("articleForm")) {
            adminForm = new AdminForm();
            model.addAttribute("adminForm", adminForm);
            model.addAttribute("isEdit", false);
    	}
    	return "/admin/form";
    }
}
