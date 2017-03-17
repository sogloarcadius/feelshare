package presentation;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.soglomania.feelshare.R;

/**
 * Created by sogloarcadius on 15/03/17.
 */

public class MyDialogFragment extends DialogFragment {


    public static MyDialogFragment newInstance(String title) {
        MyDialogFragment dialog = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        Button button = (Button) v.findViewById(R.id.share_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog("dialog");
            }
        });


        return v;
    }


    public void showDialog(String titre) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance(titre);
        newFragment.show(ft, "dialog");
        getDialog().setTitle(getArguments().getString("title"));

    }
}


