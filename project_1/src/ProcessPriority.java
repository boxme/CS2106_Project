public enum ProcessPriority {
    SYSTEM(2), USER(1), INIT(0);

    private final int level;

    ProcessPriority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static ProcessPriority priority(int priority) {
        if (priority == 0)      return ProcessPriority.INIT;
        else if (priority == 1) return ProcessPriority.USER;
        else                    return ProcessPriority.SYSTEM;
    }
}

