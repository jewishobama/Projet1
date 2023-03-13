package modele;
/**
 * Cette classe représente les coordonnées 
 * possible dans différents jeux de grille (ligne-colonne).
 * 
 * Les attributs sont utilisables à l'aide des méthodes.
 * (même principe que  java.awt.Dimension).
 * 
 *@author pbelisle
 *@version Copyright H2009
 *@revisite H2023
 */
public class Coord {


	/*
	 * Les items choisis par l'utilisateur 
	 */
	private int ligne;
	private int colonne;

	/**
	 * Constructeur par défaut avec la coordonnée  (0,0)
	 */
	public Coord() {

		ligne = 0;
		colonne = 0;
	}


	/**
	 * Accesseur de la ligne.
	 * 
	 * @return La ligne
	 */
	public int getLigne() {

		return ligne;
	}

	/**
	 * Mutateur de la ligne.
	 * 
	 * @param ligne La ligne de remplacement.
	 */
	public void setLigne(int ligne) {

		this.ligne = ligne;
	}

	/**
	 * Accesseur de la colonne.
	 * 
	 * @return La colonne.
	 */
	public int getColonne() {

		return colonne;
	}

	/**
	 * Mutateur de la colonne.
	 * 
	 * @param colonne La colonne de remplacement.
	 */
	public void setColonne(int colonne) {

		this.colonne = colonne;
	}

	/**
	 * Construit une chaîne avec les attributs et la retourne.
	 * 
	 * @return Une chaîne contenant les infos de la coordonnée.
	 */
	public String toString(){

		return "(" + colonne + "-" + ligne + ")";
	}
}