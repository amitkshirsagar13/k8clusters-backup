package io.k8clusters.auth.repo.context;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PersistenceEncryptionContext {
    private static final ThreadLocal<PersistenceEncryptionContext> threadInstance = new ThreadLocal<>();

    private PBEStringEncryptor encryptor = null;

    private PersistenceEncryptionContext() {
    }

    public static PersistenceEncryptionContext getInstance() {
        if (threadInstance.get() == null) {
            PersistenceEncryptionContext inst = new PersistenceEncryptionContext();
            threadInstance.set(inst);
        }
        return threadInstance.get();
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.encryptor = new StandardPBEStringEncryptor();
        this.encryptor.setPassword(password);
    }

    public PBEStringEncryptor getEncryptor() {
        return encryptor;
    }

    public void setEncryptor(PBEStringEncryptor encryptor) {
        this.encryptor = encryptor;
    }
}
