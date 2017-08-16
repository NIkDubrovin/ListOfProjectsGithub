package com.nikdubrovin.list_of_projects_github;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.name;
import static android.util.Log.i;


/**
 * Created by NikDubrovin on 16.08.2017.
 */

public class GetGithubData extends AsyncTask<String,Void,ArrayList<JSONObject>> {

    private final String TAG = "GitHubAPI";
    private final String URL_API = "https://api.github.com/users/";
    private URL url;
    private String User;
    private String Data;
    private ArrayList<JSONObject> listJsonArray = new ArrayList<>();
   // https://api.github.com/users/yatingupta10/repos - список репозиториев с данными

    @Override
     protected ArrayList<JSONObject> doInBackground(String... strings) {

        try{

            //region Получаем  Json-строку( виде String) от сервера при помощи Http запроса

            String url_str = URL_API +  User + "/" + Data;

            if(validateUrl(url_str)) { // Проверка - является ли строка url-адресом
                url = new URL(url_str);
                i(TAG,url_str);
            }
            else
                i(TAG,"Failed URL!");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection == null)
                i(TAG,"Failed to make connection!");
            else
                i(TAG,"Successfully to make connection!");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //endregion Получение строки закончили

            JSONArray json = new JSONArray(response.toString()); // Превращаем String in JSONObject

            for(int i= 0;i<json.length();i++)
                listJsonArray.add(json.getJSONObject(i));

            for (int i =0;i<listJsonArray.size();i++) {
                String name = listJsonArray.get(i).getString("name");
               // i(TAG, "Name: " + name + " " + i);
                String lang = listJsonArray.get(i).getString("language");
            //    i(TAG, "Language: " + lang + " " + i);
                String url_repos = listJsonArray.get(i).getString("html_url");
              //  i(TAG, "Url_repos: " + url_repos + " " + i);
                String description = listJsonArray.get(i).getString("description");
              //  i(TAG, "Description: " + description + " " + i);

                listJsonArray.get(i).put("name", name).put("url_repos", url_repos).put("description", description).put("language", lang);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        catch (Throwable cause){
            cause.printStackTrace();
        }

        return listJsonArray;
    }

    public static boolean validateUrl(String adress){
        return android.util.Patterns.WEB_URL.matcher(adress).matches();
    }


    public void setUser(String User) {this.User = User;}
    public void setData(String Data) {this.Data = Data;}
}
