import java.util.*;
import java.io.*;

// IHDR: 49 48 44 52
// IDAT: 49 44 41 54
// IEND: 49 45 4E 44

public class ImageManager {
	private final int HEADER_SIZE = 8;
	
	private File path;
	private InputStream iStream;
	
	private ImageInformation imageInformation;
	private byte[] header = new byte[HEADER_SIZE];
	private Chunk ihdrChunk;
	private List<Chunk> miscChunks = new ArrayList<>();
	private Chunk idatChunk;
	private Chunk iendChunk;
	
	public ImageManager(String fileName) {
		path = new File(fileName);
		
		try {
			iStream = new FileInputStream(path);
			
			// Header
			header = iStream.readNBytes(HEADER_SIZE);
			
			// IHDR Chunk
			ihdrChunk = extractChunk(iStream);
			
			// Image Information
			imageInformation = new ImageInformation(ihdrChunk.getData());
			
			// Intermediate Chunks
			for(int i = 0; i < 4; i++) {
				miscChunks.add(extractChunk(iStream));
			}
			
			// IDAT Chunk
			idatChunk = extractChunk(iStream);
			
			// IDEND Chunk
			iendChunk = extractChunk(iStream);
			
			iStream.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Chunk extractChunk(InputStream iStream) {
		try {
			byte[] lengthBytes = iStream.readNBytes(4);
			int lengthInt = Chunk.getLengthFromBytes(lengthBytes) + 8;
			byte[] remainingBytes = iStream.readNBytes(lengthInt);
			
			byte[] complete = new byte[4 + remainingBytes.length];
			
			int i = 0;
			for(byte b : lengthBytes) {
				complete[i] = b;
				i++;
			}
			for(byte b : remainingBytes) {
				complete[i] = b;
				i++;
			}
			
			return new Chunk(complete);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void reconstructImage(String imageName) {
		byte[] newImage;
		int totalBytes = 0;
		
		// Calculate total bytes
		totalBytes += header.length;
		totalBytes += ihdrChunk.getBytes().length;
		for(Chunk c : miscChunks) {
			totalBytes += c.getBytes().length;
		}
		totalBytes += idatChunk.getBytes().length;
		totalBytes += iendChunk.getBytes().length;
		
		// Fill bytes in the correct order
		newImage = new byte[totalBytes];
		int pointer = 0;
		
		// Header
		for(int i = pointer; i < HEADER_SIZE; i++) {
			newImage[i] = header[i];
			pointer++;
		}
		// IHDR Chunk
		for(byte b : ihdrChunk.getBytes()) {
			newImage[pointer] = b;
			pointer++;
		}
		// Misc Chunks
		for(Chunk c : miscChunks) {
			for(byte b : c.getBytes()) {
				newImage[pointer] = b;
				pointer++;
			}
		}
		// IDAT Chunk
		for(byte b : idatChunk.getBytes()) {
			newImage[pointer] = b;
			pointer++;
		}
		// IEND Chunk
		for(byte b : iendChunk.getBytes()) {
			newImage[pointer] = b;
			pointer++;
		}
		
		// Save image
		File path = new File("src/Images/" + imageName);
		
		try {
			OutputStream oStream = new FileOutputStream(path);
			oStream.write(newImage);
			oStream.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void printIHDRChunk() {
		System.out.println(ihdrChunk);
	}
	
	public void printIDATChunk() {
		System.out.println(idatChunk);
	}
	
	public void printIENDChunk() {
		System.out.println(iendChunk);
	}
	
	public void printMiscChunks() {
		for(Chunk c : miscChunks)
			System.out.println(c);
	}
	
	public void printImagePixelData() {
		StringBuilder sb = new StringBuilder("-Image Pixel Data-\n");
		byte[] data = idatChunk.getData();
		
		for(byte d : data) {
			int lastEight = d & 0xff;
			sb.append("0x" + Integer.toHexString(lastEight) + "\n");
		}

		System.out.println(sb.toString());
	}
	
	public void printImageHeader() {
		StringBuilder sb = new StringBuilder("-Image Header-\n");
		
		for(byte b : header) {
			int lastEight = b & 0xff;
			sb.append("0x" + Integer.toHexString(lastEight) + " ");
		}
		sb.append("\n");
		
		System.out.println(sb.toString());
	}
	
	public void printImageInformation() {
		System.out.println(imageInformation + "\n");
	}
}
