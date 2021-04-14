package com.example.tipcalculatorproject_parinp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnKeyListener;


import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText totalEditText;
    private RadioButton percent_15;
    private RadioButton percent_18;
    private RadioButton percent_20;
    private RadioButton tipOther;
    private RadioButton radioButtonSelected;
    private RadioGroup radioBtnGroup;
    private EditText otherTip;
    private EditText peopleEditText;
    public Button calculateButton;
    private TextView results;
    private double billAmount, numberOfPeople, tipPercent;
    private boolean isCustomTip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalEditText = findViewById(R.id.bill);
        peopleEditText = findViewById(R.id.people);
        calculateButton = findViewById(R.id.calculate);
        results = findViewById(R.id.output);

        radioBtnGroup = findViewById(R.id.radioGroupPercentage);
        percent_15 = findViewById(R.id.radioButton15);
        percent_18 = findViewById(R.id.radioButton18);
        percent_20 = findViewById(R.id.radioButton20);
        tipOther = findViewById(R.id.radioButtonCustom);

        otherTip = findViewById(R.id.otherTip);
        //hiding custom tip text field by default.
        otherTip.setVisibility(View.INVISIBLE);

        totalEditText.setOnKeyListener(mKeyListener);
        peopleEditText.setOnKeyListener(mKeyListener);
        otherTip.setOnKeyListener(mKeyListener);

        radioBtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int selectedRadioPercentage = radioBtnGroup.getCheckedRadioButtonId();
                // get radio button by id
                radioButtonSelected = findViewById(selectedRadioPercentage);

                switch (radioBtnGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton15:
                        //uncheck other radio buttons and hide custom text field
                        tipPercent = Float.parseFloat(radioButtonSelected.getText().toString().substring(0,2));
                        percent_18.setChecked(false);
                        percent_20.setChecked(false);
                        tipOther.setChecked(false);
                        otherTip.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radioButton18:
                        //uncheck other radio buttons and hide custom text field
                        tipPercent = Float.parseFloat(radioButtonSelected.getText().toString().substring(0,2));
                        percent_15.setChecked(false);
                        percent_20.setChecked(false);
                        tipOther.setChecked(false);
                        otherTip.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radioButton20:
                        //uncheck other radio buttons and hide custom text field
                        tipPercent = Float.parseFloat(radioButtonSelected.getText().toString().substring(0,2));
                        percent_15.setChecked(false);
                        percent_18.setChecked(false);
                        tipOther.setChecked(false);
                        otherTip.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.radioButtonCustom:
                        //uncheck other radio buttons and show custom text field
                        isCustomTip = true;
                        otherTip.setVisibility(View.VISIBLE);
                        percent_15.setChecked(false);
                        percent_18.setChecked(false);
                        percent_20.setChecked(false);
                        break;
                }
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTotal();
            }
        });
    }
    public void calculateTotal() {
        if(totalEditText.getText().toString().equals("")) {
            showErrorAlert("Enter the total amount of Bill:", R.id.bill);
            return;
        } else if(radioButtonSelected == null){
            showErrorAlert("Please select your tip amount below:", R.id.people);
            return;
        } else if(otherTip.getText().toString().equals("")) {
            showErrorAlert("Enter tip amount", R.id.radioButtonCustom);
            return;
        }
        else if(peopleEditText.getText().toString().equals("")){
            showErrorAlert("Number of People must be 1 or more:", R.id.people);
            return;
        }

        double billVal = Double.parseDouble(totalEditText.getText().toString());
        double numOfPeopleVal = Double.parseDouble(peopleEditText.getText().toString().trim());

        if(isCustomTip){
            double otherTipVal = Double.parseDouble(otherTip.getText().toString());
            if(otherTipVal != tipPercent) {
                tipPercent = otherTipVal;
            }
        }

        //check if user did not hit enter, if so, update values.
        if(billVal != billAmount) {
            billAmount = billVal;
        }
        if(numOfPeopleVal != numberOfPeople) {
            numberOfPeople = numOfPeopleVal;
        }

        int selectedRadioPercentage = radioBtnGroup.getCheckedRadioButtonId();
        // get radio button by id
        radioButtonSelected = findViewById(selectedRadioPercentage);

        if(billAmount < 1.0) {
            showErrorAlert("Enter bill amount", R.id.bill);
            return;
        } else if(radioButtonSelected == null){
            showErrorAlert("Please select a tip option", R.id.people);
            return;
        } else if(tipPercent < 1.0) {
            showErrorAlert("Enter tip amount", R.id.radioButtonCustom);
            return;
        }
        else if(numberOfPeople < 1.0){
            showErrorAlert("Number of People cannot be less than one", R.id.people);
            return;
        }

        if(tipPercent < 1) {
            showErrorAlert("Tip can not be less then 1", R.id.radioButtonCustom);
            return;
        } else if(tipPercent > 100) {
            showErrorAlert("Tip can not be more then 100", R.id.radioButtonCustom);
            return;
        }

        DecimalFormat df2 = new DecimalFormat("0.00");

        double tipDollar = billAmount * (tipPercent/100.00);
        double totalBill = billAmount + tipDollar;

        String output = "Total Bill: " + df2.format(totalBill) + "\n" +
                "Tips: " + df2.format(tipDollar) + "\n" +
                "Each Pay: " + df2.format(totalBill/numberOfPeople);

        results.setText(output);
    }

    private void showErrorAlert(String errorMessage, final int fieldId) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setNeutralButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                findViewById(fieldId).requestFocus();
                            }
                        }).show();
    }

    private View.OnKeyListener mKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            switch (v.getId()) {
                case R.id.bill:

                    //Check that the user pressed ENTER
                    if (keyCode == KeyEvent.KEYCODE_ENTER){

                        //Ensure that the user actually input data
                        if (!totalEditText.getText().equals("")) {

                            //save the user input into a temporary variable
                            float temp = Float.parseFloat(totalEditText.getText().toString());

                            //Check that the value is greater than 1
                            if (temp < 1.0){
                                showErrorAlert("Check Amount can't be less than $1.00", R.id.bill);
                            }
                            else{
                                billAmount = temp;
                            }
                        }
                    }
                    break;

                case R.id.people:
                    //Check that the user pressed ENTER
                    if (keyCode == KeyEvent.KEYCODE_ENTER){

                        //Ensure that the user actually input data
                        if (!peopleEditText.getText().equals("")){

                            //save the user input into a temporary variable
                            int temp = Integer.parseInt(peopleEditText.getText().toString());

                            //Check that the value is greater than or equal to 1
                            if (temp < 1){
                                showErrorAlert("Number of People can't be less than 1", R.id.people);
                            }
                            else{
                                numberOfPeople = temp;
                            }
                        }
                    }
                    break;

                case R.id.otherTip:
                    //Check that the user pressed ENTER
                    if (keyCode == KeyEvent.KEYCODE_ENTER){

                        //Ensure that the user actually input data
                        if (!otherTip.getText().equals("")){

                            //save the user input into a temporary variable
                            float temp = Float.parseFloat(otherTip.getText().toString());

                            //Check that the value is greater than or equal to 1
                            if (temp < 1.0){
                                showErrorAlert("Tip Percentage can't be less than 1", R.id.otherTip);
                            }
                            //Check that the value is less than or equal to 100
                            else if (temp >= 100.0){
                                showErrorAlert("Tip Percentage can't be more than 100", R.id.otherTip);
                            }
                            else{
                                tipPercent = temp;
                            }
                        }
                    }
                    break;

            }

            return false;
        }

    };

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        String billAmountValue = Double.toString(billAmount);
        outState.putString("billAmount", billAmountValue);

        String numberOfPeopleValue = Double.toString(numberOfPeople);
        outState.putString("numberOfPeople", numberOfPeopleValue);

        String tipPercentValue = Double.toString(tipPercent);
        outState.putString("tipPercent", tipPercentValue);

        String resultValue = results.getText().toString();
        outState.putString("result", resultValue);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedState){
        super.onRestoreInstanceState(savedState);

        String billAmountValue = savedState.getString( "billAmount");
        billAmount = Double.parseDouble(billAmountValue);

        String numberOfPeopleValue = savedState.getString( "numberOfPeople");
        numberOfPeople = Double.parseDouble(numberOfPeopleValue);

        String tipPercentValue = savedState.getString( "tipPercent");
        tipPercent = Double.parseDouble(tipPercentValue);

        String resultValue = savedState.getString( "result");
        results.setText(resultValue);
    }
}