package com.aytel;

import com.mongodb.MongoClient;
import org.jetbrains.annotations.NotNull;
import xyz.morphia.Datastore;
import xyz.morphia.Morphia;
import xyz.morphia.query.Query;

import java.util.List;
import java.util.NoSuchElementException;

public class PhoneBook {
    @NotNull private final Datastore datastore;

    public PhoneBook(@NotNull String databaseName) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.aytel");
        datastore = morphia.createDatastore(new MongoClient(), databaseName);
        datastore.getDB().dropDatabase();
        datastore.ensureIndexes();
    }

    private Ownership find(@NotNull Ownership ownership) {
        Query<Ownership> query = datastore.createQuery(Ownership.class);
        query.and(
                query.criteria("name").equal(ownership.name),
                query.criteria("number").equal(ownership.number)
        );
        return query.get();
    }

    private boolean contains(@NotNull Ownership ownership) {
        return find(ownership) != null;
    }

    public boolean add(@NotNull String name, @NotNull String number) {
        final var ownership = new Ownership(name, number);
        if (contains(ownership)) {
            return false;
        }
        datastore.save(ownership);
        return false;
    }

    public boolean remove(@NotNull String name, @NotNull String number) {
        final var ownership = new Ownership(name, number);
        if (!contains(ownership)) {
            return false;
        }
        datastore.delete(ownership);
        return true;
    }

    private boolean update(@NotNull Ownership ownershipBefore,
                           @NotNull Ownership ownershipAfter,
                           @NotNull String fieldToUpdate,
                           @NotNull String updateValue) {

        if (!contains(ownershipBefore)) {
            throw new NoSuchElementException(ownershipBefore.toString() + " doesn't exist.");
        }

        if (contains(ownershipAfter)) {
            datastore.delete(ownershipBefore);
            return true;
        }

        datastore.update(ownershipBefore,
                datastore.createUpdateOperations(Ownership.class).set(fieldToUpdate, updateValue));

        return false;
    }

    public boolean updateName(@NotNull String nameBefore, @NotNull String number, @NotNull String nameAfter) {
        return update(
                new Ownership(nameBefore, number),
                new Ownership(nameAfter, number),
                "name",
                nameAfter
        );
    }

    public boolean updateNumber(@NotNull String name, @NotNull String numberBefore, @NotNull String numberAfter) {
        return update(
                new Ownership(name, numberBefore),
                new Ownership(name, numberAfter),
                "number",
                numberAfter
        );
    }

    @NotNull
    public List<Ownership> getAll() {
        return datastore.createQuery(Ownership.class).asList();
    }

    @NotNull
    public List<Ownership> getByName(@NotNull String name) {
        return datastore.createQuery(Ownership.class).field("name").equal(name).asList();
    }

    @NotNull
    public List<Ownership> getByNumber(@NotNull String number) {
        return datastore.createQuery(Ownership.class).field("number").equal(number).asList();
    }

}
