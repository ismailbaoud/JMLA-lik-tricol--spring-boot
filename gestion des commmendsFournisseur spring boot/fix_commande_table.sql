-- Script pour corriger la table commande et ajouter la séquence auto-increment

-- 1. Créer une séquence pour l'ID
CREATE SEQUENCE IF NOT EXISTS commande_id_seq;

-- 2. Modifier la table pour utiliser la séquence
ALTER TABLE commande ALTER COLUMN id SET DEFAULT nextval('commande_id_seq');

-- 3. Associer la séquence à la colonne
ALTER SEQUENCE commande_id_seq OWNED BY commande.id;

-- 4. Mettre à jour la séquence avec la valeur maximale actuelle (si des données existent)
SELECT setval('commande_id_seq', COALESCE((SELECT MAX(id) FROM commande), 1), false);

-- Vérifier la structure
\d commande;

