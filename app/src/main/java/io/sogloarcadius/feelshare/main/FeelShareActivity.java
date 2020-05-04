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
import io.sogloarcadius.feelshare.mood.MoodFragment;
import io.sogloarcadius.feelshare.support.WebViewActivity;

public class FeelShareActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;

    private Integer[] moodsUID;
    private String[] moodsNames;
    private String[] moodsDesc;
    private Integer[] moodsImages;

    private final String TAG = "FeelShareMainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private List fragments = new Vector();
    private FeelShareApplication context;

    // constructeur
    public FeelShareActivity() {
        Log.d(TAG, "constructor");
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
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };


        //references to moods ID
        moodsUID = new Integer[]{
                0,
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10,
                11,
                12,
                13,
                14,
                15,
                16,
                17,
                18,
                19,
                20,
                21
        };

        // ==references to our images title
        moodsNames = new String[]{
                getResources().getString(R.string.afraid),
                getResources().getString(R.string.angry),
                getResources().getString(R.string.bore),
                getResources().getString(R.string.naughty),
                getResources().getString(R.string.confused),
                getResources().getString(R.string.cool),
                getResources().getString(R.string.crying),
                getResources().getString(R.string.depression),
                getResources().getString(R.string.excited),
                getResources().getString(R.string.frustated),
                getResources().getString(R.string.funny),
                getResources().getString(R.string.happy),
                getResources().getString(R.string.hungry),
                getResources().getString(R.string.neutral),
                getResources().getString(R.string.romantic),
                getResources().getString(R.string.sad),
                getResources().getString(R.string.scared),
                getResources().getString(R.string.shy),
                getResources().getString(R.string.sick),
                getResources().getString(R.string.sleepy),
                getResources().getString(R.string.surprised),
                getResources().getString(R.string.tired)
        };

        //references to our images
        moodsImages = new Integer[]{
                R.drawable.afraid,
                R.drawable.angry,
                R.drawable.bored,
                R.drawable.childish,
                R.drawable.confused,
                R.drawable.cool,
                R.drawable.crying,
                R.drawable.depressed,
                R.drawable.excited,
                R.drawable.frustrated,
                R.drawable.funny,
                R.drawable.happy,
                R.drawable.hungry,
                R.drawable.neutral,
                R.drawable.romantic,
                R.drawable.sad,
                R.drawable.scared,
                R.drawable.shy,
                R.drawable.sick,
                R.drawable.sleepy,
                R.drawable.surprised,
                R.drawable.tired,
        };

        //reference to descriptions
        moodsDesc = new String[]{
                getResources().getString(R.string.afraid_desc),
                getResources().getString(R.string.angry_desc),
                getResources().getString(R.string.bore_desc),
                getResources().getString(R.string.naughty_desc),
                getResources().getString(R.string.confused_desc),
                getResources().getString(R.string.cool_desc),
                getResources().getString(R.string.crying_desc),
                getResources().getString(R.string.depression_desc),
                getResources().getString(R.string.excited_desc),
                getResources().getString(R.string.frustated_desc),
                getResources().getString(R.string.funny_desc),
                getResources().getString(R.string.happy_desc),
                getResources().getString(R.string.hungry_desc),
                getResources().getString(R.string.neutral_desc),
                getResources().getString(R.string.romantic_desc),
                getResources().getString(R.string.sad_desc),
                getResources().getString(R.string.scared_desc),
                getResources().getString(R.string.shy_desc),
                getResources().getString(R.string.sick_desc),
                getResources().getString(R.string.sleepy_desc),
                getResources().getString(R.string.surprised_desc),
                getResources().getString(R.string.tired_desc)
        };

        context = ((FeelShareApplication) getApplicationContext());
        context.setMoodsDesc(moodsDesc);
        context.setMoodsImages(moodsImages);
        context.setMoodsNames(moodsNames);
        context.setMoodsUID(moodsUID);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragments.add(AccountFragment.newInstance("Account"));
        fragments.add(MoodFragment.newInstance("Feelings"));
        fragments.add(UserChartFragment.newInstance("User"));
        fragments.add(WorldChartFragment.newInstance("World"));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), fragments);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "Création menu en cours");
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
