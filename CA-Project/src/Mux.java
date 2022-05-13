public class Mux {
  private int input1;
   private int input2;


    public Mux() {
        input1 = 0;
        input2 = 0;
    }

    public int getOutput(int control) {
        if (control == 0) {
            return input1;
        } else {
            return input2;
        }
    }

    public void setInput1(int input) {
        input1 = input;
    }

    public void setInput2(int input) {
        input2 = input;
    }
}
