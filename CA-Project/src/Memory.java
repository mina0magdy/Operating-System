public class Memory {
    private int[] mem;
    private int memWrite;
    private int memRead;
    private int aluResult;

    public Memory(int size) {
        mem = new int[size];
        this.memWrite = 0;
        this.memRead = 0;
        this.aluResult = 0;
    }

    public int get(int index) {
        return mem[index];
    }

    public void set(int index, int value) {
        mem[index] = value;
    }

    public void setMemWrite(int value) {
        memWrite = value;
    }

    public void setMemRead(int value) {
        memRead = value;
    }




}
