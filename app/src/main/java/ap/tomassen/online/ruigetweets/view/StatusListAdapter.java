package ap.tomassen.online.ruigetweets.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.MainActivity;
import ap.tomassen.online.ruigetweets.activity.ProfileActivity;
import ap.tomassen.online.ruigetweets.fragment.ErrorDialogFragment;
import ap.tomassen.online.ruigetweets.model.Entity;
import ap.tomassen.online.ruigetweets.model.Error;
import ap.tomassen.online.ruigetweets.model.Mention;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Tweet;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 10-5-2017.
 */

public class StatusListAdapter extends ArrayAdapter<Tweet> {

    private final String TAG = StatusListAdapter.class.getSimpleName();

    private ImageView mIvFavorites;
    private ImageView mIvRetweet;
    private ImageView mIvProfileImg;
    private TextView mTvTweetText;
    private TextView mTvDate;
    private TextView mTvRetweetCount;
    private TextView mTvFavoriteCount;

    private TwitterModel model = TwitterModel.getInstance();
    private MyTwitterApi api = MyTwitterApi.getInstance();

    private Intent userIntent;

    private FragmentManager manager;


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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        mIvFavorites = (ImageView) convertView.findViewById(R.id.iv_favorites);
        mIvRetweet = (ImageView) convertView.findViewById(R.id.iv_retweets);
        mIvProfileImg = (ImageView) convertView.findViewById(R.id.iv_profile_img);
        mTvTweetText = (TextView) convertView.findViewById(R.id.tv_tweetText);
        mTvDate = (TextView) convertView.findViewById(R.id.tv_date);
        mTvRetweetCount = (TextView) convertView.findViewById(R.id.tv_retweet_count);
        mTvFavoriteCount = (TextView) convertView.findViewById(R.id.tv_favorites_count);

        Picasso.with(getContext()).
                load(tweet.getUser().getProfileImageUrl())
                .into(mIvProfileImg);

        if (!tweet.getEntities().isEmpty()) {
            entitiesToClickableSpan(tweet);
        } else {
            mTvTweetText.setText(tweet.getText());
        }


        mTvDate.setText(tweet.getCreatedAt().toString());
        mTvRetweetCount.setText("" + tweet.getRetweetCount());
        mTvFavoriteCount.setText("" + tweet.getFavoritesCount());

        userIntent = new Intent(getContext(), ProfileActivity.class);

        mIvProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userIntent.putExtra(MainActivity.PROFILE_ID, tweet.getUser().getId());
                getContext().startActivity(userIntent);

            }
        });

        mIvFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateFavoriteTask().execute(tweet);
            }
        });

        mIvRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReTweetTask().execute(tweet);
            }
        });


        return convertView;
    }

    private void entitiesToClickableSpan(Tweet tweet) {
        ArrayList<Entity> entities = new ArrayList<>(tweet.getEntities());

        final SpannableString tweetString = new SpannableString(tweet.getText());
        for (final Entity e : entities) {

            int[] indices = e.getIndices();



            final ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (e instanceof Mention) {
                        int id = ((Mention) e).getUserId();

                        userIntent.putExtra(MainActivity.PROFILE_ID, id);
                    }
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


        mTvTweetText.setText(tweetString);
    }

    private class ReTweetTask extends AsyncTask<Tweet, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Tweet... tweets) {
            Tweet tweet = tweets[0];
            long tweetId = tweet.getId();

            OAuth10aService authService = model.getAuthService();

            String url = "https://api.twitter.com/1.1/statuses/retweet/" +  tweetId + ".json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            authService.signRequest(api.getAccessToken(), request);

            Response response = request.send();

            try {

                if (response.isSuccessful()) {
                    tweet.increaseRetweetCount();

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

    private class CreateFavoriteTask extends AsyncTask<Tweet, Void, Boolean>{

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

    private void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
    }

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
