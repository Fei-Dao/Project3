import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierUI {
    public JFrame view;

    public JButton btnManageCustomer = new JButton("Manage Customers");
    public JButton btnAddPurchase = new JButton("Add New Purchase");
    public JButton btnUpdatePurchase = new JButton("Update a Purchase");
    public JButton btnUpdateSelf = new JButton("Update Your Info");

    public CashierUI(UserModel user) {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Cashier View");
        view.setSize(400, 300);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System");

        title.setFont(title.getFont().deriveFont(24.0f));
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnManageCustomer);
        panelButtons.add(btnAddPurchase);
        panelButtons.add(btnUpdatePurchase);
        panelButtons.add(btnUpdateSelf);

        view.getContentPane().add(panelButtons);

        btnManageCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageCustomerUI ap = new ManageCustomerUI();
                ap.run();
            }
        });

        btnAddPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManagePurchaseUI ap = new ManagePurchaseUI(false);
                ap.run();
            }
        });

        btnUpdatePurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManagePurchaseUI ap = new ManagePurchaseUI(true);
                ap.run();
            }
        });

        btnUpdateSelf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageUserUI ap = new ManageUserUI(user, 2);
                ap.run();
            }
        });
    }
}