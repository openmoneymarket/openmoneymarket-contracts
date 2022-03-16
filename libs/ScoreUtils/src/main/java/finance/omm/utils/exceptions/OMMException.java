package finance.omm.utils.exceptions;


import score.UserRevertException;
import score.UserRevertedException;

public class OMMException extends UserRevertException {

    /**
     * OMMException.RewardDistribution => 0 ~ OMMException.RESERVED => 80 ~ 99
     */
    enum Type {
        RewardController(0), RewardDistribution(10), RESERVED(80);

        int offset;

        Type(int offset) {
            this.offset = offset;
        }

        int apply(int code) {
            code = offset + code;
            if (this.equals(RESERVED) || code >= values()[ordinal() + 1].offset) {
                throw new IllegalArgumentException();
            }
            return code;
        }

        int recover(int code) {
            code = code - offset;
            if (this.equals(RESERVED) || code < 0) {
                throw new IllegalArgumentException();
            }
            return code;
        }

        static Type valueOf(int code) throws IllegalArgumentException {
            for (Type t : values()) {
                if (code < t.offset) {
                    if (t.ordinal() == 0) {
                        throw new IllegalArgumentException();
                    } else {
                        return t;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
    }

    private final Type type;
    private final int code;

    OMMException(Code c) {
        this(Type.RewardDistribution, c, c.name());
    }

    OMMException(Code code, String message) {
        this(Type.RewardDistribution, code, message);
    }

    OMMException(Type type, Coded code, String message) {
        this(type, code.code(), message);
    }

    OMMException(Type type, int code, String message) {
        super(message);
        this.type = type;
        this.code = type.apply(code);
    }

    OMMException(UserRevertedException e) {
        super(e.getMessage(), e);
        this.code = e.getCode();
        this.type = Type.valueOf(code);
    }

    public static OMMException of(UserRevertedException e) {
        return new OMMException(e);
    }

    @Override
    public int getCode() {
        return code;
    }

    public int getCodeOfType() {
        return type.recover(code);
    }

    public interface Coded {

        int code();

        default boolean equals(OMMException e) {
            return code() == e.getCodeOfType();
        }
    }

    public enum Code implements Coded {
        Unknown(0);

        final int code;

        Code(int code) {this.code = code;}

        public int code() {return code;}

    }

    public static OMMException unknown(String message) {
        return new OMMException(Code.Unknown, message);
    }

    public static class RewardDistribution extends OMMException {

        public RewardDistribution(int code, String message) {
            super(Type.RewardDistribution, code, message);
        }

        public RewardDistribution(Coded code, String message) {
            this(code.code(), message);
        }
    }

    public static class RewardController extends OMMException {

        public RewardController(int code, String message) {
            super(Type.RewardController, code, message);
        }

        public RewardController(Coded code, String message) {
            this(code.code(), message);
        }
    }

    public static class AddressProviderException extends OMMException {

        public AddressProviderException(int code, String message) {
            super(Type.RewardController, code, message);
        }

        public AddressProviderException(Coded code, String message) {
            this(code.code(), message);
        }
    }
}