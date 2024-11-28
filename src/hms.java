import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class hms {
    public static void main(String[] args) {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private JCheckBox showPasswordCheckbox;
    private JLabel forgotPasswordLabel;
    private Map<String, String> userCredentials = new HashMap<>();

    public LoginFrame() {
        userCredentials.put("admin", "admin");

        setTitle("Highland Hotel");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 12, 44));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(45, 52, 74));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 10, 5, 10);

        addField(formPanel, constraints, "Username:", usernameField = new JTextField(15), 0);
        addField(formPanel, constraints, "Password:", passwordField = new JPasswordField(15), 1);

        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setForeground(Color.WHITE);
        showPasswordCheckbox.setBackground(new Color(45, 52, 54));
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.EAST;
        formPanel.add(showPasswordCheckbox, constraints);

        showPasswordCheckbox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckbox.isSelected() ? (char) 0 : '\u2022');
        });

        forgotPasswordLabel = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgotPasswordLabel.setForeground(Color.GREEN);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        formPanel.add(forgotPasswordLabel, constraints);

        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetPassword();
            }
        });

        JButton loginButton = createButton("Login", new Color(50, 132, 227));
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, constraints);

        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        formPanel.add(statusLabel, constraints);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JLabel headerLabel = new JLabel("Highland Hotel", JLabel.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        add(mainPanel);

        loginButton.addActionListener(e -> handleLogin());

        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    private void addField(JPanel panel, GridBagConstraints constraints, String label, JTextField field, int row) {
        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setFont(new Font("Arial", Font.PLAIN, 18));

        constraints.gridx = 0;
        constraints.gridy = row;
        panel.add(jLabel, constraints);

        constraints.gridx = 1;
        panel.add(field, constraints);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 17));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private boolean authenticate(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private void handleLogin() {
        if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            new HotelDashboard().setVisible(true);
        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }

    private void resetPassword() {
        String masterPassword = JOptionPane.showInputDialog(
                this,
                "Enter master password to reset the admin password:",
                "Master Password",
                JOptionPane.PLAIN_MESSAGE
        );
        if (masterPassword == null) return;

        if (masterPassword.equals("Abdullah")) {
            String newPassword = JOptionPane.showInputDialog(
                    this,
                    "Enter new password for admin:",
                    "New Password",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (newPassword == null || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password reset cancelled.");
                return;
            }
            userCredentials.put("admin", newPassword);
            JOptionPane.showMessageDialog(this, "Password successfully reset.");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect master password. Cannot reset admin password.");
        }
    }
}

class HotelDashboard extends JFrame {
    private ArrayList<String[]> guests = new ArrayList<>();
    private ArrayList<String[]> rooms = new ArrayList<>();
    private ArrayList<String[]> bookings = new ArrayList<>();
    private Map<String, String> accounts = new HashMap<>();
    private int bookingIdCounter = 1;

    private JTable guestTable, bookingTable;
    private DefaultTableModel guestTableModel, roomTableModel, bookingTableModel;

    public HotelDashboard() {
        setTitle("Hotel Management Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        accounts.put("admin", "admin");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 12, 94));

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(150, 150, 150));

        menuBar.add(createGuestManagementMenu());
        menuBar.add(createBookingManagementMenu());
        menuBar.add(createAccountMenu());
        menuBar.add(createPaymentMenu());

        JLabel headerLabel = new JLabel("Hotel Management Dashboard", JLabel.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Guest Details", createGuestDetailsPanel());
        tabbedPane.addTab("Booking Details", createBookingDetailsPanel());

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setJMenuBar(menuBar);
        add(mainPanel);
    }

    private JPanel createGuestDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        guestTableModel = new DefaultTableModel(new String[]{"Guest Name", "Room"}, 0);
        guestTable = new JTable(guestTableModel);
        panel.add(new JScrollPane(guestTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBookingDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        bookingTableModel = new DefaultTableModel(
    new String[]{"ID", "Guest", "Room", "Check-In", "Check-Out", "Payment"}, 0
);
        bookingTable = new JTable(bookingTableModel);
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        return panel;
    }    

    private JMenu createGuestManagementMenu() {
        JMenu guestMenu = new JMenu("Guest Management");

        JMenuItem addGuest = new JMenuItem("Add Guest");
        addGuest.addActionListener(e -> {
            String guestName = JOptionPane.showInputDialog(this, "Enter guest name:");
            if (guestName == null) return; 
            String room = JOptionPane.showInputDialog(this, "Enter room for the guest:");
            if (room == null) return; 
            if (!guestName.isEmpty() && !room.isEmpty()) {
                guests.add(new String[]{guestName, room});
                guestTableModel.addRow(new Object[]{guestName, room});
                addToBookingTable(guestName, room);
                JOptionPane.showMessageDialog(this, "Guest added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Guest details cannot be empty.");
            }
        });
        guestMenu.add(addGuest);

        JMenuItem deleteGuest = new JMenuItem("Delete Guest");
deleteGuest.addActionListener(e -> {
    String guestToDelete = JOptionPane.showInputDialog(this, "Enter guest name to delete:");
    if (guestToDelete == null) return; 

    guests.removeIf(guest -> guest[0].equals(guestToDelete));

    for (int i = 0; i < guestTableModel.getRowCount(); i++) {
        if (guestTableModel.getValueAt(i, 0).equals(guestToDelete)) {
            guestTableModel.removeRow(i);
            break;
        }
    }


    for (int i = bookingTableModel.getRowCount() - 1; i >= 0; i--) {
        if (bookingTableModel.getValueAt(i, 1).equals(guestToDelete)) {
            bookingTableModel.removeRow(i);
            bookings.removeIf(booking -> booking[1].equals(guestToDelete));
        }
    }

    JOptionPane.showMessageDialog(this, "Guest and associated bookings deleted successfully!");
});
guestMenu.add(deleteGuest);
        return guestMenu;
    }

    private JMenu createBookingManagementMenu() {
        JMenu bookingMenu = new JMenu("Booking Management");
    
        JMenuItem addBooking = new JMenuItem("Add Booking");
        addBooking.addActionListener(e -> {
            String guest = JOptionPane.showInputDialog(this, "Enter Guest Name:");
            String room = JOptionPane.showInputDialog(this, "Enter Room:");
            if (guest == null || room == null || guest.isEmpty() || room.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Guest and Room cannot be empty.");
                return;
            }
            addToBookingTable(guest, room);
            guests.add(new String[]{guest, room});
            guestTableModel.addRow(new Object[]{guest, room});
            JOptionPane.showMessageDialog(this, "Booking added successfully!");
        });
        bookingMenu.add(addBooking);
    
        JMenuItem deleteBooking = new JMenuItem("Delete Booking");
        deleteBooking.addActionListener(e -> {
            String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to delete:");
            if (bookingId == null) return;
    
            for (int i = 0; i < bookingTableModel.getRowCount(); i++) {
                if (bookingTableModel.getValueAt(i, 0).equals(bookingId)) {
                    bookingTableModel.removeRow(i);
                    bookings.removeIf(booking -> booking[0].equals(bookingId));
                    JOptionPane.showMessageDialog(this, "Booking deleted successfully!");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Booking ID not found.");
        });
        bookingMenu.add(deleteBooking);
    
        JMenuItem updateBooking = new JMenuItem("Update Booking");
        updateBooking.addActionListener(e -> {
            String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to update:");
            if (bookingId == null) return;
    
            for (int i = 0; i < bookings.size(); i++) {
                String[] booking = bookings.get(i);
                if (booking[0].equals(bookingId)) {
                    String checkInDate = JOptionPane.showInputDialog(this, "Enter Check-In Date (DD/MM/YYYY):");
                    String checkOutDate = JOptionPane.showInputDialog(this, "Enter Check-Out Date (DD/MM/YYYY):");
    
                    if (checkInDate != null && checkOutDate != null) {
                        try {
                            
                            long days = calculateDays(checkInDate, checkOutDate);
                            if (days <= 0) {
                                JOptionPane.showMessageDialog(this, "Check-Out date must be after Check-In date.");
                                return;
                            }
    
                            double paymentAmount = days * 50; // $50 per day
    
                            booking[3] = checkInDate;
                            booking[4] = checkOutDate;
                            booking[5] = String.format("$%.2f", paymentAmount);
    
                            bookingTableModel.setValueAt(checkInDate, i, 3);
                            bookingTableModel.setValueAt(checkOutDate, i, 4);
                            bookingTableModel.setValueAt(String.format("$%.2f", paymentAmount), i, 5);
    
                            JOptionPane.showMessageDialog(this, "Booking updated successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Check-In and Check-Out dates cannot be empty.");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Booking ID not found.");
        });
        bookingMenu.add(updateBooking);
    
        return bookingMenu;
    }
    

    private void addToBookingTable(String guest, String room) {
    String bookingId = String.format("%02d", bookingIdCounter++);
    bookings.add(new String[]{bookingId, guest, room, null, null, null}); 
    bookingTableModel.addRow(new Object[]{bookingId, guest, room, null, null, null}); 
}

    private JMenu createAccountMenu() {
        JMenu accountMenu = new JMenu("Account Management");

        JMenuItem viewAccount = new JMenuItem("View Accounts");
        viewAccount.addActionListener(e -> {
            StringBuilder accountList = new StringBuilder("Accounts:\n");
            for (String account : accounts.keySet()) {
                accountList.append(account).append("\n");
            }
            JOptionPane.showMessageDialog(this, accountList.toString());
        });
        accountMenu.add(viewAccount);

        JMenuItem updateAccount = new JMenuItem("Update Account");
        updateAccount.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Enter account username to update:");
            if (username == null) return; 
            if (accounts.containsKey(username)) {
                String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
                if (newPassword == null || newPassword.isEmpty()) return; 

                accounts.put(username, newPassword);

                JCheckBox adminCheckbox = new JCheckBox("Admin");
                JCheckBox managerCheckbox = new JCheckBox("Manager");
                JCheckBox receptionistCheckbox = new JCheckBox("Receptionist");

                Object[] options = {adminCheckbox, managerCheckbox, receptionistCheckbox};
                int result = JOptionPane.showConfirmDialog(this, options, "Set Account Privileges",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String privileges = "Privileges for " + username + ": ";
                    if (adminCheckbox.isSelected()) privileges += "Admin ";
                    if (managerCheckbox.isSelected()) privileges += "Manager ";
                    if (receptionistCheckbox.isSelected()) privileges += "Receptionist";

                    JOptionPane.showMessageDialog(this, privileges);
                }

                JOptionPane.showMessageDialog(this, "Account updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Account not found.");
            }
        });
        accountMenu.add(updateAccount);

        JMenuItem addAccount = new JMenuItem("Add Account");
        addAccount.addActionListener(e -> {
            String newUsername = JOptionPane.showInputDialog(this, "Enter new account username:");
            if (newUsername == null) return; 
            String newPassword = JOptionPane.showInputDialog(this, "Enter new account password:");
            if (newPassword == null) return; 

            if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                accounts.put(newUsername, newPassword);

                JCheckBox adminCheckbox = new JCheckBox("Admin");
                JCheckBox managerCheckbox = new JCheckBox("Manager");
                JCheckBox receptionistCheckbox = new JCheckBox("Receptionist");

                Object[] options = {adminCheckbox, managerCheckbox, receptionistCheckbox};
                int result = JOptionPane.showConfirmDialog(this, options, "Set Account Privileges",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String privileges = "Privileges for " + newUsername + ": ";
                    if (adminCheckbox.isSelected()) privileges += "Admin ";
                    if (managerCheckbox.isSelected()) privileges += "Manager ";
                    if (receptionistCheckbox.isSelected()) privileges += "Receptionist";

                    JOptionPane.showMessageDialog(this, privileges);
                }

                JOptionPane.showMessageDialog(this, "Account added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid input. Account not added.");
            }
        });
        accountMenu.add(addAccount);

        JMenuItem deleteAccount = new JMenuItem("Delete Account");
        deleteAccount.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Enter username of the account to delete:");
            if (username == null) return; 
            if (accounts.containsKey(username)) {
                if (username.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "The admin account cannot be deleted.");
                } else {
                    accounts.remove(username);
                    JOptionPane.showMessageDialog(this, "Account deleted successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Account not found.");
            }
        });
        accountMenu.add(deleteAccount);

        return accountMenu;
    }
    private JMenu createPaymentMenu() {
        JMenu paymentMenu = new JMenu("Payment Options");
    
        JMenuItem cashPayment = new JMenuItem("Cash Payment");
        cashPayment.addActionListener(e -> {
            String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID for payment:");
            if (bookingId == null) return; 
    
            for (int i = 0; i < bookings.size(); i++) {
                String[] booking = bookings.get(i);
                if (booking[0].equals(bookingId)) {
                    String checkInDate = booking[3];
                    String checkOutDate = booking[4];
    
                    if (checkInDate != null && checkOutDate != null) {
                        long days = calculateDays(checkInDate, checkOutDate);
                        double paymentAmount = days * 50; // $50 per day
                        booking[5] = String.format("$%.2f", paymentAmount); 
                        bookingTableModel.setValueAt(booking[5], i, 5); 
    
                        JOptionPane.showMessageDialog(this,
                                "Cash payment processed successfully!\n" +
                                "Booking ID: " + bookingId + "\n" +
                                "Total Amount: $" + paymentAmount);
                    } else {
                        JOptionPane.showMessageDialog(this, "Check-in and Check-out dates are required to calculate payment.");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Booking ID not found.");
        });
        paymentMenu.add(cashPayment);
    
        JMenuItem onlinePayment = new JMenuItem("Online Payment");
        onlinePayment.addActionListener(e -> {
            String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID for payment:");
            if (bookingId == null) return; 
    
            for (int i = 0; i < bookings.size(); i++) {
                String[] booking = bookings.get(i);
                if (booking[0].equals(bookingId)) {
                    String checkInDate = booking[3];
                    String checkOutDate = booking[4];
    
                    if (checkInDate != null && checkOutDate != null) {
                        long days = calculateDays(checkInDate, checkOutDate);
                        double paymentAmount = days * 50; // $50 per day
    
                        String cardNumber = JOptionPane.showInputDialog(this, "Enter Card Number:");
                        if (cardNumber == null) return; 
                        String expiryDate = JOptionPane.showInputDialog(this, "Enter Expiry Date (MM/YY):");
                        if (expiryDate == null) return; 
                        String cvv = JOptionPane.showInputDialog(this, "Enter CVV:");
                        if (cvv == null) return; 
    
                        if (!cardNumber.isEmpty() && !expiryDate.isEmpty() && !cvv.isEmpty()) {
                            booking[5] = String.format("$%.2f", paymentAmount); 
                            bookingTableModel.setValueAt(booking[5], i, 5); 
    
                            JOptionPane.showMessageDialog(this,
                                    "Online payment processed successfully!\n" +
                                    "Booking ID: " + bookingId + "\n" +
                                    "Total Amount: $" + paymentAmount);
                        } else {
                            JOptionPane.showMessageDialog(this, "Payment cancelled due to incomplete information.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Check-in and Check-out dates are required to calculate payment.");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Booking ID not found.");
        });
        paymentMenu.add(onlinePayment);
    
        return paymentMenu;
    }  
    
    private long calculateDays(String checkInDate, String checkOutDate) {
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date checkIn = dateFormat.parse(checkInDate);
            java.util.Date checkOut = dateFormat.parse(checkOutDate);
    
            long differenceInMillis = checkOut.getTime() - checkIn.getTime();
            return differenceInMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(this, "Please ensure they are in the format dd/MM/yyyy.");
            return 0;
        }
    }

}