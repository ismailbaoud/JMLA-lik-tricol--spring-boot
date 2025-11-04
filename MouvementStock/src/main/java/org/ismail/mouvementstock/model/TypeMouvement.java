package org.ismail.mouvementstock.model;

/**
 * Énumération représentant les différents types de mouvements de stock.
 *
 * ENTREE: Mouvement d'entrée de stock (achat, retour client, etc.)
 * SORTIE: Mouvement de sortie de stock (vente, perte, etc.)
 * AJUSTEMENT: Ajustement de stock suite à un inventaire ou correction
 */
public enum TypeMouvement {
    ENTREE,
    SORTIE,
    AJUSTEMENT
}
