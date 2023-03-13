package modele;
import java.io.Serializable;
import util.UtilitaireMath;

import java.util.Arrays;
import java.util.NoSuchElementException;

/*
 * Module qui permet la gestion d'une boîte électrique
 * avec disjoncteurs.
 *
 * La boite doit d'abord être initialisée au nombre d'ampères voulus 
 * ainsi que son nombre de disjoncteurs maximum possibles.
 *
 * Implémente l'interface Serializable pour la sauvegarde
 * dans un fichier binaire. 
 */
public class Boite implements Serializable{

	/**
	 * Enlève un "warning". On ne gère pas les versions.
	 */
	private static final long serialVersionUID = 1L;

	/*********************************
	 *  LES CONSTANTES DE LA BOITE
	 *********************************/

	// Nombre maximum de disjoncteurs dans la boîte
	public static final int MAX_DISJONCTEURS  = 60;

	// Nombre de colonnes dans la boîte
	public static final int NB_COLONNES  = 2;

	// Nombre de lignes dans la boîte, calculé à partir du nombre maximum de disjoncteurs et du nombre de colonnes
	public static final int NB_LIGNES_MAX  = MAX_DISJONCTEURS / NB_COLONNES;

	// Pourcentage de remplissage de la boîte par défaut
	public static final double POURC_REMPLI = 0.6;

	// Pourcentage de tension d'entrée par défaut
	public static final double POURC_TENSION_ENTREE = 0.3;

	// Ampérage minimum et maximum des disjoncteurs dans la boîte
	public static final int AMPERAGE_MIN= 100;
	public static final int AMPERAGE_MAX = 400;

	/*********************************
	 *  LES ATTRIBUTS DE LA BOITE
	 *********************************/

	// Nombre maximum d'ampères dans la boîte
	private int maxAmperes;

	// Puissance minimale aléatoire pour les disjoncteurs
	private double minWattAlea = 0;

	// Puissance maximale aléatoire pour les disjoncteurs
	private double maxWattAlea = 100;

	// Tableau 2D des disjoncteurs dans la boîte
	// La position (x, y) du tableau correspond à la colonne x et la ligne y
	// Toutes les méthodes qui nécessitent la position, reçoivent une coordonnée (colonne-ligne)
	private Disjoncteur[][] tabDisjoncteurs;	

	// Nombre de disjoncteurs dans la boîte
	private int nbDisjoncteurs;

	// Nombre de disjoncteurs de tension d'entrée dans la boîte, déduit en fonction de la différence entre le nombre de disjoncteurs total et le nombre de disjoncteurs de phase
	private int nbDisjoncteursPhase;

	// Constructeur de la boîte, qui initialise le nombre maximum d'ampères et le tableau des disjoncteurs
	public Boite(int max_amperes) {
		this.maxAmperes = max_amperes;
		this.tabDisjoncteurs = new Disjoncteur[NB_COLONNES][NB_LIGNES_MAX];
	}

	/**
	 * Calcule la consommation totale en Watts de la boîte en additionnant la consommation de chaque disjoncteur
	 */
	public double getConsommationTotalEnWatt() {
		double total = 0; // Initialisation de la variable 'total' à 0
		for (int i = 0; i < this.NB_COLONNES; i++) { // Boucle sur les colonnes
			for (int j = 0; j < this.NB_LIGNES_MAX; j++) { // Boucle sur les lignes
				if (this.tabDisjoncteurs[i][j] != null) { // Vérifie si le disjoncteur n'est pas null
					if (this.tabDisjoncteurs[i][j].getEtat() == this.tabDisjoncteurs[i][j].ALLUME) { // Vérifie si le disjoncteur est allumé
						total += this.tabDisjoncteurs[i][j].getTotalDemande(); // Ajoute la demande totale du disjoncteur à la variable 'total'
					}
				}
			}
		}
		return total; // Retourne la consommation totale en Watt
	}


