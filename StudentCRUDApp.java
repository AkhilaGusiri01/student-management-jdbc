package project;

import java.sql.*;
import java.util.Scanner;

public class StudentCRUDApp {

    // Database credentials
    static final String URL = "jdbc:mysql://localhost:3306/cleanjdbc";
    static final String USER = "root";
    static final String PASSWORD = "Akhi@2001";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            int choice;
            do {
                System.out.println("\n=== Student Management System ===");
                System.out.println("1. Insert Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // clear buffer

                switch (choice) {
                    case 1 -> insertStudent();
                    case 2 -> viewStudents();
                    case 3 -> updateStudent();
                    case 4 -> deleteStudent();
                    case 5 -> System.out.println("Exiting... Goodbye!");
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            } while (choice != 5);

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
        }
    }

    // Insert Student
    public static void insertStudent() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine(); // clear buffer

        if (name.isBlank()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        String sql = "INSERT INTO students(name, age) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, age);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " student(s) inserted.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View Students
    public static void viewStudents() {
        String sql = "SELECT * FROM students";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Student List ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        " | Name: " + rs.getString("name") +
                        " | Age: " + rs.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Student
    public static void updateStudent() {
        System.out.print("Enter student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new age: ");
        int age = sc.nextInt();
        sc.nextLine();

        String sql = "UPDATE students SET name=?, age=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("No student found with that ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Student
    public static void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("No student found with that ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
