package gui;
/**
 * Regroupe tous les SP pour g�rer l'affichage d'une bo�te de disjoncteurs 
 * dans le cadre du travail pratique #1 inf111 H2023 (avoir �nonc� en main).
 * 
 * AUX �L�VES : Il n'est pas n�cessaire de lire cette classe pour 
 * r�ussir le travail.  Elle est utilis�e ici pour l'affichage des 
 * disjoncteurs dans le cadre du travail pour lequel la th�orie des GUI 
 * n'a pas encore �t� vue.
 *  
 * @author Pierre B�lisle 
 * @version Copyright H2023
 *
 */
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import modele.Boite;
import modele.Disjoncteur;

public class UtilitaireAffichageBoite {

	public static final int NB_DISJ_PAR_COLONNE = 40;

	// A un effet sur la taille du texte affich�.
	public static final int NB_BTN_LARGEUR = 8;

	/*
	 * La strat�gie est d'utiliser une classe interne GrilleGUI qui permet 
	 * d'afficher dans une grille et d'ajouter des boutons d'options de menu.  
	 * Cette classe existait d�j�, elle a simplement �t� adapt�e pour ce travail.
	 */

	// Permet l'affichage avec les options du menu d�crite dans 
	// OPTIONS_MENU.
	private static GrilleGui gui = 
			new GrilleGui(NB_DISJ_PAR_COLONNE,
					NB_BTN_LARGEUR, 
					Constantes.COULEUR_TEXTE, 
					Constantes.COULEUR_FOND, 
					Constantes.OPTIONS_MENU,
					GrilleGui.QUITTE);

	/**
	 * Retourne si vrai si un des boutons de menu a �t� cliqu�.
	 * 
	 * @return Si un des boutons de menu a �t� cliqu�.
	 */
	public static boolean optionMenuEstCliquee(){
		return gui.estBoutonMenu;
	}

	/**
	 * Retourne la derni�re option cliqu�e et null autrement.
	 * @return Le texte dans le bouton cliqu� s'il y a lieu.
	 */
	public static String getOptionMenuClique(){

		if(gui.estBoutonMenu)
			gui.estBoutonMenu = false;
		else
			gui.optionClique = null;

		return gui.optionClique;
	}


	/**
	 * Affiche les informations que contient la bo�te et les 
	 * options de menu.
	 * 
	 * @param boite La bo�te � afficher.
	 */
	public static void afficherBoite(Boite boite){


		final int NB_COL_AFFICHAGE = 5;

		// Nombre de cases de d�placement pour afficher les NB_COLONNES.
		final int DEP_POSITION = 3;

		gui.effacer();

		afficherInfoBoite(boite);

		// le NB_COL_AFFICHAGE c'est pour les cases repr�sentant la boite
		// contient des disjoncteurs et un espace au centre.
		int posJ = (gui.getNbColonnes() - NB_COL_AFFICHAGE) /
				Boite.NB_COLONNES;

		Disjoncteur disjoncteur;

		for(int i = 0; i < Boite.NB_LIGNES_MAX; i++){

			for(int j = 0; j < Boite.NB_COLONNES; j++){

				gui.setBordureVisible(i,posJ + DEP_POSITION * j, true);

				if(!boite.getEmplacementEstVide(j,i)){

					// �vite pls appels � l'accesseur.
					disjoncteur = boite.getDisjoncteur(j,i);

					if(disjoncteur.getEtat() == Disjoncteur.ETEINT){

						gui.setCouleurFond(i,
								posJ+ j * DEP_POSITION + 1, Color.RED);

						gui.setBordureVisible(i,
								posJ+ j * DEP_POSITION + 1, true);

						gui.setCouleurFond(i, 
								posJ + j * DEP_POSITION, Color.YELLOW);

						gui.setBordureVisible(i,posJ + j * DEP_POSITION, true);
						gui.setValeur(i, posJ + j * DEP_POSITION, 
								disjoncteur.getAmpere() + "A/" +
										disjoncteur.getTension() + "V/" +
										disjoncteur.getPuissanceWatt()  + "W");
					}

					else{
						gui.setCouleurFond(i, 
								posJ + j * DEP_POSITION, Color.GREEN);

						gui.setBordureVisible(i,posJ + j * DEP_POSITION, true);

						gui.setCouleurFond(i, 
								posJ+ j * DEP_POSITION + 1, Color.WHITE);

						gui.setBordureVisible(i,posJ+ j * DEP_POSITION + 1, true);

						gui.setValeur(i, posJ + j * DEP_POSITION + 1, 
								disjoncteur.getAmpere() + "A/" +
										disjoncteur.getTension() + "V/" +
										disjoncteur.getPuissanceWatt()  + "W");
					}					
				}
			}
		}
	}


