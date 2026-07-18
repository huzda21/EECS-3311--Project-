package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Conference Room Scheduler - Swing GUI
 * Demonstrates all six D2 patterns end-to-end:
 *   Singleton         -> ChiefEventCoordinator generates admin accounts (Req2)
 *   Strategy          -> User.getHourlyRate() by role (Req3)
 *   Template Method   -> User.booking() fixed algorithm (Req4)
 *   State             -> Room enable/disable/close (Req6)
 *   Factory Method    -> PaymentFactory creates Credit/Debit or Institutional payment (Req10)
 *   Observer          -> Sensor notifies CheckInManager on badge scan (Req5)
 */
public class ConfrenceRoomGui extends JFrame {

    private JTextArea logArea;
    private JLabel statusLabel;
    private JTabbedPane tabs;
    private DefaultTableModel roomTableModel;
    private JTable roomTable;

    public ConfrenceRoomGui() {
        super("YorkU Conference Room Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        statusLabel = new JLabel("Not logged in.");
        logArea = new JTextArea(8, 80);
        logArea.setEditable(false);

        tabs = new JTabbedPane();
        tabs.addTab("Account", buildAccountPanel());
        tabs.addTab("Browse & Book", buildBookingPanel());
        tabs.addTab("Admin Panel", buildAdminPanel());

        JPanel top = new JPanel(new BorderLayout());
        top.add(statusLabel, BorderLayout.WEST);
        top.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void refreshStatus() {
        if (AppData.currentUser == null) {
            statusLabel.setText("Not logged in.");
        } else {
            statusLabel.setText("Logged in: " + AppData.currentUser.getEmail()
                    + " (" + AppData.currentUser.getRoleName() + ")");
        }
    }

    // ---------------------------------------------------------------
    // ACCOUNT TAB: register + login (Req1)
    // ---------------------------------------------------------------
    private JPanel buildAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField regEmail = new JTextField(18);
        JPasswordField regPassword = new JPasswordField(18);
        JComboBox<String> regType = new JComboBox<>(new String[]{"Student", "Faculty", "Staff", "Partner"});
        JTextField regNumber = new JTextField(18);
        JButton registerBtn = new JButton("Register");

        JTextField loginEmail = new JTextField(18);
        JPasswordField loginPassword = new JPasswordField(18);
        JButton loginBtn = new JButton("Login");
        JButton logoutBtn = new JButton("Logout");

        int row = 0;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("-- Register (Req1) --"), c);
        row++;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Email:"), c);
        c.gridx = 1; c.gridy = row; panel.add(regEmail, c);
        row++;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; c.gridy = row; panel.add(regPassword, c);
        row++;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Account Type:"), c);
        c.gridx = 1; c.gridy = row; panel.add(regType, c);
        row++;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Student#/Employee#/Staff#/Partner#:"), c);
        c.gridx = 1; c.gridy = row; panel.add(regNumber, c);
        row++;
        c.gridx = 1; c.gridy = row; panel.add(registerBtn, c);
        row++;

        c.gridx = 0; c.gridy = row; panel.add(new JLabel("-- Login --"), c);
        row++;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Email:"), c);
        c.gridx = 1; c.gridy = row; panel.add(loginEmail, c);
        row++;
        c.gridx = 0; c.gridy = row; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; c.gridy = row; panel.add(loginPassword, c);
        row++;
        JPanel btnRow = new JPanel();
        btnRow.add(loginBtn);
        btnRow.add(logoutBtn);
        c.gridx = 1; c.gridy = row; panel.add(btnRow, c);

        registerBtn.addActionListener(e -> {
            try {
                String email = regEmail.getText().trim();
                String pw = new String(regPassword.getPassword());
                long num = Long.parseLong(regNumber.getText().trim());
                
                //check if email exists
                for (User existing : AppData.users) {
                    if (existing.getEmail().equalsIgnoreCase(email)) {
                        JOptionPane.showMessageDialog(this, "An account with this email already exists.");
                        return;
                    }
                }

                //password requirements
                if (!pw.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$")) {
                    JOptionPane.showMessageDialog(this,
                        "Password must be at least 8 characters and contain an uppercase letter, lowercase letter, number, and symbol.");
                    return;
                }
                
                String id = "U-" + System.currentTimeMillis() % 100000;
                String accountType = (String) regType.getSelectedItem();
                //checks when student account selected that the email is from yorku
                boolean isVerified;
                if (accountType.equalsIgnoreCase("Student") || accountType.equalsIgnoreCase("Faculty")) {
                    isVerified = email.toLowerCase().endsWith("@yorku.ca");
                    if (!isVerified) {
                        JOptionPane.showMessageDialog(this, "University accounts must use a valid @yorku.ca email.");
                        return;
                    }
                } else {
                    isVerified = true; // Staff/Partner accounts don't require university verification
                }
                User u = UserFactory.createUser(accountType, id, email, pw, isVerified, num);

                AppData.users.add(u);
                log("[Account] Registered " + u.getRoleName() + " account: " + email);
                JOptionPane.showMessageDialog(this, "Registered successfully. You can now log in.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for the ID field.");
            }
        });

        loginBtn.addActionListener(e -> {
            String email = loginEmail.getText().trim();
            String pw = new String(loginPassword.getPassword());
            User found = null;
            for (User u : AppData.users) {
                if (u.getEmail().equalsIgnoreCase(email) && u.login(pw)) {
                    found = u;
                    break;
                }
            }
            // The Chief Event Coordinator logs in with the Singleton's fixed credentials
            ChiefEventCoordinator cec = ChiefEventCoordinator.getInstance();
            if (cec.getEmail().equalsIgnoreCase(email) && cec.login(pw)) {
                found = cec;
            }
            if (found != null) {
                AppData.currentUser = found;
                log("[Account] Logged in as " + email + " (" + found.getRoleName() + ")");
                refreshStatus();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        });

        logoutBtn.addActionListener(e -> {
            if (AppData.currentUser != null) {
                AppData.currentUser.logout();
                log("[Account] Logged out " + AppData.currentUser.getEmail());
            }
            AppData.currentUser = null;
            refreshStatus();
        });

        return panel;
    }

    // ---------------------------------------------------------------
    // BOOKING TAB: browse rooms, book, pay (Req3, Req4, Req7, Req10)
    // ---------------------------------------------------------------
    private JPanel buildBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        roomTableModel = new DefaultTableModel(new Object[]{"Room", "Building", "Capacity", "Status"}, 0);
        roomTable = new JTable(roomTableModel);
        refreshRoomTable();

        JButton bookBtn = new JButton("Book Selected Room (1 hour, starting now)");
        JButton refreshBtn = new JButton("Refresh");

        JPanel bottom = new JPanel();
        bottom.add(bookBtn);
        bottom.add(refreshBtn);

        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshRoomTable());

