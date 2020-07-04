package edu.miu.reversi;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;

import java.util.ArrayList;

public class MainGUI implements WindowListener, MouseListener, MouseMotionListener, ActionListener, ComponentListener
{
	private GraphicsEnvironment mGE = null;
	private GraphicsDevice mGD = null;
	private GraphicsConfiguration mGC = null;
	
	private JFrame mFrame = null;
	private JPanel mBasePanel = null;
	private JPanel mMainPanel = null;
	private JPanel mControlPanel = null;
	private JPanel mStatusPanel = null;
	private GridPanel mGridPanel = null;
	private ArrayList<GridSquare> mSquares = null;
	private JButton cmdNew = null;
	private static final String NEW_GAME_BUTTON = "Start New Game";
	private Component mPostNewButtonSpacer = null;
	private JButton cmdOptions = null;
	private static final String OPTION_BUTTON = "Choose Options";
	private Component mControlPanelSpacer = null;
	private JLabel lblPlayerMoves = null;
	private JLabel lblComputerMoves = null;
	private JLabel lblPlayerPieces = null;
	private JLabel lblComputerPieces = null;
	
	private Engine mEngine = null;
	private Point mMousePosition = null;
	private boolean allowResize = false;
	private OptionsWindow mOptionsWindow = null;
	
	public MainGUI(Engine anEngine)
	{
		this.initializeMain();
		this.mEngine = anEngine;
		return;
	}
	
	private void initializeMain()
	{
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e){
			try{
				JFrame.setDefaultLookAndFeelDecorated(true);
			}catch(Exception ex){
				System.out.println("Error on look and feel");
			}
		}
		
		this.mGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.mGD = this.mGE.getDefaultScreenDevice();
		this.mGC = this.mGD.getDefaultConfiguration();
		
