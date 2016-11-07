package io.eidukas.fivethirtyeight.Views;


import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import io.eidukas.fivethirtyeight.Models.Models;
import io.eidukas.fivethirtyeight.Models.SortType;
import io.eidukas.fivethirtyeight.R;

public class MainActivity extends AppCompatActivity implements SettingsDialog.NoticeDialogListener {
    private Bundle settings = new Bundle();
    private String PREFS_NAME;
    private String MODEL_TAG;
    private String SORT_TAG;
    private ListFragment listFragment;
    private boolean detailCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load preferences / state from last time.
        PREFS_NAME = this.getResources().getString(R.string.preferences_file);
        MODEL_TAG = this.getResources().getString(R.string.model_tag);
        SORT_TAG = this.getResources().getString(R.string.sort_tag);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        settings.putInt(MODEL_TAG, sharedPreferences.getInt(MODEL_TAG, R.id.plus_button_id));
        settings.putInt(SORT_TAG, sharedPreferences.getInt(SORT_TAG, R.id.alphabetic_button_id));

        listFragment = new ListFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_location_id, listFragment).commit();
        detailCurrent = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Refresh button + settings button.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                listFragment.reloadData();
                return true;
            case R.id.action_settings:
                launchSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(MODEL_TAG, settings.getInt(MODEL_TAG));
        editor.putInt(SORT_TAG, settings.getInt(SORT_TAG));

        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if(!detailCurrent){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("Are you sure you want to exit?");
            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            });
            dialogBuilder.setNegativeButton("No", null);
            dialogBuilder.show();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Launch settings fragment. Called from actionbar.
     */
    public void launchSettings(){
        DialogFragment newFragment = new SettingsDialog();
        newFragment.setArguments(settings);
        newFragment.show(getFragmentManager(), null);
    }

    /**
     * User hit apply on settings menu.
     * @param dialog Menu
     * @param modelsId ID of model's radiobutton toggled.
     * @param sortId ID of sort's radiobutton toggled.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int modelsId, int sortId) {
        settings.putInt(MODEL_TAG, modelsId);
        settings.putInt(SORT_TAG, sortId);
        updateMode();
        listFragment.sortAdapterData(getCurrentSortMode());
    }

    /**
     * User hit cancel on settings menu.
     * @param dialog Menu
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    /**
     * Get current model to be used from the settings menu.
     * @return current Model to use.
     */
    public Models getCurrentMode(){
        switch(settings.getInt(MODEL_TAG)){
            case R.id.now_button_id:
                return Models.NOW;
            case R.id.polls_button_id:
                return Models.POLLS;
            default:
                return Models.PLUS;
        }
    }


    /**
     * Prompts all dataelements to have a different model to draw info from, then updates the view.
     */
    private void updateMode(){
        listFragment.updateMode(getCurrentMode());
    }

    /**
     * Get current sort mode to sort the listview.
     * @return SortType to use.
     */
    public SortType getCurrentSortMode(){
        switch(settings.getInt(SORT_TAG)){
            case R.id.alphabetic_button_id:
                return SortType.STATE_ALPHABETIC;
            case R.id.candidate_button_id:
                return SortType.CANDIDATE;
            case R.id.probability_button_id:
                return SortType.HIGHEST_PROBABILITY;
            default:
                return SortType.STATE_ALPHABETIC;
        }
    }
}
