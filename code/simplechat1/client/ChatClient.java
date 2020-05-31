// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  /**
   * The login id of the client that is displayed to the server.
   */
  String loginID;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   * @param loginID The login ID of the client.
   */

  public ChatClient(String host, int port, String loginID, ChatIF clientUI)
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    try { // ChatClient can still be created if server is not open and listening
        openConnection();
        sendLoginToServer();
    }
    catch (IOException e) {
      this.clientUI.display("Cannot open connection. Awaiting command.");
    } // If connection fails, console must #login to connect
  }


  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      if (message.charAt(0) == '#') {
        String[] commands = message.split(" ");
        commands[0] = commands[0].toLowerCase();
        if(commands[0].equals("#quit")) {
          quit();
        }
        else if(commands[0].equals("#logoff")) {
          closeConnection();
        }
        else if(commands[0].equals("#sethost")) {
          if (!isConnected()) {
            setHost(commands[1]);
            clientUI.display("Host set to: " + getHost());
          }
          else {
            clientUI.display("Could not set host as client is currently connected.");
          }
        }
        else if (commands[0].equals("#setport")) {
          if (!isConnected()) {
            setPort(Integer.parseInt(commands[1]));
            clientUI.display("Port set to: " + getPort());
          }
          else {
            clientUI.display("Could not set port as client is currently connected.");
          }
        }
        else if (commands[0].equals("#login")) {
          if (!isConnected()) {
            openConnection();
            sendLoginToServer();
          }
          else {
            clientUI.display("Client is already connected to the server!");
          }
        }
        else if (commands[0].equals("#gethost")) {
          clientUI.display("Connected to host: " + getHost());
        }
        else if (commands[0].equals("#getport")) {
          clientUI.display("Connected on port: " + getPort());
        }
      }
      else {
        sendToServer(message);
      }
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  /**
   * This method is called when the client is listening for the server and the server shuts down.
   * @param exception The exception thrown when the server does not respond.
   */
  public void connectionException(Exception exception) {
    clientUI.display("Abnormal termination of connection.");
  }

  /**
   * This method is called when the connection to the server has been closed.
   */
  protected void connectionClosed() {
    clientUI.display("Any open connection to the server has been closed.");
  }

  /**
   * This method, called when the client establishes a new connection to
   * the server, sends its login ID to the server.
   */
  private void sendLoginToServer() {
    try {
      sendToServer("#login " + loginID);
    }
    catch (IOException e) {
      clientUI.display("could not send login to server.");
    }
  }
}
//End of ChatClient class
