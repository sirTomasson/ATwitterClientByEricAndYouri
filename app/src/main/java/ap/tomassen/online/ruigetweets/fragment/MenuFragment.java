package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by Eric on 25-5-2017.
 */

public class MenuFragment extends Fragment {

    private MenuFragmentCallBackListener listener;

    public MenuFragment (){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MenuFragmentCallBackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }



    private interface MenuFragmentCallBackListener {

    }
}
