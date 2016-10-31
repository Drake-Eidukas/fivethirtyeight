package io.eidukas.fivethirtyeight;


import android.Manifest;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SettingsDialog.NoticeDialogListener {
    private Bundle settings = new Bundle();
    private String PREFS_NAME;
    private String MODEL_TAG;
    private String SORT_TAG;

    private ArrayList<ProbabilityItem> data;
    private ProbabilityItemAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    private final int REQUEST_INTERNET_PERMISSION = 1;

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

        //Initialize data + listview.
        data = new ArrayList<>();
        initializeListView();

    }

    /**
     * Instantiate the SwipeRefreshLayout parts, and fetch + load data.
     */
    private void initializeListView(){
        adapter = new ProbabilityItemAdapter(this, data);
        ListView listView = (ListView) findViewById(R.id.state_list_view);
        listView.setAdapter(adapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        swipeContainer.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        reloadData();
    }

    /**
     * Prompts all dataelements to have a different model to draw info from, then updates the view.
     */
    private void updateMode(){
        Models mode = getCurrentMode();
        for(ProbabilityItem item : data){
            item.setMode(mode);
        }
        adapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //Refresh button + settings button.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                reloadData();
                return true;
            case R.id.action_settings:
                launchSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    /**
     * Added tests to make sure that returned data is not null / if network errors occur, just make a toast explaining that.
     * Given data, update the model, and then reshow the data.
     * @param toUpdate Model to change values to.
     */
    public void updateData(ProbabilityObject toUpdate){
        if(toUpdate == null || toUpdate.getStates() == null){
            Toast.makeText(this, "Error fetching data over network!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (data.size() == 0) {
                data = toUpdate.toItemList(getCurrentMode());
                adapter.clear();
                adapter.addAll(data);

            } else {
                for (ProbabilityItem p : toUpdate.toItemList(getCurrentMode())) {
                    data.get(data.indexOf(p)).update(p);
                }
            }
        } catch(Exception e){
            Toast.makeText(this, "Error fetching data over network!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Check permissions, then fetch data in async task, then update listview.
     */
    public void reloadData(){
        PermissionManager manager = new PermissionManager(this);
        if(!manager.checkPermission(Manifest.permission.INTERNET, this.getResources().getString(R.string.internet_title),
                this.getResources().getString(R.string.internet_explanation), REQUEST_INTERNET_PERMISSION)){
            return;
        }
//        if(!manager.checkPermission(Manifest.permission.RECEIVE_SMS, this.getResources().getString(R.string.internet_title),
//                this.getResources().getString(R.string.internet_explanation), REQUEST_INTERNET_PERMISSION)){
//            return;
//        }

        new AsyncTask<String, Void, String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeContainer.setRefreshing(true);
            }

            @Override
            protected String doInBackground(String... strings) {
                ApiRequest request = new ApiRequest(strings[0]);
                String response = "";
                try{
                    response= request.getResponse(getApplicationContext().getResources().getInteger(R.integer.timeout));
                } catch (IOException ioE){
                    ioE.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                updateData(ProbabilityObject.fromJson(s));
                adapter.sortData(getCurrentSortMode());
                swipeContainer.setRefreshing(false);
            }
        }.execute(getApplicationContext().getResources().getString(R.string.api_url));
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
        adapter.sortData(getCurrentSortMode());
    }

    /**
     * User hit cancel on settings menu.
     * @param dialog Menu
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }
}
