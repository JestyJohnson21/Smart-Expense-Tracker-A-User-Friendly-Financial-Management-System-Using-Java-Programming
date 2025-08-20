import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.toedter.calendar.JDateChooser;
import java.util.List;
import java.util.ArrayList;


public class ExpenseTracker {
    private JFrame frame;
    private JTextField nameField, amountField;
    private JDateChooser dateChooser, startDateChooser, endDateChooser;
    private JTable table;
    private DefaultTableModel model;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public ExpenseTracker() {
        frame = new JFrame("Smart Expense Tracker");
        frame.setSize(750, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Expense"));
        
        inputPanel.add(new JLabel("Expense Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);
        
        inputPanel.add(new JLabel("Date:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setMaxSelectableDate(new Date()); // Restrict future dates
        inputPanel.add(dateChooser);
        
        JPanel btnPanel = new JPanel();
        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Expense");
        JButton statsButton = new JButton("Show Statistics");
        
        btnPanel.add(addButton);
        btnPanel.add(deleteButton);
        btnPanel.add(statsButton);
        
        String[] columnNames = {"Expense Name", "Amount", "Date"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);
        
        JPanel filterPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Expenses"));
        
        filterPanel.add(new JLabel("Start Date:"));
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        filterPanel.add(startDateChooser);
        
        filterPanel.add(new JLabel("End Date:"));
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        filterPanel.add(endDateChooser);
        
        JButton filterButton = new JButton("Filter");
        filterPanel.add(new JLabel());
        filterPanel.add(filterButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        bottomPanel.add(filterPanel, BorderLayout.SOUTH);
        
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(tableScroll, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        
        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        statsButton.addActionListener(e -> showStatistics());
        filterButton.addActionListener(e -> filterExpenses());
        
        setDarkTheme();
        frame.setVisible(true);
    }
    
    private void addExpense() {
        String name = nameField.getText();
        String amountText = amountField.getText();
        Date selectedDate = dateChooser.getDate();
        
        if (name.isEmpty() || amountText.isEmpty() || selectedDate == null) {
            JOptionPane.showMessageDialog(frame, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(frame, "Expense amount must be greater than zero!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String dateText = dateFormat.format(selectedDate);
            model.addRow(new Object[]{name, amount, dateText});
            nameField.setText("");
            amountField.setText("");
            dateChooser.setDate(null);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Enter a valid amount!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(frame, "Select an expense to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showStatistics() {
        double total = 0, highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;
        int rowCount = model.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(frame, "No expenses to calculate statistics!", "Statistics", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        HashMap<String, Double> dailyExpenses = new HashMap<>();
        
        for (int i = 0; i < rowCount; i++) {
            double amount = Double.parseDouble(model.getValueAt(i, 1).toString());
            String date = model.getValueAt(i, 2).toString();
            total += amount;
            dailyExpenses.put(date, dailyExpenses.getOrDefault(date, 0.0) + amount);
        }
        
        for (double expense : dailyExpenses.values()) {
            highest = Math.max(highest, expense);
            lowest = Math.min(lowest, expense);
        }
        
        double average = total / rowCount;
        JOptionPane.showMessageDialog(frame, "Total Expenses: " + total + "\nAverage Expense: " + average + "\nHighest Expense Day: " + highest + "\nLowest Expense Day: " + lowest, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void filterExpenses() {
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(frame, "Please select both start and end dates!", "Filter Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Object[]> filteredData = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                Date expenseDate = dateFormat.parse(model.getValueAt(i, 2).toString());
                if (!expenseDate.before(startDate) && !expenseDate.after(endDate)) {
                    filteredData.add(new Object[]{model.getValueAt(i, 0), model.getValueAt(i, 1), model.getValueAt(i, 2)});
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        model.setRowCount(0);
        for (Object[] row : filteredData) {
            model.addRow(row);
        }
    }
    
    private void setDarkTheme() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTracker::new);
    }
}
