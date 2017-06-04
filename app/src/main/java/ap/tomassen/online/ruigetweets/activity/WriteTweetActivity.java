package ap.tomassen.online.ruigetweets.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.scribejava.core.model.OAuth1AccessToken;

import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 4-6-2017.
 */

public class WriteTweetActivity extends Activity {

    private TwitterModel model = TwitterModel.getInstance();
    private MyTwitterApi api =  MyTwitterApi.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OAuth1AccessToken accessToken = api.getAccessToken();
    }
    private class WriteTweetTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {



            return null;
        }
    }
}
