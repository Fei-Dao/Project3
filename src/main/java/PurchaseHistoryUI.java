import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PurchaseHistoryUI {

    public JFrame view;
    // public JList purchases;
    public JTable purchaseTable;

    public UserModel user = null;

    public PurchaseHistoryUI(UserModel user) {
        this.user = user;

        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (user == null) {
            view.setTitle("Sales Report");
        } else {
            view.setTitle("Your Purchase History");
        }

        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title;
        if (user == null) {
            title = new JLabel("Sales Report");
        } else {
            title = new JLabel("Purchase history for " + user.mFullname);
        }

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        view.getContentPane().add(title);

        PurchaseListModel list;
        // user will be null if manager, the -1 will be to get all purchase history
        if (user == null) {
            list = StoreManager.getInstance().getDataAdapter().loadPurchaseHistory(-1);
        } else {
            list = StoreManager.getInstance().getDataAdapter().loadPurchaseHistory(user.mCustomerID);
        }

        // DefaultListModel<String> data = new DefaultListModel<>();
        DefaultTableModel tableData = new DefaultTableModel();
        // String[] columnNames = {"PurchaseID", "ProductID", "Total"};
        // data.addElement(String.format("PurchaseId: %d, ProductId: %d, Total: %8.2f",
        // purchase.mPurchaseID,
        // purchase.mProductID,
        // purchase.mTotal));

        tableData.addColumn("PurchaseID");
        tableData.addColumn("ProductID");
        tableData.addColumn("Product Name");
        tableData.addColumn("Total");

        for (PurchaseModel purchase : list.purchases) {
            Object[] row = new Object[tableData.getColumnCount()];
            row[0] = purchase.mPurchaseID;
            row[1] = purchase.mProductID;
            ProductModel product = StoreManager.getInstance().getDataAdapter().loadProduct(purchase.mProductID);
            row[2] = product.mName;
            row[3] = purchase.mTotal;
            tableData.addRow(row);
        }

        // purchases = new JList(data);

        purchaseTable = new JTable(tableData);

        JScrollPane scrollableList = new JScrollPane(purchaseTable);

        view.getContentPane().add(scrollableList);

    }

    public void run() {
        view.setVisible(true);
    }
}