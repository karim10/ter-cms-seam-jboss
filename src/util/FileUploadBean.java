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

import composant.GestionContenu;
import composant.GestionFrontEnd;
import composant.GestionNews;

import entite.File;

@SuppressWarnings("serial")
@Scope(ScopeType.SESSION)
@Name("fileUploadBean")
public class FileUploadBean implements Serializable{

	@In(required=false)
	private GestionNews gestionNews;
	@In(required=false)
	private GestionFrontEnd gestionFrontEnd;
	@In(required=false)
	private GestionContenu gestionContenu;
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

	
	public synchronized void paintLogoContenu(OutputStream stream, Object object) throws IOException {
		stream.write(gestionContenu.getContenu().getLogo().getData());
		setUploadsAvailableLogo(0);
	}
	
	public synchronized void paintUploadLogo(OutputStream stream, Object object) throws IOException {
		stream.write(getLogo().getData());
		setUploadsAvailableLogo(0);
	}
	
	public synchronized void paintLogoContenuGF(OutputStream stream, Object object) throws IOException {
		stream.write(gestionFrontEnd.getContenu().getLogo().getData());
		setUploadsAvailableLogo(0);
	}
	
	public synchronized void paintLogoContenuGN(OutputStream stream, Object object) throws IOException {
		stream.write(gestionNews.getNews().getLogo().getData());
		setUploadsAvailableLogo(0);
	}
	
	public synchronized void paintLogo(OutputStream stream, Object object) throws IOException {
		stream.write(gestionFrontEnd.getListContenuFront().get(((Integer)object)).getLogo().getData());
		setUploadsAvailableLogo(0);
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

		// Cr�ation du tableau pour contenir les donn�es du fichiers
		byte[] bytes = new byte[(int)length];

		// lie le fichier
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		// assure que tous les bytes du fichiers on �t� lu
		if (offset < bytes.length) {
			throw new IOException("le fichier n'a pas pu �tre lu entierement "+file.getName());
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

	public void setUploadsAvailableFiles(int uploadsAvailableFiles) {
		this.uploadsAvailableFiles = uploadsAvailableFiles;
	}

	public int getUploadsAvailableFiles() {
		return uploadsAvailableFiles;
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
