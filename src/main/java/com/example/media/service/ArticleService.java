package com.example.media.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.media.mapper.ArticleMapper;
import com.example.media.model.Article;
import static com.example.media.Constants.*;

@Service
@Transactional
public class ArticleService {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	public List<Article> findAll() {
		return articleMapper.selectAll();
	}
	
	public List<Article> findPerPage(int pageNum) {

		int offset = (pageNum - 1) * ARTICLES_PER_PAGE_COUNT;
		return articleMapper.selectPerPage(offset);
	}
	
	public int findPagerCount() {
		
		int articleCount = articleMapper.countAll();
		
		int pagerCount = 0;
		
		if(articleCount % ARTICLES_PER_PAGE_COUNT > 0) {
			pagerCount = articleCount / ARTICLES_PER_PAGE_COUNT + 1;
		} else {
			pagerCount = articleCount / ARTICLES_PER_PAGE_COUNT;
		}
		
		return  pagerCount;
	}
	
	public int add(Article article) {
		return articleMapper.insert(article);
	}

}
