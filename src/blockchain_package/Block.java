package blockchain_package;

import java.util.Date;

public class Block {

    public String hash;
    public String previousHash;
    private String data; // Our simple message to be transferred
    private long timeStamp; // As milliseconds since 1/1/1970

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); // Last item, only after the previous variables are set
    }

    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        data
        );
        return calculatedHash;
    }

}
