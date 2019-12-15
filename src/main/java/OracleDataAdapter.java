import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDataAdapter implements IDataAdapter {

    Connection conn = null;

    public int connect(String dbFile) {
        try {
            // db parameters
            String url = "jdbc:oracle:thin" + dbFile;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to Oracle database has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_OPEN_FAILED;
        }
        return CONNECTION_OPEN_OK;
    }

    @Override
    public int disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_CLOSE_FAILED;
        }
        return CONNECTION_CLOSE_OK;
    }

    public ProductModel loadProduct(int productID) {
        ProductModel product = null;

        try {
            String sql = "SELECT ProductID, Name, Price, Quantity FROM Product WHERE ProductID = " + productID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                product = new ProductModel();
                product.mProductID = rs.getInt("ProductID");
                product.mName = rs.getString("Name");
                product.mPrice = rs.getDouble("Price");
                product.mQuantity = rs.getDouble("Quantity");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return product;
    }

    public int saveProduct(ProductModel product) {
        try {
            String sql = "INSERT INTO Product(ProductID, Name, Price, Quantity) VALUES " + product;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PRODUCT_SAVE_FAILED;
        }

        return PRODUCT_SAVE_OK;
    }

    public CustomerModel loadCustomer(int customerID) {
        CustomerModel customer = new CustomerModel();

        try {
            String sql = "SELECT CustomerID, Name, Address, PhoneNumber FROM Customer WHERE CustomerID = " + customerID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            customer.mCustomerID = rs.getInt("CustomerID");
            customer.mName = rs.getString("Name");
            customer.mAddress = rs.getString("Address");
            customer.mPhone = rs.getString("Phone");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }

    public int saveCustomer(CustomerModel customer) {
        try {
            String sql = "INSERT INTO Customer(CustomerID, Name, Address, PhoneNumber) VALUES " + customer;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
            if (message.contains("UNIQUE constraint failed"))
                return CUSTOMER_SAVE_FAILED;
        }
        return CUSTOMER_SAVE_OK;
    }

    public PurchaseModel loadPurchase(int purchaseID) {
        PurchaseModel purchase = new PurchaseModel();

        try {
            String sql = "SELECT PurchaseID, ProductID, CustomerID, PurchaseDateTime, Price,"
                    + " Quantity, Cost, Tax, TotalCost FROM Purchase WHERE PurchaseID = " + purchaseID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            purchase.mPurchaseID = rs.getInt("PurchaseID");
            purchase.mProductID = rs.getInt("ProductID");
            purchase.mCustomerID = rs.getInt("CustomerID");
            purchase.mDate = rs.getString("PurchaseDateTime");
            purchase.mPrice = rs.getDouble("Price");
            purchase.mQuantity = rs.getDouble("Quantity");
            purchase.mCost = rs.getDouble("Cost");
            purchase.mTax = rs.getDouble("Tax");
            purchase.mTotal = rs.getDouble("TotalCost");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return purchase;
    }

    public int savePurchase(PurchaseModel purchase) {
        try {
            Statement stmt = conn.createStatement();

            // SQL for writing purchase into database
            String purchaseSQL = "INSERT INTO Purchase(PurchaseID, ProductID, CustomerID, PurchaseDateTime,"
                    + " Price, Quantity, Cost, Tax, TotalCost) VALUES " + purchase;
            System.out.println(purchaseSQL);
            stmt.executeUpdate(purchaseSQL);

            ProductModel product = loadProduct(purchase.mProductID);

            // SQL for updating product stock following a purchase
            String productSQL = "UPDATE Product SET Quantity = " + (product.mQuantity - purchase.mQuantity)
                    + " WHERE ProductID = " + purchase.mProductID;
            System.out.println(productSQL);
            stmt.executeUpdate(productSQL);
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
            if (message.contains("UNIQUE constraint failed"))
                return PURCHASE_SAVE_FAILED;
        }
        return PURCHASE_SAVE_OK;
    }

    @Override
    public PurchaseListModel loadPurchaseHistory(int id) {
        PurchaseListModel res = new PurchaseListModel();
        try {
            String sql = "SELECT * FROM Purchases WHERE CustomerId = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PurchaseModel purchase = new PurchaseModel();
                purchase.mCustomerID = id;
                purchase.mPurchaseID = rs.getInt("PurchaseID");
                purchase.mProductID = rs.getInt("ProductID");
                purchase.mPrice = rs.getDouble("Price");
                purchase.mQuantity = rs.getDouble("Quantity");
                purchase.mCost = rs.getDouble("Cost");
                purchase.mTax = rs.getDouble("Tax");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");

                res.purchases.add(purchase);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    @Override
    public ProductListModel searchProduct(String name, double minPrice, double maxPrice) {
        ProductListModel res = new ProductListModel();
        try {
            String sql = "SELECT * FROM Products WHERE Name LIKE \'%" + name + "%\' " + "AND Price >= " + minPrice
                    + " AND Price <= " + maxPrice;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.mProductID = rs.getInt("ProductID");
                product.mName = rs.getString("Name");
                product.mPrice = rs.getDouble("Price");
                product.mQuantity = rs.getDouble("Quantity");
                res.products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public UserModel loadUser(String username) {
        UserModel user = null;

        try {
            String sql = "SELECT * FROM Users WHERE Username = \"" + username + "\"";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                user = new UserModel();
                user.mUsername = username;
                user.mPassword = rs.getString("Password");
                user.mFullname = rs.getString("Fullname");
                user.mUserType = rs.getInt("Usertype");
                if (user.mUserType == UserModel.CUSTOMER)
                    user.mCustomerID = rs.getInt("CustomerID");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public int saveUser(UserModel user) {
        return 0;
    }
}
