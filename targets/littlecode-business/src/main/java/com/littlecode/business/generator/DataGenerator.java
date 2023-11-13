package com.littlecode.business.generator;

import lombok.*;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataGenerator {
    private static final String[] EMAIL_NAMES = {"alice", "bob", "charlie", "david", "emma", "frank", "george", "harry", "irene"};
    private static final String[] EMAIL_DOMAINS = {"gmail.com", "yahoo.com", "hotmail.com", "example.com", "test.com"};

    private static final String[] PERSON_NAMES = {"Alice", "Bob", "Charlie", "David", "Emma", "Frank", "George", "Helen", "Irene"};
    private static final String[] PERSON_LAST_NAME = {"Smith", "Johnson", "Brown", "Lee", "Davis", "Wilson", "Evans", "Taylor", "Clark"};

    @Getter
    private final List<Object> records =new ArrayList<>();
    @Getter
    private int count;
    @Setter
    private Boolean maskInclude;
    @Setter
    private DataType dataType;
    @Getter
    @Setter
    private String[] names;
    @Getter
    @Setter
    private Function<DataGenerator,Object> maker;
    public static <T> List<T> generate(DataType dataType, int count, boolean maskInclude){

        DataType dataTypeLast=DataType.PersonPF;

        List<T> __return=new ArrayList<>();
        for(var i=0;i<count;i++){
            Object docStr=switch (dataType){
                case CPF -> generateCPF();
                case CNPJ -> generateCNPJ();
                case CNH -> generateCNH();
                case CHASSIS -> generateCHASSIS();
                case RG -> generateRG(maskInclude);
                case IE -> generateIE();
                case eMail -> generateEMAIL();
                case PhoneNumber -> generatePhoneNumber();
                case Names -> generatePersonName();
                case PersonPJ -> generatePersonPJ();
                case PersonPF -> generatePersonPF();
                case PersonPFAndPJ->(dataTypeLast.equals(DataType.PersonPF))
                        ?generatePersonPF()
                        :generatePersonPJ();
                default -> "";
            };

            if(dataType.equals(DataType.PersonPFAndPJ)){
                dataTypeLast=
                        dataTypeLast.equals(DataType.PersonPF)
                                ?DataType.PersonPJ
                                :DataType.PersonPF;
            }

            if(docStr.getClass().equals(String.class)){
                if(((String)docStr).isEmpty())
                    continue;
            }
            //noinspection unchecked
            __return.add((T)docStr);
        }
        return __return;
    }
    public DataGenerator clear(){
        this.records.clear();
        return this;
    }


    public DataGenerator count(int count){
        this.count=count;
        return this;
    }

    public Boolean getMaskInclude(){
        if(maskInclude==null)
            maskInclude=false;
        return maskInclude;
    }

    public DataGenerator maskInclude(Boolean maskInclude){
        this.maskInclude=maskInclude;
        return this;
    }

    public DataType getDataType(){
        if(this.maker!=null)
            return DataType.CUSTOM;
        return this.dataType;
    }

    public DataGenerator dataType(DataType dataType){
        this.dataType=dataType;
        return this;
    }

    public <T> List<T> generate() {
        //noinspection MismatchedQueryAndUpdateOfCollection
        List<T> list=new ArrayList<>();
        if(this.getMaker()!=null){
            for(var i=0;i<count;i++){
                @SuppressWarnings("unchecked")
                var o=(T) maker.apply(this);
                if(o!=null)
                    list.add(o);
            }
            return list;
        }
        //noinspection unchecked
        var lst= (List<T>) generate(this.getDataType(), this.getCount(), this.getMaskInclude());
        list.addAll(lst);
        return list;
    }

    public static String generatePhoneNumber() {
        Function<Integer, String> makeNumber = new Function<Integer, String>() {
            @Override
            public String apply(Integer len) {
                Random rand = new SecureRandom();
                rand.setSeed(LocalDate.now().toEpochDay());
                long e = (long) (rand.nextExponential() * 10000);
                long n = rand.nextLong();
                n = n * e;
                n = (n < 0) ? n * -1 : n;
                return String.valueOf(n).substring(0, len);
            }
        };

        var country = makeNumber.apply(2);
        var areaCode = makeNumber.apply(2);
        var number = makeNumber.apply(9);
        return country + areaCode + number;
    }

    public static String generateCPF(){
        return generateCPF(false);
    }
    public static String generateCPF(boolean maskInclude) {
        Random rand = new Random();
        int[] cpf = new int[11];

        // Gera os 9 primeiros dígitos
        for (int i = 0; i < 9; i++) {
            cpf[i] = rand.nextInt(10);
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += cpf[i] * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        cpf[9] = (digito1 >= 10) ? 0 : digito1;

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += cpf[i] * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        cpf[10] = (digito2 >= 10) ? 0 : digito2;

        // Converte para String
        StringBuilder cpfString = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            cpfString.append(cpf[i]);
            if(maskInclude){
                if (i == 2 || i == 5)
                    cpfString.append('.');
                else if (i == 8)
                    cpfString.append('-');
            }
        }

        return cpfString.toString();
    }
    public static String generateCNPJ() {
        return generateCNPJ(false);

    }
    public static String generateIE() {
        Random rand = new Random();
        StringBuilder cnh = new StringBuilder();

        // Gera 9 números aleatórios
        for (int i = 0; i < 9; i++) {
            cnh.append(rand.nextInt(10));
        }

        // Gera 2 números aleatórios para os dígitos verificadores
        for (int i = 0; i < 2; i++) {
            cnh.append(rand.nextInt(10));
        }

        return cnh.toString();
    }
    public static String generateCNPJ(boolean maskInclude) {
        Random rand = new Random();
        int[] cnpj = new int[14];

        // Gera os 8 primeiros dígitos (não podem ser todos zero)
        for (int i = 0; i < 8; i++) {
            cnpj[i] = rand.nextInt(9);
        }
        while (cnpj[0] == 0) {
            cnpj[0] = rand.nextInt(9);
        }

        // Gera os 4 dígitos do meio
        for (int i = 8; i < 12; i++) {
            cnpj[i] = rand.nextInt(10);
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        int multiplicador = 5;
        for (int i = 0; i < 12; i++) {
            soma += cnpj[i] * multiplicador;
            multiplicador = (multiplicador == 2) ? 9 : multiplicador - 1;
        }
        int digito1 = 11 - (soma % 11);
        cnpj[12] = (digito1 >= 10) ? 0 : digito1;

        // Calcula o segundo dígito verificador
        soma = 0;
        multiplicador = 6;
        for (int i = 0; i < 13; i++) {
            soma += cnpj[i] * multiplicador;
            multiplicador = (multiplicador == 2) ? 9 : multiplicador - 1;
        }
        int digito2 = 11 - (soma % 11);
        cnpj[13] = (digito2 >= 10) ? 0 : digito2;

        // Converte para String
        StringBuilder cnpjString = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            cnpjString.append(cnpj[i]);
            if(maskInclude){
                if (i == 1 || i == 4 || i == 7)
                    cnpjString.append('.');
                else if (i == 11)
                    cnpjString.append('/');
                else if (i == 13)
                    cnpjString.append('-');
            }
        }

        return cnpjString.toString();
    }
    public static String generateCNH() {
        Random rand = new Random();
        StringBuilder cnh = new StringBuilder();

        // Gera 9 números aleatórios
        for (int i = 0; i < 9; i++) {
            cnh.append(rand.nextInt(10));
        }

        // Gera 2 números aleatórios para os dígitos verificadores
        for (int i = 0; i < 2; i++) {
            cnh.append(rand.nextInt(10));
        }

        return cnh.toString();
    }
    public static String generateRG() {
        return generateRG(false);
    }
    public static String generateRG(boolean maskInclude) {

        Random numAleatorio = new Random();
        //numeros gerados
        int n1 = numAleatorio.nextInt(10);
        int n2 = numAleatorio.nextInt(10);
        int n3 = numAleatorio.nextInt(10);
        int n4 = numAleatorio.nextInt(10);
        int n5 = numAleatorio.nextInt(10);
        int n6 = numAleatorio.nextInt(10);
        int n7 = numAleatorio.nextInt(10);
        int n8 = numAleatorio.nextInt(10);
        int n9 = numAleatorio.nextInt(10);

        //union os numeros
        var numeroGerado = String.valueOf(n1) + String.valueOf(n2) + String.valueOf(n3)  + String.valueOf(n4) +
                String.valueOf(n5) + String.valueOf(n6) + String.valueOf(n7) +String.valueOf(n8)  +
                String.valueOf(n9);

        if (maskInclude)
            numeroGerado = "" + n1 + n2 + "." + n3 + n4 + n5 + "." + n6 + n7 + n8 + "-" + n9;
        else
            numeroGerado = "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9;

        return numeroGerado;
    }
    public static String generateEMAIL() {
        Random rand = new Random();
        String nomeAleatorio = EMAIL_NAMES[rand.nextInt(EMAIL_NAMES.length)];
        String dominioAleatorio = EMAIL_DOMAINS[rand.nextInt(EMAIL_DOMAINS.length)];

        return nomeAleatorio + "@" + dominioAleatorio;
    }

    private static Person.PersonBuilder generatePersonBuilder() {
        Random rand = new Random();
        rand.setSeed(LocalDate.now().toEpochDay());
        return Person
                .builder()
                .id(UUID.randomUUID())
                .idNumber(rand.nextInt())
                .rev(UUID.randomUUID())
                .dt(LocalDateTime.now())
                .dtBirt(LocalDate.now())
                .name(generatePersonName())
                .email(generateEMAIL())
                .phoneNumber(generatePhoneNumber());

    }

    public static String generateCHASSIS() {
        Random rand = new Random();
        StringBuilder chassis = new StringBuilder();
        // Gera um número aleatório de 17 dígitos
        for (int i = 0; i < 17; i++)
            chassis.append(rand.nextInt(10));

        return chassis.toString();
    }
    public static String generatePersonName() {
        Random rand = new Random();
        String nomeAleatorio = PERSON_NAMES[rand.nextInt(PERSON_NAMES.length)];
        String sobrenomeAleatorio = PERSON_LAST_NAME[rand.nextInt(PERSON_LAST_NAME.length)];
        return nomeAleatorio + " " + sobrenomeAleatorio;
    }
    public enum DataType {
        CUSTOM, CPF, CNPJ, CNH, CHASSIS, RG, IE, eMail, PhoneNumber, Names, PersonPJ, PersonPF, PersonPFAndPJ
    }

    public static Person generatePersonPF() {
        return generatePersonBuilder()
                .type(Person.Type.PF)
                .ie("")
                .rg(generateRG(false))
                .cpf(generateCPF(false))
                .cnh(generateCNH())
                .build();
    }

    public static Person generatePersonPJ() {
        return generatePersonBuilder()
                .type(Person.Type.PJ)
                .ie(generateRG(false))
                .cnpj(generateCNPJ(false))
                .cnh("")
                .build();
    }

    @Data
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Person implements Serializable {
        private UUID id;
        private int idNumber;
        private LocalDateTime dt;
        private UUID rev;
        private String name;
        private LocalDate dtBirt;
        private Type type;
        private String cpf;
        private String cnh;
        private String rg;
        private String cnpj;
        private String ie;
        private String email;
        private String phoneNumber;

        public String getPhoneNumber(){
            return phoneNumber==null?"":phoneNumber.trim();
        };

        public String getPhoneCountry(){
            var number=getPhoneNumber();
            return number.length()>=2?number.substring(0,2):"";
        };
        public String getPhoneAreaCode(){
            var number=getPhoneNumber();
            return number.length()>=4?number.substring(2,4):"";
        }
        public String getPhoneSingleNumber(){
            var number=getPhoneNumber();
            return number.length()>=4?number.substring(5):"";
        };

        public String getPhoneShortNumber(){
            return this.getPhoneAreaCode()+this.getPhoneSingleNumber();
        };


        public enum Type{
            PF,PJ
        }
    }
}
