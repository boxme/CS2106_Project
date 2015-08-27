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
    
    /**
     * @return res units allocated to process
     */
    public int reqRes(String resId, int resUnits) {
        final int size = resList.size();
        AllocatedRes res; 
        boolean found = false;
        int allocatedUnits = resUnits;

        for (int i = 0; i < size; ++i) {
            res = resList.get(i);
            if (res.getResId().equalsIgnoreCase(resId)) {
                allocatedUnits = res.setRequestedUnits(resUnits); 
                found = true;
            }
        }
        
        if (!found) {
            res = new AllocatedRes(resId, resUnits); 
            resList.offer(res); 
        }

        return allocatedUnits;
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
        private int requestedUnits;
        
        public AllocatedRes(String resId, int requestedUnits) {
            this.resId = resId;
            this.requestedUnits = requestedUnits;
        }
        
        public String getResId() {
            return resId;
        }
      
        /**
         * @return actual units allocated
         */
        public int setRequestedUnits(int reqUnits) {
            if (this.requestedUnits + reqUnits <= Resource.MAX_UNIT) {
                this.requestedUnits += reqUnits;
                return reqUnits;
            } else {
                final int prevReqUnits = this.requestedUnits;
                this.requestedUnits = Resource.MAX_UNIT;
                return this.requestedUnits - prevReqUnits;
            }
        }

        public int getReqUnits() {
            return requestedUnits;
        }
    }
}
