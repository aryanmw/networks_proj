import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //TODO get value from client socket
        ServerSocket ss = new ServerSocket(6000);
        Socket s = ss.accept();

        InputStream inputStream = s.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);


        /*
        This object received has the key which will be used to form the decryption
        table and convert the cipher text to plain text
         */
        KeyObject keyObj = (KeyObject) objectInputStream.readObject();
        int rows = keyObj.getRows();
        int asciiRange = keyObj.getAsciiRange();
        int cols = keyObj.getCols();
        int asciiLow = keyObj.getAsciiLow();






        Socket s1 = ss.accept();

        /*
        This object is a bipartite graph object which is the cipher text which needs to be
        decrypted to get the plain text
         */
        InputStream is = s1.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Graph graph = (Graph) ois.readObject();
        ArrayList<ArrayList<Pair<Integer,Integer>>> adjList = graph.getAdjList();


        HashMap<Integer,Character> decryptionTable = createDecryptionTable(rows,cols,asciiLow,asciiRange);



        String plainText = decryptNumbers(decryptionTable,adjList);
        System.out.println(plainText);

        s1.close();
        ss.close();






    }

        /*
         Function which decrypts the cipher text by first making
         an array list of pairs which will have the characters
         and the corresponding random value which is then sorted
         to return the plain text.
         */

    public static String decryptNumbers(HashMap<Integer,Character> decryptionTable, ArrayList<ArrayList<Pair<Integer,Integer>>> adjList){
        List<Pair<Integer,Character>> charList = new ArrayList<>();
        for(int i = 0 ; i < adjList.size() ; i++){
            for(int j = 0 ; j < adjList.get(i).size() ; j++){
                int colNum = adjList.get(i).get(j).getKey();
                int weight = adjList.get(i).get(j).getValue();

                String codeStringForChar = i+1 + "" + colNum;
                Character ptChar = decryptionTable.get(Integer.parseInt(codeStringForChar));
                charList.add(new Pair<>(weight,ptChar));
            }
        }

        Collections.sort(charList, new Comparator<Pair<Integer, Character>>() {
            @Override
            public int compare(Pair<Integer, Character> o1, Pair<Integer, Character> o2) {
                if (o1.getKey() > o2.getKey()) {
                    return 1;
                } else if (o1.getKey().equals(o2.getKey())) {
                    return 0; // You can change this to make it then look at the
                    //words alphabetical order
                } else {
                    return -1;
                }
            }
        });
        String plainText = "";
        for(int i = 0 ; i < charList.size() ; i++){
            plainText += charList.get(i).getValue();
        }

        return plainText;
    }

    /*
    Creates the decryption table which
    is used to convert cipher text to plain text
     */
    public static HashMap<Integer,Character> createDecryptionTable(int rows,int cols,int asciiLow, int asciiRange){
        HashMap<Integer,Character> map = new HashMap<>();
        int c = asciiLow;

        for(int j = rows + 1 ; j <= cols ; j++){
            for(int k = 1 ; k <= rows ; k++){
                if(c > asciiRange){
                    break;
                }
                char ch = (char) c;
                String v = k + "" + j;
                int val = Integer.parseInt(v);
                map.put(val,ch);
                c++;

            }
            if(c > asciiRange){
                break;
            }
        }


        return map;
    }


}
