
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Calendar {

    public static Map<String, int[]> phonebook = new HashMap<>();

    public static void main(String args[]) {

        if(args.length != 1){
            System.out.println("ERROR: Invalid Arguments");
            System.out.println("USAGE: ./a.java <site_id> ");
            System.exit(1);
        }

        // TODO read <hostname> <port> from known_udp.txt
//        try{
//            System.out.println(InetAddress.getLocalHost().getHostName());
//        }
//        catch(UnknownHostException e){
//            System.out.println("unknownhostexception");
//        }
        phonebook.put("740c37430bd6", new int[] {0, 5000});
        phonebook.put("d02582f2b193", new int[] {1, 5001});
//        phonebook.put("127.4.0.3", new Pair(2, 5002));

        String siteid = args[0];
        int port = phonebook.get(siteid)[1];

        Server server = new Server(siteid, port);
        server.setDaemon(true);
        server.start();

        Client client = new Client(siteid, port);

        Scanner sc = new Scanner(System.in);
        String command = "";
        while (server.getStatus()) {
            command = sc.nextLine();
            Message msg = client.parse_command(command);
            if (msg == null) continue;
            client.sendMsg(msg, siteid, port);
        }

        client.close();
    }

    //  public void readFile(String path){
//        try {
//            File file = new File(path);
//            BufferedReader buffer = new BufferedReader(new FileReader(file));
//            String line = "";
//
//            int index = 0;
//            mtoT = new HashMap<>();
//            while ( (line = buffer.readLine()) != null ) {
//                System.out.println(line);
//                line = line.trim();
//                String [] words = line.split(" ");
//                mtoT.put(words[0], index);
//                index++;
//
//            }
//        }
//        catch (IOException i) {
//            System.out.println(i);
//        }
//
//
//    }

}