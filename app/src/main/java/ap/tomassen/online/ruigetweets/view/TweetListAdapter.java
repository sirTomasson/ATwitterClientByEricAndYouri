package ap.tomassen.online.ruigetweets.view;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import ap.tomassen.online.ruigetweets.R;
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

    public TweetListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Tweet> tweets) {
        super(context, resource, textViewResourceId, tweets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tweet tweet = getItem(position);

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

        mTvTweetText.setText(tweet.getText());
        mTvDate.setText(tweet.getCreatedAt().toString());
        mTvRetweetCount.setText("" + tweet.getReTweetCount());
        mTvFavoriteCount.setText("" + tweet.getFavoriteCount());

        return convertView;
    }
}