        bookBtn.addActionListener(e -> {
            if (AppData.currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please log in first.");
                return;
            }
            int row = roomTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a room first.");
                return;
            }
            Room room = AppData.rooms.get(row);
            if (!room.isAvailable()) {
                JOptionPane.showMessageDialog(this, "Room is not available (status: " + room.getStatus() + ").");
                return;
            }
            try {
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = start.plusHours(1);
                // Template Method in action: fixed algorithm, hooks vary by role
                Booking booking = AppData.currentUser.booking(room, start, end);
                AppData.bookings.add(booking);
                log("[Booking] Created " + booking.getBookingId() + " for room " + room.getRoomNumber()
                        + " | total=$" + booking.getTotal() + " deposit=$" + booking.getDeposit());

                // Observer in action: a badge scan at check-in notifies subscribers
                Sensor sensor = new Sensor("SEN-" + room.getRoomNumber(), room);
                sensor.addObserver(new CheckInManager());
                Badge badge = new Badge("BADGE-" + AppData.currentUser.getId());
                sensor.scanBadge(badge);
                booking.checkIn(badge);
                sensor.findOccupancy();
                sensor.sendData();

                promptPayment(booking);
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            refreshRoomTable();
        });

        return panel;
    }

    private void refreshRoomTable() {
        roomTableModel.setRowCount(0);
        for (Room r : AppData.rooms) {
            roomTableModel.addRow(new Object[]{r.getRoomNumber(), r.getBuilding(), r.getCapacity(), r.getStatus()});
        }
    }

    // Factory Method in action: user picks a payment type, factory builds the right Payment
    private void promptPayment(Booking booking) {
        String[] options = {"Credit/Debit Card", "Institutional Billing"};
        int choice = JOptionPane.showOptionDialog(this, "Pay $" + booking.getTotal() + " for booking "
                        + booking.getBookingId() + " using:", "Payment (Req10)",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        PaymentFactory factory;
        if (choice == 0) {
            String card = JOptionPane.showInputDialog(this, "Card number:", "4111111111111111");
            factory = new CreditDebitPaymentFactory(card == null ? "0000" : card, 123, "12/29");
        } else if (choice == 1) {
            String emp = JOptionPane.showInputDialog(this, "Employee number:", "EMP-1001");
            factory = new InstitutionalBillingPaymentFactory(emp == null ? "EMP-0000" : emp);
        } else {
            log("[Payment] Payment skipped for booking " + booking.getBookingId());
            return;
        }

        Payment payment = factory.processPayment(booking.getTotal());
        booking.setPayment(payment);
        boolean depositApplied = booking.depositBack();
        log("[Payment] Processed " + payment.getClass().getSimpleName() + " for booking "
                + booking.getBookingId() + " | deposit applied: " + depositApplied);
    }

    // ---------------------------------------------------------------
    // ADMIN TAB: add/enable/disable/close rooms (Req6), generate admins (Req2)
    // ---------------------------------------------------------------
    private JPanel buildAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JTextField roomNum = new JTextField(8);
        JTextField building = new JTextField(12);
        JTextField capacity = new JTextField(5);
        JButton addRoomBtn = new JButton("Add Room");
        JButton enableBtn = new JButton("Enable Selected");
        JButton disableBtn = new JButton("Disable Selected");
        JButton closeBtn = new JButton("Close (Maintenance)");

        JPanel formRow = new JPanel();
        formRow.add(new JLabel("Room#:")); formRow.add(roomNum);
        formRow.add(new JLabel("Building:")); formRow.add(building);
        formRow.add(new JLabel("Capacity:")); formRow.add(capacity);
        formRow.add(addRoomBtn);

        JPanel stateRow = new JPanel();
        stateRow.add(enableBtn); stateRow.add(disableBtn); stateRow.add(closeBtn);
        stateRow.add(new JLabel("  (select a room in Browse & Book tab first)"));

        JTextField newAdminEmail = new JTextField(18);
        JButton generateAdminBtn = new JButton("Generate Admin Account (Req2 - Singleton)");
        JPanel cecRow = new JPanel();
        cecRow.add(new JLabel("New admin's email:"));
        cecRow.add(newAdminEmail);
        cecRow.add(generateAdminBtn);

        JPanel top = new JPanel(new GridLayout(3, 1));
        top.add(formRow);
        top.add(stateRow);
        top.add(cecRow);

        JTextArea note = new JTextArea(
            "Tip: log in as the Chief Event Coordinator to generate admin accounts:\n"
          + "  email: coordinator@yorku.ca\n  password: InitialSetupPass1!\n\n"
          + "Room enable/disable/close acts on whichever room row is selected in the\n"
          + "'Browse & Book' tab (rooms and admin actions share the same room list)."
        );
        note.setEditable(false);
        note.setBackground(panel.getBackground());

        panel.add(top, BorderLayout.NORTH);
        panel.add(note, BorderLayout.CENTER);

        addRoomBtn.addActionListener(e -> {
            if (!requireAdmin()) return;
            try {
                Room r = new Room(roomNum.getText().trim(), building.getText().trim(),
                Integer.parseInt(capacity.getText().trim()), "AVAILABLE");

                ((Admin) AppData.currentUser).addRoom(r);
                AppData.rooms.add(r);
                log("[Admin] Room added: " + r.getRoomNumber());
                refreshRoomTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Capacity must be a number.");
            }
        });

        enableBtn.addActionListener(e -> {
            if (!requireAdmin()) return;
            withSelectedRoom(r -> { ((Admin) AppData.currentUser).enableRoom(r); log("[Admin] Enabled " + r.getRoomNumber()); });
        });
        disableBtn.addActionListener(e -> {
            if (!requireAdmin()) return;
            withSelectedRoom(r -> { ((Admin) AppData.currentUser).disableRoom(r); log("[Admin] Disabled " + r.getRoomNumber()); });
        });
        closeBtn.addActionListener(e -> {
            if (!requireAdmin()) return;
            withSelectedRoom(r -> { ((Admin) AppData.currentUser).closeRoom(r); log("[Admin] Closed " + r.getRoomNumber()); });
        });

        generateAdminBtn.addActionListener(e -> {
            ChiefEventCoordinator cec = ChiefEventCoordinator.getInstance();
            if (AppData.currentUser != cec) {
                JOptionPane.showMessageDialog(this,
                        "Only the Chief Event Coordinator can generate admin accounts.\n"
                        + "Log in as coordinator@yorku.ca / InitialSetupPass1! to demo this.");
                return;
            }
            String email = newAdminEmail.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter an email for the new admin.");
                return;
            }
            Admin newAdmin = cec.generateAdminAcc(email);
            AppData.users.add(newAdmin);
            log("[Singleton] ChiefEventCoordinator generated admin " + newAdmin.getAdminId() + " for " + email
                    + " (temp password logged to console)");
            JOptionPane.showMessageDialog(this, "Admin account created: " + newAdmin.getAdminId());
        });

        return panel;
    }

    private boolean requireAdmin() {
        if (!(AppData.currentUser instanceof Admin)) {
            JOptionPane.showMessageDialog(this, "Log in with an Admin (or Chief Event Coordinator) account first.");
            return false;
        }
        return true;
    }

    private interface RoomAction { void run(Room r); }

    private void withSelectedRoom(RoomAction action) {
        int row = roomTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a room in the Browse & Book tab first.");
            return;
        }
        action.run(AppData.rooms.get(row));
        refreshRoomTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConfrenceRoomGui().setVisible(true));
    }
}
