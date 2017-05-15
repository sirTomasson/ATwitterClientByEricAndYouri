package ap.tomassen.online.ruigetweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ap.tomassen.online.ruigetweets.R;

/**
 * Created by Eric on 10-5-2017.
 */

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        if (intent.hasExtra(MainActivity.PROFILE_ID)) {
            int id = intent.getIntExtra(MainActivity.PROFILE_ID, -1);

            if (id != -1) {

            }
        }
    }
}
