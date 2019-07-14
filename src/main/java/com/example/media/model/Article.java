package com.example.media.model;

import java.time.format.DateTimeFormatter;

import io.github.gitbucket.markedj.Marked;
import io.github.gitbucket.markedj.Options;

public class Article extends DataModelBase {
    
    private int id;
    private String title;
    private String body;
    private int authorId;
    private String authorName;
    
    private Options markdownOptions = new Options();
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public String getCreatedAtFormatted() {
        return this.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getMarkedBody() {
        markdownOptions.setSanitize(true);
        return Marked.marked(this.body, markdownOptions);
    }
}
