import java.io.*;
import java.net.URISyntaxException;
import java.util.*;


public class scheduler {
    int currentTime;
    int timeSlice;
    OsProcess currentProcess;
    Queue<OsProcess> readyQueue;
    ArrayList<OsProcess> processesList;
    Interpreter interpreter;
    static Memory memory;
    boolean addInFirstSlotInMemory ;

    int runningProcessIndex;


    public scheduler(int timeSlice, ArrayList<OsProcess> processesList, Memory memory) throws URISyntaxException, IOException {
        this.currentTime = 0;
        this.timeSlice = timeSlice;
        this.currentProcess = null;
        this.readyQueue = new LinkedList<>();
        this.memory = memory;
        this.addInFirstSlotInMemory = true;
        this.processesList = processesList;
        this.interpreter = new Interpreter(readyQueue);
        this.runningProcessIndex = -1;

        //check if all processes are terminated to return out of the scheduler

        while (true) {
            int terminatedProcesses = 0;
           // boolean terminatedFlag = true;

            //check if all processes in memory or in disk are terminated to return out of the scheduler
            if (memory.getFromMemory(1) != null && memory.getFromMemory(1).equals(processState.TERMINATED)) {
                terminatedProcesses++;
            }
            if (memory.getFromMemory(21) != null && memory.getFromMemory(21).equals(processState.TERMINATED)) {
                terminatedProcesses++;
            }

            FileReader fr = new FileReader("Disk.txt");
            BufferedReader br = new BufferedReader(fr);
            String s = null;
            String temp="";
//            for (int i = 0; i < 2; i++) {
//                if ( (temp = br.readLine()) != null) {
//                    //System.out.println("temp goes here" + temp+"1");
//                    s= temp.split(" ")[1];
//                }
//            }
//            br.close();
//            fr.close();

            if(readyQueue.isEmpty())
                s="TERMINATED";

            if (s != null && s=="TERMINATED") {
                terminatedProcesses++;
            }
            if (terminatedProcesses == processesList.size())
                break;

                //check for current process being preempted or terminated
                boolean sameProcess = false;
                System.out.println("current time: " + currentTime);
//                displayMemory();

                if (currentProcess != null) {

                    //match current process with the process in memory
                    if(memory.getFromMemory(0)!=null && memory.getFromMemory(0).equals(currentProcess.getProcessId())){
                        runningProcessIndex = 0;
                    }
                    else{
                        runningProcessIndex = 20;
                    }


                    switch ((processState)memory.getFromMemory(runningProcessIndex+1)) {
                        case TERMINATED:
                            System.out.println(currentProcess.getFileName() + " is terminated");
                            displayQueues();
                            currentProcess.setRunningTime(0);
                            currentProcess = null;
                            sameProcess = false;
                            break;
                        case READY:
                            currentProcess.setRunningTime(0);
                            System.out.println(currentProcess.getFileName() + " is preempted");
                            displayQueues();
                            readyQueue.add(currentProcess);
                            currentProcess = null;
                            sameProcess = false;
                            break;
                        case RUNNING:
                            sameProcess = true;
                            break;
                        case BLOCKED:
                            System.out.println(currentProcess.getFileName() + " is blocked");
                            displayQueues();
                            currentProcess.setRunningTime(0);
                            break;
                    }
                }

                //add processes to the ready queue at their respective arrival time

            for(OsProcess process : processesList){
                if(process.getArrivalTime()==currentTime) {
                    process.giveID();
                    swapProcessInMemory(process,processState.READY);
                    readyQueue.add(process);
                }
            }

                //check if there is a process in the ready queue
                //if there is, set it as the current process and remove it from the ready queue
            if(!readyQueue.isEmpty()&&!sameProcess) {
                currentProcess = readyQueue.poll();
//                memory.setInMemory(runningProcessIndex + 1, processState.RUNNING);
                System.out.println(currentProcess.getFileName() + " is chosen");
            }
 //           displayMemory();


            if(memory.getFromMemory(0)!=null && memory.getFromMemory(0).equals(currentProcess.getProcessId())){
                runningProcessIndex=0;
            }
            else if(memory.getFromMemory(20)!=null && memory.getFromMemory(20).equals(currentProcess.getProcessId())){
                runningProcessIndex=20;
            }
            else{
                ArrayList<Object> currentProcessData=readProcessDataFromDisk();
                if(addInFirstSlotInMemory){
                    ArrayList<Object> oldProcessData=readProcessDataFromMemory(0,20);
                    if(oldProcessData.get(0)!=null){
                        System.out.println("Process " + oldProcessData.get(0) + " is swapped out of memory(into Disk)");
                        writeProcessDataInDisk(oldProcessData);
                    }
                    System.out.println("Process " + currentProcessData.get(0) + " is swapped into memory(from Disk)");
                    memory.clearMemoryFromTo(0,20);
                    writeProcessDataInMemory(0,20,currentProcessData);
//                        writeProcessDataInDisk(oldProcessData);
                    runningProcessIndex=0;
                }
                else{
                    ArrayList<Object> oldProcessData=readProcessDataFromMemory(20,40);
                    if(oldProcessData.get(0)!=null){
                        System.out.println("Process " + oldProcessData.get(0) + " is swapped out of memory(into Disk)");
                        writeProcessDataInDisk(oldProcessData);
                    }
                    System.out.println("Process " + currentProcessData.get(0) + " is swapped into memory(from Disk)");
                    memory.clearMemoryFromTo(20,40);
                    writeProcessDataInMemory(20,40,currentProcessData);
                    //writeProcessDataInDisk(oldProcessData);
                    runningProcessIndex=20;
                }
                addInFirstSlotInMemory=!addInFirstSlotInMemory;
            }
            memory.setInMemory(runningProcessIndex + 1, processState.RUNNING);
            System.out.println("Memory Before Execution :");
            displayMemory();
           // displayQueues();
            //if the time slice not reached, use the current process to run
            //if(currentProcess!=null) {

            String currentInstruction = (String)memory.getFromMemory((int)memory.getFromMemory(runningProcessIndex + 2)+runningProcessIndex);
//            if(!addInFirstSlotInMemory)
//                if((int)memory.getFromMemory(runningProcessIndex+2)<20)
//                    currentInstruction = (String)memory.getFromMemory((int)memory.getFromMemory(runningProcessIndex + 2));
//            else
//                currentInstruction = (String)memory.getFromMemory((int)memory.getFromMemory(runningProcessIndex + 2)+20);
            if(currentInstruction!=null) {

                interpreter.interpret(currentProcess, runningProcessIndex, memory, currentInstruction, timeSlice);
                System.out.println("Memory After Execution :");
                displayMemory();
            }
            else
                break;
            //displayMemory();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            currentTime++;


           // }


            }
        }

