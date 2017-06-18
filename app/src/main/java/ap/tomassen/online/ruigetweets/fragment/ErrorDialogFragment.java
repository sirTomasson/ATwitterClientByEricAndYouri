package ap.tomassen.online.ruigetweets.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import ap.tomassen.online.ruigetweets.activity.MainActivity;

/**
 * Created by youri on 18-6-2017.
 */

public class ErrorDialogFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        if (b != null && b.containsKey(MainActivity.ERROR_TITLE)) {
            alertDialog.setTitle(b.getString(MainActivity.ERROR_TITLE));
            alertDialog.setMessage(b.getString(MainActivity.ERROR_MESSAGE));
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return alertDialog;
    }
}