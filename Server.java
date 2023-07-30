import java.util.*;
import java.net.*;
import java.io.*;

public class Server
{
private int playerCount;
private int player1val;
private int player2val;
private clienthandler player1;
private clienthandler player2;
private ServerSocket ss;

Scanner sc = new Scanner(System.in);
public Server()
{
playerCount = 0;
try
{
ss = new ServerSocket(26771);
//creates Server
}
catch(IOException ex)
{
System.out.println("IOException in server Constructor");
}
}
private void start()

xiii

{
try
{
System.out.println("Waiting for clients to connect with the
server.......");
while (playerCount < 2) //accepts 2 clients
{
Socket s = ss.accept();
playerCount++;
System.out.println("player #"+ playerCount + " is connected." );
clienthandler cc = new clienthandler(s,playerCount);
if(playerCount == 1)
{
player1 = cc;
//if 1st client then player 1
}
else
{
player2 = cc;
//if 2nd client then player 2
}
Thread t = new Thread(cc);
t.start();
//thread starts
}
System.out.println("No more connections accepted.(both the players have
joined)");
//No more than 2 clients(players) accepted
}
catch (IOException ex)
{
System.out.println("IOException while initiating connection, please try
again.");
}

xiv

}

private class clienthandler implements Runnable
{
private Socket socket;
private DataInputStream Din;
private DataOutputStream Dout;
private int playerID;
clienthandler (Socket s, int id)
{
socket = s;
//Socket created
playerID = id;
//player ID is assigned
try
{
Din = new DataInputStream (socket.getInputStream());
Dout = new DataOutputStream(socket.getOutputStream());
}
catch(IOException ex)
{
System.out.println("IOException in clienthandler");
}
}
public void run()
{
try
{
Dout.writeInt(playerID);
Dout.flush();
while(true)
{

xv

if(playerID == 1)

{

player1val = Din.readInt();
System.out.println("player1 clicked "+ player1val);

//displayed in command prompt as to which button player 1

clicked
player2.sendButtonVal(player1val);

//player 1 button sent to player 2 and it's reflected in player

2 GUI also

}
else if(player1.socket.isClosed() || player2.socket.isClosed())

{

System.out.println("Connection lost !! Please try again.");

//connection lost

break;
}
else
{

player2val = Din.readInt();
System.out.println("player2 clicked "+ player2val);
player1.sendButtonVal(player2val);
}
}
}
catch(Exception ex)
{
player1.closeConnection();
player2.closeConnection();
}
}
//used to send value to Client
public void sendButtonVal(int n)

xvi

{
try
{
Dout.writeInt(n);
Dout.flush();
System.out.println("value succesfully written back to client");
//to verify that value successfully written in GUI for both players
}
catch(IOException ex)
{
System.out.println("IOException in sending value to opponent");
}
}
//closes Server side connection
public void closeConnection()
{
try
{
socket.close();
System.out.println("Connection successfully closed.");
}
catch(IOException ex)
{
System.out.println("IOException while closing the connection");
}
}
}
public static void main(String args[])
{
Server server = new Server();
server.start();
}}
