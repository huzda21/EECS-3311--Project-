package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConfrenceRoomGui extends JFrame {

    private JTextArea logArea;
    private JLabel statusLabel;
    private JTabbedPane tabs;
    private DefaultTableModel roomTableModel;
    private JTable roomTable;
    private DefaultTableModel myBookingsModel;
    private JTable myBookingsTable;

    public ConfrenceRoomGui() {
        super("YorkU Conference Room Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        statusLabel=new JLabel("Not logged in.");
        logArea=new JTextArea(8, 80);
        logArea.setEditable(false);

        tabs=new JTabbedPane();
        tabs.addTab("Account", buildAccountPanel());
        tabs.addTab("Browse & Book", buildBookingPanel());
        tabs.addTab("My Bookings", buildMyBookingsPanel());
        tabs.addTab("Admin Panel", buildAdminPanel());

        JPanel top=new JPanel(new BorderLayout());
        top.add(statusLabel, BorderLayout.WEST);
        top.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);
    }

    private void log(String msg) {
        logArea.append(msg+ "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void refreshStatus() {
        if (AppData.currentUser==null) {
            statusLabel.setText("Not logged in.");
        } else {
            statusLabel.setText("Logged in: "+AppData.currentUser.getEmail()
                    + " (" +AppData.currentUser.getRoleName()+ ")");
        }
    }


    private JPanel buildAccountPanel() {
        JPanel panel=new JPanel(new GridBagLayout());
        GridBagConstraints c=new GridBagConstraints();
        c.insets=new Insets(5, 5, 5, 5);
        c.fill=GridBagConstraints.HORIZONTAL;

        JTextField regEmail=new JTextField(18);
        JPasswordField regPassword=new JPasswordField(18);
        JComboBox<String> regType=new JComboBox<>(new String[]{"Student", "Faculty", "Staff", "Partner"});
        JTextField regNumber=new JTextField(18);
        JButton registerBtn=new JButton("Register");

        JTextField loginEmail=new JTextField(18);
        JPasswordField loginPassword=new JPasswordField(18);
        JButton loginBtn=new JButton("Login");
        JButton logoutBtn= new JButton("Logout");

        int row=0;
        c.gridx=0; c.gridy=row; panel.add(new JLabel("-- Register (Req1) --"), c);
        row++;
        c.gridx=0;c.gridy=row; panel.add(new JLabel("Email:"), c);
        c.gridx=1;c.gridy=row;panel.add(regEmail,c);
        row++;
        c.gridx=0;c.gridy=row;panel.add(new JLabel("Password:"), c);
        c.gridx=1;c.gridy=row;panel.add(regPassword, c);
        row++;
        c.gridx=0;c.gridy=row;panel.add(new JLabel("Account Type:"), c);
        c.gridx=1; c.gridy=row; panel.add(regType, c);
        row++;
        c.gridx=0;c.gridy=row; panel.add(new JLabel("Student#/Employee#/Staff#/Partner#:"), c);
        c.gridx=1;c.gridy=row; panel.add(regNumber, c);
        row++;
        c.gridx=1;c.gridy=row; panel.add(registerBtn, c);
        row++;

        c.gridx=0;c.gridy=row;panel.add(new JLabel("-- Login --"),c);
        row++;
        c.gridx=0;c.gridy=row;panel.add(new JLabel("Email:"),c);
        c.gridx=1;c.gridy=row; panel.add(loginEmail, c);
        row++;
        c.gridx=0;c.gridy=row;panel.add(new JLabel("Password:"), c);
        c.gridx=1;c.gridy=row; panel.add(loginPassword, c);
        row++;
        JPanel btnRow= new JPanel();
        btnRow.add(loginBtn);
        btnRow.add(logoutBtn);
        c.gridx=1;c.gridy=row;panel.add(btnRow, c);

        registerBtn.addActionListener(e -> {
            try {
                String email=regEmail.getText().trim();
                String pw=new String(regPassword.getPassword());
                long num=Long.parseLong(regNumber.getText().trim());
                
                for (User existing : AppData.users) {
                    if (existing.getEmail().equalsIgnoreCase(email)) {
                        JOptionPane.showMessageDialog(this, "An account with this email already exists.");
                        return;
                    }
                }
                if (!pw.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$")) {
                    JOptionPane.showMessageDialog(this,
                        "Password must be at least 8 characters and contain an uppercase letter, lowercase letter, number, and symbol.");
                    return;
                }
                String id = "ab-" + System.currentTimeMillis() % 100000;
                String accountType=(String)regType.getSelectedItem();
                boolean isVerified;
                if (accountType.equalsIgnoreCase("Student")||accountType.equalsIgnoreCase("Faculty")) {
                    isVerified=email.toLowerCase().endsWith("@yorku.ca");
                    if (!isVerified) {
                        JOptionPane.showMessageDialog(this, "University accounts must use a valid @yorku.ca email.");
                        return;
                    }
                } else {
                    isVerified = true;
                }
                User u = UserFactory.createUser(accountType, id, email, pw, isVerified, num);

                AppData.users.add(u);
                log("[Account] Registered"+u.getRoleName()+" account: " + email);
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

            ChiefEventCoordinator cec=ChiefEventCoordinator.getInstance();
            if (cec.getEmail().equalsIgnoreCase(email) && cec.login(pw)) {
                found = cec;
            }
            if (found!=null) {
                AppData.currentUser = found;
                log("[Account] Logged in as " + email + " (" + found.getRoleName() + ")");
                refreshStatus();
                refreshMyBookingsTable();
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
            refreshMyBookingsTable();
        });
        return panel;
    }

    private JPanel buildBookingPanel() {
        JPanel panel=new JPanel(new BorderLayout(5, 5));

        roomTableModel = new DefaultTableModel(new Object[]{"Room", "Building", "Capacity", "Status"}, 0);
        roomTable = new JTable(roomTableModel);
        refreshRoomTable();

        JButton bookBtn = new JButton("Book Selected Room");
        JButton refreshBtn = new JButton("Refresh");

        JPanel bottom = new JPanel();
        bottom.add(bookBtn);
        bottom.add(refreshBtn);

        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e->refreshRoomTable());

        bookBtn.addActionListener(e->{
            if (AppData.currentUser==null) {
                JOptionPane.showMessageDialog(this, "Please log in first.");
                return;
            }
            int row=roomTable.getSelectedRow();
            if (row==-1) {
                JOptionPane.showMessageDialog(this, "Select a room first.");
                return;
            }
            Room room=AppData.rooms.get(row);
            if (!room.isAvailable()) {
                JOptionPane.showMessageDialog(this,"Room is not available (status: " + room.getStatus() + ").");
                return;
            }

            Object[] durationOptions = {"0.5", "1", "2", "3", "4", "6", "8"};
            Object selected= JOptionPane.showInputDialog(this,"How many hours would you like to book " + room.getRoomNumber() + " for?","Booking Duration",JOptionPane.QUESTION_MESSAGE, null,durationOptions,"1");
            if(selected==null) return; 
            double hours=Double.parseDouble((String) selected);
         
            String date;

            while (true) {
                date = JOptionPane.showInputDialog(
                        this,
                        "Enter booking date (YYYY-MM-DD):");

                if (date == null) return; // user pressed Cancel

                date = date.trim();

                if (date.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Date cannot be empty.");
                    continue;
                }

                try {
                    java.time.LocalDate.parse(date);
                    break; // valid date
                } catch (java.time.format.DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a valid date in YYYY-MM-DD format.");
                }
            }
            
     
            String time;

            while (true) {
                time = JOptionPane.showInputDialog(
                        this,
                        "Enter start time (HH:MM, 24 hour):");

                if (time == null) return;

                time = time.trim();

                if (time.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Time cannot be empty.");
                    continue;
                }

                try {
                    java.time.LocalTime.parse(time);
                    break; // valid time
                } catch (java.time.format.DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a valid time in HH:MM format.");
                }
            }

          
            try {
            	LocalDateTime start =
            	        LocalDateTime.parse(date + "T" + time);
            	
            	if(start.isBefore(LocalDateTime.now())) {
            	    JOptionPane.showMessageDialog(
            	        this,
            	        "Booking must start in the future.");
            	    return;
            	}

            	LocalDateTime end =
            	        start.plusMinutes(Math.round(hours * 60));
            	
            	if (!Booking.roomAvailable(room, start, end)) {
            	    JOptionPane.showMessageDialog(this,
            	            "That room is already booked during that time.");
            	    return;
            	}
           
                Booking booking=AppData.currentUser.booking(room, start, end);
                
                
                /*Sensor sensor = new Sensor("sens-" + room.getRoomNumber(), room);
                sensor.addObserver(room);
                Badge badge = new Badge("bad-" + AppData.currentUser.getId());
                sensor.scanBadge(badge);
                booking.checkIn(badge);
                sensor.findOccupancy();
                sensor.sendData(); */

                promptPayment(booking);

            
            } 
            catch (java.time.format.DateTimeParseException ex) 
            {
            	JOptionPane.showMessageDialog(this, "Please enter the date as YYYY-MM-DD and the time as HH:MM.");
            } 
            catch (IllegalStateException ex) 
            {
            	JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            refreshRoomTable();
            refreshMyBookingsTable();
        });
        return panel;
    }
    private void refreshRoomTable() {
        roomTableModel.setRowCount(0);
        for (Room r : AppData.rooms) {
            roomTableModel.addRow(new Object[]{r.getRoomNumber(), r.getBuilding(), r.getCapacity(), r.getStatus()});
        }
    }
    private JPanel buildMyBookingsPanel() {
        JPanel panel=new JPanel(new BorderLayout(5,5));
        myBookingsModel = new DefaultTableModel(
                new Object[]{"Booking ID","Room","Start","End","Status","Total"}, 0);
        myBookingsTable = new JTable(myBookingsModel);
        refreshMyBookingsTable();

        JButton extendBtn=new JButton("Extend Selected Booking");
        JButton cancelBtn=new JButton("Cancel Selected Booking");
        JButton refreshBtn=new JButton("Refresh");
        JButton checkInBtn = new JButton("Check In");

        JPanel bottom= new JPanel();
        bottom.add(checkInBtn);
        bottom.add(extendBtn);
        bottom.add(cancelBtn);
        bottom.add(refreshBtn);
        

        panel.add(new JScrollPane(myBookingsTable),BorderLayout.CENTER);
        panel.add(bottom,BorderLayout.SOUTH);

        refreshBtn.addActionListener(e->refreshMyBookingsTable());

        extendBtn.addActionListener(e-> {
            Booking booking=selectedMyBooking();
            if (booking==null) return;

            Object[] durationOptions={"0.5", "1", "2", "3", "4"};
            Object selected = JOptionPane.showInputDialog(this,"Extend booking " + booking.getBookingId() + " by how many hours?","Extend Booking",JOptionPane.QUESTION_MESSAGE,null,durationOptions,"1");
            if (selected==null) return;
            double hours=Double.parseDouble((String) selected);
            
            
            
            LocalDateTime newEnd=booking.getEndTime().plusMinutes(Math.round(hours * 60));
            boolean success=booking.extendBooking(newEnd);
            if(success) {
                log("[Booking] Extended "+booking.getBookingId() +" by "+hours+"h, new end "+ booking.getEndTime()+ ", new total=$"+ booking.getTotal());
            } else{
                JOptionPane.showMessageDialog(this,"Couldn't extend that booking - it may have already ended, the room may be "+ "closed/disabled, or someone else already has the room booked during that extra time.");
            }
            refreshMyBookingsTable();
        });

        cancelBtn.addActionListener(e-> {
            Booking booking=selectedMyBooking();
            if (booking==null) return;

            boolean success=booking.cancelBooking();
            if(success){
                log("[Booking] Cancelled "+booking.getBookingId());
            }else{
                JOptionPane.showMessageDialog(this, "Can't cancel - this booking has already started.");
            }
            refreshMyBookingsTable();
        });

        
        checkInBtn.addActionListener(e -> {

            Booking booking = selectedMyBooking();

            if (booking == null)
                return;

            Badge badge = new Badge("bad-" + AppData.currentUser.getId());
            Sensor sensor = new Sensor(
                    "sens-" + booking.getRoom().getRoomNumber(),
                    booking.getRoom());

            sensor.addObserver(booking.getRoom());
            sensor.scanBadge(badge);

            if (booking.checkIn(badge)) {
                sensor.findOccupancy();
                sensor.sendData();
                
                booking.depositBack();                
                JOptionPane.showMessageDialog(this,
                        "Checked in successfully!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cannot check in at this time.");
            }

            refreshMyBookingsTable();

        });
        
        return panel;
    }

    private void refreshMyBookingsTable() {
        myBookingsModel.setRowCount(0);
        for (Booking b : myBookingsForCurrentUser()) {
            myBookingsModel.addRow(new Object[]{
                    b.getBookingId(), b.getRoom().getRoomNumber(),
                    b.getStartTime(), b.getEndTime(), b.getStatus(), b.getTotal()
            });
            
        }
    }

    private List<Booking> myBookingsForCurrentUser() {
        List<Booking> mine = new ArrayList<>();
        if (AppData.currentUser == null) return mine;

        for (Booking b : AppData.bookings) {
            if (b.getBookedBy() == AppData.currentUser
                    && !b.getStatus().equals("CANCELLED")) {
                mine.add(b);
            }
        }
        return mine;
    }

    private Booking selectedMyBooking() {
        if (AppData.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first.");
            return null;
        }
        int row = myBookingsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a booking first.");
            return null;
        }
        return myBookingsForCurrentUser().get(row);
    }

    private void promptPayment(Booking booking) {

        String[] options = {"Credit/Debit Card", "Institutional Billing"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "Pay deposit of $" + booking.getDeposit() + " for booking " + booking.getBookingId(),
                "Payment",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        PaymentStrategy strategy;
        Payment payment;

        if (choice == 0) {

        	String card;

        	while (true) {
        	    card = JOptionPane.showInputDialog(this, "Enter card number:");

        	    if (card == null) return;

        	    card = card.replaceAll("\\s+", "");
        	    if (card.matches("\\d{13,19}")) {
        	        break;
        	    }
        	    JOptionPane.showMessageDialog(this,
        	            "Card number must be between 13 and 19 digits.");
        	}

        	String expiry;

        	while (true) {
        	    expiry = JOptionPane.showInputDialog(this,
        	            "Enter expiry date (MM/YY):");

        	    if (expiry == null) return;
        	    if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
        	        JOptionPane.showMessageDialog(this,
        	                "Expiry must be in MM/YY format.");
        	        continue;
        	    }
        	    String[] parts = expiry.split("/");
        	    int month = Integer.parseInt(parts[0]);
        	    int year = 2000 + Integer.parseInt(parts[1]);

        	    java.time.YearMonth expiryDate =
        	            java.time.YearMonth.of(year, month);

        	    if (expiryDate.isBefore(java.time.YearMonth.now())) {
        	        JOptionPane.showMessageDialog(this,
        	                "This card has expired.");
        	        continue;
        	    }
        	    break;
        	}

            String cvc;

            while (true) {
                cvc = JOptionPane.showInputDialog(this, "Enter CVC:");
                if (cvc == null) return;
                if (cvc.matches("\\d{3,4}")) {
                    break;
                }
                JOptionPane.showMessageDialog(this,
                        "CVC must be 3 or 4 digits.");
            }
            payment = new CreditDebit(
                    booking.getDeposit(),
                    card,
                    Integer.parseInt(cvc),
                    expiry);

        } else if (choice == 1) {

            String emp = JOptionPane.showInputDialog(this,
                    "Employee number:", "EMP-1001");

            payment = new InstitutionalBilling(
                    booking.getDeposit(),
                    emp == null ? "EMP-0000" : emp);

        } else {
            log("[Payment] Payment skipped.");
            return;
        }

        strategy = payment;

        strategy.pay();

        booking.setPayment(payment);
        AppData.bookings.add(booking);
        log("[Booking] Created "
                + booking.getBookingId()
                + " for room "
                + booking.getRoom().getRoomNumber()
                + " | total=$"
                + booking.getTotal()
                + " | deposit=$"
                + booking.getDeposit());
      
        log("[Payment] Processed "
                + payment.getClass().getSimpleName());    
    }
    

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
          + "  email: coordinator@yorku.ca\n  password: mycoordPassword@12\n\n"
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
                        + "Log in as coordinator@yorku.ca / mycoordPassword@12 to demo this.");
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
