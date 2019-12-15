import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerUI {
    public JFrame view;

    public JButton btnSalesReport = new JButton("View Sales Report");
    public JButton btnManageProduct = new JButton("Manage Products");
    public JButton btnUpdateSelf = new JButton("Update Your Info");

    public ManagerUI(UserModel user) {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Manager View");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System");

        title.setFont(title.getFont().deriveFont(24.0f));
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnManageProduct);
        panelButtons.add(btnSalesReport);
        panelButtons.add(btnUpdateSelf);

        view.getContentPane().add(panelButtons);

        btnManageProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageProductUI ui = new ManageProductUI();
                ui.run();
            }
        });

        btnSalesReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PurchaseHistoryUI ui = new PurchaseHistoryUI(null);
                ui.run();
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
