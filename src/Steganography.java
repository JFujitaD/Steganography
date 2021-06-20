
public class Steganography {

	public static void main(String[] args) {
		ImageManager im = new ImageManager("src/3x3.png");
		im.printImageHeader();
		im.printImageInformation();
		im.printIHDRChunk();
		im.printIDATChunk();
		im.printIENDChunk();
		
		//im.printImagePixelData();
	}

}
