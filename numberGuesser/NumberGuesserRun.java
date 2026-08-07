//STARTER CODE DO NOT EDIT


import java.util.Scanner;

public class NumberGuesserRun {
    
    public static void main(String[] args){

        NumberGuesser N = new NumberGuesser();

        int runInt = 1;
        

        Scanner scanner = new Scanner(System.in);
        System.out.println("To play the number guesser, enter 1. To exit enter 0");
        runInt = scanner.nextInt();
        while (runInt != 1 && runInt != 0){
            System.out.println("Invalid response");
            System.out.println("Enter 1 to play again. To exit, enter 0");
            runInt = scanner.nextInt();
        }

        int guess = 0;
        while(runInt ==1){
            int  response = 10;
            int target = N.generateRandomTargetNum();
            
            while (response != 0){
                guess = N.getUserNum();
                response = N.checkGuess(guess, target);
    
                if(response == 1){
                    System.out.println("Guess is too high!");
                } else if (response == -1){
                    System.out.println("Guess is too low!");
                } else if (response == 0){
                    System.out.println("Congratulations! You guessed correctly!");
                    System.out.println("Enter 1 to play again. To exit, enter 0");
                    runInt = scanner.nextInt();
                    if (runInt != 1 && runInt != 0){
                        System.out.println("Invalid response");
                        System.out.println("Enter 1 to play again. To exit, enter 0");
                    }
                } else{
                    System.out.println("invalid guess. number must be between 0-100 and a whole number (integer)");
                }
            }   
            

        }
        System.out.println("Thanks for playing! Ending program now...");


    }

}
