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

import entite.Contenu;
import entite.File;

@SuppressWarnings("serial")
@Scope(ScopeType.SESSION)
@Name("fileUploadBean")
public class FileUploadBean implements Serializable{

	@In(required=false)
	private Contenu contenu;
	private List<File> files = new ArrayList<File>();
	private File logo = new File();
	private int uploadsAvailable = 5;
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
	
	public synchronized void paint(OutputStream stream, Object object) throws IOException {
		stream.write(getFiles().get((Integer)object).getData());
	}
	
	public synchronized void listenerFiles(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		File file = new File();
		byte[] ImageData = getBytesFromFile(item.getFile());
		file.setSize(item.getFile().length());
		file.setName(item.getFileName());
		file.setData(ImageData);
		files.add(file);
		uploadsAvailable--;
	}
	
	public synchronized void listenerLogo(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		File file = new File();
		byte[] ImageData = getBytesFromFile(item.getFile());
		file.setSize(item.getFile().length());
		file.setName(item.getFileName());
		file.setData(ImageData);
		logo = file;
	}

	// Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(java.io.File file) throws IOException {
		InputStream is = new FileInputStream(file);
		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int)length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}


	public String clearUploadData() {
		files.clear();
		setUploadsAvailable(5);
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

	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
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

	public Contenu getContenu() {
		return contenu;
	}
	
	@Destroy
	public void destroy(){}
}
