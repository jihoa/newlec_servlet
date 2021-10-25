/**
 * 
 */
package com.newlecture.web.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

@MultipartConfig(
	fileSizeThreshold=1024*1024,
	maxFileSize=1024*1024*50,
	maxRequestSize=1024*1024*50*5
)
@WebServlet("/admin/notice/reg")
public class RegController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request
		.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp")
		.forward(request, response);
	
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		
		System.out.println(title);
		
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");
		
		
		Collection<Part> parts = request.getParts();
		StringBuilder builder = new StringBuilder();
		
		
		for(Part p : parts) {
			
			if(!p.getName().equals("file")) {
				continue;
			}
		
			//Part filePart = request.getPart("file");
			Part filePart = p;
			String fileName = filePart.getSubmittedFileName();
			builder.append(fileName);
			builder.append(",");
			
			
			//인풋스트림을 통해 쉽게 얻어 저장
			InputStream fis = filePart.getInputStream();
			
			
			
			//파일 저장 제공 API 는 절대경로만 쓸수있다.
			
			String realPath = request.getServletContext().getRealPath("/member/upload");
			System.out.println(realPath);
			
			
			File path = new File(realPath);
			if(!path.exists())
				path.mkdirs();
			
			String filePath = realPath + File.separator + fileName;
			System.out.println(filePath);
			FileOutputStream fos = new FileOutputStream(filePath);
			
			
			byte[] buf = new byte[1024];
			int size = 0;
			while( (size= fis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			
			fos.close();
			fis.close();

		}
		
		builder.delete(builder.length()-1, builder.length());
		
		
		
		
		
		
		boolean pub = false;
		
		if(isOpen != null)
			pub = true;
		
		Notice notice = new Notice();
		
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterId("newlec");
		notice.setFiles(builder.toString());
		
		NoticeService service = new NoticeService();
		int result = service.insertNotice(notice);
		
		response.sendRedirect("list");
		
	
	
	}
	
}
