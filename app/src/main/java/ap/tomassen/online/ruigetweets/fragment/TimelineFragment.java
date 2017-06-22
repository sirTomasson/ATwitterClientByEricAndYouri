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
 * A list of tweets that is displayed on the main screen whenever the app opens
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

    /**
     * changes the statuses that reside in the ListAdapter to newer Statuses or Statuses that belong
     * to a different collection like a list of tweets that were favorited by the user
     */
    public void updateStatuses() {
        ListAdapter listAdapter = getListAdapter();
        if (listAdapter instanceof StatusListAdapter)
            ((StatusListAdapter) listAdapter).notifyDataSetChanged();

    }
}
