package jpabasic.inspacebe.dto.item;

import jpabasic.inspacebe.entity.CType;

public class ConfirmItemRequest {

    private String uuid;
    private String ctype;
    private Integer uid;
    private Integer spaceId;

    public ConfirmItemRequest(String uuid, String ctype, Integer uid, Integer spaceId) {
        this.uuid = uuid;
        this.ctype = ctype;
        this.uid = uid;
        this.spaceId = spaceId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCType() {
        return ctype;
    }

    public void setCType(CType ctype) {
        this.ctype = String.valueOf(ctype);
    }
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }
}
