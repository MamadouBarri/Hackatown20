package UI;

import IA.APILinker;
import Inventory.*;
import Recipe.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.util.ArrayList;
import Recipe.RecipesList;

import Webcam.*;
import com.google.gson.Gson;

public class Application extends JFrame {

    public JFrame frame = this;
    private JPanel contentPane;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel panelInventory;
    private JPanel panelRecipe;
    private JPanel panelInput;


    private JLabel titleInventory;
    private JLabel titleRecipe;
    private JLabel titleInput;

    private JScrollPane scrollPaneInput;
    private JScrollPane scrollPaneRecipe;
    private JScrollPane spIngredients;
    private JScrollPane spIngredientsManquants;

    private JButton screenshotButton = new JButton("CAPTURE ECRAN");
    private JLabel labelFoodName = new JLabel( "", SwingConstants.CENTER);

    private WebcamDisplay webcamDisplay = new WebcamDisplay();
    private ImagePanel showImage = new ImagePanel();
    //private ImagePanel icon = new ImagePanel();
    public static Inventory inventory;
    public static RecipesList recipeList;
    private JList list = new JList();
    private DefaultListModel model = new DefaultListModel();

    DefaultListCellRenderer r2;

    public static JList listeRecipe = new JList();
    private DefaultListModel modelRecipe = new DefaultListModel();
    private JPanel recipePanel = new JPanel();
    private JLabel recipeImage = new JLabel();
    private JLabel lblIngrediant = new JLabel("Ingrédiant(s)", JLabel.CENTER);
    private JLabel lblIngrediantManquant = new JLabel("Ingrédiant(s) manquant(s)", JLabel.CENTER);

    private JList listeIngrediant = new JList();
    private DefaultListModel modelListeIngrediant = new DefaultListModel();
    private JList listeIngrediantManquant = new JList();
    private DefaultListModel modelListeIngrediantManquant = new DefaultListModel();

    private ArrayList<JButton> buttons = new ArrayList<>();

