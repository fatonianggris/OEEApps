package com.project.oee;

/**
 * Created by Ravi on 13/05/15.
 */
public class Record {
    public String No;
    public String IDCal;
    public String IDFac;
    public String Machine;
    public String OTime;
    public String TTime;
    public String PAmount;
    public String ICTime;
    public String GCount;
    public String TCount;
    public String Availability;
    public String Performance;
    public String ROQuality;
    public String OEE;
    public String dtime;
    public String tdtime;
    public String pdtime;
    public String ltime;
    public String tdelay;
    public String whours;

    public Record(String idcal, String idf, String mch, String ot, String tt, String pa, String ict, String gc,
                  String tc, String avail, String per, String roq, String oee, String dtime,
                  String tdtime, String pdtime, String ltime, String tdelay, String whours) {
        this.IDCal = idcal;
        this.IDFac = idf;
        this.Machine = mch;
        this.OTime = ot;
        this.TTime = tt;
        this.PAmount = pa;
        this.ICTime = ict;
        this.GCount = gc;
        this.TCount = tc;
        this.Availability = avail;
        this.Performance = per;
        this.ROQuality = roq;
        this.OEE = oee;
        this.dtime = dtime;
        this.tdtime = tdtime;
        this.pdtime = pdtime;
        this.ltime = ltime;
        this.tdelay = tdelay;
        this.whours = whours;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) { this.No = no;}

    public String getIDCal() {
        return IDCal;
    }

    public void setIDCal(String idc) {
        this.IDCal = IDCal;
    }

    public String getIDFac() {
        return IDFac;
    }

    public void setIDFac(String idf) {
        this.IDFac = IDFac;
    }

    public String getMachine() {
        return Machine;
    }

    public void setMachine(String mch) {
        this.Machine = mch;
    }

    public String getOTime() {
        return OTime;
    }

    public void setOTime(String ot) {
        this.OTime = ot;
    }

    public String getTTime() {
        return TTime;
    }

    public void setTTime(String tt) {
        this.TTime = tt;
    }

    public String getPAmount() {
        return PAmount;
    }

    public void setPAmount(String pa) {
        this.PAmount = pa;
    }

    public String getICTime() {
        return ICTime;
    }

    public void setICTime(String ict) {
        this.ICTime = ict;
    }

    public String getGCount() {
        return GCount;
    }

    public void setGCount(String gc) {
        this.GCount = gc;
    }

    public String getTCount() {
        return TCount;
    }

    public void setTCount(String tc) {
        this.TCount = tc;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String ava) {
        this.Availability = ava;
    }

    public String getPerformance() {
        return Performance;
    }

    public void setPerformance(String per) {
        this.Performance = per;
    }

    public String getROQuality() {
        return ROQuality;
    }

    public void setROQuality(String roq) {
        this.ROQuality = roq;
    }

    public String getOEE() {
        return OEE;
    }

    public void setOEE(String oee) {
        this.OEE = oee;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dt) {
        this.dtime = dt;
    }

    public String getTdtime() {
        return tdtime;
    }

    public void setTdtime(String td) {
        this.tdtime = td;
    }

    public String getPdtime() {
        return pdtime;
    }

    public void setPdtime(String pdtime) {
        this.pdtime = pdtime;
    }

    public String getLtime() {
        return ltime;
    }

    public void setLtime(String ltime) {
        this.ltime = ltime;
    }

    public String getTdelay() {
        return tdelay;
    }

    public void setTdelay(String tdelay) {
        this.tdelay = tdelay;
    }

    public String getWhours() {
        return whours;
    }

    public void setWhours(String whours) {
        this.whours = whours;
    }
}
