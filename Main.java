import java.util.Scanner;

public class Main {

    static public Scanner sc = new Scanner(System.in);

    static public int algoIDParser(){
        try{
            int num = sc.nextInt();
            if (num ==1 || num == 2)
                return num;
            else {
                System.out.println("Sorry, invalid input. Must input the integer 1 or 2. EXITING...");
                System.exit(1);
            }
        }
        catch (Exception e){
            System.out.println("Sorry, invalid input. Must input the integer 1 or 2. EXITING...");
            System.exit(1);
        }
        return 0;
    }


    public static void main(String args[]){

        System.out.println("Please choose a simulator for Leader Election Algorithm Demo:");
        System.out.println("1) LCR simulator\t2) HS simulator");

        int algoID;
        do {
            algoID = algoIDParser();
        }while (algoID == 0);

       if(algoID == 1){
           Ass1LCRSimulator.LCRSimulator();
       }
       if(algoID == 2) {
           Ass1HSSimulator.HSSimulator();
       }

        //

    }
}
