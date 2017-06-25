package ap.tomassen.online.ruigetweets.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.fragment.ErrorDialogFragment;
import ap.tomassen.online.ruigetweets.fragment.MenuFragment;
import ap.tomassen.online.ruigetweets.fragment.ProfileFragment;
import ap.tomassen.online.ruigetweets.fragment.SearchFragment;
import ap.tomassen.online.ruigetweets.fragment.StatusDetailFragment;
import ap.tomassen.online.ruigetweets.fragment.TimelineFragment;
import ap.tomassen.online.ruigetweets.fragment.UpdateStatusFragment;
import ap.tomassen.online.ruigetweets.model.Error;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Profile;
import ap.tomassen.online.ruigetweets.model.Tweet;
import ap.tomassen.online.ruigetweets.model.TwitterModel;


public class MainActivity extends AppCompatActivity
        implements MenuFragment.CallBackListener,
        UpdateStatusFragment.CallbackListener,
        SearchFragment.CallbackListener,
        StatusDetailFragment.CallbackListener {

    public static final String TOKEN_NOT_SAVED = "tokens not in shared preferences";
    public static final String TAG_DIALOG_FRAGMENT = "dialog_tag_fragment";
    public static final String ERROR_TITLE = "ERROR_TITLE";
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";

    private TwitterModel model = TwitterModel.getInstance();

    private MyTwitterApi api = MyTwitterApi.getInstance();

    private FragmentManager manager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(LoginActivity.USER_TOKEN, TOKEN_NOT_SAVED);

        String secret = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(LoginActivity.USER_SECRET, TOKEN_NOT_SAVED);

        //check if the access token is in the shared preferences, if not go authorize the user
        if (true) {
//        if (secret.equals(TOKEN_NOT_SAVED) && token.equals(TOKEN_NOT_SAVED)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            //if the access token is NOT set in the MyTwitterApi go set it
            if (!api.isAccessTokenSet()) api.setAccessToken(new OAuth1AccessToken(token, secret));


            new UserProfileTask().execute();    //request the profile from the authorized user
            new HomeTimeLineTask().execute();   //then get the users home time line
        }
    }

    /*=======================================CALLBACKS============================================*/

    /**
     * opens a new fragment in users can input text
     */
    @Override
    public void createNewTweet() {
        Fragment fragment = manager.findFragmentById(R.id.fl_container);

        if (fragment != null && fragment instanceof UpdateStatusFragment) {
            manager.popBackStack();
        } else {
            fragment = new UpdateStatusFragment();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction
                    .add(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * SENDS the text that the user wrote in the UpdateStatusFragment
     * @param tweet the text that was previously input by the user
     */
    @Override
    public void sendTweet(String tweet) {
        new UpdateStatusTask().execute(tweet);
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
    }

    /**
     * opens a new fragment in which users can search for users and tweets
     */
    @Override
    public void showSearchTweet() {

        Fragment fragment = manager.findFragmentById(R.id.fl_container);

        if (fragment != null && fragment instanceof SearchFragment) {
            manager.popBackStack();
        } else {

            fragment = new SearchFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction
                    .add(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * starts searching for tweets that match the searchText, after that it removes the search bar from view
     * @param searchText the text the user wants to see tweets about
     */
    @Override
    public void searchTweet(String searchText) {
        new SearchTweetTask().execute(searchText);
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
    }

    /**
     * replaces the current fragment with a fragment of the currently authorized user's profile
     */
    @Override
    public void showProfile() {
        ProfileFragment fragment = new ProfileFragment();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * shows the users timeline by starting a UserTimeLineTask
     */
    @Override
    public void showUserTimeline() {
        new UserTimelineTask().execute();
    }

    /**
     * starts a FavoriteListTask that retrieves a list of tweets 'favorited' by the authorized user
     */
    @Override
    public void showFavoritesList() {
        new FavoriteListTask().execute();
    }

    /**
     * starts a HomeTimeLineTask
     */
    @Override
    public void showHomeTimeline() {
        new HomeTimeLineTask().execute();
    }


    /**
     * returns the fragment that is currently visible to the user
     * @return Fragment of any kind
     */
    @Override
    public Fragment currentFragment() {
        return manager.findFragmentById(R.id.fl_container);
    }

    /**
     * Displays a dialog fragment explaining to the user what happened
     * @param message a message from the twitter api when something went wrong.
     */
    @Override
    public void showErrorMessage(String message) {
        createErrorDialog(message);
    }


    /*==============================ASYNC TASKS===================================================*/

    /**
     * retrieves the twitter profile, from the twitter api for the authorized user
     */
    private class UserProfileTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            OAuth10aService authService = model.getAuthService();
            OAuth1AccessToken accessToken = api.getAccessToken();

            try {

                OAuthRequest request = new OAuthRequest(Verb.GET,
                        "https://api.twitter.com/1.1/account/verify_credentials.json",
                        authService);

                authService.signRequest(accessToken, request);

                Response response = request.send();

                if (response.isSuccessful()) {

                    JSONObject userObj = new JSONObject(response.getBody());
                    Profile.getInstance(userObj);

                } else {
                    createErrorDialog(response.getMessage());
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * retrieves the tweets from the twitter api made by the authorized user
     */
    private class UserTimelineTask extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {
            OAuth10aService authService = model.getAuthService();
            OAuth1AccessToken accessToken = api.getAccessToken();

            OAuthRequest request = new OAuthRequest(Verb.GET,
                    "https://api.twitter.com/1.1/statuses/user_timeline.json",
                    authService);

            authService.signRequest(accessToken, request);

            Response response = request.send();

            try {
                if (response.isSuccessful()) {

                    model.setStatuses(new JSONArray(response.getBody()));

                } else {
                    createErrorDialog(response.getBody());
                }

            } catch (IOException | ParseException | JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.isSuccessful()) {
                Fragment f = manager.findFragmentById(R.id.fl_container);
                if (f != null && f instanceof ProfileFragment) {
                    ((ProfileFragment) f).changeListContents(model.getStatuses());
                }
            }
        }
    }

    /**
     * retrieves tweets from the twitter api, from various users that are being followed by the
     * authorized user
     */
    private class HomeTimeLineTask extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... voids) {
            OAuth10aService authService = model.getAuthService();
            OAuth1AccessToken accessToken = api.getAccessToken();

            OAuthRequest request = new OAuthRequest(Verb.GET,
                    "https://api.twitter.com/1.1/statuses/home_timeline.json",
                    authService);
            authService.signRequest(accessToken, request);

            Response response = request.send();

            try {
                if (response.isSuccessful()) {

                    model.setStatuses(new JSONArray(response.getBody()));
                } else {
                    createErrorDialog(response.getBody());
                }
            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.isSuccessful()) showTimeLine();
        }
    }

    /**
     * retrieves tweets from the twitter api that where favorited by the authorized user
     */
    private class FavoriteListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            OAuth10aService authService = model.getAuthService();


            OAuthRequest request = new OAuthRequest(Verb.GET,
                    "https://api.twitter.com/1.1/favorites/list.json",
                    authService);

            JSONArray tweetsObject = null;

            try {
                authService.signRequest(api.getAccessToken(), request);

                Response response = request.send();

                if (response.isSuccessful()) {

                    tweetsObject = new JSONArray(response.getBody());
                    model.setStatuses(tweetsObject);

                } else {
                    createErrorDialog(response.getBody());
                }

            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Fragment f = manager.findFragmentById(R.id.fl_container);
            if (f != null && f instanceof ProfileFragment) {
                ((ProfileFragment) f).changeListContents(model.getStatuses());
            }
        }
    }

    /**
     * updates the status of the authorized user a.k.a tweeting
     */
    private class UpdateStatusTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... Strings) {

            String statusUpdate = Strings[0];

            OAuth10aService authService = model.getAuthService();


            String url = "https://api.twitter.com/1.1/statuses/update.json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            request.addBodyParameter("status", statusUpdate);


            authService.signRequest(api.getAccessToken(), request);

            Response response = request.send();

            try {
                if (response.isSuccessful()) {
                    model.add(0, new Tweet(new JSONObject(response.getBody())));
                } else {
                    createErrorDialog(response.getBody());
                }
            } catch (IOException | ParseException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Fragment f = manager.findFragmentById(R.id.fl_container);
            if (f instanceof TimelineFragment) ((TimelineFragment) f).updateStatuses();
        }
    }

    /**
     * retrieves a list of tweets from the twitter api based on a query made by the authorized
     */
    private class SearchTweetTask extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... Strings) {
            OAuth10aService authService = model.getAuthService();
            String searchText = Strings[0];
            String url = "https://api.twitter.com/1.1/search/tweets.json?";
            JSONArray searchArray = null;

            OAuthRequest request = new OAuthRequest(Verb.GET,
                    url + "q=" + searchText,
                    authService);

            try {
                authService.signRequest(api.getAccessToken(), request);
                Response response = request.send();

                if (response.isSuccessful()) {
                    JSONObject searchObject = new JSONObject(response.getBody());
                    searchArray = searchObject.getJSONArray("statuses");
                    model.setStatuses(searchArray);
                }
            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }
            return searchArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            if (jsonArray != null)
                showTimeLine();
        }
    }



    /*============================================================================================*/

    /**
     * replaces the current fragment with a TimeLineFragment
     */
    public void showTimeLine() {
        TimelineFragment fragment = new TimelineFragment();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * creates an ErrorDialogFragment explaining to the user what went wrong, based on the message that
     * the twitter api sends back
     * @param json a Json array with one or more messages that explain what happened and/or why the service failed
     */
    public void createErrorDialog(String json) {
        ArrayList<Error> errors = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            errors = model.getErrorMessagesFromJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle b = new Bundle();

        if (errors != null) {
            String message = "";
            for (Error error : errors) {
                message += "Code: " + error.getCode() + "\n";
                message += error.getMessage() + "\n\n";
            }
            b.putString(ERROR_TITLE, "SOMETHING HAPPENED!");
            b.putString(ERROR_MESSAGE, message);
        }

        dialogFragment.setArguments(b);
        dialogFragment.show(getFragmentManager(), TAG_DIALOG_FRAGMENT);
    }

}
