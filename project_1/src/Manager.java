import java.util.*;

public class Manager {
    private static Manager sInstance;
    private ArrayList<LinkedList<Process>> readyList;
    private Process current;

    private Manager() {
        readyList = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            readyList.add(new LinkedList<>());
        }
    }

    public static Manager getInstance() {
        if (sInstance == null) {
            sInstance = new Manager();
        }

        return sInstance;
    }

    public void timeOut() {
        if (current != null) {
            insertProcessIntoReadyList(current);
            current = null;
        }
        schedule();
    }

    public void createProcess(ProcessPriority priority, String id) {
        Process newProcess = new Process(priority, id);
        linkedNewProcessToCurrent(newProcess);
        insertProcessIntoReadyList(newProcess);
        schedule();
    }

    private void linkedNewProcessToCurrent(Process newProcess) {
        if (newProcess == null) return;

        newProcess.setParent(current);

        if (current == null) return;

        current.setChild(newProcess);
    }

    private void insertProcessIntoReadyList(Process newProcess) {
        if (newProcess == null) return;

        newProcess.setType(ProcessType.READY);
        ProcessPriority priority = newProcess.getPriority();
        final int priorityLevel = priority.getLevel();
        readyList.get(priorityLevel).offer(newProcess);
        newProcess.setList(readyList.get(priorityLevel));
    }

    private void schedule() {
        LinkedList<Process> queue;
        Process process;

        final int lowerBound = ProcessPriority.INIT.getLevel(); 
        final int upperBound = ProcessPriority.SYSTEM.getLevel();
        for (int i = upperBound; i >= lowerBound; --i) {
            queue = readyList.get(i);
            process = queue.peek();
            if (process != null) {
                runProcess(process);
                break;
            }
        }       
    }

    private void runProcess(Process process) {
        boolean hasSwitchContext = true;

        if (current != null) {
            if (current.getPriority().getLevel() < process.getPriority().getLevel()
                    || !ProcessType.RUNNING.equals(current.getType())) {
                insertProcessIntoReadyList(current);
            } else {
                hasSwitchContext = false;
                process = current;
            }
        }

        current = process;
        current.setType(ProcessType.RUNNING);
        if (hasSwitchContext) detachProcessFromList(current);

        System.out.println(current.getId() + " priority: " + current.getPriority() + " type: " + current.getType());
    }

    private void detachProcessFromList(Process process) {
        if (process == null) return;

        ProcessPriority priority = process.getPriority();
        final int priorityLevel = priority.getLevel();
        readyList.get(priorityLevel).poll();
    }

    public void printAll() {
        // Print all details
    }
}
