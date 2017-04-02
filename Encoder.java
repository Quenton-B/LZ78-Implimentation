import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;



public class Encoder {

	
	public static void main(String args[]) throws IOException {
		
		//taking input from file
		Trie trie = new Trie();
		long rcntPhrase = 1;
		int maxPhrase = 65535;
		long curPhrase = 2;
		byte missMatch;
		Node head = trie.getHead();
		Node curr;
		LinkedList<Node> nodes = new LinkedList<Node>();
		LinkedList<Long> currPhrases = new LinkedList<Long>();
		//if to check how many arguments have been passed into the program
		if(args.length >= 1 && args.length < 3)
		{
			
			//checking that the file does exist
			File f = new File(args[0]);
			if(f.isFile() && f.canRead())
			{
				//sending the file name to the bit packer
				System.out.println(args[0]);
				Path path = Paths.get(args[0]);
				byte[] data = Files.readAllBytes(path);
				
				//setting the max size for phrases if its specified
				if(args.length == 2)
				{
					//checking if the number given is bigger or equal to 1
					if(Integer.parseInt(args[1]) >= 1)
					{
						String temp = "";
						while(temp.length() != Integer.parseInt(args[1]))
						{
							temp = temp.concat("1");					
						}
						maxPhrase= Integer.parseInt(temp, 2);
					}
					else
					{
						System.out.println("Please enter a non zero positive integer");
						System.exit(0);
					}
				}
				//for loop to start reading the file into the dictionary
				for(int i = 0; i < data.length; i++)
				{
					//if to check w
					if(curPhrase != maxPhrase)
					{
						head = trie.getHead();
						rcntPhrase = 1;
						curr = head;
						missMatch = data[i];
						//if to check if the dictionary is empty
						if(head.getNodes().isEmpty())
						{
							curr.addLeaf(2, missMatch);		
							curPhrase++;
							System.out.println(rcntPhrase+ "," + missMatch );
						}
						else
						{
						nodes = curr.getNodes();
						currPhrases = curr.getPhrase();
						
							for(int k = 0; k < nodes.size(); k++)
							{
								
								//if to check if the value of the miss match matches anything in the curr list of nodes
								if(0 == (Byte.valueOf(missMatch).compareTo(Byte.valueOf(nodes.get(k).getData()))))
								{
									//System.out.println(currPhrases.get(k).toString());
									i++;
									if(i == data.length)
									{
										break;
									}
									missMatch = data[i];
									rcntPhrase = (long) curr.getPhrase().get(k);
									curr = nodes.get(k);
									nodes = curr.getNodes();
									k = -1;
					
								}
				
							}
							//adding the path to the trie, incrementing the cur phrase counter.
							curr.addLeaf(curPhrase, missMatch);
							curPhrase++;
							//still has to send data into the dictionary list
							System.out.println(rcntPhrase+ "," + missMatch );
						}
					}
					else
					{
						trie = new Trie();
						curPhrase = 2;
						System.out.println(0+ "," + 0 );
						i--;
					}
				}				
				
			}
			else
			{
				System.out.println("file does not exist.");
				System.exit(0);
			}					
		}
		else
		{
			System.out.println("Program arguments consist of path to file and if stated maximum for phrase numbers.");
			System.exit(0);
		}			 	
	}

}

 class Node {
	
	LinkedList<Long> phraseNo;
	LinkedList<Node> nextDatum;
	Byte data;
	
	public Node()
	{
		phraseNo = new LinkedList<Long>();
		nextDatum = new LinkedList<Node>();
		data = null;
	}
	
	public Node(Byte b)
	{
		phraseNo = new LinkedList<Long>();
		nextDatum = new LinkedList<Node>();
		data = b;
	}
	
	public void addLeaf(long newPhrase, Byte newData )
	{
		phraseNo.add(newPhrase);
		Node newN = new Node(newData);
		nextDatum.add(newN);
	}
	
	public Byte getData()
	{
		return this.data;
	}
	
	public LinkedList getNodes()
	{
		return this.nextDatum;
		
	}
	
	public LinkedList getPhrase()
	{
		return this.phraseNo;
		
	}
	
}
 class Trie{
	
	long maxPhrase;
	long currPhrase;
	long lastPath;
	Node head;
	LinkedList<Node> nodes;
	
	public Trie()
	{
		lastPath = 1;
		head = new Node();
		currPhrase = 2;
		
	}
	
	public double search(Byte misMatch)
	{
		//If to check if its the first item to be added to the trie
		if (head.getNodes().isEmpty())
		{
			head.addLeaf(currPhrase, misMatch);
			currPhrase ++;
			return lastPath;
			
		}
		//for to search through the trie for the mismatched item.
		for(int i = 0; i < head.getNodes().size(); i++)
		{
			nodes = head.getNodes();
			//nodes.get(0).getNodes();
		}
		return currPhrase;
		
	}
	
	public Node getHead()
	{
		return head;
	}
}