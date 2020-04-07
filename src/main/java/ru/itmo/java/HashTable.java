package ru.itmo.java;

public class HashTable {
    private Entry[] entriesArray;
    private int entriesQuantity;
    private final float loadFactor;

    private class Entry {
        private Object entryKey;
        private Object entryValue;
        private boolean deletedStatus;

        Entry(Object key_, Object value_) {
            entryKey = key_;
            entryValue = value_;
            deletedStatus = false;
        }

        void delete() {
            deletedStatus = true;
        }
        Object getKey() {
            return entryKey;
        }
        Object getValue() {
            return  entryValue;
        }

        boolean isDeleted() {
            return deletedStatus;
        }
    }

    public HashTable(int size) {
        if (size <= 0) {
            entriesArray = new Entry[1];
        } else {
            entriesArray = new Entry[size];
        }
        loadFactor = 0.2F;
    }
    public HashTable(int size, float loadFactor) {
        if (size <= 0) {
            entriesArray = new Entry[1];
        } else {
            entriesArray = new Entry[size];
        }
        if (loadFactor >= 1.0F) {
            this.loadFactor = 0.2F;
        } else {
            this.loadFactor = loadFactor;
        }
    }

    private int hashFunction(int hash, int addition) {
        int result = ((hash + addition) % entriesArray.length);
        if (result < 0) {
            return (-result);
        }
        return result;
    }
    private int threshold() {
        return (int)(entriesArray.length * loadFactor);
    }
    private void arrayExtension() {
        int previousLength = entriesArray.length;
        Entry[] temp = new Entry[previousLength];
        for (int i = 0; i < entriesArray.length; i++) {
            temp[i] = entriesArray[i];
        }
        entriesArray = new Entry[previousLength * 2];
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == null) {
                continue;
            }
            if (temp[i].isDeleted()) {
                continue;
            }

            Entry currentEntry = temp[i];

            int keyHash = currentEntry.getKey().hashCode();
            if (keyHash < 0) {
                keyHash = -keyHash;
            }

            int predictedIndex;
            int addition = 0;
            while (true) {
                predictedIndex = hashFunction(keyHash, addition);
                if (entriesArray[predictedIndex] == null) {
                    entriesArray[predictedIndex] = currentEntry;
                    break;
                }
                addition++;
            }
        }
    }

    Object put(Object key, Object value) {
        if (key == null) {
            return null;
        }
        if ((entriesQuantity + 1) >= threshold()) {
            arrayExtension();
        }

        int keyHash = key.hashCode();
        if (keyHash < 0) {
            keyHash = -keyHash;
        }

        int predictedIndex;
        int addition = 0;
        while (true) {
            predictedIndex = hashFunction(keyHash, addition);
            if (entriesArray[predictedIndex] == null) {
                entriesArray[predictedIndex] = new Entry(key, value);
                entriesQuantity += 1;
                return null;
            }
            if (entriesArray[predictedIndex].isDeleted()){
                if (entriesArray[predictedIndex].getKey().equals(key)) {
                    entriesArray[predictedIndex] = null;
                    entriesArray[predictedIndex] = new Entry(key, value);
                    entriesQuantity += 1;
                    return null;
                } else {
                    addition++;
                }
            } else {
                if (entriesArray[predictedIndex].getKey().equals(key)) {
                    Entry result = new Entry(entriesArray[predictedIndex].getKey(),
                            entriesArray[predictedIndex].getValue());
                    entriesArray[predictedIndex] = null;
                    entriesArray[predictedIndex] = new Entry(key, value);
                    return result.getValue();
                } else {
                    addition++;
                }
            }
            if (addition > entriesArray.length * 2) {
                return null;
            }
        }
    }

    Object get(Object key) {
        if (key == null) {
            return null;
        }

        int keyHash = key.hashCode();
        if (keyHash < 0) {
            keyHash = -keyHash;
        }

        int predictedIndex;
        int addition = 0;
        while (true){
            predictedIndex = hashFunction(keyHash, addition);
            if (entriesArray[predictedIndex] == null) {
                return null;
            }
            if (entriesArray[predictedIndex].isDeleted()) {
                if (entriesArray[predictedIndex].getKey().equals(key)){
                    return null;
                } else {
                    addition++;
                }
            } else {
                if (entriesArray[predictedIndex].getKey().equals(key)){
                    return entriesArray[predictedIndex].getValue();
                } else {
                    addition++;
                }
            }
            if (addition > entriesArray.length * 2) {
                return null;
            }
        }
    }

    Object remove(Object key) {
        if (key == null) {
            return null;
        }

        int keyHash = key.hashCode();
        if (keyHash < 0) {
            keyHash = -keyHash;
        }

        int predictedIndex;
        int addition = 0;
        while (true) {
            predictedIndex = hashFunction(keyHash, addition);
            if (entriesArray[predictedIndex] == null) {
                return null;
            }
            if (entriesArray[predictedIndex].isDeleted()) {
                if (entriesArray[predictedIndex].getKey().equals(key)) {
                    return null;
                } else {
                    addition++;
                }
            } else {
                if (entriesArray[predictedIndex].getKey().equals(key)) {
                    entriesArray[predictedIndex].delete();
                    entriesQuantity -= 1;
                    return entriesArray[predictedIndex].getValue();
                } else {
                    addition++;
                }
            }
            if (addition > entriesArray.length * 2) {
                return null;
            }
        }
    }

    int size() {
        return entriesQuantity;
    }

}
