package com.example.weeek15_023_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity2 extends AppCompatActivity {
WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        wv=findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        Intent myint= getIntent();

        String str=myint.getExtras().getString("content");
        wv.loadData(str,"text/html","UTF-8");
                ;
    }
}