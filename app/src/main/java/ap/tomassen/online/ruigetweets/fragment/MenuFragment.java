package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ap.tomassen.online.ruigetweets.R;

public class MenuFragment extends Fragment {



    public CallBackListener listener;

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

        LinearLayout llAddTweet = (LinearLayout) view.findViewById(R.id.ll_add_tweet);
        llAddTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.createNewTweet();
            }
        });

        LinearLayout llSearchTweet = (LinearLayout) view.findViewById(R.id.ll_search_tweet);
        llSearchTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.showSearchTweet();
            }
        });

        LinearLayout llViewTimeline = (LinearLayout) view.findViewById(R.id.ll_go_to_timeline);
        llViewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = listener.currentFragment();
                if (f instanceof ProfileFragment) {
                    if (currentList == USER_TIME_LINE) {
                        listener.showFavoritesList();
                        ((ProfileFragment) f).changeListHeader();
                        ivTwitterFeed.setImageResource(R.drawable.twitterblue);
                        currentList = FAVORITE_LIST;
                    } else if (currentList == FAVORITE_LIST) {
                        ((ProfileFragment) f).changeListHeader();
                        ivTwitterFeed.setImageResource(R.drawable.favorite);
                        listener.showUserTimeline();
                        currentList = USER_TIME_LINE;
                    }
                } else {
                    listener.showHomeTimeline();
                }
            }
        });

        LinearLayout llViewProfile = (LinearLayout) view.findViewById(R.id.ll_go_to_profile);
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

        void showProfile();

        void showUserTimeline();

        void showFavoritesList();

        void showHomeTimeline();

        Fragment currentFragment();
    }
}
