import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Ass1HSSimulator {
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




    public static void HSSimulator(){
        int leaderID = 0;
        int leaderRound = 0;
        int leaderMessageSent = 0;
        int leaderPhase = 0;
        //request num of processors
        System.out.println("Please enter the number of processors to execute the HS algo: ");
        int numOfProcessors;
        do {
            numOfProcessors = intParser();
        }while (numOfProcessors == 0);

        //construct a bi-dir ring ADT
        ArrayList<Processor> processorList = Processor.ringConstructor(numOfProcessors);

        //LCR leader election
        boolean leaderFlag = false;
        do {
         //for(int i = 0; i < 10; i++) {
            System.out.println("----------------------------After HSProcess in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-12s%-12s%-1s%-12s%-12s%-12s%-1s%-12s%-1s%-12s\n", "Processor", "|" , "toSendCLK ","Direction", "HopCount ", "|", "toSendCCW", "Direction", "HopCount", "|", "ProcPhase", "|", "MyStatus");
            for (int counter = 0; counter < processorList.size(); counter++) {
                processorList.get(counter).exeHSProcess();
                if (processorList.get(counter).getMyStatus() == Status.LEADER) {
                    leaderFlag = true;
                    leaderID = processorList.get(counter).getMyID();
                    leaderRound = Message.getRound();
                    leaderPhase = processorList.get(counter).getPhase();
                    leaderMessageSent = Message.getNumOfMsgSent();

                    //System.out.println("The leaderID is " + processorList.get(counter).getMyID() + " Produced in Phase " + processorList.get(counter).getPhase()+ " Round " + Message.getRound());
                }
            }

            System.out.println("----------------------------After message sent in round " + Message.getRound());
            System.out.format("%-12s%-1s%-12s%-12s%-12s%-1s%-12s%-12s%-12s%-1s%-12s%-1s%-12s\n", "Processor", "|" , "receivedCLK","Direction", "HopCount", "|", "ReceivedCCW", "Direction", "HopCount", "|", "ProcPhase","|","MyStatus");
            for (int counter = 0; counter < processorList.size(); counter++) {
                processorList.get(counter).sendHSMessage();
            }

            Message.incRound();
           // }
            }while(leaderFlag == false);

            System.out.println("The leader ID is: " + leaderID + " elected in Phase: " + leaderPhase + " round:" + leaderRound + " with number of sent message: " + leaderMessageSent);
    }
}
