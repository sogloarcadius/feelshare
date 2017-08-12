package io.soglomania.appfeelshare.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Vector;

import io.realm.SyncConfiguration;
import io.soglomania.appfeelshare.R;
import io.soglomania.appfeelshare.account.AccountFragment;
import io.soglomania.appfeelshare.charts.UserChartsFragment;
import io.soglomania.appfeelshare.charts.WorldChartsFragment;
import io.soglomania.appfeelshare.feelings.FeelingsFragment;
import io.soglomania.appfeelshare.support.WebViewActivity;

public class MainActivity extends AppCompatActivity {


    private String userID;
    private String userName;
    private String email;

    private Integer[] moodsUID;
    private String[] moodsNames;
    private String[] moodsDesc;
    private Integer[] moodsImages;

    SyncConfiguration syncConfiguration;

    CallbackManager callbackManager;

    private SharedPreferences sharedPref;
    AccessTokenTracker accessTokenTracker;

    // le gestionnaire de fragments
    private SectionsPagerAdapter mSectionsPagerAdapter;

    // le conteneur de fragments
    private ViewPager mViewPager;

    // Création de la liste de Fragments que fera défiler le PagerAdapter
    List fragments = new Vector();


    public MyApplication context;

    // constructeur
    public MainActivity() {
        Log.d(MyApplication.TAG, "constructor");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MyApplication.TAG, "onCreate");



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

        context = ((MyApplication) getApplicationContext());
        context.setMoodsDesc(moodsDesc);
        context.setMoodsImages(moodsImages);
        context.setMoodsNames(moodsNames);
        context.setMoodsUID(moodsUID);

        // parent
        super.onCreate(savedInstanceState);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        // vue
        setContentView(R.layout.activity_main);

        // barre d'outils
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        callbackManager = CallbackManager.Factory.create();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    userID = (String) object.get("id");
                                    userName = (String) object.get("name");
                                    email = (String) object.get("email");
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("userID", userID);
                                    editor.putString("userName", userName);
                                    editor.putString("email", email);
                                    editor.apply(); //handle it in background

                                    Toast.makeText(getApplicationContext(), getString(R.string.success_login_message, userName), Toast.LENGTH_LONG).show();
                                    Log.v("LoginActivity", response.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(MyApplication.TAG, getString(R.string.facebook_error));

            }
        });


        // Ajout des Fragments dans la liste
        fragments.add(AccountFragment.newInstance("Account"));
        fragments.add(FeelingsFragment.newInstance("Feelings"));
        fragments.add(UserChartsFragment.newInstance("User"));
        fragments.add(WorldChartsFragment.newInstance("World"));

        // le gestionnaire de fragments
        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), fragments);


        // le conteneur de fragments est associé au gestionnaire de fragments
        // ç-à-d que le fragment n° i du conteneur de fragments est le fragment n° i délivré par le gestionnaire de fragments
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // la barre d'onglets est également associée au conteneur de fragments
        // ç-à-d que l'onglet n° i affiche le fragment n° i du conteneur
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {

                    //User logged out
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("userID").remove("userName").remove("email");

                    editor.apply();//handle it in background
                    Toast.makeText(getApplicationContext(), getString(R.string.success_logout_message, userName), Toast.LENGTH_LONG).show();


                }
            }
        };

        accessTokenTracker.startTracking();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("menu", "création menu en cours");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_licence) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", getString(R.string.conditions_title).toLowerCase());
            startActivity(intent);
        }

        if (id == R.id.action_help) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", getString(R.string.help_title).toLowerCase());
            startActivity(intent);
        }
        if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "http://feelshare.soglomania.io");
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
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }


}
