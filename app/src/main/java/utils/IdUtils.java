package utils;

public class IdUtils {
    private int id;
    public IdUtils(){
        this.id=1;
    }
    public IdUtils(int id) {
        this.id = id;
    }
    public int generateViewId() {
        return id++;
    }
}
