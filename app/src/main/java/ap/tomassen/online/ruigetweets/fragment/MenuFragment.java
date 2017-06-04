package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.ProfileActivity;

/**
 * Created by Eric on 25-5-2017.
 */

public class MenuFragment extends Fragment {
    final public static String PROFILE_INTENT="ShowProfile";
    public CallBackListener listener;

    private ImageView ivAddTweet;

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

        ImageView ivViewProfile = (ImageView) view.findViewById(R.id.iv_view_profile);
        ivViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);

                listener.showProfile();
            }
        });
        ImageView ivTwitterFeed = (ImageView) view.findViewById(R.id.iv_twitter_feed);
        ivTwitterFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);

                listener.showTimeLine();
            }
        });

        ivAddTweet = (ImageView) view.findViewById(R.id.iv_add_tweet);
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
