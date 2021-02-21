package Lab2;

public class Lab2 {
    private static void printEmployee(Employee emp) {
        System.out.println("Employee Information");
        System.out.println("--------------------");
        System.out.println("Name: " + emp.getFirstName() + " " + emp.getLastName());
        System.out.println("Birthdate: " + emp.getBirthdate());
        System.out.println("Title and Company: " + emp.getJobTitle() + " at " + emp.getCompany());
        System.out.println("ID: " + emp.getEmployeeID());
        System.out.println("Annual Salary: $" + emp.getAnnualSalary());
        System.out.println();
    }

    public static void main(String[] args) {
        Employee emp1 = new SalariedEmployee("Bill", "Gates", "October 28, 1955", 1, "Co-founder", "Microsoft", 11_500_000_000.00);
        Employee emp2 = new SalariedEmployee("Katie", "Bouman", "May 9, 1989", 2, "Professor", "Caltech", 200_000.00);
        Employee emp3 = new CommissionEmployee("Sammy", "Sales", "January 1, 1970", 3, "Salesperson", "Sales Company", 100_000.00, 3_000_000, 0.1);
        Employee emp4 = new HourlyEmployee("Harley", "Hourly", "December 31, 1971", 4, "Hourly Worker", "HoursPlus", 20, 40);
        printEmployee(emp1);
        printEmployee(emp2);
        printEmployee(emp3);
        printEmployee(emp4);
    }
}
