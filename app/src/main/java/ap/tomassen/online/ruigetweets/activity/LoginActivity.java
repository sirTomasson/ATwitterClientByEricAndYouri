package ap.tomassen.online.ruigetweets.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.fragment.LoginView;

/**
 * Created by youri on 17-5-2017.
 */

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginView loginView=new LoginView();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_layout, loginView);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
