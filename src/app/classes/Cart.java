package app.classes;

import java.io.Serializable;
import java.util.ArrayList;
public class Cart implements Serializable {
    private final ArrayList<Product> list;
    private double total;
    public Cart() {
        this.list = new ArrayList<>();
        this.total = 0;
    }

    private int getQuantityInList (Product p) {
        for (Product a: this.list)
            if (a.getName().equals(p.getName()) && a.getPrice() == p.getPrice())
                return a.getQuantity();
        return 0;
    }
    public void add(Product p) {
        int quantity = getQuantityInList(p);
        if (quantity > 0)
            list.add(new Product(p.getName(), p.getPrice(), quantity + 1));
        else
            list.add(new Product(p.getName(), p.getPrice(), 1));
        this.total += p.getPrice();
    }
    public void remove(Product p) {
        list.remove(p);
        total -= p.getPrice();
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

    public ArrayList<Product> getCartList() {
        return this.list;
    }

    public double getTotal() {
        return total;
    }
}
