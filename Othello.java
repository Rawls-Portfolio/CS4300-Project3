package p3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
	
public class Othello {
	private List<List<Character>> board;
	private int dim = 6; // default
	private List<Character> players;
	public static char black = '\u25C9';
	public static char white = '\u25EF';
	public static char valid = '\u2606';
	public static char empty = '_';
	private int numBlack;
	private int numWhite;
	private char player;
	private char current;
	private List<List<Integer>> flips;
	boolean showMoves;
	
	public Othello(char player, char display){
		this.setPlayer(player);
		this.setCurrent(black);
		players = new ArrayList<Character>(2);
		players.add(black);
		players.add(white);
		this.setNumBlack(0);
		this.setNumWhite(0);
		flips = new ArrayList<List<Integer>>();
		if (display == 'y' || display == 'Y')
			this.showMoves = true;
		else
			this.showMoves = false;
		
		board = new ArrayList<List<Character>>(this.getDim());
		for(int i = 0; i < this.getDim(); i++){
			List<Character> row = new ArrayList<Character>(this.getDim());
			getBoard().add(i, row);
			for (int j = 0; j < this.getDim(); j++){
				if((i == this.getDim()/2 - 1 && j == this.getDim()/2 - 1 ) || i == this.getDim()/2 && j == this.getDim()/2){
					getBoard().get(i).add(j, white); 
					this.setNumWhite(this.getNumWhite() + 1);
				} // end place White
				else if((i == this.getDim()/2 - 1 && j == this.getDim()/2) || i == this.getDim()/2 && j == this.getDim()/2 - 1){
					getBoard().get(i).add(j, black);
					this.setNumBlack(this.getNumBlack() + 1);
				} // end place Black
				else
					getBoard().get(i).add(j, Othello.empty);
			} // end for
		} // end for
	} // end Othello public constructor

	public Othello(Othello board2) {
		this.setNumBlack(board2.getNumBlack());
		this.setNumWhite(board2.getNumWhite());
		this.player = board2.player;
		this.current = board2.current;
		this.showMoves = board2.showMoves;
		this.players = new ArrayList<Character>(board2.players.size());
		for(int i = 0; i < board2.players.size(); i++)
			this.players.add(board2.players.get(i));
		this.flips = new ArrayList<List<Integer>>(board2.flips.size());
		for(int i = 0; i < board2.flips.size(); i++)
			this.flips.add(board2.flips.get(i));
		this.setBoard(board2.getBoard());
	} // end copy constructor

	public void display(){
		System.out.println("\t\t\t\tx\n");
		System.out.println(" \t\t1\t2\t3\t4\t5\t6");
		for(int i = 0; i < this.getDim(); i++){
			if(i == (int)this.dim/2)
				System.out.println("\ny");
			else
				System.out.println("\n ");
			System.out.print("\t" + (i + 1));
			for (int j = 0; j < this.getDim(); j++){
				System.out.print("\t" + this.getBoard().get(i).get(j));
			} // end for
		} // end for
		System.out.println("\n");
	} // end display
	
	public boolean findFlips(int x, int y) {
		if(board.get(y).get(x) != Othello.empty)
			return false;
		List<List<Integer>> neighbors = Arrays.asList(
				Arrays.asList(x, y-1), 		// north
				Arrays.asList(x+1, y-1), 	// northeast
				Arrays.asList(x+1, y), 		// east
				Arrays.asList(x+1, y+1), 	// southeast
				Arrays.asList(x, y+1), 		// south
				Arrays.asList(x-1, y+1), 	// southwest
				Arrays.asList(x-1, y), 		// west
				Arrays.asList(x-1, y-1));	// northwest
		
		for(int i = 0; i < neighbors.size(); i++){
			int neighborX = neighbors.get(i).get(0);
			int neighborY = neighbors.get(i).get(1);
			
			// if neighbor is within boundaries and has opponent piece
			if(	(neighborX >= 0 && neighborX < this.getDim())
			 && (neighborY >= 0 && neighborY < this.getDim())
			 && (this.getBoard().get(neighborY).get(neighborX) != this.getCurrent())
			 && (this.getBoard().get(neighborY).get(neighborX) != Othello.empty))
				setFlips(x, y, neighbors.get(i).get(0), neighbors.get(i).get(1));	
		}// end for (all neighbors)
	
		if(!this.flips.isEmpty())
			return true;
		else{
			return false;
		}
	} // end findFlips
	
