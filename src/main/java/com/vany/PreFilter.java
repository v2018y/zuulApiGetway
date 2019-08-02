package com.vany;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.vany.config.JwtRequestFilter;
import com.vany.config.JwtTokenUtil;
import com.vany.model.DAOUser;
import com.vany.repositeroy.UserDao;

@Component
public class PreFilter extends ZuulFilter {


	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserDao userDao;
	
	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		 return "pre";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		 return 1;
	}
	
	@Override
	public Object run() {
		 RequestContext ctx = RequestContext.getCurrentContext();
		    ctx.getRequest().getRequestURL();
		    System.out.println("Token "+jwtRequestFilter.requestTokenHeader);
		    String Token=jwtRequestFilter.requestTokenHeader.substring(7);
		    String username = jwtTokenUtil.getUsernameFromToken(Token);
		    DAOUser daoUser=userDao.findByUsername(username);
		    ctx.addZuulRequestHeader("userId", ""+ daoUser.getId());
		  return null;
	    }

}
