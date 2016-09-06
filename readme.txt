HUFFMAN CODING

Implementation of a fully functional Huffman coding processor equipped with methods to compress 
and decompress files. The Huffman compression algorithm works by reformatting data to use fewer
bits. By default, text files use the 8-bit (or byte) codes specified by ASCII (American Standard 
Code for Information Interchange). While this may not seem like such a waste of space, 8 bits 
allows for 256 distinct characters to be expressed. This is far more an most text files use. 
Thus, in many cases, by ignoring the characters that don't actually occur in the file and reducing
the number of unique codes needed, shorter bit codes can be conceived. 

For example, consider the file with only four unique characters, the characters could be uniquely 
identified with only 2 bits apiece as opposed to 8 using 00, 01, 10, and 11 to represent each one. 
A naive approach may do something like this and just trim the size of the character library to be 
only 128 characters or maybe even just 64, saving 1 bit and 2 bits per character respectively. 
Although mildly effective in some cases, more complicated algorithms can capitalize on the 
statistical data of character distributions to extend compressibility even further. Huffman is 
one of these entropy encoding algorithms.

ork mainly by utilizing variable length codes combined with character frequencies. Characters that 
occur a lot get shorter codes while lesser used characters get longer codes. In some cases, longer 
codes may even exceed the 8-bit length of the standard ASCII codes. While this may seem 
counter-intuitive, it actually allows for more bits to be saved on higher frequency characters, 
making Huffman coding an efficient method for compression. 