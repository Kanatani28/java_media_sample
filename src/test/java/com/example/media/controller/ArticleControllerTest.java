package com.example.media.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.example.media.MediaApplication;
import com.example.media.config.DataSourceConfigForTest;
import com.example.media.config.StandaloneMvcTestViewResolver;
import com.example.media.model.Article;
import com.example.media.service.ArticleService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
  MediaApplication.class, 
  DataSourceConfigForTest.class
})
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
class ArticleControllerTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private DataSourceConfigForTest testConfig;

    @Autowired
    private ArticleService articleService;
    
    @Autowired
    ArticleController target;
    
    @BeforeAll
    void setUpBeforeClass() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
        		.setViewResolvers(new StandaloneMvcTestViewResolver())
        		.build();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class ArticleList {
        final int DEFAULT_PAGE_NUM = 1;
        final int EXPECTED_PAGER = 3;
        
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
        @DisplayName("パラメータなしで記事一覧へアクセス")
        void test() throws Exception {
            mockMvc.perform(get("/articles/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("/articles/list"))
            .andExpect(model().attributeExists("articles"))
            .andExpect(model().attribute("pagerCount", EXPECTED_PAGER))
            .andExpect(model().attribute("currentPage", DEFAULT_PAGE_NUM))
            .andExpect(model().hasNoErrors());
        }
        @Test
        @DisplayName("正常なパラメータで記事一覧へアクセス")
        void test2() throws Exception {
        	final int PAGE_NUM = 2;
            mockMvc.perform(get("/articles/list?page=" + PAGE_NUM))
            .andExpect(status().isOk())
            .andExpect(view().name("/articles/list"))
            .andExpect(model().attributeExists("articles"))
            .andExpect(model().attribute("pagerCount", EXPECTED_PAGER))
            .andExpect(model().attribute("currentPage", PAGE_NUM))
            .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("不正なパラメータで記事一覧へアクセス(文字列)")
        void test3() throws Exception {
        	final String PAGE_NUM = "a";
            mockMvc.perform(get("/articles/list?page=" + PAGE_NUM))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/articles/list"))
            .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("不正なパラメータで記事一覧へアクセス(0)")
        void test4() throws Exception {
        	final int PAGE_NUM = 0;
            mockMvc.perform(get("/articles/list?page=" + PAGE_NUM))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/articles/list"))
            .andExpect(model().hasNoErrors());
        }
        
        @Test
        @DisplayName("不正なパラメータで記事一覧へアクセス(マイナス)")
        void test5() throws Exception {
        	final int PAGE_NUM = -1;
            mockMvc.perform(get("/articles/list?page=" + PAGE_NUM))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/articles/list"))
            .andExpect(model().hasNoErrors());
        }
    }

}
