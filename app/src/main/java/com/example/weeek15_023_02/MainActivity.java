package com.example.weeek15_023_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase articleDB;
    ArrayList<ArticleModel> titles= new ArrayList<>();
    ArrayAdapter adap;
    ListView lv;
    //https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty
    public class DT extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
           StringBuilder str = new StringBuilder("");

           URL url;
            HttpURLConnection urlConnection=null;

            try {
                url= new URL(strings[0]);
                urlConnection=(HttpURLConnection)  url.openConnection();

                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data= reader.read();
                while(data!=-1){

                    char curr= (char) data;
                    str.append(curr);
                    data = reader.read();
                }

                Log.d("TEST",str.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return str.toString();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv= (ListView) findViewById(R.id.myList);
        adap= new ArrayAdapter(this, android.R.layout.simple_list_item_1,titles);
        lv.setAdapter(adap);
    articleDB = openOrCreateDatabase("Article", MODE_PRIVATE,null);
    articleDB.execSQL("Create Table if not exists articles (id INTEGER PRIMARY KEY AutoIncrement, articleID INTEGER, Title VARCHAR, content VARCHAR)");
        DT myTask = new DT();
        try {
          String result=  myTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
            JSONArray jsonArray= new JSONArray(result);
        int numberofItems=2;
        if(jsonArray.length()<2){
            numberofItems= jsonArray.length();
        }
      articleDB.execSQL("Delete from articles");
        for(int i=0;i<numberofItems;i++){
              String articleID= jsonArray.getString(i);

DT tast2= new DT();
              String rslt = tast2.execute("https://hacker-news.firebaseio.com/v0/item/"+articleID+".json?print=pretty").get();
              JSONObject myJSon= new JSONObject(rslt);

             if(!myJSon.isNull("title")&&!myJSon.isNull("url")) {
                 String articleTitle = myJSon.getString("title");
                 String articleURL = myJSon.getString("url");
                DT task3= new DT();
                 String articleContent= task3.execute(articleURL).get();

                 String sql ="INSERT INTO articles(articleID, title,content) values (?,?,?)";
                 SQLiteStatement stmt= articleDB.compileStatement(sql);
                 stmt.bindString(1,articleID);
                 stmt.bindString(2, articleTitle);
                 stmt.bindString(3,articleContent);
                 stmt.execute();

             }

        }

        }catch(Exception ex){
            Log.d("TEST_HATA",ex.getMessage());
        }


        Cursor cursorArticle
                = articleDB.rawQuery("SELECT * FROM articles", null);
if(cursorArticle.moveToNext()){
    do{
        titles.add(new ArticleModel(
                cursorArticle.getInt(0),
                cursorArticle.getInt(1),
                cursorArticle.getString(2),
                cursorArticle.getString(3)));


    }while(cursorArticle.moveToNext());
    adap.notifyDataSetChanged();

lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent myint= new Intent(getApplicationContext(), MainActivity2.class);

            ArticleModel model= titles.get(position);
        myint.putExtra("content",model.Content);
        Log.d("TEST", model.Content);
        startActivity(myint);
    }
});



}



    }
}