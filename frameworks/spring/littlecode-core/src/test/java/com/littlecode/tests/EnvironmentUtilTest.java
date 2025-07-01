package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.EnvironmentUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class EnvironmentUtilTest {

    @Test
    @DisplayName("Deve validar constructor")
    void UI_Constructor() {
        UtilCoreConfig.setApplicationContext(null);
        UtilCoreConfig.setEnvironment(null);
        Assertions.assertThrows(FrameworkException.class, () -> new EnvironmentUtil());
        Assertions.assertThrows(FrameworkException.class, () -> new EnvironmentUtil(null));

        UtilCoreConfig.setApplicationContext(Mockito.mock(ApplicationContext.class));
        UtilCoreConfig.setEnvironment(Mockito.mock(Environment.class));
        Assertions.assertDoesNotThrow(() -> new EnvironmentUtil());
        Assertions.assertDoesNotThrow(() -> new EnvironmentUtil(Mockito.mock(Environment.class)));

        Assertions.assertNotNull(new EnvironmentUtil().getEnvironment());
        Assertions.assertNotNull(new EnvironmentUtil(Mockito.mock(Environment.class)).getEnvironment());
    }

    @Test
    @DisplayName("Deve validar getValues")
    void UI_envValue() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(" itemA,itemB,itemC ");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.x")).thenReturn(false);
            var eUtil = new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr.x"));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr.x", "x"));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr.x", null));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr"));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr"), "itemA,itemB,itemC");

            Assertions.assertNull(eUtil.envValue("item.attr.x"));
            Assertions.assertNotNull(eUtil.envValue("item.attr.x", "x"));
            Assertions.assertEquals("x",eUtil.envValue("item.attr.x", "x"));
            Assertions.assertNotNull(eUtil.envValue("item.attr"));
            Assertions.assertEquals("itemA,itemB,itemC",eUtil.envValue("item.attr") );
        }
    }

    @Test
    @DisplayName("Deve validar asAString")
    void UI_asString() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(" itemA,itemB,itemC ");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.x")).thenReturn(false);
            var eUtil = new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(() -> eUtil.asString("item.attr.x"));
            Assertions.assertDoesNotThrow(() -> eUtil.asString("item.attr"));
            Assertions.assertDoesNotThrow(() -> eUtil.asString("item.attr"), "itemA,itemB,itemC");
            Assertions.assertDoesNotThrow(() -> eUtil.asString("item.attr.x"), "itemA,itemB,itemC");
            Assertions.assertDoesNotThrow(() -> eUtil.asString(null));
            Assertions.assertDoesNotThrow(() -> eUtil.asString(null, null));
            Assertions.assertDoesNotThrow(() -> eUtil.asString("item.attr.x", null));

            Assertions.assertNotNull(eUtil.asString("item.attr"));
            Assertions.assertEquals("itemA,itemB,itemC",eUtil.asString("item.attr"));
            Assertions.assertNotNull(eUtil.asString("item.attr.x"));
            Assertions.assertEquals("", eUtil.asString("item.attr.x"));
            Assertions.assertNotNull(eUtil.asString("item.attr.x", null));
            Assertions.assertEquals("",eUtil.asString("item.attr.x", null));
            Assertions.assertEquals("a",eUtil.asString("item.attr.x", "a"));
            Assertions.assertEquals("", eUtil.asString(null));
        }
        {//step 2
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(false);
            var eUtil = new EnvironmentUtil(environment);
            Assertions.assertNotNull(eUtil.asString("item.attr"));
            Assertions.assertEquals("", eUtil.asString("item.attr"));
        }
    }

    @Test
    @DisplayName("Deve validar asBool")
    void UI_asBool() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("true");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("false");
            Mockito.when(environment.containsProperty("item.attr-1")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr-2")).thenReturn(true);

            var eUtil = new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-1"));
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-2"));
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-x"));
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-x", false));
            Assertions.assertDoesNotThrow(() -> eUtil.asBool(null));
            Assertions.assertDoesNotThrow(() -> eUtil.asBool(null, false));

            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
            Assertions.assertFalse(eUtil.asBool("item.attr-x"));
            Assertions.assertFalse(eUtil.asBool("item.attr-x", false));
            Assertions.assertFalse(eUtil.asBool(null, false));
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("1");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("0");
            Mockito.when(environment.containsProperty("item.attr-1")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr-2")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-1"));
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-2"));
            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

        {//step 3
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("t");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("f");
            Mockito.when(environment.containsProperty("item.attr-1")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr-2")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-1"));
            Assertions.assertDoesNotThrow(() -> eUtil.asBool("item.attr-2"));
            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

    }

    @Test
    @DisplayName("Deve validar asDouble")
    void UI_asDouble() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", 0D,
                    "item.attr-1", 1D,
                    "item.attr-2", 3.1D,
                    "item.attr-3", 1000000D,
                    "item.attr-4", 1000000.25D,
                    "item.attr-5", -1000000.25D

            );
            for (var entry : values.entrySet()) {
                String key = entry.getKey();
                var value = entry.getValue();
                Mockito.when(environment.getProperty(key)).thenReturn(String.valueOf(value));
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(() -> eUtil.asDouble(k));
                Assertions.assertDoesNotThrow(() -> eUtil.asDouble(k, 0));

                Assertions.assertEquals(eUtil.asDouble(k), v);
                Assertions.assertEquals(eUtil.asDouble(k, 0), v);
            }
        }

        {//step 2
            var eUtil = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtil.asDouble(null));
            Assertions.assertDoesNotThrow(() -> eUtil.asDouble("teste.1234"));
            Assertions.assertDoesNotThrow(() -> eUtil.asDouble("teste.1234", 0D));
            Assertions.assertDoesNotThrow(() -> eUtil.asDouble(null, 0D));

            Assertions.assertEquals(0D, eUtil.asDouble("teste.1234"));
            Assertions.assertEquals(0D, eUtil.asDouble(null));
            Assertions.assertEquals(0D, eUtil.asDouble("teste.1234", 0));
            Assertions.assertEquals(0D, eUtil.asDouble(null, 0));

        }

    }

    @Test
    @DisplayName("Deve validar asInt")
    void UI_asInt() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", "0",
                    "item.attr-1", "1",
                    "item.attr-2", "3",
                    "item.attr-3", "1000000",
                    "item.attr-4", "-1000000",
                    "item.attr-5", ""

            );
            for (var entry : values.entrySet()) {
                String key = entry.getKey();
                var value = entry.getValue();
                Mockito.when(environment.getProperty(key)).thenReturn(String.valueOf(value));
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(() -> eUtil.asInt(k));
                Assertions.assertEquals(eUtil.asInt(k), PrimitiveUtil.toInt(v));
            }

        }
    }

    @Test
    @DisplayName("Deve validar asLong")
    void UI_asLong() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", 0L,
                    "item.attr-1", 1L,
                    "item.attr-2", 3L,
                    "item.attr-3", 1000000L,
                    "item.attr-4", -1000000L

            );
            for (var entry : values.entrySet()) {
                String key = entry.getKey();
                var value = entry.getValue();
                Mockito.when(environment.getProperty(key)).thenReturn(String.valueOf(value));
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertEquals(eUtil.asLong(k), v);
            }
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", "1",
                    "item.attr-1", "12334",
                    "item.attr-2", ""

            );
            for (Map.Entry<String, String> e : values.entrySet()) {
                String key = e.getKey();
                String value = e.getValue();
                Mockito.when(environment.getProperty(key)).thenReturn(String.valueOf(value));
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(() -> eUtil.asLong(k));
                Assertions.assertEquals(eUtil.asLong(k), PrimitiveUtil.toLong(v));
            }
        }
    }

    @Test
    @DisplayName("Deve validar asDate")
    void UI_asDate() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", LocalDate.of(1901, 1, 1).toString(),
                    "item.attr-1", LocalDate.of(2500, 1, 1).toString(),
                    "item.attr-2", LocalDate.now().toString()

            );
            for (var entry : values.entrySet()) {
                String key = entry.getKey();
                var value = entry.getValue();
                Mockito.when(environment.getProperty(key)).thenReturn(value);
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }
            Mockito.when(environment.containsProperty("")).thenReturn(false);
            Mockito.when(environment.containsProperty(null)).thenReturn(false);

            for (var entry : values.entrySet()) {
                var k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(() -> eUtil.asDate(k));
                Assertions.assertDoesNotThrow(() -> eUtil.asDate(k, LocalDate.now()));
                Assertions.assertDoesNotThrow(() -> eUtil.asDate(k, null));
                Assertions.assertEquals(eUtil.asDate(k).toString(), v);
                Assertions.assertEquals(eUtil.asDate("", LocalDate.now()), LocalDate.now());
                Assertions.assertNull(eUtil.asDate("", null));
                Assertions.assertNull(eUtil.asDate(null, null));
            }
        }
    }

    @Test
    @DisplayName("Deve validar asTime")
    void UI_asTime() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", LocalTime.of(1, 1, 1).toString(),
                    "item.attr-1", LocalTime.of(23, 59, 59).toString(),
                    "item.attr-2", LocalTime.now().toString()

            );
            for (var entry : values.entrySet()) {
                String key = entry.getKey();
                var value = entry.getValue();
                Mockito.when(environment.getProperty(key)).thenReturn(value);
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(() -> eUtil.asTime(k));
                Assertions.assertDoesNotThrow(() -> eUtil.asTime(k, null));
                Assertions.assertEquals(eUtil.asTime(k, null).toString(), v);
            }
        }
    }

    @Test
    @DisplayName("Deve validar asDateTime")
    void UI_asDateTime() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", LocalDateTime.of(LocalDate.of(2500, 1, 1), LocalTime.of(1, 1, 1)).toString(),
                    "item.attr-1", LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.now()).toString(),
                    "item.attr-2", LocalDateTime.now().toString()

            );
            for (var entry : values.entrySet()) {
                String key = entry.getKey();
                var value = entry.getValue();
                Mockito.when(environment.getProperty(key)).thenReturn(value);
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(() -> eUtil.asDateTime(k));
                Assertions.assertDoesNotThrow(() -> eUtil.asDateTime(k, null));
                Assertions.assertEquals(eUtil.asDateTime(k).toString(), v);
                Assertions.assertEquals(eUtil.asDateTime(k, null).toString(), v);
            }
        }
    }

    @Test
    @DisplayName("Deve validar asEnum")
    void UI_asEnum() {
        enum TypeTest {
            enumA, enumB, enumC
        }
        {//step 1

            var environment = Mockito.mock(Environment.class);
            for (var e : TypeTest.values()) {
                var v = e.toString();
                var k = String.format("attr.%s", e.toString().toLowerCase());
                Mockito.when(environment.getProperty(k)).thenReturn(v);
                Mockito.when(environment.containsProperty(k)).thenReturn(true);
            }
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil = new EnvironmentUtil(environment);

            Assertions.assertDoesNotThrow(() -> eUtil.asEnum("item.attr.ZZZ", TypeTest.class));
            Assertions.assertDoesNotThrow(() -> eUtil.asEnum("item.attr.ZZZ", Object.class));
            Assertions.assertDoesNotThrow(() -> eUtil.asEnum("item.attr.ZZZ", null));
            Assertions.assertDoesNotThrow(() -> eUtil.asEnum("item.attr.ZZZ", null, TypeTest.enumA));
            Assertions.assertDoesNotThrow(() -> eUtil.asEnum("item.attr.ZZZ", TypeTest.class, TypeTest.enumA));

            Assertions.assertNull(eUtil.asEnum("item.attr.ZZZ", TypeTest.class));
            Assertions.assertNull(eUtil.asEnum("item.attr.ZZZ", Object.class));
            Assertions.assertNull(eUtil.asEnum("item.attr.ZZZ", null));
            Assertions.assertEquals(TypeTest.enumA, eUtil.asEnum("item.attr.ZZZ", null, TypeTest.enumA));
            Assertions.assertEquals(TypeTest.enumA, eUtil.asEnum("item.attr.ZZZ", TypeTest.class, TypeTest.enumA));

            for (var e : TypeTest.values()) {
                var k = String.format("attr.%s", e.toString().toLowerCase());
                Assertions.assertEquals(eUtil.asEnum(k, TypeTest.class), e);
            }
        }
    }

    @Test
    @DisplayName("Deve validar asEnums")
    void UI_asEnums() {
        enum TypeTest {
            enumA, enumB, enumC
        }

        {//step 1
            var values = new StringBuilder();
            for (var e : TypeTest.values())
                values
                        .append(e.name())
                        .append(",");

            for (var eAttr : List.of(values.toString(), values.toString().toLowerCase(), values.toString().toUpperCase())) {
                var environment = Mockito.mock(Environment.class);
                Mockito.when(environment.getProperty("item.attr")).thenReturn(eAttr);
                Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
                Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
                var eUtil = new EnvironmentUtil(environment);

                Assertions.assertDoesNotThrow(() -> eUtil.asEnums("item.attr.ZZZ", TypeTest.class, List.of(TypeTest.enumA)).isEmpty());
                Assertions.assertDoesNotThrow(() -> eUtil.asEnums("item.attr.ZZZ", Object.class, List.of(TypeTest.enumB)).isEmpty());
                Assertions.assertDoesNotThrow(() -> eUtil.asEnums("item.attr.ZZZ", null, List.of(TypeTest.enumC)).isEmpty());

                Assertions.assertTrue(eUtil.asEnums("item.attr.ZZZ", TypeTest.class).isEmpty());
                Assertions.assertTrue(eUtil.asEnums("item.attr.ZZZ", Object.class).isEmpty());
                Assertions.assertTrue(eUtil.asEnums("item.attr.ZZZ", null).isEmpty());


                Assertions.assertDoesNotThrow(() -> eUtil.asEnums("item.attr.ZZZ", TypeTest.class, List.of(TypeTest.enumA)).isEmpty());
                Assertions.assertDoesNotThrow(() -> eUtil.asEnums("item.attr.ZZZ", Object.class, List.of(TypeTest.enumB)).isEmpty());
                Assertions.assertDoesNotThrow(() -> eUtil.asEnums("item.attr.ZZZ", null, List.of(TypeTest.enumC)).isEmpty());

                Assertions.assertFalse(eUtil.asEnums("item.attr.ZZZ", TypeTest.class, List.of(TypeTest.enumA)).isEmpty());
                Assertions.assertFalse(eUtil.asEnums("item.attr.ZZZ", Object.class, List.of(TypeTest.enumB)).isEmpty());
                Assertions.assertFalse(eUtil.asEnums("item.attr.ZZZ", null, List.of(TypeTest.enumC)).isEmpty());


                var value = eUtil.asEnums("item.attr", TypeTest.class);

                Assertions.assertNotNull(value);
                Assertions.assertEquals(value.size(), TypeTest.values().length);
                Assertions.assertTrue(value.contains(TypeTest.enumA));
                Assertions.assertTrue(value.contains(TypeTest.enumB));
                Assertions.assertTrue(value.contains(TypeTest.enumC));
            }

        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(",,,,");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var value = eUtil.asEnums("item.attr", TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(0L, value.size());
        }

        {//step 3
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(", ," + TypeTest.enumA.name() + ",,");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var value = eUtil.asEnums("item.attr", TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(1, value.size());
            Assertions.assertTrue(value.contains(TypeTest.enumA));

        }

        {//step 3
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn("dSS, ," + TypeTest.enumA.name() + ",h1h1h,");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var value = eUtil.asEnums("item.attr", TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(1L,value.size());
            Assertions.assertTrue(value.contains(TypeTest.enumA));
        }
    }

    @Test
    @DisplayName("Deve validar asListOfString")
    void UI_asListOfString() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("itemA,itemB,itemC");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);

            var eUtil = new EnvironmentUtil(environment);

            Assertions.assertTrue(eUtil.asListOfString("item.attr.ZZZ", null).isEmpty());
            var list = eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(3L, list.size());
            Assertions.assertTrue(list.contains("itemA"));
            Assertions.assertTrue(list.contains("itemB"));
            Assertions.assertTrue(list.contains("itemC"));
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(0L, list.size());

            Assertions.assertDoesNotThrow(() -> eUtil.asListOfString("item.attr.ZZZ", List.of("ccc")).isEmpty());
            Assertions.assertTrue(eUtil.asListOfString("item.attr.ZZZ").isEmpty());
            Assertions.assertTrue(eUtil.asListOfString("item.attr.ZZZ", null).isEmpty());
            Assertions.assertFalse(eUtil.asListOfString("item.attr.ZZZ", List.of("bbb")).isEmpty());

        }

        {//step 3
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(0L, list.size());
        }

        {//step 4
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(1L, list.size());
            Assertions.assertTrue(list.contains("itemC"));
        }
    }

    @Test
    @DisplayName("Deve validar asListOfLong")
    void UI_asListOfLong() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil = new EnvironmentUtil(environment);

            Assertions.assertTrue(eUtil.asListOfLong("item.attr.ZZZ", null).isEmpty());
            var list = eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(3L, list.size());
            Assertions.assertTrue(list.contains(1L));
            Assertions.assertTrue(list.contains(2L));
            Assertions.assertTrue(list.contains(3L));
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(0L, list.size());

            Assertions.assertDoesNotThrow(() -> eUtil.asListOfLong("item.attr.ZZZ", List.of(0L, 1L, 2L)).isEmpty());
            Assertions.assertTrue(eUtil.asListOfLong("item.attr.ZZZ", null).isEmpty());
            Assertions.assertDoesNotThrow(() -> eUtil.asListOfLong("item.attr.ZZZ", List.of(0L, 1L, 2L)).isEmpty());
            Assertions.assertFalse(eUtil.asListOfLong("item.attr.ZZZ", List.of(0L, 1L, 2L)).isEmpty());
        }

        {//step 3
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(0L, list.size());
        }

        {//step 4
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(3L, list.size());
            Assertions.assertTrue(list.contains(1L));
            Assertions.assertTrue(list.contains(2L));
            Assertions.assertTrue(list.contains(3L));
        }
    }

    @Test
    @DisplayName("Deve validar asListOfInt")
    void UI_asListOfInt() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil = new EnvironmentUtil(environment);
            Assertions.assertTrue(eUtil.asListOfInt("item.attr.ZZZ", null).isEmpty());
            var list = eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals( 3L, list.size());
            Assertions.assertTrue(list.contains(1));
            Assertions.assertTrue(list.contains(2));
            Assertions.assertTrue(list.contains(3));
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(0L, list.size());

            Assertions.assertDoesNotThrow(() -> eUtil.asListOfLong("item.attr.ZZZ", List.of(0L, 1L, 2L)).isEmpty());
            Assertions.assertTrue(eUtil.asListOfLong("item.attr.ZZZ", null).isEmpty());
            Assertions.assertDoesNotThrow(() -> eUtil.asListOfLong("item.attr.ZZZ", List.of(0L, 1L, 2L)).isEmpty());
            Assertions.assertFalse(eUtil.asListOfLong("item.attr.ZZZ", List.of(0L, 1L, 2L)).isEmpty());
        }

        {//step 3
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(0L, list.size());
        }

        {//step 4
            var environment = Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil = new EnvironmentUtil(environment);
            var list = eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals( 3L, list.size());
            Assertions.assertTrue(list.contains(1));
            Assertions.assertTrue(list.contains(2));
            Assertions.assertTrue(list.contains(3));
        }
    }

    @Test
    @DisplayName("Deve validar coleções")
    void UI_check_collections() {
        ConfigurableEnvironment environment = Mockito.mock(ConfigurableEnvironment.class);
        {
            Map<String, Object> sourceMap = new HashMap<>();
            sourceMap.put("app.name", "app-name");
            sourceMap.put("app.version", "app-version");

            PropertySource<Map<String, Object>> mockPropertySource = new PropertySource<>("mock", sourceMap) {
                @Override
                public Object getProperty(@SuppressWarnings("NullableProblems") String name) {
                    return sourceMap.get(name);
                }
            };
            Mockito.when(environment.getPropertySources()).thenReturn(new MutablePropertySources());
            environment.getPropertySources().addLast(mockPropertySource);
        }

        var eUtil = new EnvironmentUtil(environment);
        Assertions.assertDoesNotThrow(eUtil::asMap);
        Assertions.assertDoesNotThrow(eUtil::asList);


        Assertions.assertFalse(eUtil.asMap().isEmpty());
        Assertions.assertFalse(eUtil.asList().isEmpty());
    }

}
