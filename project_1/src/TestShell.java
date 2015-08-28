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
                case "init": {
                    manager.createProcess(ProcessPriority.INIT, "INIT");
                    break;
                }
                case "cr": {
                    final String id = sc.next();
                    final ProcessPriority priority = ProcessPriority.priority(sc.nextInt());
                    manager.createProcess(priority, id);
                    break;
                }
                case "de": {
                    final String processId = sc.next();
                    break;
                }
                case "req": {
                    final String resName = sc.next();
                    final int unitsReq = sc.nextInt();
                    manager.reqRes(resName, unitsReq);
                    break;
                }
                case "rel": {
                    final String resName = sc.next();
                    final int unitsRel = sc.nextInt();
                    final Process current = manager.getRunningProcess();
                    final boolean isDelete = false;
                    manager.relRes(resName, unitsRel, current, isDelete);
                    break;
                }
                case "to": {
                    manager.timeOut();
                    break;
                }
                case "help": {
                    break;
                }
                case "exit": {
                    sc.close();
                    System.exit(0);
                    break;
                } 
                case "details": {
                   // Print all details
                   break;
                }
            }
        }
    }
}
