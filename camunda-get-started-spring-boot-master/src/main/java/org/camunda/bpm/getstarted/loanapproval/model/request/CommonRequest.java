package org.camunda.bpm.getstarted.loanapproval.model.request;


//import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;

import java.util.ArrayList;
import java.util.List;

public class CommonRequest {
    private String id;
    private List<String> ids = new ArrayList();
    private String code;
    private String name;
    private String type;
    private String language;
    private String status;
    private int size;
    private int active;
    private Object value;
//    private List<ObjectList> objectList;
    private String amount;
    private String currency;
    private boolean override;
    private String groupIt;

    public CommonRequest() {
    }

    public String getGroupIt() {
        return this.groupIt;
    }

    public void setGroupIt(String groupIt) {
        this.groupIt = groupIt;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getActive() {
        return this.active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

//    public List<ObjectList> getObjectList() {
//        return this.objectList;
//    }
//
//    public void setObjectList(List<ObjectList> objectList) {
//        this.objectList = objectList;
//    }

    public boolean isOverride() {
        return this.override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public List<String> getIds() {
        return this.ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
