import java.util.*;

public class Resource {
    public static final int MAX_UNIT = 4;
    private final String id;
    
    private final int totalUnits;
    private int freeUnits;

    private final LinkedList<Process> waitingList;

    public Resource(String id) {
        this.id = id;
        this.totalUnits = MAX_UNIT;
        freeUnits = totalUnits;
        waitingList = new LinkedList<>();
    }

    /**
     * if the requested units is an invalid number, returns
     * a successful request to keep the status quote of the 
     * running process
     * 
     * @params Process : currently running process
     *
     * @return true if the request is successfull
     */
    public boolean isRequestSuccessfull(int reqUnits, Process process) {
        reqUnits = reqUnits <= MAX_UNIT ? reqUnits : MAX_UNIT;
        final int unitsAllocated = process.reqRes(id, reqUnits);

        boolean isSuccess = freeUnits >= unitsAllocated;
        freeUnits = isSuccess ? (freeUnits - unitsAllocated) : freeUnits;
        if (!isSuccess) {
            process.setType(ProcessType.BLOCKED);
            process.setList(waitingList);
            waitingList.offer(process);
        } else {
            process.setResAllocation(id);
        }

        return isSuccess;
    }

    public void releaseRes(int relUnits, Process process, final boolean isDelete) {
        relUnits = relUnits <= MAX_UNIT ? relUnits : MAX_UNIT;
        final int unitsFreed = process.relRes(id, relUnits); 
        freeUnits += unitsFreed;
        
        if (isDelete) {
            waitingList.remove(process);
        }
    }

    /**
     * @return the next process in the waiting list that has might be able to run
     */
    public Process getRunnableProcessFromWaitingList() {
        Process process = null;
        final int size = waitingList.size();
        for (int i = 0; i < size; ++i) {
            process = waitingList.get(i);
            int waitingUnits = process.getWaitingResUnits(id);
            if (freeUnits >= waitingUnits) {
                waitingList.remove(process);
                break;
            }
        }
        return process;
    }

    public LinkedList<Process> getWaitingList() {
        return waitingList;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public int getNumOfFreeUnits() {
        return freeUnits;
    }
}
