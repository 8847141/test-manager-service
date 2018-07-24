package io.choerodon.test.manager.infra.repository.impl;

import io.choerodon.test.manager.domain.test.manager.entity.TestStatusE;
import io.choerodon.test.manager.domain.repository.TestStatusRepository;
import io.choerodon.test.manager.infra.dataobject.TestStatusDO;
import io.choerodon.test.manager.infra.mapper.TestStatusMapper;
import io.choerodon.core.convertor.ConvertHelper;
import io.choerodon.core.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by 842767365@qq.com on 6/25/18.
 */
@Component
public class TestStatusRepositoryImpl implements TestStatusRepository {

    @Autowired
    TestStatusMapper testStatusMapper;

	@Override
	public List<TestStatusE> queryAllUnderProject(TestStatusE testStatusE) {
		TestStatusDO testStatusDO = ConvertHelper.convert(testStatusE, TestStatusDO.class);
		return ConvertHelper.convertList(testStatusMapper.queryAllUnderProject(testStatusDO), TestStatusE.class);
    }

	@Override
	public TestStatusE queryOne(Long statusId) {
		return ConvertHelper.convert(testStatusMapper.selectByPrimaryKey(statusId), TestStatusE.class);

	}

	@Override
    public TestStatusE insert(TestStatusE testStatusE) {
        TestStatusDO testStatusDO = ConvertHelper.convert(testStatusE, TestStatusDO.class);
        if (testStatusMapper.insert(testStatusDO) != 1) {
            throw new CommonException("error.test.status.insert");
        }
        return ConvertHelper.convert(testStatusDO, TestStatusE.class);
    }

    @Override
    public void delete(TestStatusE testStatusE) {
        TestStatusDO testStatusDO = ConvertHelper.convert(testStatusE, TestStatusDO.class);
		testStatusMapper.delete(testStatusDO);
    }

    @Override
    public TestStatusE update(TestStatusE testStatusE) {
        TestStatusDO testStatusDO = ConvertHelper.convert(testStatusE, TestStatusDO.class);
        if (testStatusMapper.updateByPrimaryKey(testStatusDO) != 1) {
            throw new CommonException("error.test.status.update");
        }
		return ConvertHelper.convert(testStatusMapper.selectByPrimaryKey(testStatusDO.getStatusId()), TestStatusE.class);
    }

	@Override
	public void validateDeleteCycleCaseAllow(Long statusId) {
		if (testStatusMapper.ifDeleteCycleCaseAllow(statusId) > 0) {
			throw new CommonException("error.delete.status.have.used");

		}
	}

	@Override
	public void validateDeleteCaseStepAllow(Long statusId) {
		Assert.notNull(statusId,"error.validate.delete.allow.parameter.statusId.not.null");
		if (testStatusMapper.ifDeleteCaseStepAllow(statusId) > 0) {
			throw new CommonException("error.delete.status.have.used");
		}
	}

	@Override
	public Long getDefaultStatus(String statusType) {
		Assert.notNull(statusType,"error.getDefault.parameter.type.not.null");
		return testStatusMapper.getDefaultStatus(statusType);
	}
}
