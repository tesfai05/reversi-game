package edu.miu.reversi;

import java.util.ArrayList;
import java.util.Random;

public class Engine
{
	private final SquareState mPlayer = SquareState.WHITE;
	private final SquareState mComputer = SquareState.BLACK;
	private int mTotalMovesAhead = 3;
	private int mCornerBias = 10;
	private int mEdgeBias = 5;
	private int mRegion4Bias = -5;
	
	private MainGUI mGUI = null;
	private int mPlayerMoves = 0;
	private int mComputerMoves = 0;
	private SquareState[][] matrix = new SquareState[Globals.GRID_SIZE_INTEGER][Globals.GRID_SIZE_INTEGER]; // Game board dimensions.
	private Node mRoot = null;
	private int mMovesAhead = 0;
	private boolean blinkingIsFinished = true;
	private boolean gameInProgress = false;
	
	public Engine()
	{
		this.initialize();
		return;
	}
	
	private void initialize()
	{
		this.mGUI = new MainGUI(this);
		
		// Add items to memory matrix.
		for(int i = 0; i < Globals.GRID_SIZE_INTEGER; i++)
			for(int j = 0; j < Globals.GRID_SIZE_INTEGER; j++)
				this.matrix[j][i] = SquareState.NONE;
		
		return;
	}
	
	public void setMovesAhead(int moves)
	{
		if(moves < 2){
			this.mTotalMovesAhead = 2;
		}else{
			this.mTotalMovesAhead = moves;
		}
		if(moves > 10){
			this.mTotalMovesAhead = 10;
		}else{
			this.mTotalMovesAhead = moves;
		}
		return;
	}
	
	public int getMovesAhead()
	{
		return this.mTotalMovesAhead;
	}
	
	public void setFavorEdges(int favor)
	{
		if(favor < -50){
			this.mEdgeBias = -50;
		}else{
			this.mEdgeBias = favor;
		}
		if(favor > 50){
			this.mEdgeBias = 50;
		}else{
			this.mEdgeBias = favor;
		}
		return;
	}
	
	public int getFavorEdges()
	{
		return this.mEdgeBias;
	}
	
	public void setFavorCorners(int favor)
	{
		if(favor < -50){
			this.mCornerBias = -50;
		}else{
			this.mCornerBias = favor;
		}
		if(favor > 50){
			this.mCornerBias = 50;
		}else{
			this.mCornerBias = favor;
		}
		return;
	}
	
	public int getFavorCorners()
	{
		return this.mCornerBias;
	}
	
	public void setDisfavorRegion4(int favor)
	{
		if(favor < -50){
			this.mRegion4Bias = -50;
		}else{
			this.mRegion4Bias = favor;
		}
		if(favor > 50){
			this.mRegion4Bias = 50;
		}else{
			this.mRegion4Bias = favor;
		}
		return;
	}
	
	public int getDisfavorRegion4()
	{
		return this.mRegion4Bias;
	}
	
	public void resetGame()
	{
		this.mGUI.setAllowResizeFlag(false);
		
		// Create grid squares.
		this.mGUI.getGridPanel().initializeGridSquares();
		
		for(int i = 0; i < Globals.GRID_SIZE_INTEGER; i++)
			for(int j = 0; j < Globals.GRID_SIZE_INTEGER; j++)
			{
				this.matrix[j][i] = SquareState.NONE;
				this.mGUI.setPiece(j, i, SquareState.NONE);
			}
		
		this.randomlyPlaceStartPositions();
		
		this.updateStatusPanel();
		this.gameInProgress = true;
		this.mGUI.setAllowResizeFlag(true);
		return;
	}
	
	private void randomlyPlaceStartPositions()
	{
		SquareState playerA = null;
		SquareState playerB = null;
		boolean first = new Random().nextBoolean();
		if(first){
			playerA = SquareState.WHITE;
			playerB = SquareState.BLACK;
		}else{
			playerA = SquareState.BLACK;
			playerB = SquareState.WHITE;
		}
		
		this.mGUI.setPiece(3, 3, playerA);
		this.matrix[3][3] = playerA;
		this.mGUI.setPiece(4, 4, playerA);
		this.matrix[4][4] = playerA;
		this.mGUI.setPiece(3, 4, playerB);
		this.matrix[3][4] = playerB;
		this.mGUI.setPiece(4, 3, playerB);
		this.matrix[4][3] = playerB;
		
		return;
	}
	
