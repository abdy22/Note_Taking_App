/*
Author: Abdirahman Hassan
Description: A Realm database object model
*/


package com.example.inote.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class noteDatails extends RealmObject {
    public noteDatails(){}
    public noteDatails(long whenCreated,long lastEdited, String title, String what) {
        this.whenCreated = whenCreated;
        Title = title;
        What = what;
        this.lastEdited=lastEdited;
    }

    @PrimaryKey
    Long whenCreated;

    public long getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(long lastEdited) {
        this.lastEdited = lastEdited;
    }

    long lastEdited;
    String Title,What;

    public long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(long whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getWhat() {
        return What;
    }

    public void setWhat(String what) {
        What = what;
    }
}
