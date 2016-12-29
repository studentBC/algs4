
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CHIN LUNG
 */
public class Clustering {

    protected static Point2D[] AllPoint;
    protected static double[][]vertices;
    protected static MinPQ PQ = new MinPQ();
    protected static MinPQ mp = new MinPQ();
    protected static LinkedList<cluster> Cluster = new LinkedList<cluster>() ;
    protected static LinkedList<cluster> record = new LinkedList<cluster>() ;
     protected static LinkedList<cluster> copy = new LinkedList<cluster>() ;
    //protected static double [] distance ;
    protected static Set<cluster> out = new HashSet<cluster>();
   // protected static LinkedList<cluster> newcluster = new LinkedList<cluster>();
    
    
    public static class cluster implements Comparable<cluster>
    {       

        private Point2D Centroid ;       
        public  LinkedList<Point2D> allpoint = new LinkedList<Point2D>() ;
        //public  LinkedList<cluster> allcluster = new LinkedList<cluster>() ;
        public boolean IsClusted = false;
        
        public cluster(cluster [] points)  //將得到cluster的所有點都merge
        {
            for(int i = 0; i < points.length ; i++)
            {
                for(int j = 0; j < points[i].size() ;j++)
                {
                    allpoint.add(points[i].allpoint.get(j));
                }
            }
        }

        public cluster(Point2D  point)
        {
            allpoint.add(point) ;
        }
        
        public  Point2D GetCentroid()
        {   double x = 0;double y = 0; 
            for(int i = 0; i < allpoint.size() ;i++)
            {
                x+=allpoint.get(i).x();
                y+=allpoint.get(i).y();
            }
            x = x/allpoint.size(); y = y/allpoint.size();
            Centroid = new Point2D(x,y);
            
            return Centroid;
        }
        // compute distance between two cluster 
        public double clusterToCluster(cluster that)
        {
          return  this.GetCentroid().distanceTo(that.GetCentroid());
        }
       
        public int size() 
        {
            return allpoint.size();
        }
        
         public boolean IsClusted()
         {
             if(this.allpoint.size() >=2)
             {
                 return true;
             }
             else
             {
                 return false;
             }
         }

        /**
         *
         * @param t
         * @return
         */
        @Override
        public int compareTo(cluster t) {
            if(this.allpoint.size() > t.allpoint.size())
            {
                return 1;
            }
            else if(this.allpoint.size() == t.allpoint.size())
                    {
                        return 0;
                    }
            else{
                return -1;
            }
        }
    }
    
    public static class Pair implements Comparable<Pair> {
        
        private cluster []clusters = new cluster[2];
        public double  distance ;
        public boolean isover = true ;
        
        public Pair(cluster c1, cluster c2)
        {
            clusters[0] = c1;clusters[1] = c2;
            distance = c1.GetCentroid().distanceTo(c2.GetCentroid());
            c1.Centroid = c1.GetCentroid();
            c2.Centroid = c2.GetCentroid();
        }
        //怎麼知道有些pair需要忽略??如果他的重心改變過??看他的距離有無重複過
  public boolean IsClusted(LinkedList<cluster> rc )
  {           //pair 內重複的距離
        boolean flag = true; int a = 0;
      if(clusters[0].size() == 1 && clusters[1].size() == 1) 
      {       
          for(int i = 0; i < 2 ; i++)
               {
                for(int j = 0; j < this.clusters[i].size();j++)
                   {
                       for(int k = 0; k < rc.size() ;k++)
                       {
                            if(this.clusters[i].allpoint.get(j).equals(rc.get(k).GetCentroid()))  
                            { 
                                if(rc.get(k).IsClusted == true)
                                { 
                                    flag = true;break;
                                }
                                else{a++;break;}
                            }
                       }
                    }
                }
                    if(a == 2) { flag = false;}
      }
      
     else if(clusters[0].size() == clusters[1].size() && clusters[1].size() >1 ) 
      {       
         if(clusters[0].GetCentroid().equals( clusters[1].GetCentroid()) != true )
         {
             flag = false;
         }
      }
     else if(clusters[0].size() >=2 && clusters[1].size() >=2)
     {
         if(clusters[0].GetCentroid().equals(clusters[1].GetCentroid()) != true)
         {
             flag = false;
         }
     }
     
      else {
               for(int i = 0; i < 2 ; i++)
               {
                       for(int k = 0; k < rc.size() ;k++)
                       {
                            if(this.clusters[i].GetCentroid().equals(rc.get(k).GetCentroid()))  
                            { 
                                if(rc.get(k).IsClusted == true)
                                { 
                                    flag = true;break;
                                }
                                else
                                {
                                    a++;break;
                                }
                            }
                       }
                    
                }
               if(a == 1) { flag = false;}
        }
            
            return flag;
        }
               
        
        
