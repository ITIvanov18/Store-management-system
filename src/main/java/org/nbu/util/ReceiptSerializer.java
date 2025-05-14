package org.nbu.util;

import org.nbu.data.Receipt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReceiptSerializer {

    public static void serialize(Receipt receipt) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("receipt-" + receipt.getId() + ".ser"))) {
            out.writeObject(receipt);
        } catch (IOException e) {
            throw new RuntimeException("Грешка при сериализация на Receipt", e);
        }
    }

    public static Receipt deserialize(int id) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("receipt-" + id + ".ser"))) {
            return (Receipt) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Грешка при зареждане на Receipt", e);
        }
    }
}

