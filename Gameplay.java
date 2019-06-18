	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.util.Random;

	import javax.swing.*;

public class Gameplay {
	
	    // Constants
		public static final int BUTTONNUM = 20; //number of cells
		public static final int NUMIMAGES = 10;
		public static final int EMPTYCELL = 0;                           // Empty Unopened cell
		public static final int MINECELL = 1;                           // Unopened mine
		public static final int EMPTYCOVERED = 2;                       // flagged empty cell
		public static final int MINECOVERED = 4;                        // flagged mine
		public static final int FLIPPED = -1; 
		public static boolean GAMEOVER = false;// opened empty cell
		
		// Variables
		public int NumOfMines = 50;                            // 
		public static int flipCount = 0;                                // Count how many empty cells have been flipped
		public int win = BUTTONNUM*BUTTONNUM - NumOfMines;       // use flipCount to determine winning number of empty cells
		public static int countMines = 0;                               // Number of mines around
		public boolean trig;                                         // Make sure we only allow a flipped cell to be checked the first time we 
		JButton[][] buttons = new JButton[BUTTONNUM][BUTTONNUM]; // Array of Buttons
		public static int[][] GamePlay = new int[BUTTONNUM][BUTTONNUM];        // Array with information of how the game goes.
		JFrame f1 = new JFrame("Minesweeper by Bwanaz");
		JPopupMenu p1 = new JPopupMenu();
		JMenuItem m = new JMenuItem();
		JMenuBar menuBar = new JMenuBar();
		public int i,j,x,y, xindex, yindex;                              // indexes used for array references.
		public int count = 0;
		ImageIcon[] img = new ImageIcon[NUMIMAGES];
		JLabel[][] lable = new JLabel[BUTTONNUM][BUTTONNUM];
		Random rand = new Random();
		
		//------------------------------------------------------------------------------------------------------------------
		
		/* Author: Brandon Bwanakocha.
		 * Date: 28 May 2019
		 * Purpose: Populates the GamePlay array which is where the progress of the game is stored.
		 * Parameters: void.
		 * Return type: void.
		 * */
		protected void populate() {
			for(i = 0; i < BUTTONNUM; i++) {
				for(j = 0; j <BUTTONNUM; j++)
					GamePlay[i][j] = EMPTYCELL;                     // Start by making every cell empty
			}
			while(count < NumOfMines) {
				xindex =Math.abs(rand.nextInt() % BUTTONNUM);       // Choose random indexes and add mines
				yindex = Math.abs(rand.nextInt() % BUTTONNUM);
				
				if(GamePlay[xindex][yindex] == EMPTYCELL ) { 
				GamePlay[xindex][yindex] = MINECELL;
				count++;
				}		
			}
		}
		
		//------------------------------------------------------------------------------------------------------------
		/* Author: Brandon Bwanakocha.
		 * Date: 28 May 2019
		 * Purpose: Checks the status of a cell when left clicked and determines what happens next in the game.
		 * Parameters: result - Status of the cell that has been left clicked.
		 * Return type: void.
		 * */
		
		protected void check(int result) {
			switch(result) {                     
			case EMPTYCELL:                            // if current cell is empty, flip it and show it's empty
				GamePlay[x][y] = FLIPPED;
				buttons[x][y].setVisible(false);
				flipCount++;
				if(flipCount == win) {    // Check winning condition
					p1.show(f1,200,200);
					GAMEOVER = true;
				}	
				else {
					trig = false;                         // allow moreEmpty to be called only once for an empty cell
					moreEmpty(x,y);                   // Check for more empty cells
					if(flipCount == win)   {           // Check for winning condition again
						p1.show(f1,200,200);
						GAMEOVER = true; // replaced by same winning logic
					}
				}	
				break;	
			case MINECELL:                             // What happens when game is over
				GameOver();
			    GAMEOVER = true;
				break;	
			case EMPTYCOVERED:
				// Do nothing        
				break;
			case MINECOVERED:
				// Do nothing
				break;
			default:
				// Do nothing
			}
		}
		//---------------------------------------------------------------------------------------------------------
		
		/* Author: Brandon Bwanakocha.
		 * Date: 28 May 2019
		 * Purpose: Creates a 2D array of buttons and adds them onto a Jframe for our game interfacing.
		 * Parameters: void.
		 * Return type: void.
		 * */
		public void Play() {
			
			populate();   // Populate gameplay array
			for(i = 0; i<NUMIMAGES;i++) {
				j = i+1;
				img[i] = new ImageIcon("C:/Users/brend/eclipse-workspace/image" + j + ".png");
			}
			
			m = new JMenuItem("Play Again");
			
			for(i = 0; i <BUTTONNUM; i++) {                                // initialize all buttons using a loop
				for(j = 0; j < BUTTONNUM; j++) {
					buttons[i][j] = new JButton();
					buttons[i][j].setBounds(i*20+50,j*20+50,20,20);
					buttons[i][j].addMouseListener(new Functionality());
					lable[i][j] = new JLabel();
					f1.add(buttons[i][j]);	
					f1.add(lable[i][j]);
				}
			}
			p1.add(m);
			f1.add(p1);
			f1.setJMenuBar(menuBar);
			f1.setSize(510,530);
			f1.setLayout(null);
			f1.setVisible(true);
		}	
		//------------------------------------------------------------------------------------------------------------
		private class Functionality extends MouseAdapter{
			
