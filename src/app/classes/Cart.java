package app.classes;

import java.util.ArrayList;
public class Cart {
    ArrayList<Product> list;

    public Cart() {
        this.list = new ArrayList<>();
    }

    public void add(Product p) {
        list.add(p);
    }

    public void remove(Product p) {
        list.remove(p);
    }

    public void remove(int index) {
        list.remove(index);
    }

    public void clear() {
        list.clear();
    }

    public int getCartSize() {
        return list.size();
    }
}
