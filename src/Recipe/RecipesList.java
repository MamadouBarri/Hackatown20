package Recipe;

import Files.Constants;
import Inventory.Inventory;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecipesList {
    public ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    public Inventory inventory_;

    private String getIngredientFromInventory()
    {
        String param = "&ingredients=";
        Object[] array = inventory_.getFoodList();

        for (int i = 0; i < array.length; i++)
        {
            if (i > 0)
                param += "," + array[i].toString();
            else
                param += array[i].toString();
        }

        param = param.replaceAll(" ", "%20");
        return param;
    }


    private String makeURL()
    {
        return Constants.urlRecipe + getIngredientFromInventory();
    }

    public void getRecipe(Inventory inventory)
    {
        inventory_ = inventory;
        try {
            getRecipe();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRecipe() throws IOException {

        String urlString = makeURL();

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
        recipes = new ArrayList<Recipe>();

        JsonArray convertedArray = new Gson().fromJson(response, JsonArray.class);

        for (JsonElement object: convertedArray)
        {
            String title = object.getAsJsonObject().get("title").toString();
            title = title.replaceAll("\"", "");

            String titleImgUrl = object.getAsJsonObject().get("image").toString();
            titleImgUrl = titleImgUrl.replaceAll("\"", "");

            int id = object.getAsJsonObject().get("id").getAsInt();

            ArrayList<String> ingredients = new ArrayList<String>();
            ArrayList<String> ingredientsMissing = new ArrayList<String>();

            JsonArray array = object.getAsJsonObject().get("missedIngredients").getAsJsonArray();
            for (JsonElement missingIngredient : array)
            {
                String missing = missingIngredient.getAsJsonObject().get("name").toString();
                missing = missing.replaceAll("\"", "");
                ingredientsMissing.add(missing);
            }

            array = object.getAsJsonObject().get("usedIngredients").getAsJsonArray();
            for (JsonElement ingredient : array)
            {
                String notMissing = ingredient.getAsJsonObject().get("name").toString();
                notMissing = notMissing.replaceAll("\"", "");
                ingredients.add(notMissing);
            }
            recipes.add(new Recipe(title, titleImgUrl, ingredients, ingredientsMissing, id));
        }
    }

    public RecipesList()
    {

    }
    public RecipesList(Inventory inventory)
    {
        inventory_ = inventory;
    }
}
