package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ap.tomassen.online.ruigetweets.R;

/**
 * Created by Eric on 25-5-2017.
 */

public class MenuFragment extends Fragment {


    private final String TAG = MenuFragment.class.getSimpleName();

    public static final String PROFILE_INTENT = "ShowProfile";
    public CallBackListener listener;

    private LinearLayout llAddTweet;
    private LinearLayout llSearchTweet;
    private LinearLayout llViewTimeline;
    private LinearLayout llViewProfile;

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


        View view = inflater.inflate(R.layout.menu_fragment, container, false);

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
                        Log.d(TAG, "onClick: favorites");
                        listener.showFavoritesList();
                        currentList = FAVORITE_LIST;
                    } else if (currentList == FAVORITE_LIST) {
                        Log.d(TAG, "onClick: user time line");
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
                    Log.d(TAG, "onClick: back to home time line");
                    listener.showHomeTimeline();
                } else {
                    listener.showProfile();
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
