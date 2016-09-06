/**                                                                                                                                      
 * 
 * Creates Huffman Tree given file using the processor
 * interface. Extracts code for each char using configured
 * tree. Compresses and decompresses file using Huffman tree.
 *                                                                  
 */

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class HuffProcessor implements Processor {
	String[] charCodes = new String[ALPH_SIZE + 1]; //Array that holds the HuffCode for each char
	
	@Override
	public void compress(BitInputStream in, BitOutputStream out) { //compresses BitInputStream and writes to output file using BitOutputStream
		//count characters in file, each index in array represents character
		int[] counter = new int[ALPH_SIZE];
		int ch;
		HashSet<Integer> uniqueChars = new HashSet<Integer>();
		while ((ch = in.readBits(BITS_PER_WORD)) != -1){
			counter[ch]++;
			uniqueChars.add(ch);
		}
		in.reset();
		//create huffman tree using HuffNodes
		PriorityQueue<HuffNode> characters = new PriorityQueue<HuffNode>(new HuffComparator());
		for (int i = 0; i < 256; i++){
			if (counter[i] != 0){
				characters.add(new HuffNode(i, counter[i]));
			}
		}
		characters.add(new HuffNode(PSEUDO_EOF, 0));
		while (characters.size() > 1){
			HuffNode small1 = characters.poll();
			HuffNode small2 = characters.poll();
			characters.add(new HuffNode(-1, small1.weight() + small2.weight(), small1, small2));
		}
		HuffNode current = characters.peek();
		extractCodes(current, "");
		out.writeBits(BITS_PER_INT, HUFF_NUMBER);
		writeHeader(current, out);
		while ((ch = in.readBits(BITS_PER_WORD)) != -1){
			String code = charCodes[ch];
			out.writeBits(code.length(), Integer.parseInt(code, 2));
		}
		String code = charCodes[PSEUDO_EOF];
		out.writeBits(code.length(), Integer.parseInt(code, 2));
	}
	private void extractCodes(HuffNode current, String path){ //assigns Huffman code for each character represented by the HuffNodes in the tree
		if (current.right() == null && current.left() == null){
			charCodes[current.value()] = path;
			return;
		}
		else{
			extractCodes(current.left(), path + "0");
			extractCodes(current.right(), path + "1");
		}
		
	}
	private void writeHeader(HuffNode current, BitOutputStream out){ //writes HuffNumber
		if (current.right() == null && current.right() == null){
			out.writeBits(1, 1);
			out.writeBits(9, current.value());
		}
		else{
			out.writeBits(1, 0);
			writeHeader(current.left(), out);
			writeHeader(current.right(), out);
		}
	}
	@Override
	public void decompress(BitInputStream in, BitOutputStream out) { //decompresses BitInputStream using Huffman Tree and writes to output file using BitOutputStream
		//check for huff number
		if(in.readBits(BITS_PER_INT) != HUFF_NUMBER){
			throw new HuffException("no huff number");
		}
		HuffNode root = readHeader(in);
		HuffNode current = root;
		int num;
		while ((num = in.readBits(1)) != -1){
			if (num == 1){
					current = current.right();
			}
			else{
				current = current.left();
			}
			if (current.right() == null && current.right() == null){
				if (current.value() == PSEUDO_EOF){
					return;
				}
				out.writeBits(BITS_PER_WORD, current.value());
				current = root;
			}
		}
		throw new HuffException("Problem with PSEUDO_EOF");
	}
	private HuffNode readHeader(BitInputStream in){ //reads HuffNumber
		if (in.readBits(1) == 0){
			HuffNode left = readHeader(in);
			HuffNode right = readHeader(in);
			return new HuffNode(-1, 0, left, right);
		}
		else{
			return new HuffNode(in.readBits(BITS_PER_WORD + 1), 0);
		}
	}
	public class HuffComparator implements Comparator<HuffNode>{
		@Override
		public int compare(HuffNode o1, HuffNode o2) {
			return o1.weight() - o2.weight();
		}	
	}
}
