package com.example.paul.demo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;

import android.os.AsyncTask;
import android.support.v4.view.GestureDetectorCompat;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.os.Bundle;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by stephen on 10/10/15.
 */
public class NewsActivity extends AppCompatActivity {

    //NEED 3 NEWS PAGES --- KEEP IN MIND FOR DEV
    //private String pageLeft; //= "http://www.nytimes.com/2015/10/07/business/international/vw-diesel-emissions-job-cuts.html";
    private String currPage; //= "http://www.nytimes.com/2015/10/07/technology/the-hardware-side-of-microsoft-unveils-a-pile-of-new-devices.html";
    //private String pageRight; = "http://www.nytimes.com/aponline/2015/10/06/world/asia/ap-as-skorea-earns-samsung-electronics-.html";

    private ArrayList<String> news = new ArrayList<String>();
    private GestureDetectorCompat mDetector;
    private static final float SWIPE_THRESHOLD = 100;
    private float x1, x2, dx, dy, y1, y2;
    private WebView webView1;
    private WebView webView2;
    private WebView webView3;
    private WebView[] webViews = new WebView[3];
    private String category;
    private String email;
    private Socket sock = com.example.paul.demo.SocketHandler.getSocket();
//    private GetNews getNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        category = intent.getStringExtra("CATEGORY");
        email = intent.getStringExtra("EMAIL");
        //^MAYBE ERASE

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        try {
            GetNews getNews = new GetNews(email, category.toLowerCase());
            getNews.execute();
            getNews.get();
        } catch (Exception ex) {ex.printStackTrace();}

        webView1 = (WebView) findViewById(R.id.webView1);

        WebSettings webSettings = webView1.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView1.setWebViewClient(new Callback());
        webView1.loadUrl(currPage);
        webView1.canGoBack();
        setOnTouch(webView1);
        webViews[1] = webView1;

        webView2 = (WebView) findViewById(R.id.webView2);

        WebSettings webSettings1 = webView2.getSettings();
        webSettings1.setJavaScriptEnabled(true);


        webView2.setWebViewClient(new Callback());
        webView2.loadUrl(news.get(0));
        news.remove(0);
        webView2.canGoBack();
        webViews[0] = webView2;

        webView3 = (WebView) findViewById(R.id.webView3);

        WebSettings webSettings2 = webView3.getSettings();
        webSettings2.setJavaScriptEnabled(true);


        webView3.setWebViewClient(new Callback());
        webView3.loadUrl(news.get(0));
        news.remove(0);
        webView3.canGoBack();
        webViews[2] = webView3;
    }

    private void setOnTouch(WebView w){
        w.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    /*
                        UNCOMMENT WHEN ADDING SWIPE MOTION
                    case MotionEvent.ACTION_MOVE:

                        dx = event.getX();

                        dy = event.getY();
                        if (x1 >= webView.getMeasuredWidth() - 15 && dx < x1) {
                            animatePage();
                        }*/
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();

                        float diffX = x1 - x2;
                        float diffY = y1 - y2;
                        if (Math.abs(diffX) > Math.abs(diffY) + 10 && Math.abs(x1 - x2) > SWIPE_THRESHOLD) {
                            if (x1 > x2) {
                                onSwipeRight();
                            } else onSwipeLeft();

                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void removeOnTouch(WebView w){
        w.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //DoNothing
                return true;
            }
        });
    }

    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading (WebView webView, String Url) {
            return (false);
        }
    }


    public void onSwipeRight() {

        removeOnTouch(webViews[1]);
        webViews[1].setVisibility(View.GONE);
        webViews[2].setVisibility(View.VISIBLE);
        setOnTouch(webViews[2]);

        if(!news.isEmpty()) {
            currPage = news.get(0);
            news.remove(0);
        }else {
            try {
                GetNews getNews = new GetNews(email, category.toLowerCase());
                getNews.execute();
                getNews.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        webViews[1].loadUrl(currPage);
        WebView temp = webViews[1];
        webViews[1] = webViews[2];
        webViews[2] = temp;
    }

    public void onSwipeLeft() {
       // animateR(webViews[1]);
        removeOnTouch(webViews[1]);
        webViews[1].setVisibility(View.GONE);
        webViews[0].setVisibility(View.VISIBLE);
        //animateL(webViews[0]);
        setOnTouch(webViews[0]);

        if(!news.isEmpty()) {
            currPage = news.get(0);
            news.remove(0);
        }else{
            try {
                GetNews getNews = new GetNews(email,category.toLowerCase());
                getNews.execute();
                getNews.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        webViews[1].loadUrl(currPage);
        WebView temp = webViews[1];
        webViews[1] = webViews[0];
        webViews[0] = temp;

    }

    public class GetNews extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mCategory;
        private final String mRequest;

        GetNews(String email, String category) {
            mEmail = email;
            mCategory = category;
            mRequest = "article";
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
//                sock = SocketHandler.getSocket();
                PrintWriter writer = new PrintWriter(sock.getOutputStream());
                //Create JSON object containing the needed user info
                JSONObject newsInfo = new JSONObject();
                newsInfo.put("request", mRequest);
                newsInfo.put("email", mEmail);
                newsInfo.put("category", mCategory);

                String info = newsInfo.toString();
                writer.println(info);
                writer.flush();

                InputStreamReader reader = new InputStreamReader(sock.getInputStream());
                BufferedReader bfRead = new BufferedReader(reader);
                String msg;

                //Edit limit on for loop to increase or decrease amount of files fed from server
                for(int i = 0; i < 5; i++){
                    msg = bfRead.readLine();

                    if(!msg.contains("http")) break;
                    news.add(msg);
                    //break;
                }
                currPage = news.get(0);
                news.remove(0);

            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
    private void animateL(final WebView view) {
        Animation anim = AnimationUtils.loadAnimation(getBaseContext(),
                android.R.anim.slide_in_left);
        view.startAnimation(anim);
    }
    private void animateR(final WebView view) {
        Animation anim = AnimationUtils.loadAnimation(getBaseContext(),
                android.R.anim.slide_out_right);
        view.startAnimation(anim);
    }

/*
UNCOMMENT WHEN READY TO ANIMATE SWIPE
    private void animatePage(){
        //CREATE BITMAP ON PAGE

        System.out.println("~~~~~~~~HERERERE~~~~~~H: " + webView.getMeasuredHeight() + " ~~~~");
        Bitmap img = Bitmap.createBitmap(webView.getWidth(), webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(img);
        webView.draw(canvas);
    }*/
}
