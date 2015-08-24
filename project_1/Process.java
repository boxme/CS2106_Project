import java.util.*;

public class Process {
    private int id;
    private ProcessType type;

    private Process parent;
    private ArrayList<Process> children;

    private ProcessPriority priority;

    private ArrayList<Process> list;

    public Process(ProcessPriority priority, int id) {
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

    public int getId() {
        return id;
    }
}
