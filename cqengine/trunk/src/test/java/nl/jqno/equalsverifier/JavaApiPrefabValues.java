/*
 * Copyright 2010-2014 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier;

import net.sf.cglib.proxy.NoOp;
import nl.jqno.equalsverifier.util.ConditionalPrefabValueBuilder;
import nl.jqno.equalsverifier.util.PrefabValues;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import static nl.jqno.equalsverifier.util.ConditionalInstantiator.classes;
import static nl.jqno.equalsverifier.util.ConditionalInstantiator.objects;

/**
 * Creates instances of classes for use in a {@link nl.jqno.equalsverifier.util.PrefabValues} object.
 *
 * Contains hand-made instances of well-known Java API classes that cannot be
 * instantiated dynamically because of an internal infinite recursion of types,
 * or other issues.
 *
 * @author Jan Ouwens
 */
public class JavaApiPrefabValues {
	private PrefabValues prefabValues;

	/**
	 * Adds instances of Java API classes that cannot be instantiated
	 * dynamically to {@code prefabValues}.
	 *
	 * @param prefabValues The instance of prefabValues that should
	 * 			contain the Java API instances.
	 */
	public static void addTo(PrefabValues prefabValues) {
		new JavaApiPrefabValues(prefabValues).addJavaClasses();
	}

	/**
	 * Private constructor. Use {@link #addTo(nl.jqno.equalsverifier.util.PrefabValues)}.
	 */
	private JavaApiPrefabValues(PrefabValues prefabValues) {
		this.prefabValues = prefabValues;
	}
	
	private void addJavaClasses() {
		addPrimitiveClasses();
		addClasses();
		addCollection();
		addLists();
		addMaps();
		addSets();
		addQueues();
		addJava8ApiClasses();
		addGoogleGuavaClasses();
		addJodaTimeClasses();
		addClassesNecessaryForCgLib();
	}
	
	private void addPrimitiveClasses() {
		prefabValues.put(boolean.class, true, false);
		prefabValues.put(byte.class, (byte)1, (byte)2);
		prefabValues.put(char.class, 'a', 'b');
		prefabValues.put(double.class, 0.5D, 1.0D);
		prefabValues.put(float.class, 0.5F, 1.0F);
		prefabValues.put(int.class, 1, 2);
		prefabValues.put(long.class, 1L, 2L);
		prefabValues.put(short.class, (short)1, (short)2);

		prefabValues.put(Boolean.class, true, false);
		prefabValues.put(Byte.class, (byte)1, (byte)2);
		prefabValues.put(Character.class, 'a', 'b');
		prefabValues.put(Double.class, 0.5D, 1.0D);
		prefabValues.put(Float.class, 0.5F, 1.0F);
		prefabValues.put(Integer.class, 1, 2);
		prefabValues.put(Long.class, 1L, 2L);
		prefabValues.put(Short.class, (short)1, (short)2);

		prefabValues.put(Object.class, new Object(), new Object());
		prefabValues.put(Class.class, Class.class, Object.class);
		prefabValues.put(String.class, "one", "two");
	}

