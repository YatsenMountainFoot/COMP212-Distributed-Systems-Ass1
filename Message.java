
enum Direction{ OUT, IN}

public class Message {

    //Static rounds & hopCount
    static private int round = 1;
    static private int numOfMsgSent = 0;

    static public void incRound(){ round++; }
    static public int getRound(){ return round; }
    static public void incNumOfMsg(){ numOfMsgSent++; }
    static public int getNumOfMsgSent(){ return  numOfMsgSent; }
    //Message data
    private int sendID;
    private Direction direction = Direction.OUT;        //HS dat initialized for sendClock/sendCounterclock
    private int hopCount = 1;                           //HS dat initialized ...

    //Message method
    public void setID(int ID){ sendID = ID; }
    public int getID(){ return sendID;}
    public void setDirection(Direction dir){ direction = dir;}
    public Direction getDirection(){return direction; }
    public void setHopCount(int hC){ hopCount = hC; }
    public int getHopCount(){ return hopCount; }

    //constructor
    public Message(){}
    public Message(int ID, Direction dir, int hCount){
        sendID = ID;
        direction = dir;
        hopCount = hCount;
    }

}
