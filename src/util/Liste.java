package util;

import java.util.NoSuchElementException;

public class Liste {

	private Noeud tete;
	private Noeud fin;
	private int nbElements;

	public Liste() {
		this.tete = this.fin = null;
		this.nbElements = 0;
	}

	public void ajouterAuDebut(Object obj) {
		Noeud nouveau = new Noeud(tete, null, obj);
		if (tete != null) {
			tete.precedent = nouveau;
		}
		tete = nouveau;
		if (fin == null) {
			fin = nouveau;
		}
		nbElements ++;
	}

	public void ajouterALaFin(Object obj) {
		Noeud nouveau = new Noeud(null, fin, obj);
		if (fin != null) {
			fin.suivant = nouveau;
		}
		fin = nouveau;
		if (tete == null) {
			tete = nouveau;
		}
		nbElements ++;
	}

	public void ajouter(int index, Object obj) throws IndexOutOfBoundsException {
		if (index < 0 || index > nbElements) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			ajouterAuDebut(obj);
			return;
		}
		if (index == nbElements) {
			ajouterALaFin(obj);
			return;
		}
		Noeud temp = tete;
		int i = 0;
		while (i < index - 1) {
			temp = temp.suivant;
			i++;
		}
		Noeud nouveau = new Noeud(temp.suivant, temp, obj);
		temp.suivant = nouveau;
		nouveau.suivant.precedent = nouveau;
		nbElements++;
	}

	public Object get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= nbElements) {
			throw new IndexOutOfBoundsException();
		}
		Noeud temp = tete;
		int i = 0;
		while (i < index) {
			temp = temp.suivant;
			i++;
		}
		return temp.donnee;
	}

	public Object retirerDernier() throws NoSuchElementException {
		if (estVide()) {
			throw new NoSuchElementException();
		}
		Object x = fin.donnee;
		if (tete == fin) {
			tete = fin = null;
		} else {
			fin = fin.precedent;
			fin.suivant = null;
		}
		nbElements--;
		return x;
	}

	public Object retirerPremier() throws NoSuchElementException {
		if (estVide()) {
			throw new NoSuchElementException();
		}
		Object x = tete.donnee;
		if (tete == fin) {
			tete = fin = null;
		} else {
			tete = tete.suivant;
			tete.precedent = null;
		}
		nbElements--;
		return x;
	}
	public Object retirer(int index) throws IndexOutOfBoundsException, NoSuchElementException {
		if (index < 0 || index >= nbElements) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			return retirerPremier();
		}
		if (index == nbElements - 1) {
			return retirerDernier();
		}
		Noeud temp = tete;
		int i = 0;
		while (i < index) {
			temp = temp.suivant;
			i++;
		}
		Object x = temp.donnee;
		temp.precedent.suivant = temp.suivant;
		temp.suivant.precedent = temp.precedent;
		nbElements--;
		return x;
	}


	public void vider() {
		while (tete != null) {
			retirerPremier();
		}
	}

	public int indexOf(Object obj) {
		Noeud temp = tete;
		int index = 0;
		while (temp != null) {
			if (temp.donnee.equals(obj)) {
				return index;
			}
			temp = temp.suivant;
			index++;
		}
		return -1;
	}

	public boolean estVide() {
		return nbElements==0;
	}

	public int taille() {
		return nbElements;
	}
	public double calculerTotal() {
		double total = 0.0;
		Noeud temp = tete;
		while (temp != null) {
			Object obj = temp.donnee;
			if (obj instanceof Number) {
				total += ((Number) obj).doubleValue();
			}
			temp = temp.suivant;
		}
		return total;
	}


	private class Noeud {
		public Object donnee;
		public Noeud suivant;
		public Noeud precedent;

		public Noeud(Noeud next, Noeud previous, Object data) {
			this.donnee = data;
			this.suivant = next;
			this.precedent = previous;
		}
	}
}