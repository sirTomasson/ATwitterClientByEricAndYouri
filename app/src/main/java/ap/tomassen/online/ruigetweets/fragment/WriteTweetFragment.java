package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 4-6-2017.
 */

public class WriteTweetFragment extends Fragment {

    private final String TAG = WriteTweetFragment.class.getSimpleName();

    /*====================================================*/

    private MyTwitterApi api = MyTwitterApi.getInstance();
    private TwitterModel model = TwitterModel.getInstance();

    private final int STATUS_LENGTH = 140;
    private int charCount = STATUS_LENGTH;

    private EditText etWriteTweet;
    private Button btnSendTweet;
    private TextView tvDateTime, tvCharCount;


    private CallbackListener listener;

    /*=====================================================*/


    public interface CallbackListener {
        void sendTweet(String tweet);
    }

    public WriteTweetFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
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
        View rootView = inflater.inflate(R.layout.fragment_write_tweet, container, false);

        tvCharCount = (TextView) rootView.findViewById(R.id.tv_char_count);
        tvCharCount.setText(STATUS_LENGTH + "");
        tvDateTime = (TextView) rootView.findViewById(R.id.tv_date_time);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);

        tvDateTime.setText(s);

        Log.i(TAG, "onCreateView: date " + s);


        etWriteTweet = (EditText) rootView.findViewById(R.id.et_write_tweet);


        etWriteTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (STATUS_LENGTH > 0) {
                    charCount = STATUS_LENGTH - charSequence.length();

                    tvCharCount.setText(charCount + "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnSendTweet = (Button) rootView.findViewById(R.id.btn_send_tweet);
        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etWriteTweet.getText().toString();

                listener.sendTweet(tweet);
            }
        });

        return rootView;
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
}