        public cluster[] Clusters()
        {
            return clusters;
        }
        public int pairsize()
        {
            return clusters[0].size()+clusters[1].size();
        }

  
    public int compareTo(Pair that) {
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


    
        public static void UpdatePair(cluster merged , LinkedList<cluster> clusters)
        {   //distance = new double [merged.size() * clusters.size()];int a =0;
         
            merged.IsClusted = true; 
            
            for(int j = 0; j < clusters.size() ;j++)
            {
                    Pair target = new Pair(merged,clusters.get(j));
                    PQ.insert(target);
            }

        }
        public static boolean judge(Pair m , LinkedList<cluster> c)
        {    int a = 0;
            for(int i = 0; i < c.size() ;i++)
            { 
                if(m.clusters[0].GetCentroid().equals(m.clusters[1].GetCentroid()) != true )
                {  
                    if(m.clusters[0].GetCentroid().equals(c.get(i).GetCentroid()))
                    {
                        a++;
                    }
                    if(m.clusters[1].GetCentroid().equals(c.get(i).GetCentroid()))
                    {
                        a++;
                    }
                }
            }
            if(a==2){return true;}
            else{
                return false;
            }
        }
    
    
    
    public static void main(String[] args)throws Exception {
            try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {  //to amend when upload
            int Totalpoints = Integer.parseInt(br.readLine().trim());
            vertices = new double[Totalpoints][2]; AllPoint = new Point2D[Totalpoints];//Cluster = new cluster[Totalpoints];
            // 1. read in the file containing N 2-dimentional points
            for(int i = 0 ; i < Totalpoints;i++)
            {
                String[] data= br.readLine().split(" ");
                vertices[i][0] = Double.parseDouble(data[0]) ;
                vertices[i][1] = Double.parseDouble(data[1]) ;
                Point2D p2 = new Point2D(vertices[i][0],vertices[i][1]) ;
                //StdDraw.circle(p2.x(), p2.y(), 0.01);
                AllPoint[i] = p2;
                cluster c = new cluster(p2);
                Cluster.add(c) ;//let point become clust individually
                record.add(c);  
            }    
        }
            
            // get every points generated pair
            
            for(int i = 0 ; i < Cluster.size();i++)
            {
                for(int j = i+1; j < Cluster.size();j++)
                {
                    Pair target = new Pair(Cluster.get(i),Cluster.get(j));
                    PQ.insert(target);
                }
            } 
            
            
            
            Pair merge; cluster merged  ;Pair[] outcome = new Pair[3];cluster newmerge;
             Set goal=new HashSet();  
             
            while( Cluster.size() > 3 )
            {               
                
               merge = (Pair)PQ.delMin();               
   
               while(merge.IsClusted( record  ) && PQ.isEmpty()!=true) //弄出"沒有做記號"要merge的那一組
               {                      
                   merge = (Pair)PQ.delMin();  
                   
                    if(PQ.isEmpty() && Cluster.size() > 3)
                    {
                        for(int i = 0 ; i < copy.size();i++)
                        {
                            for(int j = i+1; j < copy.size();j++)
                            {
                                Pair target = new Pair(copy.get(i),copy.get(j));
                                PQ.insert(target);
                            }
                        } 
                    }
               }
               
               
               
                              // delete merged point in clusters
               for(int i = 0; i < merge.clusters.length;i++)
               {
                   for(int j = 0; j < Cluster.size() ;j++)
                   {
                       if(merge.clusters[i].GetCentroid().equals(Cluster.get(j).GetCentroid()))
                       {
                           Cluster.remove(j); break;                           
                       }
                   }
               }       
               
               merged = new cluster(merge.Clusters()); //將那一組真的merge 產生新的cluster
             
               // delete merged point in clusters
//               for(int i = 0; i < merged.size();i++)
//               {
//                   for(int j = 0; j < Cluster.size() ;j++)
//                   {
//                       if(merged.allpoint.get(i).equals(Cluster.get(j).GetCentroid()))
//                       {
//                           Cluster.remove(j); break;                           
//                       }
//                       if(merged.GetCentroid().equals(Cluster.get(j).GetCentroid()))
//                       {
//                           Cluster.remove(j); break;                           
//                       }
//                   }
//               }               
                for(int i = 0; i < merged.size();i++)
                {
                   for(int j = 0; j < record.size() ;j++)
                   {
                          
                        if(merged.allpoint.get(i).equals(record.get(j).GetCentroid()))
                        {
                            record.get(j).IsClusted = true;break;
                        }
                        if(merged.GetCentroid().equals(record.get(j).GetCentroid()))
                        {
                            record.get(j).IsClusted = true;break;
                        }
                   }
                } 
                newmerge = new cluster(merged.GetCentroid()); //新的merge point 誕生!!!
                
                newmerge.IsClusted = true;
                record.add(newmerge);               
                Cluster.add(newmerge);
                copy.add(merged);
                UpdatePair( merged , Cluster) ;  // update clusters 新的cluster和舊式cluster的關係( 加入new pair
                //有些pair沒加進去??!!!因為你寫的update船進去的是 cluster!!!所以根本不會有3對3組合乾
                
  
               
                
            }
              for(int i = 0; i < record.size() ;i++)
              {
                  if(record.get(i).IsClusted == false){ copy.add(record.get(i));}
              }
            cluster [] temp = new cluster[3];int a = 0;
            

            for(int b = 0; b < Cluster.size() ;b++)
            {
                for(int c = 0; c < copy.size() ;c++)
                {
                    if(Cluster.get(b).GetCentroid().equals(copy.get(c).GetCentroid()))
                    {
                        temp[a] = copy.get(c);a++;
                    }
                }
            }
            
            
              
            Arrays.sort(temp);


            
            //還沒考慮x order y order的輸出
            
            System.out.println(temp[temp.length -1 ].size() +" " +String.format("%.2f", temp[temp.length -1 ].GetCentroid().x())+" " + String.format("%.2f", temp[temp.length -1 ].GetCentroid().y()));
            System.out.println(temp[temp.length -2 ].size() +" " + String.format("%.2f",temp[temp.length -2 ].GetCentroid().x() )+" " + String.format("%.2f",temp[temp.length -2 ].GetCentroid().y()));
            System.out.println(temp[0].size() +" " + String.format("%.2f",temp[0].GetCentroid().x() )+" " + String.format("%.2f",temp[0].GetCentroid().y()));
            

        }

}
 //　還沒解決　ｐａｉｒ出現　一個ｍｅｒｇｅ過後的重心　　ｖｓ　另一個ｍｅｒｇｅ過後的重心　＋　輸出格式問題