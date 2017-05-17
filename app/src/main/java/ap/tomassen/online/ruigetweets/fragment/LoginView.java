package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ap.tomassen.online.ruigetweets.R;

/**
 * Created by Eric on 17-5-2017.
 */

public class LoginView extends Fragment implements View.OnClickListener{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_view, container, false);

        Button btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        TwitterView twitterView=new TwitterView();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, twitterView);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
