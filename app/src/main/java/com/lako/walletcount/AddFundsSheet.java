package com.lako.walletcount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

public class AddFundsSheet extends BottomSheetDialogFragment {
    private TextInputEditText fundsToAdd;
    private TextView amount;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.BottomSheet);
        View view = inflater.inflate(R.layout.add_funds_bottomsheet, container, false);
        Button addFunds = view.findViewById(R.id.add_funds_button);
        amount = requireActivity().findViewById(R.id.home_amountText);
        fundsToAdd = view.findViewById(R.id.add_funds_textEdit);
        fundsToAdd.requestFocus();
        requireDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        addFunds.setOnClickListener(v -> {
            if(Objects.requireNonNull(fundsToAdd.getText()).toString().length() == 0){ fundsToAdd.setText("0"); }
            try {
                double num1 = Objects.requireNonNull(NumberFormat.getInstance().parse(fundsToAdd.getText().toString().replaceAll("[^\\d.,-]", ""))).doubleValue();
                double num2 = Objects.requireNonNull(NumberFormat.getInstance().parse(amount.getText().toString().replaceAll("[^\\d.,-]", ""))).doubleValue();
                double sum = num1 + num2;
                amount.setText(NumberFormat.getCurrencyInstance().format(sum));
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT, amount.getText().toString());
                editor.apply();
                dismiss();
            }catch(NumberFormatException | ParseException exception){
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Error")
                        .setMessage("Invalid number was entered.")
                        .setNeutralButton("OK", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return view;
    }

}
