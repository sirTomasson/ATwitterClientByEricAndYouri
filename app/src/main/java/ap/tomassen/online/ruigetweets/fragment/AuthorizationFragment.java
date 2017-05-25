package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.scribejava.apis.TwitterApi;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;

/**
 * Created by Eric on 17-5-2017.
 */

public class AuthorizationFragment extends Fragment {

    private AuthorizationCallbackListener listener;

    private final String TAG = AuthorizationFragment.class.getSimpleName();

    private String authorizationUrl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AuthorizationCallbackListener) context;
        } catch (ClassCastException e) {
            System.err.println("CallbackListener should be implemented in host activity");
        }
    }

    public AuthorizationFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_fragment, container, false);

        WebView wvAuthorization = (WebView) view.findViewById(R.id.wv_twitterLogin);
        wvAuthorization.loadUrl(authorizationUrl);

        wvAuthorization.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith(MyTwitterApi.CALL_BACK_URL)) {

                    int start = url.indexOf("=");

                    String s = url.substring(start + 1);

                    start = s.indexOf("&");

                    String verifier = s.substring(start + 1);

                    start = verifier.indexOf("=");

                    verifier = verifier.substring(start + 1);

                    Log.i(TAG, "shouldOverrideUrlLoading: url " + url);

                    Log.i(TAG, "shouldOverrideUrlLoading: verifier " + verifier);

                    listener.AuthorizationListener(verifier);


                }
                return false;
            }
        });


        return view;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }


    public interface AuthorizationCallbackListener {
        void AuthorizationListener(String verifier);
    }
}

