import java.util.*;
import java.util.zip.CRC32;

public class Chunk {
	private byte[] length = new byte[4];
	private byte[] chunkType = new byte[4];
	private byte[] data;
	private byte[] crc32 = new byte[4];
	
	private int byteCount = 0;
	
	public Chunk(byte[] bytes) {	
		// Keep track of byteCount
		byteCount = bytes.length;
		
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
	
	public void setData(byte[] data) {
		this.data = data;
		recalculateCRC();
	}
	
	private void recalculateCRC() {
		CRC32 c = new CRC32();
		byte[] typeAndData = new byte[chunkType.length + data.length];
		int i = 0;
		
		for(byte b : chunkType) {
			typeAndData[i] = b;
			i++;
		}
		for(byte b : data) {
			typeAndData[i] = b;
			i++;
		}
		
		c.update(typeAndData);
		
		// Change long(8 bytes) into byte[4]
		long value = c.getValue();
		
		byte d1 = (byte) ((value & 0xff000000) >> 24);
		byte d2 = (byte) ((value & 0x00ff0000) >> 16);
		byte d3 = (byte) ((value & 0x0000ff00) >> 8);
		byte d4 = (byte) (value & 0x000000ff);
		
		crc32[0] = (byte) (d1 & 0xff);
		crc32[1] = (byte) (d2 & 0xff);
		crc32[2] = (byte) (d3 & 0xff);
		crc32[3] = (byte) (d4 & 0xff);
	}
	
	public byte[] getLengthData() {
		return length;
	}
	
	public byte[] getBytes() {
		byte[] bytes = new byte[byteCount];
		
		// Length is first 4 bytes
		for(int i = 0; i < 4; i++) {
			bytes[i] = length[i];
		}
		
		// Chunk type is next 4 bytes
		for(int i = 4; i < 8; i++) {
			bytes[i] = chunkType[i - 4];
		}
		
		// Data is "length" bytes
		int chunkLength = Chunk.getLengthFromBytes(length) + 8;
		
		for(int i = 8; i < chunkLength; i++) {
			bytes[i] = data[i - 8];
		}

		// CRC-32 is the last 4 bytes
		int endIndex = bytes.length;
		for(int i = endIndex - 4; i < endIndex; i++) {
			bytes[i] = crc32[i - endIndex + 4];
		}
		
		return bytes;
	}
	
	public boolean isIHDR() {
		return isCorrectChunk(ChunkType.IHDR);
	}
	
	public boolean isIDAT() {
		return isCorrectChunk(ChunkType.IDAT);
	}
	
	public boolean isIEND() {
		return isCorrectChunk(ChunkType.IEND);
	}
	
	private boolean isCorrectChunk(ChunkType type) {
		StringBuilder sb = new StringBuilder();
		
		for(byte b : chunkType) {
			int lastEight = b & 0xff;
			sb.append((char)lastEight);
		}
		
		if(sb.toString().equals(type.toString()))
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("-Chunk-");
		
		sb.append("\nLength: " + Chunk.getLengthFromBytes(length));
		
		sb.append("\nChunk Type: ");
		for(byte b : chunkType) {
			int lastEight = b & 0xff;
			sb.append((char)lastEight);
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
