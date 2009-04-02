package entite;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
@Entity
@Name("file")
@Scope(ScopeType.PAGE)
public class File implements Serializable{
	
	private static final long serialVersionUID = 2585929164591908297L;

	private Long Id_File;

	private String Name;
	
    private String mime;
    
    private long size;
    
    private byte[] data;
    
    @Id @Column(name="ID_FILE")
	@GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId_File() {
		return Id_File;
	}
    
	public void setId_File(Long id_File) {
		Id_File = id_File;
	}
    
	@Column(name="NAME",nullable=false)
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
		int extDot = name.lastIndexOf('.');
		if(extDot > 0){
			String extension = name.substring(extDot +1);
			if("bmp".equals(extension)){
				mime="image/bmp";
			} else if("jpg".equals(extension)){
				mime="image/jpeg";
			} else if("gif".equals(extension)){
				mime="image/gif";
			} else if("png".equals(extension)){
				mime="image/png";
			} else {
				mime = "image/unknown";
			}
		}
	}
	
	@Column(name="SIZE")
    public long getSize() {
            return size;
    }
    public void setSize(long length) {
            this.size = length;
    }
    
    public String getMime(){
            return mime;
    }
    public void setMime(String mime){
        this.mime = mime;
    }
    
	@Lob
	@Column(name="DATA")
	@Basic(fetch=FetchType.LAZY)
    public byte[] getData() {
            return data;
    }
    public void setData(byte[] data) {
            this.data = data;
    }
}

