import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public class Graph implements Serializable {
    private ArrayList<ArrayList<Pair<Integer,Integer>>> adjList;

    public Graph() {

    }

    public ArrayList<ArrayList<Pair<Integer, Integer>>> getAdjList() {
        return adjList;
    }

    public void setAdjList(ArrayList<ArrayList<Pair<Integer, Integer>>> adjList) {
        this.adjList = adjList;
    }


}