    private JPanel recipeFramePanel = new JPanel();
    public static JTextArea recipeFrameLabel;
    public static JScrollPane recipeFrameSp;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Application frame = new Application("App");
                    frame.requestFocus();
                    frame.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public Application (String title) throws IOException {

        inventory = new Inventory();
        recipeList = new RecipesList(inventory);

        updateModel();

        try {
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
//            UIManager.LookAndFeelInfo laf[] = UIManager.getInstalledLookAndFeels();
//            for (int i = 0, n = laf.length; i < n; i++) {
//                System.out.print("LAF Name: " + laf[i].getName() + "\t");
//                System.out.println("  LAF Class name: " + laf[i].getClassName());
//            }
        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        initFrame(screenSize, title);
        constructUI();
    }

    private void updateModel() {
        String[] v = new String[inventory.getFoodList().length];
        model.removeAllElements();

        for(int i = 0; i < inventory.getFoodList().length; i++) {
            Object food = inventory.getFoodList()[i];
            String value = food.toString();
            String number =  " : " + inventory.getNbFood(new Food(food.toString()));
            v[i] = value;
            model.addElement(value + "    " + number);
        }
    }

    private void initFrame(Dimension d, String title){

        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setTitle(title);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'q')
                    System.exit(0);
            }
        });

        contentPane = new JPanel();
        contentPane.setBounds(0, 0, d.width, d.height);
        contentPane.setLayout(null);
        frame.setContentPane(contentPane);
    }

    private void constructUI() throws IOException {

        try {
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
//            UIManager.LookAndFeelInfo laf[] = UIManager.getInstalledLookAndFeels();
//            for (int i = 0, n = laf.length; i < n; i++) {
//                System.out.print("LAF Name: " + laf[i].getName() + "\t");
//                System.out.println("  LAF Class name: " + laf[i].getClassName());
//            }
        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        panelInventory = new JPanel();
        panelInventory.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelInventory.setLayout(null);
        panelInventory.setPreferredSize(new Dimension(screenSize.width/3, screenSize.height));
        panelInventory.setBounds(0, 0, screenSize.width/3, screenSize.height);
        panelInventory.setBackground(contentPane.getBackground());
        contentPane.add(panelInventory);

        //recipeFrameLabel.setBounds(20 ,20, recipeFrame.getWidth() - 20, recipeFrame.getHeight() - 20);

        // Inventory panel
        titleInventory = new JLabel("INVENTAIRE", SwingConstants.CENTER);
        titleInventory.setFont(new Font(titleInventory.getName(), Font.PLAIN, 32));
        titleInventory.setBounds(0, 25, screenSize.width/3, 50);
        panelInventory.add(titleInventory);

        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setBounds(panelInventory.getWidth()/2 - (panelInventory.getWidth() - 10)/2, 100, panelInventory.getWidth() - 10, panelInventory.getWidth() - 160);
        list.setFixedCellWidth((panelInventory.getWidth() - 10));
        DefaultListCellRenderer r = (DefaultListCellRenderer)(list.getCellRenderer());
        r.setHorizontalAlignment(SwingConstants.CENTER);
        list.setFont(new Font(list.getName(), Font.BOLD, 32));
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);


        //panelInventory.add(scrollPaneInput);

        scrollPaneInput = new JScrollPane(list);
        scrollPaneInput.setBounds(panelInventory.getWidth()/2 - (panelInventory.getWidth() - 10)/2, 100, panelInventory.getWidth() - 10, panelInventory.getWidth() - 160);
        scrollPaneInput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelInventory.add(scrollPaneInput);

        showImage.setBounds(120, panelInventory.getWidth(), 400, 400);
        showImage.setBackground(Color.WHITE);
        showImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelInventory.add(showImage);

        // ====================================

        // Input panel
        panelInput = new JPanel();
        panelInput.setLayout(null);
        panelInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelInput.setPreferredSize(new Dimension(screenSize.width/3, screenSize.height));
        panelInput.setBounds(screenSize.width/3, 0, screenSize.width/3, screenSize.height);
        panelInput.setBackground(contentPane.getBackground());

        titleInput = new JLabel("ENTREES", SwingConstants.CENTER);
        titleInput.setFont(new Font(titleInput.getName(), Font.PLAIN, 32));
        titleInput.setBounds(0, 25, screenSize.width/3, 50);
        panelInput.add(titleInput);

        labelFoodName.setBounds(0, panelInput.getWidth() + 140, panelInput.getWidth(), 100);
        labelFoodName.setBackground(Color.red);
        labelFoodName.setFont(new Font(labelFoodName.getName(), Font.PLAIN, 48));
        labelFoodName.setForeground(Color.WHITE);
        panelInput.add(labelFoodName);

        screenshotButton.setBounds(50, panelInput.getWidth() - 30, panelInput.getWidth() - 100, 50);
        screenshotButton.setBackground(Color.WHITE);
        buttons.add(screenshotButton);
        screenshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                showImage.setImage(webcamDisplay.getImage());
                showImage.repaint();
                APILinker.addFoodInventory(webcamDisplay.imagePath, inventory);
                modelRecipe.removeAllElements();

                String lastFoodAdded = inventory.getLastFoodAdded();
                labelFoodName.setForeground(new Color(102, 255, 153));
                labelFoodName.setText(lastFoodAdded.toUpperCase());
                try {
                    recipeList.getRecipe();

                    for(Recipe r : recipeList.recipes) {
                            modelRecipe.addElement(r.title_);
                    }

                    listeRecipe.setCellRenderer(new CellRenderer());
                    r2 = (DefaultListCellRenderer)(listeRecipe.getCellRenderer());
                    r2.setHorizontalAlignment(SwingConstants.CENTER);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                updateModel();


                frame.repaint();

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panelInput.add(screenshotButton);

        webcamDisplay.panel.setBounds(5, 100, panelInput.getWidth() - 10, panelInput.getWidth() - 160);
        webcamDisplay.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        webcamDisplay.panel.setBackground(panelInput.getBackground());

        panelInput.add(webcamDisplay.panel);


        contentPane.add(panelInput);

        for(int i = 0; i < buttons.size(); i++) {
            JButton b = buttons.get(i);
            b.addMouseListener(new MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(new Color(120, 120, 120));
                    repaint();
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.WHITE);
                    repaint();
                }
            });
        }

