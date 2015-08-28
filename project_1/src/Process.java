import java.util.*;

public class Process {
    private final String id;
    private ProcessType type;
    private Process parent;
    
    private final ArrayList<Process> children;
    private final ProcessPriority priority;
    
    private LinkedList<Process> list;
    private LinkedList<AllocatedRes> resList;
    
    public Process(ProcessPriority priority, String id) {
        this.priority = priority; this.id = id;
        this.type = ProcessType.READY;
        this.children = new ArrayList<>();
        resList = new LinkedList<>();
    }

    public void setResAllocation(String resId) {
        final int size = resList.size();
        AllocatedRes res;

        for (int i = 0; i < size; ++i) {
            res = resList.get(i);
            if (res.getResId().equalsIgnoreCase(resId)) {
                res.setSuccessfullAllocation();
                return;
            }
        }
    }
    
    /**
     * @return res units allocated to process
     */
    public int reqRes(String resId, int resUnits) {
        final int size = resList.size();
        AllocatedRes res; 
        boolean found = false;
        int reqUnits = resUnits;

        for (int i = 0; i < size; ++i) {
            res = resList.get(i);
            if (res.getResId().equalsIgnoreCase(resId)) {
                reqUnits = res.setRequestedUnits(resUnits); 
                found = true;
            }
        }
        
        if (!found) {
            res = new AllocatedRes(resId, resUnits); 
            resList.offer(res); 
        }

        return reqUnits;
    }
    
    public void setParent(Process parent) { this.parent = parent; }
    
    public Process getParent() { return parent; }
    
    public void setChild(Process child) { children.add(child); }
    
    public void removeChild(Process child) { children.remove(child); }
    
    public ArrayList<Process> getChildren() { return children; }
    
    public void setType(ProcessType type) { this.type = type; }
    
    public ProcessType getType() { return type; }
    
    public ProcessPriority getPriority() { return priority; }
    
    public String getId() { return id; }
    
    public void setList(LinkedList<Process> list) { this.list = list; }
    
    public LinkedList<Process> getList() { return list; }

    public LinkedList<AllocatedRes> getResList() { return resList; }
    
    private static class AllocatedRes {
        private String resId;
        private int waitingUnits;
        private int allocatedUnits;
        
        public AllocatedRes(String resId, int requestedUnits) {
            this.resId = resId;
            this.waitingUnits = requestedUnits;
            this.allocatedUnits = 0;
        }
        
        public String getResId() {
            return resId;
        }
     
        public void setSuccessfullAllocation() {
            allocatedUnits += waitingUnits;
            waitingUnits = 0;
        }

        /**
         * @return actual units allocated
         */
        public int setRequestedUnits(int reqUnits) {
            if (allocatedUnits + waitingUnits + reqUnits <= Resource.MAX_UNIT) {
                waitingUnits += reqUnits;
            } else {
                final int validReqUnits = Resource.MAX_UNIT - allocatedUnits - waitingUnits;
                waitingUnits += validReqUnits;
            }
            return waitingUnits;
        }

        public int getReqUnits() {
            return waitingUnits;
        }

        public int getAllocatedUnits() {
            return allocatedUnits;
        }
    }
}
