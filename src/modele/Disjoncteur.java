package modele;
import java.io.Serializable;

import util.Liste;

/*
 * Classe qui regroupe tout qui concerne un
 * disjoncteur dans le projet.
 *
 * On y retrouve les constantes et les sous-programmes
 * liés à un disjoncteur.
 * 
 * Implémente l'interface Serializable pour la sauvegarde
 * dans un fichier binaire. 
 * 
 * @suthor Pierre Bélisle
 * @version Copyright H2023
 */
public class Disjoncteur implements Serializable{

	/**
	 * Enlève un "warning". On ne gère pas les versions.
	 */
	private static final long serialVersionUID = 1L;

	// État possible d'un disjoncteur.
	public static final int ALLUME = 1;
	public static final int ETEINT = 0;

	// Choix d'ampérages possibles.
	private static final int MIN_AMPERAGE = 15;
	private static final int MAX_AMPERAGE = 60;

	// Tous les ampérages permis dans un tableau.  
	public static final int AMPERAGES_PERMIS[] =
		{MIN_AMPERAGE, 20, 40, 50, MAX_AMPERAGE};

	// Construction d'une chaîne avec les ampérages permis. Sert à valider.
	public static final  String CHAINE_AMPERAGE_PERMIS = 
			"15/20/40/50/60";

	// Les tensions possibles.
	public static final int TENSION_ENTREE = 240;
	public static final int TENSION_PHASE = 120;

	// Construction d'une chaîne avec les tensions permises. Sert à valider.
	public static final  String CHAINE_TENSION_PERMISE = 
			"120/240";

	/******************************
	 Les attributs d'un disjoncteur
	 ********************************/
	private boolean estEntree; // Indique si le disjoncteur est à l'entrée ou sur une phase.
	private int ampere; // L'ampérage du disjoncteur.
	private int tension; // La tension du disjoncteur.
	private Liste demandeDuCircuit; // Une liste qui contient les demandes (charge) sur le circuit.
	private int etat; // ALLUME ou ETEINT.

	// Les multiples getters
	public double getAmpere() {
		return ampere;
	}

	public double getTension() {

		return tension;
	}
	public void setAmpere(int ampere) {
		this.ampere = ampere;
	}
	public void setTension(int tension) {
		this.tension = tension;
	}

	public int getEtat() {
		return etat;
	}

	// Construction d'un disjoncteur, créé à partir de la tension et ampérage. Initialise l'état à 0 et une liste vide.
	public Disjoncteur(int tension, int ampere) {
		this.ampere = ampere;
		this.tension = tension;
		this.etat = ETEINT;
		this.demandeDuCircuit = new Liste();
	}

	// Construction d'un disjoncteur, créé à partir d'un boolean estEntree et ampérage. Initialise l'état à 0 et une liste vide.
	public Disjoncteur(boolean estEntree, int ampere) {
		if(estEntree) {
			this.ampere = ampere;
			this.tension = TENSION_ENTREE;
		}
		else {
			this.ampere = ampere;
			this.tension = TENSION_PHASE;
		}
		this.etat = ETEINT;
		this.demandeDuCircuit = new Liste();
	}

	// Méthode pour obtenir la demande totale en puissance (4.2)
	public double getTotalDemande() {
		return this.demandeDuCircuit.calculerTotal(); // retourne la somme de toutes les demandes de puissance enregistrées dans la liste "demandeDuCircuit"
	}

	// Méthode pour obtenir la puissance maximale en watts que le disjoncteur peut supporter
	public double getPuissanceMaxEnWatt() {
		return this.MAX_AMPERAGE * this.TENSION_ENTREE * 0.8; // calcule la puissance maximale en utilisant les constantes "MAX_AMPERAGE" et "TENSION_ENTREE" et la retourne
	}

	@Override
	public String toString() {
		return "Disjoncteur [estEntree=" + estEntree + ", ampere=" + ampere + ", tension=" + tension
				+ ", demandeDuCircuit=" + demandeDuCircuit + ", etat=" + etat + "]";
	}

	// Méthode pour obtenir la puissance actuelle en watts consommée par le circuit
	public double getPuissanceWatt() {
		return this.tension * this.ampere; // calcule la puissance en multipliant la tension par l'ampérage
	}

	// Méthode pour ajouter ou retirer une demande de puissance
	public void modifPuissance(double puissance) {
		// Ajoute la nouvelle demande de puissance au début de la liste "demandeDuCircuit"
		this.demandeDuCircuit.ajouterAuDebut(puissance);
		// Si la demande totale de puissance est supérieure à la puissance maximale que le disjoncteur peut supporter, l'état du disjoncteur est "éteint"
		if (this.getTotalDemande() >= this.getPuissanceWatt()) {
			this.etat = ETEINT;
		}
		// Sinon, l'état du disjoncteur est "allumé"
		else {
			this.etat = ALLUME;
		}
		// Si une demande de puissance négative est ajoutée, on la retire de la liste 
		if (puissance < 0 && this.demandeDuCircuit.indexOf(-puissance) != -1) {
			this.demandeDuCircuit.retirer(this.demandeDuCircuit.indexOf(puissance));
			this.demandeDuCircuit.retirerPremier();
			// Si l'ampérage du disjoncteur est inférieur à la valeur maximale, l'état du disjoncteur est "allumé"
			if (this.ampere <= this.MAX_AMPERAGE) {
				this.etat = ALLUME;
			}
		}
	}

	// Méthode pour obtenir le ratio de la puissance consommée par le circuit sur la puissance maximale que le disjoncteur peut supporter
	public double getRatio() {
		return (this.totalCicuit() / this.getPuissanceMaxEnWatt()) * 100; // Calcule le ratio en pourcentage en divisant la puissance totale du circuit par la puissance maximale et en multipliant par 100
	}

	// Méthode pour calculer la puissance totale du circuit
	public double totalCicuit() {
		if (!this.demandeDuCircuit.estVide()) {
			return this.demandeDuCircuit.calculerTotal(); // Retourne la somme de toutes les demandes de puissance enregistrées dans la liste "demandeDuCircuit"
		}
		return 0;
	}

}
