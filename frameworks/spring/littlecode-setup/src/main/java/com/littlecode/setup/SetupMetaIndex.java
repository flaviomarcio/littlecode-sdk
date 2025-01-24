package com.littlecode.setup;

import lombok.*;
import org.springframework.orm.jpa.vendor.Database;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(since = "Descontinuar o uso")
public class SetupMetaIndex {
    public static final String Default = "";
    private Integer sequence;
    private String name;
    private Object type;
    private Object using;
    private List<String> fields;
    private String description;

    public String getName() {
        if (this.name == null || this.name.trim().isEmpty())
            this.name = null;
        return this.name;
    }

    public String getFinalName(String baseIndexName) {
        baseIndexName = (this.getName() != null)
                ? this.getName()
                : (baseIndexName == null || baseIndexName.trim().isEmpty())
                ? null
                : baseIndexName.trim();
        if (baseIndexName == null)
            return null;

        var finalName = new StringBuilder();

        if (Type.Unique.equals(this.type))
            finalName.append("un_");
        else
            finalName.append("ix_");

        if (this.getSequence() != null)
            finalName
                    .append(String.format("%03d_", this.getSequence()));

        return finalName
                .append(baseIndexName)
                .toString()
                .trim();
    }

    public Object getType() {
        return (this.type == null || this.type.toString().trim().isEmpty())
                ? null
                :
                (this.type.equals(Default))
                        ? null
                        : this.type;
    }

    public Object getUsing() {
        return (this.using == null || this.using.toString().trim().isEmpty())
                ? null
                :
                (this.using.equals(Default))
                        ? null
                        : this.using;
    }

    public List<String> getFields() {
        if (this.fields == null)
            this.fields = new ArrayList<>();
        return this.fields;
    }

    @Getter
    public enum Type {
        Default(""), Unique("unique");
        private final String value;

        Type(String value) {
            this.value = (value == null)
                    ? ""
                    : value.trim().toLowerCase();
        }
    }

    @Getter
    public enum Using {
        Default("", new Database[]{Database.DEFAULT}),
        Btree("btree", new Database[]{Database.POSTGRESQL}),
        Hash("hash", new Database[]{Database.POSTGRESQL}),
        GIN("gin", new Database[]{Database.POSTGRESQL}),
        BRIN("brin", new Database[]{Database.POSTGRESQL}),
        GiST("gist", new Database[]{Database.POSTGRESQL}),
        SPGiST("spgist", new Database[]{Database.POSTGRESQL});
        private final String value;
        private final Database[] dataBase;

        Using(String value, Database[] dataBase) {
            this.value = (value == null)
                    ? ""
                    : value.trim().toLowerCase();
            this.dataBase = (dataBase == null)
                    ? new Database[]{Database.DEFAULT}
                    : dataBase;
        }
    }
}
