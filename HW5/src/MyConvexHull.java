import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.List;
/**
 *
 * @author CHIN LUNG
 */
public class MyConvexHull {
    protected static double[][]vertices;
    protected static double MD;
    protected static Point2D[] AllPoint;
    protected static Point2D[] GroupPoint ;
    protected static Point2D[][] GroupPoints ;
   // protected static int [][]Coordinate;
    protected static WeightedQuickUnionUF wuf;
    protected static Stack<Point2D> hull = new Stack<Point2D>();
        public static int[] ConvexHullVertex(Point2D[] a) {          
        // 回傳ConvexHullVertex的index set，index 請依該點在a陣列中的順序編號：0, 1, 2, 3, 4, ....a.length-1
            // defensive copy
        int N = a.length;    int [] indices ;
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++)
        {  
            points[i] = a[i];
        }

        // preprocess so that points[0] has lowest y-coordinate; break ties by x-coordinate
        // points[0] is an extreme point of the convex hull
        // (alternatively, could do easily in linear time)
        Arrays.sort(points);

        // sort by polar angle with respect to base point points[0],
        // breaking ties by distance to points[0]
        Arrays.sort(points, 1, N, points[0].POLAR_ORDER);

        hull.push(points[0]);       // p[0] is first extreme point

        // find index k1 of first point not equal to points[0]
        int k1;
        for (k1 = 1; k1 < N; k1++)
            if (!points[0].equals(points[k1])) break;
        if (k1 == N) return null;        // all points equal

        // find index k2 of first point not collinear with points[0] and points[k1]
        int k2;
        for (k2 = k1 + 1; k2 < N; k2++)
            if (Point2D.ccw(points[0], points[k1], points[k2]) != 0) break;
        hull.push(points[k2-1]);    // points[k2-1] is second extreme point

        // Graham scan; note that points[N-1] is extreme point different from points[0]
        for (int i = k2; i < N; i++) {
            Point2D top = hull.pop();
            while (Point2D.ccw(hull.peek(), top, points[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points[i]);
        }
        indices = new int [hull.size()];
        Point2D[] temp = new Point2D[indices.length];
        for(int i = indices.length-1; i >=0 ;i--)
        {
            temp[i] = hull.pop();
        }
        //check thier original indices
        for(int i = 0 ; i < temp.length;i++)
        {
            for(int j = 0; j < a.length;j++)
            {
                if(temp[i].equals(a[j]) == true)
                {
                    indices[i] = j;
                }
            }
        }
        
        return indices;
    }
         public static Iterable<Point2D> hull() {
        Stack<Point2D> s = new Stack<Point2D>();
        for (Point2D p : hull) s.push(p);
        return s;
    }

    // check that boundary of hull is strictly convex
    private static boolean isConvex() {
        int N = hull.size();
        if (N <= 2) return true;

        Point2D[] points = new Point2D[N];
        int n = 0;
        for (Point2D p : hull()) {
            points[n++] = p;
        }

        for (int i = 0; i < N; i++) {
            if (Point2D.ccw(points[i], points[(i+1) % N], points[(i+2) % N]) <= 0) {
                return false;
            }
        }
        return true;
    }

         


    public static void main(String[] args)throws Exception {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            MD = Double.parseDouble(br.readLine().trim()) ;
            int Points = Integer.parseInt(br.readLine().trim());
            vertices = new double[Points][2]; AllPoint = new Point2D[Points];int [] parent = new int [Points];
            int [] copyParent = new int [Points];
            // 1. read in the file containing N 2-dimentional points
            for(int i = 0 ; i < Points;i++)
            {
                String[] data= br.readLine().split(" ");
                vertices[i][0] = Double.parseDouble(data[0]) ;
                vertices[i][1] = Double.parseDouble(data[1]) ;
                Point2D p2 = new Point2D(vertices[i][0],vertices[i][1]) ;
                //StdDraw.circle(p2.x(), p2.y(), 0.01);
                AllPoint[i] = p2;
            }
            // 2. create an edge for each pair of points with a distance <= d
            wuf = new WeightedQuickUnionUF(Points);
            for(int i = 0; i < AllPoint.length;i++)
            {
                for(int j = i+1 ; j < AllPoint.length;j++)
                {
                    if(AllPoint[i].distanceTo(AllPoint[j]) <= MD)
                    {
                        wuf.union(i, j);
                    }
                }
            } 
        // 3. find connected components (CCs) with a size >= 3
            int mark = 0;  ;int []count = new int[Points];//共有幾個cc是大於3個ㄉ 
            int Count = 0; int index = 0;
            for(int i = 0; i < Points;i++)
            {
                parent[i] = wuf.find(i);
                copyParent[i] = parent[i];
            }
          GroupPoint = new Point2D[Points];
            for(int i = 0; i < parent.length;i++)
            {  
               for(int j = i; j < copyParent.length;j++)
                {
                    if(parent[i] == copyParent[j])
                    {
                        copyParent[j] = -1;
                        GroupPoint[j]=AllPoint[j];
                        count[i]++;
                    }
                }              
            }

        for(int i = 0; i < count.length;i++)
        {
            if(count[i]>=3)
            {             
                Count++;
            }
        }
        GroupPoints = new Point2D[Count][];
        for(int i = 0; i < count.length;i++)
        {
            if(count[i]>=3)
            {             
                GroupPoints[mark] = new Point2D[count[i]];
                for(int k = index; k < index+count[i];k++)
                {
                    GroupPoints[mark][k-index] = GroupPoint[k];                  
                }
               mark++;  index = count[i];
            }
        }
        // 4. for each CC, find its convex hull vertices by calling ConvexHullVertex(a[])
            int total = 0;
            for(int i = 0; i < Count;i++)
            {
                total+=ConvexHullVertex(GroupPoints[i]).length;
            }
        // 5. count the number of points in N serving as a convex hull vertex, print it
            System.out.println(total);
            }                               
    }
}
