import java.io.*;
import java.util.*;

public class TestShell {
    public static void main(String[] args) throws IOException {
        Scanner sc = null;
        BufferedReader br = null;
        boolean isReadFromFile = false;

        if (args.length == 0) {
            sc = new Scanner(System.in);
        } else {
            String filePath = args[0];
            br = new BufferedReader(new FileReader(filePath));
            sc = new Scanner(br);
            isReadFromFile = true;
            PrintStream out = null;

            if (filePath.contains(":\\")) {
                int index = 0;
                for (int i = filePath.length() - 1; i >= 0; --i) {
                    if (filePath.charAt(i) == '\\') {
                        index = i + 1;
                        break;
                    }
                }

                String outFilePath = filePath.substring(0, index);
                out = new PrintStream(new FileOutputStream(outFilePath+"desmond.txt"));
            } else {
                out = new PrintStream(new FileOutputStream("desmond.txt"));
            }
            System.setOut(out);
        }

        final Manager manager = Manager.getInstance();
        manager.createProcess(ProcessPriority.INIT, "INIT");
        manager.schedule();

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
                    manager.deleteProcess(processId);
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
                    if (isReadFromFile) {
                        br.close();
                    }
                    System.exit(0);
                    break;
                } 
                case "details": {
                   // Print all details
                   break;
                }
            }
            manager.schedule();
        }
    }
}
