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



    public void interpret(OsProcess currentProcess,String instruction,int timeSlice) throws URISyntaxException, IOException {
        System.out.println("current process: " + currentProcess.display());
        System.out.println("current instruction: " + instruction);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        systemCall systemCall = new systemCall(currentProcess);
        String[] tokens = instruction.split(" ");
        String command = tokens[0];
        switch (command) {
            case "print" -> print(tokens,systemCall);
            case "assign" -> assign(currentProcess, tokens, systemCall);
            case "writeFile" -> writeFile(tokens[1],tokens[2], systemCall);
            case "readFile" -> readFile(tokens[1], systemCall);
            case "printFromTo" -> printFromTo(tokens[1],tokens[2], systemCall);
            case "semWait" -> semWait(currentProcess, tokens[1],generalBlockedQueue);
            case "semSignal" -> semSignal(currentProcess, tokens[1],readyQueue,generalBlockedQueue);
        }
        //
        if(currentProcess.getInstructionsQueue().isEmpty()){
            currentProcess.setState(processState.TERMINATED);
        }
else {
            currentProcess.setRunningTime(currentProcess.getRunningTime() + 1);
            if (currentProcess.getRunningTime() == timeSlice) {
                //readyQueue.add(currentProcess);
                currentProcess.setRunningTime(0);
                currentProcess.setState(processState.READY);
            }
        }
    }

    private  void print(String[] value,systemCall systemCall) {
            systemCall.print(value[1]);
        }

    private  void assign(OsProcess currentProcess,String[] value,systemCall systemCall) throws URISyntaxException, IOException {
        String content = "";
        if(!value[2].equals("input")&& !value[2].equals("readFile")){
            systemCall.assign(value);
        }
        else {
            if (value[2].equals("input")) {
                //get input from user
                content = systemCall.takeInput();

            } else if (value[2].equals("readFile")) {
                content = systemCall.readFile(value[3]);
            }

            //replace the nested instruction with the new simple instruction
            String newInstruction = "assign " + value[1] + " "+ content;

            //put the new instruction at the beginning of the instruction queue
            currentProcess.getInstructionsQueue().add(newInstruction);
            for (int i = 0; i < currentProcess.getInstructionsQueue().size()-1; i++) {
                currentProcess.getInstructionsQueue().add(currentProcess.getInstructionsQueue().poll());
            }
        }
    }

    //what if the file is not there?
    private  void writeFile(String fileName, String content,systemCall systemCall) throws URISyntaxException, IOException {
        systemCall.writeFile(fileName,content);
    }

    private String readFile(String fileName,systemCall systemCall) throws URISyntaxException, IOException {
        return systemCall.readFile( fileName);
    }

    private  void printFromTo(String From, String To,systemCall systemCall)   {
        systemCall.printFromTo(From,To);
    }

    private  void semWait(OsProcess currentProcess,String semName,ArrayList<OsProcess> generalBlockedQueue) {
        switch (semName) {
            case "userInput" -> semWaitHelper(currentProcess,userInputMutex,generalBlockedQueue);
            case "userOutput" -> semWaitHelper(currentProcess,userOutputMutex,generalBlockedQueue);
            case "file" -> semWaitHelper(currentProcess,fileSystemMutex,generalBlockedQueue);
        }
    }

    private void semWaitHelper(OsProcess currentProcess,Mutex mutexName,ArrayList<OsProcess> generalBlockQueue) {
        if(mutexName.isLocked()==true){
            currentProcess.setState(processState.BLOCKED);
            mutexName.getBlockedQueue().add(currentProcess);
            generalBlockQueue.add(currentProcess);
        }
        else{
            mutexName.semWait(currentProcess);
        }
    }

    private  void semSignal(OsProcess currentProcess,String semName,Queue<OsProcess> readyQueue,ArrayList<OsProcess> generalBlockedQueue) {
        switch (semName) {
            case "userInput" -> semSignalHelper(currentProcess,userInputMutex,readyQueue,generalBlockedQueue);
            case "userOutput" -> semSignalHelper(currentProcess,userOutputMutex,readyQueue,generalBlockedQueue);
            case "file" -> semSignalHelper(currentProcess,fileSystemMutex, readyQueue,generalBlockedQueue);
        }
    }

    private void semSignalHelper(OsProcess currentProcess,Mutex mutexName,Queue<OsProcess> readyQueue,ArrayList<OsProcess> generalBlockedQueue) {
        mutexName.semSignal(currentProcess);
        if(mutexName.getBlockedQueue().size()>0){
            OsProcess blockedProcess = mutexName.getBlockedQueue().poll();
            generalBlockedQueue.remove(blockedProcess);
            blockedProcess.setState(processState.READY);
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

    public static void main(String[] args) throws URISyntaxException, IOException {

        Queue<OsProcess> readyQueue = new LinkedList<>();
        Interpreter interpreter = new Interpreter(readyQueue);
        OsProcess osProcess = new OsProcess("Program_1.txt",0);
        interpreter.interpret(osProcess,"assign a b",10);
        osProcess.displayProcessMemory();


    }
}