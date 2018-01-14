package com.li.product.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * tomcat7之前处理乱码问题，tomcat8之后就不需要了
 * 通用解决 get 和 post乱码过滤器
 * 
 * request 实现HttpServletRequest接口太麻烦，每个方法都要重写，直接继承适配器HttpServletRequestWrapper
 * 
 */

/**
 * tomcat7:
	URIEncoding
	This specifies the character encoding used to decode the URI bytes, after %xx decoding the URL. 
	If not specified, ISO-8859-1 will be used
	这个参数用来设置解码url参数，如果没指定，默认是ISO-8859-1。
	useBodyEncodingForURI
	This specifies if the encoding specified in contentType should be used for URI query parameters,
	instead of using the URIEncoding. This setting is present for compatibility with Tomcat 4.1.x, 
	where the encoding specified in the contentType, or explicitly set using Request.
	setCharacterEncoding method was also used for the parameters from the URL. The default value is false.
	这个参数是说是否使用contentType 里面的encoding来解码url中的参数，而不是使用URIEncoding。
	主要是为了兼容Tomcat 4.1.x。默认值是false。设置了setCharacterEncoding以后实际上也就设置了contentType 。
	tomcat8:
	URIEncoding 
	This specifies the character encoding used to decode the URI bytes, after %xx decoding the URL. 
	If not specified, UTF-8 will be used unless the org.apache.catalina.STRICT_SERVLET_COMPLIANCE 
	system property is set to true in which case ISO-8859-1 will be used.
	这个参数用来设置解码url参数，如果没指定，默认是UTF-8，除非设置了org.apache.catalina.STRICT_SERVLET_COMPLIANCE这个系统参数为true，这个时候会使用ISO-8859-1。
 * 
 * 
 * （1）URIEncoding和useBodyEncodingForURI两个参数互斥。useBodyEncodingForURI主要是为了兼容老版本，尽量用URIEncoding。
        （2）tomcat8之前，URL中参数的默认解码是ISO-8859-1，而tomcat8的默认解码为utf-8。
        （3）在过滤器中使用 request.setCharacterEncoding() 只能用来处理 POST 请求，对 GET 则无效。
        （4）tomcat乱码的终极解决办法：
	tomcat8之前：设置URIEncoding + Post的过滤器 或者 设置useBodyEncodingForURI+Post过滤器。
	tomcat8之后：只要设置一个Post过滤器就可以了，tomcat9跟tomcat8是一样的。
 *
 */
public class EncodingFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 处理请求乱码
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletRequest myRequest = new MyRequest(httpServletRequest);

		request.setCharacterEncoding("utf-8");
		// 处理响应乱码
		response.setContentType("text/html;charset=utf-8");
		//response.setCharacterEncoding("UTF-8");//设置将字符以"UTF-8"编码输出到客户端浏览器
		
		//response.setHeader("content-type", "text/html;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据
		
		//如果客户端post是以UTF-8字符编码将表单数据传输到服务器端的，因此服务器也需要设置以UTF-8字符编码进行接收，要想完成此操作，
		//服务器可以直接使用从ServletRequest接口继承而来的"setCharacterEncoding(charset)"方法进行统一的编码设置
		//request.setCharacterEncoding("UTF-8");
		/**
		 * 对于以get方式传输的数据，request即使设置了以指定的编码接收数据也是无效的，因为在Tomcat5.0中，
		 * 默认情况下使用ISO- 8859-1对URL提交的数据和表单中GET方式提交的数据进行重新编码（解码），
		 * 而不使用该参数对URL提交的数据和表单中GET方式提交的数据进行重新编码（解码）。
		 * 要解决该问题，应该在Tomcat的配置文件的Connector标签中设置useBodyEncodingForURI或者 URIEncoding属性，
		 * 其中useBodyEncodingForURI参数表示是否用request.setCharacterEncoding 参数对URL提交的数据和表单中GET方式提交的数据进行重新编码，
		 * 在默认情况下，该参数为false（Tomcat4.0中该参数默认为true）
		 * 默认的还是使用ISO8859-1这个字符编码来接收数据，客户端以UTF-8的编码传输数据到服务器端，
		 * 而服务器端的request对象使用的是ISO8859-1这个字符编码来接收数据，服务器和客户端沟通的编码不一致因此才会产生中文乱码的。
		 * 解决办法：在接收到数据后，先获取request对象以ISO8859-1字符编码接收到的原始数据的字节数组，
		 * 然后通过字节数组以指定的编码构建字符串，解决乱码问题。
		 * new String(name.getBytes("ISO8859-1"), "UTF-8") ;
		 * 获取request对象以ISO8859-1字符编码接收到的原始数据的字节数组，然后通过字节数组以指定的编码构建字符串，解决乱码问题
		 * 
		 * 两种解决方法哈哈哈
		 */

		chain.doFilter(myRequest, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}

/**
* @Description: Servlet API中提供了一个request对象的Decorator设计模式的默认实现类HttpServletRequestWrapper,
* (HttpServletRequestWrapper类实现了request接口中的所有方法，但这些方法的内部实现都是仅仅调用了一下所包装的的 request对象的对应方法)
* 以避免用户在对request对象进行增强时需要实现request接口中的所有方法。
* 所以当需要增强request对象时，只需要写一个类继承HttpServletRequestWrapper类，然后在重写需要增强的方法即可
*   1.实现与被增强对象相同的接口 
    2、定义一个变量记住被增强对象
    3、定义一个构造函数，接收被增强对象
    4、覆盖需要增强的方法
    5、对于不想增强的方法，直接调用被增强对象（目标对象）的方法
*/ 
// 自定义request对象
class MyRequest extends HttpServletRequestWrapper {

	private HttpServletRequest request;

	//多次调用getParameterMap()编解码多次又搞坏了
	private boolean hasEncode;

	public MyRequest(HttpServletRequest request) {
		super(request);// super必须写
		this.request = request;
	}

	// 对需要增强方法 进行覆盖
	// 会用到复选框之类的，很多参数
	// 对request.getParameterMap()的返回值使用泛型时应该是Map<String,String[]>形式，
	// 因为有时像checkbox这样的组件会有一个name对应对个value的时候，所以该Map中键值对是<String-->String[]>的实现。
	@Override
	public Map getParameterMap() {
		// 先获得请求方式
		String method = request.getMethod();
		if (method.equalsIgnoreCase("post")) {
			// post请求
			try {
				// 处理post乱码
				request.setCharacterEncoding("utf-8");
				return request.getParameterMap();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if (method.equalsIgnoreCase("get")) {
			// get请求
			Map<String, String[]> parameterMap = request.getParameterMap();
			if (!hasEncode) { // 确保get手动编码逻辑只运行一次
				for (String parameterName : parameterMap.keySet()) {
					String[] values = parameterMap.get(parameterName);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							try {
								// 处理get乱码
								values[i] = new String(values[i]
										.getBytes("ISO-8859-1"), "utf-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
					}
				}
				hasEncode = true;
			}
			return parameterMap;
		}

		return super.getParameterMap();
	}

	@Override
	public String getParameter(String name) {
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		if (values == null) {
			return null;
		}
		return values[0]; // 取回参数的第一个值
	}

	@Override
	public String[] getParameterValues(String name) {
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		return values;
	}

}
