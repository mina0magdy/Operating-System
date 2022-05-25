public class Memory {
    private Object[] memory;
    private boolean[] occupiedSlots;


    public Memory() {
        this.memory = new Object[40];
        this.occupiedSlots = new boolean[2];
    }

    public Object getFromMemory(int index) {
        return memory[index];
    }

    public void setInMemory(int index, Object value) {
            memory[index] = value;
        }

    public boolean isOccupied(int index) {
            return occupiedSlots[index];
        }

    public void setOccupied(int index, boolean value) {
            occupiedSlots[index] = value;
        }

    public void clearMemoryFromTo(int from, int to) {
        for (int i = from; i < to; i++) {
                    memory[i] = null;
                }
            }

    public void clearMemoryOfTerminatedProcess(int processId) {
        if(memory[0]!=null&&(int)memory[0]==processId) {
            clearMemoryFromTo(0,20);
        }
        else if(memory[20]!=null&&(int)memory[20]==processId) {
            clearMemoryFromTo(20,40);
        }
    }


}
