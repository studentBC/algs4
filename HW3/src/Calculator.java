import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;


/**
 *
 * @author CHIN LUNG
 */
public class Calculator {
  
    public Double ans(String e)
    {
       Stack<String> ops = new Stack<String>();
       Stack<Double> vals = new Stack<Double>();
       String data[] = new String[e.split(" ").length];//將得到的字串先存下來
        data =e.split(" ").clone();
    
       //先將前序轉為後序
       for(int i = 0; i < data.length;i++)
       {
               if(data[i].equals("("));
               else if(data[i].equals("+")) ops.push(data[i]);
               else if(data[i].equals("-")) ops.push(data[i]);
               else if(data[i].equals("*")) ops.push(data[i]);
               else if(data[i].equals("/")) ops.push(data[i]);
               else if(data[i].equals(")"))
               {
                  String op = ops.pop();
                  if(op.equals("+")) vals.push(vals.pop() + vals.pop());
                  else if(op.equals("-")) vals.push(-vals.pop() + vals.pop());
                  else if(op.equals("*")) vals.push(vals.pop() * vals.pop());
                  else if(op.equals("/")) vals.push(1/vals.pop() * vals.pop());
               }
               else vals.push(Double.parseDouble(data[i]));
           }
		return vals.pop();
    }
    
//    public int priority(String op)
//    {
//        switch(op)
//        {
//            case"(":
//                return 1;
//            case"+":
//            case"-":
//                return 2;
//            case "*":
//            case"/":
//                return 3;
//            default : return 0;    
//        }
//    }
   
    public static void main(String[] args) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String number = br.readLine();
            Calculator cct = new Calculator();
            System.out.println(cct.ans(number));
        }
    }
}
