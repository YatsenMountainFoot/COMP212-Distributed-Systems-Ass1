import java.util.ArrayList;
import java.util.Random;

enum Status{ UNKNOWN, LEADER}
enum IDAssType{ RANDOM, ASCENDING, DESCENDING}

public class Processor {

    //Processor data
    private int myID;
    private int sndID;
    private int phase = 0;
    private Status myStatus;
    private Message preMessage;
    private Message nxtMessage;
    private Processor previousProcessor;
    private Processor nextProcessor;

    //HS part data
    private Message sendClock;
    private Message sendCounterClock;

    public Processor(){}
    public Processor(int ID){
        myID = ID;
        myStatus = Status.UNKNOWN;
        previousProcessor = null;
        nextProcessor = null;

        //LCR Message to be send stored here
        sndID = myID;
        //LCR & HS incoming Message stored here
        preMessage = new Message();
        nxtMessage = new Message();
        //HS Message to be SEND stored here
        sendClock = new Message(myID, Direction.OUT, 1);
        sendCounterClock = new Message(myID, Direction.OUT, 1);

    }

    //Processor method
    //set data
    public void setMyID(int ID){ myID = ID; }
    public void setSndID(int ID){ sndID = ID; }
    public void setMyStatus(Status status){ myStatus = status; }
    public void setPreviousProcessor(Processor pre){ previousProcessor = pre; }
    public void setNextProcessor(Processor nxt){ nextProcessor = nxt; }
    public void setPreMessage(int ID){ preMessage.setID(ID); }
    public void setNxtMessage(int ID){ nxtMessage.setID(ID);}
    //get data
    public int getMyID(){ return myID; }
    public int getSndID(){ return sndID; }
    public Status getMyStatus(){ return myStatus; }

    public Message getPreMessage(){ return preMessage;}
    public Message getNxtMessage() { return nxtMessage; }

    public void incPhase(){ phase++; }
    public int getPhase() { return phase; }


