import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Adversarial {
	static Scanner userInput = new Scanner(System.in);
	
	public static void main(String[] args) {
		char player;
		System.out.println("Would you like to play as black or white? (b or B for black, else white): ");
		char display = userInput.next().charAt(0); 
		userInput.nextLine(); // clear userInput
		
		if (display == 'b' || display == 'B')
			player = Othello.black;
		else
			player = Othello.white;
	
		System.out.println("Would you like to display valid moves (" + Othello.valid +") to be displayed on your turn? (y or Y for yes, else no): ");
		display = userInput.next().charAt(0); 
		userInput.nextLine(); // clear userInput

			
		Othello board = new Othello(player, display);
		Minimax hal9000 = new Minimax();
		while(gameOn(board, hal9000));
		board.display();
		userInput.close();
		System.out.println("Game over! Winner is " + board.getWinner());
		System.out.println("Thank you. Program is complete.");
	} // end main

	private static boolean gameOn(Othello board, Minimax hal9000) {
		List<List<Integer>> validMoves = board.validMoves();
	
		if(validMoves.isEmpty()){
			System.out.println("Player " + board.getCurrent() + " has no valid moves left!");
			return false;
		} // end if
		
		List<Integer> move;
		if(board.getCurrent() == board.getPlayer()){
			if(board.showMoves)
				board.toggleMoves(validMoves);
			board.display();
			move = userTurn(board, validMoves);	
		}else{
			board.display();
			System.out.println("The computer opponent, playing as " + board.getCurrent() + " is thinking... ");
			move = hal9000.decision(board);
			System.out.println("It has chosen '" + (move.get(0) + 1) +  " " + (move.get(1)+1) + "'");
		} // end else
		
		if((board.getCurrent() == board.getPlayer()) && board.showMoves)
			board.toggleMoves(validMoves);
		board.findFlips(move.get(0), move.get(1)); 
		board.place(move.get(0), move.get(1));
		board.nextPlayer();
		return !board.endGame();
	} // end gameOn

	private static List<Integer> userTurn(Othello board, List<List<Integer>> validMoves) {
		Integer x = null;
		Integer y = null;
		List<Integer> move;
		boolean success = false;
		do{
			if(x != null && y != null)
				System.out.println("That was an invalid move.");
			System.out.println("Player " + board.getCurrent() + ", please enter two integers representing your next move in the format 'x y': ");
			try{
			x = userInput.nextInt();
			y = userInput.nextInt();
			} catch(InputMismatchException ex){
				System.out.println("Invalid entry. Please use integers.");
				x = -1;
				y = -1;
			} // end try catch
			userInput.nextLine(); // clear userInput
			if (( x < 1 || x > board.getDim()) || (y < 1 || y > board.getDim()))
				System.out.println("Please restrict your values to be greater than 1 and less than " + board.getDim());
			move = Arrays.asList(--x, --y);
			for(int i = 0; i < validMoves.size(); i ++)
				if(validMoves.get(i).equals(move))
					success = true;
		} while (!success);
		return Arrays.asList(x, y);
	} // end  userTurn
} // end Adversarial
