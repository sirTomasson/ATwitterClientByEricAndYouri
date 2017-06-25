package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.activity.LoginActivity;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;

import static android.content.ContentValues.TAG;

public class AuthorizationFragment extends Fragment {

    public static final String AUTHORIZATION_URL_KEY = "AUTHORIZATION_URL_KEY";
    private CallbackListener listener;


    private String authorizationUrl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CallbackListener) context;
        } catch (ClassCastException e) {
            System.err.println("CallbackListener should be implemented in host activity");
        }
    }

    public AuthorizationFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter, container, false);

        Bundle b = getArguments();

        if (b != null && b.containsKey(AUTHORIZATION_URL_KEY)) {
            String url = b.getString(AUTHORIZATION_URL_KEY, "bad url");
            if (!url.equals("bad url")) authorizationUrl = url;
        }

        WebView wvAuthorization = (WebView) view.findViewById(R.id.wv_twitterLogin);
        wvAuthorization.loadUrl(authorizationUrl);

        wvAuthorization.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith(MyTwitterApi.CALL_BACK_URL+"?oath_token")) {

                    Log.d(TAG, "shouldOverrideUrlLoading: " + url);

                    int start = url.indexOf("=");

                    String s = url.substring(start + 1);

                    start = s.indexOf("&");

                    String verifier = s.substring(start + 1);

                    start = verifier.indexOf("=");

                    verifier = verifier.substring(start + 1);

                    listener.authorize(verifier);
                } else {
                    listener.showLoginFragment();
                }
                return false;
            }
        });

        return view;
    }


    public interface CallbackListener {
        /**
         * Use the verifier to retrieve a access token. MORE? see AccessTokenTask in MainActivity
         * @param verifier that was returned with the callback url
         */
        void authorize(String verifier);

        /**
         * when login fails, reload the login fragment and get a new request token so the user can
         * try again
         */
        void showLoginFragment();
    }
}

