import React from 'react';
import { Select } from 'choerodon-ui';
import { find } from 'lodash';
import User from '../User';
import { getUsers, getUser } from '../../api/IamApi';
import { getProjectVersion } from '../../api/agileApi';
import { getApps, getAppVersions } from '../../api/AutoTestApi';

const { Option } = Select;

export default {
  user: {
    request: ({ filter, page }) => new Promise(resolve => getUsers(filter, undefined, page).then((UserData) => { resolve({ ...UserData, list: UserData.content.filter(user => user.enabled) }); })),
    render: user => (
      <Option key={user.id} value={user.id}>
        <div style={{ display: 'inline-flex', alignItems: 'center', padding: 2 }}>
          <User
            user={user}
          />
        </div>
      </Option>
    ),
    avoidShowError: (props, List) => new Promise((resolve) => {
      const { value } = props;
      const extraList = [];
      const values = value instanceof Array ? value : [value];
      const requestQue = [];
      values.forEach((a) => {
        if (a && !find(List, { id: a })) {
          requestQue.push(getUser(a));
        }
      });
      Promise.all(requestQue).then((users) => {
        users.forEach((res) => {
          if (res.list && res.list.length > 0) {
            extraList.push(res.list[0]);
          }
        });
        resolve(extraList);
      }).catch((err) => {
        resolve(extraList);
      });
    }),
  },
  version: {
    request: getProjectVersion,
    render: (version, { optionDisabled }) => (
      <Option value={version.versionId} key={version.versionId} disabled={optionDisabled && optionDisabled(version)}>
        {version.name}
      </Option>
    ),
  },
  app: {
    request: ({ page, filter }) => getApps({
      page,
      size: 20,
      sort: { field: 'id', order: 'desc' },
      postData: { searchParam: { name: filter }, params: [] },
    }),
    render: app => <Option value={app.id} key={app.id}>{app.name}</Option>,
  },
  appVersion: {
    propArg: 'appId',
    request: ({ page, filter }, appId) => getAppVersions(appId, {
      page,
      size: 20,
      sort: { field: 'id', order: 'desc' },
    },
    // { version: filter }),
    { }),
    render: appVersion => <Option value={appVersion.id} key={appVersion.id}>{appVersion.version}</Option>,
  },
};
