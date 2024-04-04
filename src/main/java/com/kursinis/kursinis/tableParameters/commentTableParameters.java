package com.kursinis.kursinis.tableParameters;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class commentTableParameters {
    SimpleStringProperty title = new SimpleStringProperty();
    SimpleStringProperty commentBody = new SimpleStringProperty();
    SimpleIntegerProperty user_id = new SimpleIntegerProperty();


    public commentTableParameters(SimpleStringProperty title, SimpleStringProperty commentBody, SimpleIntegerProperty user_id) {
        this.title = title;
        this.commentBody = commentBody;
        this.user_id = user_id;

    }
    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getCommentBody() {
        return commentBody.get();
    }

    public SimpleStringProperty commentBodyProperty() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody.set(commentBody);
    }

    public int getCommentedBy() {
        return user_id.get();
    }

    public SimpleIntegerProperty commentedByProperty() {
        return user_id;
    }

    public void setCommentedBy(int user_id) {
        this.user_id.set(user_id);
    }



    public commentTableParameters() {
    }

}
