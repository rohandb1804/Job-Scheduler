package PriorityQueue;


import java.util.ArrayList;


public class MaxHeap<T extends Comparable> implements PriorityQueueInterface<T> {

    public ArrayList<Pair<T>> array;
    int currentsize=0;
    int count=0;

    public MaxHeap()
    {
        this.array=new ArrayList<>();
        currentsize=0;

    }

    @Override
    public void insert(T element) {


        count++;
        Pair<T>  pair=new Pair<>(element,count);
        array.add(pair);
        currentsize++;
//        System.out.print(array.get(0).data);
        move_up(array.size()-1);
    }

    @Override
    public T extractMax() {
        if(currentsize==0){
            return null;
        }
        Pair<T> remove= array.get(0);

        Pair tmp=array.get(array.size()-1);
        array.set(array.size()-1,array.get(0));
        array.set(0,tmp);
        currentsize--;
        array.remove(currentsize);
        // heapify_down(0);
        move_down(0);

        return remove.data;

    }


    public void move_up(int index){

        while(index>0){
            int root=(index-1)/2;
            if(array.get(index).compareTo(array.get(root))<0){
                break;
            }
            //swap
            Pair tmp=array.get(root);
            array.set(root,array.get(index));
            array.set(index,tmp);
            index=root;

        }

    }

    public void move_down(int index){
        while(true){
            int left=2*index+1;
            int right=2*index+2;

            int max=right;

            //  System.out.print(array.get(index));
            if(left<currentsize){
                max=left;
            }
            if(right<currentsize&& array.get(left).compareTo(array.get(right))<0){
                max=right;
            }
            if( left >= currentsize || array.get(index).compareTo(array.get(max))>=0){
                break;
            }
            // System.out.println(" " +array.get(max));
            Pair tmp=array.get(max);
            array.set(max,array.get(index));
            array.set(index,tmp);
            //  System.out.println(" " +array.get(index));
            index=max;

        }

    }

    public int size(){
        return array.size();
    }

    public void clear(){
        while(array.size()!=0){
            extractMax();
        }
    }


}