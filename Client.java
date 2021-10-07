import javafx.util.Pair;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    public static void main(String[] args) throws IOException {
        //Socket socket = new Socket("localhost",6000);
        String msg = "graph";
        int key = 3,range = 12;
        HashMap<Character,Integer> encryptionTable = createEncryptionTable(key,range,122);



        Graph graph = new Graph();
        graph.setAdjList(encrypt(encryptionTable,msg,key));
        String cT = "";

        ArrayList<ArrayList<Pair<Integer,Integer>>> l = graph.getAdjList();
        for(int i = 0 ; i < l.size() ; i++){
            for(int j = 0 ; j < l.get(i).size() ; j++){
                cT += i+1 + "-" +l.get(i).get(j).getKey() + "-" + l.get(i).get(j).getValue() + "\n";
            }
        }

        System.out.println(cT);

    }

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
            low = getRandomVal(low,high++);

            Pair<Integer,Integer> p = new Pair<>(col,low);
            adjList.get(row-1).add(p);


        }
        return adjList;
        
    }

    public static int getRandomVal(int low,int high){
        return ThreadLocalRandom.current().nextInt(low,high);
    }

    public static HashMap<Character,Integer> createEncryptionTable(int key, int total,int asciiRange){
        HashMap<Character,Integer> map = new HashMap<>();


        int c = 97;

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
