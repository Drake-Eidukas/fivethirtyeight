package io.eidukas.fivethirtyeight.Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import io.eidukas.fivethirtyeight.R;


public class SettingsDialog extends DialogFragment {
    private RadioGroup modelSelect;
    private RadioGroup sortSelect;
    private NoticeDialogListener mListener;


    /**
     * Interface to receive callbacks for positive and negative click events.
     */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, int modelsId, int sortId);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    /**
     * @see android.app.Fragment#onAttach(Context)
     * @param context Activity that listens to this fragment.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    /**
     * Instantiates important views, as well as sets functionality for positive and negative buttons on the
     * dialog popup.
     * @param savedInstanceState Saved state from activity.
     * @return Returns a built dialogfragment to display.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_popup, null);
        builder.setView(view)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogPositiveClick(SettingsDialog.this, modelSelect.getCheckedRadioButtonId(), sortSelect.getCheckedRadioButtonId());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogNegativeClick(SettingsDialog.this);
                        SettingsDialog.this.getDialog().cancel();
                    }
                });

        modelSelect = (RadioGroup) view.findViewById(R.id.model_select_id);
        sortSelect = (RadioGroup) view.findViewById(R.id.sort_select_id);

        Bundle arguments = getArguments();
        //Check the buttons that were last checked.
        ((RadioButton)view.findViewById(arguments.getInt(getActivity().getResources().getString(R.string.model_tag), R.id.plus_button_id))).toggle();
        ((RadioButton)view.findViewById(arguments.getInt(getActivity().getResources().getString(R.string.sort_tag), R.id.alphabetic_button_id))).toggle();

        return builder.create();
    }

}
