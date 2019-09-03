/* eslint-disable react/jsx-props-no-spreading */
import React from 'react';
import PropTypes from 'prop-types';
import {
  Upload, Button, Icon, Tooltip,
} from 'choerodon-ui';
import { stores } from '@choerodon/master';
import SingleFileUpload from '@choerodon/agile/lib/components/SingleFileUpload';
import { deleteFileAgile } from '../../../../api/FileApi';

import './UploadButtonNow.less';

const { AppState } = stores;
/**
 * hasPermission 校验是否有权限删除
 * 参数类型 Boolean
 * 后续需要开启 则传入校验结果即可
 * 默认无传入校验结果时，不进行删除权限控制
 */
class UploadButtonNow extends React.Component {
  // static propTypes = {
  //   onRemove: PropTypes.func,
  //   beforeUpload: PropTypes.func,
  // };

  constructor(props, context) {
    super(props, context);
    this.state = {};
  }

  render() {
    const {
      fileList, updateNow, onRemove, onBeforeUpload, hasPermission = true,
    } = this.props;
    const props = {
      action: '//jsonplaceholder.typicode.com/posts/',
      multiple: true,
      beforeUpload: (file) => {
        if (file.size > 1024 * 1024 * 30) {
          Choerodon.prompt('文件不能超过30M');
          return false;
        } else if (file.name && encodeURI(file.name).length > 210) {
          // check name length, the name in the database will
          // like `file_uuid_encodeURI(file.name)`,
          // uuid's length is 32
          // the total could save is 255
          // so select length of encodeURI(file.name)
          // 255 - 32 - 6 = 217 -> 210

          Choerodon.prompt('文件名过长，建议不超过20个字');
          return false;
        } else {
          const tmp = file;
          tmp.status = 'done';
          if (updateNow) {
            if (fileList.length > 0) {
              updateNow(fileList.slice().concat(file));
            } else {
              updateNow([file]);
            }
          }
        }
        return false;
      },

    };

    const handleRemove = (file) => {
      const index = fileList.indexOf(file);
      const newFileList = fileList.slice();
      if (onRemove) {
        deleteFileAgile(file.uid)
          .then((response) => {
            if (response) {
              newFileList.splice(index, 1);
              onRemove(newFileList.reverse());
              Choerodon.prompt('删除成功');
            }
          })
          .catch(() => {
            Choerodon.prompt('删除失败，请稍后重试');
          });
      }
    };

    return (
      <div className="c7n-agile-uploadButtonNow">
        <Upload
          {...props}
          className="upload-button"
        >
          <Tooltip title="上传附件" getPopupContainer={(triggerNode) => triggerNode.parentNode}>
            <Button style={{ padding: '0 6px' }}>
              <Icon type="file_upload" />
            </Button>
          </Tooltip>
        </Upload>
        <div className="c7n-agile-uploadButtonNow-fileList">
          {
            fileList && fileList.length > 0 && fileList.map((item) => (
              <SingleFileUpload
                key={item.uid}
                url={item.url}
                fileName={item.name}
                onDeleteFile={() => { handleRemove(item); }}
                hasDeletePermission={hasPermission || AppState.userInfo.id === item.userId}
              />
            ))
          }
        </div>
      </div>
    );
  }
}

export default UploadButtonNow;