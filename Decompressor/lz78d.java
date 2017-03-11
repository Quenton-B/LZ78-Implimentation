import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lz78d {

	public static void main(String[] args){
		String filename = argCheck(args);
		if (filename != null){
			System.out.println(filename);
		}
	}

	/* 
		Validate input is in proper form and checks file exists.
		Returns file name on success, null on failure
	*/
	protected static String argCheck(String[] args){
		// exactly 1 arg
		if (args.length != 1){ 
			System.out.println("Please enter the name of one file you would like to decompress");
			return null;
		}
		// check file type is correct *.lz78
		boolean filecheck = Pattern.matches(".+.lz78", args[0]);
		if (filecheck){
			File f = new File(args[0]);
			if(f.exists() && !f.isDirectory()) { 
				return args[0];
			}
			else{
				System.out.println("Could not find! Please enter the absolute path to file.");
				return null;
			}
		}
		else {
			System.out.println("Please enter a .lz78 file");
			return null;
		}
	}
}