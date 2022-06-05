import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class OS {

    private int timeSlice;
    ArrayList<OsProcess> processes;
    Memory memory;


    public OS(int timeSlice,ArrayList<OsProcess> processes) throws URISyntaxException, IOException {
        this.timeSlice = timeSlice;
        this.processes = processes;
        this.memory = new Memory();


        scheduler scheduler = new scheduler(timeSlice,this.processes,memory);

    }

    public int getTimeSlice() {
        return this.timeSlice;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        OsProcess process1=new OsProcess("Program_1.txt",0);
        OsProcess process2=new OsProcess("Program_2.txt",1);
        OsProcess process3=new OsProcess("Program_3.txt",4);

        ArrayList<OsProcess> processes = new ArrayList<>();
        processes.add(process1);
        processes.add(process2);
        processes.add(process3);

        OS os = new OS(2, processes);

        //System.out.println(processState.READY+" mina");







    }

}
