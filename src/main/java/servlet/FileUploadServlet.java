package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 
 
@WebServlet("/fileUpload")
public class FileUploadServlet extends HttpServlet {
 
 
    private static final String CHARSET = "utf-8";
    //private static final String ATTACHES_DIR = "D:/javaStudy/mysite/src/main/webapp/WEB-INF/fileUpload";
    private static final String ATTACHES_DIR = "/Users/isujeong/javaStudy/workspace/mysite/src/main/webapp/WEB-INF/fileUpload";
    private static final int LIMIT_SIZE_BYTES = 1024 * 1024;
    
 
    @Override
    protected void doPost(HttpServletRequest request,  HttpServletResponse response)
            throws ServletException, IOException {
 
 
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding(CHARSET);
        PrintWriter out = response.getWriter();
 
 
        File attachesDir = new File(ATTACHES_DIR);
 
 
        // 업로드된 파일을 저장할 저장소와 관련된 코드
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setRepository(attachesDir);
        fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
        
        // HTTP 요청에 대한 HttpServletRequest 객체로부터 multipart/form-data형식으로
        // 넘어온 HTTP Body 부분을다루기 쉽게 변환(parse)해주는 역할
        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
 
 
        try {
            List<FileItem> items = fileUpload.parseRequest(request);
            for (FileItem item : items) {
            	if (item.isFormField()) {
                    System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString(CHARSET));
                } else {
                    System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(),
                    		item.getName(), item.getSize());
                    if (item.getSize() > 0) {
                        String separator = File.separator;
                        int index =  item.getName().lastIndexOf(separator);
                        String fileName = item.getName().substring(index  + 1);
                        File uploadFile = new File(ATTACHES_DIR +  separator + fileName);
                        item.write(uploadFile);
                    }
                }
            }
 
            out.println("<h1>파일 업로드 완료</h1>");
 
 
        } catch (Exception e) {
            // 파일 업로드 처리 중 오류가 발생하는 경우
            e.printStackTrace();
            out.println("<h1>파일 업로드 중 오류가  발생하였습니다.</h1>");
        }
    }
}
