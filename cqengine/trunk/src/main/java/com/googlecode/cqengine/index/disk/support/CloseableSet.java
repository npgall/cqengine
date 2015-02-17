package com.googlecode.cqengine.index.disk.support;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;

/**
 * Utility class to facilitate the addition of ResultSet's resources in the QueryOptions' CloseableQueryResources.
 *
 * @author Silvano Riz
 */
public class CloseableSet extends HashSet<Closeable> implements Closeable{

    @Override
    public void close() throws IOException {
        for(Closeable closeable : this){
            try {
                closeable.close();
            }catch (Exception e){
                // ignore.
            }
        }
        this.clear();
    }
}
