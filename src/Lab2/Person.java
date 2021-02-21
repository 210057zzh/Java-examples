package Lab2;

class Person {
    protected String firstName;
    protected String lastName;
    protected String birthdate;
}

abstract class Employee extends Person {
    protected int employeeID;
    protected String jobTitle;
    protected String company;

    protected Employee(String firstName, String lastName, String birthDate, int id, String jobTitle, String company) {
        super();
        this.firstName = firstName;
        this.employeeID = id;
        this.company = company;
        this.jobTitle = jobTitle;
        this.birthdate = birthDate;
        this.lastName = lastName;
    }

    public abstract double getAnnualSalary();

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getBirthdate();

    public abstract String getJobTitle();

    public abstract String getCompany();

    public abstract String getEmployeeID();
}

class HourlyEmployee extends Employee {
    protected int hourlyRate;
    protected int numberHoursPerWeek;

    public HourlyEmployee(String firstName, String lastName, String birthDate, int id, String jobTitle, String company, int hourlyRate, int numberHoursPerWeek) {
        super(firstName, lastName, birthDate, id, jobTitle, company);
        this.hourlyRate = hourlyRate;
        this.numberHoursPerWeek = numberHoursPerWeek;
    }

    @Override
    public double getAnnualSalary() {
        return 52 * hourlyRate * numberHoursPerWeek;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getBirthdate() {
        return birthdate;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public String getEmployeeID() {
        return Integer.toString(employeeID);
    }
}

class SalariedEmployee extends Employee {
    protected double annualSalary;

    public SalariedEmployee(String bill, String gates, String s, int i, String s1, String microsoft, double v) {
        super(bill, gates, s, i, s1, microsoft);
        this.annualSalary = v;
    }

    @Override
    public double getAnnualSalary() {
        return annualSalary;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getBirthdate() {
        return birthdate;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public String getCompany() {
        return company;
    }

    @Override
    public String getEmployeeID() {
        return Integer.toString(employeeID);
    }
}

class CommissionEmployee extends SalariedEmployee {
    protected int salesTotal;
    protected double commissionPercentage;

    public CommissionEmployee(String sammy, String sales, String s, int i, String salesperson, String sales_company, double v, int i1, double v1) {
        super(sammy, sales, s, i, salesperson, sales_company, v);
        this.salesTotal = i1;
        this.commissionPercentage = v1;
    }

    @Override
    public double getAnnualSalary() {
        return annualSalary + salesTotal * commissionPercentage;
    }
}
