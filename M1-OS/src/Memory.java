public class Memory {
    private MemoryWord[] memory;



    public Memory() {
        this.memory = new MemoryWord[40];
        memory[0] = new MemoryWord("ProcessID", null);
        memory[1] = new MemoryWord("ProcessState", null);
        memory[2] = new MemoryWord("PC", 8);
        memory[3] = new MemoryWord("StartInMemory", null);
        memory[4] = new MemoryWord("EndInMemory", null);
        memory[5] = new MemoryWord("Variable1", null);
        memory[6] = new MemoryWord("Variable2", null);
        memory[7] = new MemoryWord("Variable3", null);

        for(int i=8;i<20;i++)
            memory[i] = new MemoryWord("Instruction" + (i-8) + "", null);

        memory[20] = new MemoryWord("ProcessID", null);
        memory[21] = new MemoryWord("ProcessState", null);
        memory[22] = new MemoryWord("PC", 8);
        memory[23] = new MemoryWord("StartInMemory", null);
        memory[24] = new MemoryWord("EndInMemory", null);
        memory[25] = new MemoryWord("Variable1", null);
        memory[26] = new MemoryWord("Variable2", null);
        memory[27] = new MemoryWord("Variable3", null);

        for (int i = 28; i < 40; i++)
            memory[i] = new MemoryWord("Instruction" + (i-28) + "", null);

    }

    public String getKey(int index){
        return memory[index].getKey();
    }

    public Object getFromMemory(int index) {
        return memory[index].getValue();
    }



    public void setInMemory(int index, Object value) {
            memory[index].setValue(value);
        }



    public void clearMemoryFromTo(int from, int to) {
        for (int i = from; i < to; i++) {
                    memory[i].setValue(null);
                }
            }

    public void clearMemoryOfSwappedProcess(int processId) {
        if(memory[0].getValue()!=null&&(int)memory[0].getValue()==processId) {
            clearMemoryFromTo(0,20);
        }
        else if(memory[20].getValue()!=null&&(int)memory[20].getValue()==processId) {
            clearMemoryFromTo(20,40);
        }
    }


}
