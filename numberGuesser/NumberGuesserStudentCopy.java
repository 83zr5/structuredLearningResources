//STUDENT STARTER CODE
//Name: 

import java.util.Random;
import java.util.Scanner;

public class NumberGuesser{
    private int targetNum;
    private int userGuess;


    public NumberGuesser(){
        //initialize a guess and a target variables
    }

    public int generateRandomTargetNum(){
        Random rand = new Random();
        int targetNum = rand.nextInt(0, 101);
        return targetNum;
    }

    public int getUserNum(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("enter a number between 0-100 to guess:");
        userGuess = scanner.nextInt();
        return userGuess;
    } 

    
    public int checkGuess(int userGuess, int targetNum){
        if (userGuess > 100 || userGuess < 0){ //case if userGuess is invalid
            return 2;
        } else{
            //create a conditional block that returns an integer
            // based on the comparison of the targetNum and the userGuess
        }
    }


}