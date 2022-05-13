import java.util.Objects;

public class Register {
   private String name;
   private int data;


    public Register(String name) {
        this.name = name;
        this.data = 0;
    }

    public void setData(int data) {
        if(Objects.equals(name, "R0")){
            System.out.println("Cannot write to R0");
        }
        else{
            this.data = data;
        }
    }

    public int getData() {
        return this.data;
    }

    public String getName() {
        return this.name;
    }
}
