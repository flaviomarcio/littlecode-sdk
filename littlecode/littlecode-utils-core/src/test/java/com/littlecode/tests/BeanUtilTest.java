package com.littlecode.tests;

import com.littlecode.util.BeanUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BeanUtilTest {
    private static final List<String> listString = List.of("0", "1", "2", "3");
    private static final List<Integer> listInt = List.of(0, 1, 2, 3);
    private static final List<Long> listLong = List.of(0L, 1L, 2L, 3L);
    private static final List<Double> listDouble = List.of(0.1, 1.2, 2.3, 3.4);
    private static final String BEAN_STRING = "beanStringTest";
    private static final String BEAN_STRING_VALUE = "A";
    private static BeanUtil beanUtil;

    @BeforeAll
    public static void before() {
        var context = Mockito.mock(ApplicationContext.class);
        Mockito.when(context.getBeanNamesForAnnotation(Builder.class)).thenReturn(new String[]{"1", "2"});
        Mockito.when(context.getBeanDefinitionNames()).thenReturn(new String[]{BEAN_STRING});
        Mockito.when(context.getBean("beanString", String.class)).thenReturn("A");
        Mockito.when(context.getBean("beanDouble", Double.class)).thenReturn(0.5789);
        Mockito.when(context.getBean("beanLong", Long.class)).thenReturn(628987L);
        Mockito.when(context.getBean("beanInt", Integer.class)).thenReturn(142657);
        Mockito.when(context.getBean("beanDate", LocalDate.class)).thenReturn(LocalDate.MAX);
        Mockito.when(context.getBean("beanTime", LocalTime.class)).thenReturn(LocalTime.MAX);
        Mockito.when(context.getBean("beanDateTime", LocalDateTime.class)).thenReturn(LocalDateTime.of(LocalDate.MAX, LocalTime.MAX));
        Mockito.when(context.getBean("beanBoolean", Boolean.class)).thenReturn(false);
        Mockito.when(context.getBean("beanListString", List.class)).thenReturn(listString);
        Mockito.when(context.getBean("beanListInt", List.class)).thenReturn(listInt);
        Mockito.when(context.getBean("beanListLong", List.class)).thenReturn(listLong);
        Mockito.when(context.getBean("beanListDouble", List.class)).thenReturn(listDouble);
        Mockito.when(context.getBean("beanList", List.class)).thenReturn(listString);
        Mockito.when(context.getBean(BEAN_STRING, String.class)).thenReturn(BEAN_STRING_VALUE);
        beanUtil = BeanUtil.of(context);
    }

    @Test
    public void UT_CHECK() {
        Assertions.assertNotNull(beanUtil.getBeanNames());
        Assertions.assertFalse(beanUtil.asAnnotation(Builder.class).isEmpty());
        Assertions.assertFalse(beanUtil.getBeanNames().isEmpty());
        Assertions.assertEquals(beanUtil.bean(BEAN_STRING).bean(), BEAN_STRING);
        Assertions.assertEquals(beanUtil.bean("beanString").as(String.class), "A");
        Assertions.assertEquals(beanUtil.bean("beanDouble").as(Double.class), 0.5789);
        Assertions.assertEquals(beanUtil.bean("beanLong").as(Long.class), 628987L);
        Assertions.assertEquals(beanUtil.bean("beanInt").as(Integer.class), 142657);
        Assertions.assertEquals(beanUtil.bean("beanDate").as(LocalDate.class), LocalDate.MAX);
        Assertions.assertEquals(beanUtil.bean("beanTime").as(LocalTime.class), LocalTime.MAX);
        Assertions.assertEquals(beanUtil.bean("beanDateTime").as(LocalDateTime.class), LocalDateTime.of(LocalDate.MAX, LocalTime.MAX));
        Assertions.assertEquals(beanUtil.bean("beanBoolean").as(Boolean.class), false);
        Assertions.assertEquals(beanUtil.bean("beanListString").as(List.class), listString);
        Assertions.assertEquals(beanUtil.bean("beanListInt").as(List.class), listInt);
        Assertions.assertEquals(beanUtil.bean("beanListLong").as(List.class), listLong);
        Assertions.assertEquals(beanUtil.bean("beanListDouble").as(List.class), listDouble);
    }
}
