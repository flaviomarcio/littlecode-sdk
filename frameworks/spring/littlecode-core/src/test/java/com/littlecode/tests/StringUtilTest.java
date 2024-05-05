package com.littlecode.tests;

import com.littlecode.parsers.StringUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

//ref
// https://textedit.tools/snakecase
// https://textedit.tools/camelcase
@Slf4j
@ExtendWith(MockitoExtension.class)
public class StringUtilTest {

    public static List<String> REPLACE_STRING = List.of(" ", "__", "+", "-");

    @Test
    @DisplayName("Deve validar formatadores")
    public void UT_CAMEL_CASE_FORMAT() {
        List<Check> checkItems =
                List.of(
                        Check.builder().camelCase("testName").snakeCase("test_name")
                                .checkList(List.of("testName", "TEST_NAME", "TEST__NAME", "test_name", "_TEST_name", "test_name_", " test__name ")).build(),
                        Check.builder().camelCase("test1Name").snakeCase("test1_name")
                                .checkList(List.of("test1Name", "TEST1_NAME", "TEST__1NAME", "test1name", "_TEST_1name", "test1_name_", " test_1_name ")).build(),
                        Check.builder().camelCase("m1TEst1234Name").snakeCase("m1_t_est1234_name")
                                .checkList(List.of("M_1tEst1234Name", "M1t_est1234Name", "M_1t_Est1234Name", "__M_1t_Est1234Name")).build(),
                        Check.builder().camelCase("1234Name").snakeCase("1234_name")
                                .checkList(List.of("1234_NAME", "1234__NAME", "1234_name", "_1234_name", "1234_name_", " 1234__name ")).build(),
                        Check.builder().camelCase("aa1234Name").snakeCase("aa1234_name")
                                .checkList(List.of("AA1234_NAME", "Aa1234__NAME", "AA1234_name", "aa_1234_name", " aa1234__name ")).build(),
                        Check.builder().camelCase("aA1234Name").snakeCase("a_a1234_name")
                                .checkList(List.of("a_a_1234_name_", "a_a_1234_NAME_", "_A_A_1234_NAME_")).build(),
                        Check.builder().camelCase("test").snakeCase("test")
                                .checkList(List.of("_test_", "_test", "_test")).build(),
                        Check.builder().camelCase("srcMd5").snakeCase("src_md5")
                                .checkList(List.of("_src_md5_", "src_md5_", "_src_md5")).build(),
                        Check.builder().camelCase("hashSrcMd5").snakeCase("hash_src_md5")
                                .checkList(List.of("_hash_src_md5_", "hash_src_md5_", "_hash_src_md5")).build()

                );


        checkItems.forEach(check ->
                {
                    List<String> checkList = new ArrayList<>();
                    checkList.add(check.camelCase);
                    REPLACE_STRING.forEach(replace ->
                            check.checkList
                                    .forEach(s ->
                                            checkList.add(s.replace(StringUtil.SNAKE_CASE_SEPARATOR, replace))
                                    )
                    );
                    checkList.forEach(
                            sCheck ->
                            {
                                var stringUtil = StringUtil.target(sCheck);
                                Assertions.assertEquals(stringUtil.getTarget(), sCheck);

                                Assertions.assertEquals(StringUtil.toCamelCase(sCheck), check.camelCase);
                                Assertions.assertEquals(stringUtil.toCamelCase(), check.camelCase);
                                Assertions.assertTrue(StringUtil.target(stringUtil.toCamelCase()).isCamelCase());

                                Assertions.assertEquals(StringUtil.toSnakeCase(sCheck), check.snakeCase);
                                Assertions.assertEquals(stringUtil.toSnakeCase(), check.snakeCase);
                                Assertions.assertTrue(StringUtil.target(stringUtil.toSnakeCase()).isSnakeCase());
                            }
                    );
                }
        );

        Assertions.assertEquals(StringUtil.target("TEST").toWord(), "Test");


    }

    @Getter
    @Builder
    private static class Check {
        String camelCase;
        String snakeCase;
        List<String> checkList;
    }

}
