/**
 * Copyright 2012 Niall Gallagher
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
package com.googlecode.cqengine.collection.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An extension of {@link ReentrantReadWriteLock} which provides an {@link #isReadLockHeldByCurrentThread()} method.
 *
 * @author ngallagher
 * @since 2012-04-24 19:15
 */
public class TrackedReentrantReadWriteLock extends ReentrantReadWriteLock {

    final ThreadLocal<IntHolder> readLockHoldCount = new ThreadLocal<IntHolder>() {
        @Override
        protected IntHolder initialValue() {
            return new IntHolder();
        }
    };

    static class IntHolder {
        int value = 0;

        void increment() {
            value += 1;
        }

        void decrement() {
            value -= 1;
        }

        int value() {
            return value;
        }
    }

    public boolean isReadLockHeldByCurrentThread() {
        return readLockHoldCount.get().value() > 0;
    }

    @Override
    public ReadLock readLock() {
        final ReadLock realLock = super.readLock();
        return new ReadLock(this) {
            @Override
            public void lock() {
                realLock.lock();
                readLockHoldCount.get().increment();
            }

            @Override
            public void lockInterruptibly() throws InterruptedException {
                realLock.lockInterruptibly();
                readLockHoldCount.get().increment();
            }

            @Override
            public boolean tryLock() {
                boolean acquired = realLock.tryLock();
                if (acquired) {
                    readLockHoldCount.get().increment();
                }
                return acquired;
            }

            @Override
            public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
                boolean acquired = realLock.tryLock(timeout, unit);
                if (acquired) {
                    readLockHoldCount.get().increment();
                }
                return acquired;
            }

            @Override
            public void unlock() {
                realLock.unlock();
                readLockHoldCount.get().decrement();
            }

            @Override
            public Condition newCondition() {
                // This always throws UnsupportedOperationException...
                return realLock.newCondition();
            }
        };
    }
}
