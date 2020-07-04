package edu.miu.reversi;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;

public class Node
{
	private Node mParent = null;
	private Hashtable<Integer, Node> mChildren = null;
	private int mScore = 0;
	private Move mMove = null;
	private int mMinScore = 100000; // Just any big number.
	private int mMaxScore = 0;
	
	public Node()
	{
		this.mChildren = new Hashtable<Integer, Node>();
		return;
	}
	
	public Node(Move move)
	{
		this.mChildren = new Hashtable<Integer, Node>();
		this.setMove(move);
		return;
	}
	
	public Node(Move move, Node parent)
	{
		this.mChildren = new Hashtable<Integer, Node>();
		this.setMove(move);
		this.setParent(parent);
		return;
	}
	
	public void setMove(Move move)
	{
		if(move != null){
			this.mMove = move;
		}else{
			System.out.println("Node.setMove - Attempt to pass null value.");
		}
		return;
	}
	
	public Move getMove()
	{
		return this.mMove;
	}	
	
	public void setParent(Node parent)
	{
		if(parent != null){
			this.mParent = parent;
		}else{
			System.out.println("Node.setParent - Attempt to pass null value.");
		}
		return;
	}
	
	public Node getParent()
	{
		return this.mParent;
	}
	
	public void setChild(int ID, Node child)
	{
		if(ID > -1 && ID < 65 && child != null){
			this.mChildren.put(ID, child);
			if(child.getMove().opponentPieces() < this.mMinScore){
				this.setMinScore(child.getMove().opponentPieces());
				if(this.mParent != null){
					this.mParent.setMinScore(this.mMinScore);
				}
			}
			if(child.getMove().opponentPieces() > this.mMaxScore){
				this.setMaxScore(child.getMove().opponentPieces());
				if(this.mParent != null){
					this.mParent.setMaxScore(this.mMaxScore);
				}
			}
		}else{
			System.out.println("Node.setChild - Attempt to pass null or invalid value.");
		}
		return;
	}
	
	public Node getChild(int ID)
	{
		if(this.mChildren.containsKey(ID)){
			return this.mChildren.get(ID);
		}else{
			System.out.println("Node.getChild - Child with this ID not found.");
			return null;
		}
	}
	
	public ArrayList<Node> getChildren()
	{
		ArrayList<Node> kids = new ArrayList<Node>();
		Iterator<Node> it = this.mChildren.values().iterator();
		while(it.hasNext())
			kids.add(it.next());
		
		return kids;
	}
	
	public void setScore(int score)
	{
		this.mScore = score;
		return;
	}
	
	public int getScore()
	{
		return this.mScore;
	}
	
	public void setMinScore(int score)
	{
		//if(score < this.mMinScore){
			this.mMinScore = score;
		//}
		return;
	}
	
	public int getMinScore()
	{
		return this.mMinScore;
	}
	
	public void setMaxScore(int score)
	{
		//if(score > this.mMaxScore){
			this.mMaxScore = score;
		//}
		return;
	}
	
	public int getMaxScore()
	{
		return this.mMaxScore;
	}
	
	public int getMinMaxFromChildren(boolean getMaxFromMin)
	{
		int maxScore = 0;
		int minScore = 100000; // just any big number.
		ArrayList<Node> children = this.getChildren();
		
		for(Node child : children)
		{
			if(getMaxFromMin == true){
				if(child.getMaxScore() > maxScore){
					maxScore = child.getMaxScore();
//					if(child.getMove().X() == 0 || child.getMove().X() == 7 || child.getMove().Y() == 0 || child.getMove().Y() == 7){
//						maxScore *= 2;
//					}
				}
			}else{
				if(child.getMinScore() < minScore){
					minScore = child.getMinScore();
				}
			}
		}
		return maxScore;
	}
	
//	public int getMinFromChildren()
//	{
//		int minScore = 100000; // just any big number.
////		ArrayList<Node> kids = new ArrayList<Node>();
////		Iterator<Node> it = this.mChildren.values().iterator();
////		while(it.hasNext())
////			kids.add(it.next());
//		ArrayList<Node> kids = this.getChildren();
//		
//		for(Node kid : kids)
//		{
//			if(kid.getMinScore() < minScore){
//				minScore = kid.getMinScore();
//			}
//		}
//		return minScore;
//	}
	
}
