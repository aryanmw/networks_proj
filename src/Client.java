import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",6000);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string with lowercase alphabets:");
        String msg = scanner.next();

        /*
        Pre-setting the key values
        These can also be randomised to make
        it more realistic
         */
        int key = 3,range = 12,asciiLow = 97,asciiHigh = 122;



        HashMap<Character,Integer> encryptionTable = createEncryptionTable(key,range,asciiLow,asciiHigh);

        /*
        The decryption key which will be sent along with the data
        to the server
         */
        KeyObject keyObj = new KeyObject(key,range,asciiLow,asciiHigh);

        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(keyObj);
        oos.flush();
        socket.close();



        Socket s = new Socket("localhost",6000);
        System.out.println(s.isConnected());
        Graph graph = new Graph();
        graph.setAdjList(encrypt(encryptionTable,msg,key));


        OutputStream os1 = s.getOutputStream();
        ObjectOutputStream oos1 = new ObjectOutputStream(os1);
        oos1.writeObject(graph);
        oos1.flush();
        s.close();
        String cT = "";


        /*
        Printing the encrypted text
        which is cipher text for demo
         */
        ArrayList<ArrayList<Pair<Integer,Integer>>> l = graph.getAdjList();
        for(int i = 0 ; i < l.size() ; i++){
            for(int j = 0 ; j < l.get(i).size() ; j++){
                cT += i+1 + "-" +l.get(i).get(j).getKey() + "-" + l.get(i).get(j).getValue() + "\n";
            }
        }

        System.out.println(cT);

    }

    /*
    Function which converts plain text to
    cipher text using the encryption table
     */
    public static ArrayList<ArrayList<Pair<Integer,Integer>>> encrypt(HashMap<Character,Integer> eTable, String plainText,int key){
        ArrayList<ArrayList<Pair<Integer,Integer>>> adjList = new ArrayList<>();

        for(int i = 0 ; i < key ; i++){
            ArrayList<Pair<Integer,Integer>> list = new ArrayList<>();
            adjList.add(list);
        }
        String curr = "";
        int low = 1;
        int high = 10000;

        for(int i = 0 ; i < plainText.length() ; i++){
            curr = eTable.get(plainText.charAt(i)).toString();
            String r = curr.charAt(0)+"";
            String c = curr.substring(1);
            int col = Integer.parseInt(c);
            int row = Integer.parseInt(r);
            low = getRandomVal(low,high+=5);

            Pair<Integer,Integer> p = new Pair<>(col,low);
            adjList.get(row-1).add(p);


        }
        return adjList;
        
    }

    /*
    Generates a random value in the given range
     */
    public static int getRandomVal(int low,int high){
        return ThreadLocalRandom.current().nextInt(low,high);
    }


    /*
    This function creates the encryption table
    using the elements of the key object
     */
    public static HashMap<Character,Integer> createEncryptionTable(int key, int total,int asciiLow,int asciiRange){
        HashMap<Character,Integer> map = new HashMap<>();


        int c = asciiLow;

        for(int j = key + 1 ; j <= total ; j++){
            for(int k = 1 ; k <= key ; k++){
                if(c > asciiRange){
                    break;
                }
                char ch = (char) c;
                String v = k + "" + j;
                int val = Integer.parseInt(v);
                map.put(ch,val);
                c++;

            }
            if(c > asciiRange){
                break;
            }
        }


        return map;
    }
}
