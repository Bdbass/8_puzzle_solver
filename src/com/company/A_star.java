package com.company;

import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class A_star {

    private static final int[] GOAL = {1, 2, 3, 8, 0, 4, 7, 6, 5};
    private static final HashMap<Integer, Pair<Integer, Integer>> GOALMAP =
            new HashMap<>(){
                {
                    put(1, new Pair<>(0, 0));
                    put(2, new Pair<>(0, 1));
                    put(3, new Pair<>(0, 2));
                    put(8, new Pair<>(1, 0));
                    put(0, new Pair<>(1, 1));
                    put(4, new Pair<>(1, 2));
                    put(7, new Pair<>(2, 0));
                    put(6, new Pair<>(2, 1));
                    put(5, new Pair<>(2, 2));
                }
            };

    //A* shortest path algorithm
    static Node AStar(ArrayList<Integer> start, Heuristic h) {

        //create pQ and visited arrayList, so we don't loop
        pQueue<Node> pq = new pQueue<Node>(0);
        ArrayList<ArrayList<Integer>> visited = new ArrayList<>();
        Node temp = new Node(start, h, GOAL, GOALMAP);

        //add start node to pq with priority of heuristic
        pq.enqueue(temp, temp.getf());

        //loop until pq is empty
        while(!pq.isEmpty()){
            //dequeue vertex w/ highest priority and set as visited
            temp = pq.dequeue();
            visited.add(temp.getState());

            // found goal state
            if(temp.gethDisplaced() == 0) return temp;

            //loop through all unvisited neighbors of current
            for(Node n: temp.expand(h, GOAL, GOALMAP)){
                if (!isVisited(visited, n.getState())){
                    // enqueue if it hasn't been visited yet
                    enqueue(n, pq);
                }
            }
        }

        // not solvable (at least on this machine)
        return null;
    }

    // returns true if the node has been visited false, else
    static boolean isVisited(ArrayList<ArrayList<Integer>> visited,
                             ArrayList<Integer> currentState){
        for (ArrayList<Integer> state: visited){
            if (state.equals(currentState)) return true;
        }
        return false;
    }

    // enqueues the node for us, dealing with priority updates
    static void enqueue(Node n, pQueue<Node> pq){
        // check if node is already in pq, if so just update it
        for (Pair<Node, Integer> p: pq.getPq()){
            if (p != null && p.getKey().getState().equals(n.getState())){
                //update the f(n) if needed
                if (n.getf() < p.getKey().getf()){
                    pq.changePriority(p, n, n.getf());
                }
                return;
            }
        }
        // else add it
        pq.enqueue(n, n.getf());
    }

    // reads line from file and returns an arrayList
    static ArrayList<Integer> readLine(String textFile){
        String temp;
        String temp_array[];
        ArrayList<Integer> start = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(textFile));
            temp = s.nextLine();
            temp_array = temp.split(",");
            for (String string: temp_array){
                start.add(Integer.parseInt(string));
            }
            return start;
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }

    // driver function
    public static void main(String args[]){
        Node answer = AStar(readLine("/Users/brandonbass/IdeaProjects/8_puzzle_solver/src/input.txt"), Heuristic.MAN);
        System.out.println(answer);
    }
}
