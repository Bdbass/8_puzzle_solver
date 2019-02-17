package com.company;
import java.util.ArrayList;
import javafx.util.Pair;

public class  pQueue<T>{

    //class fields
    private ArrayList<Pair<T, Integer>> pq;
    private int size;
    private int type;

    //constructor
    pQueue(int type){
        pq = new ArrayList<>();
        //want to start at index 1 so 0 index should be null
        pq.add(null);
        size = 0;
        this.type = type;
    }

    // returns the pq
    public ArrayList<Pair<T, Integer>> getPq(){
        return this.pq;
    }

    // in place swap of two elements
    private void swap(int loc1, int loc2){
        Pair<T, Integer> temp = pq.get(loc1);
        pq.set(loc1, pq.get(loc2));
        pq.set(loc2, temp);
    }

    //adds element to pqueue
    public void enqueue(T obj, int p) {
        size++;
        // add element at the end of the queue
        Pair<T, Integer> newPair = new Pair(obj, p);
        pq.add(newPair);
        // initialize parent and child
        int parent = size/2;
        int child = size;
        //while child is Higher Priority than parent swap
        while(parent > 0 && HigherPriority(pq.get(child).getValue() , pq.get(parent).getValue())){
            swap(parent, child);
            child = parent;
            parent = child/2;
        }
    }

    //removes element from pqueue
    public T dequeue() {
        //check if queue is empty
        T head = peak();
        if (head == null) return null;
        //swap first and last element then erase last element
        swap(1, size);
        Remove(1);
        return head;
    }

    //removes an element from the pq
    private void Remove(int parent){
        //remove last element and decrement size
        pq.remove(size);
        size--;
        //initialize children
        int child1 = parent*2;
        int child2 = child1+1;
        // keep moving parent down while it is higher priority than child
        while(child1 <= size && child2 <= size &&
                (HigherPriority(pq.get(child1).getValue(), pq.get(parent).getValue()) ||
                        HigherPriority(pq.get(child2).getValue(), pq.get(parent).getValue()))){
            if (HigherPriority(pq.get(child1).getValue(), pq.get(child2).getValue())){
                swap(parent, child1);
                parent = child1;
            }else{
                swap(parent, child2);
                parent = child2;
            }
            child1 = parent*2;
            child2 = child1+1;
        }
        // swap child1 and parent if child1 is lower priority than parent
        if (child1 <= size && HigherPriority(pq.get(child1).getValue(), pq.get(parent).getValue())){
            swap(child1,parent);
        }
    }

    // returns the top element of the pq
    public T peak() {
        if (isEmpty()) {
            System.out.println("P-Queue is empty");
            return null;
        }
        return pq.get(1).getKey();
    }

    //returns the priority of the top element
    public Integer peakPriority() {
        if(isEmpty()) {
            System.out.println("Queue is empty");
            return null;
        }
        return pq.get(1).getValue();
    }

    //checks if the pq is empty
    public boolean isEmpty() {
        return (size<1);
    }

    //changes the priority of the given obj
    public void changePriority(Pair<T, Integer> obj, T obj2,  int P) {
        int parent = pq.indexOf(obj);
        //T temp = pq.get(parent).getKey();
        swap(parent, size);
        Remove(parent);
        enqueue(obj2, P);
    }

    // empties the pq
    public void clear() {
        pq.clear();
        pq.add(null);
        size = 0;
    }

    //compares two Priorities, if the first has higher priority it returns true, else returns false
    public boolean HigherPriority(int P1, int P2) {
        if (P1 < P2 ) return true;
        if (P1 > P2) return false;
        return (P1 < (P2));
    }
}
