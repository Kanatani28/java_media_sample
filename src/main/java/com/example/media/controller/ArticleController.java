package com.example.media.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.media.form.ArticleForm;
import com.example.media.model.Admin;
import com.example.media.model.Article;
import com.example.media.security.SecurityUser;
import com.example.media.service.ArticleService;
import com.example.media.validator.ArticleFormValidator;

@Controller
@RequestMapping("/articles")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ArticleFormValidator articleFormValidator;
	
	@InitBinder("articleForm")
	public void validatorBinder(WebDataBinder binder) {
		binder.addValidators(articleFormValidator);
	}

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
	
	@GetMapping("/form")
	public String form(Model model) {
		
		ArticleForm articleForm;
		if (!model.containsAttribute("articleForm")) {
			articleForm = new ArticleForm();
			model.addAttribute("articleForm", articleForm);
		} else {
			articleForm = (ArticleForm) model.asMap().get("articleForm");
		}
		
		return "/articles/form";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute("articleForm") @Valid ArticleForm articleForm, 
			RedirectAttributes redirectAttributes, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "articleForm",
					bindingResult);
			redirectAttributes.addFlashAttribute("articleForm", articleForm);
			return "redirect:/articles/form";
		}
		
		Article article = new Article();
		BeanUtils.copyProperties(articleForm, article);
		article.setAuthorId(((Admin) SecurityUser.getCurrentLoginUser()).getId());
		
		articleService.add(article);
		
		return "redirect:/articles/list";
	}
}
