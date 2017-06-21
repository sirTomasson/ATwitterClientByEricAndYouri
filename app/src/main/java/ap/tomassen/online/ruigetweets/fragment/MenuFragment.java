package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ap.tomassen.online.ruigetweets.R;

/**
 * Created by Eric on 25-5-2017.
 */

public class MenuFragment extends Fragment {


    private final String TAG = MenuFragment.class.getSimpleName();

    public CallBackListener listener;

    private LinearLayout llAddTweet;
    private LinearLayout llSearchTweet;
    private LinearLayout llViewTimeline;
    private LinearLayout llViewProfile;
    private ImageView ivTwitterFeed;
    private ImageView ivProfile;

    private final int USER_TIME_LINE = 42;
    private final int FAVORITE_LIST = 33;
    private int currentList = USER_TIME_LINE;

    public MenuFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CallBackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);


        ivTwitterFeed = (ImageView) view.findViewById(R.id.iv_twitter_feed);
        ivProfile = (ImageView) view.findViewById(R.id.iv_view_profile);

        llAddTweet = (LinearLayout) view.findViewById(R.id.ll_add_tweet);
        llAddTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.createNewTweet();
            }
        });

        llSearchTweet = (LinearLayout) view.findViewById(R.id.ll_search_tweet);
        llSearchTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.showSearchTweet();
            }
        });

        llViewTimeline = (LinearLayout) view.findViewById(R.id.ll_go_to_timeline);
        llViewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = listener.currentFragment();
                if (f instanceof ProfileFragment) {
                    if (currentList == USER_TIME_LINE) {
                        ivTwitterFeed.setImageResource(R.drawable.twitterblue);
                        listener.showFavoritesList();
                        currentList = FAVORITE_LIST;
                    } else if (currentList == FAVORITE_LIST) {
                        ivTwitterFeed.setImageResource(R.drawable.favorite);
                        listener.showUserTimeline();
                        currentList = USER_TIME_LINE;
                    }
                } else {
                    listener.refreshTimeline();
                }
            }
        });

        llViewProfile = (LinearLayout) view.findViewById(R.id.ll_go_to_profile);
        llViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = listener.currentFragment();
                if (f instanceof ProfileFragment) {
                    listener.showHomeTimeline();
                    ivProfile.setImageResource(R.drawable.user);
                    ivTwitterFeed.setImageResource(R.drawable.twitterblue);
                } else {
                    ivProfile.setImageResource(R.drawable.back_button);
                    listener.showProfile();

                    ivTwitterFeed.setImageResource(R.drawable.favorite);
                    listener.showUserTimeline();
                    currentList = USER_TIME_LINE;
                }
            }
        });

        return view;
    }

    public interface CallBackListener {
        void createNewTweet();

        void showSearchTweet();

        void refreshTimeline();

        void showProfile();

        void showUserTimeline();

        void showFavoritesList();

        void showHomeTimeline();

        Fragment currentFragment();
    }
}
