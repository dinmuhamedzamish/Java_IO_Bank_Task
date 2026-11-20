import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the first 3 digits of your bank account: ");
        String input = scanner.nextLine().trim();

        String digitsOnly = input.replaceAll("[^0-9]", "");
        if (digitsOnly.length() < 3) {
            System.out.println("Please enter at least 3 digits.");
            return;
        }

        String searchDigits;
        if (digitsOnly.length() == 26) {
            searchDigits = digitsOnly.substring(2, 5);
        } else {
            searchDigits = digitsOnly.substring(0, 3);
        }

        String urlString = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";
        InputStream inputStream;

        try {
            URL url = new URL(urlString);
            inputStream = url.openStream();
        } catch (IOException e) {
            File localFile = new File("plewibnra.txt");
            if (localFile.exists()) {
                try {
                    inputStream = new FileInputStream(localFile);
                } catch (IOException ex) {
                    System.out.println("Error reading file: " + ex.getMessage());
                    return;
                }
            } else {
                System.out.println("Could not download file: " + e.getMessage());
                return;
            }
        }

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "Cp852"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t");
                if (parts.length >= 2) {
                    String abbreviatedNumber = parts[0].trim();
                    String bankName = parts[1].trim();

                    if (abbreviatedNumber.equals(searchDigits)) {
                        System.out.println("Abbreviated bank number: " + abbreviatedNumber);
                        System.out.println("Bank name: " + bankName);
                        System.out.println("You have an account in bank: " + bankName);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("No bank found with code: " + searchDigits);
            }
        } catch (IOException e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
    }
}
