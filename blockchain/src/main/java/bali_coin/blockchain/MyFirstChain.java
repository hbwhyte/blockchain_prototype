package bali_coin.blockchain;

import java.security.Security;
import java.util.ArrayList;

import bali_coin.transactions.Transaction;
import bali_coin.utilities.StringUtil;
import bali_coin.wallet.Wallet;
import com.google.gson.GsonBuilder;

public class MyFirstChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {
        //Setup Bouncey castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();
        //Test public and private keys
        System.out.println("Private and public keys:");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
        //Create a test transaction from WalletA to walletB
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);
        //Verify the signature works and verify it from the public key
        System.out.println("Is signature verified");
        System.out.println(transaction.verifiySignature());

//        blockchain.add(new Block("Hello World!", "0"));
//        System.out.println("Trying to mine block 1...");
//        blockchain.get(0).mineBlock(difficulty);
//
//        blockchain.add(new Block("Ooh the first two-sided block!", blockchain.get(blockchain.size()-1).hash));
//        System.out.println("Trying to mine block 2...");
//        blockchain.get(1).mineBlock(difficulty);
//
//        blockchain.add(new Block("I\'m number three, rounding it out", blockchain.get(blockchain.size()-1).hash));
//        System.out.println("Trying to mine block 3...");
//        blockchain.get(2).mineBlock(difficulty);
//
//        System.out.println("\nBlockchain is valid: " + isChainValid());
//
//        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//        System.out.println("\nThe Blockchain:");
//        System.out.println(blockchainJson);
    }

    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0','0');

        // Loop through the blocks to check hashes
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // Checks if the current hash equals the calculated hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("Current hashes are not equal :(");
                return false;
            }
            // Checks if the previous hash equals the registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous hashes are not equal :(");
                return false;
            }
            // Checks if the hash has been solved
            if (!currentBlock.hash.substring(0,difficulty).equals(hashTarget)) {
                System.out.println("Block has not been mined yet :(");
                return false;
            }

        }
        return true;
    }
}
