# Smart-Expense-Tracker-A-User-Friendly-Financial-Management-System-Using-Java-Programming
Smart Expense Tracker – Project Description

The Smart Expense Tracker is a Java Swing-based desktop application designed to simplify personal finance management through an intuitive, user-friendly interface. Built with a structured layout using BorderLayout, the application organizes its components into three sections: input fields at the top, an expense table in the center, and action buttons at the bottom.

Expense Entry & Management:
Users can enter expense details including name, amount, and date. The date is selected via a calendar widget (JDateChooser) with restrictions to prevent future dates. Each expense entry undergoes strict validation to ensure fields are not left empty, the amount entered is numeric and positive, and dates are valid. Expenses are dynamically displayed in a JTable, allowing users to easily view, add, and delete records. Deletion requires explicit row selection to avoid accidental removals.

Filtering & Statistics:
The app provides a filtering system where expenses can be displayed for a chosen date range using start and end date selectors. In addition, it includes a statistics module that calculates total, average, highest, and lowest expenses per day. These insights help users analyze spending patterns effectively. If statistics are requested without any records, the app displays informative messages to avoid errors.

UI & Usability Features:
A modern look is achieved through the Nimbus dark theme, enhancing readability and user experience. The interface is structured for easy navigation, with clearly separated panels for input, records, and controls. The table updates dynamically with each action, giving users real-time visibility of their financial data.

Error Handling & Robustness:
Robust validation and exception handling ensure reliability. Error messages guide users when fields are incomplete, invalid numbers are entered, or dates are improperly selected. Filtering requires both start and end dates, and exceptions such as parsing errors are managed gracefully through try-catch blocks. This ensures smooth execution without crashes.

Technical Design:
All actions—adding, deleting, filtering, and viewing statistics—are event-driven using ActionListeners. The program ensures thread-safe execution by running GUI updates on the Event Dispatch Thread (EDT) with SwingUtilities.invokeLater().

In summary, the Smart Expense Tracker combines usability, validation, and error prevention to provide a reliable, secure, and efficient personal finance management tool that empowers users to track and analyze expenses with ease.
