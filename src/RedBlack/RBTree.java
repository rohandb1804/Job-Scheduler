package RedBlack;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class RBTree<T extends Comparable, E> implements RBTreeInterface<T, E>  {
    static final boolean red=true;
    static final boolean black=false;

   int size=0;
    public RedBlackNode root;

    @Override
    public void insert(T key, E value) {
        if (searcher(key) != null) {
            search(key).values.add(value);
            return;
        }
        RedBlackNode p=root, q=null;
         RedBlackNode redBlackNode=new RedBlackNode<T,E>(key,value);
        redBlackNode.color=red;
        if(root==null){
            root=redBlackNode;
            redBlackNode.parent=null;
        } else{
            while(p!=null)
            {
                q=p;
                if(p.key.compareTo(redBlackNode.key)<0)
                    p=p.right;
                else
                    p=p.left;
            }
            redBlackNode.parent=q;
            if(q.key.compareTo(redBlackNode.key)<0)
                q.right=redBlackNode;
            else
                q.left=redBlackNode;
        }
        fix(redBlackNode);
    }

    RedBlackNode inserthelper(RedBlackNode root, T key, E value){
             if(root==null){
                 root=new RedBlackNode(key,value);
                 root.parent=null;
                 root.color=black;
                 return root;
             }
             int cmp=key.compareTo(root.key);
             if(cmp<0){
                 root.left=inserthelper(root.left,key,value);
                 root.left.parent=root;
             } else if (cmp > 0) {

                 root.right=inserthelper(root.right,key,value);
                 root.right.parent=root;
             }

             return root;
    }



    void fix(RedBlackNode z)
    {
        while(z.parent.color == red) {
            RedBlackNode grandparent = z.parent.parent;
            RedBlackNode uncle = root;
            if(z.parent == grandparent.left) {
                if(grandparent.right!=null) { uncle = grandparent.right; }
                if(uncle.color == red){
                    z.parent.color = black;
                    uncle.color = black;
                    grandparent.color = red;
                    if(grandparent.key != root.key){ z = grandparent; }
                    else { break; }
                }
                else if(z == grandparent.left.right) {
                    Rotate_left(z.parent);
                }
                else {
                    z.parent.color = black;
                    grandparent.color =red;
                   Rotate_right(grandparent);
                    if(grandparent.key != root.key){ z = grandparent; }
                    else { break; }
                }
            }
            else {
                if(grandparent.left!=null) { uncle = grandparent.left; }
                if(uncle.color == red){
                    z.parent.color = black;
                    uncle.color = black;
                    grandparent.color = red;
                    if(grandparent.key != root.key){ z = grandparent; }
                    else { break; }
                }
                else if(z == grandparent.right.left){
                   Rotate_right(z.parent);
                }
                else {
                    z.parent.color = black;
                    grandparent.color = red;
                    Rotate_left(grandparent);
                    if(grandparent.key != root.key){ z = grandparent; }
                    else { break; }
                }
            }
        }
    }



    public void Rotate_left(RedBlackNode p){
        if(p.right==null)
            return ;
        else
        {
            RedBlackNode y=p.right;
            if(y.left!=null)
            {
                p.right=y.left;
                y.left.parent=p;
            }
            else
                p.right=null;
            if(p.parent!=null)
                y.parent=p.parent;
            if(p.parent==null)
                root=y;
            else
            {
                if(p==p.parent.left)
                    p.parent.left=y;
                else
                    p.parent.right=y;
            }
            y.left=p;
            p.parent=y;
        }

    }

    public void Rotate_right(RedBlackNode node){

        if(node.left==null){
            return;
        }else{
            RedBlackNode y=node.left;
            if(y.right!=null)
            {
                node.left=y.right;
                y.right.parent=node;
            }
            else
                node.left=null;
            if(node.parent!=null)
                y.parent=node.parent;
            if(node.parent==null)
                root=y;
            else
            {
                if(node==node.parent.left)
                  node.parent.left=y;
                else
                    node.parent.right=y;
            }
            y.right=node;
            node.parent=y;

        }
    }

    /*public RedBlackNode leftleft(RedBlackNode node){

        node=Rotate_right(node);
        boolean tmpColor = node.color;
        node.color = node.right.color;
        node.right.color = tmpColor;
        return node;

    }

    public RedBlackNode rightright(RedBlackNode node){
        node=Rotate_left(node);
        //    System.out.println(node.key + "hdhudh" + " " + node.color);
//        System.out.println(node.left.key + " " + node.left.color + " " +node.right.key);
        boolean tmpColor = node.color;
        node.color = node.left.color;
        node.left.color = tmpColor;
        return node;
    }

    public RedBlackNode leftright(RedBlackNode node){
        node.left=Rotate_left(node.left);
        return leftleft(node);
    }
    public RedBlackNode rightleft(RedBlackNode node){
        node.right=Rotate_left(node.right);
        return rightright(node);
    }
   /* public void insert(T key, E value) {


        if (value == null) throw new IllegalArgumentException();

        if (searcher(key) != null) {
            search(key).values.add(value);
            return;
        }
        // No root node.
        if (root == null) {
            root = new RedBlackNode(key,value, null);
            root.color=black;
            insertionRelabel(root);
            return ;
        }

        for (RedBlackNode node = root; ; ) {

            int cmp = key.compareTo(node.key);

            // Left subtree.
            if (cmp < 0) {
                if (node.left == null) {
                    node.left =  new RedBlackNode(key,value, node);
                    insertionRelabel(node.left);
                  //  nodeCount++;
                    //return true;
                }
                node = node.left;

                // Right subtree.
            } else if (cmp > 0) {
                if (node.right == null) {
                    node.right = new RedBlackNode(key,value,node);
                    insertionRelabel(node.right);
                  //  nodeCount++;
                   // return true;
                }
                node = node.right;


            }
        }
    }*/

    private void insertionRelabel(RedBlackNode node) {

        RedBlackNode parent = node.parent;

        // Root node case.
        if (parent == null) {
            node.color = black;
            root = node;
            return;
        }

        RedBlackNode grandParent = parent.parent;
        if (grandParent == null) return;

        // The red-black tree invariant is already satisfied.
        if (parent.color == black || node.color == black) return;

        boolean nodeIsLeftChild = (parent.left == node);
        boolean parentIsLeftChild = (parent == grandParent.left);
        RedBlackNode uncle = parentIsLeftChild ? grandParent.right : grandParent.left;
        boolean uncleIsRedNode = (uncle == null) ? black : uncle.color;

        if (uncleIsRedNode) {

            parent.color = black;
            grandParent.color = black;
            uncle.color = black;

            // At this point the parent node is red and so is the new child node.
            // We need to re-balance somehow because no two red nodes can be
            // adjacent to one another.
        } else {

            // Parent node is a left child.
            if (parentIsLeftChild) {

                // Left-left case.
                if (nodeIsLeftChild) {
                    grandParent = leftLeftCase(grandParent);

                    // Left-right case.
                } else {
                    grandParent = leftRightCase(grandParent);
                }

                // Parent node is a right child.
            } else {

                // Right-left case.
                if (nodeIsLeftChild) {
                    grandParent = rightLeftCase(grandParent);

                    // Right-right case.
                } else {
                    grandParent = rightRightCase(grandParent);
                }
            }
        }

        insertionRelabel(grandParent);
    }

    private void swapColors(RedBlackNode a, RedBlackNode b) {
        boolean tmpColor = a.color;
        a.color = b.color;
        b.color = tmpColor;
    }

    private RedBlackNode leftLeftCase(RedBlackNode node) {
        node = rightRotate(node);
        swapColors(node, node.right);
        return node;
    }

    private RedBlackNode leftRightCase(RedBlackNode node) {
        node.left = leftRotate(node.left);
        return leftLeftCase(node);
    }

    private RedBlackNode rightRightCase(RedBlackNode node) {
        node = leftRotate(node);
        swapColors(node, node.left);
        return node;
    }

    private RedBlackNode rightLeftCase(RedBlackNode node) {
        node.right = rightRotate(node.right);
        return rightRightCase(node);
    }

    private RedBlackNode rightRotate(RedBlackNode parent) {

        RedBlackNode grandParent = parent.parent;
        RedBlackNode child = parent.left;

        parent.left = child.right;
        if (child.right != null) child.right.parent = parent;

        child.right = parent;
        parent.parent = child;

        child.parent = grandParent;
        updateParentChildLink(grandParent, parent, child);

        return child;
    }

    private RedBlackNode leftRotate(RedBlackNode parent) {

        RedBlackNode grandParent = parent.parent;
        RedBlackNode child = parent.right;

        parent.right = child.left;
        if (child.left != null) child.left.parent = parent;

        child.left = parent;
        parent.parent = child;

        child.parent = grandParent;
        updateParentChildLink(grandParent, parent, child);

        return child;
    }

    // Sometimes the left or right child node of a parent changes and the
    // parent's reference needs to be updated to point to the new child.
    // This is a helper method to do just that.
    private void updateParentChildLink(RedBlackNode parent, RedBlackNode oldChild, RedBlackNode newChild) {
        if (parent != null) {
            if (parent.left == oldChild) {
                parent.left = newChild;
            } else {
                parent.right = newChild;
            }
        }
    }





    @Override
    public RedBlackNode<T, E> search(T key) {
        if(getutil(root,key)==null){
            RedBlackNode redBlackNode=new RedBlackNode(null,null);
            redBlackNode.parent=null;
            redBlackNode.values=null;
            return redBlackNode;
        }
        return getutil(root,key);

    }

    public RedBlackNode<T,E> searcher(T key){
        if(getutil(root,key)==null){
            return null;
        }
        return getutil(root,key);
    }


    RedBlackNode  getutil(RedBlackNode node,T key){
        if(node==null){
            return null;
        }
        int cmp=key.compareTo(node.key);
        if(cmp<0){
            RedBlackNode child=getutil(node.left,key);
            node=child;
        }
        else if(cmp>0){
            RedBlackNode child=getutil(node.right,key);
            node= child;
        }

        return node;
    }



   public int size(){
        return size;
   }

   public int height(RedBlackNode n){
        if(n==null){
            return 0;
        }
        return 1+Math.max(height(n.left),height(n.right));
   }

}