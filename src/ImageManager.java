import java.io.*;

public class ImageManager {
	private File path;
	private InputStream iStream;
	private byte[] bytes;
	private byte[] header = new byte[8];
	
	public ImageManager(String fileName) {
		path = new File(fileName);
		
		try {
			iStream = new FileInputStream(path);
			bytes = iStream.readAllBytes();
			this.extractHeader();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void extractHeader() {
		for(int i = 0; i < 8; i++) {
			this.header[i] = this.bytes[i];
		}
	}
	
	public void printHeader() {
		System.out.println("-Image Header-");
		
		for(byte b : header) {
			int lastEight = b & 0xff;
			System.out.print(lastEight + " ");
		}
	}
}
