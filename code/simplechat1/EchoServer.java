// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;
import common.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Instance variables **********************************************

  ChatIF serverUI;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI)
  {
    super(port);
    this.serverUI = serverUI;
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient (Object msg, ConnectionToClient client) {
    String[] parts = ((String)msg).split(" ");
    if (parts[0].equals("#login") && (client.getInfo("firstMessageReceived") == null || ((boolean) client.getInfo("firstMessageReceived")) == false)) {
      serverUI.display("Message received: " + msg + " from " + client);
      client.setInfo("loginID", ((String)msg).substring(7));
      client.setInfo("firstMessageReceived", true);
      serverUI.display(client.getInfo("loginID") + " has logged on.");
      sendToAllClients(client.getInfo("loginID") + " has logged on.");
    }
    else if (parts[0].equals("#login")) {
      try {
        client.sendToClient("Already connected, cannot login again.");
      }
      catch (IOException e) {}
    }
    else {
      if (client.getInfo("firstMessageReceived") == null || ((boolean) client.getInfo("firstMessageReceived")) == false) {
        try {
          client.sendToClient("No login found. Terminating connection.");
          client.close();
        }
        catch (IOException e) {}
      }
      serverUI.display("Message received: " + msg + " from " + client.getInfo("loginID"));
      sendToAllClients(client.getInfo("loginID") + ": " + msg);
    }
  }

  /**
   * This method is called when data arrives from the UI.
   * @param message The data sent from the UI.
   */
  public void handleMessageFromServerUI(String message) {
    if (message == null) {
      return; // If server quits by Ctrl + C, avoid sending "null" to clients
    }
    if (message.charAt(0) == '#') { // Commands
      String[] commands = message.split(" ");
      commands[0] = commands[0].toLowerCase(); // Case insensitive
      if (commands[0].equals("#quit")) {
        try {
          close();
          System.exit(0);
        }
        catch (IOException e) {
          serverUI.display("Unable to quit.");
        }
      }
      else if (commands[0].equals("#stop")) {
        stopListening();
      }
      else if (commands[0].equals("#close")) {
        stopListening();
        try {
          close();
        }
        catch (IOException e) {
          serverUI.display("Unable to close existing connections.");
        }
      }
      else if (commands[0].equals("#setport")) {
        if (!isListening() && getNumberOfClients() == 0) {
          setPort(Integer.parseInt(commands[1]));
          serverUI.display("Port set to: " + getPort());
        }
        else {
          serverUI.display("Could not set port as server is not closed.");
        }
      }
      else if (commands[0].equals("#getport")) {
        serverUI.display("The port number is: " + getPort());
      }
      else if (commands[0].equals("#start")) {
        try {
          listen();
        }
        catch (IOException e) {
          serverUI.display("Unable to start listening.");
        }
      }
    }
    else {
      serverUI.display("SERVER MSG>" + message);
      sendToAllClients("SERVER MSG>" + message);
    }
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    serverUI.display("Server has stopped listening for connections.");
    sendToAllClients("WARNING - Server has stopped listening for connections.");
  }

  /**
   * This method overrides the one in the superclass. Called when
   * a new client connects.
   * @param client the connection with the client.
   */
  protected void clientConnected(ConnectionToClient client) {
    serverUI.display("Client at " + client + " has connected.");
  }

  /**
   * This method overrides the one in the superclass. Called when
   * a client disconnects.
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    serverUI.display(client.getInfo("loginID") + " has disconnected.");
    client.setInfo("firstMessageReceived", false);
  }

  /**
   * This method overrides the one in the superclass. Called when
   * a client disconnects due to an exception.
   * @param client the connection with the client.
   */
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    serverUI.display("A client has unexpectedly disconnected: " + exception);
    client.setInfo("firstMessageReceived", false);
  }
}

//End of EchoServer class
