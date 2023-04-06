package com.googlecode.cqengine.persistence.disk.chronicle;

import io.protostuff.*;
import io.protostuff.WireFormat.FieldType;
import io.protostuff.runtime.*;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampDelegate implements Delegate<Timestamp> {

    @Override
    public FieldType getFieldType() {
        return FieldType.SFIXED64;
    }

    @Override
    public Class<?> typeClass() {
        return Timestamp.class;
    }

    @Override
    public Timestamp readFrom(Input input) throws IOException {
        return new Timestamp(input.readSFixed64());
    }

    @Override
    public void writeTo(Output output, int number, Timestamp value, boolean repeated) throws IOException {
        output.writeFixed64(number, value.getTime(), repeated);
    }

    @Override
    public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws
                                                                                              IOException {
        output.writeFixed64(number, input.readSFixed64(), repeated);
    }
}
