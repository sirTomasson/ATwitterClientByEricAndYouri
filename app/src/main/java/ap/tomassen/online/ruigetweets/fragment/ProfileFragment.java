package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.exception.ProfileException;
import ap.tomassen.online.ruigetweets.model.Profile;
import ap.tomassen.online.ruigetweets.model.Tweet;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.model.User;
import ap.tomassen.online.ruigetweets.view.StatusListAdapter;


public class ProfileFragment extends Fragment {


    private ImageView ivProfileBackground;
    private ImageView ivProfileImg;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvDescription;
    private TextView tvFollowersCount;
    private TextView tvFavoritesCount;
    private TextView tvLocation;
    private ListView lvProfile;


    private final String TAG = ProfileFragment.class.getSimpleName();

    private CallbackListener listener;

    private TwitterModel model = TwitterModel.getInstance();

    public ProfileFragment(){

    }

    public interface CallbackListener {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach (Context context){
        super.onAttach(context);

        try {
            listener = (CallbackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initXmlElements(rootView);
        try {
            setContent(Profile.getInstance());
        } catch (ProfileException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void initXmlElements(View rootView) {

        ivProfileImg = (ImageView) rootView.findViewById(R.id.iv_profile_img);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvScreenName = (TextView) rootView.findViewById(R.id.tv_sceen_name);
        tvDescription = (TextView) rootView.findViewById(R.id.tv_description);
        tvFollowersCount = (TextView) rootView.findViewById(R.id.tv_followers_count);
        tvFavoritesCount = (TextView) rootView.findViewById(R.id.tv_favorites_count);
        tvLocation = (TextView) rootView.findViewById(R.id.tv_location);
        ivProfileBackground = (ImageView) rootView.findViewById(R.id.iv_profile_background);
        lvProfile  = (ListView) rootView.findViewById(R.id.lv_profile);
    }

    private void setContent(User u) {

        Picasso.with(getContext()).
                load(u.getProfileImageUrl())
                .into(ivProfileImg);

        Picasso.with(getContext())
                .load(u.getProfileBackgroundUrl())
                .into(ivProfileBackground);

        tvName.setText(u.getName());
        tvScreenName.setText(u.getScreenName());
        tvDescription.setText(u.getDescription());
        tvFollowersCount.setText(u.getFollowersCount());
        tvFavoritesCount.setText(u.getFavoritesCount());
        tvLocation.setText(u.getLocation());
        lvProfile.setAdapter(new StatusListAdapter(getContext(), R.layout.list_item, model.getStatuses()));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void changeListContents(List<Tweet> tweets) {
        Object adapter = lvProfile.getAdapter();
        if (adapter instanceof StatusListAdapter) {
            StatusListAdapter arrayAdapter = (StatusListAdapter) adapter;
            arrayAdapter.clear();
            arrayAdapter.addAll(tweets);
        }
    }
}
