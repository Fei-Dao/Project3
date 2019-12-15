import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminUI {
    public JFrame view;

    public JButton btnAddUser = new JButton("Add New User");
    public JButton btnUpdateUserType = new JButton("Update a User's Type");
    public JButton btnUpdateSelf = new JButton("Update Your Info");

    public AdminUI(UserModel user) {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Admin View");
        view.setSize(400, 300);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System");

        title.setFont(title.getFont().deriveFont(24.0f));
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAddUser);
        panelButtons.add(btnUpdateUserType);
        panelButtons.add(btnUpdateSelf);

        view.getContentPane().add(panelButtons);

        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageUserUI ap = new ManageUserUI(new UserModel(), 0);
                ap.run();
            }
        });

        btnUpdateUserType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageUserUI ap = new ManageUserUI(new UserModel(), 1);
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