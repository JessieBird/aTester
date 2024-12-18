import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExpenseReportWithChart {
    public static void generateReport(JFrame parentFrame, ArrayList<String[]> records) {
        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No records to generate General Report!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 計算分類總金額和整體總金額
        HashMap<String, Double> categoryTotals = new HashMap<>();
        double totalExpense = 0;

        for (String[] record : records) {
            String category = record[2];
            double amount = Double.parseDouble(record[3]);

            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
            totalExpense += amount;
        }

        // 建立報表框架
        JFrame reportFrame = new JFrame("General Report with Time");
        reportFrame.setSize(700, 400);
        reportFrame.setLayout(new BorderLayout());

        // 建立報表內容，新增日期和時間列
        DefaultTableModel reportTableModel = new DefaultTableModel(new String[]{"日期", "時間", "分類", "金額", "百分比"}, 0);

        for (String[] record : records) {
            String date = record[0];
            String time = record[1];
            String category = record[2];
            double amount = Double.parseDouble(record[3]);
            double percentage = (categoryTotals.get(category) / totalExpense) * 100;

            // 加入每條記錄到表格中
            reportTableModel.addRow(new Object[]{date, time, category, String.format("%.2f", amount), String.format("%.2f%%", percentage)});
        }

        // 創建表格並包裹到 JScrollPane 中
        JTable reportTable = new JTable(reportTableModel);
        JScrollPane reportScrollPane = new JScrollPane(reportTable);

        // 將報表內容加入框架
        reportFrame.add(reportScrollPane, BorderLayout.CENTER);

        // 顯示報表框架
        reportFrame.setVisible(true);
    }
}