//        icon.setBounds(panelInventory.getWidth()/2 - 100/2, panelInventory.getWidth() + 300, 100, 100);
//        icon.setBackground(Color.WHITE);
//        icon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        panelInput.add(icon);

        // ====================================

        // Recipe.Recipe.Recipe.Recipe panel

        panelRecipe = new JPanel();
        panelRecipe.setLayout(null);
        panelRecipe.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelRecipe.setPreferredSize(new Dimension(screenSize.width/3, screenSize.height));
        panelRecipe.setBounds((int) (screenSize.width /3.0 * 2), 0, screenSize.width/3, screenSize.height);
        panelRecipe.setBackground(contentPane.getBackground());
        contentPane.add(panelRecipe);

        titleRecipe = new JLabel("RECETTES", SwingConstants.CENTER);
        titleRecipe.setFont(new Font(titleRecipe.getName(), Font.PLAIN, 32));
        titleRecipe.setBounds(0, 25, screenSize.width/3, 50);
        panelRecipe.add(titleRecipe);

        listeRecipe = new JList(modelRecipe);
        listeRecipe.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listeRecipe.setBounds(5, 100, panelRecipe.getWidth() - 10, panelRecipe.getWidth() - 160);
        listeRecipe.setFixedCellWidth((panelInventory.getWidth()));
        listeRecipe.setCellRenderer(new CellRenderer());

        r2 = (DefaultListCellRenderer)(listeRecipe.getCellRenderer());
        r2.setHorizontalAlignment(SwingConstants.CENTER);

        listeRecipe.setFont(new Font(listeRecipe.getName(), Font.BOLD, 32));
        listeRecipe.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listeRecipe.setVisibleRowCount(-1);
        listeRecipe.addListSelectionListener(new SharedListSelectionHandler());
        listeRecipe.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JList l = (JList)e.getSource();
                int i = l.locationToIndex(e.getPoint());
                Rectangle r = l.getCellBounds(i,i);
                try {
                    if (!r.contains(e.getPoint())) {
                        //l.getSelectionModel().setLeadSelectionIndex(l.getModel().getSize());
                        //l.getSelectionModel().setLeadSelectionIndex(-1);
                        l.clearSelection();
                    }
                } catch(NullPointerException e2) {
                    e2.printStackTrace();
                }

                frame.requestFocus();
            }
        });

        scrollPaneRecipe = new JScrollPane(listeRecipe);
        scrollPaneRecipe.setBounds(5, 100, panelRecipe.getWidth() - 10, panelRecipe.getWidth() - 160);
        scrollPaneRecipe.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelRecipe.add(scrollPaneRecipe);

        recipeFramePanel = new JPanel();
        recipeFramePanel.setLayout(null);
        recipeFramePanel.setBounds(30, panelRecipe.getWidth() + 200, panelRecipe.getWidth() - 60, 200);
        panelRecipe.add(recipeFramePanel);

        recipeFrameLabel = new JTextArea("Recette : \n");
        recipeFrameLabel.setFont(lblIngrediant.getFont());
        recipeFrameLabel.setFont(new Font(lblIngrediant.getFont().getName(), Font.PLAIN, 20));
        recipeFrameLabel.setForeground(Color.WHITE);
        recipeFrameLabel.setWrapStyleWord(true);
        recipeFrameLabel.setLineWrap(true);
        recipeFrameLabel.setEditable(false);

        recipeFrameSp = new JScrollPane(recipeFrameLabel);
        recipeFrameSp.setBounds(10 ,10, recipeFramePanel.getWidth() - 20, recipeFramePanel.getHeight() - 10);
        recipeFrameSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        recipeFrameSp.getVerticalScrollBar().setValue(recipeFrameSp.getHorizontalScrollBar().getMinimum());
        recipeFrameSp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        recipeFramePanel.add(recipeFrameSp);

        recipeFramePanel.add(recipeFrameSp);


