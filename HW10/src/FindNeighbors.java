import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author CHIN LUNG
 */
public class FindNeighbors {
    
     protected static Point2D[] AllPoint;
     protected static KdTree kt = new KdTree();
     protected static MaxPQ mpq = new MaxPQ();
     
    // DO NOT MODIFY THE CONSTRUCTOR! 
    public FindNeighbors(){}

    // TODO
    // please use the Point2D from algs4.jar 
    public void init(Point2D[] points){
        
        for(int i = 0; i < points.length; i++)
        {
            kt.insert(points[i]);
        }       
        return;
    }

    // TODO
    // the result should be sorted accordingly to their distances to the query, from small to large
    // please use the Point2D from algs4.jar 
    public Point2D[] query(Point2D point, int k){
        Point2D[] result = new Point2D[k];
        Pair outcome;
        kt.nearest(point,k);
        
        
        for(int i = result.length-1; i >=0;i--)
        {
            outcome  = (Pair)mpq.delMax();
            result[i] = outcome.point;
        }
        return result;
    }
    
     public static class Pair implements Comparable<Pair> {
        
         Point2D point;
         double distance;
         
        public Pair(Point2D point, double dist)
        {
            this.point = point;
            distance = dist;
        }
        @Override
        public int compareTo(Pair that) 
        {
              if(this.distance > that.distance)
            {
                return 1;
            }
            else if(this.distance == that.distance)
            {
                return 0;
            }
            else{ return -1;}
        }
    }

    
    
    public static class KdTree {
    
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;
    
    private class Node {
        private Node left;
        private Node right;
        private Point2D value;       
        private boolean type;
        private int size;

        public Node(Point2D val, boolean type) {
            this.value = val;
            this.type = type;
            this.size = 1;
        }
        
        public int compare(double p1, double p2) {
            if (p1 > p2) {
                return 1;
            } else if (p1 < p2) {
                return -1;
            } else {
                return 0;
            }
        }
        
        public int compareTo(Point2D point) 
        {
            if (this.type == VERTICAL) 
            {                
                return compare(this.value.x(), point.x());
            }
            else
            {
                return compare(this.value.y(), point.y());
            }
        }

    }
    
    public Node root = null;    
    public Point2D minPoint = null;
    private double minDist = 0.0;
    /**
     * construct an empty set of points
     */
    public KdTree() {
        
    }
    
    
    /**
     * @return number of points in the set
     */
    public int size() {
        return getSize(root);
    }  
  public void insert(Point2D p) 
    {
        root = insert(root, p, VERTICAL, null);
    }
    
    private int getSize(Node node) 
    {
        if (node == null) 
        {
            return 0;
        }
        
        return node.size;
    }
    
    private Node insert(Node node, Point2D p, boolean type, Node parent) {
        
        if (node == null) {            
            return new Node(p, type);
        }

        if (node.compareTo(p) > 0)
        {
            node.left = insert(node.left, p, !type, node);
        } 
        else if (node.compareTo(p) < 0) 
        {
            node.right = insert(node.right, p, !type, node);
        } 
        
        node.size = 1 + getSize(node.left) + getSize(node.right);
        
        return node;
    }
    
  
    
    /**
     * @param p point
     * @return does the set contain the point p?
     */
    public boolean contains(Point2D p) {
        return search(root, p) != null;
    }
    
    private Node search(Node node, Point2D p) {
        if (node == null) {
            return null;
        }
        
        if (node.compareTo(p) > 0) {
            return search(node.left, p);
        } else if (node.compareTo(p) < 0) {
            return search(node.right, p);
        }
        
        return node;
    }
    
    /**
     * @param p point
     * @return a nearest neighbor in the set to p; null if set is empty
     */
    public Point2D nearest(Point2D p,int k) {
        minPoint = root.value;
        minDist = minPoint.distanceSquaredTo(p);      
        searchNearest(root, p,k);
        return minPoint;
    }
    
    
    

    Pair pa; Node previous = null;
    private void searchNearest(Node node, Point2D p, int k ) 
    {
        double dist = node.value.distanceSquaredTo(p);
        // find 前幾小的
                          
        pa  = new Pair(node.value,dist);

        if( mpq.size() == k && pa.compareTo((Pair)mpq.max()) == -1 )
        {
            mpq.delMax();
            mpq.insert(pa);
        }
        else if(mpq.size() <k)
        {
            mpq.insert(pa);
        }
        if (minDist > dist) 
        {
            minPoint = node.value;
            minDist = dist;  
        }
        
        if (node.left != null && node.right != null) 
        {
            
            // go left
            if (Go(node,p)) 
            {
//                previous = node;
//                if(judge(node.left,previous,p))
//                {
//                   searchNearest(node.right, p,k);
//                }
                
                searchNearest(node.left, p,k);
                
                if(node.size >=3 && node.right != null)
                {
                     searchNearest(node.right, p,k);
                }
                
            } 
            else //go right
            {
//                previous = node;
//                if(judge(node.left,previous,p))
//                {
//                    searchNearest(node.right, p,k);
//                }
                
                searchNearest(node.right, p,k);
                
                if(node.size >=3 && node.left != null)
                {
                     searchNearest(node.left, p,k);
                }
            }          
            return;
        }

        if (node.left != null) 
        {
            searchNearest(node.left, p,k);
        }
        
        if (node.right != null) 
        {
            searchNearest(node.right, p,k);
        }
      
        return;
    }
    
    // use vertical distance to judge whether you need to go back or not
    private boolean judge(Node now ,Node pre, Point2D target)
    {
        if(pre.type == VERTICAL)
        {  //另一邊的路徑可以不考慮
            if(Math.abs(pre.value.x() - target.x()) > target.distanceTo(now.value))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(Math.abs(pre.value.y() - target.y()) > target.distanceTo(now.value))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    //go left return true
    private boolean Go(Node next ,Point2D t)
    {
        if(next.type == VERTICAL)
        {
            if(t.x() < next.value.x())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
             if(t.y() < next.value.y())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
    
    
    
     public static void main(String[] args)throws Exception {
            try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {  
            int Totalpoints = Integer.parseInt(br.readLine().trim());
           double [][] vertices = new double[Totalpoints][2]; AllPoint = new Point2D[Totalpoints];//Cluster = new cluster[Totalpoints];
            // 1. read in the file containing N 2-dimentional points
            for(int i = 0 ; i < Totalpoints;i++)
            {
                String[] data= br.readLine().split(" ");
                vertices[i][0] = Double.parseDouble(data[0]) ;
                vertices[i][1] = Double.parseDouble(data[1]) ;
                Point2D p2 = new Point2D(vertices[i][0],vertices[i][1]) ;
                //StdDraw.circle(p2.x(), p2.y(), 0.01);
                AllPoint[i] = p2;
            }    
        }
            Point2D target = new Point2D(0.1354339200, 0.7019446863);
            Point2D [] ans = new Point2D[20];
            FindNeighbors fn = new FindNeighbors();
            fn.init(AllPoint);
            ans = fn.query(target, 20);
            for(int i = 0; i < ans.length;i++)
            {
                System.out.println(ans[i].x());
            }
     }
}
