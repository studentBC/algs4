
import java.util.Arrays;



/**
 *
 * @author CHIN LUNG
 */
public class Hand implements Comparable<Hand> {

    // sorted by Card value are recommended but not necessary
    private Card[] cards = new Card[5];
    public int[] Points = new int[5];
    public int[] Points1 = new int[5];
    public int[] Points2 = new int[5];
//    MaxPQ PQ = new MaxPQ();
    
    // TODO, Judge System will call this constructor once for each hand
    public Hand(Card[] cards){
        
        this.cards = cards;
        
    }

    // TODO
  public int compareTo(Hand that) {
  int outcome =0;
         //得到牌的所有數字
        for (int i = 0; i < 5; i++) {
            Points1[i] = GetPoint(this.cards[i]);
	    Points2[i] = GetPoint(that.cards[i]);	
        }
        Arrays.sort(Points1);
	Arrays.sort(Points2);
        if (Compare(this) > Compare(that)) {
            outcome = 1;
        } else if (Compare(this) < Compare(that)) {
            outcome = -1;
        } else if(Compare(this) == Compare(that)){
            if(Compare(this) == 3 ) //straight 
		{ 
                    if(straight(Points1)  > straight(Points2))
                    {
                        outcome = 1;
                    }
                    else if(straight(Points1)== straight(Points2))
                    {
                        outcome = 0;
                    }
                    else outcome = -1;
                }

           
             if(Compare(this) == 5)  //full house
                {
                     if( fullhouse(Points1) > fullhouse(Points2) )
                    {
                        outcome = 1;
                    }
                    else if(fullhouse(Points1) == fullhouse(Points2))
                    {
                        outcome = 0;
                    }
                    else outcome = -1;
                }
             if(Compare(this) == 2 && Compare(that)==2)  //two pair
                {
                     if( twopair(Points1) > twopair(Points2) )
                    {
                        outcome = 1;
                    }
                    else if(twopair(Points1) == twopair(Points2))
                    {
                        outcome = 0;
                    }
                    else outcome = -1;
                }
//              //  if(compare(this) == 1)  //one pair
                 if(Compare(this) == 1 && Compare(that) == 1)
                {
                     if( onepair(Points1) > onepair(Points2) )
                    {
                        outcome = 1;
                    }
                    else if(onepair(Points1) == onepair(Points2))
                    {
                        outcome = 0;
                    }
                    else outcome = -1;
                }
                 if(Compare(this) == 4 || Compare(this) == 0)  //high card and flush
                 {
                      if( highfulshcard(Points1) > highfulshcard(Points2) )
                    {
                        outcome = 1;
                    }
                    else if(highfulshcard(Points1) == highfulshcard(Points2))
                    {
                        outcome = 0;
                    }
                    else outcome = -1;
                 }
            
        }
        return outcome ;
    }

      // Do not modified this function
    public Card[] getCards() { return this.cards; }
     public int GetPoint(Card c) {
        int face = 0;
        switch (c.getFace()) {
            case "A":
                face = 1;
                break;
            case "2":
                face = 2;
                break;
            case "3":
                face = 3;
                break;
            case "4":
                face = 4;
                break;
            case "5":
                face = 5;
                break;
            case "6":
                face = 6;
                break;
            case "7":
                face = 7;
                break;
            case "8":
                face = 8;
                break;
            case "9":
                face = 9;
                break;
            case "10":
                face = 10;
                break;
            case "J":
                face = 11;
                break;
            case "Q":
                face = 12;
                break;
            case "K":
                face = 13;
                break;
        }
        return face;
    }

   
      public int Compare(Hand that) {
        int count = 0; 
        int Pair = 0;
        //sort the suit first  check if it is flush first
        Arrays.sort(that.cards, Card.SUIT_ORDER);
        for (int i = 0; i < 4; i++) {
            if (Card.SUIT_ORDER.compare(that.cards[i], that.cards[i + 1]) == 0) //flush
            {
                count++;
                if (count == 4) {
                    return 4;
                }
            }
        }
        count = 0;
        //得到牌的所有數字
        for (int i = 0; i < 5; i++) {
            Points[i] = GetPoint(that.cards[i]);
        }
        Arrays.sort(Points);
        for (int i = 0; i < 4; i++) //straight
        {
            if (Points[i] == Points[i + 1] - 1) {
                count++;
            }
        }
        if (count == 4) {
            return 3;
        }
        if(Points[0]==1 && Points[1] == 10 && Points[2] == 11 &&Points[3] == 12 &&Points[4] == 13)
        {
            return 3;
        }
        if(Points[0]==1 && Points[1] == 2 && Points[2] == 11 &&Points[3] == 12 &&Points[4] == 13)
        {
            return 3;
        }
        if(Points[0]==1 && Points[1] == 2 && Points[2] == 3 &&Points[3] == 12 &&Points[4] == 13)
        {
            return 3;
        }
        if(Points[0]==1 && Points[1] == 2 && Points[2] == 3 &&Points[3] == 4 &&Points[4] == 13)
        {
            return 3;
        }
        count = 0;
        // one pair two pair full house
        for (int i = 0; i < 4; i++) {
            if (Points[i] == Points[i + 1]) //one pair
            {
                Pair++;
            } else {
                Pair--;
            }

        }
        if (Pair == 2) {  //fullhouse
            return 5;
        } else if (Pair == 0) { //two pair
            return 2;
        } else if (Pair == -2) { //one pair
            return 1;
        } else {
            return 0;
        }
    }
    
    
    public int fullhouse(int [] c)
    { int count = 0; int flag = 0;

        for(int i = 0; i < 4; i++)
        {
            if(c[i] == c[i+1])
            {
                count++;
                if(count == 2)
                {
                    if(c[i] == 1)
                    {
                        flag = 100;
                    }
                    else flag = c[i];
                   break;
                }
            }
            else
            {
                count--;
            }
        
        }
        return flag;
    }

    
    
     public int onepair(int [] c)
    { int count = 0; int flag = 0;
        for(int i = 0; i < 4; i++)
        {
            if(c[i] == c[i+1])
            {
                count++;
                
                if(count == 1)
                {
                     if(c[i] == 1)
                    {
                        flag = 100;
                    }
                     else flag = c[i];
                   break;
                }
            }
        }
        return flag;
    }
     
      public int twopair(int [] c)
    { int count = 0; int flag = 0;
        for(int i = 0; i < 4; i++)
        {
            if(c[i] == c[i+1] )
            {
                count++;
                if(count == 2)
                {
                   flag = c[i];
                   break;
                }
            }
             if(c[i] == c[i+1] && c[i] == 1)
            {
                flag = 100;break;
            }
        }
        return flag;
    }
      public int highfulshcard(int [] c)
      {         
              if(c[0] == 1)
              {
                  return 100;
              } 
              else{
                  return c[4];
              }
      }
      public int straight(int []c )
      {
        if(Points[0]==1 && Points[1] == 10 && Points[2] == 11 &&Points[3] == 12 &&Points[4] == 13)
        {
            return 100;
        }
        if(Points[0]==1 && Points[1] == 2 && Points[2] == 11 &&Points[3] == 12 &&Points[4] == 13)
        {
            return 100;
        }
        if(Points[0]==1 && Points[1] == 2 && Points[2] == 3 &&Points[3] == 12 &&Points[4] == 13)
        {
            return 100;
        }
        if(Points[0]==1 && Points[1] == 2 && Points[2] == 3 &&Points[3] == 4 &&Points[4] == 13)
        {
            return 100;
        }
          else{
              return c[4];
          }
      }
}

