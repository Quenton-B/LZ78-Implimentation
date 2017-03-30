import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class decoder {
	private ArrayList<decodeNode> decodeTable = new ArrayList<decodeNode>();
	private List<Byte> toWriteBuffer = new ArrayList<Byte>();
	private BufferedOutputStream  outputWriter;
	public static void main(String[] args){
		String filename = argCheck(args);
		decoder de = new decoder(filename);
	}
	public decoder(String filename) {
		if (filename != null){
			try {
		        File file = new File("./" + filename + ".out");
		        if (!file.exists()) {
		            file.createNewFile();
		        }
		        FileOutputStream FileOutStream = new FileOutputStream(file);
		        outputWriter = new BufferedOutputStream(FileOutStream);
			} catch (Exception e) {
				return;
			}
			this.createNodeTable(filename);
			try {
		        this.outputWriter.close();
			} catch (Exception e){
				return;
			}
		}
	}
	protected void createNodeTable(String filename) {
		this.decodeTable.add(new decodeNode (0, (byte) 13)); // reset
		this.decodeTable.add(new decodeNode (0, (byte) 0)); // null entry
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;
		    while ((line = br.readLine()) != null) { // goes through ever line
		    	String[] splitLine = line.split(",");
		    	int pointerInt = Integer.parseInt(splitLine[0]);
		    	byte missmatch = (byte) Integer.parseInt(splitLine[1]);
		    	decodeNode d = new decodeNode( pointerInt , missmatch);
	    		this.decodeTable.add(d);
	    		d.writeGroup();
		    }
		}
		catch (Exception e) {
			return;
		}
		this.saveData();
	}

	protected void saveData(){ // writes everything in toWriteBuffer onto the end of outputFile
		try{
			for (int i = 0; i < this.toWriteBuffer.size(); i++){
				outputWriter.write(this.toWriteBuffer.get(i));
			}
			outputWriter.flush();
		} catch (Exception e) {
			return;
		}

	}
	
	private class decodeNode{
		private int pointerInt;
		private byte mismatch;
		public decodeNode(int pointerInt, byte mismatch){
			this.pointerInt = pointerInt;
			this.mismatch = mismatch;
		}
		public int getPointer(){
			return this.pointerInt;
		}
		public byte getMismatch(){
			return this.mismatch;
		}
		public void writeGroup(){ // recursively search the table to prior entries
			if (this.pointerInt != 0){
				decodeTable.get(pointerInt).writeGroup();
				toWriteBuffer.add(this.mismatch);
			}
		}
	}
	
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