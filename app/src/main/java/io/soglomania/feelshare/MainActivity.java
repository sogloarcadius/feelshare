package io.soglomania.feelshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.List;
import java.util.Vector;

import presentation.AccountFragment;
import presentation.ChartsFragment;
import presentation.FeelingsFragment;
import presentation.SectionsPagerAdapter;
import presentation.SupportFragment;

public class MainActivity extends AppCompatActivity {


    private String userID;
    private String userName;
    private String email;
    CallbackManager callbackManager;

    private SharedPreferences sharedPref;
    AccessTokenTracker accessTokenTracker;

    // le gestionnaire de fragments
    private SectionsPagerAdapter mSectionsPagerAdapter;

    // le conteneur de fragments
    private ViewPager mViewPager;

    // Création de la liste de Fragments que fera défiler le PagerAdapter
    List fragments = new Vector();
    // constructeur
    public MainActivity() {
        Log.d("MainActivity", "constructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate");
        // parent
        super.onCreate(savedInstanceState);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {

//                    refreshLoginView();
                    //User logged out
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("userID").remove("userName").remove("email");

//                    editor.putString("userID", null);
//                    editor.putString("userName", null);
//                    editor.putString("email", null);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), getString(R.string.success_logout_message), Toast.LENGTH_LONG).show();


                }
            }
        };

        accessTokenTracker.startTracking();

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
                                    editor.commit();

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
                Toast.makeText(getApplicationContext(), getString(R.string.facebook_error), Toast.LENGTH_LONG).show();

            }
        });


        // Ajout des Fragments dans la liste
        fragments.add(AccountFragment.newInstance("Account"));
        fragments.add(FeelingsFragment.newInstance("Feelings"));
        fragments.add(ChartsFragment.newInstance("Charts"));
        fragments.add(Fragment.instantiate(this, SupportFragment.class.getName()));

        // le gestionnaire de fragments
        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), fragments);

//    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // le conteneur de fragments est associé au gestionnaire de fragments
        // ç-à-d que le fragment n° i du conteneur de fragments est le fragment n° i délivré par le gestionnaire de fragments
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // la barre d'onglets est également associée au conteneur de fragments
        // ç-à-d que l'onglet n° i affiche le fragment n° i du conteneur
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

  /*  // bouton flottant
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });*/

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
        Log.d("menu", "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("menu", "action_settings selected");
            return true;
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
