package hms.models;

import java.time.LocalDate;

public class Medicine {

    private int       medicineId;
    private String    name;
    private String    category;
    private String    manufacturer;
    private double    price;
    private int       stockQuantity;
    private LocalDate expiryDate;

    public Medicine(int medicineId, String name, String category,
                    String manufacturer, double price,
                    int stockQuantity, LocalDate expiryDate) {
        this.medicineId    = medicineId;
        this.name          = name;
        this.category      = category;
        this.manufacturer  = manufacturer;
        this.price         = price;
        this.stockQuantity = stockQuantity;
        this.expiryDate    = expiryDate;
    }

    // Getters
    public int       getMedicineId()    { return medicineId; }
    public String    getName()          { return name; }
    public String    getCategory()      { return category; }
    public String    getManufacturer()  { return manufacturer; }
    public double    getPrice()         { return price; }
    public int       getStockQuantity() { return stockQuantity; }
    public LocalDate getExpiryDate()    { return expiryDate; }

    // Setters
    public void setPrice(double price)             { this.price = price; }
    public void setStockQuantity(int qty)          { this.stockQuantity = qty; }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isLowStock() {
        return stockQuantity < 20;
    }

    @Override
    public String toString() {
        return "Medicine #" + medicineId
                + " | " + name
                + " | Category: " + category
                + " | Price: Rs." + price
                + " | Stock: " + stockQuantity
                + (isExpired()  ? " [EXPIRED]"   : "")
                + (isLowStock() ? " [LOW STOCK]" : "");
    }
}