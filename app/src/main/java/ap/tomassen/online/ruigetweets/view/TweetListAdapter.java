package ap.tomassen.online.ruigetweets.view;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.MainActivity;
import ap.tomassen.online.ruigetweets.activity.ProfileActivity;
import ap.tomassen.online.ruigetweets.model.Entity;
import ap.tomassen.online.ruigetweets.model.Mention;
import ap.tomassen.online.ruigetweets.model.Tweet;

/**
 * Created by youri on 10-5-2017.
 */

public class TweetListAdapter extends ArrayAdapter<Tweet> {
    private ImageView mIvProfileImg;
    private TextView mTvTweetText;
    private TextView mTvDate;
    private TextView mTvRetweetCount;
    private TextView mTvFavoriteCount;

    private Intent userIntent;

    public TweetListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Tweet> tweets) {
        super(context, resource, tweets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        mIvProfileImg = (ImageView) convertView.findViewById(R.id.iv_profileImg);
        mTvTweetText = (TextView) convertView.findViewById(R.id.tv_tweetText);
        mTvDate = (TextView) convertView.findViewById(R.id.tv_date);
        mTvRetweetCount = (TextView) convertView.findViewById(R.id.tv_retweetCount);
        mTvFavoriteCount = (TextView) convertView.findViewById(R.id.tv_favoriteCount);

        Picasso.with(getContext()).
                load(tweet.getUser().getProfileImageUrl())
                .into(mIvProfileImg);
        
        if (!tweet.getEntities().isEmpty()) {
            entitiesToClickableSpan(tweet);
        } else {
            mTvTweetText.setText(tweet.getText());
        }

        mTvDate.setText(tweet.getCreatedAt().toString());
        mTvRetweetCount.setText("" + tweet.getReTweetCount());
        mTvFavoriteCount.setText("" + tweet.getFavoriteCount());

        userIntent = new Intent(getContext(), ProfileActivity.class);

        mIvProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userIntent.putExtra(MainActivity.PROFILE_ID, tweet.getUser().getId());
                getContext().startActivity(userIntent);
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
}
