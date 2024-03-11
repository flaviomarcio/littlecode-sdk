package com.littlecode.tests;

import com.littlecode.util.EnvironmentUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EnvironmentUtilTest {
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
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,1,2,3");
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

        {//step 4
            var environment= Mockito.mock(Environment.class);
            Mockito.when(environment.getProperty("item.list")).thenReturn(", ,itemC,1,2,3");
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