	/*
	 * Proc�dure locale pour afficher les infos d'en-t�te de la bo�te.
	 */
	private static void afficherInfoBoite(Boite boite){

		//  On veut afficher en bas � gauche.
		int ligne = gui.getNbLignes() - 2;

		gui.setValeur(ligne, 0,"Qte de tension entrée :");
		gui.setValeur(ligne, 1, 
				String.valueOf(boite.getNbDisjoncteursEntree()));

		ligne--;
		gui.setValeur(ligne, 0,"Qte de tension phase :");
		gui.setValeur(ligne, 1, 
				String.valueOf(boite.getNbDisjoncteursPhase()));

		ligne--;
		gui.setValeur(ligne, 0,"Consommation :");
		gui.setValeur(ligne, 1, String.valueOf(boite.getConsommationTotalEnWatt()) + "W");

		ligne--;
		gui.setValeur(ligne, 0, "Capacité :");
		gui.setValeur(ligne, 1, String.valueOf(boite.getMaxAmperes()) + "A");

	}


	/**
	 * Classe locale PRIV�E qui permet la gestion une application de
	 * type grille.  
	 * 
	 *
	 * DESCRIPTION : 
	 * Grille de jeu rectangulaire d'au maximum de
	 * MAX_LIGNES X MAX_COLONNES, ce qui permet d'obtenir s'il y  eu 
	 * un clic, la position du clic et modifier le contenu de la case 
	 * (couleur et texte).
	 * 
	 * Il est possible aussi d'ajouter des boutons de menu.  Dans ce cas, 
	 * estBoutonMenu retourne vrai et getTexteMenu retourne le texte contenu 
	 * dans le bouton.  Ces boutons sont cr��s en bas de la fen�tre � partir d'un
	 * tableau de String fourni au constructeur (mettre null si non d�sir�).
	 * 
	 * Utile pour des TP en inf111 (Jeux de grille  tels Sudoku, Binero, affichage 
	 * d'un tableau 2D, ...)
	 * 
	 * @author Pierre B�lisle (copyright 2016)
	 * @version H2016
	 */
	private static class GrilleGui  implements Runnable{

		/*
		 * STRAT�GIE : On met des boutons dans un panneau et on offre les
		 * m�thodes pour les modifier la couleur de fond et le texte.
		 */

		//Limite pour voir le texte
		public static final int MAX_LIGNES = 50;
		public static final int MAX_COLONNES = 30;

		public static final int TAILLE_CAR = 10;

		// Deux modes de fermeture du gui.  On quitte le programme ou on 
		// dispose juste la fen�tre.
		public static final int QUITTE = JFrame.EXIT_ON_CLOSE;

		// On compose dans un cadre.
		private JFrame cadre = new JFrame();

		// La grille de boutonns qui est affich�e.
		private JButton [][] grille;

		// Retenir la taille de la grille.
		private int nbLignes;
		private int nbColonnes;

		// Les couleurs.
		private Color couleurTexte;
		private Color couleurFond;

		// La taille de l'�cran.
		private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		// Retenir le tableau des options de menus.
		private String [] tabMenus;

		// Pour les options de menus du panneau du bas. 
		private boolean estBoutonMenu;

		// Le texte du bouton cliqu� s'il y a eu un clic sur un des boutons 
		// de menu et il est mis � null apr�s getOptionMenu.
		private String optionClique;


		/**
		 * Cr�e une grille selon les dimensions et les couleurs re�ues.
		 * 
		 * S'il y a un tableau de menus, des boutons  sont ajout�s en bas 
		 * de l'�cran.
		 * 
		 * @param nbLignes L'axe des Y
		 * @param nbColonnes L'axe des X
		 * @param couleurTexte
		 * @param couleurFond
		 * @param tabMenus Les options du menu du bas
		 */
		public GrilleGui(int nbLignes, int nbColonnes, 
				Color couleurTexte, Color couleurFond,
				String[] tabMenus,
				int modeFermeture){

			//On retient la taille et les couleurs de la grille.
			this.nbLignes = (nbLignes>MAX_LIGNES)
					?MAX_LIGNES
							:nbLignes;

			this.nbColonnes = (nbColonnes>MAX_COLONNES)
					?MAX_COLONNES
							:nbColonnes;

			this.couleurFond = couleurFond;
			this.couleurTexte = couleurTexte;

			// On retient les options du menu.
			this.tabMenus = tabMenus;

			// On cr�e le tableau 2D (vide).
			grille = new JButton[nbLignes][nbColonnes];


			// Rien de cliqu� � date.
			estBoutonMenu = false;

			//On cr�e le panneau du bas avec les boutons de menu.

			// On affiche le cadre dans un thread.
			Thread t = new Thread(this);
			t.start();

		}

		/**
		 * Efface la grille d'en haut.
		 */
		public void effacer(){

			/*
			 * Comme c'est un Thread,  il se peut que la grille ne soit pas  
			 * encore cr��e alors on attend.
			 */
			if(grille[0][0]==null)
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}

