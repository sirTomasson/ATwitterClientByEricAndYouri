package ap.tomassen.online.ruigetweets.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 31-5-2017.
 */

public class TimeLineActivity extends Activity {

    private TwitterModel model = TwitterModel.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}
