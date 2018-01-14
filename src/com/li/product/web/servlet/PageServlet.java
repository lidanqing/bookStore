package com.li.product.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.li.product.domain.PageBean;
import com.li.product.service.ProductService;

public class PageServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//导航按钮的查询条件
		String category = request.getParameter("category");
		/**
		 * 许多浏览器在Content-Type头中不会指定字符编码方式，那么容器就会使用"ISO-8859-1"方式解析POST数据，
		 * 而此时，为了向开发人员提示字符编码方式未指定，容器将会在getCharacterEncoding返回null.
   		 * 如果客户机没有设置字符编码信息，但是request数据又以和缺省编码方式不同的方式编码，就会发生数据破坏。
   		 * setCharacterEncoding(String enc)方法可以防止这种状况发生，
   		 * 但是必须在解析数据或从request中读取数据之前调用。否则调用该方法不会有任何效果。
		 */
		if(category==null){
			category="";
		}
		//初始化每页显示的记录数
		int pageSize = 4;
		
		int currentPage = 1;//当前页
		String currPage = request.getParameter("currentPage");//从上一页或下一页得到的数据
		if(currPage!=null&&!"".equals(currPage)){//第一次访问资源时，currPage可能是null，从网页直接进servlet可能是null
			currentPage = Integer.parseInt(currPage);
		}
		
		ProductService bs = new ProductService();
		//分页查询，并返回PageBean对象
		PageBean pb = bs.findBooksPage(currentPage,pageSize,category);
		request.setAttribute("pb", pb);
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
