package com.sysargument.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="sys_argument")
public class SysArgVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="sys_argument_id", updatable = false, insertable = false)
    private Integer sysId;

    @Column(name="sys_argument")
    private String sysArgument;

    @Column(name="sys_argument_value")
    private String sysArgumentValue;

    // 無參數的構造方法
    public SysArgVO() {}

    // getter 和 setter 方法
    public Integer getSysId() {
        return sysId;
    }

    public void setSysId(Integer sysId) {
        this.sysId = sysId;
    }

    public String getSysArgument() {
        return sysArgument;
    }

    public void setSysArgument(String sysArgument) {
        this.sysArgument = sysArgument;
    }

    public String getSysArgumentValue() {
        return sysArgumentValue;
    }

    public void setSysArgumentValue(String sysArgumentValue) {
        this.sysArgumentValue = sysArgumentValue;
    }
}
