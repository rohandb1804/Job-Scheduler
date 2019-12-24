package RedBlack;

import Util.RBNodeInterface;

import java.util.LinkedList;
import java.util.List;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {

    public T key;
    List<E> values=new LinkedList<>();
    public RedBlackNode left,right,parent;
    boolean color;


    RedBlackNode(T key, E elem) {
        this.key=key;
        values.add(elem);
    }

    @Override
    public E getValue() {
        return null;
    }

    @Override
    public List<E> getValues() {
        //System.out.print(" "+ color);
        return values;
    }
}
