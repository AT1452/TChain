import java.security.*;
import java.util.ArrayList;


public class Transaction {

    public String transactionId;
    public  PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs){
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash(){
        sequence++;
        return StringUtil.applySha256(
                        StringUtil.getStringFromKey(sender) +
                              StringUtil.getStringFromKey(recipient) +
                              Float.toString(value) + sequence
                        );
    }

    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction(){
        if(!verifySignature()){
            System.out.println("Transaction signature failed to verify, ruh roh!");
            return false;
        }

        for (TransactionInput i : inputs){
            i.UTXO = TChain.UTXOs.get(i.transactionOutputId);
        }

        if(getInputsValue() < TChain.minimumTransaction){
            System.out.println("Transaction input is too small: " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        for(TransactionOutput o : outputs){
            TChain.UTXOs.put(o.id, o);
        }

        for(TransactionInput i : inputs){
            if(i.UTXO == null) continue;
            TChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputsValue(){
        float total = 0;
        for(TransactionInput i : inputs){
            if(i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    public float getOutputsValue(){
        float total = 0;
        for(TransactionOutput o : outputs){
            total += o.value;
        }
        return total;
    }



}
