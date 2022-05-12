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

    public void print(String s) {
        if(currentProcess.checkIfExists(s))
            System.out.println(currentProcess.getFromProcessMemory(s));
        else
            System.out.println(s);
    }

    public   void assign(String[] value) {
        String s=value[2];
        if(value.length>3) {
            s="";
            for (int i = 2; i < value.length; i++) {
                s += value[i] + " ";
            }
        }
        if(currentProcess.checkIfExists(s))
            currentProcess.addToProcessMemory(value[1], currentProcess.getFromProcessMemory(s));
        else
            currentProcess.addToProcessMemory(value[1], s);
    }
    //what if the file is not there?
    public   void writeFile(String fileName, String content) throws URISyntaxException, IOException {
        //create new file in the file system
        //write content to the file
        fileName= (String) currentProcess.getFromProcessMemory(fileName);
        content= (String) currentProcess.getFromProcessMemory(content);
        FileWriter file = new FileWriter(fileName + ".txt");
        file.write(content + "");
        file.close();
    }

    public String readFile(String fileName) throws URISyntaxException, IOException {
        fileName= (String) currentProcess.getFromProcessMemory(fileName);
        //read file and return the content
        /*
        String result = "";
        URL path = ClassLoader.getSystemResource(fileName);
        File f = new File(path.toURI());
        BufferedReader br = new BufferedReader(new FileReader(f));
        String st;
        while (( st=br.readLine()) != null) {
            result += st+"\n";
        }
        return result;


         */

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

    public   void printFromTo(String From, String To) {
        //print values from 'From' to 'To'
        int from=Integer.parseInt(""+ currentProcess.getFromProcessMemory(From));
        int to=Integer.parseInt((""+ currentProcess.getFromProcessMemory(To)));
        for(int i=from;i<=to;i++)
            System.out.println(i);
    }

}