		this.mFrame = new JFrame();
		this.mFrame.setTitle(Globals.APPLICATION_TITLE);
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BorderLayout());
		this.mBasePanel.addMouseListener(this);
		this.mMainPanel = new JPanel();
		this.mMainPanel.setLayout(new BoxLayout(this.mMainPanel, BoxLayout.Y_AXIS));
		this.mMainPanel.addMouseListener(this);
		
		this.initializeControlPanel();
		this.mMainPanel.add(this.mControlPanel);
		
		this.initializeStatusPanel();
		this.mMainPanel.add(this.mStatusPanel);
		
		this.initializeGameGrid();
		this.mMainPanel.add(this.mGridPanel);
		
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.NORTH);
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.WEST);
		this.mBasePanel.add(this.mMainPanel, BorderLayout.CENTER);
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.EAST);
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.SOUTH);
		
		this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mFrame.getContentPane().add(this.mBasePanel);
		this.mFrame.setMinimumSize(new Dimension(Globals.MINIMUM_WINDOW_WIDTH, Globals.MINIMUM_WINDOW_HEIGHT));
		this.mFrame.setPreferredSize(new Dimension(Globals.MINIMUM_WINDOW_WIDTH, Globals.MINIMUM_WINDOW_HEIGHT));
		this.mFrame.setVisible(true);
		this.mFrame.pack();
		
		mMousePosition = new Point(0, 0);
		return;
	}
	
	public void initializeControlPanel()
	{
		this.mControlPanel = new JPanel();
		this.mControlPanel.setLayout(new BoxLayout(this.mControlPanel, BoxLayout.X_AXIS));
		this.mControlPanel.setPreferredSize(new Dimension(600, 30));
		this.mControlPanel.setMinimumSize(new Dimension(600, 30));
		this.mControlPanel.addMouseListener(this);
		this.cmdNew = new JButton("New Game");
		Font theFont = new Font("Sans Serif", Font.BOLD, this.mMainPanel.getFont().getSize());
		Dimension textSize = TextMetric.getMetric(this.cmdNew.getText(), theFont);
		this.cmdNew.setMargin(new Insets(Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET, Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET));
		this.cmdNew.setMinimumSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdNew.setPreferredSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdNew.setActionCommand(NEW_GAME_BUTTON);
		this.cmdNew.setToolTipText(NEW_GAME_BUTTON);
		this.cmdNew.addActionListener(this);
		this.mControlPanel.add(this.cmdNew);
		
		this.mPostNewButtonSpacer = Box.createRigidArea(new Dimension(Globals.AFTER_COMPONENT_SPACER_WIDTH, 20));
		this.mControlPanel.add(this.mPostNewButtonSpacer);
		
		this.cmdOptions = new JButton("Options");
		theFont = new Font("Sans Serif", Font.BOLD, this.mMainPanel.getFont().getSize());
		textSize = TextMetric.getMetric(this.cmdOptions.getText(), theFont);
		this.cmdOptions.setMargin(new Insets(Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET, Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET));
		this.cmdOptions.setMinimumSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdOptions.setPreferredSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdOptions.setActionCommand(OPTION_BUTTON);
		this.cmdOptions.setToolTipText(OPTION_BUTTON);
		this.cmdOptions.addActionListener(this);
		this.mControlPanel.add(this.cmdOptions);
		
		this.mControlPanelSpacer = Box.createRigidArea(new Dimension(3000, 20));
		this.mControlPanel.add(this.mControlPanelSpacer);
		
		return;
	}
	
	public void initializeStatusPanel()
	{
		this.mStatusPanel = new JPanel();
		this.mStatusPanel.setLayout(new BoxLayout(this.mStatusPanel, BoxLayout.X_AXIS));
		this.mStatusPanel.addMouseListener(this);
		Font theFont = new Font("Serif", Font.BOLD, this.mMainPanel.getFont().getSize());
		
		JLabel aLabel = new JLabel("Player Moves:");
		aLabel = (JLabel)this.setMinimumControlSize(aLabel, "Player Moves: ", theFont);
		aLabel.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(aLabel);
		
		this.lblPlayerMoves = new JLabel("0");
		this.setMinimumControlSize(this.lblPlayerMoves, "000", theFont);
		this.lblPlayerMoves.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(this.lblPlayerMoves);
		
		this.mStatusPanel.add(Box.createRigidArea(new Dimension(16, 20)));

		aLabel = new JLabel("Player Pieces:");
		aLabel = (JLabel)this.setMinimumControlSize(aLabel, "Player Pieces: ", theFont);
		aLabel.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(aLabel);
		
		this.lblPlayerPieces = new JLabel("0");
		this.setMinimumControlSize(this.lblPlayerPieces, "000", theFont);
		this.lblPlayerPieces.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(this.lblPlayerPieces);
		
		this.mStatusPanel.add(Box.createRigidArea(new Dimension(16, 20)));
		
		aLabel = new JLabel("Computer Moves:");
		aLabel = (JLabel)this.setMinimumControlSize(aLabel, "Computer Moves: ", theFont);
		aLabel.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(aLabel);
		
		this.lblComputerMoves = new JLabel("0");
		this.setMinimumControlSize(this.lblComputerMoves, "000", theFont);
		this.lblComputerMoves.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(this.lblComputerMoves);
		
		this.mStatusPanel.add(Box.createRigidArea(new Dimension(16, 20)));
		
		aLabel = new JLabel("Computer Pieces:");
		aLabel = (JLabel)this.setMinimumControlSize(aLabel, "Computer Pieces: ", theFont);
		aLabel.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(aLabel);
		
		this.lblComputerPieces = new JLabel("0");
		this.setMinimumControlSize(this.lblComputerPieces, "000", theFont);
		this.lblComputerPieces.setPreferredSize(new Dimension(300, 30));
		this.mStatusPanel.add(this.lblComputerPieces);
		
		this.mStatusPanel.add(Box.createRigidArea(new Dimension(3000, 16)));
		
		this.mStatusPanel.setPreferredSize(new Dimension(650, 30));
		
		return;
	}
	
	public void initializeGameGrid()
	{
		this.mGridPanel = new GridPanel(this);
		this.mGridPanel.addComponentListener(this);
		this.mGridPanel.addMouseListener(this);
		this.mGridPanel.addMouseMotionListener(this);
		return;
	}
	
	public Component setMinimumControlSize(Component control, String text, Font theFont)
	{
		Dimension metrics = this.getTextSize(text, theFont);
		control.setMinimumSize(new Dimension(metrics.width, metrics.height));
		return control;
	}
	
	public Dimension getTextSize(String text, Font theFont)
	{
		BufferedImage tempImage = this.mGC.createCompatibleImage(10, 10, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)tempImage.getGraphics();
		Dimension result = new Dimension();
		FontMetrics metrics = g2d.getFontMetrics(theFont);
		result.height = metrics.getHeight();
		result.width = metrics.stringWidth(text) + 16;
		return result;
	}
	
	public void resetSquares()
	{
		this.mSquares = new ArrayList<GridSquare>();
		return;
	}
	
	public synchronized ArrayList<GridSquare> getGridSquares()
	{
		return this.mSquares;
	}
	
	public GridPanel getGridPanel()
	{
		return this.mGridPanel;
	}
	
	public synchronized void setPiece(int x, int y, SquareState aColor)
	{
		int index = GridMath.getID(x, y);
		if(index >= 0 && index < (Globals.GRID_SIZE_INTEGER * Globals.GRID_SIZE_INTEGER)){
			if(aColor != null && this.mSquares != null){
				if(aColor == SquareState.NONE){
					this.mSquares.get(index).switchImage("blank");
				}else if(aColor == SquareState.WHITE){
					this.mSquares.get(index).switchImage("white");
				}else if(aColor == SquareState.BLACK){
					this.mSquares.get(index).switchImage("black");
				}
//				else if(aColor == SquareState.BLUE){
//					this.mSquares.get(index).switchImage("blue");
//				}else if(aColor == SquareState.YELLOW){
//					this.mSquares.get(index).switchImage("yellow");
//				}
			}else{
				System.out.println("MainGUI.setPiece - attempt to pass null object.");
			}
		}else{
			System.out.println("MainGUI.setPiece - index from (8*y)+x doesn't exist in mSquares array: " + index);
		}
		this.mGridPanel.repaint();
		return;
	}
	
	public void setPlayerMovesLabel(String someText)
	{
		this.lblPlayerMoves.setText(someText);
		return;
	}
	
	public void setPlayerPiecesLabel(String someText)
	{
		this.lblPlayerPieces.setText(someText);
		return;
	}
	
	public void setComputerMovesLabel(String someText)
	{
		this.lblComputerMoves.setText(someText);
		return;
	}
	
	public void setComputerPiecesLabel(String someText)
	{
		this.lblComputerPieces.setText(someText);
		return;
	}
	
	public void setAllowResizeFlag(boolean isAllowed)
	{
		this.allowResize = isAllowed;
		return;
	}
	
	public void displayMessageWindow(String title, String message)
	{
		JOptionPane.showMessageDialog(this.mFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
		return;
	}
	
	public void writeValuesFromOptionsDialog(int movesAhead, int favorEdges, int favorCorners, int disfavorRegion4)
	{
		this.mEngine.setMovesAhead(movesAhead);
		this.mEngine.setFavorEdges(favorEdges);
		this.mEngine.setFavorCorners(favorCorners);
		this.mEngine.setDisfavorRegion4(disfavorRegion4);
		System.out.println("Moves Ahead: " + this.mEngine.getMovesAhead());
		System.out.println("Favor Edges: " + this.mEngine.getFavorEdges());
		System.out.println("Favor Corners: " + this.mEngine.getFavorCorners());
		System.out.println("DisFavor Region4: " + this.mEngine.getDisfavorRegion4());
		return;
	}
	
	public void setOptionsWindowNull()
	{
		this.mOptionsWindow = null;
		return;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if(source.getSource()==(JButton) cmdNew){
			this.mEngine.resetGame();
			this.mGridPanel.repaint();
		}
		if(source.getSource()==(JButton) cmdOptions){
			if(this.mOptionsWindow == null){
				int x = (int)Math.round((this.mFrame.getWidth() / 2.0) - (Globals.MINIMUM_OPTIONS_WINDOW_WIDTH / 2.0));
				int y = (int)Math.round((this.mFrame.getHeight() / 2.0) - (Globals.MINIMUM_OPTIONS_WINDOW_HEIGHT / 2.0));
				this.mOptionsWindow = new OptionsWindow(this, x, y, this.mEngine.getMovesAhead(), this.mEngine.getFavorEdges(), this.mEngine.getFavorCorners(), this.mEngine.getDisfavorRegion4());
			}else{
				this.mOptionsWindow.forceThisWindowToClose();
				this.mOptionsWindow = null;
			}
		}
		return;
	}
	
	public void mouseClicked(MouseEvent source)	{ return; }
	
	public void mousePressed(MouseEvent e)
	{
		if(this.mOptionsWindow != null){
			this.mOptionsWindow.forceThisWindowToClose();
			this.mOptionsWindow = null;
		}else{
			if(e.getSource() == this.mGridPanel){
				if(e.getPoint() != null){
			    	mMousePosition = e.getPoint();
			    	//System.out.println("Pos: " + mMousePosition.x + ", " + mMousePosition.y);
			    }
				int width = this.mSquares.get(0).getSize();
				int numPieces = this.mSquares.size();
				for(int i = 0; i < numPieces; i++)
				{
					GridSquare aPiece = this.mSquares.get(i);
					if(this.mMousePosition.x >= aPiece.getX() && this.mMousePosition.x <= aPiece.getX() + width){
						if(this.mMousePosition.y >= aPiece.getY() && this.mMousePosition.y <= aPiece.getY() + width){
							this.mEngine.performPlayerMove(GridMath.getX(i), GridMath.getY(i));
							//aPiece.switchImage("white"); // just for testing.
							break;
						}
					}
				}
			}
		}
		return;
	}
	
	public synchronized void mouseEntered(MouseEvent e)
	{
		this.mouseMoved(e);
		return;
	}
	public synchronized void mouseExited(MouseEvent e)
	{
		this.mouseMoved(e);
		return;
	}
	
	public void mouseReleased(MouseEvent e)	{ return; }
	public void mouseDragged(MouseEvent e)	{ return; }
	
	public synchronized void mouseMoved(MouseEvent e)
	{
	    if(e.getPoint() != null){
	    	mMousePosition = e.getPoint();
	    	//System.out.println("Pos: " + mMousePosition.x + ", " + mMousePosition.y);
	    }
	}
	
	public void componentHidden(ComponentEvent e)	{ return; }
	public void componentShown(ComponentEvent e)	{ return; }
	public void componentMoved(ComponentEvent e)	{ return; }
	
	public synchronized void componentResized(ComponentEvent e)
	{
		if(this.allowResize == true){
			this.mGridPanel.reinitializeGridSquares();
			this.mEngine.resetGUIGraphics();
			this.mGridPanel.repaint();
		}
		return;
	}
	
	public void windowActivated ( WindowEvent e )   {   }
	public void windowClosed(WindowEvent e)			{	}
    public void windowDeactivated ( WindowEvent e ) {   }
    public void windowDeiconified ( WindowEvent e ) {   }
    public void windowIconified ( WindowEvent e )   {   }
    public void windowOpened ( WindowEvent e )      {   }
    
    public void windowClosing ( WindowEvent e )
    {
    	this.mFrame.dispose();
    }
}
