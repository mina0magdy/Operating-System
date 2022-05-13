public class Alu {
    private int inputA;
    private int inputB;
    private int output;
    private int control;
    private int zero;


    public Alu() {
        inputA = 0;
        inputB = 0;
        output = 0;
        control = 0;
        zero = 0;
    }
    public int getOutput() {
         switch (control) {
            case 0 -> output=inputA + inputB;
            case 1 -> output=inputA - inputB;
            case 2 -> output=inputA * inputB;
            case 3 -> output=inputA & inputB;
            case 4 -> output=inputA | inputB;
            case 5 -> output=inputA << inputB;
            case 6 -> output=inputA >> inputB;
            default -> throw new IllegalStateException("Unexpected value: " + control);
        };

        if(output==0)
            zero=1;
        else
            zero=0;

        return output;
    }

    public void setInputA(int inputA) {
        this.inputA = inputA;
    }

    public void setInputB(int inputB) {
        this.inputB = inputB;
    }

    public int getInputA() {
        return inputA;
    }

    public int getInputB() {
        return inputB;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public int getControl() {
        return control;
    }

    public int getZero() {
        return zero;
    }




}
