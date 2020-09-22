package IA;

import Files.Constants;
import Inventory.*;
import com.google.gson.Gson;
import IA.Base64Converter;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APILinker {

    public static Food getAlimentFromImage(BufferedImage image) {

        return null;
    }

    public static void addFoodInventory(String imageLocalPath, Inventory inventory) throws IOException {
        Base64Converter converter = new Base64Converter();
        String encodedString = converter.encodeFileToBase64Binary(imageLocalPath);
        //print 64 based image in txt
        PrintWriter writer = new PrintWriter(imageLocalPath, "UTF-8");
        writer.println(encodedString);
        String response = requestWebsite("POST", Constants.urlGoogle, "", Constants.getJsonFormat(encodedString, "" + Constants.numberOfResults));

        System.out.println(response);
        addFoodFromResponse(response, inventory);


    }

    public static String requestWebsite(String method, String websiteURL, String urlParameters, String jsonFormat) {

        StringBuilder output = new StringBuilder();

        try {
            URL url = new URL(websiteURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            if (jsonFormat.equals("")) {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(urlParameters);
                outputStream.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";

                while((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }

                reader.close();
            } else {

                System.out.println("TEST");

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonFormat.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        output.append(responseLine.trim());
                    }

                }
            }
            return output.toString();

        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static void addFoodFromResponse(String response, Inventory inventory) {

        for(int i = 0; i < Constants.foodList.length; i++) {
            if (response.toLowerCase().contains(Constants.foodList[i])) {



                try {
                    System.out.println("Food in response : " + Constants.foodList[i]);
                    Food f = new Food(Constants.foodList[i]);
                    System.out.println(f.getFoodName());
                    inventory.addFood(f);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}

