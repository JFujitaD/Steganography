
public class Steganography {

	public static void main(String[] args) {
		ImageManager im = new ImageManager("src/Images/mushroom.png");
		im.printImageHeader();
		im.printImageInformation();
		im.printChunks();
		
		//im.printImagePixelData();
		im.injectMessage("Hello!");
		im.reconstructImage("new-image.png");
	}

}
