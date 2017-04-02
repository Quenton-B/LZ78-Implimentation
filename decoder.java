import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedOutputStream;
import java.util.*;

public class decoder {
	private ArrayList<decodeNode> decodeTable;
	private List<Byte> toWriteBuffer = new ArrayList<Byte>();
	private BufferedOutputStream  outputWriter;
	private Scanner sc;
	public static void main(String[] args){
		decoder de = new decoder();
	}
	public decoder() { // main loop opens file to write to and calls doDecode to start decoding pairs
		this.sc = new Scanner(System.in);
		String filename = sc.nextLine();
		try {
			File file = new File("./" + filename.replace(".lz78", ""));
			if (!file.exists()) {
		            file.createNewFile();
		        }
		        FileOutputStream FileOutStream = new FileOutputStream(file);
		        outputWriter = new BufferedOutputStream(FileOutStream);
		} catch (Exception e) {
			return;
		}
		this.doDecode(filename);
		try {
			this.outputWriter.close();
		} catch (Exception e){
			return;
		}

	}
	protected void doDecode(String filename) { // creates the decode table and writes all data taken from this table
		this.createDecodeTable();
		String line;
		while (sc.hasNextLine()) { // goes through ever line
			String[] splitLine = sc.nextLine().split(",");
		    	int pointerInt = Integer.parseInt(splitLine[0]);
		    	byte missmatch = (byte) Integer.parseInt(splitLine[1]);
		    	if (pointerInt == 0){ // reset entry
		    		this.createDecodeTable();
		    	}
		    	else{
			    	decodeNode d = new decodeNode( pointerInt , missmatch);
		    		this.decodeTable.add(d);
		    		d.writeGroup();
		    	}
		}
		this.saveData();
	}
	
	protected void createDecodeTable (){ // init the decode table
		this.decodeTable = new ArrayList<decodeNode>();
		this.decodeTable.add(new decodeNode (0, (byte) 13)); // reset
		this.decodeTable.add(new decodeNode (0, (byte) 0)); // null entry
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
}
