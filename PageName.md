# Notes #


Compte rendu:
  * on a définit les méthodes et les attributs des classes entités. (je pense qu'il en manque encore (voir downloads).
  * on a testé le diagramme de classe UML en le générant en Java : Fonctionne avec le State et le composite.
  * on a commencé à réfléchir sur les classes contrôles et frontières. (voir downloads)

spécifications additionnelles!
  * Pas de modification possible dans la partie public, Simplement une consultation du contenu publié. plus simple à développer! --> comme dans SPIP -
  * Acces a la partie public : Tout le monde
  * Acces a la partie membre : utilisateur enregistré
  * Acces a la partie privée : rédacteur, gestionnaire, administrateur -
  * Pour gérer le problème des listes contenus rédacteur et gestionnaire, à priori PAS besoin de switch! appel de Utilisateur.setResponsabilitéGestionnaire(Contenu) ou utilisateur.setResponsabilitéRedacteur(Contenu)
ces fonctions seront implémentées différemment selon le statut de l'utilisateur grâce au state pattern.
la fonction ajoutera l'utilisateur dans la liste apropiée dans contenu et dans sa propre liste.
Si aucun Rédacteur et aucun Gestionnaire pour un contenu : l'administrateur seul peut le gérer
A Y REFLECHIR