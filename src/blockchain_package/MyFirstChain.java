package blockchain_package;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class MyFirstChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {

        blockchain.add(new Block("Hello World!", "0"));
        blockchain.add(new Block("Ooh the first two-sided block!", blockchain.get(blockchain.size()-1).hash));
        blockchain.add(new Block("I\'m number three, rounding it out", blockchain.get(blockchain.size()-1).hash));

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }

    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        // Loop through the blocks to check hashes
        for (int i = 0; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())){
                return false;
            }
            if (!previousBlock.hash.equals(previousBlock.calculateHash())){
                return false;
            }

        }
        return true;
    }
}
