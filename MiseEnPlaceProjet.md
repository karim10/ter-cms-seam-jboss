# Etapes de la mise en place du projet sous Eclipse

# Details #

Suivre les étapes suivantes:
  * creer un repertoire ter-cms-jboss-seam dans votre workspace et exporter les sources via un logiciel SVN
  * importer le projet ter-cms-jboss-seam depuis Eclipse
  * ajouter un server Tomcat 6.0 et verifier dans le fichier server.xml que l'attribut path de l'element Context (en bas de fichier généralement) est "/cms"
  * creer une base de donnee nommee cms
  * verifier et/ou modifier le fichier hibernate.cfg.xml pour la connexion à la base de donnée