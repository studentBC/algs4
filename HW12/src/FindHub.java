import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;

/**
 *
 * @author CHIN LUNG
 */
public class FindHub {
    
        protected static Point2D[] AllPoint;
        protected static List<Integer> list = new ArrayList<Integer>();
        protected static double[][]vertices;
        protected static int Totalpoints = 0;
        protected static EdgeWeightedGraph EWG;
        protected static Edge edge;
        protected static DirectedEdge dedge;
        protected static PrimMST PMST;
        protected static DijkstraSP dijk;
        protected static EdgeWeightedDigraph EWDG;
        
        public static void main(String[] args)throws Exception {
            try (BufferedReader br = new BufferedReader(new FileReader("2.txt"))) {  //to amend when upload
            Totalpoints = Integer.parseInt(br.readLine().trim());
            //number of vertices
            EWG = new EdgeWeightedGraph(Totalpoints);
            EWDG = new EdgeWeightedDigraph(Totalpoints);
            vertices = new double[Totalpoints][2]; AllPoint = new Point2D[Totalpoints];
            // 1. read in the file containing N 2-dimentional points
            double min = 10;double max = 0;
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
       //construct graph not using k nearest point
            for(int i = 0; i < AllPoint.length;i++)
            {
                for(int j = 0; j < AllPoint.length;j++)
                {
                    edge = new Edge(i,j,AllPoint[i].distanceTo(AllPoint[j]));
                    EWG.addEdge(edge);
                }
            }
            PMST = new PrimMST(EWG);
            //get the spanning tree edge and construct a digraph for dijkstra
            
            int onepoint = 0; int theotherpoint = 0;
            for(Edge edg : PMST.edges())
            {
                onepoint = edg.either();
                theotherpoint = edg.other(edg.either());
                dedge = new DirectedEdge(onepoint,theotherpoint,edg.weight());
                EWDG.addEdge(dedge);
                dedge = new DirectedEdge(theotherpoint,onepoint,edg.weight());
                EWDG.addEdge(dedge);
            }
            
            //get the shortest path
            double total = 0;double d = 100;
            for(int i = 0; i < AllPoint.length ;i++)
            {
               dijk = new DijkstraSP(EWDG,i); 

                for(int j = 0; j < AllPoint.length;j++)
                {
                    total+=dijk.distTo(j);
                }
                
               if(d > total)
               {
                   d = total;
               }
               total = 0;
            }
            
         
       System.out.printf("%5.5f\n", d);     
    }
}
