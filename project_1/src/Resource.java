import java.util.*;

public class Resource {
    private final String id;
    
    private final int totalUnits;
    private int freeUnits;

    private final LinkedList<Process> waitingList;

    public Resource(String id, int totalUnits) {
        this.id = id;
        this.totalUnits = totalUnits;
        freeUnits = totalUnits;
        waitingList = new LinkedList<>();
    }

    public boolean isRequestSuccessfull(int allocatedUnits, Process process) {
        boolean isSuccess = freeUnits >= allocatedUnits;
        freeUnits = isSuccess ? (freeUnits - allocatedUnits) : freeUnits;
        if (!isSuccess) {
            process.setType(ProcessType.BLOCKED);
            process.setList(waitingList);
            waitingList.offer(process);
        }

        process.reqRes(id, allocatedUnits);
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
