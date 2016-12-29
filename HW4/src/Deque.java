
import java.util.NoSuchElementException;
import java.util.ListIterator;

/**
 *
 * @author CHIN LUNG
 */
public class Deque<T> implements Iterable<T> {

    private int N;        // number of elements on list
    private Node pre;     // sentinel before first item
    private Node post;    // sentinel after last item

    public Deque() {
        pre = new Node();
        post = new Node();
        pre.next = post;
        post.prev = pre;
    }

    // linked list node helper data type
    private class Node {
        private T item;
        private Node next;
        private Node prev;
    }

    public boolean isEmpty() {
        return (N==0);
    }

    public int size() {
        return N;
    }
//implement
public ListIterator<T> iterator()  { return new DoublyLinkedListIterator(); }

    // assumes no calls to DoublyLinkedList.add() during iteration
    private class DoublyLinkedListIterator implements ListIterator<T> {
        private Node current      = pre.next;  // the node that is returned by next()
        private Node lastAccessed = null;      // the last node to be returned by prev() or next()
                                               // reset to null upon intervening remove() or add()
        private int index = 0;

        public boolean hasNext()      { return index < N; }
        public boolean hasPrevious()  { return index > 0; }
        public int previousIndex()    { return index - 1; }
        public int nextIndex()        { return index;     }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastAccessed = current;
            T item = current.item;
            current = current.next; 
            index++;
            return item;
        }

        public T previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            current = current.prev;
            index--;
            lastAccessed = current;
            return current.item;
        }

        // replace the item of the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void set(T item) {
            if (lastAccessed == null) throw new IllegalStateException();
            lastAccessed.item = item;
        }

        // remove the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void remove() { 
            if (lastAccessed == null) throw new UnsupportedOperationException();
            Node x = lastAccessed.prev;
            Node y = lastAccessed.next;
            x.next = y;
            y.prev = x;
            N--;
            if (current == lastAccessed)
                current = y;
            else
                index--;
            lastAccessed = null;
        }

        // add element to list 
        public void add(T item) {
            Node x = current.prev;
            Node y = new Node();
            Node z = current;
            y.item = item;
            x.next = y;
            y.next = z;
            z.prev = y;
            y.prev = x;
            N++;
            index++;
            lastAccessed = null;
        }

    }
    // add the item to the list
    public void addLast(T item) {
         if (item == null) {
            throw new NullPointerException();
        }
        if (isEmpty() == true) {
            Node x = new Node();
            x.item = item;
            x.prev = pre;
            x.next = post;
            post.prev = x;
            pre.next = x;
            N++;
        } else {
            Node x = new Node();
            x.item = item;
            x.next = post;
            x.prev = post.prev;
            post.prev.next = x;
            post.prev = x;           
            N++;
        }
    }

    public void addFirst(T item) {
         if (item == null) {
            throw new NullPointerException();
        }
        if (isEmpty() == true) {
            Node x = new Node();
            x.item = item;
            x.prev = pre;
            x.next = post;
            post.prev = x;
            pre.next = x;
            N++;
        } else {
            Node x = new Node();
            x.item = item;
            x.prev = pre;
            x.next = pre.next;
            pre.next.prev = x;
            pre.next = x;
            
            N++;
        }
    }

    public T removeFirst() // remove and return the item from the front
    {
        
        if (isEmpty() == true) {
             throw new NoSuchElementException();
        } else {
            T item = pre.next.item;
            pre.next = pre.next.next;
            pre.next.prev=pre;
             N--;
             return item;
        }      
    }

    public T removeLast() // remove and return the item from the end
    {
        if (isEmpty() == true) {
             throw new NoSuchElementException();
        } else {
            T item = post.prev.item;
            post.prev.prev.next = post;
            post.prev = post.prev.prev;
            N--;
            return item;           
        }     
    }


    public static void main(String[] args) {
            Deque DQ =new Deque();
            DQ.addFirst(1);
            DQ.addLast(2);
            DQ.addFirst(0);
            DQ.addLast(3);
            System.out.println( DQ.removeFirst());
            System.out.println( DQ.removeLast());
            System.out.println( DQ.removeFirst());
            System.out.println( DQ.removeLast());
            System.out.println(DQ.removeFirst());
    }
}
