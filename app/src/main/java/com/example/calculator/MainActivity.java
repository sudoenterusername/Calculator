package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;
    private Double operand1 = null;
    private String pendingOperation = "=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        result = findViewById(R.id.result);
        newNumber = findViewById(R.id.newNumber);
        displayOperation = findViewById(R.id.displayOperation);
        displayOperation.setText("");

        View.OnClickListener delAcNegListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String bText = b.getText().toString();
                String currentNum = newNumber.getText().toString();
                if(bText.equals("DEL") && !currentNum.isEmpty()){
                    currentNum = currentNum.substring(0, currentNum.length() - 1);
                    newNumber.setText(currentNum);
                }
                if(bText.equals("AC")){
                    newNumber.setText("");
                    result.setText("");
                    operand1 = null;
                    displayOperation.setText("");
                    pendingOperation = "=";
                }
                if(bText.equals("NEG")){
                    if(currentNum.contains("-")) {
                        currentNum = currentNum.replace("-", "");
                        newNumber.setText(currentNum);
                    }else {
                        String negNewNumber = "-" + currentNum;
                        newNumber.setText(negNewNumber);
                    }
                }
            }
        };

        Button buttonDEL = findViewById(R.id.buttonDEL);
        Button buttonAC = findViewById(R.id.buttonAC);
        Button buttonNEG = findViewById(R.id.buttonNEG);

        buttonDEL.setOnClickListener(delAcNegListener);
        buttonAC.setOnClickListener(delAcNegListener);
        buttonNEG.setOnClickListener(delAcNegListener);

        int[] buttonIDs = new int[] {R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
                R.id.buttonDot};

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                newNumber.append(b.getText());
            }
        };

        for (int buttonID : buttonIDs) {
            Button b = findViewById(buttonID);
            b.setOnClickListener(listener);
        }

        Button buttonMinus = findViewById(R.id.buttonMinus);
        Button buttonDivide = findViewById(R.id.buttonDivide);
        Button buttonEquals = findViewById(R.id.buttonEquals);
        Button buttonMultiply = findViewById(R.id.buttonMultiply);
        Button buttonPlus = findViewById(R.id.buttonPlus);

        View.OnClickListener opListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String op = b.getText().toString();
                String value = newNumber.getText().toString();
                try{
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue,op);
                }catch (NumberFormatException e) {
                    newNumber.setText("");
                }
                pendingOperation = op;
                displayOperation.setText(pendingOperation);
            }
        };
        buttonMinus.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);
        buttonEquals.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);
    }

    private void performOperation(Double value, String op){
        if(null == operand1){
            operand1 = value;
        } else{
            if(pendingOperation.equals("=")) {
                pendingOperation = op;
            }
        switch (pendingOperation) {
            case "=":
                operand1 = value;
                break;
            case "/":
                if (value == 0) {
                    operand1 = 0.0;
                } else {
                    operand1 /= value;
                }
                break;
            case "*":
                operand1 *= value;
                break;
            case "-":
                operand1 -= value;
                break;
            case "+":
                operand1 += value;
                break;
         }
        }
        result.setText(operand1.toString());
        newNumber.setText("");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("op", pendingOperation);
        if(null != operand1)
            outState.putDouble("operand1", operand1);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString("op");
        displayOperation.setText(pendingOperation);
        operand1 = savedInstanceState.getDouble("operand1");
    }
}