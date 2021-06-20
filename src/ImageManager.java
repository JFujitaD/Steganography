import java.util.*;
import java.io.*;

public class ImageManager {
	private final int HEADER_SIZE = 8;
	
	private File path;
	private InputStream iStream;
	
	private ImageInformation imageInformation;
	private byte[] header = new byte[HEADER_SIZE];
	private List<Chunk> chunks = new ArrayList<>();
	
	public ImageManager(String fileName) {
		path = new File(fileName);
		
		try {
			iStream = new FileInputStream(path);
			
			// Header
			header = iStream.readNBytes(HEADER_SIZE);
			
			// Chunks
			Chunk chunk;
			while(true) {
				chunk = extractChunk(iStream);
				chunks.add(chunk);
				
				if(chunk.isIHDR())
					imageInformation = new ImageInformation(chunk.getData());
				if(chunk.isIEND())
					break;
			}
			
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
	
	public void injectMessage(String message) {
		Chunk idatChunk = null;
		byte[] pixels = null;
		
		for(Chunk c : chunks) {
			if(c.isIDAT()) {
				idatChunk = c;
				pixels = c.getData();
			}
		}
		
		Random r = new Random();
		for(int i = 0; i < pixels.length; i++) {
			if(r.nextBoolean())
				pixels[i] = (byte) 0b01000101;
		}
		
		idatChunk.setData(pixels);
	}
	
	public void reconstructImage(String imageName) {
		byte[] newImage;
		int totalBytes = 0;
		
		// Calculate total bytes
		totalBytes += header.length;
		for(Chunk c : chunks) {
			totalBytes += c.getBytes().length;
		}
		
		// Fill bytes in the correct order
		newImage = new byte[totalBytes];
		int pointer = 0;
		
		// Header
		for(int i = pointer; i < HEADER_SIZE; i++) {
			newImage[i] = header[i];
			pointer++;
		}
		// Chunks
		for(Chunk c : chunks) {
			for(byte b : c.getBytes()) {
				newImage[pointer] = b;
				pointer++;
			}
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
		
	public void printChunks() {
		for(Chunk c : chunks)
			System.out.println(c);
	}
	
	public void printImagePixelData() {
		StringBuilder sb = new StringBuilder("-Image Pixel Data-\n");
		Chunk idatChunk = null;
		for(Chunk c : chunks) {
			if(c.isIDAT()) {
				idatChunk = c;
				break;
			}	
		}
		
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
