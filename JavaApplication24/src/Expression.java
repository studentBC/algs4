
import java.io.BufferedReader;
import java.io.FileReader;



/**
 * @author CHIN LUNG
 */
public class Expression {
    private Node root;
    private static Stack<Node> operator = new Stack<Node>();
    private static Stack<Node> values = new Stack<Node>();
    private  int N;           // number of key-value pairs in the B-tree
    private  Node[] outcome;
    private double answers;
    // DO NOT MODIFY THIS
    public Expression(){}

    // Build a Binary and Return the Root
    public Node Infix2BT(String infix){
        root = scanf(infix);outcome = new Node[N];
        answers = ans(infix);
        return(root);
    }

    
    //中 左 右 traversal
    int a = 0;
    
    public Node[] preOrder(Node node) {
         if ( node != null ) { 
         outcome[a] = node; a++;     
         preOrder(node.getLeft());  // 走訪左子樹         
         preOrder(node.getRight()); // 走訪右子樹
         }
         return outcome;
   }
    
    public Node[] PrintPrefix(){
        if(root == null)
        {
            throw new NullPointerException();
        }
        else{
                outcome = new Node[N];
        }
        return preOrder(root);
    }
    //左 右 中
    int i = 0;
    
    public Node[] postOrder(Node node) {
      if ( node != null ) {  
         postOrder(node.getLeft());  // 走訪左子樹
         postOrder(node.getRight()); // 走訪右子樹
         outcome[i] = node; i++;
         
      }
      return outcome;
   }
    
    public Node[] PrintPostfix(){
        if(root == null)
        {
             throw new NullPointerException();
        }
        else{
            outcome = new Node[N];
        }     
        return postOrder(root);
    }

    public double Evaluation(){
        if(root == null)
        {
            throw new NullPointerException();
        }       
        Double answer = answers;
                
        return(answer);
    }
    
    //處理百位數十位數千位數小數等等
    public String[] handle(String[] data)
    {
        Queue number = new Queue();
        Queue equation = new Queue();
        String c;
        int count = 0;
        
        for(int i = 0 ; i < data.length;i++)
        {
            if(data[i].equals("(") || data[i].equals("*") || data[i].equals("/") || data[i].equals("+")|| data[i].equals("-") || data[i].equals(")"))
            {
                equation.enqueue(data[i]);
            }
            else
            {
                 number.enqueue(data[i]);
                 if(data[i+1].equals("(") || data[i+1].equals("*") || data[i+1].equals("/") || data[i+1].equals("+")|| data[i+1].equals("-") || data[i+1].equals(")"))
                 {
                     c = number.dequeue().toString();
                     if(number.isEmpty())
                     {
                         equation.enqueue(c);
                     }
                   if(number.isEmpty() != true)
                   {
                        while(number.isEmpty()!= true)
                        {
                            String n= number.dequeue().toString();
                            c +=n;
                        }
                     equation.enqueue(c);
                    }
                 }
            }
            
        }
        String [] out = new String[equation.size()];
        while(equation.isEmpty()!= true)
        {
            out[count] = equation.dequeue().toString();
            count++;
        }
        count=0;
        return out;
        
    }
    
    
    public Node scanf(String e)
    {
        Node ops;
        Node vals ;       
        String data[] = new String[e.length()];//將得到的字串先存下來
        
        for(int i = 0; i < e.length() ;i++)
        {
            data[i] =e.substring(i, i+1);
        }
        String datas[] = handle(data);
        
        
    
       //先將前序轉為後序
       for(int i = 0; i < datas.length;i++)
       {
               if(datas[i].equals("("));
               else if(datas[i].equals("+")){ ops = new Node(null,null,"+");ops.setValue("+");operator.push(ops);N++;}
               else if(datas[i].equals("-")){ ops = new Node(null,null,"-");ops.setValue("-");operator.push(ops);N++;}
               else if(datas[i].equals("*")){ ops = new Node(null,null,"*");ops.setValue("*");operator.push(ops);N++;}
               else if(datas[i].equals("/")) {ops = new Node(null,null,"/");ops.setValue("/");operator.push(ops);N++;}
               else if(datas[i].equals(")")) 
               {
                  Node op = operator.pop();
                  Node right = values.pop();
                  Node left = values.pop();
                  op.setRight(right);    
                  op.setLeft (left);
                  values.push(op);
               }
               else 
               {
                   vals = new Node(null,null,datas[i]);
                   vals.setValue(datas[i]);
                   values.push(vals);
                   N++;
               }
           }
		return values.pop();
    }
    
    
    
    public Double ans(String e)
    {
        Stack<String> ops = new Stack<String>();
        Stack<Double> vals = new Stack<Double>();
        String data[] = new String[e.length()];//將得到的字串先存下來
        for(int i = 0; i < e.length() ;i++)
        {
            data[i] =e.substring(i, i+1);
        }
        String datas[] = handle(data);
       //先將前序轉為後序
       for(int i = 0; i < datas.length;i++)
       {
               if(datas[i].equals("("));
               else if(datas[i].equals("+")) ops.push(datas[i]);
               else if(datas[i].equals("-")) ops.push(datas[i]);
               else if(datas[i].equals("*")) ops.push(datas[i]);
               else if(datas[i].equals("/")) ops.push(datas[i]);
               else if(datas[i].equals(")"))
               {
                  String op = ops.pop();
                  if(op.equals("+")) vals.push(vals.pop() + vals.pop());
                  else if(op.equals("-")) vals.push(-vals.pop() + vals.pop());
                  else if(op.equals("*")) vals.push(vals.pop() * vals.pop());
                  else if(op.equals("/")) vals.push(1/vals.pop() * vals.pop());
               }
               else vals.push(Double.parseDouble(datas[i]));
           }
		return vals.pop();
    }
    
    
        public static void main(String[] args) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String number = br.readLine();
            Expression ee = new Expression();
            ee.Infix2BT(number);
            ee.PrintPostfix();
            System.out.println("\n");
            ee.PrintPrefix();
            System.out.println(ee.Evaluation());
        }
    }
}
