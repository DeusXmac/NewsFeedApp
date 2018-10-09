package com.example.newsfeedapp;

/**
 * Created by Ferhat on 8.5.2018
 */

public class NewsLibrary {

    private String mImage;

    private String mWebTitle;

    private String mWebUrl;

    private String mAuthor;

    private String mWebPublicationDate;

    public NewsLibrary(String voidImage, String voidTitle, String voidUrl, String voidAuthor, String voidDate) {
        mImage = voidImage;
        mWebTitle = voidTitle;
        mWebUrl = voidUrl;
        mAuthor = voidAuthor;
        mWebPublicationDate = voidDate;
    }

    public String getImage() {
        return mImage;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }
}
