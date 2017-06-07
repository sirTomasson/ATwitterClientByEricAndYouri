package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.ProfileActivity;

/**
 * Created by Eric on 25-5-2017.
 */

public class MenuFragment extends Fragment {
    final public static String PROFILE_INTENT="ShowProfile";
    public CallBackListener listener;

    private LinearLayout ivAddTweet;
    private LinearLayout llViewProfile;
    private LinearLayout llViewTimeline;

    public MenuFragment (){
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

        llViewProfile = (LinearLayout) view.findViewById(R.id.ll_go_to_profile);
        llViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.showProfile();
            }
        });
        llViewTimeline = (LinearLayout) view.findViewById(R.id.ll_go_to_timeline);
        llViewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.showTimeLine();
            }
        });

        ivAddTweet = (LinearLayout) view.findViewById(R.id.ll_add_tweet);
        ivAddTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.createNewTweet();
            }
        });

        return view;
    }

    public interface CallBackListener {
        void createNewTweet();
        void showProfile();
        void showTimeLine();
    }
}
