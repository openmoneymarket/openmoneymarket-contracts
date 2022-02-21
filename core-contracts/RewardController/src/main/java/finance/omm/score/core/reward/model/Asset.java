package finance.omm.score.core.reward.model;

import java.math.BigInteger;
import score.Address;
import score.ObjectReader;
import score.ObjectWriter;

public class Asset {
    public final String id;
    public final String typeId;
    public String name;
    public BigInteger lpID;
    public Address address;

    public Asset(String id, String typeId) {
        this.id = id;
        this.typeId = typeId;
    }

    public static void writeObject(ObjectWriter w, Asset a) {
        w.beginList(4);
        w.write(a.id);
        w.write(a.typeId);
        w.write(a.name);
        w.writeNullable(a.lpID);
        w.writeNullable(a.address);
        w.end();
    }

    public static Asset readObject(ObjectReader r) {
        r.beginList();
        Asset a = new Asset(r.readString(), r.readString());
        a.name = r.readString();
        a.lpID = r.readNullable(BigInteger.class);
        a.address = r.readNullable(Address.class);
        r.end();
        return a;
    }

}
