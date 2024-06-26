# PrimitiveUtil

- String implements

```java
        Boolean bool = true;
        var dt = LocalDate.of(2000, 1, 3);
        var tm = LocalTime.of(22, 37, 12);
        Assertions.assertEquals(PrimitiveUtil.toString(bool), "true");
        Assertions.assertEquals(PrimitiveUtil.toString(LocalDateTime.of(dt, tm)), "2000-01-03T22:37:12");
        Assertions.assertEquals(PrimitiveUtil.toString(dt), "2000-01-03");
        Assertions.assertEquals(PrimitiveUtil.toString(tm), "22:37:12");

        Assertions.assertEquals(PrimitiveUtil.toString(10.125D), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.125D), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.12D), "10.12");

        Assertions.assertEquals(PrimitiveUtil.toString(10.125), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.125), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.12), "10.12");
        Assertions.assertEquals(PrimitiveUtil.toString(1), "1");
        Assertions.assertEquals(PrimitiveUtil.toString(1000), "1000");
        Assertions.assertEquals(PrimitiveUtil.toString(1L), "1");
        Assertions.assertEquals(PrimitiveUtil.toString(1000L), "1000");

        Assertions.assertEquals(PrimitiveUtil.toString("a"), "a");
        Assertions.assertEquals(PrimitiveUtil.toString("test"), "test");
```

- UUID implements

```java
var uuid = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");
var uuid_str = "c4ca4238a0b923820dcc509a6f75849b";
Assertions.assertNull(PrimitiveUtil.toUUID(""));
Assertions.assertEquals(PrimitiveUtil.toUUID(uuid.toString()), uuid);
Assertions.assertEquals(PrimitiveUtil.toUUID(uuid_str), uuid);
```

- Long implements
    - to
      ```java
      Assertions.assertEquals(PrimitiveUtil.toLong(""), 0);
      Assertions.assertEquals(PrimitiveUtil.toLong("100"), 100);
      Assertions.assertEquals(PrimitiveUtil.toLong("100.1"), 0);
      Assertions.assertEquals(PrimitiveUtil.toLong(100L), 100L);
      Assertions.assertEquals(PrimitiveUtil.toLong(true), 1);
      Assertions.assertEquals(PrimitiveUtil.toLong(false), 0);
      ```
    - as
      ```java
      Assertions.assertEquals(PrimitiveUtil.asLong(""), 0);
      Assertions.assertEquals(PrimitiveUtil.asLong("100"), 100);
      Assertions.assertEquals(PrimitiveUtil.asLong("100.1"), 100);
      Assertions.assertEquals(PrimitiveUtil.asLong("999999999"), 999999999);
      Assertions.assertEquals(PrimitiveUtil.asLong(100), 100);
      Assertions.assertEquals(PrimitiveUtil.asLong(true), 1);
      Assertions.assertEquals(PrimitiveUtil.asLong(false), 0);
      ```
- Integer implements
    - to
      ```java
          Assertions.assertEquals(PrimitiveUtil.toInt(""), 0);
          Assertions.assertEquals(PrimitiveUtil.toInt("100"), 100);
          Assertions.assertEquals(PrimitiveUtil.toInt("100.1"), 0);
          Assertions.assertEquals(PrimitiveUtil.toInt("9999999999999999999999999"), 0);
          Assertions.assertEquals(PrimitiveUtil.toInt(100), 100);
          Assertions.assertEquals(PrimitiveUtil.toInt(true), 1);
          Assertions.assertEquals(PrimitiveUtil.toInt(false), 0);
      ```
    - as
      ```java
          Assertions.assertEquals(PrimitiveUtil.asInt(""), 0);
          Assertions.assertEquals(PrimitiveUtil.asInt("100"), 100);
          Assertions.assertEquals(PrimitiveUtil.asInt("100.1"), 100);
          Assertions.assertEquals(PrimitiveUtil.asInt(100), 100);
          Assertions.assertEquals(PrimitiveUtil.asInt(true), 1);
          Assertions.assertEquals(PrimitiveUtil.asInt(false), 0);
      ```
- Double implements
    ```java
    Assertions.assertEquals(PrimitiveUtil.toDouble(""), 0);
    Assertions.assertEquals(PrimitiveUtil.toDouble("0"), 0);
    Assertions.assertEquals(PrimitiveUtil.toDouble("-100"), -100);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100"), 100);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.00"), 100);
    Assertions.assertEquals(PrimitiveUtil.toDouble("0.01"), 0.01);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.1"), 100.1);
    Assertions.assertEquals(PrimitiveUtil.toDouble("9999999999999999999999999"), 9999999999999999999999999D);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.11001", 0), 100);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.11001", 1), 100.1);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.12001", 2), 100.120);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.12301", 3), 100.123);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.12341", 4), 100.1234);
    Assertions.assertEquals(PrimitiveUtil.toDouble("100.12341", 5), 100.12341);
    Assertions.assertEquals(PrimitiveUtil.toDouble("240.225", 5), 240.225);
    Assertions.assertEquals(PrimitiveUtil.toDouble("240.225", 3), 240.225);
    Assertions.assertEquals(PrimitiveUtil.toDouble("240.225", 2), 240.23);
    Assertions.assertEquals(PrimitiveUtil.toDouble("240.23", 2), 240.23);
    
    
    Assertions.assertEquals(PrimitiveUtil.toDouble(100.11001D, 0), 100);
    Assertions.assertEquals(PrimitiveUtil.toDouble(100.11001D, 1), 100.1);
    Assertions.assertEquals(PrimitiveUtil.toDouble(100.12001D, 2), 100.120);
    Assertions.assertEquals(PrimitiveUtil.toDouble(100.12301D, 3), 100.123);
    Assertions.assertEquals(PrimitiveUtil.toDouble(100.12341D, 4), 100.1234);
    Assertions.assertEquals(PrimitiveUtil.toDouble(100.12341D, 5), 100.12341);
    Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 5), 240.225);
    Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 3), 240.225);
    Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 2), 240.23);
    Assertions.assertEquals(PrimitiveUtil.toDouble(240.23, 2), 240.23);
    Assertions.assertEquals(PrimitiveUtil.toDouble(true), 1);
    Assertions.assertEquals(PrimitiveUtil.toDouble(false), 0);
    ```