//        recipeFramePanel = new JPanel();
//        recipeFramePanel.setBounds(30, panelRecipe.getWidth() + 200, panelRecipe.getWidth() - 60, 200);
//        //recipeFramePanel.setLayout(null);
//        //recipeFramePanel.setBackground(UIManager.getLookAndFeel().getDefaults().getColor("com.bulenkov.darcula.ui.DarculaButtonUI"));
//        panelRecipe.add(recipeFramePanel);
//
//
//        recipeFrameLabel.setLineWrap(true);
//        recipeFrameLabel.setPreferredSize(new Dimension(recipeFramePanel.getSize().width - 50, recipeFramePanel.getHeight() - 50));
//        //recipeFrameLabel.setFont(lblIngrediant.getFont());
//        recipeFrameLabel.setFont(new Font(lblIngrediant.getFont().getName(), Font.PLAIN, 20));
//        recipeFrameLabel.setBackground(recipeFramePanel.getBackground());
//        recipeFrameLabel.setWrapStyleWord(true);
//        recipeFrameLabel.setForeground(Color.WHITE);
//        recipeFrameLabel.setEditable(false);
//
//        recipeFrameSp = new JScrollPane(recipeFrameLabel);
//        //recipeFrameSp.setBackground(Color.red);
//        //recipeFrameSp.setEnabled(true);
//        recipeFrameSp.setBounds(10 ,10, recipeFramePanel.getWidth() - 20, recipeFramePanel.getHeight() - 10);
//        //recipeFrameSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//        //recipeFrameSp.setBackground(recipeFramePanel.getBackground());
//        recipeFramePanel.add(recipeFrameSp);
//        recipePanel.add(recipeFramePanel);

        //panelRecipe.add(listeRecipe);

