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
        AllocatedRes res = getRes(resId);
        res.setSuccessfullAllocation();
    }
    
    /**
     * @return res units allocated to process
     */
    public int reqRes(String resId, int resUnits) {
        int reqUnits = resUnits;
        AllocatedRes res = getRes(resId);
        
        if (res == null) {
            res = new AllocatedRes(resId, resUnits); 
            resList.offer(res); 
        } else {
            reqUnits = res.setRequestedUnits(resUnits);
        }

        return reqUnits;
    }

    public int relRes(String resId, int relUnits) {
        AllocatedRes res = getRes(resId);
        int releasedUnits = 0;

        if (res != null) {
            releasedUnits = res.releaseUnits(relUnits);
        }

        return releasedUnits;
    }

    public int getWaitingResUnits(String resId) {
        AllocatedRes res = getRes(resId);
        int waitingUnits = 0;
        if (res != null) {
            waitingUnits = res.getWaitingUnits();
        }

        return waitingUnits;
    }

    public boolean isProcessFree() {
        boolean isFree = true;
        final int size = resList.size();
        AllocatedRes res;
        for (int i = 0; i < size; ++i) {
            res = resList.get(i);
            isFree = res.getWaitingUnits() == 0 && isFree;
        }

        return isFree;
    }

    private AllocatedRes getRes(String resId) {
        final int size = resList.size();
        AllocatedRes res;
        for (int i = 0; i < size; ++i) {
            res = resList.get(i);
            if (res.getResId().equalsIgnoreCase(resId)) {
                return res;
            }
        }
        return null;
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

    public String getResInfo() {
        String result = " ";
        AllocatedRes res;
        final int size = resList.size();
        for (int i = 0; i < size; ++i) {
            res = resList.get(i);
            result += res.getInfo();
        }
        return result;
    }
    
    private static class AllocatedRes {
        private String resId;
        private int waitingUnits;
        private int allocatedUnits;
        
        public AllocatedRes(String resId, int requestedUnits) {
            this.resId = resId;
            this.waitingUnits = requestedUnits;
            this.allocatedUnits = 0;
        }

        public String getInfo() {
            return "Res: " + resId + " allocated: " + allocatedUnits + " waiting: " + waitingUnits + "\n";
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

        public int releaseUnits(int relUnits) {
            if (allocatedUnits >= relUnits) {
                allocatedUnits -= relUnits;
                return relUnits;
            } else {
                final int freedUnits = relUnits - allocatedUnits;
                allocatedUnits = 0;
                return freedUnits;
            }
        }

        public int getWaitingUnits() {
            return waitingUnits;
        }

        public int getAllocatedUnits() {
            return allocatedUnits;
        }
    }
}
