package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.util.EnvironmentUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class EnvironmentUtilTest {

    @Test
    public void UI_envValue() {
        {//step 1
            var environment = Mockito.mock(Environment.class);
            Map.of("item.attr", "itemA,itemB,itemC")
                    .forEach((k, v) -> {
                        Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
                        Mockito.when(environment.getProperty("item.attr")).thenReturn("itemA,itemB,itemC");
                    });

            Assertions.assertThrows(FrameworkException.class, () -> new EnvironmentUtil(null));
            Assertions.assertDoesNotThrow(() -> new EnvironmentUtil(environment));
            var eUtil = new EnvironmentUtil(environment);

            UtilCoreConfig.setEnvironment(null);
            Assertions.assertThrows(FrameworkException.class, () -> new EnvironmentUtil());
            UtilCoreConfig.setEnvironment(environment);
            Assertions.assertDoesNotThrow(() -> new EnvironmentUtil());

            Assertions.assertNotNull(eUtil.getEnvironment());

            Assertions.assertDoesNotThrow(() -> eUtil.envValue(""));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue(" "));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue(null));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr"));
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr"), "0,1,2");
            Assertions.assertDoesNotThrow(() -> eUtil.envValue("item.attr"), "itemA,itemB,itemC");

            Assertions.assertNull(eUtil.envValue(""));
            Assertions.assertNull(eUtil.envValue(" "));
            Assertions.assertNull(eUtil.envValue(null));

            Assertions.assertNull(eUtil.envValue("item.attrx"));
            Assertions.assertNotNull(eUtil.envValue("item.attr"), "itemA,itemB,itemC");
        }
    }


    @Test
    public void UI_asString() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(" itemA,itemB,itemC ");
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertNotNull(eUtil.asString("item.attr"));
            Assertions.assertEquals(eUtil.asString("item.attr"),"itemA,itemB,itemC");
        }
        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(null);
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertNull(eUtil.asString("item.attr"));
            Assertions.assertNotNull(eUtil.asString("item.attr", "teste"));
            Assertions.assertEquals(eUtil.asString("item.attr", ""), "");
            Assertions.assertEquals(eUtil.asString("item.attr", "teste"), "teste");
        }
    }

    @Test
    public void UI_asBool() {

        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asBool("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asBool("key.test", true));
            Assertions.assertEquals(eUtilSingle.asBool("key.test"), false);
            Assertions.assertEquals(eUtilSingle.asBool("key.test", true), true);
            Assertions.assertEquals(eUtilSingle.asBool("key.test", false), false);
        }

        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("true");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("false");
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("1");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("0");
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("t");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("f");
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

    }

    @Test
    public void UI_asDouble() {
        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asDouble("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asDouble("key.test", 1005));
            Assertions.assertEquals(eUtilSingle.asDouble("key.test"), 0);
            Assertions.assertEquals(eUtilSingle.asDouble("key.test", 1005), 1005);
        }
        {//step 1
            var environment= Mockito.mock(Environment.class);
            var values= Map.of(
                    "item.attr-0",0D,
                    "item.attr-1",1D,
                    "item.attr-2",3.1,
                    "item.attr-3",1000000D,
                    "item.attr-4",1000000.25,
                    "item.attr-5",-1000000.25

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });

            values.forEach((k, v) -> {
                        var eUtil=new EnvironmentUtil(environment);
                        Assertions.assertEquals(eUtil.asDouble(k),v);
                    }
            );

        }
    }

    @Test
    public void UI_asInt() {

        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asInt("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asInt("key.test", 1005));
            Assertions.assertEquals(eUtilSingle.asInt("key.test"), 0);
            Assertions.assertEquals(eUtilSingle.asInt("key.test", 1005), 1005);
        }

        {//step 1
            var environment= Mockito.mock(Environment.class);
            var values= Map.of(
                    "item.attr-0",0,
                    "item.attr-1",1,
                    "item.attr-2",3,
                    "item.attr-3",1000000,
                    "item.attr-4",-1000000

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });

            values.forEach((k, v) -> {
                        var eUtil=new EnvironmentUtil(environment);
                        Assertions.assertEquals(eUtil.asInt(k),v);
                    }
            );

        }
    }

    @Test
    public void UI_asLong() {
        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asLong("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asLong("key.test", 1005));
            Assertions.assertEquals(eUtilSingle.asLong("key.test"), 0);
            Assertions.assertEquals(eUtilSingle.asLong("key.test", 1005), 1005);
        }
        {//step 1
            var environment= Mockito.mock(Environment.class);
            var values= Map.of(
                    "item.attr-0",0L,
                    "item.attr-1",1L,
                    "item.attr-2",3L,
                    "item.attr-3",1000000L,
                    "item.attr-4",-1000000L

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });


            values.forEach((k, v) -> {
                        var eUtil=new EnvironmentUtil(environment);
                        Assertions.assertEquals(eUtil.asLong(k),v);
                    }
            );

        }
    }

    @Test
    public void UI_asDate() {

        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asDate("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asDate("key.test", LocalDate.now()));
            Assertions.assertNull(eUtilSingle.asDate("key.test"));
            Assertions.assertNotNull(eUtilSingle.asDate("key.test", LocalDate.now()));
            Assertions.assertEquals(eUtilSingle.asDate("key.test", LocalDate.now()), LocalDate.now());
        }

        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", LocalDate.of(1901, 1, 1).toString(),
                    "item.attr-1", LocalDate.of(2500, 1, 1).toString(),
                    "item.attr-2", LocalDate.now().toString()

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });

            values.forEach((k, v) -> {
                        var eUtil = new EnvironmentUtil(environment);
                        Assertions.assertEquals(eUtil.asDate(k), LocalDate.parse(v));
                    }
            );
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);

            var values = Map.of(
                    "item.attr-0", "1901-1-1",
                    "item.attr-1", "2500-1-1",
                    "item.attr-2", ""

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });

            for (Map.Entry<String, String> entry : values.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(() -> eUtil.asDate(k), v);
                Assertions.assertNotEquals(eUtil.asDate(k), v);
            }
        }
    }

    @Test
    public void UI_asTime() {

        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asTime("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asTime("key.test", LocalTime.now()));
            Assertions.assertNull(eUtilSingle.asTime("key.test"));
            Assertions.assertNotNull(eUtilSingle.asTime("key.test", LocalTime.now()));
            var tm = LocalTime.now();
            Assertions.assertEquals(eUtilSingle.asTime("key.test", tm), tm);
        }

        {//step 1
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", LocalTime.of(1, 1, 1).toString(),
                    "item.attr-1", LocalTime.of(23, 59, 59).toString(),
                    "item.attr-2", LocalTime.now().toString()

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });

            values.forEach((k, v) -> {
                        var eUtil = new EnvironmentUtil(environment);
                        Assertions.assertEquals(eUtil.asTime(k), LocalTime.parse(v));
                    }
            );
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", "1.1.1",
                    "item.attr-1", "2.2",
                    "item.attr-2", ""

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });

            values.forEach((k, v) -> {
                        var eUtil = new EnvironmentUtil(environment);
                        Assertions.assertNull(eUtil.asTime(k));
                    }
            );
        }
    }

    @Test
    public void UI_asDateTime() {

        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asDateTime("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asDateTime("key.test", LocalDateTime.now()));
            Assertions.assertNull(eUtilSingle.asDateTime("key.test"));
            Assertions.assertNotNull(eUtilSingle.asDateTime("key.test", LocalDateTime.now()));
            var dt = LocalDateTime.now();
            Assertions.assertEquals(eUtilSingle.asDateTime("key.test", dt), dt);
        }


        {//step 1
            var dtLst = Map.of(
                    "item.attr-0", LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(23, 59, 59, 999000001)),
                    "item.attr-1", LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(23, 59, 59)),
                    "item.attr-2", LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(23, 59)),
                    "item.attr-3", LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(23, 0, 0))
            );

            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", "1901-01-01T23:59:59.999000001",
                    "item.attr-1", "1901-01-01T23:59:59",
                    "item.attr-2", "1901-01-01T23:59",
                    "item.attr-3", "1901-01-01T23"
            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(v);
            });

            int iDt = 0;
            for (Map.Entry<String, String> entry : values.entrySet()) {
                String k = entry.getKey();
                var eUtil = new EnvironmentUtil(environment);
                var vConvert = eUtil.asDateTime(k);
                var vCheck = dtLst.get(k);
                Assertions.assertEquals(vConvert, vCheck);
                ++iDt;
            }
        }

        {//step 2
            var environment = Mockito.mock(Environment.class);
            var values = Map.of(
                    "item.attr-0", "1.1.1",
                    "item.attr-1", "2.2",
                    "item.attr-2", ""

            );
            values.forEach((k, v) -> {
                Mockito.when(environment.getProperty(k)).thenReturn(String.valueOf(v));
            });

            for (Map.Entry<String, String> entry : values.entrySet()) {
                String k = entry.getKey();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertNull(eUtil.asDateTime(k));
            }
        }
    }

    @Test
    public void UI_asEnums() {
        enum TypeTest{
            enumA,enumB,enumC
        }

        {//step 1
            var values=new StringBuilder();
            for(var e:TypeTest.values())
                values
                        .append(e.name())
                        .append(",");

            for(var eAttr: List.of(values.toString(), values.toString().toLowerCase(), values.toString().toUpperCase())){
                var environment= Mockito.mock(Environment.class);
                Mockito.when(environment.getProperty("item.attr")).thenReturn(eAttr);
                var eUtil=new EnvironmentUtil(environment);
                var value=eUtil.asEnums("item.attr",TypeTest.class);
                Assertions.assertNotNull(value);
                Assertions.assertEquals(value.size(),TypeTest.values().length);
                Assertions.assertTrue(value.contains(TypeTest.enumA));
                Assertions.assertTrue(value.contains(TypeTest.enumB));
                Assertions.assertTrue(value.contains(TypeTest.enumC));
            }

        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(",,,,");
            var eUtil=new EnvironmentUtil(environment);
            var value=eUtil.asEnums("item.attr",TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(value.size(),0);
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(", ,"+TypeTest.enumA.name()+",,");
            var eUtil=new EnvironmentUtil(environment);
            var value=eUtil.asEnums("item.attr",TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(value.size(),1);
            Assertions.assertTrue(value.contains(TypeTest.enumA));

        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn("dSS, ,"+TypeTest.enumA.name()+",h1h1h,");
            var eUtil=new EnvironmentUtil(environment);
            var value=eUtil.asEnums("item.attr",TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(value.size(),1);
            Assertions.assertTrue(value.contains(TypeTest.enumA));
        }
    }

    @Test
    public void UI_asListOfString() {
        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asListOfString("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asListOfString("key.test", List.of("itemA", "itemB", "itemC")));
            Assertions.assertTrue(eUtilSingle.asListOfString("key.test").isEmpty());
            Assertions.assertNotNull(eUtilSingle.asListOfString("key.test", List.of("itemA", "itemB", "itemC")));
            var list = eUtilSingle.asListOfString("item.listx", List.of("itemA", "itemB", "itemC"));
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(), 3);
            Assertions.assertTrue(list.contains("itemA"));
            Assertions.assertTrue(list.contains("itemB"));
            Assertions.assertTrue(list.contains("itemC"));
        }

        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("itemA,itemB,itemC");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains("itemA"));
            Assertions.assertTrue(list.contains("itemB"));
            Assertions.assertTrue(list.contains("itemC"));

            list = eUtil.asListOfString("item.listxx");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(), 0);
            Assertions.assertFalse(list.contains("itemA"));
            Assertions.assertFalse(list.contains("itemB"));
            Assertions.assertFalse(list.contains("itemC"));


        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 4
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),1);
            Assertions.assertTrue(list.contains("itemC"));
        }
    }

    @Test
    public void UI_asListOfLong() {
        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asListOfLong("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asListOfLong("key.test", List.of(1L, 2L, 3L)));
            Assertions.assertTrue(eUtilSingle.asListOfLong("key.test").isEmpty());
            Assertions.assertNotNull(eUtilSingle.asListOfLong("key.test", List.of(1L, 2L, 3L)));
            var list = eUtilSingle.asListOfLong("item.listx", List.of(1L, 2L, 3L));
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(), 3);
            Assertions.assertTrue(list.contains(1L));
            Assertions.assertTrue(list.contains(2L));
            Assertions.assertTrue(list.contains(3L));
        }

        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("1,2,3");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains(1L));
            Assertions.assertTrue(list.contains(2L));
            Assertions.assertTrue(list.contains(3L));

            list = eUtil.asListOfLong("item.listxx");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(), 0);
            Assertions.assertFalse(list.contains(1L));
            Assertions.assertFalse(list.contains(2L));
            Assertions.assertFalse(list.contains(3L));
        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 4
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("9,1,5");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains(5L));
            Assertions.assertFalse(list.contains(2L));
        }
    }

    @Test
    public void UI_asListOfInt() {
        {//step 0
            var eUtilSingle = new EnvironmentUtil(Mockito.mock(Environment.class));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asListOfInt("key.test"));
            Assertions.assertDoesNotThrow(() -> eUtilSingle.asListOfInt("key.test", List.of(1, 2, 3)));
            Assertions.assertTrue(eUtilSingle.asListOfInt("key.test").isEmpty());
            Assertions.assertNotNull(eUtilSingle.asListOfInt("key.test", List.of(1, 2, 3)));
            var list = eUtilSingle.asListOfInt("item.listx", List.of(1, 2, 3));
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(), 3);
            Assertions.assertTrue(list.contains(1));
            Assertions.assertTrue(list.contains(2));
            Assertions.assertTrue(list.contains(3));
        }

        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("1,2,3");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains(1));
            Assertions.assertTrue(list.contains(2));
            Assertions.assertTrue(list.contains(3));
        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

    }

}
