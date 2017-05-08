package cn.sinjinsong.common.util;

import cn.sinjinsong.common.enumeration.FileSource;
import cn.sinjinsong.common.enumeration.FileType;
import cn.sinjinsong.common.exception.file.FileDownloadException;
import cn.sinjinsong.common.exception.file.FileNotFoundException;
import cn.sinjinsong.common.exception.file.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

public final class FileUtil {
    private FileUtil() {
    }

    public static String upload(FileSource fileSource, FileType fileType, MultipartFile file, ServletContext servletContext) {
        if (file.getSize() <= 0) {
            throw new FileUploadException(file.getOriginalFilename());
        }
        String root = servletContext.getRealPath("/WEB-INF/uploads/" + StringUtils.lowerCase(fileSource.toString()) + "/" + StringUtils.lowerCase(fileType.toString()) + "/");
        LocalDate today = LocalDate.now();
        File dir = new File(root, "/" + today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest;
        try {
            dest = new File(dir, String.valueOf(System.currentTimeMillis()) + "_" + file.getOriginalFilename());
            file.transferTo(dest);
        } catch (IOException e) {
            throw new FileUploadException(file.getOriginalFilename());
        }
        System.out.println(dest.getAbsolutePath());
        return dest.getAbsolutePath().substring(dest.getAbsolutePath().indexOf("\\WEB-INF"));
    }


    public static void download(String relativePath, ServletContext servletContext, HttpServletResponse response) {
        File file = new File(servletContext.getRealPath(relativePath));
        String fileName = file.getName();
        if (!file.exists()) {
            System.out.println("文件不存在");
            throw new FileNotFoundException(fileName);
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            IOUtils.copy(in, response.getOutputStream());//将字节数据从流1拷贝到流2
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件传输失败");
            throw new FileDownloadException(file.getName());
        }
    }


}
