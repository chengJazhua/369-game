package project;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.border.*;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import java.util.ArrayList; 
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
public class panel extends JPanel implements MouseListener{
   
	JPanel panel;
	JButton start;
	JButton stop;
	int player1 = 0;
	int player2 = 0;
	boolean player1Turn = true;
	boolean started = false;
	JCheckBox reqSquares;
	int mouseLocX;
	int mouseLocY;
	String status = "";
	
	int[][] board = new int[9][9];
	ArrayList<String> posLoc = new ArrayList<String>();
	
   
   
   public panel() {
	   
	   JFrame window = new JFrame();
	   window.setSize(800,800);  
	   window.setLocation(1, 1);				         
	   window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   window.setVisible(true);
      
	   addMouseListener(this);
	   
	   panel = new JPanel();
	   panel.setLayout(new BorderLayout());
	   
	   start = new JButton("Start");
	   start.addActionListener(new Listener_Start());
	   start.setSize(100, 50);
	   start.setLocation(100, 200);
	   start.setVisible(true);
	   
	   stop = new JButton("Stop");
	   stop.addActionListener(new Listener_Stop());
	   stop.setSize(200, 200);
	   stop.setLocation(100, 200);
	   stop.setVisible(true);
	   stop.setEnabled(false);
	   
	   panel.add(start, BorderLayout.SOUTH);
	   panel.add(stop, BorderLayout.NORTH);
	   
	   reqSquares = new JCheckBox("Required lines");
	   reqSquares.setBounds(100, 100, 50, 50);
	   panel.add(reqSquares);
     
	   window.setContentPane(this);		
	   window.add(panel);
	   window.setVisible(true);

	   
	   

      repaint();
   }
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      showBoard(g);					      
   }
   
   
   public void showBoard(Graphics g){
	   int count = 0;
	   int length = (int) (this.getWidth()*.75);
	   int height = (int) (this.getHeight()*.75);
	   g.setColor(Color.black);
	   
	   if (reqSquares.isSelected())
		   checkValidSquares();
	   for (int i = 0; i < board.length; i++) {
		   for (int j = 0; j < board[0].length; j++) {
			   if (started) {
				   if (mouseLocX > length/9*(i+1)+50 && mouseLocX < length/9*(i+2)+50
				   		&& mouseLocY > height/9*(j+1)+50 && mouseLocY < height/9*(j+2)+50) {
				   	if (board[i][j] == 0) {
				   		if (posLoc.isEmpty() || posLoc.contains(i+","+j)) {
				   			board[i][j] = 1;
				   			if (!awardPoints(i, j))
				   				player1Turn = !player1Turn;
				   			posLoc = new ArrayList<String>();
				   			status = "";
				   		}
				   		else {
				   			status = "Please select a valid square (can make a 3,6,or 9)";
				   		}
				   	}
				   
			   	}
			   }
			   
			   g.drawRect(length/9*(i+1)+50, height/9*(j+1)+50, length/9, height/9);
			   g.setColor(Color.BLACK);
			   if (board[i][j] == 1) {
				   g.drawLine(length/9*(i+1)+50, height/9*(j+1)+50, length/9*(i+2)+50, height/9*(j+2)+50);
				   g.drawLine(length/9*(i+2)+50, height/9*(j+1)+50, length/9*(i+1)+50, height/9*(j+2)+50);
				   count++;
			   }
			   
		   }
	   }
	   for (int i = 0; i < posLoc.size(); i++) {
		   String[] split = posLoc.get(i).split("\\,");
		   int x = Integer.parseInt(split[0]);
		   int y = Integer.parseInt(split[1]);
		   g.setColor(Color.CYAN);
		   g.drawRect(length/9*(x+1)+50, height/9*(y+1)+50, length/9, height/9);
	   }
	   
	   if (count == 81) {
		   if (player1 > player2)
			   status = "Game over: Player 1 wins";
		   else
			   status = "Game over: Player 2 wins";
		   
	   }
	  
	   
	   g.setColor(Color.BLACK);
	   g.drawString("Player 1: " + player1, 50, 25);
	   g.drawString("Player 2: " + player2, 50, 75);
	   g.drawString(status, 300, 100);
	   
	   
   
   }
   
   public boolean awardPoints(int i, int j) {
	   int count = 0;
	   boolean toReturn = false;
	   boolean includes = false;
	   for (int k = 0; k < board.length; k++) {
		   if (board[i][k] == 1)
			   count++;
		   if (k == j)
			   includes = true;
		   if (board[i][k] == 0 || k == board.length-1) {
			   if (includes && (count == 3 || count == 6 || count == 9)) {
				   if (player1Turn)
					   player1 += count;
				   else
					   player2 += count;
				   toReturn = true;
			   }
				   
			   count = 0;
			   includes = false;
		   }
			   
	   }
	   
	   for (int k = 0; k < board[0].length; k++) {
		   if (board[k][j] == 1)
			   count++;
		   if (k == i)
			   includes = true;
		   if (board[k][j] == 0 || k == board.length-1) {
			   if (includes && (count == 3 || count == 6 || count == 9)) {
				   if (player1Turn)
					   player1 += count;
				   else
					   player2 += count;
				   toReturn = true;
				   
			   }
				   
			   count = 0;
			   includes = false;
		   }
			   
	   }
	   
	   return toReturn;
   }
   
   public void checkValidSquares() {
	   
	   for (int i = 0; i < board.length; i++) {
		   for (int j = 0; j < board[0].length; j++) {
			   if (board[i][j] == 0 && checkPoints(i,j))
				   posLoc.add(i +"," + j);
		   }
	   }
   }
   
   public boolean checkPoints(int i, int j) {
	   int count = 0;
	   boolean includes = false;
	   for (int k = 0; k < board.length; k++) {
		   if (board[i][k] == 1)
			   count++;
		   
		   if (board[i][k] == 0 || k == board.length-1) {
			   if (k == j) {
				   includes = true;
				   count++;
			   }
			   else {
				   if (includes && (count == 3 || count == 6 )) 
					   return true;
				   count = 0;
				   includes = false;
			   }
			   if (k == board.length-1) {
				   if (includes && (count == 3 || count == 6 )) 
					   return true;
			   
			   }
		   }
			   
	   }
	   if (count == 9) 
		   return true;
	   
	   for (int k = 0; k < board[0].length; k++) {
		   if (board[k][j] == 1)
			   count++;
		   if (board[k][j] == 0 || k == board.length-1) {
			   if (k == i) {
				   includes = true;
				   count++;
			   }
			   else {
				   if (includes && (count == 3 || count == 6 )) 
					   return true;
				   count = 0;
				   includes = false;
			   }
			   if (k == board.length-1) {
				   if (includes && (count == 3 || count == 6 )) 
					   return true;
			   
			   }
		   }
			   
	   }
	   if (count == 9) {
		   return true;
	   }
	   return false;
	   
   }
   
   public void mouseClicked(MouseEvent e) {  
	   mouseLocX = e.getX();
	   mouseLocY = e.getY();
	   
	   repaint();
   }  
   public void mouseEntered(MouseEvent e) {  
   }  
   public void mouseExited(MouseEvent e) {  
   }  
   public void mousePressed(MouseEvent e) {  
   }  
   public void mouseReleased(MouseEvent e) {  
   }  
   
   
 
   
   private class Listener_Start implements ActionListener{
      public void actionPerformed( ActionEvent evt){
      
    	  player1Turn = true;
    	  start.setEnabled(false);
    	  stop.setEnabled(true);
    	  started = true;
    	  mouseLocX = 0;
    	  mouseLocY = 0;
    	  reqSquares.setEnabled(false);
    	  repaint();
   
      }
   }
   
   private class Listener_Stop implements ActionListener{
	      public void actionPerformed( ActionEvent evt){
	      
	    	  player1 = 0;
	    	  player2 = 0;
	    	  start.setEnabled(true);
	    	  stop.setEnabled(false);
	    	  board = new int[9][9];
	    	  started = false;
	    	  posLoc = new ArrayList<String>();
	    	  reqSquares.setEnabled(true);
	    	  repaint();
	         
	   
	      }
	   }
   

      
        
   
   
  
}
 




