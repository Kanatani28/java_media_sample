package com.example.media.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
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
    class Find0Records {
        
        @Test
        @DisplayName("0件のデータ登録がある場合の表示項目")
        void test1() {
            List<Article> articlesActual = null;
            int expectedSize = 0;
            articlesActual = articleService.findPerPage(1);
            assertEquals(expectedSize, articlesActual.size());
            
            // pager count
            int pagerCount = articleService.findPagerCount();
            assertEquals(expectedSize, pagerCount);
        }
    }
    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class Find10Records {
        @BeforeAll
        void setup() throws IOException, SQLException {
            testConfig.setupDatabase();
            testConfig.setUpData("datasets/articles/find/articles01.yml");
        }
        @AfterAll
        void tearDown() throws SQLException, IOException {
            testConfig.setupDatabase();    
        }
        
        @Test
        @DisplayName("10件のデータ登録がある場合の表示項目")
        void test1() {
        	final int DATA_COUNT = 10;
        	
            List<Article> articlesActual = null;
            List<Article> articlesExpected = null;
            int[] expectedSize = {ARTICLES_PER_PAGE_COUNT, ARTICLES_PER_PAGE_COUNT, 0};
            
            // test in 2 times 
            for(var i = 0; i < 3; i++) {
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
            assertEquals(DATA_COUNT / ARTICLES_PER_PAGE_COUNT, pagerCount);
        }
    }
    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class Find12Records {
        
    	@BeforeAll
        void setup() throws IOException, SQLException {
            testConfig.setupDatabase();
            testConfig.setUpData("datasets/articles/find/articles02.yml");
        }
        @AfterAll
        void tearDown() throws SQLException, IOException {
            testConfig.setupDatabase();    
        }
        
        @Test
        @DisplayName("12件のデータ登録がある場合の表示項目")
        void test1() {
        	final int DATA_COUNT = 12;
        	
            List<Article> articlesActual = null;
            List<Article> articlesExpected = null;
            int[] expectedSize = {ARTICLES_PER_PAGE_COUNT, ARTICLES_PER_PAGE_COUNT, DATA_COUNT % ARTICLES_PER_PAGE_COUNT, 0};
            
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
    @TestInstance(Lifecycle.PER_CLASS)
    class Find {
    	
    	@BeforeAll
        void setup() throws IOException, SQLException {
            testConfig.setupDatabase();
            testConfig.setUpData("datasets/articles/find/articles03.yml");
        }
        @AfterAll
        void tearDown() throws SQLException, IOException {
            testConfig.setupDatabase();    
        }
    	
        @Test
        @DisplayName("1件取得")
        void test1() throws IOException {
        	final String ARTICLE_ID = "1";
            Article articlesActual = articleService.find(ARTICLE_ID);
            
            Map<String, List<Map<String, Object>>> ymlData = testConfig.getYamlData("datasets/articles/find/articles03.yml");
            Map<String, Object> article = ymlData.get("articles").get(0);
            Map<String, Object> author = ymlData.get("users").get(0);

            assertEquals(article.get("author_id"), articlesActual.getAuthorId());
            assertEquals(author.get("name"), articlesActual.getAuthorName());
            assertEquals(article.get("body"), articlesActual.getBody());
            assertEquals(article.get("created_at"), articlesActual.getCreatedAtFormatted());
            assertEquals(article.get("id"), articlesActual.getId());
            assertEquals(article.get("title"), articlesActual.getTitle());
            assertNull(articlesActual.getDeletedAt());
            assertNull(articlesActual.getUpdatedAt());
            
        }
        
        @Test
        @DisplayName("削除日時があるデータは取得しない")
        void test2() {
            Article articlesActual = null;
            articlesActual = articleService.find("2");
            assertNull(articlesActual);
        }
        
        @Test
        @DisplayName("存在しないID")
        void test3() {
            Article articlesActual = null;
            articlesActual = articleService.find("3");
            assertNull(articlesActual);
        }

        @Test
        @DisplayName("IDが文字列")
        void test4() {
            Article articlesActual = null;
            articlesActual = articleService.find("a");
            assertNull(articlesActual);
        }
    }
    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class Add {
    	@BeforeAll
        void setup() throws IOException, SQLException {
            testConfig.setupDatabase();
            testConfig.setUpData("datasets/articles/add/user01.yml");
        }
        @AfterAll
        void tearDown() throws SQLException, IOException {
            testConfig.setupDatabase();    
        }
        
        @Test
        @DisplayName("記事を1件追加")
        void test1() throws IOException {
            Map<String, Object> articleMap = testConfig.getYamlData("datasets/articles/add/article01.yml").get("articles").get(0);
            Map<String, Object> userMap = testConfig.getYamlData("datasets/articles/add/user01.yml").get("users").get(0);
            
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration()
                    .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                    .setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
            Article article = mapper.map(articleMap, Article.class);
            
            articleService.add(article);
            
            Article actual = articleService.find("1");
            assertEquals(article.getId(), actual.getId());
            assertEquals(article.getAuthorId(), actual.getAuthorId());
            assertEquals(article.getBody(), actual.getBody());
            assertEquals(article.getTitle(), actual.getTitle());
            assertEquals(userMap.get("name"), actual.getAuthorName());
            assertNotNull(actual.getCreatedAt());
            assertNull(actual.getUpdatedAt());
            assertNull(actual.getDeletedAt());
        }
    }
}
