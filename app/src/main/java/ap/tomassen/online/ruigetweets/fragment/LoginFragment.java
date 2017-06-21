package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ap.tomassen.online.ruigetweets.R;

/**
 * Created by Eric on 17-5-2017.
 */

public class LoginFragment extends Fragment {

    private LoginFragmentCallbackListener listener;

    public LoginFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (LoginFragmentCallbackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLoginClick();
            }
        });

        return view;
    }

    public interface LoginFragmentCallbackListener {
        void onLoginClick();
    }
}
