package com.example.newsfeedapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ferhat on 8.5.2018.
 */

public final class NewsQuery {

    private static final String Response = "response";
    private static final String Results = "results";
    private static final String WebTitle = "webTitle";
    private static final String WebUrl = "webUrl";
    private static final String WebDate = "webPublicationDate";
    private static final String Tags = "tags";
    private static final String Fields = "fields";
    private static final String Image = "thumbnail";

    /*** Error Messages ***/
    private static final String NewsQuery = "NewsQuery";
    private static final String parsingError = "There a is problem parsing JSON.";
    private static final String requestError = "Problem making the HTTP request.";
    private static final String connectError = "There is problem connecting to website. Response code: ";
    private static final String jsonError = "Problem getting news JSON.";
    private static final String urlError = "Problem building the URL";

    private static final int sleep = 2000;
    private static final int readTimeOut = 10000;
    private static final int connectTimeOut = 15000;

    private static List<NewsLibrary> getJson(String newsJson) {
        if(TextUtils.isEmpty(newsJson)) {
            return null;
        }

        List<NewsLibrary> getNews = new ArrayList<>();

        try {
            JSONObject getResponse = new JSONObject(newsJson);

            JSONArray newsArray = getResponse.getJSONObject(Response).getJSONArray(Results);

            for(int i = 0; i < newsArray.length(); i++) {

                JSONObject news = newsArray.getJSONObject(i);

                String webTitle = news.getString(WebTitle);

                String webUrl = news.getString(WebUrl);

                String date = news.getString(WebDate);

                JSONArray tags = news.getJSONArray(Tags);

                JSONObject blocks = news.getJSONObject(Fields);

                JSONObject tagsObj = tags.getJSONObject(0);

                String author = null;

                if(tags.length() == 1) {
                    author = tagsObj.getString(WebTitle);
                }

                String url = null;

                if(blocks.length() == 1) {
                    url = blocks.getString(Image);
                }

                NewsLibrary library = new NewsLibrary(url,webTitle,webUrl,author,date);

                getNews.add(library);

            }
        } catch (JSONException e) {
            Log.e(NewsQuery, parsingError);
        }

        return getNews;
    }

    public static List<NewsLibrary> newsData(String requestedUrl) {

        URL url = createUrl(requestedUrl);

        String JSON = null;
        try {
            JSON = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(NewsQuery, requestError, e);
        }

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<NewsLibrary> news = getJson(JSON);

        return news;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String getResponse = "";

        if (url == null) {
            return getResponse;
        }

        HttpURLConnection connection = null;
        InputStream stream = null;

        try{

            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(readTimeOut);
            connection.setConnectTimeout(connectTimeOut);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == connection.HTTP_OK) {
                stream = connection.getInputStream();
                getResponse = reader(stream);
            } else {
                Log.e(NewsQuery, connectError + connection.getResponseCode());
            }

        } catch (IOException e) {
                Log.e(NewsQuery, jsonError , e);
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            if(stream != null) {
                stream.close();
            }
        }

        return getResponse;

    }

    private static URL createUrl(String requestedUrl) {
        URL url = null;
        try {
            url = new URL(requestedUrl);
        } catch (MalformedURLException e) {
            Log.e(NewsQuery, urlError, e);
        }
        return url;
    }

    private static String reader(InputStream inputStream) throws IOException{
        StringBuilder data = new StringBuilder();

        if(inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String readLine = bufferedReader.readLine();
            while(readLine != null) {
                data.append(readLine);
                readLine = bufferedReader.readLine();
            }
        }

        return data.toString();
    }
}
