package ca.ubc.cs304.model;

public class Customer {
    private final int phoneNumber;
    private final String name;
    private final String address;
    private final int dLicense;

    public Customer(int phoneNumber, String name, String address, int dLicense ) {
        this.phoneNumber= phoneNumber;
        this.name = name;
        this.address = address;
        this.dLicense = dLicense;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public int getdLicense() {
        return dLicense;
    }
}

