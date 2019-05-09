/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package people;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Anonymous
 */
public class People {

    Connection conn;

    public People() {
        try {
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:3306/people";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "root");
        } catch (Exception ex) {
            Logger.getLogger(People.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        People people = new People();
        people.getPeopleWithBlueEyecolor();
    }

    public String getPeopleWithIDone() {
        try {
            URL url = new URL("https://swapi.co/api/people/1/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            if (connection.getResponseCode() != 200) {
                int code = connection.getResponseCode();
                String message = connection.getResponseMessage();

                System.out.println(code + " message " + message);
                return "could not connect with code " + connection.getResponseCode();
            }
            String result = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((result = br.readLine()) != null) {
                System.out.println(result);

                JSONObject obj = new JSONObject(result);
                String name = (String) obj.get("name");
                String eyecolor = (String) obj.get("eye_color");
                String birthYear = (String) obj.get("birth_year");

                System.out.println(name + eyecolor + birthYear);
                // the mysql insert statement
                String query = " insert into person(name, eyecolor, YOB) values (?, ?, ?)";
                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, name);
                preparedStmt.setString(2, eyecolor);
                preparedStmt.setString(3, birthYear);
                // execute the preparedstatement
                preparedStmt.execute();

            }

            conn.close();
            connection.disconnect();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String getPeopleWithBlueEyecolor() {
        try {
            //connecting to the rest URL 
            URL url = new URL("https://swapi.co/api/people/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            if (connection.getResponseCode() != 200) {
                System.out.println(connection.getResponseCode() + " message " + connection.getResponseMessage());
                return "could not connect with code " + connection.getResponseCode();

            }
            String result = null;
            //receiving the Rest result
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((result = br.readLine()) != null) {
                JSONObject obj = new JSONObject(result);
                JSONArray arr = obj.getJSONArray("results");
                for (int i = 0; i < arr.length(); i++) {
                    String name = arr.getJSONObject(i).getString("name");
                    String height = arr.getJSONObject(i).getString("height");
                    String gender = arr.getJSONObject(i).getString("gender");
                    String eye_color = arr.getJSONObject(i).getString("eye_color");

                    if (eye_color.equals("blue")) {

                        String query = " insert into person(name, height, gender,eyecolor) values (?, ?, ?, ?)";
                        //Inserting the result to database
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(1, name);
                        preparedStmt.setString(2, height);
                        preparedStmt.setString(3, gender);
                        preparedStmt.setString(4, eye_color);
                        preparedStmt.execute();
                        System.out.println("Added " + name);
                    } else {

                    }
                }
            }
            connection.disconnect();
            return null;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

}
