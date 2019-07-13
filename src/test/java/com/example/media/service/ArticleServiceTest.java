package com.example.media.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.media.MediaApplication;
import static com.example.media.Constants.*;
import com.example.media.config.DataSourceConfigForTest;
import com.example.media.model.Article;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
  MediaApplication.class, 
  DataSourceConfigForTest.class
})
@ActiveProfiles("test")
class ArticleServiceTest {
    
    @Autowired
    private DataSourceConfigForTest testConfig;
    
    @Autowired
    private ArticleService articleService;
    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class Find12Records {
        
        @BeforeAll
        void setup() throws IOException, SQLException {
            testConfig.setUpDatabase();    
            testConfig.setUpData("datasets/articles/find/articles.yml");
        }
        @AfterAll
        void tearDown() throws SQLException, IOException {
            testConfig.setUpDatabase();    
        }
        
        @Test
        @DisplayName("12件のデータ登録がある場合の表示項目")
        void test1() {
        	final int DATA_COUNT = 12;
        	
            List<Article> articlesActual = null;
            List<Article> articlesExpected = null;
            int[] expectedSize = {ARTICLES_PER_PAGE_COUNT, ARTICLES_PER_PAGE_COUNT, DATA_COUNT % ARTICLES_PER_PAGE_COUNT, 0};
            articlesActual = articleService.findPerPage(1);
            assertEquals(articlesActual.size(), ARTICLES_PER_PAGE_COUNT);
            
            // test in 4 times 
            for(var i = 0; i < 4; i++) {
            	int pageNum = i + 1;
            	articlesActual = articleService.findPerPage(pageNum);
            	// check size
                assertEquals(articlesActual.size(), expectedSize[i]);

                articlesExpected = articlesActual.stream()
                		.sorted(Comparator.comparing(Article::getCreatedAt).reversed()).collect(Collectors.toList());                	
                for(var j = 0; j < articlesActual.size(); j++) {
                	assertEquals(articlesExpected.get(j).getId(), articlesActual.get(j).getId());
                }
            }
            
            // pager count
            int pagerCount = articleService.findPagerCount();
            assertEquals(DATA_COUNT / ARTICLES_PER_PAGE_COUNT + 1, pagerCount);
        }
    }
    
    @Nested
    @SpringBootTest
    class Add {
        void test1() {
            
        }
    }
}
