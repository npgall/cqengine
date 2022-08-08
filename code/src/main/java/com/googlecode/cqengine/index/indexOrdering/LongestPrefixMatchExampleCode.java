package com.googlecode.cqengine.index.indexOrdering;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

public class LongestPrefixMatchExampleCode {
    private String aNumber;
    private Long bValidFrom;
    private Long cValidTo;
    private String dResult;


    public LongestPrefixMatchExampleCode(String aNumber, Long bValidFrom, Long cValidTo, String dResult) {
        this.aNumber = aNumber;
        this.bValidFrom = bValidFrom;
        this.cValidTo = cValidTo;
        this.dResult = dResult;
    }

    public static Attribute<LongestPrefixMatchExampleCode, String> a_NUMBER = new SimpleAttribute<LongestPrefixMatchExampleCode, String>("aNumber") {
        @Override
        public String getValue(LongestPrefixMatchExampleCode o, QueryOptions queryOptions) {
            return o.getANumber();
        }
    };

    public static Attribute<LongestPrefixMatchExampleCode, Long> bVALID_FROM = new SimpleNullableAttribute<LongestPrefixMatchExampleCode, Long>("bValidFrom") {
        @Override
        public Long getValue(LongestPrefixMatchExampleCode o, QueryOptions queryOptions) {
            return o.getBValidFrom();
        }
    };

    public static Attribute<LongestPrefixMatchExampleCode, Long> cVALID_TO = new SimpleNullableAttribute<LongestPrefixMatchExampleCode, Long>("cValidTo") {
        @Override
        public Long getValue(LongestPrefixMatchExampleCode o, QueryOptions queryOptions) {
            return o.getCValidTo();
        }
    };

    public String getDResult() {
        return dResult;
    }

    public void setDResult(String dResult) {
        this.dResult = dResult;
    }

    public Long getBValidFrom() {
        return bValidFrom;
    }

    public void setBValidFrom(Long bValidFrom) {
        this.bValidFrom = bValidFrom;
    }

    public Long getCValidTo() {
        return cValidTo;
    }

    public void setCValidTo(Long cValidTo) {
        this.cValidTo = cValidTo;
    }

    public String getANumber() {
        return aNumber;
    }

    public void setANumber(String aNumber) {
        this.aNumber = aNumber;
    }


    @Override
    public String toString() {
        return "LongestPrefixMatchExampleCode{" +
                "aNumber='" + aNumber + '\'' +
                ", bValidFrom=" + bValidFrom +
                ", cValidTo=" + cValidTo +
                ", dResult='" + dResult + '\'' +
                '}';
    }


}
