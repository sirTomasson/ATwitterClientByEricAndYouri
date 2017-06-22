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
import android.widget.Toast;

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
import java.util.List;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.MainActivity;
import ap.tomassen.online.ruigetweets.fragment.ErrorDialogFragment;
import ap.tomassen.online.ruigetweets.fragment.ProfileFragment;
import ap.tomassen.online.ruigetweets.fragment.StatusDetailFragment;
import ap.tomassen.online.ruigetweets.model.Entity;
import ap.tomassen.online.ruigetweets.model.Error;
import ap.tomassen.online.ruigetweets.model.Mention;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Tweet;
import ap.tomassen.online.ruigetweets.model.TwitterModel;


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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_status, parent, false);
        }

        mIvFavorites = (ImageView) convertView.findViewById(R.id.iv_favorites);
        mIvRetweet = (ImageView) convertView.findViewById(R.id.iv_retweets);
        mIvProfileImg = (ImageView) convertView.findViewById(R.id.iv_user_profile_img);
        mTvTweetText = (TextView) convertView.findViewById(R.id.tv_status_text);
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

        mIvProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new ShowStatusesByUser().execute(tweet.getUser().getId());

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
                        Log.d(TAG, "onClick: mention");
                        long id = ((Mention) e).getUserId();

//                        new ShowStatusesByUser().execute(id);

                        Fragment fragment = new ProfileFragment();
                        Bundle b = new Bundle();
                        b.putLong("USER_ID", id);
                        fragment.setArguments(b);
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction
                                .replace(R.id.fl_container, fragment)
                                .addToBackStack(null)
                                .commit();

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

    private class ShowStatusesByUser extends AsyncTask<Long, Void, Response> {

        @Override
        protected Response doInBackground(Long... longs) {
            long userId = longs[0];
            Log.d(TAG, "doInBackground: userId" + userId);

            OAuth10aService service = model.getAuthService();
            OAuth1AccessToken accessToken = api.getAccessToken();

            OAuthRequest request = new OAuthRequest(Verb.GET,
                    "https://api.twitter.com/1.1/statuses/show/.json",
                    service);

            service.signRequest(accessToken, request);

            Response response = null;

            try {
                response = request.send();
                if (response.isSuccessful()) {

                } else {
                    createErrorDialog(response.getBody());
                }
            } catch (IOException e) {
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

    private void changeListItems(List<Tweet> tweets) {
        clear();
        addAll(tweets);
    }
}
