package coursjsf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Fonctions statiques techniques pour la lecture du fichier catalogue.data, servant à initialiser le 
 * contenu du catalogue des produits. 
 */
public class CatalogueDataUtil {
	
	/**
	 * Découpe les lignes lues du fichier texte catalogue.data et construit les objets 
	 * des classes Produit et LigneCatalogue. 
	 * @return les lignes du catalogue
	 * @throws IOException
	 */
	public static List<LigneCatalogue> chargeLignesCatalogue() throws IOException {
		// Lit les lignes de catalogue depuis le fichier catalogue.data
		List<LigneCatalogue> lignes = new ArrayList<LigneCatalogue>();
		String data = readAsString(
						loadResource(
							CatalogueDataUtil.class.getPackage().getName() + "/catalogue.data"));
		// Découpe en lignes de catalogue (¤¤ indique la fin de ligne)
		String[] lignesData = data.split("¤¤");
		for (String ligneData : lignesData) {
			// Découpe les champs d'une ligne de catalogue (un seul ¤ sépare les champs)
			String[] fields = ligneData.trim().split("¤"); 
			if (fields!=null && fields.length==4) {
				// Crée la ligne de catalogue
				lignes.add(
						new LigneCatalogue(
								new Produit(StringUtils.trim(fields[0]), 
											StringUtils.trim(fields[1]),
											StringUtils.trim(fields[3])),
								Double.parseDouble(StringUtils.trim(fields[2]))));
			}
		}
		return lignes; 
	}
	
	/**
	 * Renvoie sous la forme d'une chaîne de caractères le contenu d'un Reader.
	 * @param reader le Reader à lire.
	 * @return le contenu du Reader sous la forme d'une String.
	 * @throws IOException
	 */
	private static String readAsString(Reader reader) throws IOException {
		StringBuilder sb = new StringBuilder(5000);
		BufferedReader in = new BufferedReader(reader);
		String line;
		while ((line = in.readLine())!=null) {
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Charge un fichier présent dans le classpath.
	 * @param resourcePath chemin dans le classpath.
	 * @return le flux (Reader) sur le fichier ressource.
	 * @throws IOException
	 */
	private static Reader loadResource(String resourcePath) throws IOException {
		ClassLoader classLoader = CatalogueDataUtil.class.getClassLoader();
		URL url = classLoader.getResource(resourcePath);
		if (url == null) {
			throw new IOException("Can't find resource " + resourcePath);
		}
		return new InputStreamReader(url.openStream());
	}
	
}
