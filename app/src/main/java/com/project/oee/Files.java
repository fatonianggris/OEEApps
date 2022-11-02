package com.project.oee;

/**
 * Created by Ravi on 13/05/15.
 */
public class Files {
    public String nameFile;
    public String size;
    public String date;
    public String path;

    public Files(String nameFile, String size , String date, String path) {
        this.nameFile = nameFile;
        this.size = size;
        this.date = date;
        this.path = path;
    }


    public String getNameFile() {return nameFile;}

    public void setNameFile(String nmF) {
        this.nameFile = nmF;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String sz) {
        this.size = sz;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String pt) {this.path = pt;}

    public String getDate() {
        return date;
    }

    public void setDate(String dt) {this.date = dt;}

}
