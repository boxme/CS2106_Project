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
        }

        return isSuccess;
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
