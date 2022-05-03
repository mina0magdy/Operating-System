import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OsProcess {
    private static final AtomicInteger count = new AtomicInteger(0);
    private String FileName;
    private int ProcessId;
    private HashMap<String,Object> processMemory;

    public OsProcess(String fileName) {
        FileName = fileName;
        ProcessId = count.incrementAndGet();
        processMemory = new HashMap<>();

    }

    public String getFileName() {
        return FileName;
    }

    public int getProcessId() {
        return ProcessId;
    }
    public String display(){
        return "ProcessId: "+ProcessId+" FileName: "+FileName;
    }

    public void addToProcessMemory(String key, Object value){
        processMemory.put(key, value);
    }

    public Object getFromProcessMemory(String key){
        return processMemory.get(key);
    }

    public boolean checkIfExists(String key){
        return processMemory.containsKey(key);
    }
}