//        ArrayList<String> ingrediant = new ArrayList<String>();
//        ingrediant.add("apple");
//        ArrayList<String> ingrediantManquant = new ArrayList<String>();
//        ArrayList<String> ingrediantManquant2 = new ArrayList<String>();
//        ingrediantManquant2.add("banana");
//        Recipe recipe = new Recipe("Milkshake", "google.ca" , ingrediant, ingrediantManquant, 0);
//        Recipe rip = new Recipe("GG NO RE", "google.ca" , ingrediant, ingrediantManquant2, 0);
//
//        recipeList.recipes.add(recipe);
//        recipeList.recipes.add(rip);
//
//
//        modelRecipe.addElement(recipe.title_);
//        modelRecipe.addElement(rip.title_);

        recipePanel.setBounds(20, 100 + listeRecipe.getHeight() + 10, panelRecipe.getWidth() - 40, panelRecipe.getHeight() - titleRecipe.getHeight() - listeRecipe.getHeight() - 75);
        recipePanel.setBackground(panelRecipe.getBackground());
        recipePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        recipePanel.setLayout(null);

        lblIngrediant.setForeground(Color.white);
        lblIngrediant.setBounds(0,0, recipePanel.getWidth()/2 - 20, 50);

        lblIngrediantManquant.setForeground(Color.white);
        lblIngrediantManquant.setBounds(recipePanel.getWidth()/2,0, recipePanel.getWidth()/2, 50);

        lblIngrediant.setFont(new Font(list.getName(), Font.BOLD, 20));
        lblIngrediantManquant.setFont(new Font(list.getName(), Font.BOLD, 20));
        recipePanel.add(lblIngrediant);
        recipePanel.add(lblIngrediantManquant);

        listeIngrediant = new JList(modelListeIngrediant);
        listeIngrediant.setFont(new Font(list.getName(), Font.PLAIN, 20));
        listeIngrediantManquant = new JList(modelListeIngrediantManquant);
        listeIngrediantManquant.setFont(new Font(list.getName(), Font.PLAIN, 20));

        DefaultListCellRenderer r3 = (DefaultListCellRenderer)(listeIngrediant.getCellRenderer());
        r3.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultListCellRenderer r4 = (DefaultListCellRenderer)(listeIngrediantManquant.getCellRenderer());
        r4.setHorizontalAlignment(SwingConstants.CENTER);


        listeIngrediant.setBounds(lblIngrediant.getX() + 10, lblIngrediant.getY() + lblIngrediant.getHeight(), lblIngrediant.getWidth() - 10, recipePanel.getHeight() - lblIngrediant.getHeight());
        listeIngrediantManquant.setBounds(lblIngrediantManquant.getX() + 10, lblIngrediantManquant.getY() + lblIngrediantManquant.getHeight(), lblIngrediantManquant.getWidth() - 10, recipePanel.getHeight() - lblIngrediantManquant.getHeight());

        listeIngrediant.setBackground(recipePanel.getBackground());

        spIngredients = new JScrollPane(listeIngrediant);

        spIngredients.setBounds(lblIngrediant.getX() + 10, lblIngrediant.getY() + lblIngrediant.getHeight(), lblIngrediant.getWidth() - 10, recipePanel.getHeight() - lblIngrediant.getHeight() - 250);
        spIngredients.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        recipePanel.add(spIngredients);

        spIngredientsManquants = new JScrollPane(listeIngrediantManquant);
        spIngredientsManquants.setBounds(lblIngrediant.getX() + 10 + recipePanel.getWidth()/2, lblIngrediant.getY() + lblIngrediant.getHeight(), lblIngrediant.getWidth() - 10, recipePanel.getHeight() - lblIngrediant.getHeight() - 250);
        spIngredientsManquants.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        recipePanel.add(spIngredientsManquants);

        //recipePanel.add(listeIngrediant);
        //recipePanel.add(listeIngrediantManquant);
        panelRecipe.add(recipePanel);

        //ajouterIngredientEtIngredientManquant(rip);

    }

    class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                boolean hasFound = false;
                Recipe recipe = null;
                for (Recipe r : recipeList.recipes) {
                    if (r.title_.equals(listeRecipe.getSelectedValue())) {
                        recipe = r;
                        hasFound = true;
                    }
                }

                if (recipe != null) {
                    ajouterIngredientEtIngredientManquant(recipe);


                    //Read an image and save it
                    /*
                    BufferedImage image;
                    try {
                        System.out.println(recipe.titleImgUrl_);
                        URL imageUrl = new URL(recipe.titleImgUrl_);
                        image = ImageIO.read(imageUrl);
                        //if(image != null){
                           // File file = new File("./recipeImage.jpg");
                           // ImageIO.write(image, "jpg", file);
                       // }
                        recipe.image = image;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    //end read image from net
                    */
                    //icon.setImage(recipe.image);

                    // A mettre dans l'event du screenshot

                }
            }
        }
    }

    private static class CellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

            boolean hasFound = false;
            Recipe recipe = null;
            for (Recipe r : recipeList.recipes) {
                if (r.title_.equals(listeRecipe.getSelectedValue())) {
                    recipe = r;
                    hasFound = true;
                }
            }
            recipeFrameLabel.setText("Recette: " + "\n");
            if(recipe != null){
                int stepCounter = 1;
                for(String step : recipe.steps_){
                    recipeFrameLabel.append(stepCounter + ") " + step);
                    recipeFrameLabel.append("\n");
                    stepCounter++;
                }
            }

            if (!isSelected) {
                if (Application.indexHasEnoughIngredient(index))
                    c.setForeground(new Color(102, 255, 153));
            }
            recipeFrameSp.getVerticalScrollBar().setValue(recipeFrameSp.getHorizontalScrollBar().getMinimum());

            return c;
        }
    }

    private void ajouterIngredientEtIngredientManquant(Recipe r) {

        if (r != null) {

            modelListeIngrediantManquant.removeAllElements();
            modelListeIngrediant.removeAllElements();

            for (int n = 0; n < r.ingredients_.size(); n++) {
                modelListeIngrediant.addElement(r.ingredients_.get(n));
            }

            for (int n = 0; n < r.ingredientsManquant_.size(); n++) {
                modelListeIngrediantManquant.addElement(r.ingredientsManquant_.get(n));
            }
        }

    }

    public static boolean indexHasEnoughIngredient(int index) {

        if (index < recipeList.recipes.size()) {
            if (recipeList.recipes.get(index).ingredientsManquant_.size() == 0)
                return true;

        }
        return false;


    }


}