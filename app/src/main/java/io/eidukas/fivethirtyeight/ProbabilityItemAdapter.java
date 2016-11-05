package io.eidukas.fivethirtyeight;

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

        /**
         * Should fetch strings from elsewhere.
         * Error Constant expressions required when I try to do this.
         */
        switch(getItem(position).getCandidate()){
            case "Trump":
                convertView.setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark)));
                break;
            case "Clinton":
                convertView.setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark)));
                break;
            case "McMullin":
                convertView.setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.holo_purple)));
                break;
            case "Johnson":
                convertView.setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.yellow)));
                break;
            default:
                convertView.setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.white)));
                break;
        }

        return convertView;
    }

    /**
     * Given sorting enum, sort list, then refresh the listview.
     * @param type
     */
    public void sortData(SortType type){
        Comparator<ProbabilityItem> sorter;
        switch(type){
            case HIGHEST_PROBABILITY:
                sorter = new Comparator<ProbabilityItem>() {
                    @Override
                    public int compare(ProbabilityItem probabilityItem, ProbabilityItem t1) {
                        Double p1 = probabilityItem.getProbability();
                        int compare = p1.compareTo(t1.getProbability());
                        return -1 * ((compare == 0)? probabilityItem.getState().compareTo(t1.getState()) : compare);
                    }
                };
                break;
            case CANDIDATE:
                sorter = new Comparator<ProbabilityItem>() {
                    @Override
                    public int compare(ProbabilityItem probabilityItem, ProbabilityItem t1) {
                        int compare = probabilityItem.getCandidate().compareTo(t1.getCandidate());
                        return (compare == 0)? probabilityItem.getState().compareTo(t1.getState()) : compare;
                    }
                };
                break;
            case STATE_ALPHABETIC:
                sorter = new Comparator<ProbabilityItem>() {
                    @Override
                    public int compare(ProbabilityItem probabilityItem, ProbabilityItem t1) {
                        return probabilityItem.getState().compareTo(t1.getState());
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
