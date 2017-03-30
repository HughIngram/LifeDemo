package uk.co.hughingram.lifedemo.presenter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import uk.co.hughingram.lifedemo.R;

public class FormulaInputDialog extends DialogFragment {

    interface FormulaInputDialogListener {
        void onFinishEditDialog(String inputText, boolean[] values);
    }

    private EditText formulaEditText;
    private CheckBox checkBoxA;
    private CheckBox checkBoxB;
    private CheckBox checkBoxC;
    private CheckBox checkBoxD;

    public FormulaInputDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_formula, container);
        formulaEditText = (EditText) view.findViewById(R.id.txt_function_input);
        checkBoxA = (CheckBox) view.findViewById(R.id.checkbox_a);
        checkBoxB = (CheckBox) view.findViewById(R.id.checkbox_b);
        checkBoxC = (CheckBox) view.findViewById(R.id.checkbox_c);
        checkBoxD = (CheckBox) view.findViewById(R.id.checkbox_d);
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

        final Button buttonC = (Button) view.findViewById(R.id.btn_button_c);
        buttonC.setTag("C");
        buttonC.setOnClickListener(buttonListener);

        final Button buttonD = (Button) view.findViewById(R.id.btn_button_d);
        buttonD.setTag("D");
        buttonD.setOnClickListener(buttonListener);

        final Button buttonAnd = (Button) view.findViewById(R.id.btn_button_and);
        buttonAnd.setTag("^");
        buttonAnd.setOnClickListener(buttonListener);

        final Button buttonOr = (Button) view.findViewById(R.id.btn_button_or);
        buttonOr.setTag("v");
        buttonOr.setOnClickListener(buttonListener);

        final Button buttonNot = (Button) view.findViewById(R.id.btn_button_not);
        buttonNot.setTag("~");
        buttonNot.setOnClickListener(buttonListener);

        final Button buttonOpenBracket = (Button) view.findViewById(R.id.btn_button_open_bracket);
        buttonOpenBracket.setTag("(");
        buttonOpenBracket.setOnClickListener(buttonListener);

        final Button buttonCloseBracket = (Button) view.findViewById(R.id.btn_button_close_bracket);
        buttonCloseBracket.setTag(")");
        buttonCloseBracket.setOnClickListener(buttonListener);
    }

    final Button.OnClickListener okButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final boolean[] values = new boolean[4];
            values[0] = checkBoxA.isChecked();
            values[1] = checkBoxB.isChecked();
            values[2] = checkBoxC.isChecked();
            values[3] = checkBoxD.isChecked();
            final FormulaInputDialogListener activity = (FormulaInputDialogListener) getActivity();
            activity.onFinishEditDialog(formulaEditText.getText().toString(), values);
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
