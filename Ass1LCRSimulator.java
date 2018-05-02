import java.util.*;

public class Ass1LCRSimulator {

    static public Scanner sc = new Scanner(System.in);

    static public int intParser(){
        try{
            int num = sc.nextInt();
            if (num < 30000 && num > 0)
                return num;
            else {
                System.out.println("Sorry, invalid input. Must input an integer between 1-29999.");
                return 0;
            }
        }
        catch (Exception e){
            System.out.println("Sorry, invalid input. Must input an integer between 1-29999.");
            System.exit(1);
        }
        return 0;
    }



    public static void LCRSimulator(){
        int leaderID = 0;
        int leaderRound = 0;
        int leaderMessageSent = 0;
        //request num of processors
        System.out.println("Please enter the number of processors to execute the LCR algo: ");
        int numOfProcessors;
        do {
            numOfProcessors = intParser();
        }while (numOfProcessors == 0);

        //construct a bi-dir ring ADT
        ArrayList<Processor> processorList = Processor.ringConstructor(numOfProcessors);

        //LCR leader election
        boolean leaderFlag = false;
        do {
            System.out.println("----------------------------After LCRProcess in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-1s%-12s\n", "MyID", "|" , "IDtoSend", "|", "Status");
            for (int counter = 0; counter < processorList.size(); counter++) {
                processorList.get(counter).exeLCRProcess();
                if (processorList.get(counter).getMyStatus() == Status.LEADER) {
                    leaderFlag = true;
                    leaderID = processorList.get(counter).getMyID();
                    leaderRound = Message.getRound();
                    leaderMessageSent = Message.getNumOfMsgSent();
                    //System.out.println("The leaderID is " + processorList.get(counter).getMyID() + " Round " + Message.getRound());
                }
            }

            System.out.println("----------------------------After LCRMessage sent in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-1s%-12s\n", "MyID", "|" , "receivedID", "|", "Status");
            for (int counter = 0; counter < processorList.size(); counter++) {
                processorList.get(counter).sendLCRMessage();
            }

            Message.incRound();
        }while(leaderFlag == false);

        System.out.println("The leader ID is: " + leaderID + " elected in round: " + leaderRound + " with number of sent message: " + leaderMessageSent);

    }

}
