package com.littlecode.tests;

import com.littlecode.util.EnvironmentUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
public class EnvironmentUtilTest {
    @Test
    public void UI_asString() {

    }



    @Test
    public void UI_asEnums() {

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
