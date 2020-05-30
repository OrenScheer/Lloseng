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
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  /**
   * This method overrides the one in the superclass. Called when
   * a new client connects.
   * @param client the connection with the client.
   */
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("Client at " + client + " has connected.");
  }

  /**
   * This method overrides the one in the superclass. Called when
   * a client disconnects.
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println("Client at " + client + " has disconnected.");
  }

  /**
   * This method overrides the one in the superclass. Called when
   * a client disconnects due to an exception.
   * @param client the connection with the client.
   */
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println("A client has unexpectedly disconnected: " + exception);
  }
}

//End of EchoServer class
