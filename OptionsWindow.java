package edu.miu.reversi;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class OptionsWindow implements WindowListener, ActionListener
{
	private JFrame mFrame = null;
	private JPanel mBasePanel = null;
	private JPanel mMainPanel = null;
	private JPanel mStepsAheadPanel = null;
	private JTextField txtMovesAheadBox = null;
	private JPanel mFavorEdgesPanel = null;
	private JTextField txtFavorEdgesTextBox = null;
	private JPanel mFavorCornersPanel = null;
	private JTextField txtFavorCornersTextBox = null;
	private JPanel mDisfavorRegion4Panel = null;
	private JTextField txtDisfavorRegion4TextBox = null;
	private JPanel mButtonPanel = null;
	private JButton cmdOkayButton = null;
	static final private String OKAY_BUTTON = "OK";
	private JButton cmdCancelButton = null;
	static final private String CANCEL_BUTTON = "Cancel";
	
	private int mX = 0;
	private int mY = 0;
	private int mTotalMovesAhead = 3;
	private int mCornerBias = 10;
	private int mEdgeBias = 5;
	private int mRegion4Bias = -5;
	private MainGUI mGUI = null;
	Engine e = new Engine();
	public OptionsWindow(){

	}
	
	public OptionsWindow(MainGUI GUI, int x, int y, int movesAhead, int favorEdges, int favorCorners, int disfavorRegion4)
	{
		this.setGUI(GUI);
		this.setX(x);
		this.setY(y);
		this.setMovesAhead(movesAhead);
		this.setFavorEdges(favorEdges);
		this.setFavorCorners(favorCorners);
		this.setDisfavorRegion4(disfavorRegion4);
		this.initializeWindow();
		return;
	}
	
	private void setGUI(MainGUI GUI)
	{
		if(GUI != null){
			this.mGUI = GUI;
		}else{
			System.out.println("OptionsWindow.setGUI - attempt to pass null object.");
		}
		return;
	}
	
	private void setMovesAhead(int moves)
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
	
	private void setFavorEdges(int favor)
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
	
	private void setFavorCorners(int favor)
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
	
	private void setDisfavorRegion4(int favor)
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
	
	private void initializeWindow()
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
		
		this.mFrame = new JFrame();
		this.mFrame.setTitle(Globals.OPTIONS_WINDOW_TITLE);
		this.mBasePanel = new JPanel();
		this.mBasePanel.setLayout(new BorderLayout());
		this.mMainPanel = new JPanel();
		this.mMainPanel.setLayout(new BoxLayout(this.mMainPanel, BoxLayout.Y_AXIS));
		
		this.initializeFirstRow();
		this.initializeSecondRow();
		this.mMainPanel.add(this.mStepsAheadPanel);
		this.initializeThirdRow();
		this.initializeFourthRow();
		this.mMainPanel.add(this.mFavorEdgesPanel);
		this.initializeFifthRow();
		this.initializeSixthRow();
		this.mMainPanel.add(this.mFavorCornersPanel);
		this.initializeSeventhRow();
		this.initializeEighthRow();
		this.mMainPanel.add(this.mDisfavorRegion4Panel);
		this.initializeNinthRow();
		this.initializeTenthRow();
		this.mMainPanel.add(this.mButtonPanel);
		
		this.txtMovesAheadBox.setText(String.valueOf(this.mTotalMovesAhead));
		this.txtFavorEdgesTextBox.setText(String.valueOf(this.mEdgeBias));
		this.txtFavorCornersTextBox.setText(String.valueOf(this.mCornerBias));
		this.txtDisfavorRegion4TextBox.setText(String.valueOf(this.mRegion4Bias));
		
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.NORTH);
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.WEST);
		this.mBasePanel.add(this.mMainPanel, BorderLayout.CENTER);
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.EAST);
		this.mBasePanel.add(Box.createRigidArea(new Dimension(16, 16)), BorderLayout.SOUTH);
		
		this.mFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.mFrame.setAlwaysOnTop(true);
		this.mFrame.setResizable(false);
		this.mFrame.getContentPane().add(this.mBasePanel);
		this.mFrame.setMinimumSize(new Dimension(Globals.MINIMUM_OPTIONS_WINDOW_WIDTH, Globals.MINIMUM_OPTIONS_WINDOW_HEIGHT));
		this.mFrame.setPreferredSize(new Dimension(Globals.MINIMUM_OPTIONS_WINDOW_WIDTH, Globals.MINIMUM_OPTIONS_WINDOW_HEIGHT));
		this.mFrame.setLocation(this.mX, this.mY);
		this.mFrame.setVisible(true);
		this.mFrame.pack();
		return;
	}
	
	private void initializeFirstRow()
	{
		this.mMainPanel.add(Box.createRigidArea(new Dimension(3000, 5)));
		return;
	}
	
	private void initializeSecondRow()
	{
		this.mStepsAheadPanel = new JPanel();
		this.mStepsAheadPanel.setLayout(new BoxLayout(this.mStepsAheadPanel, BoxLayout.X_AXIS));
		this.mStepsAheadPanel.setPreferredSize(new Dimension(400, 30));
		this.mStepsAheadPanel.setMinimumSize(new Dimension(400, 30));
		
		this.mStepsAheadPanel.add(Box.createRigidArea(new Dimension(25, 20)));
		
		JLabel aLabel = new JLabel("Steps Ahead:");
		Font theFont = new Font("Sans Serif", Font.PLAIN, Globals.LABEL_FONT_SIZE);
		Dimension textSize = TextMetric.getMetric(aLabel.getText(), theFont);
		aLabel.setFont(theFont);
		aLabel.setMinimumSize(textSize);
		aLabel.setPreferredSize(textSize);
		this.mStepsAheadPanel.add(aLabel);
		
		this.mStepsAheadPanel.add(Box.createRigidArea(new Dimension(Globals.AFTER_LABEL_SPACER_WIDTH, 20)));
		
		this.txtMovesAheadBox = new JTextField();
		this.txtMovesAheadBox.setHorizontalAlignment(JTextField.LEFT);
		Font sizingFont = new Font("Sans Serif", Font.PLAIN, Globals.TEXTFIELD_SIZING_FONT);
		textSize = TextMetric.getMetric("XXX", sizingFont);
		this.txtMovesAheadBox.setFont(theFont);
		this.txtMovesAheadBox.setMinimumSize(textSize);
		this.txtMovesAheadBox.setMaximumSize(textSize);
		this.txtMovesAheadBox.setPreferredSize(textSize);
		this.txtMovesAheadBox.setMargin(new Insets(0, 5, 0, 5));
		this.mStepsAheadPanel.add(this.txtMovesAheadBox);
		
		this.mStepsAheadPanel.add(Box.createRigidArea(new Dimension(3000, 20)));
		
		return;
	}
	
	private void initializeThirdRow()
	{
		this.mMainPanel.add(Box.createRigidArea(new Dimension(3000, 15)));
		return;
	}
	
	private void initializeFourthRow()
	{
		this.mFavorEdgesPanel = new JPanel();
		this.mFavorEdgesPanel.setLayout(new BoxLayout(this.mFavorEdgesPanel, BoxLayout.X_AXIS));
		this.mFavorEdgesPanel.setPreferredSize(new Dimension(400, 30));
		this.mFavorEdgesPanel.setMinimumSize(new Dimension(400, 30));
		
		this.mFavorEdgesPanel.add(Box.createRigidArea(new Dimension(25, 20)));
		
		JLabel aLabel = new JLabel("Favor Edges:");
		Font theFont = new Font("Sans Serif", Font.PLAIN, Globals.LABEL_FONT_SIZE);
		Dimension textSize = TextMetric.getMetric(aLabel.getText(), theFont);
		aLabel.setFont(theFont);
		aLabel.setMinimumSize(textSize);
		aLabel.setPreferredSize(textSize);
		this.mFavorEdgesPanel.add(aLabel);
		
		this.mFavorEdgesPanel.add(Box.createRigidArea(new Dimension(Globals.AFTER_LABEL_SPACER_WIDTH, 20)));
		
		this.txtFavorEdgesTextBox = new JTextField();
		this.txtFavorEdgesTextBox.setHorizontalAlignment(JTextField.LEFT);
		Font sizingFont = new Font("Sans Serif", Font.PLAIN, Globals.TEXTFIELD_SIZING_FONT);
		textSize = TextMetric.getMetric("XXX", sizingFont);
		this.txtFavorEdgesTextBox.setFont(theFont);
		this.txtFavorEdgesTextBox.setMinimumSize(textSize);
		this.txtFavorEdgesTextBox.setMaximumSize(textSize);
		this.txtFavorEdgesTextBox.setPreferredSize(textSize);
		this.txtFavorEdgesTextBox.setMargin(new Insets(0, 5, 0, 5));
		this.mFavorEdgesPanel.add(this.txtFavorEdgesTextBox);
		
		this.mFavorEdgesPanel.add(Box.createRigidArea(new Dimension(3000, 20)));
		
		return;
	}
	
	private void initializeFifthRow()
	{
		this.mMainPanel.add(Box.createRigidArea(new Dimension(3000, 15)));
		return;
	}
	
	private void initializeSixthRow()
	{
		this.mFavorCornersPanel = new JPanel();
		this.mFavorCornersPanel.setLayout(new BoxLayout(this.mFavorCornersPanel, BoxLayout.X_AXIS));
		this.mFavorCornersPanel.setPreferredSize(new Dimension(400, 30));
		this.mFavorCornersPanel.setMinimumSize(new Dimension(400, 30));
		
		this.mFavorCornersPanel.add(Box.createRigidArea(new Dimension(25, 20)));
		
		JLabel aLabel = new JLabel("Favor Corners:");
		Font theFont = new Font("Sans Serif", Font.PLAIN, Globals.LABEL_FONT_SIZE);
		Dimension textSize = TextMetric.getMetric(aLabel.getText(), theFont);
		aLabel.setFont(theFont);
		aLabel.setMinimumSize(textSize);
		aLabel.setPreferredSize(textSize);
		this.mFavorCornersPanel.add(aLabel);
		
		this.mFavorCornersPanel.add(Box.createRigidArea(new Dimension(Globals.AFTER_LABEL_SPACER_WIDTH, 20)));
		
		this.txtFavorCornersTextBox = new JTextField();
		this.txtFavorCornersTextBox.setHorizontalAlignment(JTextField.LEFT);
		Font sizingFont = new Font("Sans Serif", Font.PLAIN, Globals.TEXTFIELD_SIZING_FONT);
		textSize = TextMetric.getMetric("XXX", sizingFont);
		this.txtFavorCornersTextBox.setFont(theFont);
		this.txtFavorCornersTextBox.setMinimumSize(textSize);
		this.txtFavorCornersTextBox.setMaximumSize(textSize);
		this.txtFavorCornersTextBox.setPreferredSize(textSize);
		this.txtFavorCornersTextBox.setMargin(new Insets(0, 5, 0, 5));
		this.mFavorCornersPanel.add(this.txtFavorCornersTextBox);
		
		this.mFavorCornersPanel.add(Box.createRigidArea(new Dimension(3000, 20)));
		
		return;
	}
	
	private void initializeSeventhRow()
	{
		this.mMainPanel.add(Box.createRigidArea(new Dimension(3000, 15)));
		return;
	}
	
	private void initializeEighthRow()
	{
		this.mDisfavorRegion4Panel = new JPanel();
		this.mDisfavorRegion4Panel.setLayout(new BoxLayout(this.mDisfavorRegion4Panel, BoxLayout.X_AXIS));
		this.mDisfavorRegion4Panel.setPreferredSize(new Dimension(400, 30));
		this.mDisfavorRegion4Panel.setMinimumSize(new Dimension(400, 30));
		
		this.mDisfavorRegion4Panel.add(Box.createRigidArea(new Dimension(25, 20)));
		
		JLabel aLabel = new JLabel("Disfavor Region4:");
		Font theFont = new Font("Sans Serif", Font.PLAIN, Globals.LABEL_FONT_SIZE);
		Dimension textSize = TextMetric.getMetric(aLabel.getText(), theFont);
		aLabel.setFont(theFont);
		aLabel.setMinimumSize(textSize);
		aLabel.setPreferredSize(textSize);
		this.mDisfavorRegion4Panel.add(aLabel);
		
		this.mDisfavorRegion4Panel.add(Box.createRigidArea(new Dimension(Globals.AFTER_LABEL_SPACER_WIDTH, 20)));
		
		this.txtDisfavorRegion4TextBox = new JTextField();
		this.txtDisfavorRegion4TextBox.setHorizontalAlignment(JTextField.LEFT);
		Font sizingFont = new Font("Sans Serif", Font.PLAIN, Globals.TEXTFIELD_SIZING_FONT);
		textSize = TextMetric.getMetric("XXX", sizingFont);
		this.txtDisfavorRegion4TextBox.setFont(theFont);
		this.txtDisfavorRegion4TextBox.setMinimumSize(textSize);
		this.txtDisfavorRegion4TextBox.setMaximumSize(textSize);
		this.txtDisfavorRegion4TextBox.setPreferredSize(textSize);
		this.txtDisfavorRegion4TextBox.setMargin(new Insets(0, 5, 0, 5));
		this.mDisfavorRegion4Panel.add(this.txtDisfavorRegion4TextBox);
		
		this.mDisfavorRegion4Panel.add(Box.createRigidArea(new Dimension(3000, 20)));
		
		return;
	}
	
	private void initializeNinthRow()
	{
		this.mMainPanel.add(Box.createRigidArea(new Dimension(3000, 30)));
		return;
	}
	
	private void initializeTenthRow()
	{
		this.mButtonPanel = new JPanel();
		this.mButtonPanel.setLayout(new BoxLayout(this.mButtonPanel, BoxLayout.X_AXIS));
		this.mButtonPanel.setPreferredSize(new Dimension(400, 30));
		this.mButtonPanel.setMinimumSize(new Dimension(400, 30));
		
		this.cmdOkayButton = new JButton("Okay");
		Font theFont = new Font("Sans Serif", Font.BOLD, this.mMainPanel.getFont().getSize());
		Dimension textSize = TextMetric.getMetric(this.cmdOkayButton.getText(), theFont);
		this.cmdOkayButton.setMargin(new Insets(Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET, Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET));
		this.cmdOkayButton.setMinimumSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdOkayButton.setPreferredSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdOkayButton.setActionCommand(OKAY_BUTTON);
		this.cmdOkayButton.setToolTipText(OKAY_BUTTON);
		this.cmdOkayButton.addActionListener(this);
		this.mButtonPanel.add(this.cmdOkayButton);
		
		this.mButtonPanel.add(Box.createRigidArea(new Dimension(Globals.AFTER_COMPONENT_SPACER_WIDTH, 30)));
		
		this.cmdCancelButton = new JButton("Cancel");
		theFont = new Font("Sans Serif", Font.BOLD, this.mMainPanel.getFont().getSize());
		textSize = TextMetric.getMetric(this.cmdOkayButton.getText(), theFont);
		this.cmdCancelButton.setMargin(new Insets(Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET, Globals.BUTTON_VERTICAL_INSET, Globals.BUTTON_HORIZONTAL_INSET));
		this.cmdCancelButton.setMinimumSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdCancelButton.setPreferredSize(new Dimension(textSize.width + (Globals.BUTTON_HORIZONTAL_INSET * 2), textSize.height + (Globals.BUTTON_VERTICAL_INSET * 2)));
		this.cmdCancelButton.setActionCommand(CANCEL_BUTTON);
		this.cmdCancelButton.setToolTipText(CANCEL_BUTTON);
		this.cmdCancelButton.addActionListener(this);
		this.mButtonPanel.add(this.cmdCancelButton);
		
		return;
	}
	
	private void setX(int x)
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		if(x >= 0 && x < dim.width){
			this.mX = x;
		}else{
			this.mX = 0;
		}
		return;
	}
	
	private void setY(int y)
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		if(y >= 0 && y < dim.height){
			this.mY = y;
		}else{
			this.mY = 0;
		}
		return;
	}
	
	public void saveValues()
	{
		if(this.txtMovesAheadBox.getText().length() > 0){
			this.mTotalMovesAhead = Integer.valueOf(this.txtMovesAheadBox.getText());
		}
		if(this.txtFavorEdgesTextBox.getText().length() > 0){
			this.mEdgeBias = Integer.valueOf(this.txtFavorEdgesTextBox.getText());
		}
		if(this.txtFavorCornersTextBox.getText().length() > 0){
			this.mCornerBias = Integer.valueOf(this.txtFavorCornersTextBox.getText());
		}
		if(this.txtDisfavorRegion4TextBox.getText().length() > 0){
			this.mRegion4Bias = Integer.valueOf(this.txtDisfavorRegion4TextBox.getText());
		}
		this.mGUI.writeValuesFromOptionsDialog(this.mTotalMovesAhead, this.mEdgeBias, this.mCornerBias, this.mRegion4Bias);
		this.forceThisWindowToClose();
		return;
	}
	
	public void cancelOptions()
	{
		boolean hasChanged = false;
		if(Integer.parseInt(this.txtMovesAheadBox.getText()) != this.mTotalMovesAhead){
			hasChanged = true;
		}
		if(Integer.parseInt(this.txtFavorEdgesTextBox.getText()) != this.mEdgeBias){
			hasChanged = true;
		}
		if(Integer.parseInt(this.txtFavorCornersTextBox.getText()) != this.mCornerBias){
			hasChanged = true;
		}
		if(Integer.parseInt(this.txtDisfavorRegion4TextBox.getText()) != this.mRegion4Bias){
			hasChanged = true;
		}
		if(hasChanged == true){
			int response = JOptionPane.showConfirmDialog(this.mFrame, "One or more values have changed.\n\nAre you sure you want to cancel?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
			if(response == JOptionPane.YES_OPTION){
				this.forceThisWindowToClose();
			}
		}else{
			this.forceThisWindowToClose();
		}
		return;
	}
	
	public void actionPerformed(ActionEvent source)
	{
		if (source.getSource()==(JButton) this.cmdOkayButton){
			this.saveValues();
		}
		if (source.getSource()==(JButton) this.cmdCancelButton){
			this.cancelOptions();
		}
		return;
	}
	
	public void windowActivated ( WindowEvent e )   {   }
	public void windowClosed(WindowEvent e)			{ return; }
    public void windowDeactivated ( WindowEvent e ) {   }
    public void windowDeiconified ( WindowEvent e ) {   }
    public void windowIconified ( WindowEvent e )   {   }
    public void windowOpened ( WindowEvent e )      {   }
    
    public void windowClosing ( WindowEvent e )
    {
    	this.cancelOptions();
    }
    
    public void forceThisWindowToClose()
    {
    	this.mFrame.dispose();
    	this.mGUI.setOptionsWindowNull();
    }
}
