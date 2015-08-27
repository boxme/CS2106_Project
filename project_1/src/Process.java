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
    
    public void reqRes(String resId, int resUnits) {
        resUnits = (resUnits < 5) ? resUnits : 4;
        final int size = resList.size();
        AllocatedRes res; boolean found = false;
        
        for (int i = 0; i < size; ++i) {
            res = resList.get(i);
            if (res.getResId().equalsIgnoreCase(resId)) {
                res.setRequestedUnits(resUnits); found = true;
            }
        }
        
        if (!found) { res = new AllocatedRes(resId, resUnits); resList.offer(res); }
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
        
        public boolean setRequestedUnits(int requestedUnits) {
            if (this.requestedUnits + requestedUnits < 5) {
                this.requestedUnits += requestedUnits;
                return true;
            }

            return false;
        }

        public int getReqUnits() {
            return requestedUnits;
        }
    }
}
