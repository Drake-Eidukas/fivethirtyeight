package io.eidukas.fivethirtyeight.Views;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import io.eidukas.fivethirtyeight.ApiRequest;
import io.eidukas.fivethirtyeight.Models.Models;
import io.eidukas.fivethirtyeight.Models.ProbabilityItem;
import io.eidukas.fivethirtyeight.Models.ProbabilityObject;
import io.eidukas.fivethirtyeight.Models.SortType;
import io.eidukas.fivethirtyeight.PermissionManager;
import io.eidukas.fivethirtyeight.Adapters.ProbabilityItemAdapter;
import io.eidukas.fivethirtyeight.R;

public class ListFragment extends android.support.v4.app.Fragment {

    private ProbabilityItemAdapter adapter;
    private ListView listView;
    private ArrayList<ProbabilityItem> data;
    private MainActivity activity;
    private SwipeRefreshLayout swipeContainer;
    private final int REQUEST_INTERNET_PERMISSION = 1;

    public ListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_list, null);
        listView = (ListView)view.findViewById(R.id.state_list_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity)getActivity();
        initializeListView();
    }

    /**
     * Instantiate the SwipeRefreshLayout parts, and fetch + load data.
     */
    private void initializeListView(){
        data = new ArrayList<>();
        adapter = new ProbabilityItemAdapter(activity, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                activity.updateDetailView(data.get(position).getPartyItems());
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) activity.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        swipeContainer.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));

        reloadData();
    }

    private void setRefreshingStatus(boolean isRefreshing){
        swipeContainer.setRefreshing(isRefreshing);
    }

    public void sortAdapterData(SortType sort){
        adapter.sortData(sort);
    }

    public void updateMode(Models mode){
        for(ProbabilityItem item : data){
            item.setMode(mode);
        }
        adapter.notifyDataSetChanged();
    }


    public void reloadData(){
        PermissionManager manager = new PermissionManager(activity);
        if(!manager.checkPermission(Manifest.permission.INTERNET, activity.getResources().getString(R.string.internet_title),
                activity.getResources().getString(R.string.internet_explanation), REQUEST_INTERNET_PERMISSION)){
            return;
        }
        new AsyncTask<String, Void, String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setRefreshingStatus(true);
            }

            @Override
            protected String doInBackground(String... strings) {
                ApiRequest request = new ApiRequest(strings[0]);
                String response = "";
                try{
                    response= request.getResponse(activity.getApplicationContext().getResources().getInteger(R.integer.timeout));
                } catch (IOException ioE){
                    ioE.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                updateData(ProbabilityObject.fromJson(s));
                adapter.sortData(activity.getCurrentSortMode());
                setRefreshingStatus(false);
            }
        }.execute(activity.getApplicationContext().getResources().getString(R.string.api_url));
    }

    /**
     * Added tests to make sure that returned data is not null / if network errors occur, just make a toast explaining that.
     * Given data, update the model, and then reshow the data.
     * @param toUpdate Model to change values to.
     */
    private void updateData(ProbabilityObject toUpdate){
        if(toUpdate == null || toUpdate.getStates() == null){
            Toast.makeText(activity, "Error fetching data over network!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (data.size() == 0) {
                data = toUpdate.toItemList(activity.getCurrentMode());
                adapter.clear();
                adapter.addAll(data);

            } else {
                for (ProbabilityItem p : toUpdate.toItemList(activity.getCurrentMode())) {
                    data.get(data.indexOf(p)).update(p);
                }
                adapter.notifyDataSetChanged();
            }
        } catch(Exception e){
            Toast.makeText((getActivity()), "Error fetching data over network!", Toast.LENGTH_SHORT).show();
        }
    }
}
