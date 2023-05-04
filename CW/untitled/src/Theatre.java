import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class Theatre {
    private static final ArrayList<Ticket> tickets=new ArrayList<>();//ArrayList to store tickets

    private static final int[][] seats = new int[3][];

    public static void main(String[] args) {
        // Initialize seat arrays
        seats[0] = new int[12];
        seats[1] = new int[16];
        seats[2] = new int[20];

        System.out.println("Welcome to the New Theatre");

        Scanner scanner = new Scanner(System.in);
        //main menu print
        String option;
        do {
            System.out.println("-------------------------------------------------------------");
            System.out.println("Please select an option:");
            System.out.println("1) Buy a ticket");
            System.out.println("2) Print seating area");
            System.out.println("3) Cancel ticket");
            System.out.println("4) List available seats");
            System.out.println("5) Save to file");
            System.out.println("6) Load from file");
            System.out.println("7) Print ticket information and total price");
            System.out.println("8) Sort tickets by price");
            System.out.println("     0) Quit");
            System.out.println("--------------------------------------------------------------");
            System.out.print("Enter Option : ");
            // Create scanner object for user input
            option = scanner.next();
            // Use switch statement to perform actions based on option selected
            switch (option) {
                case "1" -> buyTicket();
                case "2" -> printSeatingArea();
                case "3" -> cancelticket();
                case "4" -> showavailable();
                case "5" -> Theatre.save();
                case "6" -> load();
                case "7" -> show_tickets_info();
                case "8" -> sort_tickets();
                case "0" -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid option. Please try again.");
            }
        } while (!option.equals("0"));
            scanner.close();
    }

    public static void buyTicket() {
        Scanner scanner = new Scanner(System.in);
        //getting name
        System.out.println("Enter your name:");
        String name = scanner.next();
        //validation name input
        while (!name.matches("[a-zA-Z. ,]+")) {
            System.out.println("Please retype your name:");
            name = scanner.next();
        }
        //getting surname
        System.out.println("Enter your surname:");
        String surname = scanner.next();
        //validation surname
        while (!surname.matches("[a-zA-Z ,]+")) {
            System.out.println("Please retype your surname:");
            surname = scanner.next();
        }
        //getting email
        System.out.println("Enter your email:");
        String email = scanner.next();
        while (!email.matches("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
                + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$")) {
            System.out.println("Please retype your email:");
            email = scanner.next();
        }
        Person person = new Person(name, surname, email);
        //getting price and validation
        double price = 0.0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Please enter the price of the ticket:");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.next(); // consume the invalid input to prevent an infinite loop
            }
        }
        //getting row number
        int row;
        try {
            System.out.print("Enter row number (1-3): ");
            row = scanner.nextInt() - 1;
        } catch (Exception ex) {
            System.out.println("Invalid input. Please enter a valid row number");
            return;
        }
        //check row number
        if (row < 0 || row > 2) {
            System.out.println("Invalid row number. Please try again.");
            return;
        }
        int[] rowSeats = seats[row];
        //getting seat number
        int seat;
        try {
            System.out.print("Enter seat number (1-" + rowSeats.length + "): ");
            seat = scanner.nextInt() - 1;
        } catch (Exception ex) {
            System.out.println("Invalid input. Please enter a valid seat number");
            return;
        }
        // add new ticket to ArrayList
        Ticket ticket = new Ticket(row, seat, price, person);
        tickets.add(ticket);
        if (seat < 0 || seat >= rowSeats.length) {
            System.out.println("Invalid seat number. Please try again.");
            return;
        }
        //check availability of seat
        if (rowSeats[seat] == 1) {
            System.out.println("Seat already sold. Please try again.");
            return;
        }

        rowSeats[seat] = 1;

        System.out.println("Ticket bought successfully.");
    }


    public static void printSeatingArea() {
        System.out.printf("%10s%-10s\n", "*****", "*****");
        System.out.printf("%10s%-10s\n", "*STA", "GE*");
        System.out.printf("%10s%-10s\n", "*****", "*****");
        //formatting the seating area
        for (int i = 0; i < seats.length; i++) {
            if (seats[i] == seats[0]) {
                System.out.print("    ");
            }
            if (seats[i] == seats[1]) {
                System.out.print("  ");
            }
            int[] rowSeats = seats[i];

            for (int j = 0; j < rowSeats.length; j++) {
                if (j == rowSeats.length / 2 - 1) {
                    System.out.print(" ");
                } else if (rowSeats[j] == 0) {
                    System.out.print("O");
                } else {
                    System.out.print("X");
                }

                // Add space between rows
                if (j == rowSeats.length - 1) {
                    System.out.println();
                }
            }
        }
    }

    public static void cancelticket() {
        // prompt user for row and seat number of ticket to cancel
        Scanner scanner = new Scanner(System.in);
        int rowNum;
        try {
            System.out.println("Enter the row number of the ticket you want to cancel:");
             rowNum = scanner.nextInt();
        }catch(Exception e){
            System.out.println("Invalid input. Please enter a valid row number");
            return;
        }
        int seatNum;
        try{
        System.out.println("Enter the seat number of the ticket you want to cancel:");
        seatNum = scanner.nextInt();
        }catch(Exception e) {
            System.out.println("Invalid input. Please enter a valid seat number");
            return;
        }
        for (Ticket ticket : tickets) {
            if (ticket.getRow() == rowNum && ticket.getSeat() == seatNum) {
                tickets.remove(ticket); // remove ticket from ArrayList
                seats[rowNum-1][seatNum-1]=0; // mark seat as available
                System.out.println("Ticket cancelled!");
                return;
            }
        }
        System.out.println("No ticket found at row " + rowNum + ", seat " + seatNum + ".");
    }

    public static void showavailable() {
        for (int i = 0; i < 3; i++) {//check available seats
            int row_num = i + 1;
            System.out.printf("Seats available in row %d: ", row_num);
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    System.out.print(j + 1 + " ");
                }
            }
            System.out.println();
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter("seating.txt");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < seats[i].length; j++) {
                    writer.write(seats[i][j] + " ");
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("Seating information saved.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the seating information.");
            e.printStackTrace();
        }
    }

    private static void load() {
        // Read data
            try {
                File file = new File("seating.txt");
                Scanner scanner = new Scanner(file);
                int row = 0;
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split(" ");
                    for (int col = 0; col < line.length; col++) {
                        seats[row][col] = Integer.parseInt(line[col]);
                    }
                    row++;
                }
                scanner.close();
                System.out.println("Seating information loaded.");
            } catch (FileNotFoundException e) {
                System.out.println("The seating information file was not found.");
                e.printStackTrace();
            }

    }

    public static void show_tickets_info() {
        double total = 0;

        System.out.println("\n---------- TICKETS INFO ----------");
        for (Ticket t : tickets) {//print tickets
            System.out.println("Ticket " + (tickets.indexOf(t) + 1) + " info:");
            t.print();
            total += t.getPrice();//calculate tickets price
            System.out.println("----------------------------------");
        }
        System.out.println("Total price for all tickets: £" + total);
    }

    public static ArrayList<Ticket> sort_tickets() {
        ArrayList<Ticket> sortedTickets = new ArrayList<Ticket>(tickets);
        Collections.sort(sortedTickets, new Comparator<Ticket>() {
            public int compare(Ticket t1, Ticket t2) {//compare ticket
                return Double.compare(t1.getPrice(), t2.getPrice());
            }
        });

        System.out.println("Tickets sorted by price (cheapest first):");
        for (Ticket t : sortedTickets) {
            t.print();
            System.out.println("----------------------------------");
        }

        return sortedTickets;
    }
}





