import java.net.URISyntaxException;
import java.util.*;
import java.io.*;

public class Interpreter {
    private Mutex userInputMutex;
    private Mutex userOutputMutex;
    private Mutex fileSystemMutex;
    private Queue<OsProcess> readyQueue;
    private ArrayList<OsProcess> generalBlockedQueue;

    public Interpreter(Queue<OsProcess> readyQueue) {
        this.userInputMutex = new Mutex();
        this.userOutputMutex = new Mutex();
        this.fileSystemMutex = new Mutex();
        this.readyQueue = readyQueue;
        this.generalBlockedQueue = new ArrayList<>();

    }



    public void interpret(OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory,String instruction,int timeSlice) throws URISyntaxException, IOException {
        System.out.println("current process: " + currentProcess.display());
        System.out.println("current instruction: " + instruction);


        systemCall systemCall = new systemCall(currentProcess);
        String[] tokens = instruction.split(" ");
        String command = tokens[0];
        switch (command) {
            case "print" -> print(tokens,systemCall,currentProcess,currentProcessMemoryIndex,memory);
            case "assign" -> assign(currentProcess, tokens, systemCall,currentProcessMemoryIndex,memory);
            case "writeFile" -> writeFile(tokens[1],tokens[2], systemCall,currentProcess,currentProcessMemoryIndex,memory);
            case "readFile" -> readFile(tokens[1], systemCall,currentProcess,currentProcessMemoryIndex,memory);
            case "printFromTo" -> printFromTo(tokens[1],tokens[2], systemCall,currentProcess,currentProcessMemoryIndex,memory);
            case "semWait" -> semWait(currentProcess, tokens[1],generalBlockedQueue,currentProcessMemoryIndex,memory);
            case "semSignal" -> semSignal(currentProcess, tokens[1],readyQueue,generalBlockedQueue,currentProcessMemoryIndex,memory);
        }

        //
        int PC= (int) memory.getFromMemory(currentProcessMemoryIndex+2);
        memory.setInMemory(currentProcessMemoryIndex+2,PC+1);
        if(memory.getFromMemory(PC+1+currentProcessMemoryIndex)==null){
            memory.setInMemory(currentProcessMemoryIndex+1,processState.TERMINATED);
        }
else {
            currentProcess.setRunningTime(currentProcess.getRunningTime() + 1);
            if (currentProcess.getRunningTime() == timeSlice) {
                //readyQueue.add(currentProcess);
                currentProcess.setRunningTime(0);
                memory.setInMemory(currentProcessMemoryIndex+1,processState.READY);
            }
        }
    }

    private  void print(String[] value,systemCall systemCall,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) throws URISyntaxException, IOException {
            systemCall.print(value[1],currentProcess,currentProcessMemoryIndex,memory);
        }

    private  void assign(OsProcess currentProcess,String[] value,systemCall systemCall,int currentProcessMemoryIndex,Memory memory) throws URISyntaxException, IOException {
        String content = "";
        if(!value[2].equals("input")&& !value[2].equals("readFile")){
            systemCall.assign(value,currentProcess,currentProcessMemoryIndex,memory);
        }
        else {
            if (value[2].equals("input")) {
                //get input from user
                content = systemCall.takeInput();

            } else if (value[2].equals("readFile")) {
                content = systemCall.readFile(value[3],currentProcess,currentProcessMemoryIndex,memory);
            }

            //replace the nested instruction with the new simple instruction
            String newInstruction = "assign " + value[1] + " "+ content;

            //put the new instruction at the beginning of the instruction queue
//            currentProcess.getInstructionsQueue().add(newInstruction);
//            for (int i = 0; i < currentProcess.getInstructionsQueue().size()-1; i++) {
//                currentProcess.getInstructionsQueue().add(currentProcess.getInstructionsQueue().poll());
//            }

            int PC= (int) memory.getFromMemory(currentProcessMemoryIndex+2);
            memory.setInMemory(PC+currentProcessMemoryIndex,newInstruction);
            memory.setInMemory(currentProcessMemoryIndex+2,PC-1);

        }
    }

    //what if the file is not there?
    private  void writeFile(String fileName, String content,systemCall systemCall,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) throws URISyntaxException, IOException {
        systemCall.writeFile(fileName,content,currentProcess,currentProcessMemoryIndex,memory);
    }

    private String readFile(String fileName,systemCall systemCall,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) throws URISyntaxException, IOException {
        return systemCall.readFile( fileName,currentProcess,currentProcessMemoryIndex,memory);
    }

