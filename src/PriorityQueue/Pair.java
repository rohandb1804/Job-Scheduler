package PriorityQueue;

public class Pair<T extends Comparable>{
    public T data;
    int count;
    public Pair(T data, int count){
        this.data=data;
        this.count=count;
    }


    public int compareTo(Pair pairs) {
        if(data.compareTo(pairs.data)==0){

            if(count<pairs.count){
                return 1;
            }else{
                return -1;
            }
        }
        else{
            return this.data.compareTo(pairs.data);
        }
    }
}
