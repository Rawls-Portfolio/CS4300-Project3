import java.util.Calendar;
import java.util.List;

public class Minimax {
	Double posinfinity = Double.POSITIVE_INFINITY;
	Double neginfinity = Double.NEGATIVE_INFINITY;
	int depth = 0;
	int maxDepth = 1;
	int sec = 5;
	long timeTrigger;
	
	public List<Integer> decision(Othello board) {
		double argmax = neginfinity;
		double utility;
		List<List<Integer>> actions = board.validMoves();
		List<Integer> best = actions.get(0);
		
		// set time to end search
		timeTrigger = Calendar.getInstance().getTimeInMillis() + 5000;
		 
		// timed iterative deepening
		while(Calendar.getInstance().getTimeInMillis() < timeTrigger){
			for(int i = 0; i < actions.size(); i++){
				Othello copy = new Othello(board);
				this.result(copy, actions.get(i));
				utility = minValue(copy);
				argmax = Math.max(argmax, utility);
				if(argmax == utility)
					best = actions.get(i);
			} // end for
			this.maxDepth++;
		} // end while
		return best;
	} // end decision


	private Double maxValue(Othello board) {
		if(board.endGame() || ++this.depth >= this.maxDepth
			|| (Calendar.getInstance().getTimeInMillis() >= timeTrigger))
			return utility(board).doubleValue();
	
		Double maxValue = neginfinity;
		Double min;
		List<List<Integer>> actions = board.validMoves();
		for(int i = 0; i < actions.size(); i++){
			Othello copy = new Othello(board);
			this.result(copy, actions.get(i));
			min = minValue(copy);
			maxValue = Math.max(maxValue, min);
		}// end for
		return maxValue;
	}


	private Double minValue(Othello board) {
		if(board.endGame() || ++this.depth >= this.maxDepth
			|| (Calendar.getInstance().getTimeInMillis() >= timeTrigger))
			return utility(board).doubleValue();

		Double minValue = posinfinity;
		Double max;
		List<List<Integer>> actions = board.validMoves();
		for(int i = 0; i < actions.size(); i++){
			Othello copy = new Othello(board);
			this.result(copy, actions.get(i));
			max = maxValue(copy);
			minValue = Math.min(minValue, max);
		}// end for
		return minValue;
	} //end minValue
	
	private Integer utility(Othello copy) {
		if(copy.getPlayer() == Othello.white)
			return(copy.getNumBlack() - copy.getNumWhite());
		else
			return(copy.getNumWhite() - copy.getNumBlack());
	} // end utility

	private void result(Othello copy, List<Integer> action) {
		copy.findFlips(action.get(0), action.get(1)); 
		copy.place(action.get(0), action.get(1)); 
		copy.nextPlayer(); 
	} // end result

	
}
