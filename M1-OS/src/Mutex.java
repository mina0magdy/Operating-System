public class Mutex {
    private boolean locked ;
    private int lockingProcessId ;

    public Mutex() {
        locked = false;
        lockingProcessId = -1;
    }

    public void semWait(OsProcess process) {
        locked = true;
        lockingProcessId = process.getProcessId();
    }

    public void semSignal(OsProcess process) {
        if(lockingProcessId == process.getProcessId()) {
            locked = false;
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public int getLockingProcessId() {
        return lockingProcessId;
    }
}