	public void resetGUIGraphics()
	{
		for(int y = 0; y < Globals.GRID_SIZE_INTEGER; y++)
		{
			for(int x = 0; x < Globals.GRID_SIZE_INTEGER; x++)
			{
				if(this.matrix[x][y] != null){
					this.mGUI.setPiece(x, y, this.matrix[x][y]);
				}
			}
		}
		return;
	}
	
	public void setBlinkingFinished(final boolean isFinished)
	{
		this.blinkingIsFinished = isFinished;
		return;
	}
	
	public synchronized boolean performMove(final int x, final int y, final SquareState player, final SquareState opponent)
	{
		boolean isValid = false;
		Traverse t = new Traverse(x, y, player, opponent, this.matrix);
		if(t.isValid()){
			if(player == SquareState.WHITE){
				this.mGUI.setPiece(x, y, SquareState.WHITE);
				this.matrix[x][y] = SquareState.WHITE;
			}else{
				this.mGUI.setPiece(x, y, SquareState.BLACK);
				this.matrix[x][y] = SquareState.BLACK;
			}
			t = new Traverse(x, y, player, opponent, this.matrix);
			ArrayList<Integer> flips = t.getFlips();
			flipPieces(flips, player, this.matrix, true);
			if(player == this.mComputer && flips.size() > 0){
				Blinker blink = new Blinker(this.mGUI, this, x, y, flips, this.mComputer);
			}
			isValid = true;
		}
		return isValid;
	}
	
	public void performPlayerMove(final int x, final int y)
	{
		if(this.blinkingIsFinished == true){
			boolean moveMade = this.performMove(x, y, this.mPlayer, this.mComputer);
			this.updateStatusPanel();
			if((moveMade == true || this.mPlayerMoves <= 0) && this.gameInProgress == true){
				if(this.mComputerMoves > 0){
					this.performComputerMove();	// Computer's turn immediately follows human player.
				}else{
					this.mGUI.displayMessageWindow("Computer Has No Moves", "The computer has no moves to take.\n\nPlease take another turn.");
				}
			}
		}
		return;
	}
	
	public void performComputerMove()
	{
		this.simMoves();
		Move bestMove = this.findBestMove();
		// Remainder of some testing code.  Generally, if findBestMove() returns null, the computer doesn't
		// have a move.
		Traverse t = new Traverse(bestMove.X(), bestMove.Y(), this.mComputer, this.mPlayer, this.matrix);
		if(bestMove == null){
			System.out.println("Error: bestMove is null.");
		}else if(!t.isValid()){
			System.out.println("Error: computer's chosen move is not valid.");
		}else{
			this.performMove(bestMove.X(), bestMove.Y(), this.mComputer, this.mPlayer);
		}
		return;
	}
	
	public synchronized void postCheckComputerMove()
	{
		this.updateStatusPanel();
		if(this.mPlayerMoves <= 0 && this.mComputerMoves > 0){
			// Computer gets to go again.
			this.mGUI.displayMessageWindow("You Have No Moves", "You have no moves at the moment.\n\nThe computer will take another turn.");
			this.performComputerMove();
		}
		return;
	}
	
