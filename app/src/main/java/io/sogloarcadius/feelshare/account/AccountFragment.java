package io.sogloarcadius.feelshare.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import io.sogloarcadius.feelshare.R;
import io.sogloarcadius.feelshare.preferences.PreferenceActivity;


public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";
    private Button mAccountInfo;
    private Button mAccountPreferences;


    public AccountFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance(String title) {
        //allow us to not forget to pass needed parameter to our fragment
        Fragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAccountInfo = (Button) view.findViewById(R.id.account_info_login);
        mAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser authenticatedUser = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = authenticatedUser.getEmail();
                if (userEmail == null || userEmail.isEmpty()){
                    for (UserInfo profile : authenticatedUser.getProviderData()){
                        userEmail = profile.getEmail();
                    }
                }
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.ic_account_circle_black_24dp)
                        .setTitle(R.string.title_account)
                        .setMessage(Html.fromHtml(getString(R.string.account_info, userEmail, authenticatedUser.getDisplayName())))
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //back to main activity
                                getActivity();
                            }
                        })
                        .show();
            }
        });

        mAccountPreferences = (Button) view.findViewById(R.id.account_preferences);
        mAccountPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), PreferenceActivity.class);
                startActivity(mIntent);
            }
        });


    }
}

