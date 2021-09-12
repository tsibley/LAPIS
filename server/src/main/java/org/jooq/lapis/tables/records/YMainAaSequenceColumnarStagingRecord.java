/*
 * This file is generated by jOOQ.
 */
package org.jooq.lapis.tables.records;


import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.lapis.tables.YMainAaSequenceColumnarStaging;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class YMainAaSequenceColumnarStagingRecord extends UpdatableRecordImpl<YMainAaSequenceColumnarStagingRecord> implements Record3<String, Integer, byte[]> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.y_main_aa_sequence_columnar_staging.gene</code>.
     */
    public void setGene(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.y_main_aa_sequence_columnar_staging.gene</code>.
     */
    public String getGene() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.y_main_aa_sequence_columnar_staging.position</code>.
     */
    public void setPosition(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.y_main_aa_sequence_columnar_staging.position</code>.
     */
    public Integer getPosition() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.y_main_aa_sequence_columnar_staging.data_compressed</code>.
     */
    public void setDataCompressed(byte[] value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.y_main_aa_sequence_columnar_staging.data_compressed</code>.
     */
    public byte[] getDataCompressed() {
        return (byte[]) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, Integer, byte[]> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, Integer, byte[]> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return YMainAaSequenceColumnarStaging.Y_MAIN_AA_SEQUENCE_COLUMNAR_STAGING.GENE;
    }

    @Override
    public Field<Integer> field2() {
        return YMainAaSequenceColumnarStaging.Y_MAIN_AA_SEQUENCE_COLUMNAR_STAGING.POSITION;
    }

    @Override
    public Field<byte[]> field3() {
        return YMainAaSequenceColumnarStaging.Y_MAIN_AA_SEQUENCE_COLUMNAR_STAGING.DATA_COMPRESSED;
    }

    @Override
    public String component1() {
        return getGene();
    }

    @Override
    public Integer component2() {
        return getPosition();
    }

    @Override
    public byte[] component3() {
        return getDataCompressed();
    }

    @Override
    public String value1() {
        return getGene();
    }

    @Override
    public Integer value2() {
        return getPosition();
    }

    @Override
    public byte[] value3() {
        return getDataCompressed();
    }

    @Override
    public YMainAaSequenceColumnarStagingRecord value1(String value) {
        setGene(value);
        return this;
    }

    @Override
    public YMainAaSequenceColumnarStagingRecord value2(Integer value) {
        setPosition(value);
        return this;
    }

    @Override
    public YMainAaSequenceColumnarStagingRecord value3(byte[] value) {
        setDataCompressed(value);
        return this;
    }

    @Override
    public YMainAaSequenceColumnarStagingRecord values(String value1, Integer value2, byte[] value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached YMainAaSequenceColumnarStagingRecord
     */
    public YMainAaSequenceColumnarStagingRecord() {
        super(YMainAaSequenceColumnarStaging.Y_MAIN_AA_SEQUENCE_COLUMNAR_STAGING);
    }

    /**
     * Create a detached, initialised YMainAaSequenceColumnarStagingRecord
     */
    public YMainAaSequenceColumnarStagingRecord(String gene, Integer position, byte[] dataCompressed) {
        super(YMainAaSequenceColumnarStaging.Y_MAIN_AA_SEQUENCE_COLUMNAR_STAGING);

        setGene(gene);
        setPosition(position);
        setDataCompressed(dataCompressed);
    }
}