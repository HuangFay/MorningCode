package com.sysargument.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_argument")
public class SysArgumentVO implements java.io.Serializable  {
    @Column(name="sys_id", updatable = false, insertable = false)
    private  Integer  SysId;
    @Column(name="sys_argument")
    private  String SysArgument;
    @Column(name="sys_argument_value")
    private  String SysArgumentValue;

    public Integer getSysId() {
        return SysId;
    }
    public void setSysId(Integer sysId) {
        SysId = sysId;
    }
    public String getSysArgument() {
        return SysArgument;
    }
    public void setSysArgument(String sysArgument) {
        SysArgument = sysArgument;
    }
    public String getSysArgumentValue() {
        return SysArgumentValue;
    }
    public void setSysArgumentValue(String sysArgumentValue) {
        SysArgumentValue = sysArgumentValue;
    }

}