	public Move findBestMove()
	{
		// Get a list of all immediately available moves.
		Move bestMove = null;
		ArrayList<Node> children = this.mRoot.getChildren();
		if(children.size() > 0){
			findBestMove(this.mRoot, true);
			// Now get the max from the root's children
			int bestIndex = 0;
			for(int i = 0; i < children.size(); i++)
			{ 
				// Bias is imposed here to simulate more strategic behavior.  Occupying corners and
				// edges of the game board often lead to strategic advantages in the game.
				if(children.get(i).getMove().X() == 0 && children.get(i).getMove().Y() == 0 || 
				   children.get(i).getMove().X() == 0 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 1 || 
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 1 && children.get(i).getMove().Y() == 0 || 
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 1 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 1){
					// Highest bias toward corners.
					children.get(i).setMaxScore(children.get(i).getMaxScore() + this.mCornerBias);
				}else if(children.get(i).getMove().X() == 1 && children.get(i).getMove().Y() == 0 ||
				   children.get(i).getMove().X() == 0 && children.get(i).getMove().Y() == 1 ||
				   children.get(i).getMove().X() == 1 && children.get(i).getMove().Y() == 1 ||
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 2 && children.get(i).getMove().Y() == 0 ||
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 1 && children.get(i).getMove().Y() == 1 ||
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 2 && children.get(i).getMove().Y() == 1 ||
				   children.get(i).getMove().X() == 0 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 2 ||
				   children.get(i).getMove().X() == 1 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 1 ||
				   children.get(i).getMove().X() == 1 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 2 ||
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 1 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 2 ||
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 2 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 1 ||
				   children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 2 && children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 2){
					// Bias against Region4.
					children.get(i).setMaxScore(children.get(i).getMaxScore() + this.mRegion4Bias);
				}else if(children.get(i).getMove().X() == 0 || 
					 children.get(i).getMove().X() == Globals.GRID_SIZE_INTEGER - 1 || 
					 children.get(i).getMove().Y() == 0 || 
					 children.get(i).getMove().Y() == Globals.GRID_SIZE_INTEGER - 1){
					// Lower bias toward edges.
					children.get(i).setMaxScore(children.get(i).getMaxScore() + this.mEdgeBias);
				}
				if(children.get(i).getMaxScore() > children.get(bestIndex).getMaxScore()){
					bestIndex = i;
				}
			}
			bestMove = children.get(bestIndex).getMove();
		}
		
		return bestMove;
	}
	
	private void findBestMove(final Node root, final boolean getMaxFromMin)
	{
		// The idea behind this recursive method, is to traverse all the way out to the leaves of 
		// the tree, then calculate scores for parent nodes while returning back to the root.
		ArrayList<Node> children = root.getChildren();
		if(children.size() <= 0){
			for(Node child : children)
			{
				this.findBestMove(root, !getMaxFromMin);
				child.setMaxScore(child.getMinMaxFromChildren(getMaxFromMin));
				child.setMinScore(child.getMinMaxFromChildren(!getMaxFromMin));
			}
		}
		return;
	}
	
	private void simMoves()
	{
		this.mRoot = new Node();
		this.mMovesAhead = 0;
		
		// Get a list of all immediately available moves.
		ArrayList<Move> moves = this.findAllPossibleMoves(this.mComputer, this.mPlayer, this.matrix);
		
		if(moves.size() > 0){
			// Simulate moves from the immediate list.
			this.simMoves(this.mRoot, moves, this.matrix, this.mComputer, this.mPlayer);
		}
		return;
	}
	
	private void simMoves(final Node root, final ArrayList<Move> moves, final SquareState[][] aMatrix, final SquareState playerA, final SquareState playerB)
	{
		if(++this.mMovesAhead < this.mTotalMovesAhead){
			for(Move aMove: moves)
			{
				Node aNode = new Node(aMove, root);
				root.setChild(GridMath.getID(aMove.X(), aMove.Y()), aNode);
				
				// Make a copy of the game board.
				SquareState[][] tempMatrix = new SquareState[Globals.GRID_SIZE_INTEGER][Globals.GRID_SIZE_INTEGER];
				for(int y = 0; y < Globals.GRID_SIZE_INTEGER; y++)
					for(int x = 0; x < Globals.GRID_SIZE_INTEGER; x++)
						tempMatrix[x][y] = aMatrix[x][y];
				
				// Make a possible prospective move.
				tempMatrix[aMove.X()][aMove.Y()] = playerA; //this.mComputer;
				// Flip the simulated pieces for the move.
				Traverse t = new Traverse(aMove.X(), aMove.Y(), playerA, playerB, tempMatrix);
				ArrayList<Integer> flips = t.getFlips();
				this.flipPieces(flips, playerA, tempMatrix, false);
				
				// Simulate the opponent's possible counter moves.
				ArrayList<Move> tempMoves = this.findAllPossibleMoves(playerB, playerA, tempMatrix);
				if(tempMoves.size() > 0){
					this.simMoves(aNode, tempMoves, tempMatrix, playerB, playerA);
				}
			}
		}
		return;
	}
	
