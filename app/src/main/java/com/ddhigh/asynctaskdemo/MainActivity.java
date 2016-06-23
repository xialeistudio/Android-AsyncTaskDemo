package com.ddhigh.asynctaskdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout;
    private final String[] urls = {
            "http://n.sinaimg.cn/news/crawl/20160623/rGdP-fxtmwep2657001.jpg",
            "http://n.sinaimg.cn/news/crawl/20160623/6mXQ-fxtmweh2331418.jpg",
            "http://n.sinaimg.cn/news/crawl/20160623/z6Io-fxtmwep2657003.jpg",
            "http://n.sinaimg.cn/news/crawl/20160623/U9QQ-fxtmweh2331421.jpg",
            "http://n.sinaimg.cn/news/crawl/20160623/BIdM-fxtmweh2331423.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mLinearLayout = (LinearLayout) findViewById(R.id.contentView);

    }

    public void loadImage(View view) {
        ImageTask imageTask = new ImageTask();
        imageTask.execute(urls);
    }

    private class ImageTask extends AsyncTask<String, Integer, List<Bitmap>> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setMax(urls.length);
        }

        @Override
        protected List<Bitmap> doInBackground(String... params) {
            List<Bitmap> list = new ArrayList<>();
            int i = 0;
            for (String url : params) {
                URLConnection connection;
                Bitmap bitmap;
                InputStream inputStream;
                try {
                    connection = new URL(url).openConnection();
                    inputStream = connection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    inputStream.close();
                    bufferedInputStream.close();
                    list.add(bitmap);
                    publishProgress(++i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmaps) {
            for (Bitmap bitmap : bitmaps) {
                ImageView imageView = new ImageView(MainActivity.this);
                ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageBitmap(bitmap);
                mLinearLayout.addView(imageView);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }
    }
}
