import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    private boolean locked ;
    private int lockingProcessId ;
    private Queue<OsProcess> blockedQueue ;

    public Mutex() {
        locked = false;
        lockingProcessId = -1;
        blockedQueue = new LinkedList<>();
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

    public Queue<OsProcess> getBlockedQueue() {
        return blockedQueue;
    }
}


