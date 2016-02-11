package com.example.hienadz_budkouski.myapplication.Models;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.hienadz_budkouski.myapplication.Interfaces.IImg;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Users")
public class CUser extends CImg implements IUser{

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private String login;
    @DatabaseField(canBeNull = false)
    private String password;
    @DatabaseField(canBeNull = false)
    private String firstName;
    @DatabaseField(canBeNull = false)
    private String lastName;
    @DatabaseField(canBeNull = true)
    String middleName;
    @DatabaseField(canBeNull = false)
    private String email;
    @DatabaseField(canBeNull = false)
    private byte post;
    @DatabaseField(canBeNull = false)
    private int balance;


    @Override
    public void changeBalance(IItem item) {
        balance += (item.getStatus() == IItem.Status.Праверано ? 1:-1)*(item.getBalls());

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getEmail() {
        return email;
    }

    public Posts getPost() {
        return Posts.getPost(post);
    }

    public byte getPostByte() {
        return  post;
    }

    public int getBalance() {
        return balance;
    }

    public String getStringFullName(){
        return lastName+" "+firstName+" "+lastName;
    }

    public int getId(){return id;}



    public static UserBuilder newBuilder() {
        return new CUser().new UserBuilder();
    }

    public  CUser(){
    }


    public  class UserBuilder {
        private UserBuilder() {
            // private constructor
        }


        public CUser build() {
            return CUser.this;
        }

        public UserBuilder setLogin(String login) {
            CUser.this.login = login;
            return this;
        }

        public UserBuilder setPassword(String password) {
            CUser.this.password = password;
         return this;
        }

        public UserBuilder setFirstName(String first_name) {
            CUser.this.firstName = first_name;
         return this;
        }

        public UserBuilder setLastName(String last_name) {
            CUser.this.lastName = last_name;
         return this;
        }

        public UserBuilder setMiddleName(String middle_name) {
            CUser.this.middleName = middle_name;
         return this;
        }

        public UserBuilder setEmail(String email) {
            CUser.this.email = email;
         return this;
        }

        public UserBuilder setPost(Posts post) {
            CUser.this.post = post.getId();
         return this;
        }

        public UserBuilder setBalance(int balance) {
            CUser.this.balance = balance;
         return this;
        }


        public UserBuilder setBitmap(Bitmap Img){
            CUser.this.setBitmap(Img);
            return  this;
        }

        public UserBuilder setBitmapFromUri(Uri path){

            CUser.this.setBitmapFromUri(path);
            return  this;
        }
    }
}