	/**
	 * @return la puissance totale consomm�e sur les disjoncteurs. 
	 */
	/**
	 * @return la puissance totale consomm�e sur les disjoncteurs. 
	 */
	public double puissance_total_boite() {
		double total = 0; // Initialisation de la variable 'total' à 0
		for (int i = 0; i < this.NB_COLONNES; i++) { // Boucle sur les colonnes
			for (int j = 0; j < this.NB_LIGNES_MAX; j++) { // Boucle sur les lignes
				if (this.tabDisjoncteurs[i][j] != null) { // Vérifie si le disjoncteur n'est pas null
					total += this.tabDisjoncteurs[i][j].getPuissanceMaxEnWatt(); // Ajoute la puissance maximale en Watt du disjoncteur à la variable 'total'
				}
			}
		}
		return total; // Retourne la puissance totale de la boîte en Watt
	}



	/*
	 * 
	 * @return  Le temps de support de la charge.
	 */
	public double temps_ups(){

		return this.puissance_total_boite()/(this.getConsommationTotalEnWatt()/240);

	}

	public boolean getEmplacementEncoreDisponible() {
		return this.nbDisjoncteurs < this.MAX_DISJONCTEURS;
	}

	public Disjoncteur getDisjoncteur(int j, int i) throws IndexOutOfBoundsException {
		return this.tabDisjoncteurs[j][i];
	}


	public int getMaxAmperes() {

		return this.AMPERAGE_MAX;

	}

	@Override
	public String toString() {
		return "Boite [maxAmperes=" + maxAmperes + ", minWattAlea=" + minWattAlea + ", maxWattAlea=" + maxWattAlea
				+ ", tabDisjoncteurs=" + Arrays.toString(tabDisjoncteurs) + ", nbDisjoncteurs=" + nbDisjoncteurs
				+ ", nbDisjoncteursPhase=" + nbDisjoncteursPhase + "]";
	}

	public int getAmpereAlea(){

		return Disjoncteur.AMPERAGES_PERMIS[util.UtilitaireMath.entierAlea(0, 4)];
	}

	public void ajouterDisjoncteurDispo(Disjoncteur d, int ampere, int tension) {
		// Obtient un emplacement disponible pour le nouveau disjoncteur
		Coord a = this.getEmplacementDisponible();
		// Crée un nouveau disjoncteur avec la tension et l'ampérage spécifiés
		Disjoncteur d1 = new Disjoncteur(tension, ampere);
		// Place le nouveau disjoncteur dans l'emplacement disponible
		this.tabDisjoncteurs[a.getColonne()][a.getLigne()] = d1;
	}

	public void remplirAlea() {
		int positionC, positionL;
		// Ajoute des disjoncteurs aléatoires jusqu'à atteindre le pourcentage de remplissage spécifié
		for(int i = 0 ; i < this.MAX_DISJONCTEURS * this.POURC_REMPLI;) {
			// Crée un nouveau disjoncteur de phase avec un ampérage aléatoire 
			Disjoncteur d = new Disjoncteur(false, getAmpereAlea());
			// Obtient une position aléatoire pour le disjoncteur
			positionC = util.UtilitaireMath.entierAlea(0, NB_COLONNES-1);
			positionL = util.UtilitaireMath.entierAlea(0, NB_LIGNES_MAX-1);
			if (this.getEmplacementEstVide(positionC,positionL)) {
				// Place le disjoncteur dans l'emplacement vide
				this.tabDisjoncteurs[positionC][positionL] = d;
				i++; // Incrémente le nombre de disjoncteurs ajoutés
			}	
		}
		// Ajoute des disjoncteurs aléatoires jusqu'à atteindre le pourcentage de disjoncteurs d'entrée spécifié
		for(int i = 0 ; i < this.MAX_DISJONCTEURS * (this.POURC_TENSION_ENTREE) ;) {
			// Crée un nouveau disjoncteur d'entree avec un ampérage aléatoire 
			Disjoncteur d = new Disjoncteur(true, getAmpereAlea());
			// Obtient une position aléatoire pour le disjoncteur
			positionC = util.UtilitaireMath.entierAlea(0, NB_COLONNES-1);
			positionL = util.UtilitaireMath.entierAlea(0, NB_LIGNES_MAX-1);
			if (this.getEmplacementEstVide(positionC,positionL)) {
				// Place le disjoncteur dans l'emplacement vide
				this.tabDisjoncteurs[positionC][positionL] = d;
				i++; // Incrémente le nombre de disjoncteurs ajoutés
			} 
		}
	}



