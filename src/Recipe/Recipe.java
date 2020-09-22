package Recipe;

import Files.Constants;
import Inventory.Inventory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Recipe {
    public String title_;
    public String titleImgUrl_;
    public ArrayList<String> ingredients_;
    public ArrayList<String> ingredientsManquant_;
    public ArrayList<String> steps_ = new ArrayList<String>();
    public int id_;
    public BufferedImage image = null;

    public void afficherRecipe()
    {
        System.out.println(title_);
        System.out.println(titleImgUrl_);
        System.out.println(ingredients_.toString());
        System.out.println(ingredientsManquant_.toString());
    }

    private void getSteps() throws IOException {
        String urlString = Constants.urlRecipeSteps + "&id=" + id_;

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        String response = content.toString();
        extractJson(response);
    }

    public void extractJson(String response)
    {
        JsonArray convertedArray = new Gson().fromJson(response, JsonArray.class);

        for(JsonElement obj : convertedArray)
        {
            JsonArray steps = obj.getAsJsonObject().get("steps").getAsJsonArray();
            for(JsonElement step : steps)
            {
                String stepString = step.getAsJsonObject().get("step").toString();
                stepString = stepString.replaceAll("\"", "");
                steps_.add(stepString);
            }
        }
    }

    public Recipe(String title, String titleImgUrl, ArrayList<String> ingredients, ArrayList<String> ingredientsManquant,int id)
    {
        title_ = title;
        titleImgUrl_ = titleImgUrl;
        ingredients_ = ingredients;
        ingredientsManquant_ = ingredientsManquant;
        id_ = id;
        try {
            getSteps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
