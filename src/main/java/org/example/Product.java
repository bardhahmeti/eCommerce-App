public class Product {
    private String id;
    private double price;
    private int stock;

    public Product(String id, double price, int stock) {
        this.id = id;
        this.price = price;
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}