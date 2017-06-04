package ap.tomassen.online.ruigetweets.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.exception.ProfileException;
import ap.tomassen.online.ruigetweets.fragment.MenuFragment;
import ap.tomassen.online.ruigetweets.model.Profile;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.model.User;

/**
 * Created by Eric on 10-5-2017.
 */

public class ProfileActivity extends AppCompatActivity implements MenuFragment.MenuFragmentCallBackListener {

    private ImageView ivProfileBackground;
    private ImageView ivProfileImg;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvDescription;
    private TextView tvFollowersCount;
    private TextView tvFavoritesCount;
    private TextView tvLocation;


    private TwitterModel model = TwitterModel.getInstance();
    private Profile profile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.PROFILE_ID)){
            int id = intent.getIntExtra(MainActivity.PROFILE_ID, -1);
            if (id != -1) {
                User u = model.getUser(id);

                initXmlElements();
                setContent(u);
            }
        }
        else {
            Profile u = null;
            try {
                u = Profile.getInstance();
            } catch (ProfileException e) {
                e.printStackTrace();
            }
            initXmlElements();
            setContent(u);
        }
    }

    private void initXmlElements() {

        ivProfileImg = (ImageView) findViewById(R.id.iv_profile_img);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvScreenName = (TextView) findViewById(R.id.tv_sceen_name);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvFollowersCount = (TextView) findViewById(R.id.tv_followers_count);
        tvFavoritesCount = (TextView) findViewById(R.id.tv_favorites_count);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        ivProfileBackground = (ImageView) findViewById(R.id.iv_profile_background);

    }

    private void setContent(User u) {

        Picasso.with(this).
                load(u.getProfileImageUrl())
                .into(ivProfileImg);

        Picasso.with(this)
                .load(u.getProfileBackgroundUrl())
                .into(ivProfileBackground);

        tvName.setText(u.getName());
        tvScreenName.setText(u.getScreenName());
        tvDescription.setText(u.getDescription());
        tvFollowersCount.setText(u.getFollowersCount());
        tvFavoritesCount.setText(u.getFavoritesCount());
        tvLocation.setText(u.getLocation());
    }
}
