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
