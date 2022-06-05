import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;

public class systemCall {
    private OsProcess currentProcess;

    public systemCall(OsProcess currentProcess) {
        this.currentProcess = currentProcess;
    }

    public String takeInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your input: ");
        return sc.nextLine();
    }

    public void print(String s,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) {
//        if(currentProcess.checkIfExists(s))
//            System.out.println(currentProcess.getFromProcessMemory(s));
//        else
//            System.out.println(s);

    for(int i=0;i<3;i++){
        if((memory.getFromMemory(currentProcessMemoryIndex + 5 + i) != null) &&((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[0].equals(s)){
            System.out.println(((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[1]);
            break;
        }
        }
    }

    public   void assign(String[] value,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) {
        String s=value[2];
        if(value.length>3) {
            s="";
            for (int i = 2; i < value.length; i++) {
                s += value[i] + " ";
            }
        }
//        if(currentProcess.checkIfExists(s))
//            currentProcess.addToProcessMemory(value[1], currentProcess.getFromProcessMemory(s));
//        else
//            currentProcess.addToProcessMemory(value[1], s);

        boolean variableFound=false;
        for(int i=0;i<3;i++){
            if((memory.getFromMemory(currentProcessMemoryIndex + 5 + i) != null) &&((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[0].equals(value[1])){
                //System.out.println(((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[1]);
                memory.setInMemory(currentProcessMemoryIndex + 5 + i,value[1]+":"+s);
                variableFound=true;
                break;
            }
        }
        if(!variableFound){
            for(int i=0;i<3;i++){
                if(memory.getFromMemory(currentProcessMemoryIndex + 5 + i) == null){
                    memory.setInMemory(currentProcessMemoryIndex + 5 + i,value[1]+":"+s);
                    break;
                }
            }
        }
    }
    //what if the file is not there?
    public   void writeFile(String fileName, String content,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) throws URISyntaxException, IOException {
        //create new file in the file system
        //write content to the file
//        fileName= (String) currentProcess.getFromProcessMemory(fileName);
//        content= (String) currentProcess.getFromProcessMemory(content);
        for(int i=0;i<3;i++){
            if((memory.getFromMemory(currentProcessMemoryIndex + 5 + i) != null) &&((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[0].equals(fileName)){
                fileName=(((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[1]);
                break;
            }
        }

        for(int i=0;i<3;i++){
            if((memory.getFromMemory(currentProcessMemoryIndex + 5 + i) != null) &&((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[0].equals(content)){
                content=(((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[1]);
                break;
            }
        }
        FileWriter file = new FileWriter(fileName + ".txt");
        file.write(content + "");
        file.close();
    }

    public String readFile(String fileName,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) throws URISyntaxException, IOException {
        //fileName= (String) currentProcess.getFromProcessMemory(fileName);
        //read file and return the content

        for(int i=0;i<3;i++){
            if((memory.getFromMemory(currentProcessMemoryIndex + 5 + i) != null) &&((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[0].equals(fileName)){
                fileName=(((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[1]);
                break;
            }
        }

//
        FileReader fr = new FileReader(fileName+".txt");
        BufferedReader br = new BufferedReader(fr);
        String s;
        String result = "";
        while((s = br.readLine()) != null) {
            result += s;
        }
        br.close();
        fr.close();
        return result;
    }

    public   void printFromTo(String From, String To,OsProcess currentProcess,int currentProcessMemoryIndex,Memory memory) {
//        //print values from 'From' to 'To'
        int from=-1;
        int to=-1;

        for(int i=0;i<3;i++){
            if((memory.getFromMemory(currentProcessMemoryIndex + 5 + i) != null) &&((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[0].equals(From)){
                  from=Integer.parseInt((((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[1]).replace(" ",""));
                break;
            }
        }

        for(int i=0;i<3;i++){
            if((memory.getFromMemory(currentProcessMemoryIndex + 5 + i) != null) &&((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[0].equals(To)){
                 to=Integer.parseInt((((String) memory.getFromMemory(currentProcessMemoryIndex + 5 + i)).split(":")[1]).replace(" ",""));
                break;
            }
        }
        for(int i=from;i<=to;i++)
            System.out.println(i);
    }

}
