package io.eidukas.fivethirtyeight.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import io.eidukas.fivethirtyeight.Models.PartyItem;
import io.eidukas.fivethirtyeight.Models.ProbabilityItem;
import io.eidukas.fivethirtyeight.Models.SortType;
import io.eidukas.fivethirtyeight.R;

import static java.lang.Math.round;

public class PartyItemAdapter extends ArrayAdapter<PartyItem>{

    public PartyItemAdapter(Context context, ArrayList<PartyItem> items){
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
        PartyItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.detail_list_item, parent, false);
        }
        TextView candidateText = (TextView) convertView.findViewById(R.id.candidate_detail_list_item);
        TextView partyText = (TextView) convertView.findViewById(R.id.party_detail_list_item);
        TextView probabilityText = (TextView) convertView.findViewById(R.id.probability_list_item);

        candidateText.setText(item.getCandidate());
        partyText.setText(item.getParty());
        probabilityText.setText(getContext().getString(R.string.percent_string, round(item.getWinShare()*100) / 100.0));

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
                colorid = R.color.gold;
                break;
            default:
                colorid = R.color.cards;
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
        Comparator<PartyItem> sorter;
        switch(type){
            case CANDIDATE:
                sorter = new Comparator<PartyItem>() {
                    @Override
                    public int compare(PartyItem partyItem, PartyItem t1) {
                        int compare = partyItem.getCandidate().compareTo(t1.getCandidate());
                        if (compare == 0){
                            compare = -1 * ((Double)partyItem.getWinShare()).compareTo(t1.getWinShare());
                        }
                        return compare;
                    }
                };
                break;
            default:
                sorter = new Comparator<PartyItem>() {
                    @Override
                    public int compare(PartyItem partyitem, PartyItem t1) {
                        int compare = -1 * ((Double)partyitem.getWinShare()).compareTo(t1.getWinShare());
                        if (compare == 0){
                            compare = partyitem.getCandidate().compareTo(t1.getCandidate());
                        }
                        return compare;
                    }
                };
                break;
        }
        sort(sorter);
    }
}