class ExpenseTracker {
    public static void main(String[] args) {
        // The list of record
        ArrayList<String[]> records = new ArrayList<>();

        // The main window
        JFrame frame = new JFrame("Bookeeeping Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // The era of above section
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Create a new record"));

        // Date Input
        JLabel dateLabel = new JLabel("Date(yyyy-MM-dd):");
        JTextField dateField = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);

        // Time Input
        JLabel timeLabel = new JLabel("Time:");
        JTextField timeField = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        inputPanel.add(timeLabel);
        inputPanel.add(timeField);

        //r or e
        JLabel revenueOrExpenseLabel = new JLabel("Revenue/Expense:");
        String[] revenueOrExpense = {"Revenue", "Expense"};
        JComboBox<String> revenueOrExpenseComboBox = new JComboBox<>(revenueOrExpense);
        inputPanel.add(revenueOrExpenseLabel);
        inputPanel.add(revenueOrExpenseComboBox);


        // Revenue/Category 選單容器
        JPanel dynamicPanel = new JPanel(new GridLayout(1, 4, 10, 10));

                // Revenue 選單
                JLabel moneyLabel = new JLabel("Revenue category:");
                String[] money = {"Salary", "Lottery", "Pocket money", "Investment profit", "Interest", "Others"};
                JComboBox<String> moneyComboBox = new JComboBox<>(money);

                // Category 選單
                JLabel categoryLabel = new JLabel("Expense category:");
                String[] categories = {"Food", "Grocery", "Transport", "Entertainment", "Shopping", "Medical", "Bill", "Education", "Others"};
                JComboBox<String> categoryComboBox = new JComboBox<>(categories);
                
                // 預設顯示 Revenue 選單
                dynamicPanel.add(moneyLabel);
                dynamicPanel.add(moneyComboBox);

                // 添加到 inputPanel
                inputPanel.add(dynamicPanel);
                

                // 根據 Revenue/Expense 選擇動態顯示對應的選單
                revenueOrExpenseComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 清空 dynamicPanel 的內容
                        dynamicPanel.removeAll();

                        // 根據選擇的值顯示對應的選單
                        if ("Revenue".equals(revenueOrExpenseComboBox.getSelectedItem())) {
                            dynamicPanel.add(moneyLabel);
                            dynamicPanel.add(moneyComboBox);
                        } else {
                            dynamicPanel.add(categoryLabel);
                            dynamicPanel.add(categoryComboBox);

                        }

                        // 重新繪製介面
                        dynamicPanel.revalidate();
                        dynamicPanel.repaint();
                    }
                });


        // Category choice
        JLabel categoryLabel = new JLabel("Category:");
        String[] categories = {"Food", "Clothes", "Entertainment"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);

        // Enter the Amount
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);

        // The area of the button
        JButton addButton = new JButton("Add new record");
        JButton reportButton = new JButton("Generate Report");
        inputPanel.add(addButton);
        inputPanel.add(reportButton);

        //The area of the below form
        String[] columnNames = {"Date", "Time", "Category", "Amount"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        //下方表格依據Revenue跟Expense顯示
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String time = timeField.getText();
                String revenueOrExpense = (String) revenueOrExpenseComboBox.getSelectedItem();
                String categoryOrMoney;
        
                // 根據 Revenue/Expense 選擇正確的值
                if ("Revenue".equals(revenueOrExpense)) {
                    categoryOrMoney = (String) moneyComboBox.getSelectedItem(); // 取得 Revenue 類別
                } else {
                    categoryOrMoney = (String) categoryComboBox.getSelectedItem(); // 取得 Expense 類別
                }
        
                String amount = amountField.getText();
        
                // 驗證金額是否正確
                try {
                    double parsedAmount = Double.parseDouble(amount);
        
                    // 建立記錄
                    String[] record = {date, time, revenueOrExpense, categoryOrMoney, String.format("%.2f", parsedAmount)};
                    records.add(record);
        
                    // 更新表格
                    tableModel.addRow(record);
        
                    // 清空輸入框
                    dateField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    timeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                    amountField.setText("");
        
                    // 恢復到 Revenue/Expense 預設選項
                    revenueOrExpenseComboBox.setSelectedIndex(0);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "請輸入有效的金額！", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // 將輸入區域與表格區域加入框架
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Add a button to add new record
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String time = timeField.getText();
                String category = (String) categoryComboBox.getSelectedItem();
                String amount = amountField.getText();

                // 驗證金額是否正確
                try {
                    double parsedAmount = Double.parseDouble(amount);
                    String[] record = {date, time, category, String.format("%.2f", parsedAmount)};
                    records.add(record);

                    // Update the form
                    tableModel.addRow(record);

                    // 清空輸入框
                    dateField.setText("");
                    timeField.setText("");
                    amountField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "請輸入有效的金額！", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Generate 按鈕的事件處理
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExpenseReportWithChart.generateReport(frame, records);
            }
        });

        // Time Filter Panel
        JPanel filterPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter by Time Range"));

        JLabel startDateLabel = new JLabel("Start Date:");
        JTextField startDateField = new JTextField(LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        filterPanel.add(startDateLabel);
        filterPanel.add(startDateField);

        JLabel endDateLabel = new JLabel("End Date:");
        JTextField endDateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        filterPanel.add(endDateLabel);
        filterPanel.add(endDateField);

        JButton filterButton = new JButton("Filter by Time");
        filterPanel.add(filterButton);

        frame.add(filterPanel, BorderLayout.SOUTH);

        // Filter button action listener
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                try {
                    LocalDate start = LocalDate.parse(startDate, formatter);
                    LocalDate end = LocalDate.parse(endDate, formatter);

                    // 重新過濾記錄
                    DefaultTableModel filterReportModel = new DefaultTableModel(new String[]{"日期", "分類", "金額", "百分比"}, 0);
                    HashMap<String, Double> filteredTotals = new HashMap<>();
                    double totalFilteredExpense = 0;

                    for (String[] record : records) {
                        LocalDate recordDate = LocalDate.parse(record[0], formatter);
                        if ((recordDate.isEqual(start) || recordDate.isAfter(start)) &&
                                (recordDate.isEqual(end) || recordDate.isBefore(end))) {

                            String category = record[2];
                            double amount = Double.parseDouble(record[3]);
                            filteredTotals.put(category, filteredTotals.getOrDefault(category, 0.0) + amount);
                            totalFilteredExpense += amount;

                            // 新增篩選記錄到表格
                            filterReportModel.addRow(new Object[]{record[0], category, String.format("%.2f", amount), ""});
                        }
                    }

                    // Caiculate the percentage and update forms
                    for (int i = 0; i < filterReportModel.getRowCount(); i++) {
                        String category = (String) filterReportModel.getValueAt(i, 1);
                        double total = filteredTotals.get(category);
                        double percentage = (total / totalFilteredExpense) * 100;
                        filterReportModel.setValueAt(String.format("%.2f%%", percentage), i, 3);
                    }

                    JTable filterReportTable = new JTable(filterReportModel);
                    JScrollPane filterReportScrollPane = new JScrollPane(filterReportTable);

                    JFrame filterReportFrame = new JFrame("Filtered Report");
                    filterReportFrame.setSize(500, 400);
                    filterReportFrame.add(filterReportScrollPane, BorderLayout.CENTER);
                    filterReportFrame.setVisible(true);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid date！", "Error Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }
}
