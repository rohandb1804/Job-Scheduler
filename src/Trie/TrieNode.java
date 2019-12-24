package Trie;
import Util.NodeInterface;

public class TrieNode<T> implements NodeInterface<T> {


    char c;
    public TrieNode[]  children;
    T data;
    boolean ending;
    int total;
    String word;


    TrieNode(){
        c=' ';
        ending=false;
        total=0;
        children=new TrieNode[95];
        this.data=null;
    }
    @Override
    public T getValue() {
        return data;
    }



}