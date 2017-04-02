import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class bitunpacker {
	byte[] data; // stores all of the data for the file unpacking in bytes
	String dataString;
	int bytecounter;
	int phraseBase;
	public bitunpacker (String filename){ // loads the file to data (byte[]) and then unpacks
		try {
			Path path = Paths.get("./" + filename);
			System.out.println(filename); // pass filename to decoder
			data = Files.readAllBytes(path);
		}
		catch (Exception e){
			System.out.println("error reading file");
			System.exit(0);
		}
		bytecounter = 0;
		this.dataString = "";
		this.unpack();
	}
	
	public static void main (String[] args){
		String filename = argCheck(args);
		bitunpacker bu = new bitunpacker(filename);
	}
	
	protected void unpack(){ // reads a each pair in bits from this.data and prints to system.out
		phraseBase = 1;
		int phraseCounter = 2;
		String bits = this.getNextBits((phraseBase) + 8); // gets the next pair
		while (bits != ""){
			if (Integer.parseInt(bits.substring(0, (phraseBase)), 2) == 0){ // handle a reset
				// reset
				phraseBase = 1;
				phraseCounter = 2;
				bits = this.getNextBits((phraseBase) + 8);
				System.out.println("0,0");
			}
			System.out.println(
					
					Integer.parseInt(bits.substring(0, (phraseBase)), 2) 
					+ ","   + (byte)Integer.parseInt(bits.substring(phraseBase , phraseBase + 8), 2));
			phraseCounter ++;
			if (phraseCounter > (Math.pow(2, (phraseBase)))){
				phraseBase ++;
			}
			bits = this.getNextBits((phraseBase) + 8);
		}
	}
	public String getNextBits (int requested){ // gets the requested amount of bits using loadMoreBits. These are placed into this.dataString these are returned so that the caller can check for end of file.
		String toReturn = "";
		if (this.dataString.length() < requested){
			if(! this.loadMoreBits(requested)){
				return "";
			}
		}
		toReturn = this.dataString.substring(0, requested);
		this.dataString = this.dataString.substring(requested);
		return (toReturn);
	}
	
	protected boolean loadMoreBits (int number){ // load the next x bits into dataString
		while (this.dataString.length() < (number + 1)){
			if (bytecounter == data.length){
				return false;
			}
			this.dataString = this.dataString + Integer.toBinaryString(data[bytecounter] & 255 | 256).substring(1);
			bytecounter ++;
		}
		return true;
	}
	
	protected static String argCheck(String[] args){ // checks that the file exists and is in the right format
		// exactly 1 arg
		if (args.length != 1){ 
			System.err.println("Please enter the name of one file you would like to decompress");
			System.exit(0);
		}
		// check file type is correct *.lz78
		boolean filecheck = Pattern.matches(".+.lz78", args[0]);
		if (filecheck){
			File f = new File(args[0]);
			if(f.exists() && !f.isDirectory()) { 
				return args[0];
			}
			else{
				System.err.println("Could not find! Please enter the absolute path to file.");
				System.exit(0);				
			}
		}
		else {
			System.err.println("Please enter a .lz78 file");
			System.exit(0);
		}
		return null;
	}
}
