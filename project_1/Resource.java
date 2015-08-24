import java.util.*;

public class Resource {
    private final int id;
    
    private final int totalUnits;
    private int freeUnits;

    private final ArrayList<Process> waitingList;

    public Resource(int id, int totalUnits) {
        this.id = id;
        this.totalUnits = totalUnits;
        freeUnits = totalUnits;
        waitingList = new ArrayList<>();
    }
}
