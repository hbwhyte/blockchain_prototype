package bali_coin.transactions;

import bali_coin.utilities.StringUtil;

import java.security.*;
import java.util.ArrayList;

public class Transaction {

    // This is also the hash of the transaction
    public String transactionId; // this is also the hash of the transaction.
    // The public key / address of the sender
    public PublicKey sender;
    // The public key / address of the recipient
    public PublicKey recipient;
    // Value of the transaction
    public float value;
    // Signature to protect others from accessing funds in the wallet
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    // Rough count of how many transactions have been generated
    private static int sequence = 0;

    // Constructor
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // This calculates the transaction hash (which will be used as its ID)
    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        Float.toString(value) + sequence);
    }

    //Signs all the data we dont wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value)	;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }
    //Verifies the data we signed hasnt been tampered with
    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value)	;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}