- Date implements
    ```java
    var dt19010101 = LocalDate.of(1901, 1, 1);
    Assertions.assertNull(PrimitiveUtil.toDate(""));
    Assertions.assertEquals(PrimitiveUtil.toDate(dt19010101.toString()), dt19010101);
    Assertions.assertEquals(PrimitiveUtil.toDate("1901-01-01"), dt19010101);
    ```

- Time implements
    ```java
    var tm010203 = LocalTime.of(1, 2, 3);
    Assertions.assertNull(PrimitiveUtil.toTime(""));
    Assertions.assertEquals(PrimitiveUtil.toTime(tm010203.toString()), tm010203);
    Assertions.assertEquals(PrimitiveUtil.toTime("01:02:03"), tm010203);
    ```

- DateTime implements
    ```java
    var dt19010101 = LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(1, 2, 3));
    Assertions.assertNull(PrimitiveUtil.toDateTime(""));
    Assertions.assertEquals(PrimitiveUtil.toDateTime(dt19010101.toString()), dt19010101);
    Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01T01:02:03"), dt19010101);
    ```

- Boolean implements
    ```java
    Assertions.assertTrue(PrimitiveUtil.toBool("true"));
    Assertions.assertFalse(PrimitiveUtil.toBool("false"));
    Assertions.assertTrue(PrimitiveUtil.toBool("1"));
    Assertions.assertFalse(PrimitiveUtil.toBool("0"));
    ```

- Empty implements
    ```java
    Object object = new Object();
    var d_tNull = LocalDateTime.now();
    var dtNull = LocalDate.now();
    var tmNull = LocalTime.now();
  
    Assertions.assertFalse(PrimitiveUtil.isEmpty(object));
    Assertions.assertFalse(PrimitiveUtil.isEmpty("A"));
    Assertions.assertFalse(PrimitiveUtil.isEmpty(1));
    Assertions.assertFalse(PrimitiveUtil.isEmpty(2L));
    Assertions.assertFalse(PrimitiveUtil.isEmpty(3D));
    Assertions.assertFalse(PrimitiveUtil.isEmpty(d_tNull));
    Assertions.assertFalse(PrimitiveUtil.isEmpty(dtNull));
    Assertions.assertFalse(PrimitiveUtil.isEmpty(tmNull));

    Assertions.assertTrue(PrimitiveUtil.isEmpty(object = null));
    Assertions.assertTrue(PrimitiveUtil.isEmpty(""));
    Assertions.assertTrue(PrimitiveUtil.isEmpty(0));
    Assertions.assertTrue(PrimitiveUtil.isEmpty(0L));
    Assertions.assertTrue(PrimitiveUtil.isEmpty(0D));
    Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalDate)null) && PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATETIME));
    Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalTime)null) && PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATE));
    Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalDateTime)null) && PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATE));
    ```

- Range implements
    ```java
    class Check<T> {
        final T v;
        final T begin;
        final T end;
        final boolean r;

        public Check(T v, T begin, T end, boolean r) {
            this.v = v;
            this.begin = begin;
            this.end = end;
            this.r = r;
        }

    };
    var curDate = LocalDate.now();

    List<Check<LocalDate>> listDate = List.of(
            new Check<>(curDate, curDate, curDate, true),
            new Check<>(curDate, null, curDate, true),
            new Check<>(curDate, curDate, null, true),
            new Check<>(null, null, null, false),
            new Check<>(curDate, curDate.plusDays(1), curDate, false),
            new Check<>(curDate, curDate, curDate.minusDays(1), false)
    );
    for (var check : listDate)
        Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);

    var curTime = LocalTime.now();
    List<Check<LocalTime>> listTime = List.of(
            new Check<>(curTime, curTime, curTime, true),
            new Check<>(curTime, null, curTime, true),
            new Check<>(curTime, curTime, null, true),
            new Check<>(null, null, null, false),
            new Check<>(curTime, curTime.plusMinutes(1), curTime, false),
            new Check<>(curTime, curTime, curTime.minusMinutes(1), false)
    );
    for (var check : listTime)
        Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);

    var curDateTime = LocalDateTime.of(curDate, curTime);
    List<Check<LocalDateTime>> listDateTime = List.of(
            new Check<>(curDateTime, curDateTime, curDateTime, true),
            new Check<>(curDateTime, null, curDateTime, true),
            new Check<>(curDateTime, curDateTime, null, true),
            new Check<>(null, null, null, false),
            new Check<>(curDateTime, curDateTime.plusDays(1), curDateTime, false),
            new Check<>(curDateTime, curDateTime, curDateTime.minusDays(1), false)
    );
    for (var check : listDateTime)
        Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);


    List<Check<Double>> listDouble = List.of(
            new Check<>(0D, 0D, 0D, true),
            new Check<>(1D, 0D, 1D, true),
            new Check<>(1D, 0D, 2D, true),
            new Check<>(0D, 0D, 2D, true),
            new Check<>(0D, -1D, 2D, true),
            new Check<>(0D, 1D, 2D, false),
            new Check<>(-1D, 1D, 2D, false)
    );
    for (var check : listDouble)
        Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);
    ```