# TER : outil de CMS sur la plateforme Seam #

**Applications de gestion**

http://www.iocean.fr développe des applications de gestion spécifiques, pour de grosses entreprises, des associations ou des collectivités territoriales.
Le développement se fait sur diverses plateformes, parfois imposées par le client : PHP, Java avec framework maison, et celle qui concerne ce projet : Java avec Seam/JSF/Hibernate.

**Plateforme Seam**

  * composants Seam pour les traitements
  * vues JSF, écrites avec Facelets (pas de JSP)
> composants JSF de la bibliothèque RichFaces (en plus des composants standards fournis avec JSF, et des quelques composants JSF fournis avec Seam)
  * persistance du modèle de données avec Hibernate, de façon indépendante de la base relationnelle
  * Tomcat ou tout autre serveur d’application (Glassfich, JBoss AS…).

**Intranet informatif**

Le CMS peut servir à publier des informations directement liées à l’application spécifique, concernant son utilisation, ou constituer un véritable intranet informatif de la société, sans rapport direct avec l’application, si ce n’est par une interface d’administration homogène avec celle de l’application (menus, gestion des utilisateurs et des droits).

**Contenu**

  * des articles, organisés en rubriques, affichés dans une page entière (un article par page), avec du texte mis en forme, et pouvant comporter des images, ainsi qu’une vignette servant de logo à l’article pour l’affichage sur la liste des articles
  * des nouvelles brèves (news), dont les plus récentes sont affichées sur une même page, avec la possibilité d’accéder aux pages de nouvelles plus anciennes
  * des documents à télécharger (PDF, Word ou autres), joints à un article
  * des galeries d’images ?

Les articles et les nouvelles brèves ne sont pas forcément différents en nature au niveau de la gestion du contenu. La différence peut résider seulement dans les templates d’affichage.

**Validation et publication, et droits des utilisateurs**

Certains utilisateurs doivent pouvoir soumettre des articles ou des nouvelles sans avoir le droit de les publier directement.
Les utilisateurs autorisés à publier voient la liste des articles soumis, et peuvent les publier éventuellement après modification.
Il est inutile d’intégrer un véritable moteur de workflow pour la validation des articles.

**Templates des pages publiques**

Les templates des pages publiques seront créés spécifiquement pour un client, en cohérence avec ceux de l’application spécifique (charte graphique, menus, etc.)
Ces templates seront des pages Facelets, autant ne pas utiliser un autre langage de templates. Par contre il peut être utile de fournir des composants Facelets pour simplifier leur écriture. Et ils feront évidemment appel aux composants Seam fournis par le CMS, par exemple pour récupérer les données d’un article, ou la liste des derniers articles d’une rubrique.

**Back office – interface de gestion du contenu**

Comme les pages publiant le contenu seront réalisées spécifiquement pour chaque projet client, les pages web fournies par le CMS et utilisées telles quelles seront celles du back-office, permettant de gérer l’arborescence des rubriques du site, de rédiger des articles pouvant contenir des images et des documents joints, et de les publier.
L’aspect visuel (menus, charte graphique) devra pouvoir être facilement adapté en fonction des contraintes du client, pour pouvoir intégrer le back-office du CMS à l’interface d’administration de l’application spécifique. Il faudra donc limiter les endroits à modifier pour permettre cette adaptation, en utilisant des compositions ou templates Facelets pour les pages web du CMS.

**Gestion des utilisateurs et des droits**

La gestion des utilisateurs et des droits sera généralement implémentée dans l’application spécifique, et servira également au CMS, pour ne pas avoir à gérer les droits de deux façons séparées.
Les droits de gestionnaire et de rédacteur pourront dépendre des rubriques du contenu : un utilisateur pourra être gestionnaire d’une rubrique, seulement rédacteur pour une autre rubrique, et n’avoir aucun droit sur une troisième rubrique.
Le CMS fournira des interfaces Java avec les méthodes nécessaires, qui seront implémentées par un ou plusieurs composants Seam de l’application spécifique. Par exemple des méthodes pour :

  * obtenir des infos sur l’utilisateur connecté (nom, prénom)
  * savoir si l’utilisateur connecté est administrateur
  * savoir si l’utilisateur connecté est gestionnaire d’une rubrique donnée
  * savoir si l’utilisateur connecté est rédacteur d’une rubrique donnée.