			for(int i =0; i < grille.length;i++){

				for(int j =0; j < grille[i].length;j++){

					grille[i][j].setBackground(Color.WHITE);
					grille[i][j].setBorderPainted(false);
					grille[i][j].setText(" ");
				}
			}
		}

		/**
		 * Permet de modifier la valeur d'une case de la grille
		 * 
		 * @param coord La position de la case d�sir�e
		 * @param valeur La nouvelle valeur
		 */
		public void setValeur(int y, int x, String valeur){

			/*
			 * Comme c'est un Thread,  il se peut que la grille ne soit pas  
			 * encore cr��e alors on attend.
			 */
			if(grille[y][x]==null){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}
			}
			grille[y][x].setText(valeur);
		}

		/**
		 * Accesseur du nombre de lignes.
		 * @return Le nombre de lignes de la grille.
		 */
		public int getNbLignes() {

			return nbLignes;
		}

		/**
		 * Accesseur du nombre de colonnes.
		 * @return Le nombre de colonnes de la grille.
		 */
		public int getNbColonnes() {

			return nbColonnes;
		}

		/**
		 * Permet de changer la couleur de fond d'une case.
		 * 
		 * @param coord La position de la case.
		 * @param couleur La nouvelle couleur.
		 */
		public void setCouleurFond(int y, int x, Color couleurFond){

			if(grille[y][x]==null){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}
			}

			grille[y][x].setBackground(couleurFond);
		}

		/**
		 * Permet de changer la couleur de texte d'une case.
		 * 
		 * @param coord La position de la case..
		 * @param couleur La nouvelle couleur.
		 */
		public void setBordureVisible(int y, int x, boolean visible){

			if(grille[y][x]==null){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}
			}

			grille[y][x].setBorderPainted(visible);
		}


		/**
		 * Fonction locale pour �viter la r�p�tition de code.
		 * Elle sert � mettre toutes les tailles � la m�me dimension.
		 * 
		 * @param paneau Le panneau � dimensionner.
		 * @param dim La dimension du panneau.
		 */
		private void setTaillePanneau (JPanel panneau, Dimension dim){

			panneau.setMinimumSize(dim);
			panneau.setMaximumSize(dim);
			panneau.setPreferredSize(dim);
		}


		@Override
		public void run() {

			// Plein �cran.
			cadre.setExtendedState(JFrame.MAXIMIZED_BOTH);

			// On quitte sur X.
			cadre.setDefaultCloseOperation(QUITTE);

			// Obtention de la r�f�rence sur le contentPane (�vite pls appels).
			JPanel panneauPrincipal = (JPanel) cadre.getContentPane();

			// Le panneau contenant la grille.
			JPanel panneauHaut = new JPanel();

			// Une disposition en grille pour celui du haut.
			panneauHaut.setLayout(new GridLayout(nbLignes, nbColonnes));

			// On ajoute les boutons vides.
			ajouterBoutons(panneauHaut);

			if(tabMenus != null){

				// Les boutons de menu s'il y en a (FlowLayout par d�faut).
				JPanel panneauBas = new JPanel();		

				Dimension dh = new Dimension (d.width, (int)(d.height*.8));
				Dimension db = new Dimension (d.width, (int)(d.height*.1));

				// La dimension pour l'allure de la fen�tre.
				setTaillePanneau(panneauHaut, dh);
				setTaillePanneau(panneauBas, db);

				ajouterMenu(panneauBas);

				panneauPrincipal.add(panneauHaut, BorderLayout.PAGE_START);
				panneauPrincipal.add(panneauBas, BorderLayout.PAGE_END);			
			}

			else{

				//Le panneau du haut est plein �cran s'il n'y a pas de menu
				panneauPrincipal.add(panneauHaut);
			}

			cadre.setVisible(true);		
		}

		/*
		 * Ajoute des boutons de menu (S'il y en a) au panneau .
		 * 
		 * Si on est ici, on est certain qu'il y a des options de menu.
		 */
		private void ajouterMenu(JPanel panneau){

			JButton b;


			for(int i = 0; i < tabMenus.length; i++){

				b =new JButton(tabMenus[i]);

				// La dimension d'un bouton d�pend de la taille de l'�cran, 
				// on centre la grille.			
				b.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {

						optionClique = ((JButton)e.getSource()).getText();
						estBoutonMenu = true;
					}	
				});


				panneau.add(b);
			}

		}
		/*
		 * Ajoute les boutons dans la grille et dans le panneau.
		 * 
		 * Principalement pour la lisibilit� du code.
		 */
		private void ajouterBoutons(JPanel panneau){

			for(int i = 0; i < nbLignes;i++){

				for(int j = 0; j <nbColonnes;j++){

					grille[i][j] =  new JButton();
					grille[i][j].setBackground(couleurFond);
					grille[i][j].setForeground(couleurTexte);
					grille[i][j].setBorderPainted(false);

					grille[i][j].setFont(new Font("sans serif", 
							Font.BOLD, TAILLE_CAR));

					panneau.add(grille[i][j]);
				}	
			}
		}
	}
}