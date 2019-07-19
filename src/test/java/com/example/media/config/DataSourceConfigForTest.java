package com.example.media.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.yaml.snakeyaml.Yaml;

@Configuration
@ComponentScan(basePackages = { "com.example.*" })
@PropertySource(value = {"classpath:application-test.yml"})
@EnableTransactionManagement
public class DataSourceConfigForTest {
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String user;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    
    @Autowired
    private ResourceLoader resourceLoader;

    private final String DDL_DIR = "./DDL/";
    
    private final String[] DDL_FILES = {"01_users.sql", "02_articles.sql"};
    
    @Bean
    @Profile("test")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
     
        return dataSource;
    }
   
    public void setupDatabase() throws SQLException, IOException {
        String[] ddlList = getSetupDDL();
        try(Connection con = dataSource().getConnection(); 
            Statement stmt = con.createStatement()) {
            for(var ddl : ddlList) {
                stmt.execute(ddl);    
            }
        }
    }
    
    private String[] getSetupDDL() throws IOException {
        List<String> ret = new ArrayList<>();
        for(var fileName : DDL_FILES) {
            String content = Files.readString(Paths.get(DDL_DIR + fileName));
            String[] sqls = content.split(";");
            for(var sql : sqls) {
                if(!sql.isBlank()) {
                    ret.add(sql);    
                }
            }
        }
        return ret.toArray(new String[ret.size()]);
    }
    
    public void setUpData(String dataPath) throws IOException, SQLException {
        
        Map<String , List<Map<String, Object>>> map = getYamlData(dataPath);
        
        List<String> sqlList = new ArrayList<>();
        for(String key : map.keySet()) {
            List<Map<String, Object>> columns = (List<Map<String, Object>>) map.get(key);
            for(var map2 : columns) {
                String sql = "INSERT INTO " + key + "(";
                sql += String.join(",", map2.keySet());
                sql += ")VALUES( ";
                List<String> values = map2.keySet().stream().map(k -> "'" + map2.get(k) + "'").collect(Collectors.toList());
                sql += String.join(",", values);
                sql += ");";
                sqlList.add(sql);
            }
        }
        
        try(Connection con = dataSource().getConnection(); 
                Statement stmt = con.createStatement()) {
            for(var sql : sqlList) {
                stmt.executeUpdate(sql);
            }
        }
    }
    
    public Map<String , List<Map<String, Object>>> getYamlData(String dataPath) throws IOException {
        Yaml yaml = new Yaml();

        InputStream input = resourceLoader.getResource("classpath:" + dataPath).getInputStream();
        @SuppressWarnings("unchecked")
        Map<String , List<Map<String, Object>>> map = 
            (Map<String , List<Map<String, Object>>>) yaml.load(input);
        
        return map;
    }
    
}
