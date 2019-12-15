import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManageUserUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load User");
    public JButton btnSave = new JButton("Save User");

    public JTextField txtUserName = new JTextField(20);
    public JTextField txtUserType = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPassword = new JPasswordField(20);

    enum mode {
        newUser, updateType, selfUpdate
    };

    public ManageUserUI(UserModel user, int updateMode) {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update User Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        txtName.setText(user.mFullname);
        txtPassword.setText(user.mPassword);

        JPanel panelButtons = new JPanel(new FlowLayout());
        if (updateMode == mode.updateType.ordinal())
            panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        if (updateMode == mode.newUser.ordinal() || updateMode == mode.updateType.ordinal()) {
            JPanel line1 = new JPanel(new FlowLayout());
            line1.add(new JLabel("Username "));
            line1.add(txtUserName);
            view.getContentPane().add(line1);

            JPanel line3 = new JPanel(new FlowLayout());
            line3.add(new JLabel("User Type "));
            line3.add(txtUserType);
            view.getContentPane().add(line3);
        }

        if (updateMode == mode.newUser.ordinal() || updateMode == mode.selfUpdate.ordinal()) {
            JPanel line2 = new JPanel(new FlowLayout());
            line2.add(new JLabel("Name "));
            line2.add(txtName);
            view.getContentPane().add(line2);

            JPanel line4 = new JPanel(new FlowLayout());
            line4.add(new JLabel("Password "));
            line4.add(txtPassword);
            view.getContentPane().add(line4);
        }

        btnSave.addActionListener(new SaveButtonListener(user, updateMode));
        btnLoad.addActionListener(new LoadButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();
            Gson gson = new Gson();
            String userName = txtUserName.getText();

            if (userName.length() == 0) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                return;
            }

            // do client/server

            try {
                Socket link = new Socket("localhost", 1000);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_USER;
                msg.data = userName;
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "User NOT exists!");
                } else {
                    user = gson.fromJson(msg.data, UserModel.class);
                    txtUserType.setText(Integer.toString(user.mUserType));
                    txtName.setText(user.mFullname);
                    txtPassword.setText(user.mPassword);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        UserModel user;
        int mode;

        SaveButtonListener(UserModel user, int mode) {
            this.user = user;
            this.mode = mode;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Gson gson = new Gson();
            String userName = txtUserName.getText();
            String userType = txtUserType.getText();
            String fullName = txtName.getText();
            String password = txtPassword.getText();

            if (mode != 2) {
                if (userName.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty!");
                    return;
                }

                try {
                    user.mUserType = Integer.parseInt(userType);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "User type is invalid!");
                    return;
                }
            }
            if (mode != 1) {

                if (fullName.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Name cannot be empty!");
                    return;
                }

                if (password.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                    return;
                }
            }

            user.mFullname = fullName;
            user.mPassword = password;
            if (mode != 2) {
                user.mUserType = Integer.parseInt(userType);
                user.mUsername = userName;
            }

            // all user info is ready! Send to Server!

            try {
                Socket link = new Socket("localhost", 1000);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.PUT_USER;
                msg.data = gson.toJson(user);
                output.println(gson.toJson(msg)); // send to Server

                msg = gson.fromJson(input.nextLine(), MessageModel.class); // receive from Server

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "User is NOT saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "User is SAVED successfully!");
                    view.dispose();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
