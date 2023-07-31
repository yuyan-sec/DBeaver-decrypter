package me.gv7.woodpecker.helper;

import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IResultOutput;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PasswdDecryptHelper implements IHelper {
    private static final byte[] LOCAL_KEY_CACHE = new byte[] { -70, -69, 74, -97, 119, 74, -72, 83, -55, 108, 45, 101, 61, -2, 84, 74 };

    public PasswdDecryptHelper() {
    }

    public String getHelperTabCaption() {
        return "DBeaver password Decrypter";
    }

    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = DBeaverPasswdDcrypter.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList();
        IArg argPassword = DBeaverPasswdDcrypter.pluginHelper.createArg();
        argPassword.setName("file");
        argPassword.setDefaultValue("C:\\Users\\administrator\\AppData\\Roaming\\DBeaverData\\workspace6\\General\\.dbeaver\\credentials-config.json");
        argPassword.setDescription("需要解密的连接文件");
        argPassword.setRequired(true);
        args.add(argPassword);

        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    public void doHelp(Map<String, Object> customArgs, IResultOutput iResultOutput) throws Throwable {
        String filename = (String) customArgs.get("file");
//        String customKey = (String) customArgs.get("key");
//        long key = 1231234234;
//        if (customKey != null) {
//            key = Long.parseLong(customKey);
//        }
        try {
//            Encode encoder = new Encode();
//            String plainText = encoder.decode(password, key);

            String plainText = Decrypt(Files.readAllBytes(Paths.get(filename)));
            iResultOutput.successPrintln("Decrypt result:");
            iResultOutput.rawPrintln("\n" + plainText + "\n");
        } catch (Exception var6) {
            iResultOutput.errorPrintln(DBeaverPasswdDcrypter.pluginHelper.getThrowableInfo(var6));
        }
    }

    public static String Decrypt(byte[] contents){
        try (InputStream byteStream = new ByteArrayInputStream(contents)) {
            byte[] fileIv = new byte[16];
            byteStream.read(fileIv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey aes = new SecretKeySpec(LOCAL_KEY_CACHE, "AES");
            cipher.init(Cipher.DECRYPT_MODE, aes, new IvParameterSpec(fileIv));
            try (CipherInputStream cipherIn = new CipherInputStream(byteStream, cipher)) {
                return inputStreamToString(cipherIn);
            } catch (Exception e){
                return e.getMessage();
            }
        } catch (Exception e){
            return e.getMessage();
        }
    }

    static String inputStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
