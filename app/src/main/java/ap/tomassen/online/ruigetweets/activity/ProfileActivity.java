package ap.tomassen.online.ruigetweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.model.User;

/**
 * Created by Eric on 10-5-2017.
 */

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileBackground;
    private ImageView profileImg;
    private TextView name;
    private TextView screenName;
    private TextView description;
    private TextView followers;
    private TextView favorites;
    private TextView location;


    private TwitterModel model = TwitterModel.getInstance();


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
    }

    private void initXmlElements() {

        profileImg = (ImageView) findViewById(R.id.iv_profileImg);
        name = (TextView) findViewById(R.id.tv_name);
        screenName = (TextView) findViewById(R.id.tv_sceenName);
        description = (TextView) findViewById(R.id.tv_description);
        followers = (TextView) findViewById(R.id.tv_followers);
        favorites = (TextView) findViewById(R.id.tv_favorites);
        location = (TextView) findViewById(R.id.tv_location);
        profileBackground = (ImageView) findViewById(R.id.iv_profileBackground);

    }

    private void setContent(User u) {

        Picasso.with(this).
                load(u.getProfileImageUrl())
                .into(profileImg);

        Picasso.with(this)
                .load(u.getProfileBackgroundUrl())
                .into(profileBackground);

        name.setText(u.getName());
        screenName.setText(u.getScreenName());
        description.setText(u.getDescription());
        followers.setText(u.getFollowers());
        favorites.setText(u.getRetweets());
        location.setText(u.getLocation());
    }
}
