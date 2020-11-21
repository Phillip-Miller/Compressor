import java.io.*;
import java.util.ArrayList;

/** Program for decompressing files from my compression algorithm
 *  @author Phillip M
 *  @version 1.0
 *  @since   11/10/2020
 */

public class Decompress {
    private static final int lastASCIIVALUE = 126;
    private static int arrayDoubled= 0;
    public static void main(String [] args){
        if(args.length != 1) {
            System.out.println("Bad input");
        }
        try{
            long start = System.currentTimeMillis();
            algorithm(binaryToArray(args[0]),args[0]);
            long delta = System.currentTimeMillis() - start;
            writeLog(args[0].substring(0,args[0].length()-4), delta);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String [] buildArray(){
        String [] starterSet = new String[191];
        for (int i = 32; i <= lastASCIIVALUE; i++) {
            starterSet[i] = String.valueOf((char) i);
        }
        starterSet[10] = (String.valueOf((char) 10));   //n
        starterSet[13] =(String.valueOf((char) 13));  //r
        starterSet[9] =(String.valueOf((char) 9));   //t
        return starterSet;
    }
    private static String [] doubleArraySize(String [] currentArray){
        arrayDoubled++;
        String [] newArray = new String [currentArray.length*2];
        for(int i = 0; i<currentArray.length; i++){
            newArray[i] = currentArray[i];
        }
        return newArray;
    }

    private static int[] binaryToArray (String fileName)throws FileNotFoundException, IOException{
        //byte [] fileData = Files.readAllBytes(Paths.get(fileName));
        FileInputStream fis = new FileInputStream(new File(fileName));
        DataInputStream dis = new DataInputStream(fis);
        ArrayList<Integer> arrayList = new ArrayList<>();
        int nextByte;
        int x = 0;
        while (dis.available()>3) {
            arrayList.add(dis.readInt());
        }

        int [] returnArray = new int [arrayList.size()];
        for(int i = 0; i<arrayList.size(); i++){
            returnArray[i] = arrayList.get(i);
        }
        return returnArray;

    }
    //fileName is the compressed File
    private static void algorithm(int [] compressed, String fileName)throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName.substring(0,fileName.length()-4)));
        String [] fakeDict = buildArray();
        int value = lastASCIIVALUE+1;
        bw.write(fakeDict[compressed[0]]);
        for(int i = 1; i<compressed.length;i++){
            if (value>= fakeDict.length-1) {
                fakeDict = doubleArraySize(fakeDict);
            }
            if(compressed[i]<fakeDict.length && fakeDict[compressed[i]] != null){ //if the entry is in the dictionary
                String nextEntry = fakeDict[compressed[i-1]] +  fakeDict[compressed[i]].charAt(0);  //last entry plus first charecter of next entry
                bw.write(fakeDict[compressed[i]]);
                fakeDict[value++] = nextEntry;
            }
            else { //fakeDict(p) = fakeDict(q) + FC fakeDict(q) This happens if you have aaa which gets compressed to 97,127. That outof bound value must be a double of the same char
                String nextEntry = fakeDict[compressed[i-1]] +  fakeDict[compressed[i-1]].charAt(0);
                fakeDict[compressed[i]] = nextEntry;
                bw.write(nextEntry);
            }

        }
        bw.close();
    }

    private static void writeLog(String inFile,double milliseconds) throws FileNotFoundException, IOException{
        BufferedWriter bw = new BufferedWriter(new PrintWriter(inFile + ".log"));
        bw.write("Decompression of " + inFile + ".zzz.\n" );
        bw.write("Decompression took " + milliseconds/1000 + " seconds.\n" + "The table was doubled " + arrayDoubled + " times.");
        bw.close();
    }
}
