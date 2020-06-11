package io.sogloarcadius.feelshare.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import io.sogloarcadius.feelshare.BuildConfig;
import io.sogloarcadius.feelshare.R;
import io.sogloarcadius.feelshare.account.AccountFragment;
import io.sogloarcadius.feelshare.chart.UserChartFragment;
import io.sogloarcadius.feelshare.chart.WorldChartFragment;
import io.sogloarcadius.feelshare.map.MapActivity;
import io.sogloarcadius.feelshare.mood.MoodFragment;
import io.sogloarcadius.feelshare.preference.PreferenceActivity;
import io.sogloarcadius.feelshare.support.WebViewActivity;

public class FeelShareActivity extends AppCompatActivity {

    private final String TAG = "FeelShareMainActivity";
    private static final int RC_SIGN_IN = 1;

    // firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private List fragments = new Vector();

    public FeelShareActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user signed in
                } else {
                    //user signed out
                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                            new AuthUI.IdpConfig.FacebookBuilder().build(),
                            new AuthUI.IdpConfig.TwitterBuilder().build()
                    );

                    // Create and launch sign-in intent
                    AuthMethodPickerLayout firebaseAuthMethodPickerLayout = new AuthMethodPickerLayout
                            .Builder(R.layout.firebase_auth_picker)
                            .setEmailButtonId(R.id.email_provider)
                            .setGoogleButtonId(R.id.google_provider)
                            .setFacebookButtonId(R.id.facebook_provider)
                            .setTwitterButtonId(R.id.twitter_provider)
                            .build();


                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                                    .setAvailableProviders(providers)
                                    .setTheme(R.style.AppTheme)
                                    .setAuthMethodPickerLayout(firebaseAuthMethodPickerLayout)
                                    .setTosAndPrivacyPolicyUrls(getString(R.string.privacy_url), getString(R.string.privacy_url))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragments.add(AccountFragment.newInstance("Account"));
        fragments.add(MoodFragment.newInstance("Feelings"));
        fragments.add(UserChartFragment.newInstance("User"));
        fragments.add(WorldChartFragment.newInstance("World"));
        //fragments.add(MapViewFragment.newInstance("Map"));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), fragments);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_licence) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("file", getString(R.string.conditions_file).toLowerCase());
            intent.putExtra("title", getString(R.string.conditions).toLowerCase());
            startActivity(intent);
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("file", getString(R.string.about_file).toLowerCase());
            intent.putExtra("title", getString(R.string.about).toLowerCase());

            startActivity(intent);
        }

        if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            startActivity(Intent.createChooser(intent, getString(R.string.share_app_select)));
        }

        if (id == R.id.action_map){
            Intent mIntent = new Intent(this, MapActivity.class);
            startActivity(mIntent);
        }

        // parent
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                    .setTitle(R.string.exit_dialog_title)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Stop the activity
                            FeelShareActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        if (mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                // Successfully signed in
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, getString(R.string.account_success_login_message, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()), Toast.LENGTH_SHORT).show();
                }
                // signed failed
                else {
                    if (response != null && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Toast.makeText(this, getString(R.string.account_network_error), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(this, getString(R.string.account_signin_cancelled), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
    }




}