    private  void printFromTo(String From, String To,systemCall systemCall,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory)   {
        systemCall.printFromTo(From,To,currentProcess,currentProcessMemoryIndex,memory);
    }

    private  void semWait(OsProcess currentProcess,String semName,ArrayList<OsProcess> generalBlockedQueue,int currentProcessMemoryIndex,Memory memory) {
        switch (semName) {
            case "userInput" -> semWaitHelper(currentProcess,userInputMutex,generalBlockedQueue,currentProcessMemoryIndex,memory);
            case "userOutput" -> semWaitHelper(currentProcess,userOutputMutex,generalBlockedQueue,currentProcessMemoryIndex,memory);
            case "file" -> semWaitHelper(currentProcess,fileSystemMutex,generalBlockedQueue,currentProcessMemoryIndex,memory);
        }
    }

    private void semWaitHelper(OsProcess currentProcess,Mutex mutexName,ArrayList<OsProcess> generalBlockQueue,int currentProcessMemoryIndex,Memory memory) {
        if(mutexName.isLocked()){
            //currentProcess.setState(processState.BLOCKED);
            memory.setInMemory(currentProcessMemoryIndex+1,processState.BLOCKED);
            mutexName.getBlockedQueue().add(currentProcess);
            generalBlockQueue.add(currentProcess);
        }
        else{
            mutexName.semWait(currentProcess);
        }
    }

    private  void semSignal(OsProcess currentProcess,String semName,Queue<OsProcess> readyQueue,ArrayList<OsProcess> generalBlockedQueue,int currentProcessMemoryIndex,Memory memory) {
        switch (semName) {
            case "userInput" -> semSignalHelper(currentProcess,userInputMutex,readyQueue,generalBlockedQueue,currentProcessMemoryIndex,memory);
            case "userOutput" -> semSignalHelper(currentProcess,userOutputMutex,readyQueue,generalBlockedQueue,currentProcessMemoryIndex,memory);
            case "file" -> semSignalHelper(currentProcess,fileSystemMutex, readyQueue,generalBlockedQueue,currentProcessMemoryIndex,memory);
        }
    }

    private void semSignalHelper(OsProcess currentProcess,Mutex mutexName,Queue<OsProcess> readyQueue,ArrayList<OsProcess> generalBlockedQueue,int currentProcessMemoryIndex,Memory memory) {
        mutexName.semSignal(currentProcess);
        if(mutexName.getBlockedQueue().size()>0){
            OsProcess blockedProcess = mutexName.getBlockedQueue().poll();
            generalBlockedQueue.remove(blockedProcess);
            //blockedProcess.setState(processState.READY);

            int blockedProcessIndex=-1;
            if((int)blockedProcess.getProcessId()==(int)memory.getFromMemory(0)){
                blockedProcessIndex=0;
            }
            else if(blockedProcess.getProcessId()==(int) memory.getFromMemory(20)){
                blockedProcessIndex=20;
            }
            else{
                blockedProcessIndex=-1;
            }

            if(blockedProcessIndex!=-1){
                memory.setInMemory(blockedProcessIndex+1,processState.READY);
            }
            else{
                ArrayList<Object> diskContent=scheduler.readProcessDataFromDisk();
                diskContent.set(1,processState.READY);
                scheduler.writeProcessDataInDisk(diskContent);
            }
           // memory.setInMemory(currentProcessMemoryIndex+1,processState.READY);
            readyQueue.add(blockedProcess);
        }
    }

    public Mutex getUserInputMutex(Mutex userInputMutex) {
        return this.userInputMutex;
    }

    public Mutex getUserOutputMutex(Mutex userOutputMutex) {
        return this.userOutputMutex;
    }

    public Mutex getFileSystemMutex(Mutex fileSystemMutex) {
        return this.fileSystemMutex;
    }

    public ArrayList<OsProcess> getGeneralBlockedQueue() {
        return this.generalBlockedQueue;
    }

    public Queue<OsProcess> getReadyQueue() {
        return this.readyQueue;
    }

    public Queue<OsProcess> getUserInputMutexBlockQueue() {
        return this.userOutputMutex.getBlockedQueue();
    }

    public Queue<OsProcess> getUserOutputMutexBlockQueue() {
        return this.userOutputMutex.getBlockedQueue();
    }

    public Queue<OsProcess> getFileSystemMutexBlockQueue() {
        return this.fileSystemMutex.getBlockedQueue();
    }

    public static void main(String[] args)  {




    }
}