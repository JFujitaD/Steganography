
public class Steganography {

	public static void main(String[] args) {
		ImageManager im = new ImageManager("src/Images/3x3.png");
		im.printImageHeader();
		im.printImageInformation();
		im.printIHDRChunk();
		im.printMiscChunks();
		im.printIDATChunk();
		im.printIENDChunk();
		
		im.printImagePixelData();
		im.reconstructImage("new-image.png");
	}

}
