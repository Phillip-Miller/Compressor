import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/** Program for compressing files using a longest recurring prefix algorithm
 *  Uses custom "HashTableChain" class I wrote to make a Chained Dictionary
 *  @author Phillip M
 *  @version 1.0
 *  @since   11/10/2020
 */

public class Compress {
    private static HashTableChain <String,Integer>  dict ;//= new HashTableChain<>();
    private static final int lastASCIIVALUE = 126;
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.out.println("Bad input");
        }
        File file = new File(args[0]);
        makeDict((int) file.length());
        try {
            long start = System.currentTimeMillis();
            startAlgorithm(args[0]);
            long delta = System.currentTimeMillis() - start;
            writeLog(args[0], delta);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    private static void makeDict(int bytes) {
        //This scales starting size by file size but it is a wild guess

        dict = new HashTableChain<>(HashTableChain.nextPrime(bytes / 5));
        for (int i = 32; i <= lastASCIIVALUE; i++) {
            dict.put(String.valueOf((char) i), i);
        }
        //put in special 3 spacing charecters \n,\t,\r
        dict.put(String.valueOf((char) 10), 10);   //n
        dict.put(String.valueOf((char) 13), 13);  //r
        dict.put(String.valueOf((char) 9), 9);   //t
    }


    /**
     * @param args string name of file to compress
     * @throws IOException
     * Parses the file and sends it to algorithm method
     */
    private static void startAlgorithm(String args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(args));
        ArrayList<String> allChars = new ArrayList<>();
        int charInt;
        while((charInt = br.read()) != -1) {
            System.out.println(String.valueOf((char) charInt));
            allChars.add(String.valueOf((char) charInt));
        }
        algorithm(allChars,args);
    }

    /**
     * @param allChars a list of all the given chars on a line
     * Writes to file with binary integers.
     */
        private static void algorithm(ArrayList<String> allChars, String args)throws FileNotFoundException, IOException{
            FileOutputStream fos = new FileOutputStream(new File(args + ".zzz"));
            DataOutputStream dos = new DataOutputStream(fos);
            String maxString = ""; String prevString = "";
            int value = lastASCIIVALUE + 1;

            //If you wanted to optimize further read the file hear instead of taking in allChars
            for(int i=0; i<allChars.size();){
                prevString = maxString;
                maxString += allChars.get(i);

                if (dict.get(maxString) == null ){ //entry doesnt exist
                    System.out.printf("Dict.put(maxString: %s, value: %d). Writing: (%s) as value (%d) \n",maxString,value,prevString,dict.get(prevString));
                    dict.put(maxString, value); value++;
                    dos.writeInt(dict.get(prevString));
                    maxString = "";
                }
                else if(i == allChars.size()-1) { //hit the last index just write and quit
                    dos.writeInt(dict.get(maxString));
                    break;
                }
                else
                    i++;
            }

        }

    private static void writeLog(String inFile,double milliseconds) throws FileNotFoundException, IOException{
        BufferedWriter bw = new BufferedWriter(new PrintWriter(inFile + ".zzz.log"));
        bw.write("Compression of " + inFile + "\n" );
        bw.write("Compressed from " + (double) Files.size(Paths.get(inFile))/1000 +  " Kilobytes to " + (double) Files.size(Paths.get(inFile + ".zzz"))/1000 + " Kilobytes \n");
        bw.write("Compression took " + milliseconds/1000 + " seconds\n"
                + "The dictionary contains " + dict.numKeys + " total entries\n"
                + "The table was rehashed " + dict.reHashed + " times");
        bw.close();
    }

}
