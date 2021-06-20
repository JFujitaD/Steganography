
public class Steganography {

	public static void main(String[] args) {
		ImageManager im = new ImageManager("src/Images/computer.png");
		im.printImageHeader();
		im.printImageInformation();
		im.printChunks();
		
		im.printImagePixelData();
		im.reconstructImage("new-image.png");
	}

}
