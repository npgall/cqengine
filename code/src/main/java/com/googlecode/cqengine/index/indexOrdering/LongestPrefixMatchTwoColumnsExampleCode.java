package com.googlecode.cqengine.index.indexOrdering;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

public class LongestPrefixMatchTwoColumnsExampleCode {
    private String aNumber;
    private String bNumber;
    private Long bValidFrom;
    private Long cValidTo;
    private String dResult;


    public LongestPrefixMatchTwoColumnsExampleCode(String aNumber, String bNumber, Long bValidFrom, Long cValidTo, String dResult) {
        this.aNumber = aNumber;
        this.bNumber = bNumber;
        this.bValidFrom = bValidFrom;
        this.cValidTo = cValidTo;
        this.dResult = dResult;
    }

    public static Attribute<LongestPrefixMatchTwoColumnsExampleCode, String> a_NUMBER = new SimpleAttribute<LongestPrefixMatchTwoColumnsExampleCode, String>("aNumber") {
        @Override
        public String getValue(LongestPrefixMatchTwoColumnsExampleCode o, QueryOptions queryOptions) {
            return o.getANumber();
        }
    };

    public static Attribute<LongestPrefixMatchTwoColumnsExampleCode, String> b_NUMBER = new SimpleAttribute<LongestPrefixMatchTwoColumnsExampleCode, String>("bNumber") {
        @Override
        public String getValue(LongestPrefixMatchTwoColumnsExampleCode o, QueryOptions queryOptions) {
            return o.getBNumber();
        }
    };

    public static Attribute<LongestPrefixMatchTwoColumnsExampleCode, Long> bVALID_FROM = new SimpleNullableAttribute<LongestPrefixMatchTwoColumnsExampleCode, Long>("bValidFrom") {
        @Override
        public Long getValue(LongestPrefixMatchTwoColumnsExampleCode o, QueryOptions queryOptions) {
            return o.getBValidFrom();
        }
    };

    public static Attribute<LongestPrefixMatchTwoColumnsExampleCode, Long> cVALID_TO = new SimpleNullableAttribute<LongestPrefixMatchTwoColumnsExampleCode, Long>("cValidTo") {
        @Override
        public Long getValue(LongestPrefixMatchTwoColumnsExampleCode o, QueryOptions queryOptions) {
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


    public String getBNumber() {
        return bNumber;
    }

    public void setBNumber(String bNumber) {
        this.bNumber = bNumber;
    }

    @Override
    public String toString() {
        return "LongestPrefixMatchExampleCode{" +
                "aNumber='" + aNumber + '\'' +
                ", bNumber='" + bNumber + '\'' +
                ", bValidFrom=" + bValidFrom +
                ", cValidTo=" + cValidTo +
                ", dResult='" + dResult + '\'' +
                '}';
    }
}
