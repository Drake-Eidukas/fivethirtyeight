package io.eidukas.fivethirtyeight.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import io.eidukas.fivethirtyeight.Adapters.PartyItemAdapter;
import io.eidukas.fivethirtyeight.Models.Models;
import io.eidukas.fivethirtyeight.Models.PartyItem;
import io.eidukas.fivethirtyeight.Models.SortType;
import io.eidukas.fivethirtyeight.R;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class DetailView extends android.support.v4.app.Fragment {

    private PartyItemAdapter adapter;
    private ListView listView;
    private ArrayList<PartyItem> data;
    private MainActivity activity;
    private boolean isVertical;

    public DetailView() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        data = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        switch(getResources().getConfiguration().orientation){
            case ORIENTATION_LANDSCAPE:
                view= inflater.inflate(R.layout.fragment_detail_view_horizontal, null);
                isVertical = false;
                break;
            default:
                view= inflater.inflate(R.layout.fragment_detail_view, null);
                isVertical = true;
                break;
        }
        listView = (ListView)view.findViewById(R.id.detail_view_list_id);

        if(activity == null){
            activity = (MainActivity)getActivity();
            initializeListView();
        }
        if(getArguments() != null) {
            data = getArguments().getParcelableArrayList(activity.getResources().getString(R.string.probability_item_bundle_arg));
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity)getActivity();
        if(getArguments() == null){
            data = new ArrayList<>();
        }else {
            data = getArguments().getParcelableArrayList(activity.getResources().getString(R.string.probability_item_bundle_arg));
        }
        initializeListView();
    }

    private void initializeListView(){
        adapter = new PartyItemAdapter(activity, data);
        listView.setAdapter(adapter);
    }

    public void sortAdapterData(SortType sort){
        adapter.sortData(sort);
    }

    public void updateMode(Models mode){
        for(PartyItem item : data){
            item.setMode(mode);
        }
        adapter.notifyDataSetChanged();
    }

    public void setData(ArrayList<PartyItem> data){
        this.data = data;
        adapter.clear();
        adapter.addAll(data);
    }
}
