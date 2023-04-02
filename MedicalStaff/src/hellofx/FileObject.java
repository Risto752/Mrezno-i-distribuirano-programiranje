package hellofx;

import java.io.Serializable;

public class FileObject implements Serializable {
	
	
	public byte[] content;
	public String fileName;
	
	
	FileObject(byte[] content,String fileName){
		
		this.content = content;
		this.fileName = fileName;
		
	}
	
	

}
