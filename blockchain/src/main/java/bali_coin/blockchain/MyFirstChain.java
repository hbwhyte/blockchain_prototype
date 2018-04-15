package bali_coin.blockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import bali_coin.transactions.Transaction;
import bali_coin.transactions.TransactionOutput;
import bali_coin.utilities.StringUtil;
import bali_coin.wallet.Wallet;
import com.google.gson.GsonBuilder;

public class MyFirstChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 5;
    public static float minimumTransaction = 0.1f;
    public static Wallet walletA;
    public static Wallet walletB;
    // List of all unspent transactions
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
    public static Transaction genesisTransaction;


    public static void main(String[] args) {
        // Setup Bouncey castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        // Create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        // Create genesis transaction, which sends 100 BaliCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        // Manually sign the genesis transaction
        genesisTransaction.generateSignature(coinbase.privateKey);
        // Manually set the transaction id
        genesisTransaction.transactionId = "0";
        // Manually add the Transactions Output
        genesisTransaction.outputs.add(new TransactionOutput
                        (genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId));
        // Store our first transaction in the UTXOs list.
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        isChainValid();

    }
//        //Test public and private keys
//        System.out.println("Private and public keys:");
//        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
//        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

//        //Create a test transaction from WalletA to walletB
//        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
//        transaction.generateSignature(walletA.privateKey);

//        //Verify the signature works and verify it from the public key
//        System.out.println("Is signature verified");
//        System.out.println(transaction.verifiySignature());

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

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
