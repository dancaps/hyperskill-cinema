package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        int selection;
        int currentIncome = 0;

        // Creating the seating chart
        char[][] seats = createSeatChart();

        // The main menu
        do {
            // Menu input
            Scanner input = new Scanner(System.in);
            System.out.printf("%n1. Show the seats%n");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            selection = input.nextInt();
            System.out.println();

            // Calling methods based on the menu input
            switch (selection) {
                case 1:
                    printSeats(seats);
                    break;
                case 2:
                    // Everytime a seat is purchased the current income is increased
                    currentIncome += purchaseSeat(seats);
                    break;
                case 3:
                    getStatistics(seats, currentIncome, totalProfit(seats));
                    break;
            }
        } while (selection != 0);
    }

    public static void getStatistics(char[][] seats, int currentIncome, int totalIncome) {
        /*
         * Prints the number of purchased tickets, the percentage of tickets that have been purchased,
         * the current income from sales and the total income if all seats are sold.
         */

        int purchasedTickets = 0;
        float totalTickets = (seats.length - 1) * (seats[0].length - 1);

        // Counting and printing the purchased tickets
        for (char[] row : seats) {
            for (char seat : row) {
                if (seat == 'B') {
                    purchasedTickets++;
                }
            }
        }
        System.out.printf("Number of purchased tickets: %d%n", purchasedTickets);

        // Percentage of tickets purchased
        System.out.printf("Percentage: %.2f%%%n", purchasedTickets * 100 / totalTickets);

        // Prints current income
        System.out.printf("Current income: $%d%n", currentIncome);

        // Prints the total available income
        System.out.printf("Total income: $%d%n", totalIncome);
    }

    public static char[][] createSeatChart() {
        /*
         * Creates the seating chart while numbering the rows and columns so that there
         * is a visual representation of the grid.
         */

        // User inputs
        Scanner input = new Scanner(System.in);
        System.out.printf("%nEnter the number of rows:%n");
        int numOfRows = input.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int numOfSeatsPerRow = input.nextInt();

        /*
         * Creates a char array with the number of rows and columns. 1 is added to the size
         * to accommodate the extra row on top and column on the left.
         */
        char[][] seats = new char[numOfRows + 1][numOfSeatsPerRow + 1];

        // Loops through the rows
        for (int row = 0; row <= numOfRows; row++) {
            // Row 0 is the labels for the column numbers, so it needs to have numbers.
            if (row == 0) {
                // Loops though the elements in the first row and assigns numbers instead of seats.
                for (int seat = 0; seat <= numOfSeatsPerRow; seat++) {
                    // Annoyingly seat 0 is a blank space in the requirements.
                    if (seat == 0) {
                        seats[row][seat] = ' ';
                    } else { // All the other spaces in this row are numbers.
                        seats[row][seat] = Integer.toString(seat).charAt(0);
                    }
                }
            } else { // All other rows.
                // Populates the seats with S, except the 0 index is the row number.
                for (int seat = 0; seat <= numOfSeatsPerRow; seat++) {
                    // Adds the row number to the 0 index.
                    if (seat == 0) {
                        seats[row][seat] = Integer.toString(row).charAt(0);
                    } else {
                        // Adds S to all other spots except index 0.
                        seats[row][seat] = 'S';
                    }
                }
            }
        }
        return seats;
    }

    public static int purchaseSeat(char[][] seats) {
        /*
         * Prints the ticket price and marks the seat taken based on the seating chart and the coordinates
         * of the requested seat.
         */

        // Constants for the ticket prices and a variable to hold the results.
        final int pricePerSeatSmall = 10;
        final int pricePerSeatLGFront = 10;
        final int pricePerSeatLGBack = 8;
        int result;
        boolean availableSeat = false;
        int seatRow;
        int seatNumberInRow;

        do {
            // User inputs
            Scanner input = new Scanner(System.in);
            System.out.println("Enter a row number:");
            seatRow = input.nextInt();
            System.out.println("Enter a seat number in that row:");
            seatNumberInRow = input.nextInt();
            System.out.println();

            // Checking for incorrect input values
            if (seatRow < 1 || seatRow > seats.length - 1 ||
                seatNumberInRow < 1 || seatNumberInRow > seats[0].length - 1) {
                System.out.printf("Wrong input!%n%n");
            } else if (seats[seatRow][seatNumberInRow] == 'B') { // Checking to see if the seat is available
                System.out.printf("That ticket has already been purchased!%n%n");
            } else { // The seat is available, so now we break out of the loop
                availableSeat = true;
            }
        } while (!availableSeat);

        /*
         * If the number of seats are greater than 60 and there are an even number of
         * rows. -1 removes the row and column that holds the labels
         */
        if ((seats.length - 1) * (seats[0].length -1) > 60 && (seats.length - 1) % 2 == 0) {
            // If the purchased seat is in the first half of the rows charge more.
            if (seatRow < seats.length / 2) {
                result = pricePerSeatLGFront;
            } else { // If the seat is in the last half charge less.
                result = pricePerSeatLGBack;
            }
            /*
             * If the number of seats are greater than 60 and there are an odd number of
             * rows. -1 removes the row and column that holds the labels
             */
        } else if ((seats.length - 1) * (seats[0].length - 1) > 60 && (seats.length -1) % 2 == 1) {
            if (seatRow < seats.length / 2) { // Dividing by 2 rounds down so these are the more expensive seats.
                result = pricePerSeatLGFront;
            } else { // All the other rows are cheaper seats.
                result = pricePerSeatLGBack;
            }
            // If there are less than 60 seats all the seats are the same price.
        } else {
            result = pricePerSeatSmall;
        }

        // Marks the spot as taken. +1 is added because the seating chart uses spots to number the rows and columns.
        seats[seatRow][seatNumberInRow] = 'B';
        // Prints the ticket price.
        System.out.printf("Ticket price: $%d %n", result);
        // Updates the class variable containing the current income
        return result;
    }

    public static void printSeats(char[][] seats){
        /*
         * Prints the seating grid, which shows both the purchased and available seats.
         */
        System.out.println("Cinema:");
        for (char[] row : seats) {
            for (char seat : row) {
                System.out.print(seat + " ");
            }
            System.out.println();
        }
    }

    public static int totalProfit(char[][] seats) {
        /*
         * Calculates the total profit if all the seats are sold. The requirements state that
         * the first have of the rows are more expensive than the back half of the rows. If
         * there is an odd number of rows we need to round down. For example, 9 row means
         * the seats in first four rows are more expensive than seats in the last 5 rows.
         */

        // Defining the number of rows and seats
        int numOfRows = seats.length - 1;
        int numOfSeatsPerRow = seats[0].length - 1;

        // Constants for the seat prices
        final int pricePerSeatSmall = 10;
        final int pricePerSeatLGFront = 10;
        final int pricePerSeatLGBack = 8;

        // Other variables
        int result;
        int totalSeats = numOfRows * numOfSeatsPerRow;

        // The requirements state that less than 60 seats on the grid pay a single price for all seats.
        if (totalSeats <= 60) {
            result = totalSeats * pricePerSeatSmall;
        } else { // When there are more than 60 seats we need to split the price
            if (numOfRows % 2 == 0) { // Testing for even number of rows to calculate the total income
                result = ((totalSeats / 2) * pricePerSeatLGFront) + ((totalSeats / 2) * pricePerSeatLGBack);
            } else { // Dealing with odd rows differently to calculate the total income
                result = (((numOfRows / 2) * numOfSeatsPerRow) * pricePerSeatLGFront) +
                        (((numOfRows / 2 + 1) * numOfSeatsPerRow) * pricePerSeatLGBack);
            }
        }
        return result;
    }
}