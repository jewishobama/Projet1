package io;
/**
 * Classe utilitaire qui permet de sauvegarder dans un fichier binaire ou texte.
 * Elle permet aussi de r�cup�rer une boite.
 *
 * @Author Pierre B�lisle
 * @version H2023
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Map;

import modele.Boite;
import modele.Disjoncteur;

public class UtilitaireFichier {

	// Permet d'�crire dans un fichier texte en colonne et l'ouvrir dans Excel.
	// Il suffit d'�crire un TAB pour changer de colonne.
	private static final String TAB = "\t";

	/**
	 * Sauvegarde la bo�te dans un fichier texte dont on re�oit le nom.
	 * 
	 */
	public static void sauvegarderBoite(String nomFichier, Boite boite) throws IOException {
		PrintWriter writer = null;
		String home = System.getProperty("user.home");

		try {
			writer = new PrintWriter(new FileWriter(home +"\\Downloads\\" + nomFichier));
			// Écrire le nombre d'ampères de la boîte dans le fichier
			writer.println("Nombre d'ampères de la boîte : " + boite.getMaxAmperes());
			// Écrire le temps de l'UPS dans le fichier
			writer.println("Temps de l'UPS : " + boite.temps_ups());
			// Écrire la puissance totale consommée dans le fichier
			writer.println("Puissance totale consommée : " + boite.getConsommationTotalEnWatt());
			// Écrire l'utilisation du circuit par disjoncteur dans le fichier
			writer.println("Utilisation du circuit par disjoncteur :");
			writer.println("Position (colonne/ligne)"+TAB+"Tension\t   Ampérage utilisé\t  (W)\tRatio d'utilisation");
			for (int i = 0; i < boite.NB_COLONNES; i++) {
				for (int j = 0; j < boite.NB_LIGNES_MAX; j++) {
					int q = i+1, p = j+1;
					if(boite.getEmplacementEstVide(i, j)) {
						writer.print(TAB+ q+"-"+p+ TAB+TAB+TAB);
						writer.print("NULL" + TAB+TAB);
						writer.print("NULL" + TAB+TAB);
						writer.print("NULL" + TAB+TAB);
						writer.print("NULL" +  "\n");
					}
					else { 

						writer.print(TAB+q + "-" + p + TAB+TAB+TAB);
						writer.print(boite.getDisjoncteur(i, j).getTension() + TAB+TAB);
						writer.print(boite.getDisjoncteur(i, j).getAmpere() + TAB+TAB);
						writer.print(boite.getDisjoncteur(i, j).getPuissanceWatt() + TAB+TAB);
						writer.print(boite.getDisjoncteur(i, j).getRatio() + "\n");
					}
				}
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}


	/**
	 * Sauvegarde la bo�te dans le fichier binaire avec le nom re�u.
	 * 
	 * On pr�sume le nom de fichier valide.
	 * 
	 * @param nomFic o� sauvegarder la bo�te.
	 * @param boite La bo�te � sauvegarder.
	 */
	public static void sauvegarderBoiteBinaire(String nomFichier, Boite boite) throws IOException {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			// Ouvre le fichier de sortie 
			fileOutputStream = new FileOutputStream(nomFichier);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			// Écrit la boîte dans le fichier binaire
			objectOutputStream.writeObject(boite);
		} finally {
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}
	}


	/**
	 * Ouvre le fichier dont le nom correspond � celui re�u.
	 * 
	 * Exception : Le fichier doit contenir une bo�te sauvegarder par
	 * la m�thode sauvegarderBoite.
	 * 
	 * @param nomFic Le nom du fichier � ouvrir
	 * @return La bo�te contenu dans le fichier.
	 */

	public static Boite recupererBoite(String nomFichier) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			// Ouvre le fichier d'entrée et le flux d'objets associé
			fileInputStream = new FileInputStream(nomFichier);
			objectInputStream = new ObjectInputStream(fileInputStream);
			// Lit la boîte à partir du fichier binaire
			Boite boite = (Boite) objectInputStream.readObject();
			return boite;
		} finally {
			// Ferme les flux d'entrée de manière appropriée
			if (objectInputStream != null) {
				objectInputStream.close();
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}
}
