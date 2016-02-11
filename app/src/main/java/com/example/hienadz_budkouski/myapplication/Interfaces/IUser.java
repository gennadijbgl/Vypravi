package com.example.hienadz_budkouski.myapplication.Interfaces;
import android.graphics.Bitmap;
import android.net.Uri;

public interface IUser  extends  IImg, IDao{

     enum  Posts
     {
          Адміністратар((byte) 0x0), Тэхнік(((byte) 0x1)),
          Выкладчык(((byte) 0x2)), Практыкант(((byte) 0x3)),
          Дырэктар(((byte) 0x4));

          private final byte id;

          Posts(int id) {
               this.id = (byte) id;
          }

          public byte getId() {
               return this.id;
          }

        static public Posts getPost(byte id) {
             return Posts.values()[id];
         }

     }


     void changeBalance(IItem item);

     String getLogin();

     String getPassword();

     String getFirstName();

     String getLastName();

     String getMiddleName();

     String getEmail();

     Posts getPost();

     byte getPostByte();

     int getBalance();

     String getStringFullName();


}