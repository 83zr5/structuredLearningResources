import java.util.Random;
import java.util.Scanner;

public class NumberGuesser{
    private int targetNum;
    private int userGuess;


    public NumberGuesser(){
        targetNum = 0;
        userGuess = 0;
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
            if (userGuess > targetNum){
                return 1; //guess is too high
            } else if (userGuess < targetNum){
                return -1; //guess is too low
            } else{
                return 0; //guess is correct
            }
        }
    }


}