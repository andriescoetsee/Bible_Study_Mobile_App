/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.bible_study_mobile_app.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.bible_study_mobile_app.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static URL buildUrl(String api_case) {

        String apiCase = "";

        if (api_case.equals("ANNOUNCEMENTS")) {
            apiCase = "/announcements";
        }
        else if (api_case.equals("QUESTIONS")) {
            apiCase = "/questions";
        }
        else if (api_case.equals("PRAYER_TOPICS")) {
            apiCase = "/prayer_topics";
        }

        String websiteURL = BuildConfig.GROWLEARNING_WEBSITE + apiCase;

        Uri builtUri = Uri.parse(websiteURL);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

       Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        String accessToken = BuildConfig.ACCESS_TOKEN;

        urlConnection.setRequestProperty("Authorization", "Token " + accessToken);

        int httpStatus = urlConnection.getResponseCode();
//        Log.v("------->>>>", "httpStatus " + httpStatus);

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}