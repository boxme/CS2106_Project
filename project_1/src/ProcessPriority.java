public enum ProcessPriority {
    SYSTEM(2), USER(1), INIT(0);

    private final int level;

    ProcessPriority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

