package com.example.weeek15_023_02;

public class ArticleModel {
    int PkId;
    int articleID;
    String Title;
    String Content;
   public  ArticleModel(int pkid, int articleID, String title, String Content ){
       this.PkId=pkid;
       this.articleID=articleID;
       this.Title=title;
      this.Content=Content;

   }

@Override
    public String toString(){
    return Title;
}
}
