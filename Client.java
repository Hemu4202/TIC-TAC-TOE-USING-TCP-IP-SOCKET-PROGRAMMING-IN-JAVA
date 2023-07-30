import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.util.Timer;
public class Client extends JPanel
{
private char playerMark;
private int playerID;
private int otherPlayer;
private int x;
private int currentplayerscore;
private int OppplayerScore;
private boolean setButtons;
private JButton[] buttons = new JButton[9];
private clientConnection CC;
private JFrame window;
private Timer timer;
Scanner sc = new Scanner(System.in);
public Client()
{
//intializing values
setButtons = false;
x = 0;
playerID = 0;
otherPlayer = 0;
currentplayerscore = 0;
OppplayerScore = 0;
}
public void buildGui()
{
//cretes GUI for client
window = new JFrame("player #" + playerID);
window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
window.getContentPane().add(this);
window.setBounds(450,450,450,450);
window.setLocationRelativeTo(null);
setLayout(new GridLayout(3,3));
//setting values of Player ID for both players
if(playerID == 1)
{

xviii

otherPlayer = 2;
setButtons = true;
toggleButtons();
}
else
{
otherPlayer = 1 ;
setButtons = false;
toggleButtons();
Thread t2 = new Thread(new Runnable()
{
public void run()
{
updateTurn();
}
});
t2.start();
}
window.setVisible(true);
}
public void initializeButtons()
{
for(int i = 0; i <= 8; i++)
{
buttons[i] = new JButton();
buttons[i].setText(" ");
buttons[i].setBackground(Color.WHITE);
add(buttons[i]);
}
}
//button clicked then this function will invoke
public void keyPress()
{
ActionListener al = new ActionListener()
{
public void actionPerformed(ActionEvent ae)
{
JButton buttonClicked = (JButton) ae.getSource();
buttonClicked.setText(String.valueOf(playerMark));
buttonClicked.setBackground(Color.GREEN);
for(x = 0 ; x<9 ; x++)
{

//to find out which button clicked

if(buttonClicked == buttons[x])
{
break;
}
}

xix

CC.sendButtonVal(x);//sending that button value to server
System.out.println("value sent to server");
setButtons = false;
toggleButtons();
displaywinner();
System.out.println("You Clicked button no "+ x+" Wait for Player #" +
otherPlayer);
Thread t = new Thread(new Runnable()
{
public void run()
{
updateTurn();
}
});
t.start();
}
};
for(int i=0;i<9;i++)
{
buttons[i].addActionListener(al);
}
}
//after sending button value waits for Server to recieve opp move
public void updateTurn()
{
//as soon as move is done timer starts
timer = new Timer();
TimerTask task = new TimerTask()
{
public void run()
{
System.out.println("Time out !!");
JOptionPane.showMessageDialog(window,"Other player left You won
!!");
CC.closeConnection();
System.exit(0);
}
};
timer.schedule(task,15000);
int n = CC.recButtonVal();
oppMove(n);
//recieves opponent move and reflected in GUI and Command Prompt
System.out.println("Opponent feedback has been written");
setButtons = true;
toggleButtons();
timer.cancel();
timer.purge();
//within 10 sec if other player makes his move then timer stops

xx

}
public void toggleButtons()
{
if(setButtons == false)
{
for (int i=0;i<9;i++)
{
buttons[i].setEnabled(setButtons);
}
}
else
{
for(int i =0;i<9;i++)
{
if(buttons[i].getText().charAt(0) == ' ')

{

buttons[i].setEnabled(setButtons);
}
}
}
}
// To write opponent move
public void oppMove(int oppval)
{
char oppPlayerMark;
if (playerMark == 'X')
{
oppPlayerMark = 'O';
}
else
{
oppPlayerMark ='X';
}
buttons[oppval].setText(String.valueOf(oppPlayerMark));
buttons[oppval].setBackground(Color.RED);
buttons[oppval].setEnabled(false);
displaywinner();
}
public void playerMarkDecider()
{
if (playerID == 1)
{
playerMark = 'X';
}
else
{
playerMark = 'O';

xxi

}
}
public void UpdatePoints(char x)
{
if(x == playerMark)
{
currentplayerscore++;
}
else
{
OppplayerScore++;
}
}
//checks and displays winner
public void displaywinner()
{
if(checkwinner('X') == true)
{
timer.cancel();
timer.purge();
int dialogResult = JOptionPane.showConfirmDialog(window, "X wins. \n
you(#1):"+currentplayerscore+" \t opponent(#2):"+OppplayerScore+" \n Would you
like to play again?","Game over.",JOptionPane.YES_NO_OPTION);
if(dialogResult == JOptionPane.YES_OPTION)
{

resetbuttons();
}
else
{
CC.closeConnection();
System.exit(0);
}
}
else if(checkwinner('O') == true)
{
timer.cancel();
timer.purge();
int dialogResult = JOptionPane.showConfirmDialog(window, "O wins. \n
you(#1):"+currentplayerscore+" \t opponent(#2):"+OppplayerScore+" \n Would you
like to play again?","Game over.",JOptionPane.YES_NO_OPTION);
if(dialogResult == JOptionPane.YES_OPTION)
{
resetbuttons();
}
else
{
CC.closeConnection();
System.exit(0);

xxii

}
}
else if(checkDraw())
{
timer.cancel();
timer.purge();
int dialogResult = JOptionPane.showConfirmDialog(window,"Draw \n
you(#1):"+currentplayerscore+" \t opponent(#2):"+OppplayerScore+" \n Would you
like to play again?","Game over.", JOptionPane.YES_NO_OPTION);
if(dialogResult == JOptionPane.YES_OPTION)
{

resetbuttons();

}
else
{
CC.closeConnection();
System.exit(0);
}
}
}
private void resetbuttons()
{
if(playerMark == 'O')
{
playerMark = 'O';
setButtons = true;
}
else
{
playerMark = 'X' ;
setButtons = false;
}
toggleButtons();
for(int i =0;i<9;i++)
{
buttons[i].setText(" ");
buttons[i].setBackground(Color.WHITE);
}
}
public boolean checkDraw()
{
boolean full = true;
for(int i = 0 ; i<9;i++)
{
if(buttons[i].getText().charAt(0) == ' ')
{
full = false;
}

xxiii

}
return full;
}
//checks for winner
public boolean checkwinner(char x)
{
if(checkRows(x) == true || checkColumns(x) == true || checkDiagonals(x)
==true)
{
return true;
}
else
{
return false;
}
}
//checks rows for a win
public boolean checkRows(char x)
{
int i = 0;
for(int j = 0;j<3;j++)
{
if( buttons[i].getText().equals(buttons[i+1].getText()) &&
buttons[i].getText().equals(buttons[i+2].getText()) &&
buttons[i].getText().charAt(0) != ' ' &&
buttons[i].getText().charAt(0) == x)
{
UpdatePoints(x);
return true;
}
i = i+3;
}
return false;
}
//checks columns for a win
public boolean checkColumns(char x)
{
int i = 0;
for(int j = 0;j<3;j++)
{
if( buttons[i].getText().equals(buttons[i+3].getText()) &&
buttons[i].getText().equals(buttons[i+6].getText())&&
buttons[i].getText().charAt(0) != ' ' &&
buttons[i].getText().charAt(0) == x)
{
UpdatePoints(x);
return true;
}

xxiv

i++;
}
return false;
}
//checks diagonals for win
public boolean checkDiagonals(char x)
{
if(buttons[0].getText().equals(buttons[4].getText()) &&
buttons[0].getText().equals(buttons[8].getText())&&
buttons[0].getText().charAt(0) !=' ' && buttons[0].getText().charAt(0) == x)
{
UpdatePoints(x);
return true;
}
else if(buttons[2].getText().equals(buttons[4].getText()) &&
buttons[2].getText().equals(buttons[6].getText())&&
buttons[2].getText().charAt(0) !=' ' && buttons[2].getText().charAt(0) == x)
{
UpdatePoints(x);
return true;
}
else return false;
}
public void connectToserver()
{
CC = new clientConnection();
}
private class clientConnection
{
private Socket socket;
private DataInputStream Din;
private DataOutputStream Dout;
private String ip;
public clientConnection()
{
try
{
System.out.println("Client side.");
System.out.println("Enter IP address:");
ip = sc.nextLine();
socket = new Socket(ip,26771);
Din = new DataInputStream(socket.getInputStream());
Dout = new DataOutputStream(socket.getOutputStream());
playerID = Din.readInt();
System.out.println("player # " + playerID + "is connected to server");
}
catch (IOException ex)
{

xxv

System.out.println("IOException in client constructor");

System.exit(0);

}
}
public void sendButtonVal(int a)
{
try
{
Dout.writeInt(a);
Dout.flush();
System.out.println("value sent to server !");
}
catch(IOException ex)
{
//Timeout happened and player tries to make move after timeout then
this window will be displayed
JOptionPane.showMessageDialog(window,"Timeout You Lost
!!","Alert",JOptionPane.WARNING_MESSAGE);
System.out.println("Timeout !!");
CC.closeConnection();
System.exit(0);
}
}
//used to recieve value from server
public int recButtonVal()
{
int n=-1;
try
{
n = Din.readInt();
System.out.println("Value received from server");
}
catch (IOException ex)
{
System.out.println("error in recButton()");
}
return n;
}
//closes client side connection
public void closeConnection()
{
try
{
socket.close();
System.out.println("Socket closed successfully");
}
catch(IOException ex)
{

xxvi

System.out.println("IOException while closing");
}
}
}
public static void main(String[] args)
{
Client cl = new Client();
cl.connectToserver();
cl.playerMarkDecider();
cl.initializeButtons();
cl.buildGui();
cl.keyPress();
}
}
