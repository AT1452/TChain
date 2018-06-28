import java.util.ArrayList;
import com.google.gson.GsonBuilder;



public class TChain {
    public static int difficulty = 5;

    public static ArrayList<Block> blockchain = new ArrayList<Block>();


    public static void main(String[] args){

        blockchain.add(new Block("Hi im the first block", "0"));
        System.out.println("Attempting to mine block 1...");
        System.out.println("Yuh ");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Attempting to mine block 2...");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Attempting to mine block 3...");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid?: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe blockchain: ");
        System.out.println(blockchainJson);


    }

    public static Boolean isChainValid(){
        Block currentBlock, previousBlock;

        for(int i = 1; i < blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("Current hashes not equal!");
                return false;
            }
            if(!previousBlock.hash.equals(previousBlock.calculateHash())){
                System.out.println("Previous Hashes not equal!");
                return false;
            }

        }

        return true;
    }

}
