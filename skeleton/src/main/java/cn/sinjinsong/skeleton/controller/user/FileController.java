package cn.sinjinsong.skeleton.controller.user;

import cn.sinjinsong.common.enumeration.FileSource;
import cn.sinjinsong.common.enumeration.FileType;
import cn.sinjinsong.common.exception.file.FileTypeNotSuppertedException;
import cn.sinjinsong.common.util.FileUtil;
import cn.sinjinsong.skeleton.domain.entity.user.UserDO;
import cn.sinjinsong.skeleton.service.user.UserService;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SinjinSong on 2017/4/29.
 */
@CrossOrigin
@RestController
@RequestMapping("/files")
@Api(value = "files", description = "文件上传与下载")
public class FileController {
    @Autowired
    private UserService service;
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "users", method = RequestMethod.POST)
    @ApiOperation(value = "用户文件上传", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "文件格式不支持"),
            @ApiResponse(code = 400, message = "文件传输失败")
    })
    public void uploadUserFile(@RequestParam("type") @ApiParam(value = "文件类型，仅支持image,file,audio,video", required = true) String fileType, @RequestParam("file") @ApiParam(value = "文件", required = true) MultipartFile file, HttpServletRequest request) {
        FileType type = Enum.valueOf(FileType.class, StringUtils.upperCase(fileType));
        if (type == null) {
            throw new FileTypeNotSuppertedException(fileType);
        }
        String relativePath = FileUtil.upload(FileSource.USER, type, file, request.getServletContext());
        //如果是上传用户的头像图片，那么将图片的相对路径保存数据库的用户记录中
        if (type == FileType.IMAGE) {
            UserDO user = new UserDO();
            user.setId(Long.valueOf((String) request.getAttribute("id")));
            user.setAvatar(relativePath);
            service.update(user);
        }
    }

    @RequestMapping(value = "public", method = RequestMethod.POST)
    @ApiOperation(value = "公共文件上传", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "文件格式不支持"),
            @ApiResponse(code = 400, message = "文件传输失败")
    })
    public void uploadPublicFile(@RequestParam("type") @ApiParam(value = "文件类型，仅支持image,file,audio,video", required = true) String fileType, @RequestParam("file") @ApiParam(value = "文件", required = true) MultipartFile file, HttpServletRequest request) {
        FileType type = Enum.valueOf(FileType.class, StringUtils.upperCase(fileType));
        if (type == null) {
            throw new FileTypeNotSuppertedException(fileType);
        }
        String relativePath = FileUtil.upload(FileSource.PUBLIC, type, file, request.getServletContext());
        //TODO process public files 
    }
}
