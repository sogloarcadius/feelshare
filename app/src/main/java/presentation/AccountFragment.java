package presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import io.soglomania.feelshare.R;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class AccountFragment extends Fragment {

    private String userName = null;
    private String userID = null;
    private String eMail = null;

    private LoginButton loginButton;
    private TextView username_tv;
    private TextView email_tv;
    private ImageView imageView;


    private SharedPreferences sharedPref;


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

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


        username_tv = (TextView) view.findViewById(R.id.user_name_info);
        email_tv = (TextView) view.findViewById(R.id.user_mail_info);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        imageView = (ImageView) view.findViewById(R.id.user_pic);
        loginButton.setReadPermissions(Arrays.asList("user_status", "email"));


        userName = sharedPref.getString("userName", null);
        userID = sharedPref.getString("userID", null);
        eMail = sharedPref.getString("email", null);

        if (userName != null && eMail != null) {
            username_tv.setText(userName);
            email_tv.setText(eMail);
            username_tv.setVisibility(View.VISIBLE);
            email_tv.setVisibility(View.VISIBLE);
        }


        try {
            //Picasso nice for simple image manipulation : http://square.github.io/#android
            Picasso.with(getActivity()).load("https://graph.facebook.com/" + userID + "/picture?type=large").into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

