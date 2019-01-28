package org.thradex.util;

public class RhEnum {

    public enum TypeCharge {

        WORKED_TIME("WORKED TIME"),
        DISCOUNT_TIME("DISCOUNT TIME"),
        COMPENSATION_TIME("COMPENSATION TIME"),
        DECOMPENSATION_TIME("DECOMPENSATION TIME"),
        EXTRA_TIME("EXTRA TIME"),
        VACATION_TIME("VACATION TIME");

        private final String toString;

        TypeCharge(String toString) {
            this.toString =toString;
        }

        public String toString(){
            return toString;
        }
    }
    public enum TypeShift{
        SCALE("SCALE"),
        SHCEDULE_JOB("SHCEDULE JOB"),
        PERMISSION(""),
        EXTRA_HOUR(""),
        LATE(""),
        ABSENT(""),
        ADITIONAL_TIME(""),
        LEAVE_DUTY(""),
        LICENSE(""),
        WORK_ABROAD(""),
        ALERT(""),
        SHIFT_LICENSE(""),
        SHIFT_WORK_ABROAD(""),
        ABSENT_ALL_DAY("ABSENT ALL DAY"),
        COMMISSION("");

        private final String toString;

        TypeShift(String toString) {
            this.toString =toString;
        }

        public String toString(){
            return toString;
        }
    }
}
