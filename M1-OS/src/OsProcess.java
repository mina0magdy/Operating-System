import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class OsProcess {
    private static final AtomicInteger count = new AtomicInteger(0);
    private String FileName;
    private int processId;
    private HashMap<String,Object> processMemory;
    private processState state;
    private int arrivalTime;
    //check
    int runningTime;
    Queue<String> instructionsQueue;

    public OsProcess(String fileName, int arrivalTime){
        FileName = fileName;
        processId = count.incrementAndGet();
        processMemory = new HashMap<>();
        state = processState.NEW;
        this.arrivalTime = arrivalTime;
        //check
        this.runningTime=0;
        this.instructionsQueue=this.getInstructionsQueuePrivate();

    }

    public String getFileName() {
        return FileName;
    }

    public int getProcessId() {
        return processId;
    }

    public String display(){
        return ( "ProcessId: "+processId+", FileName: "+FileName+" ");
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

    public void setState(processState state){
        this.state = state;
    }

    public processState getState(){
        return state;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    private Queue<String> getInstructionsQueuePrivate() {
        //read file and put each line in a queue
        Queue<String> instructionQueue = new LinkedList<>();
        try {
            //URL path = ClassLoader.getSystemResource("\\M1-OS\\"+this.FileName);
            File f = new File("C:\\Users\\20122\\eclipse-workspace\\M1-OS\\"+this.FileName);
            BufferedReader br
                    = new BufferedReader(new FileReader(f));
            String st;
            while ((st = br.readLine()) != null) {
                instructionQueue.add(st);
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instructionQueue;
    }

    public Queue<String> getInstructionsQueue() {
        return instructionsQueue;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public void displayProcessMemory(){
        for(String key:processMemory.keySet()){
            System.out.println(key+" : "+processMemory.get(key));
        }
    }

}
