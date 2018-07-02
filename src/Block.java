import java.util.ArrayList;
import java.util.Date;

public class Block {
	
	public String hash, previousHash;
	private String data; //our data will be a simple message.
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long timeStamp; //as number of milliseconds since 1/1/1970.
	private int nonce;
	
	//Block Constructor.  
	public Block(String previousHash ) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.data = data;
		this.hash = calculateHash(); //Making sure we do this after we set the other values.
	}
	

	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
		return calculatedhash;
	}
	
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0');
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}

	public boolean addTransaction(Transaction transaction){

	    if(transaction == null) return false;
	    if(!previousHash.equals("0")) {
            if(!transaction.processTransaction()){
                System.out.println("Transaction cannot be processed");
                return false;
            }
        }
        transactions.add(transaction);
	    System.out.println("Transaction successfully added to Block");
	    return true;
    }
}
