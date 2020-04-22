package com.example.project3;

import java.io.Serializable;

public class UserModel implements Serializable
{
    private int id;
    private String userName;
    private String Email;
    private String Password;
    private int access;

    /*public UserModel(String userName) {
        this.userName = userName;
    }*/

    public UserModel(int id, String userName, String Email, String Password, int access)
    {
        this.id = id;
        this.userName = userName;
        this.Email = Email;
        this.Password = Password;
        this.access = access;
    }


    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }
    public void setPassword(String Password) {
        this.Password = Password;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }
    public void setEmail(String userName) {
        this.Email = Email;
    }

    public int getAccess() {
        return access;
    }
    public void setAccess(int access) {
        this.access = access;
    }
}
