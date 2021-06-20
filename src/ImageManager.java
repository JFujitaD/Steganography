import java.io.*;

public class ImageManager {
	private final int HEADER_SIZE = 8;
	private final int IHDR_SIZE = 13;
	
	private File path;
	private InputStream iStream;
	private int width = 0;
	private int height = 0;
	private int bitDepth = 0;
	private int colorType = 0;
	private int compressionMethod = 0;
	private int filterMethod = 0;
	private int interlaceMethod = 0;
	
	private byte[] header = new byte[HEADER_SIZE];
	private byte[] ihdr = new byte[IHDR_SIZE];
	
	public ImageManager(String fileName) {
		path = new File(fileName);
		
		try {
			iStream = new FileInputStream(path);
			
			header = iStream.readNBytes(HEADER_SIZE);
			iStream.readNBytes(8);	
			ihdr = iStream.readNBytes(IHDR_SIZE);
			
			extractDimensions();
			extractBiDepth();
			extractColorType();
			extractCompressionMethod();
			extractFilterMethod();
			extractInterlaceMethod();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void extractDimensions() {
		int d1 = (ihdr[0] & 0xff) << 24;
		int d2 = (ihdr[1] & 0xff) << 16;
		int d3 = (ihdr[2] & 0xff) << 8;
		int d4 = (ihdr[3] & 0xff);
		
		width = d1 | d2 | d3 | d4;
		
		d1 = (ihdr[4] & 0xff) << 24;
		d2 = (ihdr[5] & 0xff) << 16;
		d3 = (ihdr[6] & 0xff) << 8;
		d4 = (ihdr[7] & 0xff);
		
		height = d1 | d2 | d3 | d4;
	}
	
	private void extractBiDepth() {
		bitDepth = ihdr[8] & 0xff;
		
	}
	
	private void extractColorType() {
		colorType = ihdr[9] & 0xff;
	}
	
	private void extractCompressionMethod() {
		compressionMethod = ihdr[10] & 0xff;
	}
	
	private void extractFilterMethod() {
		filterMethod = ihdr[11] & 0xff;
	}
	
	private void extractInterlaceMethod() {
		interlaceMethod = ihdr[12] & 0xff;
	}
	
	public void printImageData() {
		System.out.println("-Image Data-");
		
		System.out.print("Header: ");
		for(byte b : header) {
			int lastEight = b & 0xff;
			System.out.print(lastEight + " ");
		}
		System.out.println();
		
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);
		
		System.out.println("Bit Depth: " + bitDepth);
		System.out.println("Color Type: " + colorType);
		System.out.println("Compression Method: " + compressionMethod);
		System.out.println("Filter Method: " + filterMethod);
		System.out.println("Interlace Method: " + interlaceMethod);
	}
}
