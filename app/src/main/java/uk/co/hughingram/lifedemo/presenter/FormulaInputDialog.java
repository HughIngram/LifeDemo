package uk.co.hughingram.lifedemo.presenter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.hughingram.lifedemo.R;

public class FormulaInputDialog extends DialogFragment implements TextView.OnEditorActionListener {

    interface FormulaInputDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText formulaEditText;

    public FormulaInputDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_test, container);
        formulaEditText = (EditText) view.findViewById(R.id.txt_function_input);
        final Button okButton = (Button) view.findViewById(R.id.btn_okay);
        okButton.setOnClickListener(okButtonListener);
        setUpInputButtons(view);
        return view;
    }

    private void setUpInputButtons(final View view) {
        final Button buttonA = (Button) view.findViewById(R.id.btn_button_a);
        buttonA.setTag("A");
        buttonA.setOnClickListener(buttonListener);

        final Button buttonB = (Button) view.findViewById(R.id.btn_button_b);
        buttonB.setTag("B");
        buttonB.setOnClickListener(buttonListener);

        final Button buttonAnd = (Button) view.findViewById(R.id.btn_button_and);
        buttonAnd.setTag("^");
        buttonAnd.setOnClickListener(buttonListener);
    }

    // TODO call this on press OK button
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            FormulaInputDialogListener activity = (FormulaInputDialogListener) getActivity();
            activity.onFinishEditDialog(formulaEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }

    final Button.OnClickListener okButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FormulaInputDialogListener activity = (FormulaInputDialogListener) getActivity();
            activity.onFinishEditDialog(formulaEditText.getText().toString());
            dismiss();
        }
    };

    // the listener for input buttons
    final Button.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final String output = (String) view.getTag();
            formulaEditText.append(output);
        }
    };

}
