
public class Chunk {
	private int length;
	private int chunkType;
	private byte[] data;
	private int crc32;
	
	public Chunk(byte[] bytes) {
		// Length is first 4 bytes
		int d1 = (bytes[0] & 0xff) << 24;
		int d2 = (bytes[1] & 0xff) << 16;
		int d3 = (bytes[2] & 0xff) << 8;
		int d4 = (bytes[3] & 0xff);

		length = d1 | d2 | d3 | d4;
		
		// Chunk type is next 4 bytes
		d1 = (bytes[4] & 0xff) << 24;
		d2 = (bytes[5] & 0xff) << 16;
		d3 = (bytes[6] & 0xff) << 8;
		d4 = (bytes[7] & 0xff);

		chunkType = d1 | d2 | d3 | d4;
		
		// Data is "length" bytes
		int dataStartIndex = 8;
		data = new byte[length];
		
		for(int i = dataStartIndex; i < dataStartIndex + length; i++) {
			data[i - dataStartIndex] = bytes[i];
		}

		// CRC-32 is the last 4 bytes
		int endIndex = bytes.length - 1;
		d1 = (bytes[endIndex - 3] & 0xff) << 24;
		d2 = (bytes[endIndex - 2] & 0xff) << 16;
		d3 = (bytes[endIndex - 1] & 0xff) << 8;
		d4 = (bytes[endIndex] & 0xff);

		crc32 = d1 | d2 | d3 | d4;
	}
	
	public static int getLengthFromBytes(byte[] lengthBytes) {
		int d1 = (lengthBytes[0] & 0xff) << 24;
		int d2 = (lengthBytes[1] & 0xff) << 16;
		int d3 = (lengthBytes[2] & 0xff) << 8;
		int d4 = (lengthBytes[3] & 0xff);

		return (d1 | d2 | d3 | d4) + 8;  
	}
	
	public byte[] getData() {
		return data;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("-Chunk-");
		
		sb.append("\nLength: " + length);
		sb.append("\nChunk Type: 0x" + Integer.toHexString(chunkType));
		sb.append("\nCRC32: 0x" + Integer.toHexString(crc32));
		sb.append("\n");
		
		return sb.toString();
	}
}
