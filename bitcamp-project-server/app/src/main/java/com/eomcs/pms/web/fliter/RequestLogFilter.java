package com.eomcs.pms.web.fliter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;



@WebFilter("/*") 
public class RequestLogFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain nextChain)
      throws IOException, ServletException {

    // 파라미터로 받은 request 의 본래 타입으로 형변환 한다.
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    System.out.printf("[%s:%d] %s\n", 
        httpRequest.getRemoteAddr(), httpRequest.getRemotePort(), httpRequest.getServletPath());

    // 이 필터에서 멈출게 아니라면 다음 체인을 실행시켜야 한다.
    // 만약 다음 체인을 실행하지 않으면 이 필터에서 ServerApp으로 리턴하게 된다.
    nextChain.doFilter(request, response);

  }
}








