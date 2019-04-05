package com.github.daggerok.mvc;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
public class GenericFilterImpl extends GenericFilter {

  @Override
  @SneakyThrows
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
    log.info("GenericFilter started!");
    var resp = (HttpServletResponse) res;
    resp.addHeader("X-SERVLET", "Ololo trololo!");
    chain.doFilter(req, res);
    log.info("On exiting from GenericFilter...");
  }
}
