package bali_coin.wallet;

import bali_coin.blockchain.MyFirstChain;
import bali_coin.transactions.Transaction;
import bali_coin.transactions.TransactionInput;
import bali_coin.transactions.TransactionOutput;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

    // What is used to sign our transactions - important to keep secure
    public PrivateKey privateKey;
    // This acts as the address for the wallet
    public PublicKey publicKey;

    // only UTXOs owned by this wallet.
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();


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

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : MyFirstChain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id, UTXO); //add it to our list of unspent transactions.
                total += UTXO.value;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey _recipient, float value) {
        if (getBalance() < value) { //gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }
}
