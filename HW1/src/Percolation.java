
import java.io.FileReader;
import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.ArrayList;


public class Percolation {

    WeightedQuickUnionUF wuf;
    private boolean[][] grid;
    private int size;
    private int space = 0;
    private int[][] Grid;

    public Percolation(int N) {
        int spaces = N * N;
        wuf = new WeightedQuickUnionUF(spaces + 2);//initializes the WQUUF data structure.
        this.grid = new boolean[N][N];
        int count = 0;
        Grid = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
                Grid[i][j] = count;
                count++;
            }
        }

        //先upper virtual的相連
        for (int a = 0; a < N; a++) {
            wuf.union(N * N, a);
        }
        //lower virtual connect
        for (int b = N * (N - 1); b <= N * N - 1; b++) {
         wuf.union(N * N + 1, b);
         }
    }// end constructor;

    public void open(int i, int j) {

        if (this.grid[i][j] == false) {
            this.grid[i][j] = true;
        }
    }//end open

    public boolean isOpen(int i, int j) {
        return this.grid[i][j];
    }//end isOpen

    public boolean isFull(int i, int j) {
        if (isOpen(i, j)) {
            if (wuf.connected(Grid[i][j], (size * size))) {
                return true;
            }
        }//end for
        return false;
    }//end isFull

    /* 
     * checks if any space on the bottom is full returns true 
     * if there is one false other wise
     */
    public boolean percolates(int N) {
        for (int i = 0; i < N; i++) {
            if (isFull(i, N - 1)) {
                return true;
            }
        }
        return false;
    }//end percolates
    
    public boolean connect(int N)
    {
       return  wuf.connected(N*N, N*N+1);
    }
   

    /* 
     * checks if any space on the bottom is full returns true 
     * if there is one false other wise
     */
    public static void main(String[] args) throws Exception {

        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String number = br.readLine();
            int N = Integer.parseInt(number);
            ArrayList<Integer> temp = new ArrayList<>();
            String[] data;
            String line;

            while ((line = br.readLine()) != null) {
                data = line.split(",");
                temp.add(Integer.parseInt(data[0]));
                temp.add(Integer.parseInt(data[1]));

            }

            Integer[] temp2 = temp.toArray(new Integer[temp.size()]);
            for (int i = 0; i < temp.size(); i++) {
                temp2[i]--;
            }
            int size = N;
            int testedTimes = 1;
            // Percolation perc = new Percolation(size);
            int i = 0;
            int j = 1;

            Percolation perc = new Percolation(size);
            int coordinates[] = new int[size * size + 1];
            for (int a = 0; a < size * size + 1; a++) {
                coordinates[a] = a;
            }

            while (testedTimes != (temp2.length / 2 + 1)) {
                int x = temp2[i];
                int y = temp2[j];
//            int X =temp2[i+2];
//            int Y = temp2[j+2];
                perc.open(x, y);
//            perc.open(X, Y);

                //perc.wuf.union(perc.coordinates[perc.grid[x][y]], perc.coordinates[perc.grid[X][Y]]);
                if (y<size-1) {//right 1
                  
                        if (perc.isOpen(x , y+1) == true) {
                            perc.wuf.union(perc.Grid[x][y], perc.Grid[x][y+1]);
                        }
                    }
                
                if (y> 0) {//left 1
                   
                        if (perc.isOpen(x , y-1) == true) {
                            perc.wuf.union(perc.Grid[x][y], perc.Grid[x][y-1]);
                        }
                    }
                
                if (x>0) {//up 1
                   
                        if (perc.isOpen(x-1, y) == true) {
                            perc.wuf.union(perc.Grid[x][y], perc.Grid[x-1][y]);
                        }
                    }
                
                if (x<size-1) {//down 1
                  
                        if ((perc.isOpen(x+1, y)) == true) {
                            perc.wuf.union(perc.Grid[x][y], perc.Grid[x+1][y]);
                        }
                    
                }

                testedTimes++;
                
                
                                            
                if (perc.connect(N)) {
                    System.out.println((x + 1) + "," + (y + 1));
                    break;
                }
                i += 2;
                j += 2;
            }//end while(TestedTimes<tests)
            if (testedTimes == (temp2.length / 2 + 1) ) 
            {
                if(perc.connect(N)==false)
                {System.out.println("-1");}
            }
            BinarySearchST bs = new BinarySearchST();
            
            //end while(TestedTimes<tests)
        }
    }

}
