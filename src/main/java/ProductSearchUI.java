import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductSearchUI {

    public JFrame view;
    public JTable productTable;
    public UserModel user = null;

    public JTextField txtSearchTitle = new JTextField(20);
    public JTextField txtSearchMinPrice = new JTextField(20);
    public JTextField txtSearchMaxPrice = new JTextField(20);
    public JButton btnSearch = new JButton("Search");

    public ProductSearchUI(UserModel user) {
        this.user = user;

        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Search Product");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Search results for " + user.mFullname);

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnSearch);
        view.getContentPane().add(panelButtons);

        JPanel line = new JPanel(new FlowLayout());
        line.add(new JLabel("Product Name "));
        line.add(txtSearchTitle);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("Min Price "));
        line.add(txtSearchMinPrice);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("Max Price "));
        line.add(txtSearchMaxPrice);
        view.getContentPane().add(line);

        DefaultTableModel tableData = new DefaultTableModel();

        tableData.addColumn("ProductID");
        tableData.addColumn("Product Name");
        tableData.addColumn("Price");
        tableData.addColumn("Quantity");

        productTable = new JTable(tableData);

        JScrollPane scrollableList = new JScrollPane(productTable);

        view.getContentPane().add(scrollableList);

        btnSearch.addActionListener(new SearchButtonListener(tableData));

    }

    class SearchButtonListener implements ActionListener {
        DefaultTableModel tableData;

        SearchButtonListener(DefaultTableModel tableData) {
            this.tableData = tableData;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductListModel list = StoreManager.getInstance().getDataAdapter().searchProduct(txtSearchTitle.getText(),
                    Double.parseDouble(txtSearchMinPrice.getText()), Double.parseDouble(txtSearchMaxPrice.getText()));

            // purchases = new JList(data);
            for (ProductModel p : list.products) {
                Object[] row = new Object[tableData.getColumnCount()];

                row[0] = p.mProductID;
                row[1] = p.mName;
                row[2] = p.mPrice;
                row[3] = p.mQuantity;
                tableData.addRow(row);
            }
        }
    }
}