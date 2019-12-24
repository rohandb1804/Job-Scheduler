package Trie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Trie<T> implements TrieInterface {

    TrieNode root = new TrieNode();
    ArrayList<String> tosorted_array = new ArrayList<>();


    private void Sorted_array(ArrayList<String> arr) {
        for (int i = 1; i < arr.size(); ++i) {
            String key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && arr.get(j).compareTo(key) > 0) {
                arr.set(j + 1, arr.get(j));
                j = j - 1;
            }
            arr.set(j + 1, key);

        }
    }

    public void printTrie(TrieNode trieNode) {
        printhelper(trieNode, 0);
        Sorted_array(tosorted_array);
        for (int j = 0; j < tosorted_array.size(); j++) {
            System.out.println(tosorted_array.get(j));
        }
        tosorted_array.clear();

    }

    public void printhelper(TrieNode trieNode, int level) {
        if (trieNode.ending == true) {
            tosorted_array.add(trieNode.getValue().toString());
        }

       for(int i=0;i<trieNode.children.length;i++){
           if(trieNode.children[i]!=null){
               printhelper(trieNode.children[i],level+1);
           }
       }

    }

    @Override
    public boolean delete(String word) {
        if (search(word) == null) {
            return false;
        }

        deletehelper(root,word,0);
        return true;
    }


    private TrieNode deletehelper(TrieNode trieNode,String word, int level){
        boolean hasChildren=false;
        boolean rootchild=false;
         if(level==word.length()) {
             trieNode.ending = false;

             for (int i = 0; i < trieNode.children.length; i++) {
                 if (trieNode.children[i] != null) {
                     hasChildren = true;
                 }
             }

                 if (!hasChildren) {
                     trieNode = null;
                 }
                 return trieNode;

         }

         int ind=word.charAt(level)-' ';
         trieNode.children[ind]=deletehelper(trieNode.children[ind], word,level+1);
         for(int i=0;i<trieNode.children.length;i++){
             if (trieNode.children[i] != null) {
                 rootchild = true;
             }
         }

         if(trieNode.ending==false && !rootchild){
             trieNode=null;
         }

         return trieNode;


    }

    @Override
    public TrieNode search(String word) {

       TrieNode trieNode=root;
        for(int i=0;i<word.length();i++){
           int index=word.charAt(i)-' ';
           if(trieNode.children[index]==null){
               return null;
           }
           trieNode=trieNode.children[index];
        }

        if(trieNode!=null && trieNode.ending==true){
            return trieNode;
        }
        return null;
    }


    @Override
    public TrieNode startsWith(String prefix) {
        TrieNode trieNode=root;
        for(int i=0;i<prefix.length();i++){
            int index=prefix.charAt(i)-' ';
            if(trieNode.children[index]==null){
                return null;
            }
            trieNode=trieNode.children[index];
        }

        if(trieNode!=null){
            return trieNode;
        }
        return null;
    }

    @Override
    public boolean insert(String word, Object value) {

      if(search(word)!=null){
          return false;
      }
      int level=0;
      TrieNode node=root;
      for(int i=0;i<word.length();i++){
            int index=word.charAt(i)-' ';
            level++;
            if(node.children[index]==null){
                 node.children[index]=new TrieNode();
                 node.children[index].c=word.charAt(i);
                 node.children[index].total=level;
            }
            node=node.children[index];
        }

      node.ending=true;
      node.word=word;
      node.c=word.charAt(word.length()-1);
      node.total=level;
      node.data=value;


      return true;


    }

    @Override
    public void print() {
        System.out.println("-------------");
        System.out.println("Printing Trie");
        int start = 1;
        int stop= printhelper2(0) ;
        while (start != stop+2) {
            printLevel(start);
            start++;
        }
        System.out.println("-------------");
    }

    @Override
    public void printLevel(int level) {

        printhelper2(level);
        char_sort(char_arr);
        System.out.print("Level" + " " + level + ": ");
        for (int i = 0; i < char_arr.size(); i++) {
            if (i != char_arr.size() - 1)
                System.out.print(char_arr.get(i) + ",");
            else {
                System.out.println(char_arr.get(i));
            }

        }
        if (char_arr.size() == 0) {
            System.out.println();
        }
        char_arr.clear();





    }


    private int p;
    ArrayList<Character> char_arr=new ArrayList<>();
    private int  printhelper2(int level) {

        Queue<TrieNode> queue = new LinkedList<TrieNode>();
        queue.add(root);
        while (!queue.isEmpty())
        {
            TrieNode trieNode = queue.poll();
            if(p<trieNode.total){
                p=trieNode.total;
            }
            if(trieNode.total==level) {
                Character ch=trieNode.c;
                if(ch.equals(' ')){

                }else {
                    // System.out.print(" "+ charr[p]+ " ");
                    char_arr.add(trieNode.c);
                    p++;
                }

            }
            for(int i=0;i<trieNode.children.length;i++){
                if(trieNode.children[i]!=null){
                    queue.add(trieNode.children[i]);
                }
            }
        }

        return p;


    }

    private  void char_sort(ArrayList<Character> arr){
        for (int i = 1; i < arr.size(); ++i) {
            Character key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && arr.get(j).compareTo(key) > 0) {
                //    arr[j + 1] = arr[j];
                arr.set(j+1,arr.get(j));
                j = j - 1;
            }
            arr.set(j+1,key);
            //arr[j + 1] = key;
        }

    }

}


