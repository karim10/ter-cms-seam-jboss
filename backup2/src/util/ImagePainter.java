package util;

import java.io.*;

public class ImagePainter {

	private String fileName = "telephone.png";

	public void drawImage (OutputStream output, Object object) throws IOException{

		File file = new File(fileName);
		byte byteArray[] = new byte[1000];

		FileInputStream inputStream = new FileInputStream(file);
		while(true){
			try{
				int bytesRead = inputStream.read(byteArray);
				output.write(byteArray, 0, bytesRead);
			}finally{
				if (inputStream != null){
					inputStream.close();
				}
			}
		}
	}
}