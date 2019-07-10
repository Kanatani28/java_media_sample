package com.example.media.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.media.MediaApplication;
import com.example.media.config.DataSourceConfigForTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
  MediaApplication.class, 
  DataSourceConfigForTest.class
})
@ActiveProfiles("test")
class ArticleServiceTest {
	
	@Autowired
	private ArticleService articleService;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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

	@Test
	void test() {
		assertEquals(2, 1 + 1);
	}
	
	@Nested
    @SpringBootTest
    class Find {

        @BeforeEach
        void setup() { 
        	
        }

        @AfterEach
        void teardown() { 
        	
        }

        @Test
        @DisplayName("12件のデータ登録時の１ページ目")
        void test1() {
            assertEquals(articleService.findPerPage(1).size(), 0);
        }
    }

}
