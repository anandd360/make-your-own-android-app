package in.data360.anand.alllanguagetranslator;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MainActivity extends FragmentActivity implements LTranslator.OnSavedClickedListener{



    private boolean mTwoPane;
    public void onSaved(int position) {
        // The user selected clicked the save
        // Do something here to display the resultant

        LTranslator Frag = (LTranslator)
                getSupportFragmentManager().findFragmentById(R.id.fragment_saved);

        if (findViewById(R.id.fragment_saved) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;

            FragmentManager fragmentManager = getFragmentManager();
            // Or: FragmentManager fragmentManager = getSupportFragmentManager()
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            SavedDisplay fragment = new SavedDisplay();
            fragmentTransaction.add(R.id.fragment_saved, fragment);
            fragmentTransaction.commit();
        }
        else
        {
            mTwoPane = false;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        if (findViewById(R.id.fragment_saved) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
//
                FragmentManager fragmentManager = getFragmentManager();
                // Or: FragmentManager fragmentManager = getSupportFragmentManager()
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SavedDisplay fragment = new SavedDisplay();
                fragmentTransaction.add(R.id.fragment_saved, fragment);
                fragmentTransaction.commit();


            }
        } else {
            mTwoPane = false;
            //getSupportActionBar().setElevation(0f);
        }






    }





 }
