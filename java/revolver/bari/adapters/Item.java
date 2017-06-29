package revolver.bari.adapters;

public class Item {
    public Item(String text, int res, boolean divider, boolean checkable) {
        this.text = text;
        this.res = res;
        this.divider = divider;
        this.checkable = checkable;
    }
    public String text;
    public int res;
    public boolean divider;
    public boolean checkable;
}
