package com.example.android.bible_study_mobile_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.bible_study_mobile_app.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mMainScreen;
    private TextView mMainScreenTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainScreen = (TextView) findViewById(R.id.main_screen);
        mMainScreenTitle = (TextView) findViewById(R.id.main_screen_title);

        mMainScreenTitle.setText("Latest Announcements");
        mMainScreen.setText("....loading.....");

        new FetchBibleStudy().execute("ANNOUNCEMENTS");

    }

    public class FetchBibleStudy extends AsyncTask<String, Void, String> {

        private String mApiCase;

        @Override
        protected String doInBackground(String... params) {

            mApiCase = params[0];
            URL tutorRequestUrl = NetworkUtils.buildUrl(mApiCase);

            try {
                String jsonTutorResponse = NetworkUtils
                        .getResponseFromHttpUrl(tutorRequestUrl);

                return jsonTutorResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String apiData) {

            if (mApiCase.equals("ANNOUNCEMENTS")) {
                renderAnnouncements(apiData);

            } else if (mApiCase.equals("QUESTIONS")) {
                renderQuestions(apiData);

            } else if (mApiCase.equals("PRAYER_TOPICS")) {
                renderPrayerTopics(apiData);
            }
            else {
                return;
            }
        }

        protected void renderAnnouncements(String apiData) {

            if (apiData != null) {

                final String MSG_TEXT = "msg_text";
                final String MSG_TITLE = "title";

                JSONArray dataArray = null;

                try {
                    dataArray = new JSONArray(apiData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mMainScreen.setText("");

                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject msg = null;
                    String txt_msg = "", title = "";

                    try {
                        msg = dataArray.getJSONObject(i);

                        title = msg.getString(MSG_TITLE);
                        mMainScreen.append(title + "\n");
                        mMainScreen.append("=========================" + "\n\n");

                        txt_msg = msg.getString(MSG_TEXT);
                        txt_msg = txt_msg.replaceAll("#","\n\n");
                        mMainScreen.append(txt_msg + "\n\n");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        protected void renderQuestions(String apiData) {

            if (apiData != null) {

                final String MSG_TITLE = "title";
                final String MSG_PASSAGE = "passage";
                final String MSG_KEY_VERSE = "key_verse";
                final String MSG_QUESTIONS = "questions";

                String title ="", passage="", key_verse="", question="";

                mMainScreen.setText("");

                JSONArray dataArray = null;
                JSONObject dataObject = null;

                try {

                    dataArray = new JSONArray(apiData);
                    dataObject = dataArray.getJSONObject(0);

                    title = dataObject.getString(MSG_TITLE);
                    mMainScreen.append("Title: " + title + "\n\n");

                    passage = dataObject.getString(MSG_PASSAGE);
                    mMainScreen.append(passage + "\n\n");

                    key_verse = dataObject.getString(MSG_KEY_VERSE);

                    mMainScreen.append("kv: '" + key_verse + "'\n\n");

                    JSONArray questionArray = null;

                    questionArray = dataObject.getJSONArray(MSG_QUESTIONS);

                    mMainScreen.append("<QUESTIONS>" + "\n\n");

                    for (int i = 0; i < questionArray.length(); i++) {

                        JSONObject questionObject = null;

                        try {
                            question = questionArray.getString(i);
                            mMainScreen.append(question + "\n\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (JSONException e) {
                        e.printStackTrace();
                }
            }
        }
    }

    protected void renderPrayerTopics(String apiData) {

        if (apiData != null) {

            final String MSG_TEXT = "msg_text";
            final String MSG_CARD_TYPE = "title";

            JSONArray dataArray = null;

            try {
                dataArray = new JSONArray(apiData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mMainScreen.setText("");

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject msg = null;
                String msg_text ="", card_type="";

                try {
                    msg = dataArray.getJSONObject(i);
                    card_type = msg.getString(MSG_CARD_TYPE);
                    mMainScreen.append(card_type + "\n");
                    mMainScreen.append("=========================" + "\n\n");


                    msg_text = msg.getString(MSG_TEXT);
                    msg_text = msg_text.replaceAll("#","\n\n");
                    mMainScreen.append(msg_text + "\n\n");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // COMPLETED (5) Override onCreateOptionsMenu to inflate the menu for this Activity
    // COMPLETED (6) Return true to display the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.bible_study_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    // COMPLETED (7) Override onOptionsItemSelected to handle clicks on the refresh button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_announcements) {
            mMainScreenTitle.setText("Latest Announcements");
            mMainScreen.setText("....loading.....");

            new FetchBibleStudy().execute("ANNOUNCEMENTS");
            return true;
        }
        else if (id == R.id.action_questions) {
            mMainScreenTitle.setText("Questions next study");
            mMainScreen.setText("....loading.....");

            new FetchBibleStudy().execute("QUESTIONS");

            return true;
        }
        else if (id == R.id.action_prayer_topics) {
            mMainScreenTitle.setText("Prayer Topics");
            mMainScreen.setText("....loading.....");

            new FetchBibleStudy().execute("PRAYER_TOPICS");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