	public List<List<Integer>> validMoves(){
		List<List<Integer>> actions = new ArrayList<List<Integer>>();
		for (int x = 0; x < this.getDim(); x++){
			for (int y = 0; y < this.getDim(); y++){
				if(this.findFlips(x,  y))
					actions.add(Arrays.asList(x, y));
				this.flips.clear();
			} // end for
		} // end for
		return actions;
	}// end validMoves
	
	public void toggleMoves(List<List<Integer>> validMoves) {
		for(int i = 0; i < validMoves.size(); i++){
			int x = validMoves.get(i).get(0);
			int y = validMoves.get(i).get(1);
			if(board.get(y).get(x) == Othello.valid)
				board.get(y).set(x, Othello.empty);
			else
				board.get(y).set(x, Othello.valid);
		} // end for
	} // end toggleMoves

	public void place(int x, int y) {
		this.getBoard().get(y).set(x, this.current);
		if(getCurrent() == Othello.black)
			this.setNumBlack(this.getNumBlack() + 1);
		else
			this.setNumWhite(this.getNumWhite() + 1);
		this.flipPieces();
	} // end place

	private boolean setFlips(int lastx, int lasty, int x, int y ) {
		boolean success = false;
		int nextx = (x - lastx) + x;
		int nexty = (y - lasty) + y;
		
		if(	(nextx >= 0 && nextx < this.getDim())
		 && (nexty >= 0 && nexty < this.getDim()) ){
			if (this.getBoard().get(nexty).get(nextx) == this.getCurrent()) 
				success = true;
			else if (this.getBoard().get(nexty).get(nextx) != Othello.empty)	
				success = setFlips(x, y, nextx, nexty);
		}// end if (next location is on board)
		if(success)
			this.flips.add(Arrays.asList(x, y));
		return success;
	} // end setFlips

	private void flipPieces() {
		if(getCurrent() == Othello.black){
			this.setNumBlack(this.getNumBlack() + flips.size());
			this.setNumWhite(this.getNumWhite() - flips.size());
		}else{
			this.setNumBlack(this.getNumBlack() - flips.size());
			this.setNumWhite(this.getNumWhite() + flips.size());
		} // end else
		
		for (int i = 0; i < flips.size(); i++){
			int x = flips.get(i).get(0);
			int y = flips.get(i).get(1);
			this.getBoard().get(y).set(x, this.getCurrent());
		} // end for
		flips.clear();
	} // end flipPieces

	public boolean endGame() {
		boolean isFull = true;
		for(int i = 0; i < this.getDim(); i++)
			if(this.getBoard().get(i).contains(Othello.empty))
				isFull = false;

		if (isFull || this.getNumBlack() == 0 || this.getNumWhite() == 0)
			return true;
		else
			return false;
	} // end endGame
	
	public void nextPlayer(){
		if(this.getCurrent() == Othello.black)
			this.setCurrent(Othello.white);
		else
			this.setCurrent(Othello.black);
	} // end nextPlayer

	public void setPlayer(char player) {
		this.player = player;
	} // end setPlayer

	public void setCurrent(char current) {
		this.current = current;
	} // end setCurrent

	public char getWinner() {
		if(this.getNumBlack() > this.getNumWhite())
			return Othello.black;
		else
			return Othello.white;
	} // end getWinner

	public int getDim() {
		return dim;
	} // end getDim

	public char getCurrent() {
		return current;
	} // end getCurrent

	public char getPlayer() {
		return player;
	} // end getPlayer

	public List<List<Character>> getBoard() {
		return board;
	} // end getBoard

	public void setBoard(List<List<Character>> board) {
		this.board = new ArrayList<List<Character>>(board.size());
		for(int y = 0; y < board.size(); y++){
			List<Character> row = new ArrayList<Character>(board.get(0).size());
			for (int x = 0; x < board.get(y).size(); x++)
				row.add(board.get(y).get(x));
			this.board.add(row);
		} // end for
	} // end setBoard

	public int getNumBlack() {
		return numBlack;
	} // end getNumBlack

	public void setNumBlack(int numBlack) {
		this.numBlack = numBlack;
	} // end setNumBlack

	public int getNumWhite() {
		return numWhite;
	} // end getNumWhite

	public void setNumWhite(int numWhite) {
		this.numWhite = numWhite;
	} // end setNumWhite
} // end Othello
