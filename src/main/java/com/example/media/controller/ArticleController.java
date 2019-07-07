package com.example.media.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.media.model.Article;
import com.example.media.service.ArticleService;

@Controller
@RequestMapping("/articles")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;

	@GetMapping("/list")
	public String displayAll(@RequestParam(name = "page", defaultValue = "1") String page, Model model) {
		
		if(!StringUtils.isNumeric(page) || "0".equals(page)) {
			return "redirect:/articles/list";
		}
		
		int pageNum = Integer.parseInt(page);
		List<Article> articles = articleService.findPerPage(pageNum);
		int pagerCount = articleService.findPagerCount();
		
		model.addAttribute("articles", articles);
		model.addAttribute("pagerCount", pagerCount);
		model.addAttribute("currentPage", pageNum);
		
		return "/articles/list";
	}
}
