import java.util.*;

public class Process {
    private final String id;
    private ProcessType type;

    private Process parent;
    private final ArrayList<Process> children;

    private final ProcessPriority priority;

    private LinkedList<Process> list;

    public Process(ProcessPriority priority, String id) {
        this.priority = priority;
        this.id = id;
        this.type = ProcessType.READY;
        this.children = new ArrayList<>();
    }

    public void setParent(Process parent) {
        this.parent = parent;
    }

    public Process getParent() {
        return parent;
    }

    public void setChild(Process child) {
        children.add(child);
    }

    public void removeChild(Process child) {
        children.remove(child);
    }

    public ArrayList<Process> getChildren() {
        return children;
    }

    public void setType(ProcessType type) {
        this.type = type;
    }

    public ProcessType getType() {
        return type;
    }

    public ProcessPriority getPriority() {
        return priority;
    }

    public String getId() {
        return id;
    }

    public void setList(LinkedList<Process> list) {
        this.list = list;
    }

    public LinkedList<Process> getList() {
        return list;
    }
}
