/**
 * Created by oukouhou on 10/10/15.
 */

import java.util.Arrays;

/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int W = 9;         // codeword width
    private static int L = 512;               // number of codewords, stating from 2
    private static double original = 0;        // original size
    private static double compressed = 0;      // compressed data size
    private static double oldRatio = 0;
    private static double newRatio = 0;
    private static boolean update = true;

    // Do Nothing Mode-
    public static void compress() {
        BinaryStdOut.write('n', 8); //indicator of do nothing mode
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L) {    // Add s to symbol table.
                if (code == L - 1 && W < 16){                 // when codebook is about to be filled up, increase W then update L
                    W++;
                    L *= 2;
                }
                st.put(input.substring(0, t + 1), code++);
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    // Reset Mode-
    public static void compress_r() {
        BinaryStdOut.write('r', 8);     //indicator of reset mode
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L) {    // Add s to symbol table.
                if (code == L - 1 && W < 16) {                 // when codebook is about to be filled up, increase W then update L
                    W++;
                    L *= 2;
                } if(code == L - 1 && W == 16){ // Reset Mode
                    W = 9;
                    L = 512;
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;  // R is codeword for EOF
                }
                st.put(input.substring(0, t + 1), code++);
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    // Monitor Mode-
    public static void compress_m() {
        BinaryStdOut.write('m', 8); //indicator of monitor mode
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            original += s.length()*8;    // current original data size
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            compressed += W;            // compressed data size
            newRatio = original / compressed;   // current compression ratio
            int t = s.length();
            if (t < input.length() && code < L) {    // Add s to symbol table.
                if (code == L - 1 && W < 16) {                 // when codebook is about to be filled up, increase W then update L
                    W++;
                    L *= 2;
                }
                st.put(input.substring(0, t + 1), code++);
            }if (code == 65536){        // after no more 16 bit codewords remain
                if (update){            // set old ratio
                    oldRatio = newRatio;
                    update = false;
                }if(newRatio != 0 && oldRatio / newRatio > 1.1){     // reset the dictionary
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;  // R is codeword for EOF
                    W = 9;
                    L = 512;
                    oldRatio = 0;
                    newRatio = 0;
                    update = true;
                }
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }


    public static void expand() {
        char c = BinaryStdIn.readChar(8);       //get what mode was set during compression
        if(c == 'n') {   // Do Nothing Mode expansion
            String[] st = new String[L];
            int i; // next available codeword value

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(W);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];

            while (true) {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(W);
                if (codeword == R) break;
                String s = st[codeword];
                if (i == codeword) s = val + val.charAt(0);   // special case check
                if (i < L){
                    st[i++] = val + s.charAt(0);
                    if (i == L - 1 && W < 16){
                        W++;
                        L *= 2;
                        st = Arrays.copyOf(st, L);
                    }
                }
                val = s;
            }
            BinaryStdOut.close();
        }if (c == 'r'){      //Reset Mode expansion
            String[] st = new String[L];
            int i; // next available codeword value

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(W);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];

            while (true) {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(W);
                if (codeword == R) break;
                String s = st[codeword];
                if (i == codeword) s = val + val.charAt(0);   // special case check
                if (i < L){
                    st[i++] = val + s.charAt(0);
                    if (i == L - 1 && W < 16) {
                        W++;
                        L *= 2;
                        st = Arrays.copyOf(st, L);
                    }if(i == L - 1 && W == 16){
                        W = 9;
                        L = 512;
                        st = new String[L];
                        for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                        st[i++] = "";                        // (unused) lookahead for EOF
                    }
                }
                val = s;
            }
            BinaryStdOut.close();
        }if (c == 'm'){      //Monitor Mode expansion
            String[] st = new String[L];
            int i; // next available codeword value
            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF
            int codeword = BinaryStdIn.readInt(W);
            compressed += W;                    // compressed data size
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];
            while (true) {
                BinaryStdOut.write(val);
                original += val.length() * 8;          // original data size
                codeword = BinaryStdIn.readInt(W);
                compressed += W;            // compressed data size
                newRatio = original / compressed;
                if (codeword == R) break;
                String s = st[codeword];
                if (i == codeword) s = val + val.charAt(0);   // special case check
                if (i < L){
                    st[i++] = val + s.charAt(0);
                    if (i == L - 1 && W < 16){
                        W++;
                        L *= 2;
                        st = Arrays.copyOf(st, L);
                    }
                }if (i == 65536){
                    if (update){
                        oldRatio = newRatio;
                        update = false;
                    }if (oldRatio / newRatio > 1.1){
                        W = 9;
                        L = 512;
                        st = new String[L];
                        for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                        st[i++] = "";                        // (unused) lookahead for EOF
                        codeword = BinaryStdIn.readInt(W);
                        if(codeword == R){
                            return;
                        }
                        oldRatio = 0;
                        newRatio = 0;
                        update = true;
                    }
                }
                val = s;
            }
            BinaryStdOut.close();
        }
    }

    public static void main(String[] args) {
        if (args[0].equals("-")){
           if (args[1].equals("n")) {    // Do Nothing mode
               compress();
           }if(args[1].equals("r")){
               compress_r();
           }if (args[1].equals("m")){
                compress_m();
            }
        }else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
