

import IA.*;
import Inventory.Inventory;
import Recipe.*;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
//        Application app = new Application();

        //Inventory inv = new Inventory();
        //Food a = new Food("Apple");

        //APILinker base = new APILinker();
        //base.addFoodFromResponse("apple", inv);

        //Food b = new Food("Apple");

        //base.addFoodFromResponse("apple", inv);
        //base.addFoodFromResponse("apple", inv);


        /*
        System.out.println("Hello World!");
        String path = "C:\\repos\\hackatown20\\testImages\\testCam.png";
        File f = new File(path);
        Base64Converter base64Converter = new Base64Converter();
        String encodeString = base64Converter.encodeFileToBase64Binary(path);
        System.out.println(encodeString);
        PrintWriter writer = new PrintWriter("./testTxt/64baseImage.txt");
        writer.println(encodeString);

        //Test request
        Inventory inventory = new Inventory();
        APILinker api = new APILinker();
        api.addFoodInventory(path,inventory);

        WebcamDisplay display = new WebcamDisplay();
        JFrame window = new JFrame("Smart Fridge");
        window.add(display.panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        */

        Inventory inv = new Inventory();
        APILinker.addFoodFromResponse("beefcheesemacaroniapplesauce", inv);
        RecipesList recipesList = new RecipesList(inv);
        recipesList.getRecipe();
        for(Recipe recipe : recipesList.recipes){
            recipe.afficherRecipe();
        }
        //
    }
}
