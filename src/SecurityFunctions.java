// Some code here from
// http://exampledepot.8waytrips.com/egs/javax.crypto/KeyAgree.html

import javax.crypto.KeyAgreement;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class SecurityFunctions {

    PrivateKey privateKey;
    PublicKey publicKey;
    int[] secretKey;

    public SecurityFunctions() {
        try {
            // Use the values to generate a key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            KeyPair keypair = keyGen.generateKeyPair();

            // Get the generated public and private keys
            this.privateKey = keypair.getPrivate();
            this.publicKey = keypair.getPublic();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
    }

    public static native int[] encrypt(int[] value, int[] key);
    public static native int[] decrypt(int[] value, int[] key);

    public byte[] encrypt(byte[] byteArray) {
        // to ints
        IntBuffer intBuf = ByteBuffer.wrap(byteArray)
                .order(ByteOrder.BIG_ENDIAN)
                .asIntBuffer();
        int[] arr = new int[intBuf.remaining()];

        for (int i = 0; i < arr.length / 8; i+=8) {
            encrypt(Arrays.copyOfRange(arr, i, i+8), this.secretKey);
        }

        // to bytes
        ByteBuffer byteBuffer = ByteBuffer.allocate(arr.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(arr);
        byte[] byteArrayOut = byteBuffer.array();

        return byteArrayOut;
    }

    public byte[] decrypt(byte[] byteArray) {
        // to ints
        IntBuffer intBuf = ByteBuffer.wrap(byteArray)
                .order(ByteOrder.BIG_ENDIAN)
                .asIntBuffer();
        int[] arr = new int[intBuf.remaining()];

        for (int i = 0; i < arr.length / 8; i+=8) {
            decrypt(Arrays.copyOfRange(arr, i, i+8), this.secretKey);
        }

        // to bytes
        ByteBuffer byteBuffer = ByteBuffer.allocate(arr.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(arr);
        byte[] byteArrayOut = byteBuffer.array();

        return byteArrayOut;
    }

    public byte[] getPublicKey() {
        return this.publicKey.getEncoded();
    }

    public void makeSecretKey(byte[] othersPublicKeyBytes) {
        try {
            // Convert the public key bytes into a PublicKey object
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(othersPublicKeyBytes);
            KeyFactory keyFact = KeyFactory.getInstance("DH");
            publicKey = keyFact.generatePublic(x509KeySpec);

            // Prepare to generate the secret key with the private key and public key of the other party
            KeyAgreement ka = KeyAgreement.getInstance("DH");
            ka.init(privateKey);
            ka.doPhase(publicKey, true);

            // Specify the type of key to generate;
            // see Listing All Available Symmetric Key Generators
            String algorithm = "DES";

            // Generate the secret key
            IntBuffer intBuf = ByteBuffer.wrap(ka.generateSecret())
                    .order(ByteOrder.BIG_ENDIAN)
                    .asIntBuffer();
            this.secretKey = new int[intBuf.remaining()];

            // Use the secret key to encrypt/decrypt data;
            // see Encrypting a String with DES
        } catch (java.security.InvalidKeyException e) {
        } catch (java.security.spec.InvalidKeySpecException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        }
    }

    /* public static SecretKey makeKey() {
        //http://exampledepot.8waytrips.com/egs/javax.crypto/KeyAgree.html

        Retrieve the prime, base, and private value for generating the key pair.
         If the values are encoded as in
         Generating a Parameter Set for the Diffie-Hellman Key Agreement Algorithm,
         the following code will extract the values.
        String[] values = valuesInStr.split(",");
        BigInteger p = new BigInteger(values[0]);
        BigInteger g = new BigInteger(values[1]);
        int l = Integer.parseInt(values[2]);

        try {
            // Use the values to generate a key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            DHParameterSpec dhSpec = new DHParameterSpec(p, g, l);
            keyGen.initialize(dhSpec);
            KeyPair keypair = keyGen.generateKeyPair();

            // Get the generated public and private keys
            PrivateKey privateKey = keypair.getPrivate();
            PublicKey publicKey = keypair.getPublic();

            // Send the public key bytes to the other party...
            byte[] publicKeyBytes = publicKey.getEncoded();

            // Retrieve the public key bytes of the other party
            publicKeyBytes = ...;

            // Convert the public key bytes into a PublicKey object
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFact = KeyFactory.getInstance("DH");
            publicKey = keyFact.generatePublic(x509KeySpec);

            // Prepare to generate the secret key with the private key and public key of the other party
            KeyAgreement ka = KeyAgreement.getInstance("DH");
            ka.init(privateKey);
            ka.doPhase(publicKey, true);

            // Specify the type of key to generate;
            // see Listing All Available Symmetric Key Generators
            String algorithm = "DES";

            // Generate the secret key
            SecretKey secretKey = ka.generateSecret(algorithm);

            // Use the secret key to encrypt/decrypt data;
            // see Encrypting a String with DES
        } catch (java.security.InvalidKeyException e) {
        } catch (java.security.spec.InvalidKeySpecException e) {
        } catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        }
    }*/

}



