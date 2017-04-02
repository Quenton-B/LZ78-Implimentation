
import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class BitPacker {
	
	public static void main(String args[]) throws IOException {
			
		
		String[] split;
		String phraseNo;
		String missMatch;
		String stream ="";
		String zero = "0";
		Integer phraseSize =  1;
		BigInteger big;
		Scanner sc = new Scanner(System.in);
      		System.out.println("Bit packing starting....");
		File fout = new File(sc.nextLine() + ".lz78");
		FileOutputStream fos = new FileOutputStream(fout);
		for(int i = 1; sc.hasNextLine(); i++)
		{
			
			split =  sc.nextLine().split(",");
			//getting the phrase number and checking how many bits its going to take to represent
			big = new BigInteger(split[0]);			
			phraseNo = big.toString(2);
			//while loop to buffer the phrase when needed.
			while(phraseNo.length() != (Integer.toBinaryString(phraseSize).length())) 
			{		
				phraseNo = zero.concat(phraseNo);				
			}
			big = new BigInteger(split[1]);
			missMatch = big.toString(2);
			if (Integer.parseInt(missMatch) < 0)
			{
				missMatch = Integer.toBinaryString(Integer.parseInt(split[1]));
				missMatch = missMatch.substring(missMatch.length() -8);

			}
			//while loop to buffer the miss match to a byte.
			while(missMatch.length() != 8)
			{
				missMatch = zero.concat(missMatch);
				
			}
			if(i == 0)
			{
				stream = phraseNo.concat(missMatch);
			}
			else
			{
				String temp = phraseNo.concat(missMatch);
				stream = stream.concat(temp);
			}
			//while loop to send data
			while(stream.length() >= 8)
			{
			//grabbing first 8 bits of stream and sending it 
			String out = stream.substring(0,8);
			byte b = (byte) Integer.parseInt(out, 2);
			byte[] bArray = new byte[1];
			bArray[0] = b;
			fos.write(bArray, 0, 1);
			//deleting first 8 bits of stream
			stream = stream.substring(8);
			}
			//this will detect if there has been a reset sent, if so set bits to 1
			if (Integer.parseInt(phraseNo, 2) == 0 && Integer.parseInt(missMatch, 2) == 0)
			{				
				phraseSize =  0;
			}
			phraseSize++;

		}
		int index = 0;
		while(index < (Integer.toBinaryString(phraseSize).length())) 
		{
			
			stream = stream.concat(zero);	
			index++;
		}
		while(stream.length() >= 8)
		{
		//grabbing first 8 bits of stream and sending it 
		String out = stream.substring(0,8);
		byte b = (byte) Integer.parseInt(out, 2);
		byte[] bArray = new byte[1];
		bArray[0] = b;
		fos.write(bArray, 0, 1);
		//deleting first 8 bits of stream
		stream = stream.substring(8);
		}
		while(stream.length() != 8)
		{
			stream = stream.concat("0");
			
		}
		//printing out whatever is left in the stream
		byte b = (byte) Integer.parseInt(stream, 2);
		byte[] bArray = new byte[1];
		bArray[0] = b;
		fos.write(bArray, 0, 1);
		System.out.println("Finished!");
		fos.close();
	
	}
	
}
