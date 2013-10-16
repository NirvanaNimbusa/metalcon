package de.metalcon.storage;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Jonas Kunze
 */
public class PersistentUUIDSetLevelDB implements IPersistentUUIDSet {
	private static LevelDBHandler db;

	private final long ID;

	public static void initialize() {
		db = new LevelDBHandler("PersistentUUIDSetLevelDB");
	}

	public PersistentUUIDSetLevelDB(final long ID) {
		this.ID = ID;
	}

	public PersistentUUIDSetLevelDB(final String ID) {
		this.ID = ID.hashCode();
	}

	/**
	 * Adds the given uuid to the end of the file
	 */
	public void add(long uuid) {
		db.addToSet(ID, uuid);
	}

	/**
	 * Deletes all entries from the Set
	 */
	public void delete() {
		// TODO To be implemented
	}

	@Override
	public boolean add(Long uuid) {
		add((long) uuid);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Long> arg0) {
		// TODO To be implemented
		return false;
	}

	@Override
	public void clear() {
		// TODO To be implemented

	}

	public boolean contains(long key) {
		return db.containsElementInSet(ID, key);
	}

	@Override
	public boolean contains(Object obj) {
		// TODO To be implemented
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO To be implemented
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO To be implemented
		return false;
	}

	@Override
	public Iterator<Long> iterator() {
		return new ArrayIterator(db.getLongs(ID));
	}

	/**
	 * 
	 * @param uuid
	 *            Thee uuid to be removed
	 * @return Returns true if this set contained the uuid
	 */
	public boolean remove(long uuid) {
		db.removeFromSet(ID, uuid);
		return true;
	}

	@Override
	public boolean remove(Object uuid) {
		return remove((long) uuid);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO To be implemented
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO To be implemented
		return false;
	}

	@Override
	public int size() {
		// TODO To be implemented
		return -1;
	}

	@Override
	public Object[] toArray() {
		// TODO To be implemented
		return null;
	}

	/**
	 * Writes all uuids into the given array
	 * 
	 * @param array
	 * @return
	 */
	public long[] toArray(long[] array) {
		return db.getLongs(ID);
	}

	@Override
	public <T> T[] toArray(T[] obj) {
		// TODO To be implemented
		return null;
	}

	public void closeFileIfNecessary() {
		// do nothing
	}
}

class ArrayIterator implements Iterator<Long> {
	private long array[];
	private int pos = 0;

	public ArrayIterator(long anArray[]) {
		array = anArray;
	}

	public boolean hasNext() {
		return pos < array.length;
	}

	public Long next() throws NoSuchElementException {
		if (hasNext())
			return array[pos++];
		else
			throw new NoSuchElementException();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
