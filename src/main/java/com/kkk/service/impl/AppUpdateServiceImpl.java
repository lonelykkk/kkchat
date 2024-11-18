package com.kkk.service.impl;

import com.kkk.entity.config.AppConfig;
import com.kkk.entity.constants.Constants;
import com.kkk.entity.enums.AppUpdateFileTypeEnum;
import com.kkk.entity.enums.AppUpdateSatusEnum;
import com.kkk.entity.enums.PageSize;
import com.kkk.entity.enums.ResponseCodeEnum;
import com.kkk.entity.po.AppUpdate;
import com.kkk.entity.query.AppUpdateQuery;
import com.kkk.entity.query.SimplePage;
import com.kkk.entity.vo.PaginationResultVO;
import com.kkk.exception.BusinessException;
import com.kkk.mappers.AppUpdateMapper;
import com.kkk.service.AppUpdateService;
import com.kkk.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * app发布 业务接口实现
 */
@Service("appUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService {


    @Resource
    private AppConfig appConfig;

    @Resource
    private AppUpdateMapper<AppUpdate, AppUpdateQuery> appUpdateMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppUpdate> findListByParam(AppUpdateQuery param) {
        return this.appUpdateMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppUpdateQuery param) {
        return this.appUpdateMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppUpdate> list = this.findListByParam(param);
        PaginationResultVO<AppUpdate> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppUpdate bean) {
        return this.appUpdateMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppUpdate bean, AppUpdateQuery param) {
        StringTools.checkParam(param);
        return this.appUpdateMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppUpdateQuery param) {
        StringTools.checkParam(param);
        return this.appUpdateMapper.deleteByParam(param);
    }

    /**
     * 根据Id获取对象
     */
    @Override
    public AppUpdate getAppUpdateById(Integer id) {
        return this.appUpdateMapper.selectById(id);
    }

    /**
     * 根据Id修改
     */
    @Override
    public Integer updateAppUpdateById(AppUpdate bean, Integer id) {
        return this.appUpdateMapper.updateById(bean, id);
    }

    /**
     * 根据Id删除
     */
    @Override
    public Integer deleteAppUpdateById(Integer id) {
        AppUpdate dbInfo = this.getAppUpdateById(id);
        if (!AppUpdateSatusEnum.INIT.getStatus().equals(dbInfo.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return this.appUpdateMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws IOException {
        AppUpdateFileTypeEnum fileTypeEnum = AppUpdateFileTypeEnum.getByType(appUpdate.getFileType());
        if (null == fileTypeEnum) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (appUpdate.getId() != null) {
            AppUpdate dbInfo = this.getAppUpdateById(appUpdate.getId());
            if (!AppUpdateSatusEnum.INIT.getStatus().equals(dbInfo.getStatus())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }
        AppUpdateQuery updateQuery = new AppUpdateQuery();
        updateQuery.setOrderBy("id desc");
        updateQuery.setSimplePage(new SimplePage(0, 1));
        List<AppUpdate> appUpdateList = appUpdateMapper.selectList(updateQuery);
        if (!appUpdateList.isEmpty()) {
            AppUpdate lastest = appUpdateList.get(0);
            Long dbVersion = Long.parseLong(lastest.getVersion().replace(".", ""));
            Long currentVersion = Long.parseLong(appUpdate.getVersion().replace(".", ""));
            if (appUpdate.getId() == null && currentVersion <= dbVersion) {
                throw new BusinessException("当前版本必须大于历史版本");
            }
            if (appUpdate.getId() != null && currentVersion >= dbVersion && !appUpdate.getId().equals(lastest.getId())) {
                throw new BusinessException("当前版本必须大于历史版本");
            }

            AppUpdate versionDb = appUpdateMapper.selectByVersion(appUpdate.getVersion());
            if (appUpdate.getId() != null && versionDb != null && !versionDb.getId().equals(appUpdate.getId())) {
                throw new BusinessException("版本号已存在");
            }
        }

        if (appUpdate.getId() == null) {
            appUpdate.setCreateTime(new Date());
            appUpdate.setStatus(AppUpdateSatusEnum.INIT.getStatus());
            appUpdateMapper.insert(appUpdate);
        } else {
            appUpdate.setCreateTime(new Date());
            appUpdate.setStatus(AppUpdateSatusEnum.INIT.getStatus());
            appUpdateMapper.updateById(appUpdate, appUpdate.getId());
        }
        if (file != null) {
            File folder = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            file.transferTo(new File(folder.getAbsolutePath() + "/" + appUpdate.getId() + Constants.APP_EXE_SUFFIX));
        }
    }

    @Override
    public void postUpdate(Integer id, Integer status, String grayscaleUid) {
        AppUpdateSatusEnum satusEnum = AppUpdateSatusEnum.getByStatus(status);
        if (status == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (AppUpdateSatusEnum.GRAYSCALE == satusEnum && StringTools.isEmpty(grayscaleUid)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (AppUpdateSatusEnum.GRAYSCALE != satusEnum) {
            grayscaleUid = "";
        }
        AppUpdate update = new AppUpdate();
        update.setStatus(status);
        update.setGrayscaleUid(grayscaleUid);
        appUpdateMapper.updateById(update, id);
    }

    @Override
    public AppUpdate getLatestUpdate(String appVersion, String uid) {
        return appUpdateMapper.selectLatestUpdate(appVersion, uid);
    }
}