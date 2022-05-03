import java.util.Scanner;

public class Interpreter {
    private Mutex userInputMutex;
    private Mutex userOutputMutex;
    private Mutex fileSystemMutex;

    public Interpreter() {
        this.userInputMutex = new Mutex();
        this.userOutputMutex = new Mutex();
        this.fileSystemMutex = new Mutex();
    }



    public void interpret(OsProcess currentProcess,String instruction) {
        String[] tokens = instruction.split(" ");
        String command = tokens[0];
        switch (command) {
            case "print" -> print(tokens[1]);
            case "assign" -> assign(currentProcess, tokens[1], tokens[2]);
            case "writeFile" -> writeFile(tokens[1], tokens[2]);
            case "readFile" -> readFile(tokens[1]);
            case "printFromTo" -> printFromTo(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
            case "semWait" -> semWait(currentProcess, tokens[1]);
            case "semSignal" -> semSignal(currentProcess, tokens[1]);
        }
    }

    private  void print(Object result) {
        System.out.println(result);
    }

    private  void assign(OsProcess currentProcess,String var,Object value) {
        if(value.equals("input")) {
            //get input from user
            Scanner sc=new Scanner(System.in);
            value= sc.nextLine();
        }
        currentProcess.addToProcessMemory(var,value);
    }

    //what if the file is not there?
    private  void writeFile(String fileName, String content) {


    }

    private  void readFile(String fileName) {

    }

    private  void printFromTo(int From, int To) {
        //print values from From to To
        for(int i=From;i<=To;i++) {
            System.out.println(i);
        }


    }

    private  void semWait(OsProcess currentProcess,String semName) {
        switch (semName) {
            case "userInput" -> userInputMutex.semWait(currentProcess);
            case "userOutput" -> userOutputMutex.semWait(currentProcess);
            case "file" -> fileSystemMutex.semWait(currentProcess);
        }

    }

    private  void semSignal(OsProcess currentProcess,String semName) {
        switch (semName) {
            case "userInput" -> userInputMutex.semSignal(currentProcess);
            case "userOutput" -> userOutputMutex.semSignal(currentProcess);
            case "file" -> fileSystemMutex.semSignal(currentProcess);
        }

    }
}