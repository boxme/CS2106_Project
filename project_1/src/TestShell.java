import java.io.*;
import java.util.*;

public class TestShell {
    public static void main(String[] args) throws IOException {
        Scanner sc;
        BufferedReader br;

        sc = new Scanner(System.in);
        // if (args.length == 0) {
        //     sc = new Scanner(System.in);
        // }

        final Manager manager = Manager.getInstance();
        manager.createProcess(ProcessPriority.INIT, "INIT");

        while (true) {
            switch (sc.next()) {
                case "init":

                    break;

                case "cr":
                    final String id = sc.next();
                    final ProcessPriority priority = ProcessPriority.priority(sc.nextInt());
                    manager.createProcess(priority, id);
                    break;

                case "de":
                    break;

                case "req":
                    break;

                case "rel":
                    break;

                case "to":
                    manager.timeOut();
                    break;

                case "help":
                    break;

                case "exit":
                    sc.close();
                    System.exit(0);
                    break;
                
                case "details":
                   // Print all details
                   break;
            }
        }
    }
}
