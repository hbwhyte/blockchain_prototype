package bali_coin;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {

    // What is used to sign our transactions - important to keep secure
    public PrivateKey privateKey;
    // This acts as the address for the wallet
    public PublicKey publicKey;

    // Constructor
    public Wallet() {
        generateKeyPair();
    }

    /**
     * Uses Java's KeyPairGenerator class to generate an Elliptic Curve Key Pair
     */
    public void generateKeyPair() {
        try {
            // ECDSA = Elliptic Curve Digital Signature Algorithm (NSA approved!)
            // BC = BouncyCastle
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            // Hashed Pseudo Random Number Generator
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // Parameters for generating the Elliptic Curve
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            // 256 bytes provides an acceptable security level for this
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
