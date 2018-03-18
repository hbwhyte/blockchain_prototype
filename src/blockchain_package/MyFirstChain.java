package blockchain_package;

public class MyFirstChain {

    public static void main(String[] args) {

        Block genesisBlock = new Block("Hello World!", "0");
        System.out.println("Hash for block 1: " + genesisBlock.hash);

        Block secondBlock = new Block("Ooh the first full block!", genesisBlock.hash);
        System.out.println("Hash for block 2: " + secondBlock.hash);

        Block thirdBlock = new Block("I'm number three, rounding it out", secondBlock.hash);
        System.out.println("Hash for block 3: " + thirdBlock.hash);

    }
}
