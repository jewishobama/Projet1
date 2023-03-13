package programme;

import java.io.IOException;

import javax.swing.JOptionPane;

import gui.Constantes;
import gui.UtilitaireAffichageBoite;
import gui.UtilitaireGestionMenu;
import io.UtilitaireEntreeSortie;
import modele.Boite;
import modele.Disjoncteur;

/**
 * Dans le cadre du tp1 inf111, il s'agit de simuler la gestion d'une 
 * bo�te �lectrique avec des disjoncteurs.
 *   
 * Il est possible d'ajouter des disjoncteurs, des appareils sur un circuit, de
 * sauvegarder et de r�cup�rer des bo�tes d�crites dans la classe du m�me 
 * nom. 
 *
 * Le projet ne contient qu'une seule bo�te � la fois.
 *
 * (voir �nonc� du travail pour les d�tails).
 * 
 * @author Pierre B�lisle
 * @version Copyright H2023
 *
 */

public class DemarrerBoiteDisjoncteur {

	/*
	 * Strat�gie globale : On utilise les SP des diff�rents modules pour obtenir  
	 * une boite electrique. 
	 * 
	 * C'est ici qu'on g�re la boucle principale qui se termine si l'utilisateur 
	 * quitte ou s'il r�ussit.
	 * 
	 * De plus, on obtient s'il y a eu un clique sur une option de menu, alors
	 *  on d�l�gue au module UtilitaireGestionMenu pour la distribution des 
	 *  t�ches.
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {

		// Obtenir un amp�rage pour la bo�te.
		int max_amperes = UtilitaireEntreeSortie.entierValide("Entrez l'ampérage de la boite", Boite.AMPERAGE_MIN, Boite.AMPERAGE_MAX);

		// Si l'utilisateur n'a pas annulé.
		if(max_amperes != Boite.AMPERAGE_MIN - 1){

			// Récupérer une boîte neuve.
			Boite boite = new Boite(max_amperes);
			Disjoncteur disjoncteur = new Disjoncteur(true,20);
			// Boolean à vrai si l'utilisateur quitte.
			boolean quitter = false;

			// Sert à obtenir une option sélectionnée.
			String option;				

			/* Remplit la boîte avec des disjoncteurs au hasard.
			 *  
			 * NOTE : Peut être demandé à l'utilisateur éventuellement pour simplifier le travail.
			 */
			//boite.remplirAlea();

			// Le programme se termine si l'utilisateur appuie sur le bouton QUITTER ou sur le X. 
			do
			{
				UtilitaireAffichageBoite.afficherBoite(boite);

				// Boucle qui attend que l'utilisateur appuie sur un des boutons.
				while(!UtilitaireAffichageBoite.optionMenuEstCliquee()){

					// Laisse le temps d'intercepter le clic.
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};

				// Récupération de l'option sélectionnée pour éviter plusieurs appels à l'accesseur.
				option = UtilitaireAffichageBoite.getOptionMenuClique();

				// Gestion des options du menu.
				if(option.equals(Constantes.OPTIONS_MENU[Constantes.AJOUTER_DISJONCTEUR])){
					int tension = 0;
					int ampere = 0;
					// Demander la tension et l'ampérage du disjoncteur.
					tension = UtilitaireEntreeSortie.tensionValide("Veuillez entrer la tension du disjoncteur soit : ", Disjoncteur.TENSION_PHASE, Disjoncteur.TENSION_ENTREE);
					if(tension >= 120)
						ampere = UtilitaireEntreeSortie.ampereValide("Veuillez entrer le courrant du disjoncteur soit : ", Disjoncteur.AMPERAGES_PERMIS);
					if(ampere >= 15)
						boite.ajouterDisjoncteurDispo(disjoncteur, ampere, tension);
				}

				else if(option.equals(Constantes.OPTIONS_MENU[Constantes.AJOUTER_APPAREIL])){
					int colonne, ligne = 0; 
					// Demander la colonne et la ligne où ajouter l'appareil.
					colonne = UtilitaireEntreeSortie.entierValide("Veuillez choisir une colonne :", 1 ,2);
					if(colonne >= 1)
						ligne = UtilitaireEntreeSortie.entierValide("Veuillez choisir la ligne :", 1, 30);
					if(ligne >= 1 && !boite.getEmplacementEstVide(colonne-1, ligne-1)) {
						UtilitaireGestionMenu.ajouterDemande(boite, colonne, ligne, UtilitaireEntreeSortie.reelValide("Veuillez entrer la demande d'un appareil qui se branche :"));
					}
					else {
						if(ligne >= 1 && boite.getEmplacementEstVide(colonne-1, ligne-1))
							JOptionPane.showMessageDialog(null, "Il n'y a pas de disjoncteur ici");
					}


				}

				else if(option.equals(Constantes.OPTIONS_MENU[Constantes.RECUPERER])){

					// On essaie dans une autre boîte car si null, on veut garder la
					// boîte qui est ouverte.
					Boite boiteTmp = UtilitaireGestionMenu.recupererBoite();

					if(boiteTmp != null){
						boite = boiteTmp;
					}
				}

				else if(option.equals(Constantes.OPTIONS_MENU[Constantes.SAUVEGARDER])){

					UtilitaireGestionMenu.sauvegarderBoite(boite);

				}

				if(option.equals(Constantes.OPT_QUITTER)){
					quitter = UtilitaireGestionMenu.veutSortir(boite);
				}

			}while(!quitter );
		}
		System.exit(0);


	}
}