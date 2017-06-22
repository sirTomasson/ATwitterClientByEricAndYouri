package ap.tomassen.online.ruigetweets.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.view.StatusListAdapter;

/**
 * Created by Eric on 4-6-2017.
 */

public class TimelineFragment extends ListFragment {

    private TwitterModel model = TwitterModel.getInstance();



    public TimelineFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        setListAdapter(new StatusListAdapter(getContext(), R.layout.list_item_status, model.getStatuses()));

        return rootView;
    }

    public void updateStatuses() {
        ListAdapter listAdapter = getListAdapter();
        if (listAdapter instanceof StatusListAdapter)
            ((StatusListAdapter) listAdapter).notifyDataSetChanged();

    }
}
