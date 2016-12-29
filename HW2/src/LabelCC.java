
import java.io.FileReader;
import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 * @author CHIN LUNG
 */
public class LabelCC {


    private QuickUnionUF qu;
    private int[][] mark;
    MSD msd ;

    public LabelCC(int N) {
        mark = new int[N+2][N+2];
        qu = new   QuickUnionUF(N*N);
        int count = 0;
        for (int i = 0; i < N+2; i++) {
            for (int j = 0; j < N+2; j++) {
                if((1<=i && i<=N)&&(1<=j && j<=N))
                {
                    mark[i][j] = 1;
                    count++;
                }
                else
                {
                    mark[i][j] = 0;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String[] number = br.readLine().split(",");
            int N = Integer.parseInt(number[0]);
            int targetx = Integer.parseInt(number[1]);
            int targety = Integer.parseInt(number[2]);
            ArrayList<Integer> temp = new ArrayList<>();
            String[] data;
            String line;

            while ((line = br.readLine()) != null) {
                data = line.split(",");
                temp.add(Integer.parseInt(data[0]));
                temp.add(Integer.parseInt(data[1]));
            }

            Integer[] temp2 = temp.toArray(new Integer[temp.size()]);

            LabelCC lcc = new LabelCC(N);
            int label = 1;
            //先將讀到的bolck設為0
            for (int i = 0; i < temp2.length; i += 2) {
                lcc.mark[temp2[i]][temp2[i + 1]] = 0;
            }
            //開始找她的up left 有無鄰近社的點
            for(int x = 1; x < N+1;x++)
            {
                for(int y = 1; y < N+1;y++)
                {
                    if(lcc.mark[x][y] !=0)
                    {
                    if(lcc.mark[x][y] <=lcc.mark[x][y-1])
                    {
                        if(lcc.mark[x-1][y] !=0)
                        {
                            lcc.mark[x][y] = Math.min(lcc.mark[x-1][y], lcc.mark[x][y-1]);
                            lcc.qu.union( Math.max(lcc.mark[x-1][y], lcc.mark[x][y-1]),lcc.mark[x][y]);
                        }
                        else
                        {
                            lcc.mark[x][y] = lcc.mark[x][y-1];
                        }
                    }
                    if(lcc.mark[x][y] <=lcc.mark[x-1][y])
                    {
                        if(lcc.mark[x][y-1] !=0)
                        {
                            lcc.mark[x][y] = Math.min(lcc.mark[x-1][y], lcc.mark[x][y-1]);
                            lcc.qu.union( Math.max(lcc.mark[x-1][y], lcc.mark[x][y-1]),lcc.mark[x][y]);
                        }
                        else
                        {
                            lcc.mark[x][y] = lcc.mark[x-1][y];
                        }
                    }
                    if(lcc.mark[x-1][y]==0 && lcc.mark[x][y-1]==0)
                    {
                        lcc.mark[x][y] = label;
                        label++;
                    }
                    }
                }
            }
           //將union起來的格子做替換
            for(int x = 1; x < N+1;x++)
            {
                for(int y = 1; y <N+1;y++)
                {
                    if(lcc.mark[x][y] !=0)
                    {   
                        lcc.mark[x][y] = lcc.qu.find(lcc.mark[x][y]);
                    }
                }
            }
            //印出屬於哪一群若無則印出0
            System.out.println(lcc.mark[targetx][targety]);
        }
    }
}
