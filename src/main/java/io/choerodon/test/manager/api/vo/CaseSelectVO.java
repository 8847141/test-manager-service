package io.choerodon.test.manager.api.vo;

import io.choerodon.test.manager.infra.constant.EncryptKeyConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;

import java.util.List;

/**
 * @author zhaotianxin
 * @since 2019/11/28
 */
public class CaseSelectVO {

    private Boolean custom;

    @Encrypt(/**EncryptKeyConstants.TEST_CASE**/)
    private List<Long> selected;

    @Encrypt(/**EncryptKeyConstants.TEST_CASE**/)
    private List<Long> unSelected;

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public List<Long> getSelected() {
        return selected;
    }

    public void setSelected(List<Long> selected) {
        this.selected = selected;
    }

    public List<Long> getUnSelected() {
        return unSelected;
    }

    public void setUnSelected(List<Long> unSelected) {
        this.unSelected = unSelected;
    }
}
