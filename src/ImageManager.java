import java.io.*;

public class ImageManager {
	private final int HEADER_SIZE = 8;
	private final int IHDR_SIZE = 25;
	
	private File path;
	private InputStream iStream;
	private ImageInformation imageInformation;
	
	private byte[] header = new byte[HEADER_SIZE];
	private Chunk ihdrChunk;
	
	public ImageManager(String fileName) {
		path = new File(fileName);
		
		try {
			iStream = new FileInputStream(path);
			
			// Header
			header = iStream.readNBytes(HEADER_SIZE);
			
			// IHDR
			byte[] ihdrBytes = iStream.readNBytes(IHDR_SIZE);
			ihdrChunk = new Chunk(ihdrBytes);
			
			// Image Information
			imageInformation = new ImageInformation(ihdrChunk.getData());
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printImageHeader() {
		StringBuilder sb = new StringBuilder("-Image Header-\n");
		
		for(byte b : header) {
			int lastEight = b & 0xff;
			sb.append(lastEight + " ");
		}
		sb.append("\n");
		
		System.out.println(sb.toString());
	}
	
	public void printImageInformation() {
		System.out.println(imageInformation + "\n");
	}
}
