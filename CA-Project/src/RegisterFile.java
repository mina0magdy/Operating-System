public class RegisterFile {
    private Register[] regFile;
    private int regWrite;
    private int readReg1;
    private int readReg2;
    private int writeReg;
    private int writeData;
    private int readData1;
    private int readData2;



    public RegisterFile() {
        regFile = new Register[32];

        for (int i = 0; i < 32; i++) {
            String name = "R" + i;
            regFile[i] = new Register(name);
        }
    }

    public void setRegWrite(int regWrite) {
        this.regWrite = regWrite;
    }

    public void setReadReg1(int readReg1) {
        this.readReg1 = readReg1;
    }

    public void setReadReg2(int readReg2) {
        this.readReg2 = readReg2;
    }

    public void setWriteReg(int writeReg) {
        this.writeReg = writeReg;
    }

    public void setWriteData(int writeData) {
        this.writeData = writeData;
    }

    public int getReadData1() {
        return readData1;
    }

    public int getReadData2() {
        return readData2;
    }



}