	public Coord getEmplacementDisponible() {
		Coord x = new Coord();
		// Vérifie s'il y a un emplacement disponible dans la boîte
		if(this.getEmplacementEncoreDisponible()) {
			// Boucle sur les colonnes et les lignes pour trouver un emplacement vide
			for (int i = 0; i < this.NB_COLONNES && this.tabDisjoncteurs != null; i++) {
				for (int j = 0; j < this.NB_LIGNES_MAX && this.tabDisjoncteurs != null; j++) {
					if(this.tabDisjoncteurs[i][j] == null) {
						x.setColonne(i); // Affecte la colonne de l'emplacement trouvé à l'objet Coord 'x'
						x.setLigne(j); // Affecte la ligne de l'emplacement trouvé à l'objet Coord 'x'
					}
				}
			}
			return x; // Retourne l'emplacement trouvé
		}
		return null; // Retourne null s'il n'y a pas d'emplacement disponible dans la boîte
	}

	public void ajouterDisjoncteur(int colonne, int ligne, Disjoncteur d) throws IndexOutOfBoundsException, NoSuchElementException {
		// Vérifie si l'emplacement spécifié est vide
		if(this.getEmplacementEstVide(colonne, ligne)) {
			this.tabDisjoncteurs[colonne][ligne] = d; // Ajoute le disjoncteur à l'emplacement spécifié
		}		
	}

	public void ajouterDemande(int i, int j, double demande) {
		// Vérifie si le disjoncteur à l'emplacement spécifié n'est pas null
		if(this.tabDisjoncteurs[i][j] != null) {
			this.tabDisjoncteurs[i][j].modifPuissance(demande); // Modifie la puissance du disjoncteur à l'emplacement spécifié
		}		
	}

	//voir methode modifPuissance, elle fait les 2
	//public void retirerPuissance(int i, int j, double demande) {

	// � �crire


	//}

	public int getNbDisjoncteurs() {
		int nbDisjoncteurs = 0;
		// Boucle sur les colonnes et les lignes pour compter le nombre de disjoncteurs dans la boîte
		for (int i = 0; i < this.NB_COLONNES; i++) {
			for (int j = 0; j < this.NB_LIGNES_MAX ; j++) {
				if(this.tabDisjoncteurs[i][j] != null) {
					nbDisjoncteurs++; // Incrémente le nombre de disjoncteurs
				}
			}
		}
		return nbDisjoncteurs; // Retourne le nombre de disjoncteurs dans la boîte
	}

	public int getNbDisjoncteursPhase() {
		int nbDisjoncteursP = 0;
		// Boucle sur les colonnes et les lignes pour compter le nombre de disjoncteurs de phase dans la boîte
		for (int i = 0; i < this.NB_COLONNES; i++) {
			for (int j = 0; j < this.NB_LIGNES_MAX ; j++) {
				if(this.tabDisjoncteurs[i][j] != null) {
					if(this.tabDisjoncteurs[i][j].getTension() == this.tabDisjoncteurs[i][j].TENSION_PHASE) {
						nbDisjoncteursP++; // Incrémente le nombre de disjoncteurs de phase
					}
				}
			}
		}
		return nbDisjoncteursP; // Retourne le nombre de disjoncteurs de phase dans la boîte
	}

	public int getNbDisjoncteursEntree() {
		int nbDisjoncteursE = 0;
		// Boucle sur les colonnes et les lignes pour compter le nombre de disjoncteurs d'entrée dans la boîte
		for (int i = 0; i < this.NB_COLONNES; i++) {
			for (int j = 0; j < this.NB_LIGNES_MAX ; j++) {
				if(this.tabDisjoncteurs[i][j] != null) {
					if(this.tabDisjoncteurs[i][j].getTension() == this.tabDisjoncteurs[i][j].TENSION_ENTREE) {
						nbDisjoncteursE++; // Incrémente le nombre de disjoncteurs d'entrée
					}
				}
			}
		}
		return nbDisjoncteursE; // Retourne le nombre de disjoncteurs d'entrée dans la boîte
	}

	public boolean getEmplacementEstVide(int colonne, int ligne) {
		// Vérifie si l'emplacement spécifié est vide
		if(this.tabDisjoncteurs[colonne][ligne] == null) 
			return true; // Retourne true si l'emplacement est vide

		return false; // Retourne false si l'emplacement est occupé
	}

}