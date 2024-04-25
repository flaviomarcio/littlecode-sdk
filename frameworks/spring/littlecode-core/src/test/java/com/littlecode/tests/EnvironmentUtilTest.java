package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.EnvironmentUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class EnvironmentUtilTest {

    @Test
    public void UI_Constructor() {
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
    public void UI_envValue() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(" itemA,itemB,itemC ");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.x")).thenReturn(false);
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(()->eUtil.envValue("item.attr.x"));
            Assertions.assertDoesNotThrow(()->eUtil.envValue("item.attr"));
            Assertions.assertDoesNotThrow(()->eUtil.envValue("item.attr"),"itemA,itemB,itemC");

            Assertions.assertNull(eUtil.envValue("item.attr.x"));
            Assertions.assertNotNull(eUtil.envValue("item.attr"));
            Assertions.assertEquals(eUtil.envValue("item.attr"),"itemA,itemB,itemC");
        }
    }

    @Test
    public void UI_asString() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(" itemA,itemB,itemC ");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.x")).thenReturn(false);
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(()->eUtil.asString("item.attr.x"));
            Assertions.assertDoesNotThrow(()->eUtil.asString("item.attr"));
            Assertions.assertDoesNotThrow(()->eUtil.asString("item.attr"),"itemA,itemB,itemC");

            Assertions.assertNotNull(eUtil.asString("item.attr.x"));
            Assertions.assertNotNull(eUtil.asString("item.attr"));
            Assertions.assertEquals(eUtil.asString("item.attr"),"itemA,itemB,itemC");
        }
        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(false);
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertNotNull(eUtil.asString("item.attr"));
            Assertions.assertEquals(eUtil.asString("item.attr"),"");
        }
    }

    @Test
    public void UI_asBool() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("true");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("false");
            Mockito.when(environment.containsProperty("item.attr-1")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr-2")).thenReturn(true);

            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(()->eUtil.asBool("item.attr-1"));
            Assertions.assertDoesNotThrow(()->eUtil.asBool("item.attr-2"));

            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("1");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("0");
            Mockito.when(environment.containsProperty("item.attr-1")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr-2")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(()->eUtil.asBool("item.attr-1"));
            Assertions.assertDoesNotThrow(()->eUtil.asBool("item.attr-2"));
            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr-1")).thenReturn("t");
            Mockito.when(environment.getProperty("item.attr-2")).thenReturn("f");
            Mockito.when(environment.containsProperty("item.attr-1")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr-2")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertDoesNotThrow(()->eUtil.asBool("item.attr-1"));
            Assertions.assertDoesNotThrow(()->eUtil.asBool("item.attr-2"));
            Assertions.assertTrue(eUtil.asBool("item.attr-1"));
            Assertions.assertFalse(eUtil.asBool("item.attr-2"));
        }

    }

    @Test
    public void UI_asDouble() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            var values= Map.of(
                    "item.attr-0","0",
                    "item.attr-1","1",
                    "item.attr-2","3.1",
                    "item.attr-3","1000000D",
                    "item.attr-4","1000000.25",
                    "item.attr-5","-1000000.25"

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
                Assertions.assertEquals(eUtil.asDouble(k), PrimitiveUtil.toDouble(v));
            }

        }
    }

    @Test
    public void UI_asInt() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            var values= Map.of(
                    "item.attr-0","0",
                    "item.attr-1","1",
                    "item.attr-2","3",
                    "item.attr-3","1000000",
                    "item.attr-4","-1000000",
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
                Assertions.assertDoesNotThrow(()->eUtil.asInt(k));
                Assertions.assertEquals(eUtil.asInt(k), PrimitiveUtil.toInt(v));
            }

        }
    }

    @Test
    public void UI_asLong() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            var values= Map.of(
                    "item.attr-0",0L,
                    "item.attr-1",1L,
                    "item.attr-2",3L,
                    "item.attr-3",1000000L,
                    "item.attr-4",-1000000L

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
                Assertions.assertDoesNotThrow(()->eUtil.asLong(k));
                Assertions.assertEquals(eUtil.asLong(k), PrimitiveUtil.toLong(v));
            }
        }
    }

    @Test
    public void UI_asDate() {
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

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(()->eUtil.asDate(k));
                Assertions.assertEquals(eUtil.asDate(k).toString(), v);
            }
        }
    }

    @Test
    public void UI_asTime() {
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
                Mockito.when(environment.getProperty(key)).thenReturn(String.valueOf(value));
                Mockito.when(environment.containsProperty(key)).thenReturn(true);
            }

            for (var entry : values.entrySet()) {
                String k = entry.getKey();
                var v = entry.getValue();
                var eUtil = new EnvironmentUtil(environment);
                Assertions.assertDoesNotThrow(()->eUtil.asTime(k));
                Assertions.assertEquals(eUtil.asTime(k).toString(), v);
            }
        }
    }

    @Test
    public void UI_asDateTime() {
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
                Assertions.assertDoesNotThrow(()->eUtil.asDateTime(k));
                Assertions.assertEquals(eUtil.asDateTime(k).toString(), v);
            }
        }
    }

    @Test
    public void UI_asEnums() {
        enum TypeTest{
            enumA,enumB,enumC
        }

        {//step 1

            var environment= Mockito.mock(Environment.class);
            for(var e:TypeTest.values()){
                var v=e.toString();
                var k=String.format("attr.%s",e.toString().toLowerCase());
                Mockito.when(environment.getProperty(k)).thenReturn(v);
                Mockito.when(environment.containsProperty(k)).thenReturn(true);
            }
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil=new EnvironmentUtil(environment);

            Assertions.assertNull(eUtil.asEnum("item.attr.ZZZ",TypeTest.class));
            Assertions.assertNull(eUtil.asEnum("item.attr.ZZZ",Object.class));
            Assertions.assertNull(eUtil.asEnum("item.attr.ZZZ",null));

            for(var e:TypeTest.values()){
                var k=String.format("attr.%s",e.toString().toLowerCase());
                Assertions.assertEquals(eUtil.asEnum(k,TypeTest.class),e);
            }
        }

        {//step 2
            var values=new StringBuilder();
            for(var e:TypeTest.values())
                values
                        .append(e.name())
                        .append(",");

            for(var eAttr: List.of(values.toString(), values.toString().toLowerCase(), values.toString().toUpperCase())){
                var environment= Mockito.mock(Environment.class);
                Mockito.when(environment.getProperty("item.attr")).thenReturn(eAttr);
                Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
                Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
                var eUtil=new EnvironmentUtil(environment);

                Assertions.assertDoesNotThrow(()-> eUtil.asEnums("item.attr.ZZZ",TypeTest.class,List.of(TypeTest.enumA)).isEmpty());
                Assertions.assertDoesNotThrow(()-> eUtil.asEnums("item.attr.ZZZ",Object.class,List.of(TypeTest.enumB)).isEmpty());
                Assertions.assertDoesNotThrow(()-> eUtil.asEnums("item.attr.ZZZ",null,List.of(TypeTest.enumC)).isEmpty());

                Assertions.assertTrue(eUtil.asEnums("item.attr.ZZZ",TypeTest.class).isEmpty());
                Assertions.assertTrue(eUtil.asEnums("item.attr.ZZZ",Object.class).isEmpty());
                Assertions.assertTrue(eUtil.asEnums("item.attr.ZZZ",null).isEmpty());


                Assertions.assertDoesNotThrow(()-> eUtil.asEnums("item.attr.ZZZ",TypeTest.class,List.of(TypeTest.enumA)).isEmpty());
                Assertions.assertDoesNotThrow(()-> eUtil.asEnums("item.attr.ZZZ",Object.class,List.of(TypeTest.enumB)).isEmpty());
                Assertions.assertDoesNotThrow(()-> eUtil.asEnums("item.attr.ZZZ",null,List.of(TypeTest.enumC)).isEmpty());

                Assertions.assertFalse(eUtil.asEnums("item.attr.ZZZ",TypeTest.class,List.of(TypeTest.enumA)).isEmpty());
                Assertions.assertFalse(eUtil.asEnums("item.attr.ZZZ",Object.class,List.of(TypeTest.enumB)).isEmpty());
                Assertions.assertFalse(eUtil.asEnums("item.attr.ZZZ",null,List.of(TypeTest.enumC)).isEmpty());


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
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var value=eUtil.asEnums("item.attr",TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(value.size(),0);
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn(", ,"+TypeTest.enumA.name()+",,");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var value=eUtil.asEnums("item.attr",TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(value.size(),1);
            Assertions.assertTrue(value.contains(TypeTest.enumA));

        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.attr")).thenReturn("dSS, ,"+TypeTest.enumA.name()+",h1h1h,");
            Mockito.when(environment.containsProperty("item.attr")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var value=eUtil.asEnums("item.attr",TypeTest.class);
            Assertions.assertNotNull(value);
            Assertions.assertEquals(value.size(),1);
            Assertions.assertTrue(value.contains(TypeTest.enumA));
        }
    }

    @Test
    public void UI_asListOfString() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("itemA,itemB,itemC");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);

            var eUtil=new EnvironmentUtil(environment);

            Assertions.assertTrue(eUtil.asListOfString("item.attr.ZZZ",null).isEmpty());
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains("itemA"));
            Assertions.assertTrue(list.contains("itemB"));
            Assertions.assertTrue(list.contains("itemC"));
        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);

            Assertions.assertDoesNotThrow(()-> eUtil.asListOfString("item.attr.ZZZ",List.of("ccc")).isEmpty());
            Assertions.assertTrue(eUtil.asListOfString("item.attr.ZZZ").isEmpty());
            Assertions.assertTrue(eUtil.asListOfString("item.attr.ZZZ",null).isEmpty());
            Assertions.assertFalse(eUtil.asListOfString("item.attr.ZZZ",List.of("bbb")).isEmpty());

        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 4
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfString("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),1);
            Assertions.assertTrue(list.contains("itemC"));
        }
    }

    @Test
    public void UI_asListOfLong() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil=new EnvironmentUtil(environment);

            Assertions.assertTrue(eUtil.asListOfLong("item.attr.ZZZ",null).isEmpty());
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains(1L));
            Assertions.assertTrue(list.contains(2L));
            Assertions.assertTrue(list.contains(3L));
        }

        {//step 2
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);

            Assertions.assertDoesNotThrow(()-> eUtil.asListOfLong("item.attr.ZZZ",List.of(0L,1L,2L)).isEmpty());
            Assertions.assertTrue(eUtil.asListOfLong("item.attr.ZZZ",null).isEmpty());
            Assertions.assertDoesNotThrow(()-> eUtil.asListOfLong("item.attr.ZZZ",List.of(0L,1L,2L)).isEmpty());
            Assertions.assertFalse(eUtil.asListOfLong("item.attr.ZZZ",List.of(0L,1L,2L)).isEmpty());
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 4
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfLong("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains(1L));
            Assertions.assertTrue(list.contains(2L));
            Assertions.assertTrue(list.contains(3L));
        }
    }

    @Test
    public void UI_asListOfInt() {
        {//step 1
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn("1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            Mockito.when(environment.containsProperty("item.attr.ZZZ")).thenReturn(false);
            var eUtil=new EnvironmentUtil(environment);
            Assertions.assertTrue(eUtil.asListOfInt("item.attr.ZZZ",null).isEmpty());
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
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);

            Assertions.assertDoesNotThrow(()-> eUtil.asListOfLong("item.attr.ZZZ",List.of(0L,1L,2L)).isEmpty());
            Assertions.assertTrue(eUtil.asListOfLong("item.attr.ZZZ",null).isEmpty());
            Assertions.assertDoesNotThrow(()-> eUtil.asListOfLong("item.attr.ZZZ",List.of(0L,1L,2L)).isEmpty());
            Assertions.assertFalse(eUtil.asListOfLong("item.attr.ZZZ",List.of(0L,1L,2L)).isEmpty());
        }

        {//step 3
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(",,,");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),0);
        }

        {//step 4
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,1,2,3");
            Mockito.when(environment.containsProperty("item.list")).thenReturn(true);
            var eUtil=new EnvironmentUtil(environment);
            var list=eUtil.asListOfInt("item.list");
            Assertions.assertNotNull(list);
            Assertions.assertEquals(list.size(),3);
            Assertions.assertTrue(list.contains(1));
            Assertions.assertTrue(list.contains(2));
            Assertions.assertTrue(list.contains(3));
        }
    }

}
