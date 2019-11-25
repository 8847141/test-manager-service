import React, { Component } from 'react';
import { Select } from 'choerodon-ui';
import { injectIntl } from 'react-intl';
import { observer } from 'mobx-react';
import _ from 'lodash';
import { SelectFocusLoad, SelectVersion } from '../../../../../../components';
import { getAllEnvs } from '../../../../../../api/AutoTestApi';
import './SelectVariable.less';

const { Option } = Select;
@injectIntl
@observer
class SelectVariable extends Component {
  /**
   *选择目标版本
   *
   * @param {*} versionId
   * @memberof CreateAutoTest
   */
  handleVersionSelect = (versionId, other) => {
    const { children: versionName } = other.props;
    const { createAutoTestStore } = this.props;
    createAutoTestStore.setVersion({ versionId, versionName });
  }

  handleSelectEnv = (envId, other) => {
    const { createAutoTestStore } = this.props;
    const { envList } = createAutoTestStore;
    createAutoTestStore.setEnv(_.find(envList, { id: envId }));
  }

  loadEnvs = () => {
    const { createAutoTestStore } = this.props;
    getAllEnvs().then((res) => {
      createAutoTestStore.setEnvList(res);
    });
  }

  /**
   * 点击选择数据
   * @param record
   */
  handleSelectApp = (id) => {
    const { createAutoTestStore } = this.props;
    const { appList } = createAutoTestStore;
    createAutoTestStore.setApp(_.find(appList, { id }));
    createAutoTestStore.setAppVersion({});
  };

  handleSelectAppVersion = (id) => {
    const { createAutoTestStore } = this.props;
    const { appVersionList } = createAutoTestStore;
    createAutoTestStore.setAppVersion(_.find(appVersionList, { id }));
  }

  render() {
    const { intl, createAutoTestStore } = this.props;
    const { formatMessage } = intl;
    const {
      app, appVersion, env, version, envList,
    } = createAutoTestStore;
    return (
      <div className="deployApp-app">
        {/* 选择应用 */}
        <SelectFocusLoad
          type="app"
          label="应用"
          style={{ width: 512, display: 'block' }}
          onChange={this.handleSelectApp}
          value={app.id}
          saveList={(list) => { createAutoTestStore.setAppList(list); }}
        />
        <SelectFocusLoad
          disabled={!app.id}
          type="appVersion"
          label="应用版本"
          style={{ width: 512, display: 'block', marginTop: 20 }}
          appId={app.id}
          onChange={this.handleSelectAppVersion}
          value={appVersion.id}
          saveList={(list) => { createAutoTestStore.setAppVersionList(list); }}
        />
        {/* 选择目标版本 */}
        {/* <section className="deployApp-section">
          <SelectVersion
            value={version.versionId}
            style={{ width: 512 }}
            onChange={this.handleVersionSelect}
          />
        </section> */}
        {/* 选择环境 */}
        <section className="deployApp-section">
          <Select
            value={env.id}
            label={formatMessage({ id: 'autoteststep_one_environment' })}
            onSelect={this.handleSelectEnv}
            style={{ width: 512 }}
            optionFilterProp="children"
            filterOption={(input, option) => option.props.children[1]
              .toLowerCase().indexOf(input.toLowerCase()) >= 0}
            filter
            onFocus={this.loadEnvs}
          >
            {envList.map(v => (
              <Option value={v.id} key={v.id} disabled={v.connect}>
                {!v.connect ? <span className="c7ntest-ist-status_on" /> : <span className="c7ntest-ist-status_off" />}
                {v.name}
              </Option>
            ))}
          </Select>
        </section>
      </div>
    );
  }
}


export default SelectVariable;
