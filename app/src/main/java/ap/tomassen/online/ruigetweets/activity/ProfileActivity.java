package ap.tomassen.online.ruigetweets.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.model.User;

/**
 * Created by Eric on 10-5-2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private FrameLayout profileBackground;
    private ImageView profileImg;
    private TextView name;
    private TextView screenName;
    private TextView description;
    private TextView followers;
    private TextView retweets;
    private TextView location;
    private TwitterModel model = TwitterModel.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        if (intent.hasExtra(MainActivity.PROFILE_ID)){
            int id = intent.getIntExtra(MainActivity.PROFILE_ID, -1);
            if (id != -1){
                User u = model.getUser(id);
                URL profileImgUrl = null;
                try {
                    profileImgUrl = new URL(u.getProfileImageUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(profileImgUrl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }/*
                URL profileBackgroundUrl = null;
                try {
                    profileBackgroundUrl = new URL(u.getProfileBackgroundUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(profileBackgroundUrl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BitmapDrawable background = new BitmapDrawable(getResources(), bitmap);*/

                /*profileBackground = (FrameLayout) findViewById(R.id.rl_profileBackground);*/
                profileImg = (ImageView) findViewById(R.id.iv_profileImg);
                name = (TextView) findViewById(R.id.tv_name);
                screenName = (TextView) findViewById(R.id.tv_sceenName);
                description = (TextView) findViewById(R.id.tv_description);
                followers = (TextView) findViewById(R.id.tv_followers);
                retweets = (TextView) findViewById(R.id.tv_retweets);
                location = (TextView) findViewById(R.id.tv_location);

                /*profileBackground.setBackground(background);*/
                profileImg.setImageBitmap(bmp);
                name.setText(u.getName());
                screenName.setText(u.getScreenName());
                description.setText(u.getDescription());
                followers.setText(u.getFollowers());
                retweets.setText(u.getRetweets());
                location.setText(u.getLocation());
            }
        }
    }
}