	private void addClasses() {
		prefabValues.put(BigDecimal.class, BigDecimal.ZERO, BigDecimal.ONE);
		prefabValues.put(BigInteger.class, BigInteger.ZERO, BigInteger.ONE);
		prefabValues.put(Calendar.class, new GregorianCalendar(2010, 7, 4), new GregorianCalendar(2010, 7, 5));
		prefabValues.put(Date.class, new Date(0), new Date(1));
		prefabValues.put(DateFormat.class, DateFormat.getTimeInstance(), DateFormat.getDateInstance());
		prefabValues.put(File.class, new File(""), new File("/"));
		prefabValues.put(Formatter.class, new Formatter(), new Formatter());
		prefabValues.put(GregorianCalendar.class, new GregorianCalendar(2010, 7, 4), new GregorianCalendar(2010, 7, 5));
		prefabValues.put(Locale.class, new Locale("nl"), new Locale("hu"));
		prefabValues.put(Pattern.class, Pattern.compile("one"), Pattern.compile("two"));
		prefabValues.put(SimpleDateFormat.class, new SimpleDateFormat("yMd"), new SimpleDateFormat("dMy"));
		prefabValues.put(Scanner.class, new Scanner("one"), new Scanner("two"));
		prefabValues.put(TimeZone.class, TimeZone.getTimeZone("GMT+1"), TimeZone.getTimeZone("GMT+2"));
		prefabValues.put(Throwable.class, new Throwable(), new Throwable());
		prefabValues.put(UUID.class, new UUID(0, -1), new UUID(1, 0));
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	private void addCollection() {
		addCollection(Collection.class, new ArrayList(), new ArrayList());
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	private void addLists() {
		addCollection(List.class, new ArrayList(), new ArrayList());
		addCollection(CopyOnWriteArrayList.class, new CopyOnWriteArrayList(), new CopyOnWriteArrayList());
		addCollection(LinkedList.class, new LinkedList(), new LinkedList());
		addCollection(ArrayList.class, new ArrayList(), new ArrayList());
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	private void addMaps() {
		addMap(Map.class, new HashMap(), new HashMap());
		prefabValues.put(EnumMap.class, Dummy.RED.map(), Dummy.BLACK.map());
		addMap(ConcurrentHashMap.class, new ConcurrentHashMap(), new ConcurrentHashMap());
		addMap(HashMap.class, new HashMap(), new HashMap());
		addMap(Hashtable.class, new Hashtable(), new Hashtable());
		addMap(LinkedHashMap.class, new LinkedHashMap(), new LinkedHashMap());
		addMap(Properties.class, new Properties(), new Properties());
		addMap(TreeMap.class, new TreeMap(), new TreeMap());
		addMap(WeakHashMap.class, new WeakHashMap(), new WeakHashMap());
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	private void addSets() {
		addCollection(Set.class, new HashSet(), new HashSet());
		addCollection(CopyOnWriteArraySet.class, new CopyOnWriteArraySet(), new CopyOnWriteArraySet());
		addCollection(TreeSet.class, new TreeSet(), new TreeSet());
		prefabValues.put(EnumSet.class, EnumSet.of(Dummy.RED), EnumSet.of(Dummy.BLACK));
		
		BitSet redBitSet = new BitSet();
		BitSet blackBitSet = new BitSet();
		blackBitSet.set(0);
		prefabValues.put(BitSet.class, redBitSet, blackBitSet);
	}
	
	@SuppressWarnings("rawtypes")
	private void addQueues() {
		prefabValues.put(Queue.class, new ArrayBlockingQueue(1), new ArrayBlockingQueue(1));
		prefabValues.put(ArrayBlockingQueue.class, new ArrayBlockingQueue(1), new ArrayBlockingQueue(1));
		prefabValues.put(ConcurrentLinkedQueue.class, new ConcurrentLinkedQueue(), new ConcurrentLinkedQueue());
		prefabValues.put(DelayQueue.class, new DelayQueue(), new DelayQueue());
		prefabValues.put(LinkedBlockingQueue.class, new LinkedBlockingQueue(), new LinkedBlockingQueue());
		prefabValues.put(PriorityBlockingQueue.class, new PriorityBlockingQueue(), new PriorityBlockingQueue());
		prefabValues.put(SynchronousQueue.class, new SynchronousQueue(), new SynchronousQueue());
	}
	
	private void addJava8ApiClasses() {
		ConditionalPrefabValueBuilder.of("java.time.ZoneId")
				.callFactory("of", classes(String.class), objects("+1"))
				.callFactory("of", classes(String.class), objects("-10"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("java.time.format.DateTimeFormatter")
				.withConstant("ISO_TIME")
				.withConstant("ISO_DATE")
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("java.util.concurrent.CompletableFuture")
				.instantiate(classes(), objects())
				.instantiate(classes(), objects())
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("java.util.concurrent.locks.StampedLock")
				.instantiate(classes(), objects())
				.instantiate(classes(), objects())
				.addTo(prefabValues);
	}
	
	private void addGoogleGuavaClasses() {
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableList")
				.callFactory("of", classes(Object.class), objects("red"))
				.callFactory("of", classes(Object.class), objects("black"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableMap")
				.callFactory("of", classes(Object.class, Object.class), objects("red", "value"))
				.callFactory("of", classes(Object.class, Object.class), objects("black", "value"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableSet")
				.callFactory("of", classes(Object.class), objects("red"))
				.callFactory("of", classes(Object.class), objects("black"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableSortedMap")
				.callFactory("of", classes(Comparable.class, Object.class), objects("red", "value"))
				.callFactory("of", classes(Comparable.class, Object.class), objects("black", "value"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableSortedSet")
				.callFactory("of", classes(Comparable.class), objects("red"))
				.callFactory("of", classes(Comparable.class), objects("black"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableMultiset")
				.callFactory("of", classes(Object.class), objects("red"))
				.callFactory("of", classes(Object.class), objects("black"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableSortedMultiset")
				.callFactory("of", classes(Comparable.class), objects("red"))
				.callFactory("of", classes(Comparable.class), objects("black"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableListMultimap")
				.callFactory("of", classes(Object.class, Object.class), objects("red", "value"))
				.callFactory("of", classes(Object.class, Object.class), objects("black", "value"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableSetMultimap")
				.callFactory("of", classes(Object.class, Object.class), objects("red", "value"))
				.callFactory("of", classes(Object.class, Object.class), objects("black", "value"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableBiMap")
				.callFactory("of", classes(Object.class, Object.class), objects("red", "value"))
				.callFactory("of", classes(Object.class, Object.class), objects("black", "value"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.ImmutableTable")
				.callFactory("of", classes(Object.class, Object.class, Object.class), objects("red", "X", "value"))
				.callFactory("of", classes(Object.class, Object.class, Object.class), objects("black", "X", "value"))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.collect.Range")
				.callFactory("open", classes(Comparable.class, Comparable.class), objects(1, 2))
				.callFactory("open", classes(Comparable.class, Comparable.class), objects(3, 4))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("com.google.common.base.Optional")
				.callFactory("of", classes(Object.class), objects("red"))
				.callFactory("of", classes(Object.class), objects("black"))
				.addTo(prefabValues);
	}
	
	private void addJodaTimeClasses() {
		ConditionalPrefabValueBuilder.of("org.joda.time.Chronology")
				.withConcreteClass("org.joda.time.chrono.GregorianChronology")
				.callFactory("getInstanceUTC", classes(), objects())
				.withConcreteClass("org.joda.time.chrono.ISOChronology")
				.callFactory("getInstanceUTC", classes(), objects())
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("org.joda.time.DateTimeZone")
				.callFactory("forOffsetHours", classes(int.class), objects(+1))
				.callFactory("forOffsetHours", classes(int.class), objects(-10))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("org.joda.time.PeriodType")
				.callFactory("days", classes(), objects())
				.callFactory("hours", classes(), objects())
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("org.joda.time.YearMonth")
				.instantiate(classes(int.class, int.class), objects(2009, 6))
				.instantiate(classes(int.class, int.class), objects(2014, 7))
				.addTo(prefabValues);
		ConditionalPrefabValueBuilder.of("org.joda.time.MonthDay")
				.instantiate(classes(int.class, int.class), objects(6, 1))
				.instantiate(classes(int.class, int.class), objects(7, 26))
				.addTo(prefabValues);
	}
	
	private void addClassesNecessaryForCgLib() {
		prefabValues.put(NoOp.class, new NoOp(){}, new NoOp(){});
	}
	
	private <T extends Collection<Object>> void addCollection(Class<T> type, T red, T black) {
		red.add("red");
		black.add("black");
		prefabValues.put(type, red, black);
	}
	
	private <T extends Map<Object, Object>> void addMap(Class<T> type, T red, T black) {
		red.put("red_key", "red_value");
		black.put("black_key", "black_value");
		prefabValues.put(type, red, black);
	}
	
	private enum Dummy { 
		RED, BLACK;
		
		public EnumMap<Dummy, String> map() {
			EnumMap<Dummy, String> result = new EnumMap<Dummy, String>(Dummy.class);
			result.put(this, toString());
			return result;
		}
	}
}
