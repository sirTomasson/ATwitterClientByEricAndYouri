package ap.tomassen.online.ruigetweets.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.MainActivity;
import ap.tomassen.online.ruigetweets.activity.ProfileActivity;
import ap.tomassen.online.ruigetweets.model.Entity;
import ap.tomassen.online.ruigetweets.model.Mention;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Status;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 10-5-2017.
 */

public class StatusListAdapter extends ArrayAdapter<Status> {

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


    public StatusListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Status> statuses) {
        super(context, resource, statuses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Status status = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        mIvFavorites = (ImageView) convertView.findViewById(R.id.iv_favorites);
        mIvRetweet = (ImageView) convertView.findViewById(R.id.iv_retweets);
        mIvProfileImg = (ImageView) convertView.findViewById(R.id.iv_profile_img);
        mTvTweetText = (TextView) convertView.findViewById(R.id.tv_tweetText);
        mTvDate = (TextView) convertView.findViewById(R.id.tv_date);
        mTvRetweetCount = (TextView) convertView.findViewById(R.id.tv_retweetCount);
        mTvFavoriteCount = (TextView) convertView.findViewById(R.id.tv_favorites_ount);

        Picasso.with(getContext()).
                load(status.getUser().getProfileImageUrl())
                .into(mIvProfileImg);

        if (!status.getEntities().isEmpty()) {
            entitiesToClickableSpan(status);
        } else {
            mTvTweetText.setText(status.getText());
        }

        mTvDate.setText(status.getCreatedAt().toString());
        mTvRetweetCount.setText("" + status.getRetweetCount());
        mTvFavoriteCount.setText("" + status.getFavoritesCount());

        userIntent = new Intent(getContext(), ProfileActivity.class);

        mIvProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userIntent.putExtra(MainActivity.PROFILE_ID, status.getUser().getId());
                getContext().startActivity(userIntent);

            }
        });

        mIvFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: statusId " + status.getId());
                new CreateFavoriteTask().execute(status.getId());
            }
        });

        mIvRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return convertView;
    }

    private void entitiesToClickableSpan(Status status) {
        ArrayList<Entity> entities = new ArrayList<>(status.getEntities());

        final SpannableString tweetString = new SpannableString(status.getText());
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

    private class CreateFavoriteTask extends AsyncTask<Long, Void, String>{

        @Override
        protected String doInBackground(Long... longs) {
            long statusId = longs[0];

            OAuth10aService authService = model.getAuthService();

            String url = "https://api.twitter.com/1.1/favorites/create.json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            request.addBodyParameter("id", statusId + "");


            authService.signRequest(api.getAccessToken(), request);

            Response response = request.send();

            String res = null;

            if (response.isSuccessful()) {
                try {
                    res = response.getBody();
                    Log.i(TAG, "doInBackground: response successful" +
                            "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Log.d(TAG, "doInBackground: response unsuccessful " + response.getBody() + statusId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return res;
        }

        @Override
        protected void onPostExecute(String aString) {
            super.onPostExecute(aString);
            makeToast(aString);
        }
    }

    private void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
    }
}