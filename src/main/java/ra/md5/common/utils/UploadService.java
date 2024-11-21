package ra.md5.common.utils;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class UploadService {
    @Autowired
    private Drive drive;
    public String uploadFileToDrive(MultipartFile file) throws IOException {
        java.io.File tempFile = java.io.File.createTempFile("temp",null); // tạo 1 file tạm thời
        file.transferTo(tempFile); // di chuyển file upload tới file tạm thời đấy
        String folderId = "11f8uG-CvAUssefujtcKrYJ-kQVriODcN"; // lấy ra id của folder chứa file trên driver
        File fileMetaData = new File(); // tạo mới file metadata để upload
        fileMetaData.setName(tempFile.getName());  // set tên file upload
        fileMetaData.setParents(Collections.singletonList(folderId)); // set thư mục chứa file
        FileContent mediaFile = new FileContent(file.getContentType(), tempFile); // set kiêu nội dung file (img hay jpeg)
        File uploadedFile = drive.files().create(fileMetaData,mediaFile).setFields("id, webViewLink, webContentLink").execute(); // upload file lên drive
        Permission permission = new Permission(); // tạo quền truy cập
        permission.setType("anyone"); // bất cứ ai
        permission.setRole("reader"); // quyền xem
        drive.permissions().create(uploadedFile.getId(), permission).execute(); // lưu quyền lại
        tempFile.delete(); // xóa file tạm
        return  uploadedFile.getWebViewLink(); // tra về link truy cap
    }
}
