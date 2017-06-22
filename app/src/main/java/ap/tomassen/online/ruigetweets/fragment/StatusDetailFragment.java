package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.Entity;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Tweet;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.model.User;


public class StatusDetailFragment extends Fragment {
    public static final java.lang.String STATUS_ID = "STATUS_ID";

    private TwitterModel model = TwitterModel.getInstance();
    private MyTwitterApi api = MyTwitterApi.getInstance();

    private CallbackListener listener;

    private ImageView ivUserProfileImage;
    private TextView tvUserFavoriteCount;
    private TextView tvUserFallowerCount;
    private TextView tvUserScreenName;
    private TextView tvUserName;
    private TextView tvUserDescription;
    private TextView tvStatusText;
    private TextView tvStatusDate;
    private TextView tvUserLocation;
    private TextView tvStatusReTweetCount;
    private TextView tvStatusFavoriteCount;


    public interface CallbackListener {
        /**
         * calls back to main to show an error message regarding the twitter api
         * @param message that was given back by the twitter api
         */
        void showErrorMessage(String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CallbackListener) context;
        } catch (ClassCastException e) {
            System.err.println("Parent Activity has to implement CallbackListener methods");
        }
    }

    public StatusDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_detail, container, false);

        Bundle b = getArguments();
        if (b != null && b.containsKey(STATUS_ID)) {
            long statusId = b.getLong(STATUS_ID);
            new GetStatusTask().execute(statusId);
        }

        initialize(rootView);
        return rootView;
    }

    /**
     * sets all the TextViews and ImageViews in the rootView to the corresponding xml-elements
     * @param rootView that holds all the xml elements
     */
    public void initialize(View rootView) {
        ivUserProfileImage = (ImageView) rootView.findViewById(R.id.iv_user_profile_img);
        tvUserFavoriteCount = (TextView) rootView.findViewById(R.id.tv_user_favorites_count);
        tvUserFallowerCount = (TextView) rootView.findViewById(R.id.tv_user_followers_count);
        tvUserName = (TextView) rootView.findViewById(R.id.tv_user_name);
        tvUserScreenName = (TextView) rootView.findViewById(R.id.tv_user_sceen_name);
        tvUserLocation = (TextView) rootView.findViewById(R.id.tv_user_location);
        tvUserDescription = (TextView) rootView.findViewById(R.id.tv_user_description);

        tvStatusText = (TextView) rootView.findViewById(R.id.tv_status_text);
        tvStatusReTweetCount = (TextView) rootView.findViewById(R.id.tv_status_retweet_count);
        tvStatusFavoriteCount = (TextView) rootView.findViewById(R.id.tv_status_favorite_count);
        tvStatusDate = (TextView) rootView.findViewById(R.id.tv_date);
    }

    /**
     * Infuse the TextViews and ImageViews with data from a single tweet
     * @param tweet object that holds data that needs to be displayed to the user
     */
    private void setContentValues(Tweet tweet) {
        User user = tweet.getUser();

        Picasso.with(getContext()).
                load(user.getProfileImageUrl())
                .into(ivUserProfileImage);
        tvUserFavoriteCount.setText(user.getFavoritesCount() + "");
        tvUserFallowerCount.setText(user.getFollowersCount() + "");
        tvUserScreenName.setText(user.getScreenName());
        tvUserName.setText(user.getName());
        tvUserLocation.setText(user.getLocation());
        tvUserDescription.setText(user.getDescription());

        tvStatusText.setText(tweet.getText());
        tvStatusFavoriteCount.setText(tweet.getFavoritesCount() + "");
        tvStatusDate.setText(tweet.getCreatedAt().toString());
        tvStatusReTweetCount.setText(tweet.getRetweetCount() + "");

        entitiesToClickableSpan(tweet);
    }

    /**
     * makes entities in a tweet clickable appear clickable, clicking functionality not working at the moment
     * @param tweet that holds various entities
     */
    private void entitiesToClickableSpan(Tweet tweet) {
        ArrayList<Entity> entities = new ArrayList<>(tweet.getEntities());

        final SpannableString tweetString = new SpannableString(tweet.getText());
        for (final Entity e : entities) {

            int[] indices = e.getIndices();


            final ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            tweetString.setSpan(clickableSpan,
                    indices[0],
                    indices[1],
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvStatusText.setText(tweetString);
    }

    /**
     * get a single status from twitter api with a statusId
     */
    private class GetStatusTask extends AsyncTask<Long, Void, Tweet> {

        @Override
        protected Tweet doInBackground(Long... longs) {
            long statusId = longs[0];

            OAuth1AccessToken token = api.getAccessToken();
            OAuth10aService service = model.getAuthService();

            OAuthRequest request = new OAuthRequest(Verb.GET,
                    "https://api.twitter.com/1.1/statuses/show/" + statusId + ".json",
                    service);
            service.signRequest(token, request);

            Response response = request.send();
            Tweet tweet = null;
            try {
                if (response.isSuccessful()) {
                    tweet = new Tweet(new JSONObject(response.getBody()));

                } else {
                    listener.showErrorMessage(response.getBody());
                }
            } catch (IOException | ParseException | JSONException e) {
                e.printStackTrace();
            }
            return tweet;
        }

        @Override
        protected void onPostExecute(Tweet tweet) {
            super.onPostExecute(tweet);
            setContentValues(tweet);
        }
    }
}
