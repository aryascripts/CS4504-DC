   import java.io.*;
   import java.net.*;

    public class TCPClient {
        
        
public static File routingTimes;
public static BufferedWriter bw;
       public static void main(String[] args) throws IOException {
        
            // Variables for setting up connection and communication
         Socket Socket = null; // socket to connect with ServerRouter
         PrintWriter out = null; // for writing to ServerRouter
         BufferedReader in = null; // for reading form ServerRouter
            InetAddress addr = InetAddress.getLocalHost();
            String host = addr.getHostAddress(); // Client machine's IP
            System.out.println(host);
        //String routerName = "j263-08.cse1.spsu.edu"; // ServerRouter host name
        String routerName = "10.99.23.232";
            int SockNum = 5555; // port number
            
            // Tries to connect to the ServerRouter
         try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
         } 
             catch (UnknownHostException e) {
               System.err.println("Don't know about router: " + routerName);
               System.exit(1);
            } 
             catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + routerName);
               System.exit(1);
            }
                
        // Variables for message passing    
         Reader reader = new FileReader("file.txt"); 
            BufferedReader fromFile =  new BufferedReader(reader); // reader for the string file
         String fromServer; // messages received from ServerRouter
         String fromUser; // messages sent to ServerRouter
            String address ="10.99.23.177"; // destination IP (Server)
            //String address = "localhost";
            //String address = "
            long t0, t1, t;
            
            routingTimes = new File("cycleTimeChinese.txt");
            
            // Communication process (initial sends/receives
            out.println(address);// initial send (IP of the destination Server)
            fromServer = in.readLine();//initial receive from router (verification of connection)
            System.out.println("ServerRouter: " + fromServer);
            out.print(host); // Client s    ends the IP of its machine as initial send
            t0 = System.currentTimeMillis();
            
            out.println("Client says Hi");
            
            /*PrintWriter writer = null;
        
            try {
                writer = new PrintWriter("cycleTime.txt", "UTF-8");
            } catch(Exception e){
                System.out.println("Failed");
            }*/
            
            // Communication while loop
         while ((fromServer = in.readLine()) != null) {
             System.out.println("String length: " + fromServer.length());
            System.out.println("Server: " + fromServer);
                t1 = System.currentTimeMillis();
            if (fromServer.equals("BYE.")) // exit statement
            {
                System.out.println("Time to end");
                break;
            }
               
                t = t1 - t0;
                System.out.println("Cycle time: " + t);
                String temp = Long.toString(t);
                
                bw = new BufferedWriter(new FileWriter(routingTimes, true));
                bw.write(temp);
                bw.newLine();
                bw.close();
          
            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
               System.out.println("Client: " + fromUser);
               out.println(fromUser); // sending the strings to the Server via ServerRouter
                    t0 = System.currentTimeMillis();
            }
         }
        
            // closing connections
            System.out.println("Did it end");
         out.close();
         in.close();
         Socket.close();
      }
   }