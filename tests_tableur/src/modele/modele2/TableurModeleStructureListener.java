package modele.modele2;

public interface TableurModeleStructureListener {
	void onColonneInsered(int numero);

	void onColonneRemoved(int numero);

	void onLigneInsered(int numero);

	void onLigneRemoved(int numero);
}
