package com.example.media.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.media.Constants;
import com.example.media.MediaApplication;
import com.example.media.config.DataSourceConfigForTest;
import com.example.media.model.Admin;
import com.example.media.security.SecurityUser;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
  MediaApplication.class, 
  DataSourceConfigForTest.class
})
@ActiveProfiles("test")
class LoginAdministratorDetailsServiceTest {
	
    @Autowired
    private DataSourceConfigForTest testConfig;
    
    @Autowired
    private LoginAdministratorDetailsService service;

    
    @Nested
    @SpringBootTest
    @TestInstance(Lifecycle.PER_CLASS)
    class LoadUser {
    	@BeforeAll
        void setup() throws IOException, SQLException {
            testConfig.setupDatabase();
            testConfig.setUpData("datasets/users/find/user01.yml");
        }
        @AfterAll
        void tearDown() throws SQLException, IOException {
            testConfig.setupDatabase();    
        }
        @Test
        @DisplayName("ログイン時のユーザー取得")
        void test1() throws IOException {
        	 Map<String, Object> userMap = testConfig.getYamlData("datasets/articles/add/user01.yml").get("users").get(0);
             ModelMapper mapper = new ModelMapper();
             mapper.getConfiguration()
                     .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                     .setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
             Admin admin = mapper.map(userMap, Admin.class);
        	 
        	SecurityUser user = (SecurityUser) service.loadUserByUsername(admin.getEmail());
        	
        	assertEquals(admin.getName(), user.getLoginUser().getName());
        	assertEquals(admin.getUserId(), user.getLoginUser().getUserId());
        	assertEquals(admin.getPassword(), user.getLoginUser().getPassword());
        	assertEquals(Constants.LoginUserRole.ADMINISTRATOR, user.getLoginUser().getRole());
        }
        
        @Test
        @DisplayName("存在しないユーザー")
        void test2() throws IOException {
        	assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("none_user@example.com"));
        }
    }

}