	/**
	 * findAllPossibleMoves repeatedly calls findValidMoves to retrieve a list of all possible moves for the specified player.
	 * @param player - piece representing current player.
	 * @param opponent - not the current player.
	 * @return - ArrayList of type, Move.
	 */
	private ArrayList<Move> findAllPossibleMoves(final SquareState player, final SquareState opponent, final SquareState[][] aMatrix)
	{
		// Traverse the full grid for specified player pieces.
		ArrayList<Move> allPossibleMoves = new ArrayList<Move>();
		for(int y = 0; y < Globals.GRID_SIZE_INTEGER; y++)
			for(int x = 0; x < Globals.GRID_SIZE_INTEGER; x++)
				if(aMatrix[x][y] == player){
					Traverse t = new Traverse(x, y, opponent, aMatrix);
					ArrayList<Move> someMoves = t.getMoves();
					// Don't want to double-count moves, only tally the pieces that can be taken.
					for(Move thisMove : someMoves)
					{
						boolean found = false;
						for(Move thatMove : allPossibleMoves)
						{
							if(thisMove.X() == thatMove.X() && thisMove.Y() == thatMove.Y()){
								thatMove.opponentPieces(thatMove.opponentPieces() + thisMove.opponentPieces());
								found = true;
								break;
							}
						}
						if(!found)
							allPossibleMoves.add(thisMove);
						
					}
				}
		return allPossibleMoves;
	}
	
	/**
	 * flipPieces reverses opponent pieces one a player piece has been set.
	 */
	private void flipPieces(final ArrayList<Integer> flips, final SquareState player, final SquareState[][] aMatrix, final boolean isActualMove)
	{
		if(flips.size() > 0){
			for(int flip : flips)
			{
				int i = GridMath.getX(flip);
				int j = GridMath.getY(flip);
				if(player == SquareState.WHITE){
					aMatrix[i][j] = SquareState.WHITE;
					if(isActualMove){							// An actual move would be displayed on the board.
						this.mGUI.setPiece(i, j, SquareState.WHITE);
					}
				}else{
					aMatrix[i][j] = SquareState.BLACK;
					if(isActualMove){
						this.mGUI.setPiece(i, j, SquareState.BLACK);
					}
				}
			}
		}
		return;
	}
	
	/**
	 * Counts the number of pieces the specified player has on the game board.
	 * @param player - piece.
	 * @return - integer.
	 */
	public int countPieces(final SquareState player)
	{
		int count = 0;
		for(int y = 0; y < Globals.GRID_SIZE_INTEGER; y++)
			for(int x = 0; x < Globals.GRID_SIZE_INTEGER; x++)
				if(this.matrix[x][y] == player)
					count++;
		return count;
	}
	
	public void updateStatusPanel()
	{
		int aCount = 0;
		int bCount = 0;
		ArrayList<Move> aList = this.findAllPossibleMoves(this.mPlayer, this.mComputer, this.matrix);
		this.mPlayerMoves = aList.size();
		this.mGUI.setPlayerMovesLabel(String.valueOf(this.mPlayerMoves));
		aCount = this.countPieces(mPlayer);
		this.mGUI.setPlayerPiecesLabel(String.valueOf(aCount));
		aList.clear();
		aList = this.findAllPossibleMoves(this.mComputer, this.mPlayer, this.matrix);
		this.mComputerMoves = aList.size();
		this.mGUI.setComputerMovesLabel(String.valueOf(this.mComputerMoves));
		bCount += this.countPieces(mComputer);
		this.mGUI.setComputerPiecesLabel(String.valueOf(bCount));
		// Check if game is finished.
		// It's finished if both players have no more moves, and/or all squares have been filled. 
		if(aCount + bCount >= (Globals.GRID_SIZE_INTEGER * Globals.GRID_SIZE_INTEGER) || (this.mPlayerMoves <= 0 && this.mComputerMoves <= 0)){
			this.gameInProgress = false;
			this.displayFinalMessage(aCount, bCount);
		}
		return;
	}
	
	private void displayFinalMessage(final int playerCount, final int computerCount)
	{
		if(playerCount > computerCount){
			this.mGUI.displayMessageWindow("Congratulations", "Human player has won!");
		}else if(playerCount < computerCount){
			this.mGUI.displayMessageWindow("Sorry", "Human player has lost.");
		}else{
			this.mGUI.displayMessageWindow("Final", "Game has ended in a tie.");
		}
		return;
	}
	
	
	
	
	
}
