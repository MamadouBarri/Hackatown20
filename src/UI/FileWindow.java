package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Fenetre qui permet de chercher un fichier a lire
 * @author Gaya Mehenni
 *
 */
public class FileWindow extends JFrame {

	private JPanel contentPane;
	private JFileChooser fileChooser;
	private String path;

	/**
	 * Constructeur
	 */
	public FileWindow() {

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(0, 0, 600, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		setContentPane(contentPane);

		fileChooser = new JFileChooser();
		fileChooser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					if (!e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)){
						path = fileChooser.getSelectedFile().getAbsolutePath();
					} else {
						path = "";
					}
				} catch(NullPointerException n) {
					path = "";
				}
				setVisible(false);
			}
		});
		fileChooser.setBounds(0, 0, 400, 400);
		contentPane.add(fileChooser);
	}

	/**
	 * Methode qui retourne le chemin d'acces
	 * @return chemin d'acces
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Change le mode de selection du file chooser
	 * @param mode chaine de caractere du mode
	 */
	public void setMode(String mode) {
		if (mode.equals("Folder")) {
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else if (mode.equals("images")) {
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
	}

	/**
	 * Permet de retourner le file chooser dans la fenetre
	 * @return file chooser
	 */
	public JFileChooser getFileChooser() {
		return fileChooser;
	}

}
