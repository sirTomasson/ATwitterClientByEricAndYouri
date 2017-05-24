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

    private WebView wvTwitter;
    private String authorizationUrl;

    public TwitterFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_fragment, container, false);

        wvTwitter = (WebView) view.findViewById(R.id.wv_twitterLogin);
        wvTwitter.setWebViewClient(new WebViewClient());
        wvTwitter.loadUrl(authorizationUrl);

        return view;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }
}