        private void displayQueue (Queue < OsProcess > queue, String queueName){
            String result = queueName + ":{";
            for (OsProcess process : queue) {
                result += process.getFileName() + ",";
            }
            result += "}";
            System.out.println(result);
        }

        private void displayList (List < OsProcess > list, String listName){
            String result = listName + ":{";
            for (OsProcess process : list) {
                result += process.getFileName() + ",";
            }
            result += "}";
            System.out.println(result);
        }

        private void displayQueues () {
            displayQueue(readyQueue, "readyQueue");
            displayList(interpreter.getGeneralBlockedQueue(), "generalBlockedQueue");
            displayQueue(interpreter.getUserInputMutexBlockQueue(), "inputQueue");
            displayQueue(interpreter.getUserOutputMutexBlockQueue(), "outputQueue");
            displayQueue(interpreter.getFileSystemMutexBlockQueue(), "fileQueue");
        }

        public void swapProcessInMemory(OsProcess process,processState state) throws IOException {
                ArrayList<Object> newProcessData=new ArrayList<>();
                newProcessData.add(process.getProcessId());
                newProcessData.add(state);
            if(addInFirstSlotInMemory) {
                newProcessData.add(8);
                newProcessData.add(0);
                newProcessData.add(20);
            }
            else{
                newProcessData.add(8);
                newProcessData.add(20);
                newProcessData.add(40);
            }
                newProcessData.add(null);
                newProcessData.add(null);
                newProcessData.add(null);
                FileReader fr = new FileReader(process.getFileName());
                BufferedReader br = new BufferedReader(fr);
                String s;
                while((s = br.readLine()) != null) {
                    newProcessData.add(s);
                }
                br.close();
                fr.close();
                if(addInFirstSlotInMemory){
                    if(memory.getFromMemory(0) != null){
                        ArrayList<Object> oldProcessData=readProcessDataFromMemory(0,20);
                        System.out.println("Process with ID: "+ oldProcessData.get(0)+ " is swapped out of Memory(into Disk)");

                        writeProcessDataInDisk(oldProcessData);
                        memory.clearMemoryFromTo(0,20);
                    }
                    runningProcessIndex=0;
                    System.out.println("Process with ID: "+ newProcessData.get(0)+ " is swapped in to Memory(new process)");
                    writeProcessDataInMemory(0,20,newProcessData);
                }
                else{
                    if(memory.getFromMemory(20) != null){
                        ArrayList<Object> oldProcessData=readProcessDataFromMemory(20,40);
                        System.out.println("Process with ID: "+ oldProcessData.get(0)+ " is swapped out of Memory(into Disk)");

                        writeProcessDataInDisk(oldProcessData);
                        memory.clearMemoryFromTo(20,40);
                    }
                    runningProcessIndex=20;
                    System.out.println("Process with ID: "+ newProcessData.get(0)+ " is swapped in to Memory(new process)");
                    writeProcessDataInMemory(20,40,newProcessData);
                }
                addInFirstSlotInMemory=!addInFirstSlotInMemory;
            }



