

package com.example.riskassessmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;



public class Display_Layout extends AppCompatActivity {




    TextView textView_summary;
    Button return_button;
    private Button risk_calc;
    private Button more_info;

    // Weather Stats:
    private TextView display_temperature;
    private TextView display_airQuality;

    private float temperature;

    // Covid Stats
    private TextView display_cases;
    private TextView display_population;
    private TextView display_coefficient;

    Integer cases;
    CountyData countyCovinData;
    AirData airData;

    // Welcome Sign
    private TextView welcome;

    //Risk Assessment
    private TextView riskOfInfection;
    private TextView riskofDeath;
    private TextView labelRiskOfDeath;

    //Risk Assessment Scale:
    private TextView centroid1;
    private TextView centroid2;
    private TextView centroid3;
    private TextView centroid4;
    private TextView centroid5;
    private TextView centroid6;

    //Air quality API variables
    private Integer air_quality;
    private String air_description;

    //Variables from Form
    private String name;
    private int age;
    private boolean kidney, COPD, immune, obesity, heart, sickle, diabetes;
    private boolean actual_dis;



    public boolean isConnectingToInternet(Context context)
    {

        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))
                if (info.isConnected())
                    have_MobileData=true;
        }

        System.out.println("***  have_WIFI=" + have_WIFI + " have mobile data=" + have_MobileData);
        return have_WIFI||have_MobileData;

    }






    String closestCentroid(Double x) {
                String result;
                double centroid1 = 0.00142404;
                double centroid2 = 0.00533441;
                double centroid3 = 0.01145566;
                double centroid4 = 0.02217733;
                double centroid5 = 0.03764942;
                double centroid6 = 0.0844737;

                double d1 = (double) Math.sqrt((x - centroid1) * (x - centroid1));
                double d2 = (double) Math.sqrt((x - centroid2) * (x - centroid2));
                double d3 = (double) Math.sqrt((x - centroid3) * (x - centroid3));
                double d4 = (double) Math.sqrt((x - centroid4) * (x - centroid4));
                double d5 = (double) Math.sqrt((x - centroid5) * (x - centroid5));
                double d6 = (double) Math.sqrt((x - centroid6) * (x - centroid6));

                double min1 = Math.min(d1, d2);
                double min2 = Math.min(min1, d3);
                double min3 = Math.min(min2, d4);
                double min4 = Math.min(min3, d5);
                double min = Math.min(min4, d6);

                if (min == d1) {
                    result = "centroid1";
                } else if (min == d2) {
                    result = "centroid2";
                } else if (min == d3) {
                    result = "centroid3";
                } else if (min == d4) {
                    result = "centroid4";
                } else if (min == d5) {
                    result = "centroid5";
                } else {
                    result = "centroid6";
        }
        return result;
    }




    Integer RiskGroup(Integer age, Integer air_quality, Boolean serious_disease){
        int risk_factor = 0;
        int age_risk = 0;
        int air_risk = 0;


        if (age >= 0 && age <= 17) {
            age_risk = 1;
        } else if (age >= 18 && age <= 39) {
            age_risk = 2;
        } else if (age >= 40 && age <= 49) {
            age_risk = 3;
        } else if (age >= 50 && age <= 64) {
            age_risk = 4;
        } else if (age >= 64 && age <= 74) {
            age_risk = 5;
        } else if (age >= 75) {
            age_risk = 6;
        }


        if (air_quality >= 0 && air_quality <= 50) {
            air_risk = 1;
        } else if (air_quality >= 51 && air_quality <= 100) {
            air_risk = 2;

        } else if (air_quality >= 101 && air_quality <= 150) {
            air_risk = 3;

        } else if (air_quality >= 151 && air_quality <= 200) {
            air_risk = 4;
            air_description = "Unhealthy";
        } else if (air_quality >= 201 && air_quality <= 300) {
            air_risk = 5;

        } else if (air_quality >= 301) {
            air_risk = 6;

        }

        if (serious_disease == true) {
            risk_factor = (int) ((0.4 * 6) +  (0.4 * age_risk) + (0.2 *air_risk));
            return risk_factor;
        } else {
            risk_factor = (int) ((0.6 * age_risk) + (0.4 * air_risk));
            return  risk_factor;

        }



    }

    String AirDescription (Integer air_quality){
        if (air_quality >= 0 && air_quality <= 50) {
            air_description = "(Good)";
            return air_description;
        } else if (air_quality >= 51 && air_quality <= 100) {
            air_description = "(Moderate)";
            return air_description;

        } else if (air_quality >= 101 && air_quality <= 150) {
            air_description = "(Unhealthy for Sensitive Groups)";
            return air_description;
        } else if (air_quality >= 151 && air_quality <= 200) {
            air_description = "(Unhealthy)";
            return air_description;
        } else if (air_quality >= 201 && air_quality <= 300) {
            air_description = "(Very Unhealthy)";
            return air_description;
        } else if (air_quality >= 301) {
            air_description = "(Hazardous)";
            return air_description;
        }
        return null;
    }

    public void openCitation(){
        Intent intent = new Intent(this, Citation_Layout.class);
        startActivity(intent);
    }

    private class RestTask extends AsyncTask<Void, Void, Void> {
        CountyData countyDataFromRestApi;
        CurrentWrapperUnderData airDataFromApi;
        String county;
        String state;
        Double latitude;
        Double longitude;
        public RestTask(String county, String state){
            this.county = county;
            this.state = state;
        }



        @Override
        protected Void doInBackground(Void... voids) {
            if(isConnectingToInternet(Display_Layout.this)) {
                System.out.print("Background data collection - internet is available");
                DataCollector dataCollector = new DataCollector();
                countyDataFromRestApi = dataCollector.getCovinData(county, state);
                airDataFromApi = dataCollector.getCurrentData(latitude, longitude);
            }

            else {
                System.out.print("Skip to collect latitude and longitude on background: Internet is not available");
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (countyDataFromRestApi == null){
                display_cases.setText("Sorry, but there is no available data for your county, please choose an alternative one.");
                return;

            }
            countyCovinData = countyDataFromRestApi;
            latitude = countyCovinData.getCoordinates().getLatitude();
            longitude = countyCovinData.getCoordinates().getLongitude();
            temperature = airDataFromApi.getWeather().getTp();
            cases = countyCovinData.getStats().getConfirmed();
            air_quality = airDataFromApi.getPollution().getAqius();

            String partOfDisplayAirQuality = AirDescription(air_quality);
            String actual_air_quality = Integer.toString(air_quality);
            display_airQuality.setText("Air Quality: " + actual_air_quality + " " + partOfDisplayAirQuality);

            temperature = (float) (temperature * 9/5) + 32;
            String displayed_temperature = Float.toString(temperature);
            display_temperature.setText("Temperature (F) : " + displayed_temperature);



            String displayed_population = Integer.toString(MainActivity.confirmed_population);
            display_population.setText("Population of County: " + displayed_population);
            Double coefficient = Double.valueOf(cases) / Double.valueOf(MainActivity.confirmed_population);

            assessInfectionRisk(closestCentroid(coefficient));



            coefficient = Math.round(coefficient * 100000d)/ 100000d;
            String displayed_coefficient = String.valueOf(coefficient);
            display_coefficient.setText("Infected versus Population Coefficient: " + displayed_coefficient);

            String displayed_cases = Integer.toString(cases);
            display_cases.setText("Confirmed Cases: " + displayed_cases);
            assessDiseaseRisk();
            assessAllRisk();
        }
    }

    private void assessDiseaseRisk(){
        actual_dis= false;

        if (kidney == true ) {
            actual_dis = true;
        }
        else if (COPD == true){
            actual_dis = true;
        }
        else if (diabetes == true){
            actual_dis = true;
        }
        else if (heart == true){
            actual_dis = true;
        }
        else if (obesity == true){
            actual_dis = true;
        }
        else if (sickle == true){
            actual_dis = true;
        }
        else if (immune == true){
            actual_dis = true;
        }
    }

    private void assessInfectionRisk(String infectionRisk){
        if (infectionRisk.equals("centroid1")){
            riskOfInfection.setVisibility(View.VISIBLE);
            riskOfInfection.setText("Highly Unlikely");
            riskOfInfection.setBackgroundColor(Color.BLUE);
            riskOfInfection.setTextColor(Color.WHITE);
            riskOfInfection.setTextSize(10);
        }
        else if (infectionRisk.equals("centroid2")) {
            riskOfInfection.setVisibility(View.VISIBLE);
            riskOfInfection.setText("Unlikely");
            riskOfInfection.setBackgroundColor(Color.rgb(0, 128, 0));
            riskOfInfection.setTextColor(Color.WHITE);
            riskOfInfection.setTextSize(10);
        }
        else if (infectionRisk.equals("centroid3")) {
            riskOfInfection.setVisibility(View.VISIBLE);
            riskOfInfection.setText("Possible");
            riskOfInfection.setBackgroundColor(Color.YELLOW);
            riskOfInfection.setTextColor(Color.BLACK);
            riskOfInfection.setTextSize(10);
        }

        else if (infectionRisk.equals("centroid4")) {
            riskOfInfection.setVisibility(View.VISIBLE);
            riskOfInfection.setText("Likely");
            riskOfInfection.setBackgroundColor(Color.rgb(255, 165, 0));
            riskOfInfection.setTextColor(Color.WHITE);
            riskOfInfection.setTextSize(10);
        }


        else if (infectionRisk.equals("centroid5")) {
            riskOfInfection.setVisibility(View.VISIBLE);
            riskOfInfection.setText("Very Likely");
            riskOfInfection.setBackgroundColor(Color.RED);
            riskOfInfection.setTextColor(Color.WHITE);
            riskOfInfection.setTextSize(10);
        }


        else {
            riskOfInfection.setVisibility(View.VISIBLE);
            riskOfInfection.setText("Hazardous");
            riskOfInfection.setBackgroundColor(Color.rgb(100,50,204));
            riskOfInfection.setTextColor(Color.WHITE);
            riskOfInfection.setTextSize(10);
        }

    }

    private void assessAllRisk(){
        if (age == 0){

            labelRiskOfDeath.setText("Please input your actual age in the Risk Assessment form.");
            labelRiskOfDeath.setTextSize(10);
        }else {
            Integer RiskNumber = RiskGroup(age , air_quality, actual_dis);

            if (RiskNumber == 1){
                riskofDeath.setVisibility(View.VISIBLE);
                riskofDeath.setText("Highly Unlikely");
                riskofDeath.setBackgroundColor(Color.BLUE);
                riskofDeath.setTextColor(Color.WHITE);
                riskofDeath.setTextSize(10);
            }
            else if (RiskNumber == 2) {
                riskofDeath.setVisibility(View.VISIBLE);
                riskofDeath.setText("Unlikely");
                riskofDeath.setBackgroundColor(Color.GREEN);
                riskofDeath.setTextColor(Color.WHITE);
                riskofDeath.setTextSize(10);
            }
            else if (RiskNumber == 3) {
                riskofDeath.setVisibility(View.VISIBLE);
                riskofDeath.setText("Possible");
                riskofDeath.setBackgroundColor(Color.YELLOW);
                riskofDeath.setTextColor(Color.BLACK);
                riskofDeath.setTextSize(10);
            }

            else if (RiskNumber == 4) {
                riskofDeath.setVisibility(View.VISIBLE);
                riskofDeath.setText("Likely");
                riskofDeath.setBackgroundColor(Color.rgb(255, 165, 0));
                riskofDeath.setTextColor(Color.WHITE);
                riskofDeath.setTextSize(10);
            }


            else if (RiskNumber == 5) {
                riskofDeath.setVisibility(View.VISIBLE);
                riskofDeath.setText("Very Likely");
                riskofDeath.setBackgroundColor(Color.RED);
                riskofDeath.setTextColor(Color.WHITE);
                riskofDeath.setTextSize(10);
            }


            else if (RiskNumber == 6){
                riskofDeath.setVisibility(View.VISIBLE);
                riskofDeath.setText("Hazardous");
                riskofDeath.setBackgroundColor(Color.rgb(100,50,204));
                riskofDeath.setTextColor(Color.WHITE);
                riskofDeath.setTextSize(10);
            }

        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display__layout);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        name = prefs.getString("name", "");
        age = prefs.getInt("age", 0);
        kidney = prefs.getBoolean("Chronic kidney disease", false);
        COPD = prefs.getBoolean("Chronic obstructive pulmonary disease (COPD)", false);
        immune = prefs.getBoolean("Immunocompromised state (weakened immune system) from solid organ transplant", false);
        obesity = prefs.getBoolean("Obesity (BMI of 30 or higher)", false);
        heart = prefs.getBoolean("Serious heart conditions, such as heart failure, coronary artery disease, or cardiomyopathies", false);
        sickle = prefs.getBoolean("Sickle cell disease", false);
        diabetes = prefs.getBoolean("Type 2 diabetes mellitus", false);


        display_population = (TextView) findViewById(R.id.textView_displayPopulation);
        risk_calc = (Button) findViewById(R.id.button_calcRisk);
        return_button = (Button) findViewById(R.id.return_button);
        welcome = (TextView) findViewById(R.id.textView_welcomeTitle);
        display_cases = (TextView) findViewById(R.id.textView_displayCases);
        display_coefficient = (TextView) findViewById(R.id.textView_displayCoefficient);
        display_airQuality = (TextView) findViewById(R.id.textView_displayAirQuality);
        display_temperature = (TextView) findViewById(R.id.textView_displayTemperature);
        riskofDeath = (TextView) findViewById(R.id.textView_riskOfDeathDisplay);
        riskOfInfection = (TextView) findViewById(R.id.textView_infectionRiskDisplay);
        labelRiskOfDeath = (TextView) findViewById(R.id.textView_riskOfDeathTitle);
        more_info = (Button) findViewById(R.id.button_moreInfo);

        centroid1 = (TextView)findViewById(R.id.textView_scale1Display);
        centroid2 = (TextView)findViewById(R.id.textView_scale2Display);
        centroid3 = (TextView)findViewById(R.id.textView_scale3Display);
        centroid4 = (TextView)findViewById(R.id.textView_scale4Display);
        centroid5 = (TextView)findViewById(R.id.textView_scale5Display);
        centroid6 = (TextView)findViewById(R.id.textView_scale6Display);



        return_button.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v)
            {
                Intent intent = new Intent(Display_Layout.this, MainActivity.class);
                startActivity(intent);
            }
        });







        risk_calc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isConnectingToInternet(Display_Layout.this))
                {
                    System.out.print("internet is not available");
                    Toast.makeText(getApplicationContext(),"Internet is not available. Please connect to internet",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "internet is available", Toast.LENGTH_LONG).show();
                    if (name.equals("")) {
                        welcome.setText("Welcome");
                    } else {
                        String displayed_name = name;
                        welcome.setText("Stay Safe, " + displayed_name);
                    }

                    if (MainActivity.confirmed_population == null) {
                        display_population.setText("Please Input a Location in the Main Menu");
                        display_coefficient.setText("Please Input a Location in the Main Menu");
                        display_airQuality.setText("Please Input a Location in the Main Menu");
                        display_temperature.setText("Please Input a Location in the Main Menu");
                    } else {

                        String toBeSplit = MainActivity.actv_county;
                        String[] arrOfStr = toBeSplit.split(",", 3);
                        String keyToAPI = arrOfStr[0];
                        String stateConfirmed = arrOfStr[1].trim();
                        new RestTask(keyToAPI, stateConfirmed).execute();

                    }


                }





            }



        });


        return_button.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v)
            {
                Intent intent = new Intent(Display_Layout.this, MainActivity.class);
                startActivity(intent);
            }
        });
        more_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                openCitation();


            }
        });









    }

}