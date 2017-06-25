package ap.tomassen.online.ruigetweets.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.MainActivity;
import ap.tomassen.online.ruigetweets.fragment.ErrorDialogFragment;
import ap.tomassen.online.ruigetweets.fragment.StatusDetailFragment;
import ap.tomassen.online.ruigetweets.model.Entity;
import ap.tomassen.online.ruigetweets.model.Error;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Tweet;
import ap.tomassen.online.ruigetweets.model.TwitterModel;


public class StatusListAdapter extends ArrayAdapter<Tweet> {

    private TextView tvTweetText;

    private TwitterModel model = TwitterModel.getInstance();
    private MyTwitterApi api = MyTwitterApi.getInstance();

    private FragmentManager manager;


    /**
     * default constructor for StatusListAdapter
     * @param context from parent activity
     * @param resource layout to be used for displaying content
     * @param statuses a List of statuses to be used as content
     */
    public StatusListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Tweet> statuses) {
        super(context, resource, statuses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Tweet tweet = getItem(position);

        final Context context = parent.getContext();
        manager = ((Activity) context).getFragmentManager();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_status, parent, false);
        }

        ImageView IvFavorites = (ImageView) convertView.findViewById(R.id.iv_favorites);
        ImageView ivRetweet = (ImageView) convertView.findViewById(R.id.iv_retweets);
        ImageView ivProfileImg = (ImageView) convertView.findViewById(R.id.iv_user_profile_img);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        TextView tvRetweetCount = (TextView) convertView.findViewById(R.id.tv_retweet_count);
        TextView tvFavoriteCount = (TextView) convertView.findViewById(R.id.tv_favorites_count);
        tvTweetText = (TextView) convertView.findViewById(R.id.tv_status_text);

        Picasso.with(getContext()).
                load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImg);

        if (!tweet.getEntities().isEmpty()) {
            entitiesToClickableSpan(tweet);
        } else {
            tvTweetText.setText(tweet.getText());
        }


        tvDate.setText(tweet.getCreatedAt().toString());
        tvRetweetCount.setText("" + tweet.getRetweetCount());
        tvFavoriteCount.setText("" + tweet.getFavoritesCount());

        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putLong(StatusDetailFragment.STATUS_ID, tweet.getId());

                Fragment f = new StatusDetailFragment();
                f.setArguments(b);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction
                        .replace(R.id.fl_container, f)
                        .addToBackStack(null)
                        .commit();

            }

        });

        IvFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateFavoriteTask().execute(tweet);
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReTweetTask().execute(tweet);
            }
        });


        return convertView;
    }

    /**
     * supposed to make parts of the text clickable but not working in this build. only makes text colored
     * @param tweet that holds the entities that get colored
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


        tvTweetText.setText(tweetString);
    }

    /**
     * POST a retweet to the twitter api with a tweet id
     */
    private class ReTweetTask extends AsyncTask<Tweet, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Tweet... tweets) {
            Tweet tweet = tweets[0];
            long tweetId = tweet.getId();

            OAuth10aService authService = model.getAuthService();

            String url = "https://api.twitter.com/1.1/statuses/retweet/" + tweetId + ".json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            authService.signRequest(api.getAccessToken(), request);

            Response response = request.send();

            try {

                if (response.isSuccessful()) {
                    model.add(0, new Tweet(new JSONObject(response.getBody())));
                    tweet.increaseRetweetCount();

                } else {
                    createErrorDialog(response.getBody());
                }
            } catch (IOException | ParseException | JSONException e) {
                e.printStackTrace();
            }
            return response.isSuccessful();
        }

        @Override
        protected void onPostExecute(Boolean successfulResponse) {
            super.onPostExecute(successfulResponse);
            if (successfulResponse) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * POST a favorite to the twitter apit with a tweet id
     */
    private class CreateFavoriteTask extends AsyncTask<Tweet, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Tweet... tweets) {
            Tweet tweet = tweets[0];
            long statusId = tweet.getId();

            OAuth10aService authService = model.getAuthService();

            String url = "https://api.twitter.com/1.1/favorites/create.json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            request.addBodyParameter("id", statusId + "");

            authService.signRequest(api.getAccessToken(), request);

            Response response = request.send();

            try {
                if (response.isSuccessful()) {
                    tweet.increaseFavoriteCount();
                } else {
                    createErrorDialog(response.getBody());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.isSuccessful();
        }

        @Override
        protected void onPostExecute(Boolean successfulResponse) {
            super.onPostExecute(successfulResponse);
            if (successfulResponse) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * creates an ErrorDialogFragment explaining to the user what went wrong, based on the message that
     * the twitter api sends back
     * @param json a Json array with one or more messages that explain what happened and/or why the service failed
     */
    private void createErrorDialog(String json) {
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
            b.putString(MainActivity.ERROR_TITLE, "SOMETHING HAPPENED!");
            b.putString(MainActivity.ERROR_MESSAGE, message);
        }

        dialogFragment.setArguments(b);
        dialogFragment.show(manager, MainActivity.TAG_DIALOG_FRAGMENT);
    }

}
