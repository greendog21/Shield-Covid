package com.example.riskassessmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class Form extends AppCompatActivity {
    private Button submit_button;

    public static EditText editText_inputName, editText_inputAge;
    public static CheckBox checkbox_kidney, checkbox_COPD, checkbox_weak_immune, checkbox_obesity, checkbox_heart_conditions, checkbox_sickle_cell, checkbox_diabetes;

    public static String name;
    public static int age;
    public static Boolean disease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //to retrieve data
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();

        submit_button = (Button) findViewById(R.id.submit_button);
        editText_inputName = (EditText) findViewById(R.id.editText_inputName);
        editText_inputAge = (EditText) findViewById(R.id.editText_inputAge1);

        checkbox_kidney = (CheckBox) findViewById(R.id.checkbox_kidney);
        try{
            checkbox_kidney.setChecked(prefs.getBoolean("Chronic kidney disease", false));

        } catch (java.lang.RuntimeException e) {
            System.out.println("prefs doesnt contain check_kidney");
        }
        checkbox_kidney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("Chronic kidney disease", isChecked);
                editor.commit();
            }
        });

        checkbox_COPD = (CheckBox) findViewById(R.id.checkbox_COPD);
        try {
            checkbox_COPD.setChecked(prefs.getBoolean("Chronic obstructive pulmonary disease (COPD)", false));
        } catch (java.lang.RuntimeException e) {
            System.out.println("prefs doesnt contain checkbox_COPD");
        }
        checkbox_COPD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("Chronic obstructive pulmonary disease (COPD)", isChecked);
                editor.commit();
            }
        });

        checkbox_weak_immune = (CheckBox) findViewById(R.id.checkbox_weak_immune);
        try {
            checkbox_weak_immune.setChecked(prefs.getBoolean("Immunocompromised state (weakened immune system) from solid organ transplant", false));
        } catch (java.lang.RuntimeException e) {
            System.out.println("prefs doesnt contain checkbox_weak_immune");
        }
        checkbox_weak_immune.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("Immunocompromised state (weakened immune system) from solid organ transplant", isChecked);
                editor.commit();
            }
        });

        checkbox_obesity = (CheckBox) findViewById(R.id.checkbox_obesity);
        try {
            checkbox_obesity.setChecked(prefs.getBoolean("Obesity (BMI of 30 or higher)", false));
        } catch (java.lang.RuntimeException e) {
            System.out.println("prefs doesnt contain checkbox_obesity");
        }
        checkbox_obesity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("Obesity (BMI of 30 or higher)", isChecked);
                editor.commit();
            }
        });

        checkbox_heart_conditions = (CheckBox) findViewById(R.id.checkbox_heart_conditions);
        try {
            checkbox_heart_conditions.setChecked(prefs.getBoolean("Serious heart conditions, such as heart failure, coronary artery disease, or cardiomyopathies", false));
        } catch (java.lang.RuntimeException e) {
            System.out.println("prefs doesnt contain checkbox_heart_conditions");
        }
        checkbox_heart_conditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("Serious heart conditions, such as heart failure, coronary artery disease, or cardiomyopathies", isChecked);
                editor.commit();
            }
        });


        checkbox_sickle_cell = (CheckBox) findViewById(R.id.checkbox_sickle_cell);
        try {
            checkbox_sickle_cell.setChecked(prefs.getBoolean("Sickle cell disease", false));
        } catch (java.lang.RuntimeException e) {
            System.out.println("prefs doesnt contain checkbox_sickle_cell");
        }
        checkbox_sickle_cell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("Sickle cell disease", isChecked);
                editor.commit();
            }
        });

        checkbox_diabetes = (CheckBox) findViewById(R.id.checkbox_diabetes);
        try {
            checkbox_diabetes.setChecked(prefs.getBoolean("Type 2 diabetes mellitus", false));
        } catch (java.lang.RuntimeException e) {
            System.out.println("prefs doesnt contain checkbox_diabetes");
        }
        checkbox_diabetes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                editor.putBoolean("Type 2 diabetes mellitus", isChecked);
                editor.commit();
            }
        });

        String st1 = prefs.getString("name","");
        editText_inputName.setText(st1);

        int in1 = prefs.getInt("age",0);
        editText_inputAge.setText(Integer.toString(in1));

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = editText_inputName.getText().toString();
                String ageChecker = editText_inputAge.getText().toString();
                if (name.equals("") ){
                    name = "";
                    editor.putString("name",name);
                    editor.apply();
                }
                else {
                    editor.putString("name",name);
                    editor.apply();
                    System.out.println("name=" + name);
                }

                if (ageChecker.equals("")){
                    age = 0;
                    editor.putInt("age", age);
                    editor.apply();
                }
                else {
                    age = Integer.parseInt(editText_inputAge.getText().toString());
                    editor.putInt("age",age);
                    editor.apply();
                    System.out.println("age=" + age);
            }






                Intent intent = new Intent(Form.this, Display_Layout.class);
                startActivity(intent);




                //openDisplay_Layout();



            }
        });
    }

    public void openDisplay_Layout() {
        Intent intent = new Intent(Form.this, Display_Layout.class);
        startActivity(intent);
    }
}




