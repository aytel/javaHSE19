package com.aytel;

import com.mongodb.MongoClient;
import org.jetbrains.annotations.NotNull;
import xyz.morphia.Datastore;
import xyz.morphia.Morphia;
import xyz.morphia.query.Query;

import java.util.List;
import java.util.NoSuchElementException;

/** Class which works with database, storing {@link Ownership}. */
class PhoneBook {
    @NotNull private final Datastore datastore;

    /** Creates database with the given name. */
    PhoneBook(@NotNull String databaseName) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.aytel");
        datastore = morphia.createDatastore(new MongoClient(), databaseName);
        datastore.ensureIndexes();
    }

    private Query<Ownership> find(@NotNull Ownership ownership) {
        Query<Ownership> query = datastore.createQuery(Ownership.class);
        query.and(
                query.criteria(Ownership.NAME).equal(ownership.name),
                query.criteria(Ownership.PHONE_NUMBER).equal(ownership.phoneNumber)
        );
        return query;
    }

    private boolean contains(@NotNull Ownership ownership) {
        return find(ownership).get() != null;
    }

    /**
     * Adds new {@link Ownership} to database.
     * @return false if there was such element and true otherwise.
     */
    boolean add(@NotNull String name, @NotNull String number) {
        final var ownership = new Ownership(name, number);
        if (contains(ownership)) {
            return false;
        }
        datastore.save(ownership);
        return true;
    }

    /**
     * Removes {@link Ownership} from database.
     * @return true if there was such element and false otherwise.
     */
    boolean remove(@NotNull String name, @NotNull String number) {
        final var ownership = new Ownership(name, number);
        if (!contains(ownership)) {
            return false;
        }
        datastore.delete(find(ownership));
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
            datastore.delete(find(ownershipBefore));
            return false;
        }

        datastore.update(find(ownershipBefore),
                datastore.createUpdateOperations(Ownership.class).set(fieldToUpdate, updateValue));

        return true;
    }

    /**
     * Changes name of {@link Ownership}.
     * @return false if there was changed element in database before and true otherwise.
     * @throws NoSuchElementException if there's no element to change.
     */
    boolean updateName(@NotNull String nameBefore, @NotNull String number, @NotNull String nameAfter) {
        return update(
                new Ownership(nameBefore, number),
                new Ownership(nameAfter, number),
                Ownership.NAME,
                nameAfter
        );
    }

    /**
     * Changes phoneNumber of {@link Ownership}.
     * @return false if there was changed element in database before and true otherwise.
     * @throws NoSuchElementException if there's no element to change.
     */
    boolean updateNumber(@NotNull String name, @NotNull String numberBefore, @NotNull String numberAfter) {
        return update(
                new Ownership(name, numberBefore),
                new Ownership(name, numberAfter),
                Ownership.PHONE_NUMBER,
                numberAfter
        );
    }

    /** Returns all elements as list. */
    @NotNull List<Ownership> getAll() {
        return datastore.createQuery(Ownership.class).asList();
    }

    /** Returns all element with the given name as list/ */
    @NotNull List<Ownership> getByName(@NotNull String name) {
        return datastore.createQuery(Ownership.class).field(Ownership.NAME).equal(name).asList();
    }

    /** Returns all element with the given phoneNumber as list/ */
    @NotNull List<Ownership> getByNumber(@NotNull String number) {
        return datastore.createQuery(Ownership.class).field(Ownership.PHONE_NUMBER).equal(number).asList();
    }

    /** Drops database. */
    void clear() {
        datastore.getDB().dropDatabase();
    }
}
