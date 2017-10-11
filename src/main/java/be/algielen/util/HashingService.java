package be.algielen.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class HashingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashingService.class);
    private final String algorithm = "SHA-512";
    private MessageDigest messageDigest;

    @PostConstruct
    private void init() {
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("No algorithm named " + algorithm);
            throw new RuntimeException(e);
        }
    }

    public byte[] hash(byte[] content) {
        return messageDigest.digest(content);

    }
}
