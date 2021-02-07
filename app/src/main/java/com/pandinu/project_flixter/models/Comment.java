package com.pandinu.project_flixter.models;

public class Comment {
    String author;
    String comment;

    public Comment(String author, String comment){
        this.author = author;
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }



}
