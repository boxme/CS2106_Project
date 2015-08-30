import java.util.*;

public class Manager {
    private static Manager sInstance;
    private ArrayList<LinkedList<Process>> readyList;
    private Process current;
    private HashMap<String, Resource> resources;

    private Manager() {
        readyList = new ArrayList<>(3);
        for (int i = 0; i < 3; ++i) {
            readyList.add(new LinkedList<>());
        }
        
        resources = new HashMap<>();
        final String r1 = "R1";
        final String r2 = "R2";
        final String r3 = "R3";
        final String r4 = "R4";

        resources.put(r1, new Resource(r1));
        resources.put(r2, new Resource(r2));
        resources.put(r3, new Resource(r3));
        resources.put(r4, new Resource(r4));
    }

    public static Manager getInstance() {
        if (sInstance == null) {
            sInstance = new Manager();
        }

        return sInstance;
    }

    public void reqRes(String resId, int reqUnits) {
        Resource res = resources.get(resId);
        boolean isSuccess = res.isRequestSuccessfull(reqUnits, current);
        if (!isSuccess) {
            current = null;
        }
    }

    public void relRes(String resId, int relUnits, Process process, final boolean isDelete) {
        Resource res = resources.get(resId);
        res.releaseRes(relUnits, process, isDelete);

        if (!isDelete) {
            runProcessOnWaitingList(resId);
        }
    }

    private void checkWaitingListOfAllRes() {
        Set<String> keySet = resources.keySet();
        for (Iterator<String> iter = keySet.iterator(); iter.hasNext();) {
            String resId = iter.next();
            runProcessOnWaitingList(resId);
        }
    }

    private void runProcessOnWaitingList(String resId) {
        Resource res = resources.get(resId);
        final Process waitingProcess = res.getRunnableProcessFromWaitingList();
        if (waitingProcess != null) {
            final int waitingUnits = waitingProcess.getWaitingResUnits(resId);
            res.isRequestSuccessfull(waitingUnits, waitingProcess);
            insertProcessIntoReadyList(waitingProcess);
            timeOut();
        }
    }

    public void timeOut() {
        if (current != null) {
            insertProcessIntoReadyList(current);
            current = null;
        }
    }

    public void deleteProcess(String id) {
        Process process = searchForProcess(id);
        deleteProcess(process);
        checkWaitingListOfAllRes();
    }

    private Process searchForProcess(String id) {
        LinkedList<Process> queue;
        Process process = null;
        
        if (current != null && current.getId().equalsIgnoreCase(id)) {
            process = current;
            current = null;
            return process;
        }

        final int lowerBound = ProcessPriority.USER.getLevel(); 
        final int upperBound = ProcessPriority.SYSTEM.getLevel();
        for (int i = upperBound; i >= lowerBound; --i) {
            queue = readyList.get(i);
            process = queue.peek();
            if (process != null && process.getId().equalsIgnoreCase(id)) {
                break;
            }
        }     

        return process;
    }

    private void deleteProcess(final Process process) {
        if (process == null) return;
        
        ArrayList<Process> children = process.getChildren();
        final int size = children.size();
        Process child = null;
        for (int i = 0; i < size; ++i) {
            child = children.get(i);
            deleteProcess(child);
        }
        children.clear();

        process.releaseAllResources();

        LinkedList<Process> list = process.getList();
        if (list != null) {
            list.remove(process);
            process.setList(null);
        }

        final Process parent = process.getParent();
        if (parent != null) {
            parent.removeChild(process);
            process.setParent(null);
        }
    }

    public void createProcess(ProcessPriority priority, String id) {
        Process newProcess = new Process(priority, id);
        linkedNewProcessToCurrent(newProcess);
        insertProcessIntoReadyList(newProcess);
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

    public void schedule() {
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
            if (current.getPriority().getLevel() < process.getPriority().getLevel()) {
                insertProcessIntoReadyList(current);
            } else {
                hasSwitchContext = false;
                process = current;
            }
        }

        current = process;
        current.setType(ProcessType.RUNNING);
        if (hasSwitchContext) detachProcessFromList(current);

        System.out.println(current.getId() + " priority: " + current.getPriority() + current.getResInfo());
    }

    public Process getRunningProcess() {
        return current;
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
