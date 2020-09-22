package Inventory;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Inventory {

	private Hashtable<String, Integer> inventory = new Hashtable<String, Integer>();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private String lastFoodAdded;

	public Inventory() throws IOException {

		JSONLoader();
		System.out.println(inventory.size());

	}
	public String getLastFoodAdded() {
		return lastFoodAdded;
	}
	public void addFood(Food a) throws IOException {

		if(inventory.get(a.getFoodName()) != null) {

			int nbFood = inventory.get(a.getFoodName());
			nbFood += 1;

			inventory.put(a.getFoodName(), nbFood);

		}else {
			inventory.put(a.getFoodName(), 1);
		}
		lastFoodAdded = a.getFoodName();
		FileWriter fw  = new FileWriter("inventory.json");
		gson.toJson(inventory, fw);
		fw.close();
	}

	public void addFood(Food a, int nbFood) throws IOException {

		if(inventory.get(a.getFoodName()) != null) {

			int nbFoodPresent = inventory.get(a.getFoodName());
			nbFoodPresent += nbFood;

			inventory.put(a.getFoodName(), nbFoodPresent);

		}else {
			inventory.put(a.getFoodName(), nbFood);
		}

		FileWriter fw  = new FileWriter("inventory.json");
		gson.toJson(inventory, fw);
		fw.close();
	}

	public int getNbFood(Food a) {

		if(inventory.get(a.getFoodName()) == null) {
			return 0;
		}

		return inventory.get(a.getFoodName());
	}

	public Hashtable<String, Integer> getInventory() {
		return inventory;
	}

	public Object[] getFoodList(){

		Set<String> keys = inventory.keySet();

		return keys.toArray();
	}

	public void retriveFood(Food a) throws IOException {
		if(inventory.get(a.getFoodName()) != null) {

			int nbFood = inventory.get(a.getFoodName());
			nbFood -= 1;

			if(nbFood == 0){
				inventory.remove(a.getFoodName());
			}else {
				inventory.put(a.getFoodName(), nbFood);
			}

			FileWriter fw  = new FileWriter("inventory.json");
			gson.toJson(inventory, fw);
			fw.close();

		}else {
			System.out.println("Can't retrive food not in the inventory");
		}
	}

	public void retriveFood(Food a, int nb) throws IOException {
		if(inventory.get(a.getFoodName()) != null && inventory.get(a.getFoodName()) >= nb) {

			int nbFood = inventory.get(a.getFoodName());
			nbFood -= nb;

			if(nbFood == 0){
				inventory.remove(a.getFoodName());
			}else {
				inventory.put(a.getFoodName(), nbFood);
			}

			FileWriter fw  = new FileWriter("inventory.json");
			gson.toJson(inventory, fw);
			fw.close();

		}else {
			System.out.println("Can't retrive food not in the inventory");
		}
	}

	private void JSONLoader() throws IOException {

		Gson gson = new Gson();

		BufferedReader br = new BufferedReader(new FileReader("inventory.json"));

		String jsonString = "";

		String line = br.readLine();

		while( line != null){

			jsonString += line;
			line = br.readLine();

		}

		if(jsonString.length() > 2){

			JsonObject convertedObject = new Gson().fromJson(jsonString, JsonObject.class);
			Object[] arr = convertedObject.keySet().toArray();

			for(int i = 0; i < arr.length; i++){

				inventory.put((String) arr[i], convertedObject.get((String) arr[i]).getAsInt());

			}
		}
	}

}