    public void sendLCRMessage(){

            if(previousProcessor.sendClock != null){
                preMessage = previousProcessor.sendClock;
                previousProcessor.sendClock = null;
                Message.incNumOfMsg();
            }
            //nextProcessor.setPreMessage(getSndID());
            if(preMessage != null)
                System.out.format("%-12s%-1s%-12s%-1s%-12s\n", myID, "|", preMessage.getID(), "|", myStatus);
            else
                System.out.format("%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus);


    }

    public void exeLCRProcess(){

        if(Message.getRound() > 1){
            if(preMessage != null) {
                if (preMessage.getID() > myID) {
                    sendClock = preMessage;
                    preMessage = null;
                }
                else if (preMessage.getID() == myID) {
                    setMyStatus(Status.LEADER);
                    preMessage = null;
                }
                else
                    preMessage = null;
            }
        }
        if(sendClock != null)
            System.out.format("%-12s%-1s%-12s%-1s%-12s\n", myID, "|", sendClock.getID(), "|", myStatus);
        else
            System.out.format("%-12s%-1s%-12s%-1s%-12s\n", myID, "|", null, "|", myStatus);
    }


    public void sendHSMessage(){
        if(previousProcessor.sendClock != null){
            this.preMessage = previousProcessor.sendClock;
            previousProcessor.sendClock = null;
            Message.incNumOfMsg();
        }
        if(nextProcessor.sendCounterClock != null){
            this.nxtMessage = nextProcessor.sendCounterClock;
            nextProcessor.sendCounterClock = null;
            Message.incNumOfMsg();
        }
        //send sendClock to clockwise neighbour


        System.out.format( "%-12s%-1s", myID, "|");
        if(preMessage != null)
            System.out.format( "%-12s%-12s%-12s%-1s", preMessage.getID(), preMessage.getDirection(), preMessage.getHopCount(), "|");
        else
            System.out.format("%-12s%25s", "null", "|");

        if(nxtMessage == null)
            System.out.format("%-12s%25s", "null", "|");
        else
            System.out.format( "%-12s%-12s%-12s%-1s", nxtMessage.getID(), nxtMessage.getDirection(), nxtMessage.getHopCount(), "|");
        System.out.format("%-12s%-1s%-12s\n", phase, "|", myStatus);

    }


    public void exeHSProcess(){
        //when dir == OUT process Message from counterClockwise neighbour
        if(preMessage != null) {
            if (preMessage.getDirection() == Direction.OUT) {

                if (preMessage.getID() > myID && preMessage.getHopCount() > 1) {
                    sendClock = preMessage;
                    sendClock.setHopCount(preMessage.getHopCount() - 1);
                    preMessage = null;

                } else if (preMessage.getID() > myID && preMessage.getHopCount() == 1) {

                    sendCounterClock = preMessage;
                    sendCounterClock.setDirection(Direction.IN);
                    preMessage = null;

                } else if (preMessage.getID() == myID)

                    setMyStatus(Status.LEADER);

            }
        }
        //when dir == OUT, process Message from clockwise neighbour
        if(nxtMessage != null) {
            if (nxtMessage.getDirection() == Direction.OUT) {
                if (nxtMessage.getID() > myID && nxtMessage.getHopCount() > 1) {

                    sendCounterClock = nxtMessage;
                    //sendCounterClock.setID(nxtMessage.getID());
                    //sendCounterClock.setDirection(Direction.OUT);
                    sendCounterClock.setHopCount(nxtMessage.getHopCount() - 1);
                    nxtMessage = null;


                } else if (nxtMessage.getID() > myID && nxtMessage.getHopCount() == 1) {

                    sendClock = nxtMessage;
                    sendClock.setDirection(Direction.IN);
                    nxtMessage = null;

                } else if (nxtMessage.getID() == myID)
                    setMyStatus(Status.LEADER);
            }
        }
        //pass without check when IN && different id
        if(preMessage != null) {
            if (preMessage.getDirection() == Direction.IN) {
                if (preMessage.getHopCount() == 1 && preMessage.getID() != myID) {
                    sendClock = preMessage;
                    preMessage = null;

                }
            }
        }
        if(nxtMessage != null) {
            if (nxtMessage.getDirection() == Direction.IN) {
                if (nxtMessage.getHopCount() == 1 && nxtMessage.getID() != myID) {
                    sendCounterClock = nxtMessage;
                    nxtMessage = null;

                }
            }
        }
        //IN with both inID the same

        if(preMessage != null && nxtMessage != null) {
            if (preMessage.getDirection() == Direction.IN && nxtMessage.getDirection() == Direction.IN) {
                if (preMessage.getHopCount() == 1 && preMessage.getHopCount() == 1) {
                    if (preMessage.getID() == myID && nxtMessage.getID() == myID) {

                            incPhase();
                            sendClock = nxtMessage;
                            sendClock.setDirection(Direction.OUT);
                            sendClock.setHopCount((int) Math.pow(2, getPhase()));

                            sendCounterClock = preMessage;
                            sendCounterClock.setDirection(Direction.OUT);
                            sendCounterClock.setHopCount((int) Math.pow(2, getPhase()));

                            preMessage = null;
                            nxtMessage = null;

                    }
                }
            }
        }
        preMessage = null;
        nxtMessage = null;
        System.out.format( "%-12s%-1s", myID, "|");
        if(sendClock != null)
            System.out.format( "%-12s%-12s%-12s%-1s", sendClock.getID(), sendClock.getDirection(), sendClock.getHopCount(), "|");
        else
            System.out.format("%-12s%25s", null, "|");

        if(sendCounterClock == null)
            System.out.format("%-12s%25s", "null", "|");
        else
            System.out.format( "%-12s%-12s%-12s%-1s", sendCounterClock.getID(), sendCounterClock.getDirection(), sendCounterClock.getHopCount(), "|");
        System.out.format("%-12s%-1s%-12s\n", phase, "|", myStatus);

    }



    public static ArrayList<Processor> ringConstructor(int numOfProcessors){

        ArrayList<Processor> processorList = new ArrayList<Processor>();
        ArrayList<Integer> IDHolderList = new ArrayList<Integer>();

        Random rand = new Random();

        //construct an arrayList of Processors with unique IDs

            for (int i = 0; i < numOfProcessors; i++) {
                boolean uniqueIDFlag = true;
                int n;
                do {
                    uniqueIDFlag = true;
                    n = rand.nextInt(3 * numOfProcessors) + 1;
                    for (int counter = 0; counter < processorList.size(); counter++) {
                        if (processorList.get(counter).getMyID() == n)
                            uniqueIDFlag = false;
                    }
                } while (uniqueIDFlag == false);

                Processor p = new Processor(n);
                processorList.add(p);

            }



        System.out.println("----------------------------");
        //construct a bidirectional ring of Processors
        for (int counter = 0; counter < processorList.size(); counter++) {
            if(counter < processorList.size()-1)
                processorList.get(counter).setNextProcessor(processorList.get(counter+1));
            else
                processorList.get(counter).setNextProcessor(processorList.get(0));
            if(counter > 0)
                processorList.get(counter).setPreviousProcessor(processorList.get(counter-1));
            else
                processorList.get(counter).setPreviousProcessor(processorList.get(processorList.size()-1));
        }

        return processorList;
    }

}
