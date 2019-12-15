import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManageCustomerUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load Customer");
    public JButton btnSave = new JButton("Save Customer");

    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPhoneNumber = new JTextField(20);
    public JTextField txtAddress = new JTextField(20);

    public ManageCustomerUI() {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update Customer Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("CustomerID "));
        line1.add(txtCustomerID);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Name "));
        line2.add(txtName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Phone Number "));
        line3.add(txtPhoneNumber);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Address "));
        line4.add(txtAddress);
        view.getContentPane().add(line4);

        btnLoad.addActionListener(new LoadButtonListerner());

        btnSave.addActionListener(new SaveButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();
            Gson gson = new Gson();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }

            // do client/server

            try {
                Socket link = new Socket("localhost", 1000);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_CUSTOMER;
                msg.data = Integer.toString(customer.mCustomerID);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "Customer NOT exists!");
                } else {
                    customer = gson.fromJson(msg.data, CustomerModel.class);
                    txtName.setText(customer.mName);
                    txtAddress.setText(customer.mAddress);
                    txtPhoneNumber.setText(customer.mPhone);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();
            Gson gson = new Gson();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null!");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }

            String name = txtName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Customer name cannot be empty!");
                return;
            }

            customer.mName = name;

            String phone = txtPhoneNumber.getText();
            customer.mPhone = phone.length() == 0 ? null : phone;

            String address = txtAddress.getText();

            customer.mAddress = address.length() == 0 ? null : address;

            // all customer info is ready! Send to Server!

            try {
                Socket link = new Socket("localhost", 1000);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.PUT_CUSTOMER;
                msg.data = gson.toJson(customer);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class); // receive from Server

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "Customer is NOT saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Customer is SAVED successfully!");
                    view.dispose();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
