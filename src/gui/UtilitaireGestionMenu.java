package gui;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.UtilitaireFichier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFileChooser;

import modele.Boite;
import modele.Disjoncteur;

/*
 * Classe qui contient les sous-programmes pour gérer les boutons d'option
 * de menu.
 * 
 * S'il y a ajout de bouton, il faut modifier cette classe et y ajouter
 * le comportement désiré.
 * 
 * @Author Pierre Bélisle
 * @version H2023
 */
public class UtilitaireGestionMenu {

	// Extension choisie arbitrairement pour les noms de fichier contenant
	// une boîte.
	public static final String EXTENSION_BOITE = "bte";

	public static final String DESC_EXTENSION = "*."+EXTENSION_BOITE;

	/**
/**
	 * L'utilisateur a quitté, on lui demande si c'est bien ce qu'il veut et 
	 * s'il veut sauvegarder avant de quitter.
	 * 
	 * return Si l'utilisateur poursuit dans sa démarche de quitter.
	 */
	public static boolean veutSortir(Boite boite){
		// Afficher une boîte de dialogue pour demander à l'utilisateur s'il veut quitter
		int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous quitter l'application ?", "Confirmation de sortie", JOptionPane.YES_NO_OPTION);

		if (choix == JOptionPane.YES_OPTION) { // Si l'utilisateur a choisi de quitter
			// Demander à l'utilisateur s'il veut sauvegarder
			int choixSauvegarde = JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder avant de quitter ?", "Sauvegarde avant sortie", JOptionPane.YES_NO_OPTION);

			if (choixSauvegarde == JOptionPane.YES_OPTION) { // Si l'utilisateur a choisi de sauvegarder
				sauvegarderBoite(boite);
			}

			return true; // Indiquer que l'utilisateur veut sortir
		} else {
			return false; // Indiquer que l'utilisateur ne veut pas sortir
		}
	}


	/**
	 * Ajoute un disjoncteur à la boîte.
	 * 	
	 * @param boite la boite où ajouter le disjoncteur
	 * @param d le disjoncteur à ajouter
	 */
	public static void ajouterDisjoncteur(Boite boite, Disjoncteur d){
		// Vérifie s'il y a de la place disponible dans la boite
		if(boite.getNbDisjoncteurs() >= boite.MAX_DISJONCTEURS) {
			// Affiche un message d'erreur si la boite est pleine
			JOptionPane.showMessageDialog(null, "Il n'y a plus de place sur la boite");
		} else {
			// S'il y a de la place disponible, ajoute le disjoncteur à la boite
			int ampere = 0, tension = 0;
			boite.ajouterDisjoncteurDispo(d, ampere, tension);
		}
	}


	/**
	 * Ajoute une demande à un disjoncteur. Si la demande est trop grande,
	 * le disjoncteur est éteint.
	 *
	 * @param boite La boite à considérer.
	 * @param colonne La colonne où se trouve le disjoncteur.
	 * @param ligne La ligne où se trouve le disjoncteur.
	 * @param demande La demande à ajouter.
	 */
	public static void ajouterDemande(Boite boite, int colonne, int ligne, double demande){
		// Vérifie si l'emplacement spécifié contient un disjoncteur
		if(boite.getEmplacementEstVide(--colonne, --ligne)) {
			// Affiche un message d'erreur si l'emplacement est vide
			JOptionPane.showMessageDialog(null, "Il n'y a pas de disjoncteur a cette endroit");
		} else {
			// Ajoute la demande spécifiée au disjoncteur correspondant
			boite.ajouterDemande(colonne, ligne, demande);
		}
	}


	/**
	 * Sert à l'interaction avec l'utilisateur pour obtenir le nom du fichier 
	 * de sauvegarde et sa validation.
	 * 
	 * @return La boite récupérer ou null.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	/**
	 * Sert à l'interaction avec l'utilisateur pour obtenir le nom du fichier 
	 * de sauvegarde et charger une boîte à partir de ce fichier.
	 * 
	 * @return La boite chargée à partir du fichier ou null.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	/**
	 * Récupère une boîte à partir d'un fichier.
	 *
	 * @return La boîte récupérée, ou null si l'utilisateur a annulé l'opération.
	 * @throws IOException Si une erreur se produit lors de la lecture du fichier.
	 * @throws ClassNotFoundException Si la classe Boite n'a pas été trouvée.
	 */
	public static Boite recupererBoite() throws IOException, ClassNotFoundException {
		// Ouvre une fenêtre de sélection de fichier
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choisir le fichier de boîte à charger");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers de boîte (" + DESC_EXTENSION + ")", EXTENSION_BOITE));

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			// Récupère le nom du fichier sélectionné
			String nomFichier = fileChooser.getSelectedFile().getAbsolutePath();
			FileInputStream fileInputStream = null;
			ObjectInputStream objectInputStream = null;

			try {
				// Ouvre le fichier 
				fileInputStream = new FileInputStream(nomFichier);
				objectInputStream = new ObjectInputStream(fileInputStream);
				// Lit la boîte à partir du fichier
				Boite boite = (Boite) objectInputStream.readObject();
				return boite;
			} finally {
				// Ferme les flux d'entrée 
				if (objectInputStream != null) {
					objectInputStream.close();
				}
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			}
		} else {
			return null; // l'utilisateur a annulé
		}
	}










	/**
	 * Sert à l'interaction avec l'utilisateur pour obtenir le nom du fichier 
	 * de sauvegarde et sa validation.
	 * 
	 * @param La boîte à sauvegarder.
	 */

	/**
	 * Sauvegarde une boîte dans un fichier.
	 *
	 * @param boite La boîte à sauvegarder.
	 */
	public static void sauvegarderBoite(Boite boite) {
		// Demande à l'utilisateur de saisir le nom du fichier de sauvegarde
		String nomFichier = JOptionPane.showInputDialog(null, "Veuillez entrer le nom du fichier de sauvegarde :", "Nom du fichier", JOptionPane.QUESTION_MESSAGE);
		if (nomFichier != null && !nomFichier.isEmpty()) { 
			// Ajoute l'extension .bte si nécessaire
			nomFichier += ".bte";
			try {
				// Sauvegarde la boîte dans le fichier
				UtilitaireFichier.sauvegarderBoite(nomFichier, boite);
				JOptionPane.showMessageDialog(null, "Le fichier a été sauvegardé avec succès !\n\"Vous pouvez regarder dans vos Téléchargement\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Nom de fichier invalide !");
		}
	}

}