/**
 * Copyright 2012-2019 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.persistence.support.sqlite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.locks.Lock;

/**
 * Wraps a {@link Connection} in a proxy object which delegates all method calls to it, and which additionally
 * unlocks the given lock whenever the {@link Connection#close()} method is closed.
 * <p/>
 * Unlocks the lock at most once, ignoring subsequent calls.
 */
public class LockReleasingConnection implements InvocationHandler {
    final Connection targetConnection;
    final Lock lockToUnlock;
    boolean unlockedAlready = false;

    LockReleasingConnection(Connection targetConnection, Lock lockToUnlock) {
        this.targetConnection = targetConnection;
        this.lockToUnlock = lockToUnlock;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result = m.invoke(targetConnection, args);
        if(m.getName().equals("close") && !unlockedAlready){
            lockToUnlock.unlock();
            unlockedAlready = true;
        }
        return result;
    }

    public static Connection wrap(Connection targetConnection, Lock lockToUnlock) {
        return (Connection) Proxy.newProxyInstance(
                targetConnection.getClass().getClassLoader(),
                new Class<?>[] { Connection.class },
                new LockReleasingConnection(targetConnection, lockToUnlock)
        );
    }
}
