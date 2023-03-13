package util;
/**
 * Classe qui offre des fonctions math�matiques utilitaires qui n'existent pas 
 * int�gralement en Java.
 * 
 * @Author Pierre B�lisle
 * @version H2023
 */
public class UtilitaireMath {

	/**
	 * Retourne un nombre r�el al�atoire dans un intervalle r�el donn�.
	 * 
	 * @param min La plus petite valeur possible
	 * @param max La plus grande valeur possible
	 * @return Un nombre r�el entre min et max (inclusivement)
	 */
	public static double reelAlea(double min, double max) {

		/*
		 * Strat�gie, on utilise le g�n�rateur de Java qui retourne une valeur 
		 * r�elle entre 0 et 1  ensuite, on le ram�ne dans l'intervalle min..max
		 * 
		 * La division par / 1.000001 est pour s'assurer d'une r�partition 
		 * plus uniforme des valeurs. 
		 */
		return Math.random() / 1.000001 * (max - min + 1) + min;
	}

	/**
	 * Retourne un nombre entier al�atoire dans un intervalle entier donn�.
	 * 
	 * @param min La plus petite valeur possible
	 * @param max La plus grande valeur possible
	 * @return Un nombre entier entre min et max (inclusivement)
	 */
	public static int entierAlea(int min, int max) {

		/*
		 * Strat�gie, on utilise le g�n�rateur de Java qui retourne une valeur 
		 * r�elle entre 0 et 1  ensuite, on le ram�ne dans l'intervalle min..max 
		 * et on le transtype en entier.
		 * 
		 * La division par / 1.000001 est pour s'assurer d'une r�partition 
		 * plus uniforme des valeurs. 
		 * 
		 */
		return (int) (Math.random() / 1.000001 * (max - min + 1) + min);
	}



}
