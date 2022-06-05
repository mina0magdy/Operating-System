public class MemoryWord {
    private String key;
    private Object value;

    public MemoryWord(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return "MemoryWord [key=" + key + ", value=" + value + "]";
    }


}
