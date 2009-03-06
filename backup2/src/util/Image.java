package util;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.jboss.seam.annotations.Name;

@Name("image")
public class Image {
	
	@Id @Column(name="ID_IMAGE") 
	@GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    
	
	private String name;
    private String contentType;
    private InputStream content;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}
}