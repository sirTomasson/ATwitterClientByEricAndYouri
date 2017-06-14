package ap.tomassen.online.ruigetweets.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.view.StatusListAdapter;

/**
 * Created by Eric on 4-6-2017.
 */

public class TimelineFragment extends ListFragment {

    private TwitterModel model = TwitterModel.getInstance();

    public CallbackListener listener;


    public TimelineFragment(){

    }

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        try {
            listener = (CallbackListener) context;
        } catch (ClassCastException e) {
            System.err.println("host activity has to implement call back methods");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        setListAdapter(new StatusListAdapter(getContext(), R.layout.list_item, model.getStatuses()));

        return rootView;
    }

    public interface CallbackListener {
        void onStatusSelected();
        void onUrlSelected();
        void onMentionSelected();
        void onUserSelected();
    }
}
