package io.eidukas.fivethirtyeight.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import io.eidukas.fivethirtyeight.Models.ProbabilityItem;
import io.eidukas.fivethirtyeight.Models.SortType;
import io.eidukas.fivethirtyeight.R;

import static java.lang.Math.round;

public class ProbabilityItemAdapter extends ArrayAdapter<ProbabilityItem> {
    public ProbabilityItemAdapter(Context context, ArrayList<ProbabilityItem> items){
        super(context, 0, items);
    }

    /**
     * Recycles a used view in listview to be put at bottom.
     * @param position Position in data array.
     * @param convertView View to be changed.
     * @param parent Listview.
     * @return View to put at bottom of listview.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProbabilityItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView stateText = (TextView) convertView.findViewById(R.id.state_list_item);
        TextView probabilityText = (TextView) convertView.findViewById(R.id.probability_list_item);
        TextView candidateText = (TextView) convertView.findViewById(R.id.candidate_list_item);

        stateText.setText(getItem(position).getState());
        probabilityText.setText(getContext().getString(R.string.percent_string, round(getItem(position).getProbability()*100) / 100.0));
        candidateText.setText(getItem(position).getCandidate());

        int colorid;
        switch(item.getCandidate()){
            case "Trump":
                colorid = android.R.color.holo_red_dark;
                break;
            case "Clinton":
                colorid = android.R.color.holo_blue_dark;
                break;
            case "McMullin":
                colorid = android.R.color.holo_purple;
                break;
            case "Johnson":
                colorid = R.color.yellow;
                break;
            default:
                colorid = android.R.color.white;
                break;
        }
        convertView.setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), colorid)));

        return convertView;
    }

    /**
     * Given sorting enum, sort list, then refresh the listview.
     * @param type Sorting method
     */
    public void sortData(SortType type){
        Comparator<ProbabilityItem> sorter;
        switch(type){
            case HIGHEST_PROBABILITY:
                sorter = new Comparator<ProbabilityItem>() {
                    @Override
                    public int compare(ProbabilityItem probabilityItem, ProbabilityItem t1) {
                        int compare = ((Double)probabilityItem.getProbability()).compareTo(t1.getProbability());
                        if (compare == 0){
                            compare = probabilityItem.getState().compareTo(t1.getState());
                        }
                        if (compare == 0){
                            compare = probabilityItem.getCandidate().compareTo(t1.getCandidate());
                        }
                        return compare;
                    }
                };
                break;
            case CANDIDATE:
                sorter = new Comparator<ProbabilityItem>() {
                    @Override
                    public int compare(ProbabilityItem probabilityItem, ProbabilityItem t1) {
                        int compare = probabilityItem.getCandidate().compareTo(t1.getCandidate());
                        if (compare == 0){
                            compare = probabilityItem.getState().compareTo(t1.getState());
                        }
                        if (compare == 0){
                            compare = ((Double)probabilityItem.getProbability()).compareTo(t1.getProbability());
                        }
                        return compare;
                    }
                };
                break;
            case STATE_ALPHABETIC:
                sorter = new Comparator<ProbabilityItem>() {
                    @Override
                    public int compare(ProbabilityItem probabilityItem, ProbabilityItem t1) {
                        int compare = probabilityItem.getState().compareTo(t1.getState());
                        if (compare == 0){
                            compare = ((Double)probabilityItem.getProbability()).compareTo(t1.getProbability());
                        }
                        if (compare == 0){
                            compare = probabilityItem.getCandidate().compareTo(t1.getCandidate());
                        }
                        return compare;
                    }
                };
                break;
            default:
                sorter = new Comparator<ProbabilityItem>() {
                    @Override
                    public int compare(ProbabilityItem probabilityItem, ProbabilityItem t1) {
                        return probabilityItem.getState().compareTo(t1.getState());
                    }
                };
                break;
        }
        sort(sorter);
    }
}
