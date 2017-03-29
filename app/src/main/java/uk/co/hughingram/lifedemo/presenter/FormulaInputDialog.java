package uk.co.hughingram.lifedemo.presenter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.hughingram.lifedemo.R;

public class FormulaInputDialog extends DialogFragment implements TextView.OnEditorActionListener {

    public interface FormulaInputDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;

    public FormulaInputDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container);
        mEditText = (EditText) view.findViewById(R.id.txt_function_input);
        getDialog().setTitle("Hello");

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            FormulaInputDialogListener activity = (FormulaInputDialogListener) getActivity();
            activity.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}
