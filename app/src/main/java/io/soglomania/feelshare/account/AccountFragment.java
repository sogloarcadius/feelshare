package io.soglomania.feelshare.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import io.soglomania.feelshare.R;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class AccountFragment extends Fragment {

    private LoginButton loginButtonFacebook;


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

        loginButtonFacebook = (LoginButton) view.findViewById(R.id.login_button_facebook);
        loginButtonFacebook.setReadPermissions(Arrays.asList("user_status", "email"));

    }


}