			/* Author: Brandon Bwanakocha.
			 * Date: 28 May 2019
			 * Purpose: Determines what happens depending on which mouse button is clicked.
			 * Parameters: void.
			 * Return type: void.
			 * */	
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(GAMEOVER) {
					// do nothing
				}
				else {
				
				for(x = 0; x < BUTTONNUM; x++) {
					for( y = 0; y < BUTTONNUM; y++) {
						if(e.getSource()== buttons[x][y]) {     // get index of button pressed
							// if left click
							if(e.getButton() == MouseEvent.BUTTON3) {   // What to do when right clicked

								// Determine what to do when right clicked!
								switch (GamePlay[x][y]) {
								
								case EMPTYCELL:
									GamePlay[x][y] = EMPTYCOVERED;
									buttons[x][y].setIcon(img[8]);
								    break;
								case MINECELL:
									GamePlay[x][y] = MINECOVERED;
									buttons[x][y].setIcon(img[8]);
									break;
								case EMPTYCOVERED:
									GamePlay[x][y] = EMPTYCELL;
									buttons[x][y].setIcon(null);
									break;
								case MINECOVERED:
									GamePlay[x][y] = MINECELL;
									buttons[x][y].setIcon(null);
									break;
								default:
									 // Do nothing lmao 
									break;
								}
							}
							if(e.getButton() == MouseEvent.BUTTON1) {   // What to do when left clicked
								check(GamePlay[x][y]);					
							}
						}			
					}
				}		
				}		
			}	
		}

		/* Author: Brandon Bwanakocha.
		 * Date: 28 May 2019
		 * Purpose: Finds more empty cells around using a recursive method that goes up,
		 * 			checks left and right then it comes back down.
		 * Parameters: void.
		 * Return type: void.
		 * */
		public void moreEmpty(int a, int b) {

			if(a < 0 || a > BUTTONNUM-1 || b < 0 || b > BUTTONNUM -1 )  
				return;
			if(trig == false)
				flipCount--;
			if(trig == true && GamePlay[a][b] == FLIPPED)
				return;
			if(GamePlay[a][b] == MINECELL || GamePlay[a][b] == MINECOVERED || GamePlay[a][b] == EMPTYCOVERED )
				return;
			if(checkAround(a,b)!=0 ) {
				GamePlay[a][b] = FLIPPED;
				buttons[a][b].setVisible(false);
				lable[a][b].setIcon(img[checkAround(a,b)-1]);
				lable[a][b].setBounds(a*20+50,b*20+50,20,20);
				flipCount++;
				return;
			}
			trig = true;    // Prevents stack overflow during recursion because it avoids checking a cell more than once.
			GamePlay[a][b] = FLIPPED;
			buttons[a][b].setVisible(false);
			flipCount++;
			moreEmpty(a,b+1);     // Recursively determine if there is nearby cells that have 8 empty cells around them
			moreEmpty(a+1,b+1);
			moreEmpty(a+1,b-1);
			moreEmpty(a-1,b+1);
			moreEmpty(a-1,b-1);
			moreEmpty(a,b-1);
			moreEmpty(a+1, b);
			moreEmpty(a-1, b);

		}
		//-----------------------------------------------------------------------------------------------------------
		
		/* Author: Brandon Bwanakocha.
		 * Date: 28 May 2019
		 * Purpose: Counts the number of mines in the 8 cells around current cell.
		 * NOTE: this is probably the most cumbersome function in this code but it's very straight forward.
		 * Parameters: a,b are the x and y indexes of the current cell.
		 * Return type: countMines is the number of mines around a cell.
		 * */
		
		private void GameOver() {
		//	p1.show(f1,200,200);
			for(i = 0; i < BUTTONNUM; i++)
			    for(j = 0; j < BUTTONNUM; j++){
			    	if(GamePlay[i][j] == MINECELL || GamePlay[i][j] == MINECOVERED)
			    		buttons[i][j].setIcon(img[9]);
			    }	
			infoBox("You Lost!", "GameOver");
		}
		private int checkAround(int a, int b) {
			countMines = 0;
			if(a ==0 && b == 0) {                // if top left corner
				if(GamePlay[a+1][b] == MINECELL || GamePlay[a+1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a+1][b+1] == MINECELL || GamePlay[a+1][b+1]==MINECOVERED)
					countMines++;
				if(GamePlay[a][b+1] == MINECELL || GamePlay[a][b+1]==MINECOVERED)
					countMines++;	
			}
			else if(a == BUTTONNUM -1 && b == 0) {  // if top right corner
				if(GamePlay[a-1][b] == MINECELL || GamePlay[a-1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b+1] == MINECELL || GamePlay[a-1][b+1]==MINECOVERED)
					countMines++;
				if(GamePlay[a][b+1] == MINECELL || GamePlay[a][b+1]==MINECOVERED)
					countMines++;	
			}
			else if(a == BUTTONNUM-1 && b == BUTTONNUM-1) { // if bottom right corner 
				
				if(GamePlay[a-1][b] == MINECELL || GamePlay[a-1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b-1] == MINECELL || GamePlay[a-1][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a][b-1] == MINECELL || GamePlay[a][b-1]==MINECOVERED)
					countMines++;
			}
			else if(a == 0 && b == BUTTONNUM -1) {   // if bottom left corner
				if(GamePlay[a+1][b] == MINECELL || GamePlay[a+1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a+1][b-1] == MINECELL || GamePlay[a+1][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a][b-1] == MINECELL || GamePlay[a][b-1]==MINECOVERED)
					countMines++;	
			}
			else if(a ==0){  // if left edge
				
				if(GamePlay[a][b+1] == MINECELL || GamePlay[a][b+1]==MINECOVERED)
					countMines++;	
				if(GamePlay[a+1][b+1] == MINECELL || GamePlay[a+1][b+1]==MINECOVERED)
					countMines++;
				if(GamePlay[a+1][b] == MINECELL || GamePlay[a+1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a+1][b-1] == MINECELL || GamePlay[a+1][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a][b-1] == MINECELL || GamePlay[a][b-1]==MINECOVERED)
					countMines++;
			}
			else if(b == 0) {	 // if top edge
				if(GamePlay[a][b+1] == MINECELL || GamePlay[a][b+1]==MINECOVERED)
					countMines++;
				if(GamePlay[a+1][b+1] == MINECELL || GamePlay[a+1][b+1]==MINECOVERED)
					countMines++;
				if(GamePlay[a+1][b] == MINECELL || GamePlay[a+1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b] == MINECELL || GamePlay[a-1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b+1] == MINECELL || GamePlay[a-1][b+1]==MINECOVERED)
					countMines++;	
			}
			else if (b == BUTTONNUM-1) {   // if bottom edge
				if(GamePlay[a+1][b] == MINECELL || GamePlay[a+1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a+1][b-1] == MINECELL || GamePlay[a+1][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a][b-1] == MINECELL || GamePlay[a][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b-1] == MINECELL || GamePlay[a-1][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b] == MINECELL || GamePlay[a-1][b]==MINECOVERED)
					countMines++;	
			}
			else if(a == BUTTONNUM-1) {  // if right edge
				 
				if(GamePlay[a][b+1] == MINECELL || GamePlay[a][b+1]==MINECOVERED)
					countMines++;
				if(GamePlay[a][b-1] == MINECELL || GamePlay[a][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b-1] == MINECELL || GamePlay[a-1][b-1]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b] == MINECELL || GamePlay[a-1][b]==MINECOVERED)
					countMines++;
				if(GamePlay[a-1][b+1] == MINECELL || GamePlay[a-1][b+1]==MINECOVERED)
					countMines++;	
			}	
			else {  // if we have atleast one row and column sorrounding current cell
			if(GamePlay[a][b+1] == MINECELL || GamePlay[a][b+1]==MINECOVERED)
				countMines++;
			if(GamePlay[a+1][b+1] == MINECELL || GamePlay[a+1][b+1]==MINECOVERED)
				countMines++;
			if(GamePlay[a+1][b] == MINECELL || GamePlay[a+1][b]==MINECOVERED)
				countMines++;
			if(GamePlay[a+1][b-1] == MINECELL || GamePlay[a+1][b-1]==MINECOVERED)
				countMines++;
			if(GamePlay[a][b-1] == MINECELL || GamePlay[a][b-1]==MINECOVERED)
				countMines++;
			if(GamePlay[a-1][b-1] == MINECELL || GamePlay[a-1][b-1]==MINECOVERED)
				countMines++;
			if(GamePlay[a-1][b] == MINECELL || GamePlay[a-1][b]==MINECOVERED)
				countMines++;
			if(GamePlay[a-1][b+1] == MINECELL || GamePlay[a-1][b+1]==MINECOVERED)
				countMines++;
			}
			return countMines;		
		}
	    public static void infoBox(String infoMessage, String titleBar)
	    {
	    	JOptionPane.showConfirmDialog(null,
	    			"Play Again", "Game Over", JOptionPane.YES_NO_OPTION);
	    }
		
	// ---------------------------------------------------------------------------------------------------
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			Gameplay obj = new Gameplay();
			obj.Play();
		}

	


	public Gameplay() {
		// TODO Auto-generated constructor stub
	}

}
