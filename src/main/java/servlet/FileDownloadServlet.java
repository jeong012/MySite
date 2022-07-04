package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/fileDownload")
public class FileDownloadServlet extends HttpServlet {
 
    private static final String CHARSET = "utf-8";
    private static final String ATTACHES_DIR = "/Users/isujeong/javaStudy/workspace/mysite/src/main/webapp/WEB-INF/fileUpload";

    @Override
    protected void doPost(HttpServletRequest request,  HttpServletResponse response)
            throws ServletException, IOException {
 
    	request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding(CHARSET);
        
        String fileName = request.getParameter("fileName");
        File file = new File(ATTACHES_DIR + "/" + fileName);
        
	    OutputStream out = response.getOutputStream();
	    response.setHeader("Cache-Control", "no-cache");
	    response.setHeader("Content-disposition", "attachment; fileName=" + fileName);
	    
	    FileInputStream in = new FileInputStream(file);
	    byte[] buffer = new byte[1024 * 8];
	    while(true) {
	    	int count = in.read(buffer);
	    	if(count == -1) {
	    		break;
	    	}
	    	out.write(buffer, 0, count);
	    }
	    
	    in.close();
	    out.close();
	    
    }
}
