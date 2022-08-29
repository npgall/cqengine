package com.googlecode.cqengine.examples.ordering;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

public class LongestPrefixMatchOneColumnExampleCode {
    private String aNumber;
    private Long bValidFrom;
    private Long cValidTo;
    private String dResult;

    public LongestPrefixMatchOneColumnExampleCode(String aNumber, Long bValidFrom, Long cValidTo, String dResult) {
        this.aNumber = aNumber;
        this.bValidFrom = bValidFrom;
        this.cValidTo = cValidTo;
        this.dResult = dResult;
    }

    public static Attribute<LongestPrefixMatchOneColumnExampleCode, String> a_NUMBER = new SimpleAttribute<LongestPrefixMatchOneColumnExampleCode, String>("aNumber") {
        @Override
        public String getValue(LongestPrefixMatchOneColumnExampleCode o, QueryOptions queryOptions) {
            return o.getANumber();
        }
    };

    public static Attribute<LongestPrefixMatchOneColumnExampleCode, Long> bVALID_FROM = new SimpleNullableAttribute<LongestPrefixMatchOneColumnExampleCode, Long>("bValidFrom") {
        @Override
        public Long getValue(LongestPrefixMatchOneColumnExampleCode o, QueryOptions queryOptions) {
            return o.getBValidFrom();
        }
    };

    public static Attribute<LongestPrefixMatchOneColumnExampleCode, Long> cVALID_TO = new SimpleNullableAttribute<LongestPrefixMatchOneColumnExampleCode, Long>("cValidTo") {
        @Override
        public Long getValue(LongestPrefixMatchOneColumnExampleCode o, QueryOptions queryOptions) {
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
        return "LongestPrefixMatchOneColumnExampleCode{" +
                "aNumber='" + aNumber + '\'' +
                ", bValidFrom=" + bValidFrom +
                ", cValidTo=" + cValidTo +
                ", dResult='" + dResult + '\'' +
                '}';
    }
}
