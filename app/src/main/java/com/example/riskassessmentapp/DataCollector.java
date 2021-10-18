package com.example.riskassessmentapp;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataCollector {



    public CountyData getCovinData(String county, String state){

        CountyData[] counties = httpGet("https://corona.lmao.ninja/v2/jhucsse/counties/" + county, CountyData[].class);
        if (counties == null){
            return null;
        }
        for(CountyData countydata : counties) {

            if(countydata.getProvince().equalsIgnoreCase(state)) {
                return countydata;


            }

        }

        return counties[0];
    }

    public CurrentWrapperUnderData getCurrentData (Double latitude, Double longitude) {

        return httpGet("http://api.airvisual.com/v2/nearest_city?lat=" +  latitude + "&lon=" + longitude + "&key=be9d97cf-905c-4284-b617-83c1d0156c82", AirData.class).getData().getCurrent();

    }


    public<T> T httpGet(String url, Class<T> tClass ){
        T t = null;
        try {
        HttpURLConnection connection =(HttpURLConnection)  new URL(url).openConnection();
        connection.setRequestMethod("GET");
        String json = getJson(connection.getInputStream());
        System.out.println(json);
        Gson gson = new Gson();
        t = gson.fromJson(json, tClass);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return t;

    }
    private String getJson(InputStream input)throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }




}
