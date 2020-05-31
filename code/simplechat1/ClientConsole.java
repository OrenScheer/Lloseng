// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF
{
  //Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   * @param loginID The login ID of the client.
   */
  public ClientConsole(String host, int port, String loginID)
  {
      client= new ChatClient(host, port, loginID, this); // Exception is now caught in ChatClient constructor

  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept()
  {
    try
    {
      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true)
      {
        message = fromConsole.readLine();
        client.handleMessageFromClientUI(message);
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    System.out.println("> " + message);
  }


  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The client login id.
   * @param args[1] The host to connect to.
   * @param args[2] The port to connect to.
   */
  public static void main(String[] args)
  {
    String host = "localhost"; // Default host name
    int port = DEFAULT_PORT;  // Default host number
    if (args.length == 0) {
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      System.exit(1);
    }
    String loginID = args[0];
    if (args.length == 3) { // If both host and port are input
      try {
        host = args[1];
        port = Integer.parseInt(args[2]);
      }
      catch (NumberFormatException e) { // In case they're input in reverse order
        host = args[2];
        port = Integer.parseInt(args[1]);
      }
    }
    else if (args.length == 2) { // If only one of host/port are present
      try { // First see if it's a port
        port = Integer.parseInt(args[1]);
      }
      catch (NumberFormatException e) { // Otherwise it's a host
        host = args[1];
      }
    }
    ClientConsole chat = new ClientConsole(host, port, loginID);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
