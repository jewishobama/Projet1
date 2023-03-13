package io;
/*
 * Module utilitaire permettant la saisie au clavier � l'aide de bo�te de
 * dialogue.
 *
 * @Author Pierre B�lisle
 * @version H2023
 */

import javax.swing.JOptionPane;
import java.io.File;
import modele.Disjoncteur;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.IOException;


public class UtilitaireEntreeSortie {


	/*
	 * Fonction locale pour saisir et valider l'amp�rage d'un disjoncteur. 
	 */


	public static int ampereValide(String msgSollic, int amp[]) {

		String entier = null;

		// Version String des valeurs re�ues.
		String String0 =  String.valueOf(amp[0]) ;
		String String1 =  String.valueOf(amp[1]) ;
		String String2 =  String.valueOf(amp[2]) ;
		String String3 =  String.valueOf(amp[3]) ;
		String String4 =  String.valueOf(amp[4]) ;

		do{
			entier = 
					JOptionPane.showInputDialog(msgSollic + 
							String0 + " , " + String1 + " , " + String2 + " , " + String3 + " ou " + String4);

			// V�rifier si c'est convertissable en entier.
			try{

				if(entier != null ){
					int x = Integer.parseInt(entier);
				}
			}
			catch(Exception e){

				// Dans le cas d'une exception, on remet un entier invalide. 
				entier =  String.valueOf(amp[0]-1) ;
			}

		}while(entier != null && 
				(Integer.parseInt(entier) != amp[0] &&
				Integer.parseInt(entier) != amp[1] &&
				Integer.parseInt(entier) != amp[2]&&
				Integer.parseInt(entier) != amp[3]&&
				Integer.parseInt(entier) != amp[4]));


		return (entier== null)?amp[0]-1:Integer.parseInt(entier) ;
	}





	/*
	 * Fonction  pour saisir et valider la tension d'un disjoncteur. 
	 */
	public static int tensionValide(String msgSollic,int entree, int phase) {


		String entier = null;

		// Version String des valeurs re�ues.
		String minString =  String.valueOf(entree) ;
		String maxString =  String.valueOf(phase) ;

		do{
			entier = 
					JOptionPane.showInputDialog(msgSollic + 
							minString + " ou " + maxString);

			// V�rifier si c'est convertissable en entier.
			try{

				if(entier != null ){
					int x = Integer.parseInt(entier);
				}
			}
			catch(Exception e){

				// Dans le cas d'une exception, on remet un entier invalide. 
				entier =  String.valueOf(entree-1) ;
			}

		}while(entier != null && 
				(Integer.parseInt(entier) != entree &&
				Integer.parseInt(entier) != phase));

		return (entier== null)?entree-1:Integer.parseInt(entier) ;
	}

	/**
	 * Saisit et valide un r�el entre min et max.  
	 * La fonction retourne Double.NaN si l'utilisateur annule.
	 * 
	 * 
	 * @param msgSollic Le message affich�.
	 * @param min La plus petite valeur permise.
	 * @param max La plus grande valeur permise.
	 * 
	 * @return L'entier saisit ou min-1 si l'utilisateur annule.
	 */

	/**
	 * Saisit et valide un entier entre min et max.  La fonction retourne min - 1
	 * si l'utilisateur annule.
	 * 
	 * 
	 * @param msgSollic Le message affich�.
	 * @param min La plus petite valeur permise.
	 * @param max La plus grande valeur permise.
	 * 
	 * @return L'entier saisit ou min-1 si l'utilisateur annule.
	 */
	public static int entierValide(String msgSollic, int min, int max) {

		String entier = null;

		// Version String des valeurs re�ues.
		String minString =  String.valueOf(min) ;
		String maxString =  String.valueOf(max) ;

		do{
			entier = 
					JOptionPane.showInputDialog(msgSollic + 
							" entre " + minString + " et " + maxString);

			// V�rifier si c'est convertissable en entier.
			try{

				if(entier != null){
					int x = Integer.parseInt(entier);
				}
			}
			catch(Exception e){

				// Dans le cas d'une exception, on remet un entier invalide. 
				entier =  String.valueOf(min-1) ;
			}

		}while(entier != null && 
				(Integer.parseInt(entier) < min ||
						Integer.parseInt(entier) > max));

		return (entier== null)?min-1:Integer.parseInt(entier) ;
	}

	/**
	 * Saisit et valide un r�el entre min et max.  
	 * La fonction retourne Double.NaN si l'utilisateur annule.
	 * 
	 * 
	 * @param msgSollic Le message affich�.
	 * @param min La plus petite valeur permise.
	 * @param max La plus grande valeur permise.
	 * 
	 * @return L'entier saisit ou min-1 si l'utilisateur annule.
	 */
	public static double reelValide(String msgSollic) {

		String reel = null;

		reel = JOptionPane.showInputDialog(msgSollic);

		// V�rifier si c'est convertissable en r�el.
		try{

			if(reel != null){

				// Tentative de conversion.
				double x = Double.parseDouble(reel);
			}
		}
		catch(Exception e){

			// Dans le cas d'une exception, reel == null. 
			reel =  null;
		}
		return (reel== null)?Double.NaN:Double.parseDouble(reel) ;
	}

}