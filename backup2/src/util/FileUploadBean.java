package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import entite.Article;
import entite.Contenu;
import entite.File;
import exception.ContenuException;

@SuppressWarnings("serial")
@Scope(ScopeType.SESSION)
@Name("fileUploadBean")
public class FileUploadBean implements Serializable{

	@In(required=false)
	private Contenu contenu;
	private List<File> files = new ArrayList<File>();
	private File logo;
	private int uploadsAvailableFiles = 5;
	private int uploadsAvailableLogo = 1;
	private boolean autoUpload = false;
	public int getSize() {
		if (getFiles().size()>0){
			return getFiles().size();
		}else 
		{
			return 0;
		}
	}

	public FileUploadBean() {
	}

//	public synchronized void paint(OutputStream stream, Object object) throws IOException {
//		if(getFiles()!=null || !getFiles().isEmpty())
//		stream.write(getFiles().get((Integer)object).getData());
//	}
	
	public synchronized void paintBd(OutputStream stream, Object object) throws IOException {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		File logo = (File)HibernateUtil.getSessionFactory().getCurrentSession().load(File.class,contenu.getLogo().getId_File());
		if(logo!=null)
		stream.write(logo.getData());
	}
	
	public synchronized void paintLogoContenu(OutputStream stream, Object object) throws IOException {
		stream.write(contenu.getLogo().getData());
		setUploadsAvailableLogo(0);
	}
	
	public synchronized void paintLogo(OutputStream stream, Object object) throws IOException {
		stream.write(logo.getData());
	}
	
	public synchronized void paintFilesContenu(OutputStream stream, Object object) throws IOException {
		stream.write(getFiles().get((Integer)object).getData());
	}
	
	public synchronized void paintFiles(OutputStream stream, Object object) throws IOException {
		if(!(contenu instanceof Article))throw new ContenuException("Il n'est pas possible d'afficher les fichiers d'un contenu autre qu'un article");
		stream.write(((Article)contenu).getFiles().get((Integer)object).getData());
	}
	
	public synchronized void listenerFiles(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		File file = new File();
		byte[] ImageData = getBytesFromFile(item.getFile());
		file.setSize(item.getFile().length());
		file.setName(item.getFileName());
		file.setData(ImageData);
		files.add(file);
		uploadsAvailableFiles--;
	}
	
	public synchronized void listenerLogo(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		File file = new File();
		byte[] ImageData = getBytesFromFile(item.getFile());
		file.setSize(item.getFile().length());
		file.setName(item.getFileName());
		file.setData(ImageData);
		setLogo(file);
		uploadsAvailableLogo--;
	}

	/**
	 * <p></p>
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public static byte[] getBytesFromFile(java.io.File file) throws Exception {
		InputStream is = new FileInputStream(file);

		long length = file.length();

		// Verifie la taille pour convertir en int ensuite
		if (length > Integer.MAX_VALUE) throw new Exception("la taille du fichier est trop grande");

		// Création du tableau pour contenir les données du fichiers
		byte[] bytes = new byte[(int)length];

		// lie le fichier
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		// assure que tous les bytes du fichiers on été lu
		if (offset < bytes.length) {
			throw new IOException("le fichier n'a pas pu être lu entierement "+file.getName());
		}
		is.close();
		
		return bytes;
	}


	public String clearUploadData() {
		files.clear();
		logo = new File();
		setUploadsAvailableFiles(5);
		setUploadsAvailableLogo(1);
		return null;
	}

	public long getTimeStamp(){
		return System.currentTimeMillis();
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) { 
		this.files = files;
	}
	
	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public void setLogo(File logo) {
		this.logo = logo;
	}

	public File getLogo() {
		return logo;
	}

	public void setContenu(Contenu contenu) {
		this.contenu = contenu;
	}

	public void setUploadsAvailableFiles(int uploadsAvailableFiles) {
		this.uploadsAvailableFiles = uploadsAvailableFiles;
	}

	public int getUploadsAvailableFiles() {
		return uploadsAvailableFiles;
	}

	public Contenu getContenu() {
		return contenu;
	}
	
	public void setUploadsAvailableLogo(int uploadsAvailableLogo) {
		this.uploadsAvailableLogo = uploadsAvailableLogo;
	}

	public int getUploadsAvailableLogo() {
		return uploadsAvailableLogo;
	}

	@Destroy
	public void destroy(){}

}
