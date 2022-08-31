package com.example.allergentrackerbeta;

public class Product
{

    public int pNum; // product number / ID
    public String pName; // product name
    public String cName; // company name
    public String barcode; // barcode number
    public String allergens; // allergens in product

    public Product() // needed because there is an instance which product gets no arguments in scan operation
    {
        this.pNum = 99999;
        this.pName = "0000";
        this.cName = "0500000000";
        this.barcode = "0";
        this.allergens = "";
    }
    public Product(String pName, String cName, String barcode, String allergens, int num)
    {
        this.pNum = num;
        this.pName = pName;
        this.cName = cName;
        this.barcode = barcode;
        this.allergens = allergens;
    }

    @Override
    public String toString() {
        return "שם המוצר: " +pName +"\nאלרגנים: " + allergens;
    }
}


