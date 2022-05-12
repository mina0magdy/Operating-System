import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


public class scheduler {
    int currentTime;
    int timeSlice;
    OsProcess currentProcess;
    Queue<OsProcess> readyQueue;

    ArrayList<OsProcess> processesList;
    Interpreter interpreter;


    public scheduler(int timeSlice, ArrayList<OsProcess> processesList) throws URISyntaxException, IOException {
        this.currentTime = 0;
        this.timeSlice = timeSlice;
        this.currentProcess = null;
        this.readyQueue = new LinkedList<>();
        /*
        this.inputQueue = new LinkedList<>() ;
        this.outputQueue = new LinkedList<>();
        this.fileQueue = new LinkedList<>();

         */
        this.processesList = processesList;
        this.interpreter = new Interpreter(readyQueue);

        //check if all processes are terminated to return out of the scheduler

        while(true){
            boolean terminatedFlag = true;
            for(OsProcess process : processesList){
                if(process.getState()!=processState.TERMINATED)
                    terminatedFlag = false;
            }
            if(terminatedFlag)
                break;

            //check for current process being preempted or terminated
            boolean sameProcess = false;
            System.out.println("current time: "+currentTime);
            if(currentProcess!=null){
                switch (currentProcess.getState()){
                    case TERMINATED:
                        System.out.println(currentProcess.getFileName()+" is terminated");
                        displayQueues();
                        currentProcess.setRunningTime(0);
                        currentProcess = null;
                        sameProcess = false;
                        break;
                    case READY:
                        currentProcess.setRunningTime(0);
                        System.out.println(currentProcess.getFileName()+" is preempted");
                        displayQueues();
                        readyQueue.add(currentProcess);
                        currentProcess = null;
                        sameProcess = false;
                        break;
                    case RUNNING:
                        sameProcess = true;
                        break;
                    case BLOCKED:
                        System.out.println(currentProcess.getFileName()+" is blocked");
                        displayQueues();
                        currentProcess.setRunningTime(0);
                        break;
                }
            }

            //add processes to the ready queue at their respective arrival time
            for(OsProcess process : processesList){
                if(process.getArrivalTime()==currentTime) {
                    readyQueue.add(process);
                    process.setState(processState.READY);
                }
            }

            //check if there is a process in the ready queue
            //if there is, set it as the current process and remove it from the ready queue
            if(!readyQueue.isEmpty()&&!sameProcess){
                currentProcess = readyQueue.poll();
                currentProcess.setState(processState.RUNNING);
                System.out.println(currentProcess.getFileName()+" is chosen");
                displayQueues();
            }
            //if the time slice not reached, use the current process to run
            //if(currentProcess!=null) {
                String currentInstruction = currentProcess.getInstructionsQueue().poll();
                interpreter.interpret(currentProcess, currentInstruction, timeSlice);
                currentTime++;
           // }


        }
    }

    private void displayQueue(Queue<OsProcess> queue,String queueName){
        String result = queueName+":{";
        for(OsProcess process : queue){
            result+=process.getFileName()+",";
        }
        result+="}";
        System.out.println(result);
        }

        private void displayList(List<OsProcess> list,String listName){
            String result = listName+":{";
            for(OsProcess process : list){
                result+=process.getFileName()+",";
            }
            result+="}";
            System.out.println(result);
        }

    private void displayQueues(){
        displayQueue(readyQueue,"readyQueue");
        displayList(interpreter.getGeneralBlockedQueue(),"generalBlockedQueue");
        displayQueue(interpreter.getUserInputMutexBlockQueue(),"inputQueue");
        displayQueue(interpreter.getUserOutputMutexBlockQueue(),"outputQueue");
        displayQueue(interpreter.getFileSystemMutexBlockQueue(),"fileQueue");
    }
}
