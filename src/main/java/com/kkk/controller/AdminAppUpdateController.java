package com.kkk.controller;

import com.kkk.annotation.GlobalInterceptor;
import com.kkk.entity.po.AppUpdate;
import com.kkk.entity.query.AppUpdateQuery;
import com.kkk.entity.vo.ResponseVO;
import com.kkk.service.AppUpdateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * app发布 Controller
 */
@RestController("adminAppUpdateController")
@RequestMapping("/admin")
@Validated
public class AdminAppUpdateController extends ABaseController {

    @Resource
    private AppUpdateService appUpdateService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadUpdateList")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO loadUpdateList(AppUpdateQuery query) {
        query.setOrderBy("id desc");
        return getSuccessResponseVO(appUpdateService.findListByPage(query));
    }

    /**
     * 保存发布
     * @param id
     * @param version
     * @param updateDesc
     * @param fileType
     * @param outerLink
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("/saveUpdate")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO saveUpdate(Integer id,
                                 @NotEmpty String version,
                                 @NotEmpty String updateDesc,
                                 @NotNull Integer fileType,
                                 String outerLink,
                                 MultipartFile file) throws IOException {
        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setId(id);
        appUpdate.setVersion(version);
        appUpdate.setUpdateDesc(updateDesc);
        appUpdate.setFileType(fileType);
        appUpdate.setOuterLink(outerLink);
        appUpdateService.saveUpdate(appUpdate, file);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delUpdate")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO delUpdate(@NotNull Integer id) {
        appUpdateService.deleteAppUpdateById(id);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/postUpdate")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO postUpdate(@NotNull Integer id, @NotNull Integer status, String grayscaleUid) {
        appUpdateService.postUpdate(id, status, grayscaleUid);
        return getSuccessResponseVO(null);
    }
}