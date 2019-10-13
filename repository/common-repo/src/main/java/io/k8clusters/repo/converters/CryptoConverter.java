//package io.k8clusters.repo.converters;
//
//import io.k8clusters.repo.context.PersistenceEncryptionContext;
//import org.jasypt.encryption.pbe.PBEStringEncryptor;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
///**
// * CryptoConverter:
// *
// * @author Amit Kshirsagar
// * @version 1.0
// * @since May 4, 2019
// */
//@Converter
//public class CryptoConverter implements AttributeConverter<String, String> {
//
//    @Override
//    public String convertToDatabaseColumn(String message) {
//        return getEncryptor().encrypt(message);
//    }
//
//    @Override
//    public String convertToEntityAttribute(String encryptedMessage) {
//        try {
//            return getEncryptor().decrypt(encryptedMessage);
//        } catch (Exception e) {
//
//        }
//        return encryptedMessage;
//    }
//    private PBEStringEncryptor getEncryptor() {
//        System.out.println(String.format("CryptoConverter:Encryptor Key Used: [%s]", PersistenceEncryptionContext.getInstance().getPassword()));
//        return PersistenceEncryptionContext.getInstance().getEncryptor();
//    }
//}