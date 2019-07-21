package com.example.media.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.media.MediaApplication;
import com.example.media.config.DataSourceConfigForTest;
import com.example.media.config.StandaloneMvcTestViewResolver;
import com.example.media.form.ArticleForm;

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
    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class ArticleDetail {
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
        @DisplayName("存在するIDをパラメータにしてアクセス")
        void test() throws Exception {
        	final int ID = 1;
            mockMvc.perform(get("/articles/detail/" + ID))
                .andExpect(status().isOk())
                .andExpect(view().name("/articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attribute("article", is(notNullValue())))
                .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("存在しないIDをパラメータにしてアクセス")
        void test2() throws Exception {
        	final int ID = 99;
            mockMvc.perform(get("/articles/detail/" + ID))
                .andExpect(status().isOk())
                .andExpect(view().name("/articles/detail"))
                .andExpect(model().attribute("article", is(nullValue())))
                .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("数値以外のIDをパラメータにしてアクセス")
        void test3() throws Exception {
        	final String ID = "a";
            mockMvc.perform(get("/articles/detail/" + ID))
                .andExpect(status().isOk())
                .andExpect(view().name("/articles/detail"))
                .andExpect(model().attribute("article", is(nullValue())))
                .andExpect(model().hasNoErrors());
        }

        @Test
        @Disabled
        @DisplayName("IDを指定せずアクセス")
        void test4() throws Exception {
        	// TODO
            mockMvc.perform(get("/articles/detail"))
            .andExpect(status().is5xxServerError())
            ;
//            .andExpect(view().name("/articles/detail"))
//            .andExpect(model().attributeExists("article"))
//            .andExpect(model().attribute("article", is(nullValue())))
//            .andExpect(model().hasNoErrors());
        }
    }
    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class ArticleFormTest {
        @BeforeAll
        void setup() throws IOException, SQLException {
            testConfig.setupDatabase();
        }
        @AfterAll
        void tearDown() throws SQLException, IOException {
            testConfig.setupDatabase();    
        }
        
        @Test
        @DisplayName("アクセス")
        void test() throws Exception {
        	MvcResult result = mockMvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("/articles/form"))
                .andExpect(model().attributeExists("articleForm"))
                .andExpect(model().attribute("articleForm", is(notNullValue())))
                .andExpect(model().attribute("isEdit", false))
                .andExpect(model().hasNoErrors())
                .andReturn();
        	
        	ArticleForm form = (ArticleForm) result.getModelAndView().getModel().get("articleForm");
        	assertNull(form.getId());
        	assertNull(form.getBody());
        	assertNull(form.getTitle());
        }
    }
    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class ArticleEditTest {
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
        @DisplayName("存在するIDを指定してアクセス")
        void test() throws Exception {
        	final int ID = 1;
        	MvcResult result = mockMvc.perform(get("/articles/edit/" + ID))
                .andExpect(status().isOk())
                .andExpect(view().name("/articles/form"))
                .andExpect(model().attributeExists("articleForm"))
                .andExpect(model().attribute("articleForm", is(notNullValue())))
                .andExpect(model().attribute("isEdit", true))
                .andExpect(model().hasNoErrors())
                .andReturn();
        	
        	ArticleForm form = (ArticleForm) result.getModelAndView().getModel().get("articleForm");
        	assertEquals(form.getId(), String.valueOf(ID));
        	assertTrue(StringUtils.isNotEmpty(form.getTitle()));
        	assertTrue(StringUtils.isNotEmpty(form.getBody()));
        }
        
        @Test
        @DisplayName("存在しないIDを指定してアクセス")
        void test2() throws Exception {
        	final int ID = 99;
        	mockMvc.perform(get("/articles/edit/" + ID))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/articles/form"))
                .andExpect(model().hasNoErrors());
        }
        
        @Test
        @DisplayName("不正なIDを指定してアクセス(文字列)")
        void test3() throws Exception {
        	final String ID = "aaa";
        	mockMvc.perform(get("/articles/edit/" + ID))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/articles/form"))
                .andExpect(model().hasNoErrors());
        }
        
        @Test
        @DisplayName("不正なIDを指定してアクセス(0)")
        void test4() throws Exception {
        	final int ID = 0;
        	mockMvc.perform(get("/articles/edit/" + ID))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/articles/form"))
                .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("不正なIDを指定してアクセス(マイナス)")
        void test5() throws Exception {
        	final int ID = -1;
        	mockMvc.perform(get("/articles/edit/" + ID))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/articles/form"))
                .andExpect(model().hasNoErrors());
        }
    }

}
