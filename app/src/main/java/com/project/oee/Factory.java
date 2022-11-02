package com.project.oee;

/**
 * Created by Ravi on 13/05/15.
 */
public class Factory {
    public String idFactory;
    public String factory;
    public String username;
    public String date;

    public Factory(String idFactory, String factory , String username, String date) {
        this.idFactory = idFactory;
        this.factory = factory;
        this.username = username;
        this.date = date;
    }


    public String getIdFactory() {return idFactory;}

    public void setIdFactory(String idF) {
        this.idFactory = idF;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String idF) {
        this.factory = idF;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String usr) {this.username = usr;}

    public String getDate() {
        return date;
    }

    public void setDate(String dt) {this.date = dt;}

}
