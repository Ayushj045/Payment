import Model.Category;
import Model.Payment;
import Model.User;
import dto.PaymentDTO;
import dto.ReportDTO;
import repository.AuditTrailRepository;
import repository.PaymentRepository;
import repository.UserRepository;
import service.AuditService;
import service.PaymentService;
import service.ReportService;
import service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Setup repositories and services
        UserRepository userRepo = new UserRepository();
        PaymentRepository paymentRepo = new PaymentRepository();
        AuditTrailRepository auditRepo = new AuditTrailRepository();

        AuditService auditService = new AuditService(auditRepo);
        UserService userService = new UserService(userRepo);
        PaymentService paymentService = new PaymentService(paymentRepo, auditService);
        ReportService reportService = new ReportService(paymentRepo);

        // Seed one ADMIN user
        userService.registerUser("admin", "admin123", User.Role.ADMIN);
        userService.registerUser("ayush" , "ayush123", User.Role.VIEWER);
        userService.registerUser("john" , "john123", User.Role.FINANCE_MANAGER);

        Scanner scan = new Scanner(System.in);
        User user = null;
        while (true) {
            if (user == null) {
                System.out.print("Login\nUsername: ");
                String uname = scan.nextLine();
                System.out.print("Password: ");
                String pwd = scan.nextLine();
                try {
                    user = userService.login(uname, pwd);
                    System.out.println("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
                } catch (Exception e) {
                    System.out.println("Login failed. Try again.");
                    continue;
                }
            }
            System.out.println("\nMenu: 1.Add User 2.Add Payment 3.Update Status 4.Report 5.Monthly Report 6.Quarterly Report 7.Logout 8.Exit");
            int cmd = Integer.parseInt(scan.nextLine());
            if (cmd == 1) {
                if (user.getRole() != User.Role.ADMIN) {
                    System.out.println("Access denied. Only Admin can add users.");
                    continue;
                }
                try {
                    System.out.print("New Username: ");
                    String newU = scan.nextLine();
                    System.out.print("New Password: ");
                    String newP = scan.nextLine();
                    System.out.print("Role (ADMIN/FINANCE_MANAGER/VIEWER): ");
                    User.Role newR = User.Role.valueOf(scan.nextLine().toUpperCase());
                    userService.registerUser(newU, newP, newR);
                    System.out.println("User added.");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (cmd == 2) {
                if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.FINANCE_MANAGER) {
                    System.out.println("Access denied. Only Admin or Finance Manager can add payments.");
                    continue;
                }
                try {
                    PaymentDTO dto = new PaymentDTO();
                    System.out.print("Amount: ");
                    dto.amount = new BigDecimal(scan.nextLine());
                    System.out.print("Incoming? (true/false): ");
                    dto.incoming = Boolean.parseBoolean(scan.nextLine());
                    System.out.print("Category (SALARY/VENDOR_PAYMENT/CLIENT_INVOICE): ");
                    dto.category = Category.valueOf(scan.nextLine().toUpperCase());
                    Payment payment = paymentService.addPayment(dto, user);
                    System.out.println("Added: " + payment);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (cmd == 3) {
                if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.FINANCE_MANAGER) {
                    System.out.println("Access denied. Only Admin or Finance Manager can update status.");
                    continue;
                }
                try {
                    System.out.print("Payment ID: ");
                    Long pid = Long.parseLong(scan.nextLine());
                    System.out.print("New Status (PENDING/PROCESSING/COMPLETED): ");
                    Payment.Status newStatus = Payment.Status.valueOf(scan.nextLine().toUpperCase());
                    Payment payment = paymentService.updateStatus(pid, newStatus, user);
                    System.out.println("Updated: " + payment);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (cmd == 4) {
                try {
                    System.out.print("From date (yyyy-mm-dd): ");
                    LocalDateTime from = LocalDateTime.parse(scan.nextLine() + "T00:00:00");
                    System.out.print("To date (yyyy-mm-dd): ");
                    LocalDateTime to = LocalDateTime.parse(scan.nextLine() + "T23:59:59");
                    ReportDTO report = reportService.generateReport(from, to);
                    System.out.println("Report: Incoming=" + report.totalIncoming + " Outgoing=" + report.totalOutgoing);
                    report.payments.forEach(System.out::println);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (cmd == 5) {
                // Monthly report for current month
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime from = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime to = from.plusMonths(1).minusSeconds(1);
                ReportDTO report = reportService.generateReport(from, to);
                System.out.println("Monthly Report: Incoming=" + report.totalIncoming + " Outgoing=" + report.totalOutgoing);
                report.payments.forEach(System.out::println);
            } else if (cmd == 6) {
                // Quarterly report for current quarter
                LocalDateTime now = LocalDateTime.now();
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                int startMonth = (currentQuarter - 1) * 3 + 1;
                LocalDateTime from = LocalDateTime.of(now.getYear(), startMonth, 1, 0, 0, 0);
                LocalDateTime to = from.plusMonths(3).minusSeconds(1);
                ReportDTO report = reportService.generateReport(from, to);
                System.out.println("Quarterly Report: Incoming=" + report.totalIncoming + " Outgoing=" + report.totalOutgoing);
                report.payments.forEach(System.out::println);
            } else if (cmd == 7) {
                user = null;
                System.out.println("Logged out.");
            } else if (cmd == 8) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
        scan.close();
        System.out.println("Bye!");
    }

}