package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by Eric on 4-6-2017.
 */

public class TimelineFragment extends Fragment {
    public TimelineFragmentCallbackListener listener;
    public TimelineFragment(){

    }

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        try {
            listener = (TimelineFragmentCallbackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface TimelineFragmentCallbackListener {

    }
}
