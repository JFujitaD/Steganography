import java.io.*;

// IHDR: 49 48 44 52
// IDAT: 49 44 41 54
// IEND: 49 45 4E 44

public class ImageManager {
	private final int HEADER_SIZE = 8;
	private final int IHDR_SIZE = 25;
	
	private File path;
	private InputStream iStream;
	
	private ImageInformation imageInformation;
	private byte[] header = new byte[HEADER_SIZE];
	private Chunk ihdrChunk;
	private Chunk[] miscChunks;
	private Chunk idatChunk;
	private Chunk iendChunk;
	
	public ImageManager(String fileName) {
		path = new File(fileName);
		
		try {
			iStream = new FileInputStream(path);
			
			// Header
			header = iStream.readNBytes(HEADER_SIZE);
			
			// IHDR Chunk
			byte[] ihdrBytes = iStream.readNBytes(IHDR_SIZE);
			ihdrChunk = new Chunk(ihdrBytes);
			
			// Image Information
			imageInformation = new ImageInformation(ihdrChunk.getData());
			
			// Intermediate Chunks
			for(int i = 0; i < 4; i++) {
				byte[] chunkLengthBytes = iStream.readNBytes(4);
				int chunkLengthInt = Chunk.getLengthFromBytes(chunkLengthBytes);
				iStream.readNBytes(chunkLengthInt);
			}
			
			// IDAT Chunk
			byte[] idatLengthBytes = iStream.readNBytes(4);
			int idatLengthInt = Chunk.getLengthFromBytes(idatLengthBytes);
			byte[] remainingIdatBytes = iStream.readNBytes(idatLengthInt);
			
			byte[] idatComplete = new byte[4 + remainingIdatBytes.length];
			
			int i = 0;
			for(byte b : idatLengthBytes) {
				idatComplete[i] = b;
				i++;
			}
			for(byte b : remainingIdatBytes) {
				idatComplete[i] = b;
				i++;
			}
			
			idatChunk = new Chunk(idatComplete);	
			
			// IDEND Chunk
			byte[] iendLengthBytes = iStream.readNBytes(4);
			int iendLengthInt = Chunk.getLengthFromBytes(iendLengthBytes);
			byte[] remainingIendBytes = iStream.readNBytes(iendLengthInt);
			
			byte[] iendComplete = new byte[4 + remainingIendBytes.length];
			
			i = 0;
			for(byte b : iendLengthBytes) {
				iendComplete[i] = b;
				i++;
			}
			for(byte b : remainingIendBytes) {
				iendComplete[i] = b;
				i++;
			}
			
			iendChunk = new Chunk(iendComplete);	
		} 
		catch (Exception e) {
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