        public ArrayList<Object> readProcessDataFromMemory(int fromInMemory, int toInMemory){
            ArrayList<Object> processData = new ArrayList<>();
            for(int i=fromInMemory;i<toInMemory;i++){
                processData.add(memory.getFromMemory(i));
            }
            return processData;
        }

        public void writeProcessDataInMemory(int fromInMemory, int toInMemory, ArrayList<Object> processData){
            for(int i=fromInMemory;i<toInMemory;i++){
                if((i%20)>=processData.size()){
                    continue;
                }
                memory.setInMemory(i, processData.get(i%20));
            }
        }


        public static void writeProcessDataInDisk(ArrayList<Object> processData){
            try {
                FileWriter fw = new FileWriter("Disk.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                for(int i=0;i<processData.size();i++){
                    bw.write(memory.getKey(i)+" "+processData.get(i));
                    bw.newLine();
                }
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        public static ArrayList<Object> readProcessDataFromDisk(){
            ArrayList<Object> processData = new ArrayList<>();
            try {
                FileReader fr = new FileReader("Disk.txt");
                BufferedReader br = new BufferedReader(fr);
                String s;

                //ID
                if((s = br.readLine()) != null){
                    processData.add(Integer.parseInt(s.split(" ")[1]));
                }

                //State
                if((s = br.readLine()) != null){
                    processData.add(getStateFromString(s.split(" ")[1]));
                }

                //PC
                if((s = br.readLine()) != null){
                    processData.add(Integer.parseInt(s.split(" ")[1]));
                }

                //memoryFrom
                if((s = br.readLine()) != null){
                    processData.add(Integer.parseInt(s.split(" ")[1]));
                }

                //memoryTo
                if((s = br.readLine()) != null){
                    processData.add(Integer.parseInt(s.split(" ")[1]));
                }

                //instructions and variables
                while((s = br.readLine()) != null) {

                    if(s.split(" ")[1].equals("null")){
                        processData.add(null);
                    }

                    else {
                        String[] tokens = s.split(" ");
                        String instruction = "";
                        for(int i=1;i<tokens.length;i++){
                            instruction+= tokens[i]+" ";
                        }
                        processData.add(instruction);
                    }
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return processData;
        }

        public static processState getStateFromString(String state){
        switch (state){
            case "NEW":
                return processState.NEW;
            case "READY":
                return processState.READY;
            case "RUNNING":
                return processState.RUNNING;
            case "BLOCKED":
                return processState.BLOCKED;
            case "TERMINATED":
                return processState.TERMINATED;
            default:
                return null;
        }
        }

        public void displayMemory(){
            String result = "[";
            for(int i=0;i<40;i++){
                    result+=(memory.getKey(i)+" "+memory.getFromMemory(i)+" , ");
            }
            result+="]";
            System.out.println(result);
        }


    }

