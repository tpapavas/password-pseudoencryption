import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final int ENC_LENGTH = 32;

    private static final int NUM_OF_DIGITS = 10;
    private static final int NUM_OF_LETTERS = 26;

    private static final char[] colSymbols = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '_'
    };

    private static String [][] match;


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String password;
        String encPassword;

        match = initArray();


        do {
            System.out.print("Give your password: ");
            password = in.nextLine();
            if (password.length() > ENC_LENGTH || !password.matches("[0-9A-Za-z]+"))
                System.out.println("! Only letters and numbers please (max size 32) !");
        } while (password.length() > ENC_LENGTH || !password.matches("[0-9A-Za-z]+"));
//            System.out.println(password);

        encPassword = makeIt16(password);
        encPassword = cypherIt(encPassword);

        System.out.println("================================================================================");
        System.out.println("Password:\t" + password);
        System.out.println("Encrypted:\t" + encPassword);
    }

    private static String[][] initArray() {
        int length = colSymbols.length;
        String[][] symbs = new String[length][length];
        String[][] temps = new String[length][length];
        StringBuilder str;

        //initialize
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                str = new StringBuilder();
                str.append(colSymbols[i]).append(colSymbols[(j+i) % length]);
                symbs[i][j] = str.toString();
                temps[i][j] = symbs[i][j];
            }
        }

        //horizontal shift
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                str = new StringBuilder();
                str.append(temps[i][(j + i) % length]);
                symbs[i][j] = str.toString();
            }
        }

        //copy
        for (int i = 0; i < length; i++) {
            System.arraycopy(symbs[i], 0, temps[i], 0, length);
        }

        //vertical shift
        for (int j = 0; j < length; j++) {
            for (int i = 0; i < length; i++) {
                str = new StringBuilder();
                str.append(temps[(j+i)%length][j]);
                symbs[i][j] = str.toString();
            }
        }

        return symbs;
    }

    private static String cypherIt(String password) {
        StringBuilder pswd = new StringBuilder();

        int i = 0;
        for(char letter : password.toCharArray()) {
            pswd.append(match[getSymbolIndex(letter)][i++]);
        }

        return pswd.toString();
    }

    private static String makeIt16(String password) {
        int numOfExtraCs = ENC_LENGTH - password.length();
        int sizeOfSector = numOfExtraCs / password.length();
        StringBuilder str = new StringBuilder();

        int extras = 0;
        for (int i = 0; i < password.length(); i++) {
            str.append(password.charAt(i));
            for (int j = 0; j < sizeOfSector; j++) {
                str.append("`");
                extras++;
            }
        }

        str.append("`".repeat(Math.max(0, (numOfExtraCs - extras))));

        return str.toString();
    }

    private static int getSymbolIndex(char c) {
        if (Character.isDigit(c))
            return c - '0';
        else if (Character.isLowerCase(c))
            return c - 'a' + NUM_OF_DIGITS;
        else if (Character.isUpperCase(c))
            return c - 'A' + (NUM_OF_DIGITS+NUM_OF_LETTERS);
        else
            return NUM_OF_DIGITS + 2*NUM_OF_LETTERS;
    }
}
