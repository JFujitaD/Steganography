
public class Chunk {
	private byte[] length = new byte[4];
	private byte[] chunkType = new byte[4];
	private byte[] data;
	private byte[] crc32 = new byte[4];
	
	public Chunk(byte[] bytes) {
		// Length is first 4 bytes
		for(int i = 0; i < 4; i++) {
			length[i] = bytes[i];
		}
		
		// Chunk type is next 4 bytes
		for(int i = 4; i < 8; i++) {
			chunkType[i - 4] = bytes[i];
		}
		
		// Data is "length" bytes
		int chunkLength = Chunk.getLengthFromBytes(length) + 8;
		data = new byte[chunkLength];
		
		for(int i = 8; i < chunkLength; i++) {
			data[i - 8] = bytes[i];
		}

		// CRC-32 is the last 4 bytes
		int endIndex = bytes.length;
		for(int i = endIndex - 4; i < endIndex; i++) {
			crc32[i - endIndex + 4] = bytes[i];
		}
	}
	
	public static int getLengthFromBytes(byte[] lengthBytes) {
		int d1 = (lengthBytes[0] & 0xff) << 24;
		int d2 = (lengthBytes[1] & 0xff) << 16;
		int d3 = (lengthBytes[2] & 0xff) << 8;
		int d4 = (lengthBytes[3] & 0xff);

		return (d1 | d2 | d3 | d4);  
	}
	
	public byte[] getData() {
		return data;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("-Chunk-");
		
		sb.append("\nLength: " + Chunk.getLengthFromBytes(length));
		
		sb.append("\nChunk Type:");
		for(byte b : chunkType) {
			int lastEight = b & 0xff;
			sb.append(" 0x" + Integer.toHexString(lastEight));
		}
		
		sb.append("\nCRC32:");
		for(byte b : crc32) {
			int lastEight = b & 0xff;
			sb.append(" 0x" + Integer.toHexString(lastEight));
		}
		sb.append("\n");
		
		return sb.toString();
	}
}
