package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by Eric on 4-6-2017.
 */

public class ProfileFragment extends Fragment {
    public ProfileFragmentCallbackListener listener;
    public ProfileFragment(){

    }
    @Override
    public void onAttach (Context context){
        super.onAttach(context);

        try {
            listener = (ProfileFragmentCallbackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface ProfileFragmentCallbackListener {
        
    }
}
