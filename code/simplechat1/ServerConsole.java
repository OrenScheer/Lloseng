import java.io.*;
import client.*;
import common.*;


/**
 * This class constructs the UI for a server client.  It implements the
 * CHATIF interface.
 * @author Oren Scheer
 */
public class ServerConsole implements ChatIF {

  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance of the EchoServer associated with this ServerConsole.
   */
  EchoServer server;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the server console.
   * @param port The port number to connect on.
   */
  public ServerConsole (int port) {
    server = new EchoServer(port, this);
  }


  public void display (String message) {
    System.out.println(message);
  }

  public void accept() {
    try
    {
      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true)
      {
        message = fromConsole.readLine();
        server.handleMessageFromServerUI(message);
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance.
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    ServerConsole sv = new ServerConsole(port);

    try
    {
      sv.server.listen(); //Start listening for connections
      sv.accept();
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
