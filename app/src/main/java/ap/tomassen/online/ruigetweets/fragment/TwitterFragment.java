package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ap.tomassen.online.ruigetweets.R;

/**
 * Created by Eric on 17-5-2017.
 */

public class TwitterFragment extends Fragment {

    private TwitterFragmentCallbackListener context;

    public TwitterFragment() {
    }

    public interface TwitterFragmentCallbackListener {
        void onItemSelected();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_fragment, container, false);

        WebView wvTwitter = (WebView) view.findViewById(R.id.wv_twitterLogin);
        wvTwitter.setWebViewClient(new WebViewClient());

        return view;
    }
}

