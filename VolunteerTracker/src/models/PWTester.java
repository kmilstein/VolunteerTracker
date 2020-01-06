package models;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Ksenia
 */
public class PWTester {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "simple";
        for (int i = 0; i<10; i++) {
            System.out.printf("Password %d: %s%n", i, 
                    PasswordGenerator.getSHA512Password(password, PasswordGenerator.getSalt()));
        }
        
        //String password = "simple";
        byte[] salt = PasswordGenerator.getSalt();
        
        System.out.printf("password: %s\n", PasswordGenerator.getSHA512Password(password, salt));
        System.out.printf("password: %s\n", PasswordGenerator.getSHA512Password(password, salt));
    }
}
