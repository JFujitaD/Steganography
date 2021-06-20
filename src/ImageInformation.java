
public class ImageInformation {
	private int width = 0;
	private int height = 0;
	private int bitDepth = 0;
	private int colorType = 0;
	private int compressionMethod = 0;
	private int filterMethod = 0;
	private int interlaceMethod = 0;
	
	public ImageInformation(byte[] bytes) {		
		int d1 = (bytes[0] & 0xff) << 24;
		int d2 = (bytes[1] & 0xff) << 16;
		int d3 = (bytes[2] & 0xff) << 8;
		int d4 = (bytes[3] & 0xff);
		
		width = d1 | d2 | d3 | d4;
		
		d1 = (bytes[4] & 0xff) << 24;
		d2 = (bytes[5] & 0xff) << 16;
		d3 = (bytes[6] & 0xff) << 8;
		d4 = (bytes[7] & 0xff);
		
		height = d1 | d2 | d3 | d4;
		
		bitDepth = bytes[8] & 0xff;
		
		colorType = bytes[9] & 0xff;
		
		compressionMethod = bytes[10] & 0xff;
		
		filterMethod = bytes[11] & 0xff;
		
		interlaceMethod = bytes[12] & 0xff;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("-Image Information-");
		sb.append("\nWidth: " + width);
		sb.append("\nHeight: " + height);
		sb.append("\nBit Depth: " + bitDepth);
		sb.append("\nColor Type: " + colorType);
		sb.append("\nCompression Method: " + compressionMethod);
		sb.append("\nFilter Method: " + filterMethod);
		sb.append("\nInterlace Method: " + interlaceMethod);
		
		return sb.toString();
	}
}
